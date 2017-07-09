/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.firma.sri.pruebas;

import com.sun.xml.ws.developer.JAXWSProperties;
import com.wscer.recepcioncomprobantes.RecepcionComprobantes;
import com.wscer.recepcioncomprobantes.RecepcionComprobantesService;
import com.wscer.recepcioncomprobantes.RespuestaSolicitud;
import java.io.File;
import java.io.FileInputStream;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;

/**
 *
* @Ricardo Delgado
 */
public class EnvioComprobante {

    public static final java.lang.String CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
    public static final java.lang.String REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";

    /**
     * Metodo que envia el comprobante al web service de recepcion
     *
     * @param xml comprobante que se enviara a validacion en el SRI
     * @return objeto respuesta del SRI
     */
    public RespuestaSolicitud envCompr(byte[] xml) {

        RecepcionComprobantesService recpComprService = new RecepcionComprobantesService();
        RecepcionComprobantes port = recpComprService.getRecepcionComprobantesPort();
        ((BindingProvider) port).getRequestContext().put(JAXWSProperties.CONNECT_TIMEOUT, 30000);
        ((BindingProvider) port).getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 30000);
        return port.validarComprobante(xml);
    }

    /**
     * Metodo que se utiliza para envio de un archivo comprobante al SRI
     *
     * @param comprobante comrobate a enviar al SRI en tipo File
     * @return true envio correcto false fallo en envio
     * @throws java.lang.Exception
     */
    public String envioComprobante(File comprobante) throws Exception {
        String estado = "";
        byte[] b = new byte[(int) comprobante.length()];
        FileInputStream fis = new FileInputStream(comprobante);
        fis.read(b);
        estado = envioComprobanteFirmado(b);
        return estado;
    }

    /**
     * Metodo que se utiliza para envio de un archivo comprobante al SRI
     *
     * @param comprobante comrobate a enviar al SRI en tipo File
     * @return true envio correcto false fallo en envio
     * @throws java.lang.Exception
     */
//    public String[] envioComprobanteFirmado(File comprobante) throws Exception {
//        String estado = "";
//        byte[] b = new byte[(int) comprobante.length()];
//
//        FileInputStream fis = new FileInputStream(comprobante);
//        fis.read(b);
//        ec.bigdata.firmamultihilo.sri.pruebas.EnvioComprobante servicio_envicio_envio = new ec.bigdata.firmamultihilo.sri.pruebas.EnvioComprobante();
//        RespuestaSolicitud respuesta_solicitud = servicio_envicio_envio.envCompr(b);
//
//        if (respuesta_solicitud != null) {
//             try {
//
//                Logger.getLogger(EnvioComprobante.class.getName()).log(Level.INFO, "ESTADO DE ENVIO:{0}", respuesta_solicitud.getEstado());
//            } catch (Exception e) {
//
//            }
//            if (respuesta_solicitud.getEstado().matches("DEVUELTA")) {
//                estado = "DEVUELTO";
//                if (respuesta_solicitud.getComprobantes() != null) {
//                    if (respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getIdentificador().equals("70")) {
//                        estado = "CA_PROCESAMIENTO";
//                    } else {
//                        try {
//                            estado = estado + respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje();
//                            if (respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional() != null) {
//                                estado += respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional();
//                            }
//                        } catch (Exception e) {
//
//                        }
//
//                    }
//                }
//            } else if (!respuesta_solicitud.getEstado().matches("DEVUELTA")) {
//                estado = "RECIBIDO";
//            }
//        }
//       
//        return estado;
//    }
    public String envioComprobanteFirmado(byte[] _comprobante) throws Exception {
        String estado = "";
        ec.discheca.firma.sri.pruebas.EnvioComprobante servicio_envicio_envio = new ec.discheca.firma.sri.pruebas.EnvioComprobante();
        RespuestaSolicitud respuesta_solicitud = servicio_envicio_envio.envCompr(_comprobante);

        if (respuesta_solicitud != null) {
            try {

                Logger.getLogger(EnvioComprobante.class.getName()).log(Level.INFO, "ESTADO DE ENVIO:{0}", respuesta_solicitud.getEstado());
            } catch (Exception e) {
                Logger.getLogger(EnvioComprobante.class.getName()).log(Level.SEVERE, "ERROR EN EL ENVIO {0}", e);
            }
            if (respuesta_solicitud.getEstado().matches("DEVUELTA")) {
                estado = "DEVUELTO";
                if (respuesta_solicitud.getComprobantes() != null) {
                    if (respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getIdentificador().equals("70")) {
                        estado = "CA_PROCESAMIENTO";
                    } else {
                        try {
                            estado = estado + respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje();
                            if (respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional() != null) {
                                estado += respuesta_solicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional();
                            }
                        } catch (Exception e) {
                            Logger.getLogger(EnvioComprobante.class.getName()).log(Level.SEVERE, "ERROR EN EL ENVIO {0}", e);
                        }

                    }
                }
            } else if (!respuesta_solicitud.getEstado().matches("DEVUELTA")) {
                estado = "RECIBIDO";
            }
        }
        return estado;
    }
}
