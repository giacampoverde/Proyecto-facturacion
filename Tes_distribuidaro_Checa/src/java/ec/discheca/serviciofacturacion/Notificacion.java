/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.serviciofacturacion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.discheca.firma.sri.produccion.RespuestaSRI;
import discheca.utilidades.TransformadorArchivos;
import discheca.utilidades.Utilidades;
import ec.discheca.configuracion.Valores;
import ec.discheca.correo.Correo;
import ec.discheca.utilidades.RIDE;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;

public class Notificacion {

    public boolean enviarNotificacion(RespuestaSRI _respuestaSRI, String _direccionArchivoRespuesta, String _correoEmisor) {
        boolean enviado = false;
        List<String> correos = new ArrayList();
        if (_correoEmisor != null && !_correoEmisor.trim().equals("")) {
            correos.add(_correoEmisor);
        }
        RIDE ride = new RIDE();
        ComprobanteElectronico c = null;
        try {
            c = TransformadorArchivos.byteCompr(_respuestaSRI.getComprobante().getBytes("UTF-8"), Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS + RandomStringUtils.randomAlphanumeric(10));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Notificacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<InformacionAdicional> informacion_adicional = c.getInformacionAdicional();
        if (informacion_adicional != null && !informacion_adicional.isEmpty()) {
            for (InformacionAdicional ia : informacion_adicional) {
                if (ia.getNombre().toUpperCase().startsWith("CORREO") && !ia.getValor().equals("N/A")) {
                    correos.add(ia.getValor());
                }
            }
        }
       
                
        byte[] pdf_ride = ride.construirPDFRIDE(_respuestaSRI, c);
        String nombre_ruta_pdf_ride = Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS + File.separator + c.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + ".pdf";
        boolean pdf_creado = TransformadorArchivos.archByte(pdf_ride, nombre_ruta_pdf_ride);
        if (pdf_creado) {
            String nombre = Utilidades.obtenerRazonSocialReceptor(c);
            String clave_acceso = c.getInformacionTributariaComprobanteElectronico().getClaveAcceso();
            String ruc_receptor = Utilidades.obtenerRucReceptor(c);
            String asunto = "NOTIFICACIÓN COMPROBANTES ELECTRÓNICOS";
            String numero_documento = c.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento() + "-" + c.getInformacionTributariaComprobanteElectronico().getPuntoEmision() + "-" + c.getInformacionTributariaComprobanteElectronico().getSecuencial();
            List<String> contenido = new ArrayList<String>();
            if (_respuestaSRI.getEstado().equals("1")) {
                contenido.add("AUTORIZADO");
            } else if (_respuestaSRI.getEstado().equals("2")) {
                contenido.add("NO AUTORIZADO");
            }
            contenido.add(nombre);
            contenido.add(numero_documento);
            String tipo_documento = Utilidades.obtenerTipoDocumento(c);
            contenido.add(tipo_documento);
            contenido.add(clave_acceso);
            contenido.add("");

            List<String> imagenes = new ArrayList<String>();
            imagenes.add("cab");
            imagenes.add("pie");
            if (Valores.AMBIENTE.equals("1")) {
//                correos.add("sdavidpax-084@hotmail.com");
            }
            Correo correo = new Correo("1");
            enviado = correo.enviarNotificacionAutorizacionComprobante(_direccionArchivoRespuesta, nombre_ruta_pdf_ride, correos, contenido, asunto, Valores.VALOR_NOTIFICACIONES + File.separator + "notificacion.html", Valores.VALOR_PLANTILLA_IMAGENES_CORREO + File.separator, imagenes);
            if (enviado) {
                Logger.getLogger(Notificacion.class.getName()).log(Level.INFO, "Comprobante enviado a :{0} Clave de Acceso:{1} Correo(s):{2}", new Object[]{nombre, clave_acceso, correos});
            }
        }
        return enviado;
    }

    
       
}
