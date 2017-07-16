/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.firma.sri.pruebas;

import com.sun.xml.ws.developer.JAXWSProperties;
import com.wscer.autorizacioncomprobantes.AutorizacionComprobantes;
import com.wscer.autorizacioncomprobantes.AutorizacionComprobantesService;
import com.wscer.autorizacioncomprobantes.RespuestaComprobante;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;

/**
 *
* @Ricardo Delgado
 */
public class ConsultaComprobante {

    /**
     * Consulta la respuesta desde el SRI.
     *
     * @param claveDeAcceso numero de claveacceso del documento que se desea
     * consultar
     * @return Objeto propio de la aplicacion con la respuesta del SRI
     * @throws java.lang.Exception
     */
    public com.egastos.firma.sri.pruebas.RespuestaSRI obtenerRespuestaSRI(String claveDeAcceso) throws Exception {
        RespuestaComprobante resp = autorizacionComprobante(claveDeAcceso);
        int intentos = 0;
        while (resp.getNumeroComprobantes().equals("0") && intentos < 2) {
            intentos++;
            System.out.println("SIN respuesta " + claveDeAcceso + " INTENTO " + intentos);
            resp = autorizacionComprobante(claveDeAcceso);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConsultaComprobante.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return parsearRespuestaSRI(resp);
    }

    /**
     * Envia una peticion de consulta para verificar la autorizacion de un
     * comprobante
     *
     * @param claveAccesoComprobante clave de acceso del comprobante a consultar
     * @return objeto respuesta del SRI
     */
    public RespuestaComprobante autorizacionComprobante(java.lang.String claveAccesoComprobante) {
        AutorizacionComprobantesService autComprService = new AutorizacionComprobantesService();
        AutorizacionComprobantes autComprPort = autComprService.getAutorizacionComprobantesPort();
        ((BindingProvider) autComprPort).getRequestContext().put(JAXWSProperties.CONNECT_TIMEOUT, 30000);
        ((BindingProvider) autComprPort).getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 30000);
        return autComprPort.autorizacionComprobante(claveAccesoComprobante);
    }

    /**
     * Toma la respuesta obtenida desde el SRI y la convierte en un objeto de
     * Respuesta propio de la aplicacion
     *
     * @param _respuesta objeto respuesta del SRI
     * @return RespuestaComprobanteSRI objeto porpio de la aplicacion, similar a
     * la respuesta del SRI
     */
    public com.egastos.firma.sri.pruebas.RespuestaSRI parsearRespuestaSRI(RespuestaComprobante _respuesta) {
        com.egastos.firma.sri.pruebas.RespuestaSRI respComprSri = new com.egastos.firma.sri.pruebas.RespuestaSRI();
        respComprSri.setEstado("4");
        if (_respuesta != null) {
            respComprSri.setClaveAccesoConsultada(_respuesta.getClaveAccesoConsultada());
            respComprSri.setNumeroComprobantes(_respuesta.getNumeroComprobantes());
            List<com.wscer.autorizacioncomprobantes.Autorizacion> aut = new ArrayList<>();
            com.wscer.autorizacioncomprobantes.Autorizacion autObt = null;
            if (_respuesta.getAutorizaciones() != null && _respuesta.getAutorizaciones().getAutorizacion() != null && !_respuesta.getAutorizaciones().getAutorizacion().isEmpty()) {
                aut = _respuesta.getAutorizaciones().getAutorizacion();

                boolean flag = false;
                for (int i = 0; i < aut.size(); i++) {
                    if (aut.get(i).getEstado().equals("AUTORIZADO")) {
                        autObt = aut.get(i);
                        flag = true;
                        break;
                    }
                }
                if (autObt == null && flag == false) {
                    autObt = aut.get(0);
                }
            }
            if (autObt != null) {

                respComprSri.setEstado(autObt.getEstado());
                respComprSri.setAmbiente(autObt.getAmbiente());
                respComprSri.setNumeroAutorizacion(autObt.getNumeroAutorizacion());
                respComprSri.setComprobante(autObt.getComprobante());
                respComprSri.setFechaAutorizacion(autObt.getFechaAutorizacion().toGregorianCalendar());
                if (autObt.getMensajes().getMensaje().size() > 0) {
                    respComprSri.setNumeroIdentificadorMensaje(autObt.getMensajes().getMensaje().get(0).getIdentificador());
                    respComprSri.setInformacionAdicional(autObt.getMensajes().getMensaje().get(0).getInformacionAdicional());
                    respComprSri.setMensaje(autObt.getMensajes().getMensaje().get(0).getMensaje());
                    respComprSri.setTipoMensaje(autObt.getMensajes().getMensaje().get(0).getTipo());
                }
                aut.clear();
                aut.add(autObt);
                respComprSri.setAutorizaciones(aut);
                respComprSri.setAutorizacionObtenida(autObt);
                respComprSri = this.cambiarEstado(respComprSri);
            } else {
                respComprSri.setEstado("5");/*Estado Demora*/

                respComprSri.setMensaje("Demora en Autorización");
                respComprSri.setInformacionAdicional("El comprobante electrónico aún no tiene autorización.");
            }

            return respComprSri;
        } else {
            return respComprSri;
        }
    }

    /**
     * Cambia el estado del comprobante predefinido por el SRI en texto por
     * numeros definidos
     *
     * @param _respuesta Respuesta que se ha obtenido al mapear el objeto del
     * SRI
     * @return la respuesta con modificacion en su parametro estado 1 =
     * AUTORIZADO ; 2 = NO AUTORIZADO
     */
    public com.egastos.firma.sri.pruebas.RespuestaSRI cambiarEstado(com.egastos.firma.sri.pruebas.RespuestaSRI _respuesta) {
        if (_respuesta != null && _respuesta.getEstado() != null) {
            if (_respuesta.getEstado().equals("AUTORIZADO")) {
                _respuesta.setEstado("1");
            }
            if (_respuesta.getEstado().equals("NO AUTORIZADO")) {
                _respuesta.setEstado("2");
            }
        }
        return _respuesta;
    }

}
