/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.firma.sri.produccion;

import com.wspro.recepcioncomprobantes.RecepcionComprobantes;
import com.wspro.recepcioncomprobantes.RecepcionComprobantesService;
import com.wspro.recepcioncomprobantes.RespuestaSolicitud;
import java.io.File;
import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
* @Ricardo Delgado
 */
public class EnvioComprobantePro {

    /**
     * Metodo que envia el comprobante al web service de recepcion
     *
     * @param xml comprobante que se enviara a validacion en el SRI
     * @return objeto respuesta del SRI
     */
    public RespuestaSolicitud enviarComprobante(byte[] xml) {
        RecepcionComprobantesService recComprServ = new RecepcionComprobantesService();
        RecepcionComprobantes recComprPort = recComprServ.getRecepcionComprobantesPort();
        return recComprPort.validarComprobante(xml);
    }

    /**
     * Metodo que se utiliza para envio de un archivo comprobante al SRI
     *
     * @param comprobante comprobante a enviar al SRI en tipo File
     * @return true envio correcto false fallo en envio
     * @throws java.lang.Exception
     */
    public String envioComprobante(File comprobante) throws Exception {
        byte[] b = new byte[(int) comprobante.length()];
        String estado = "";
        FileInputStream fileInputStream = new FileInputStream(comprobante);
        fileInputStream.read(b);
        estado = envioComprobanteFirmado(b);
        return estado;
    }

    /**
     * Convierte la respuesta obtenida en un boolean
     *
     * @param respuestaSTR respuesta obtenida desde el Web Service.
     * @return true si el archivo fue enviado y false si no fue enviado.
     *
     */
    public boolean convertirRespuestaBoolean(String respuestaSTR) {
        boolean respuestaBLN = false;
        if (!respuestaSTR.matches("DEVUELTA")) {
            respuestaBLN = true;
        }
        System.out.println(respuestaSTR);
        return respuestaBLN;
    }

    public String envioComprobanteFirmado(byte[] _comprobante) throws Exception {
        String estado = "";
        EnvioComprobantePro servicioEnvioComprobante = new EnvioComprobantePro();
        RespuestaSolicitud respuesta = servicioEnvioComprobante.enviarComprobante(_comprobante);
        if (respuesta != null) {
            try {

                Logger.getLogger(EnvioComprobantePro.class.getName()).log(Level.INFO, "ESTADO DE ENVIO:{0}", respuesta.getEstado());
            } catch (Exception e) {

            }
            if (respuesta.getEstado().matches("DEVUELTA")) {
                estado = "DEVUELTO";
                if (respuesta.getComprobantes() != null) {
                    if (respuesta.getComprobantes().getComprobante() != null && !respuesta.getComprobantes().getComprobante().isEmpty()) {
                        if (respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getIdentificador().equals("70")) {
                            estado = "CA_PROCESAMIENTO";
                        } else {
                            try {
                                estado = estado + respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getMensaje();
                                if (respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional() != null) {
                                    estado += respuesta.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0).getInformacionAdicional();
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            } else if (respuesta.getEstado().matches("RECIBIDA")) {
                estado = "RECIBIDO";
            }
        }
        return estado;
    }

}
