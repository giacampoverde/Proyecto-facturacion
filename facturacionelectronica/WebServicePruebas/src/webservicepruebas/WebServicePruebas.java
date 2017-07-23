/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservicepruebas;

import autorizacion.ws.sri.gob.ec.RespuestaComprobante;
import autorizacion.ws.sri.gob.ec.RespuestaLote;
import recepcion.ws.sri.gob.ec.RespuestaSolicitud;

/**
 *
 * @author Usuario
 */
public class WebServicePruebas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }

    private static RespuestaComprobante autorizacionComprobante(java.lang.String claveAccesoComprobante) {
        autorizacion.ws.sri.gob.ec.AutorizacionComprobantesService service = new autorizacion.ws.sri.gob.ec.AutorizacionComprobantesService();
        autorizacion.ws.sri.gob.ec.AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
        return port.autorizacionComprobante(claveAccesoComprobante);
    }

    private static RespuestaLote autorizacionComprobanteLote(java.lang.String claveAccesoLote) {
        autorizacion.ws.sri.gob.ec.AutorizacionComprobantesService service = new autorizacion.ws.sri.gob.ec.AutorizacionComprobantesService();
        autorizacion.ws.sri.gob.ec.AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
        return port.autorizacionComprobanteLote(claveAccesoLote);
    }

    private static RespuestaSolicitud validarComprobante(byte[] xml) {
        recepcion.ws.sri.gob.ec.RecepcionComprobantesService service = new recepcion.ws.sri.gob.ec.RecepcionComprobantesService();
        recepcion.ws.sri.gob.ec.RecepcionComprobantes port = service.getRecepcionComprobantesPort();
        return port.validarComprobante(xml);
    }
    
}
