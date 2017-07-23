/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.firma.sri.produccion;

import com.wspro.autorizacioncomprobantes.Autorizacion;
import com.wspro.autorizacioncomprobantes.AutorizacionComprobantes;
import com.wspro.autorizacioncomprobantes.AutorizacionComprobantesService;
import com.wspro.autorizacioncomprobantes.Mensaje;
import com.wspro.autorizacioncomprobantes.RespuestaComprobante;
import java.util.ArrayList;
import java.util.List;

/**
 *
* @Ricardo Delgado
 */
public class ConsultaComprobantePro {

    /**
     * Consulta la respuesta desde el SRI.
     *
     * @param claveDeAcceso numero de clave de acceso del documento que se desea
     * consultar
     * @return Objeto propio de la aplicacion con la respuesta del SRI
     * @throws java.lang.Exception
     */
    public RespuestaSRI obtenerRespuestaSRI(String claveDeAcceso) throws Exception {
        RespuestaComprobante respuesta = autorizacionComprobante(claveDeAcceso);
        int contador_intentos = 0;
        while (respuesta.getNumeroComprobantes().equals("0") && contador_intentos < 2) {
            contador_intentos++;
            System.out.println("SIN respuesta "+claveDeAcceso +" INTENTO "+contador_intentos);
            respuesta = autorizacionComprobante(claveDeAcceso);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
            
        }
        return parsearRespuestaSRI(respuesta);
    }

    /**
     * Envia una petición de consulta para verificar la autorizacion de un
     * comprobante
     *
     * @param claveAccesoComprobante clave de acceso del comprobante a consultar
     * @return objeto respuesta del SRI
     * @throws java.lang.Exception
     */
    public RespuestaComprobante autorizacionComprobante(String claveAccesoComprobante) throws Exception {
        AutorizacionComprobantesService service = new AutorizacionComprobantesService();
        AutorizacionComprobantes port = service.getAutorizacionComprobantesPort();
        return port.autorizacionComprobante(claveAccesoComprobante);
    }

    /**
     * Toma la respuesta obtenida desde el SRI y la convierte en un objeto de
     * respuesta propio de la aplicación
     *
     * @param respuesta objeto respuesta del SRI
     * @return RespuestaSRI objeto porpio de la aplicación, similar a
 la respuesta del SRI
     */
    public RespuestaSRI parsearRespuestaSRI(RespuestaComprobante respuesta) {
        RespuestaSRI respuestaComprobanteSri = new RespuestaSRI();
        respuestaComprobanteSri.setEstado("4");
        if (respuesta != null) {
            respuestaComprobanteSri.setClaveAccesoConsultada(respuesta.getClaveAccesoConsultada());
            respuestaComprobanteSri.setNumeroComprobantes(respuesta.getNumeroComprobantes());
            List<Autorizacion> autorizaciones = new ArrayList<Autorizacion>();
            Autorizacion autorizacionObtenida = null;
            if (respuesta.getAutorizaciones() != null && respuesta.getAutorizaciones().getAutorizacion() != null && !respuesta.getAutorizaciones().getAutorizacion().isEmpty()) {
                autorizaciones = respuesta.getAutorizaciones().getAutorizacion();

                boolean bandera = false;
                for (int i = 0; i < autorizaciones.size(); i++) {
                    if (autorizaciones.get(i).getEstado().equals("AUTORIZADO")) {
                        autorizacionObtenida = autorizaciones.get(i);
                        bandera = true;
                        break;
                    }
                }
                if (autorizacionObtenida == null && bandera == false) {
                    autorizacionObtenida = autorizaciones.get(0);
                }
            }
            if (autorizacionObtenida != null) {

                respuestaComprobanteSri.setEstado(autorizacionObtenida.getEstado());
                respuestaComprobanteSri.setAmbiente(autorizacionObtenida.getAmbiente());
                respuestaComprobanteSri.setNumeroAutorizacion(autorizacionObtenida.getNumeroAutorizacion());
                respuestaComprobanteSri.setComprobante(autorizacionObtenida.getComprobante());
                respuestaComprobanteSri.setFechaAutorizacion(autorizacionObtenida.getFechaAutorizacion().toGregorianCalendar());
                if (autorizacionObtenida.getMensajes().getMensaje().size() > 0) {
                    respuestaComprobanteSri.setNumeroIdentificadorMensaje(autorizacionObtenida.getMensajes().getMensaje().get(0).getIdentificador());
                    respuestaComprobanteSri.setInformacionAdicional(autorizacionObtenida.getMensajes().getMensaje().get(0).getInformacionAdicional());
                    respuestaComprobanteSri.setMensaje(autorizacionObtenida.getMensajes().getMensaje().get(0).getMensaje());
                    respuestaComprobanteSri.setTipoMensaje(autorizacionObtenida.getMensajes().getMensaje().get(0).getTipo());
                }
                autorizaciones.clear();
                autorizaciones.add(autorizacionObtenida);
                respuestaComprobanteSri.setAutorizaciones(autorizaciones);
                respuestaComprobanteSri.setAutorizacionObtenida(autorizacionObtenida);
                respuestaComprobanteSri = this.cambiarEstado(respuestaComprobanteSri);
            } else {
                respuestaComprobanteSri.setEstado("5");/*Estado Demora*/

                respuestaComprobanteSri.setMensaje("Pendiente de Autorización");
                respuestaComprobanteSri.setInformacionAdicional("El comprobante electrónico aún no tiene autorización.");
            }

            return respuestaComprobanteSri;
        } else {
            return respuestaComprobanteSri;
        }
    }

    /**
     * Cambia el estado del comprobante predefinido por el SRI en texto por
     * numeros definidos
     *
     * @param respuesta Respuesta que se ha obtenido al mapear el objeto del SRI
     * @return la respuesta con modificacion en su parametro estado 1 =
     * AUTORIZADO ; 2 = NO AUTORIZADO
     */
    public RespuestaSRI cambiarEstado(RespuestaSRI respuesta) {

        if (respuesta != null && respuesta.getEstado() != null) {
            if (respuesta.getEstado().equals("AUTORIZADO")) {
                respuesta.setEstado("1");
            }
            if (respuesta.getEstado().equals("NO AUTORIZADO")) {
                respuesta.setEstado("2");
            }
        }
        return respuesta;
    }
}
