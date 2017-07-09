/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.utilidades;

import discheca.utilidades.TransformadorArchivos;
import ec.discheca.configuracion.Valores;
import ec.discheca.modelo.AsignacionComprobanteElectronico;
import ec.discheca.modelo.ComprobanteElectronico;
//import ec.bigdata.utilidades.TransformadorArchivos;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author andres
 */
public class Descarga implements Serializable{
    public HttpServletResponse descargarArchivo(FacesContext faces, byte[] archivoByte, String nombreArchivo) {

        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.setContentType("application/x-download");
        response.setContentLength(archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + nombreArchivo);
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(archivoByte);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public HttpServletResponse visualizarPDF(FacesContext faces, byte[] archivoByte, ComprobanteElectronico comprobante) {

        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + comprobante.getRucEmisorComprobanteElectronico() + " " + comprobante.getClaveAccesoComprobanteElectronico() + ".pdf");
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(archivoByte);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
    
    public byte[] generarZIP(String _nombreArchivoZIP, List<AsignacionComprobanteElectronico> _comprobanteElectronico) {

        byte[] zipBytes = null;
        try {

            BufferedInputStream origin = null;
            RIDE r = new RIDE();
            FileOutputStream dest = new FileOutputStream(Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS+File.separator+_nombreArchivoZIP);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            out.setMethod(ZipOutputStream.DEFLATED);
            String extension = null;
            for (int i = 0; i < _comprobanteElectronico.size(); i++) {

                //guarda bytes del archivo XML de respuesta y PDF RIDE
                byte[] bytes = null;

                bytes = _comprobanteElectronico.get(i).getComprobanteElectronico().getArchivoRespuestaSricomprobanteElectronico();
                
                //El nombre del archivo a comprimirse está con la clave de acceso
                String nombre_archivo = _comprobanteElectronico.get(i).getComprobanteElectronico().getClaveAccesoComprobanteElectronico();

                extension = ".xml";
                insetarZIP(out, bytes, origin, nombre_archivo, extension);

                bytes = r.construirPDFRIDE(_comprobanteElectronico.get(i).getComprobanteElectronico());
                extension = ".pdf";
                insetarZIP(out, bytes, origin, nombre_archivo, extension);

            }
            out.close();
            File file = new File(Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS+File.separator+_nombreArchivoZIP);
            zipBytes = TransformadorArchivos.archArrayB(file);
            if (file.exists()) {
                file.delete();
            } else {
                Logger.getLogger(Descarga.class.getName()).log(Level.SEVERE, "Error al crear el ZIP.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return zipBytes;
        }

    }

    private static void insetarZIP(ZipOutputStream _out, byte[] _bytes, BufferedInputStream _bufferInputStrem, String _nombreArchivo, String _extension) throws IOException {

        byte data[] = new byte[_bytes.length];
        InputStream input_stream = new ByteArrayInputStream(_bytes);
        _bufferInputStrem = new BufferedInputStream(input_stream, _bytes.length);
        boolean bandera_nombre_archivo = false;
        int numero_copia = 0;
        do {
            try {
                if (numero_copia == 0) {
                    _nombreArchivo = _nombreArchivo + _extension;
                } else {
                    _nombreArchivo = _nombreArchivo + "(" + numero_copia + ")" + _extension;
                }
                ZipEntry entry = new ZipEntry(_nombreArchivo);

                _out.putNextEntry(entry);
                bandera_nombre_archivo = true;
            } catch (IOException e) {
                numero_copia++;
            }
        } while (bandera_nombre_archivo == false);
        int count;

        while ((count = _bufferInputStrem.read(data, 0, _bytes.length)) != -1) {
            _out.write(data, 0, count);
        }
        _bufferInputStrem.close();

    }
}
