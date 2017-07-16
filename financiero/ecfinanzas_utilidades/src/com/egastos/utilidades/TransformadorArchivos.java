/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;


import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionNotaCredito;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionRetencion;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Delgado
 */
public class TransformadorArchivos {

    /**
     * Método que transforma un archivo de File a un array de bytes
     *
     * @param file objeto de tipo File con la referencia del archivo a
     * transformar
     * @return array de bytes con la información del archivo
     */
    public static byte[] archArrayB(File file) {

        byte[] buffer = new byte[(int) file.length()];
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            if (is.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return buffer;
    }

    /**
     * Método que escribe un archivo de cualquier tipo en un directorio a partir
     * de un array bytes
     *
     * @param array Array de bytes con información del archivo
     * @param pathArchivo Ruta-nombre del archivo a escribir
     * @return true si el archivo se escribió y false si no
     */
    public static boolean archByte(byte[] array, String pathArchivo) {
        boolean encontrado = false;
        BufferedOutputStream bOS = null;
        try {
            FileOutputStream fOS = new FileOutputStream(new File(pathArchivo));
            bOS = new BufferedOutputStream(fOS);
            bOS.write(array);
            encontrado = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bOS.flush();
                bOS.close();
            } catch (IOException ex) {
                Logger.getLogger(TransformadorArchivos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (bOS != null) {
            try {
                bOS.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return encontrado;
    }

    /**
     * Metodo que transforma un archivo XML a un objecto ComprobanteElectronico
     * del core
     *
     * @param pathArchivo direccion del archivo a transformar
     * @return ComprobanteElectronico de Core facturacion
     */
    public static ComprobanteElectronico parseCompr(String pathArchivo) {
        try {
            
            String tCompr = "";

            ImplementacionFactura facComprXML = new ImplementacionFactura();
            facComprXML.ObjetoComprobanteAXML(pathArchivo);
            tCompr = facComprXML.getInformacionTributariaComprobanteElectronico().getCodDoc();
            //factura
            switch (tCompr) {
                case "01":
                    return facComprXML;
                case "04":
                    ImplementacionNotaCredito nCredXML = new ImplementacionNotaCredito();
                    nCredXML.ObjetoComprobanteAXML(pathArchivo);
                    return nCredXML;
                
                case "07":
                    ImplementacionRetencion retXML = new ImplementacionRetencion();
                    retXML.ObjetoComprobanteAXML(pathArchivo);
                    return retXML;
                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error:" + pathArchivo);
            e.printStackTrace();
            return null;
        }
    }
        public static byte[] parseCompr2(String pathArchivo) {
        byte[] res=null;
            try{
            String tCompr = "";

            ImplementacionFactura facComprXML = new ImplementacionFactura();
           res = facComprXML.ObjetoComprobanteAXML2(pathArchivo);
           
        } catch (Exception e) {
            System.out.println("Error:" + pathArchivo);
            e.printStackTrace();
            
        }
        return res;
    }
    public static ComprobanteElectronico byteCompr(byte[] archivoByte, String pathArchivo) {
        ComprobanteElectronico compr = null;
//        File File = new File(pathArchivo);
        String rutaArch = pathArchivo;
        BufferedOutputStream bOS = null;
        
        try {
            FileOutputStream fOS = new FileOutputStream(new File(rutaArch));
            bOS = new BufferedOutputStream(fOS);
            bOS.write(archivoByte);
            bOS.close();
            bOS = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bOS != null) {
            try {
                bOS.close();
                bOS.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
           compr=TransformadorArchivos.parseCompr(rutaArch);
            
            File arch=new File(rutaArch);
            if (arch.delete()) {
            } else {
                System.out.println("archivo temporal no ha sido eliminado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compr;
    }
 public static byte[] byteCompr2(byte[] archivoByte, String pathArchivo) {
        byte[] compr = null;
        String rutaArch = pathArchivo + ".xml";
        BufferedOutputStream bOS = null;
        try {
            FileOutputStream fOS = new FileOutputStream(new File(rutaArch));
            bOS = new BufferedOutputStream(fOS);
            bOS.write(archivoByte);
            bOS.close();
            bOS = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bOS != null) {
            try {
                bOS.close();
                bOS.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
           compr=TransformadorArchivos.parseCompr2(rutaArch);
            
            File arch=new File(rutaArch);
            if (arch.delete()) {
            } else {
                System.out.println("archivo temporal no ha sido eliminado");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compr;
    }

    public static boolean writeToFile(byte[] array, String fileName) {
        boolean success = false;
        BufferedOutputStream bs = null;
        try {
            FileOutputStream fs = new FileOutputStream(new File(fileName));
            bs = new BufferedOutputStream(fs);
            bs.write(array);
            bs.close();
            bs = null;
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bs != null) {
            try {
                bs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}
