/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.serviciofacturacion;

import ec.discheca.firma.sri.pruebas.FacturacionElectronica;
import ec.discheca.dao.DAOComprobanteElectronicoPendiente;
import ec.discheca.modelo.ComprobanteElectronicoPendiente;
import discheca.utilidades.TransformadorArchivos;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComprobantesPendientes {

    ec.discheca.firma.sri.pruebas.RespuestaSRI respuesta_pruebas = null;
    ec.discheca.firma.sri.produccion.RespuestaSRI respuesta_pro = null;
    ec.discheca.firma.sri.pruebas.ConsultaComprobante consulta_prueba = new ec.discheca.firma.sri.pruebas.ConsultaComprobante();
    ec.discheca.firma.sri.produccion.ConsultaComprobantePro consulta_pro = new ec.discheca.firma.sri.produccion.ConsultaComprobantePro();
    ec.discheca.firma.sri.pruebas.EnvioComprobante envio = new ec.discheca.firma.sri.pruebas.EnvioComprobante();
    ec.discheca.firma.sri.produccion.EnvioComprobantePro envio_pro = new ec.discheca.firma.sri.produccion.EnvioComprobantePro();
    FacturacionElectronica fe = new FacturacionElectronica();

    public void enviarComprobantesPendientes(List<ComprobanteElectronicoPendiente> pendientes) {

        if (pendientes != null && !pendientes.isEmpty()) {
            DAOComprobanteElectronicoPendiente dao_pendientes = null;
            try {
                dao_pendientes = new DAOComprobanteElectronicoPendiente();
            } catch (Exception ex) {
                Logger.getLogger(ComprobantesPendientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            ArchivoRespuestaSRI ar = new ArchivoRespuestaSRI();
            for (ComprobanteElectronicoPendiente cep : pendientes) {
                respuesta_pruebas = null;
                respuesta_pro = null;
                String fecha_procesamiento = "";
                String ruta_respuesta = "";
                String ruta = "";
                /**
                 * Consulta de comprobantes sin autorización y con clave de
                 * acceso en procesamiento
                 */
                if (cep.getEstadoComprobanteElectronicoPendiente().equals("5") || cep.getEstadoComprobanteElectronicoPendiente().equals("10")) {
                    /**
                     * Se obtiene los minutos de la hora actual
                     */
                    Calendar c_actual = Calendar.getInstance();
                    Calendar c_envio_comprobante = Calendar.getInstance();
                    c_envio_comprobante.setTime(cep.getFechaEnvioComprobanteElectronicoPendiente());

                    String estado = "";
                    if (cep.getEstadoComprobanteElectronicoPendiente().equals("5")) {
                        estado = "NO_CONSULTADOS";
                    } else {
                        estado = "CA_PROCESAMIENTO";
                    }
                    /**
                     * Se obtiene la fecha en milisegundos
                     */
                    long milis_actual = c_actual.getTimeInMillis();
                    long milis_envio = c_envio_comprobante.getTimeInMillis();

                    long diferencia_milisegundos = 0;
                    long diferencia_minutos = 0;
                    /**
                     * Se obtiene la diferencia en milisegundos
                     */
                    diferencia_milisegundos = milis_actual - milis_envio;

                    /**
                     * Se obtiene la diferencia en minutos
                     */
                    diferencia_minutos = diferencia_milisegundos / (60 * 1000);

                    if (diferencia_minutos < 40) {

                        if (this.consultarRespuesta(cep).equals("OK")) {
                            
                            ar.generarArchivoRespuesta(respuesta_pro, cep.getRutaComprobanteElectronicoPendiente() + "R_" + respuesta_pro.getClaveAccesoConsultada() + ".xml");
                        }

                    } else if (diferencia_minutos >= 40) {
                        if (this.consultarRespuesta(cep).equals("OK")) {

                            ar.generarArchivoRespuesta(respuesta_pro, cep.getRutaComprobanteElectronicoPendiente() + "R_" + respuesta_pro.getClaveAccesoConsultada() + ".xml");
                        } else {
                            dao_pendientes.actualizarEstado(cep.getIdComprobanteElectronicoPendiente(), "6", "REENVIAR");
                        }
                    }
                } else if (cep.getEstadoComprobanteElectronicoPendiente().equals("6")) {
                    String estado = "";
                    if (cep.getComprobanteElectronico().getAmbienteComprobanteElectronico().equals("1")) {
                        try {
                            //se envia nuevamente el comprobante
                            
                            estado = envio.envioComprobante(new File(cep.getRutaComprobanteElectronicoPendiente() + cep.getComprobanteElectronico().getClaveAccesoComprobanteElectronico()+".xml"));
                        } catch (Exception ex) {
                            Logger.getLogger(ComprobantesPendientes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            //se envia nuevamente el comprobante
                            estado = envio_pro.envioComprobante(new File(cep.getRutaComprobanteElectronicoPendiente() + cep.getComprobanteElectronico().getClaveAccesoComprobanteElectronico()));
                        } catch (Exception ex) {
                            Logger.getLogger(ComprobantesPendientes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (!estado.equals("")) {
                        String estadodevuelto="";
                        if(estado.length()>8){
                        estadodevuelto=estado.substring(0,8);
                        }
                        if (estadodevuelto.matches("DEVUELTO")) {
                            boolean eliminado = dao_pendientes.eliminarComprobantePendiente(cep.getIdComprobanteElectronicoPendiente());
                            dao_pendientes.actualizarEstado(cep.getIdComprobanteElectronicoPendiente(), "11", estado);
                        } else if (estado.matches("CA_PROCESAMIENTO")) {
                            dao_pendientes.actualizarEstado(cep.getIdComprobanteElectronicoPendiente(), "10", estado);
                        } else if (estado.matches("RECIBIDO")) {
                            if (this.consultarRespuesta(cep).equals("OK")) {
                                  dao_pendientes.actualizarEstado(cep.getIdComprobanteElectronicoPendiente(), "Procesado con exito", estado);
                                ar.generarArchivoRespuesta(respuesta_pro, cep.getRutaComprobanteElectronicoPendiente() + "R_" + respuesta_pro.getClaveAccesoConsultada() + ".xml");
                            } else if (respuesta_pro.getEstado().equals("5")) {
                                dao_pendientes.actualizarEstado(cep.getIdComprobanteElectronicoPendiente(), "5", respuesta_pro.getMensaje());
                            }
                        }

                    }
                }
                if (respuesta_pro != null) {
                    if (respuesta_pro.getEstado().equals("1") || respuesta_pro.getEstado().equals("2")) {
                        AlmacenamientoComprobanteElectronico ace = new AlmacenamientoComprobanteElectronico();
                        byte[] comprobante_bytes = null;
                        byte[] respuesta_byte = null;
                        try {
                            comprobante_bytes = respuesta_pro.getComprobante().getBytes("UTF-8");
                            respuesta_byte = TransformadorArchivos.archArrayB(new File(cep.getRutaComprobanteElectronicoPendiente() + "R_" + respuesta_pro.getClaveAccesoConsultada() + ".xml"));
                        } catch (UnsupportedEncodingException ex) {
                            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        boolean guardado = ace.actualizarEstadoComprobantePendiente(respuesta_pro, comprobante_bytes, respuesta_byte);
                        if (guardado) {
                            boolean eliminado = dao_pendientes.eliminarComprobantePendiente(cep.getIdComprobanteElectronicoPendiente());
                            if (eliminado) {
                                /**
                                 * Enviar correo
                                 */
                                if (respuesta_pro.getEstado().equals("1")) {
                                    Notificacion n = new Notificacion();
                                     boolean notificado = n.enviarNotificacion(respuesta_pro, cep.getRutaComprobanteElectronicoPendiente() + "R_" + respuesta_pro.getClaveAccesoConsultada() + ".xml", "ricardo.telcomp@hotmail.com");
                                }
                            }
                        }
                    }
                }
            }

        }

    }

    private String consultarRespuesta(ComprobanteElectronicoPendiente cep) {
        String estado = "NO";
        if (cep.getComprobanteElectronico().getAmbienteComprobanteElectronico().equals("1")) {
            try {
                respuesta_pruebas = consulta_prueba.obtenerRespuestaSRI(cep.getComprobanteElectronico().getClaveAccesoComprobanteElectronico());
                respuesta_pro = fe.toRespuestaSRIProduccion(respuesta_pruebas);

            } catch (Exception ex) {
                Logger.getLogger(ComprobantesPendientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                respuesta_pro = consulta_pro.obtenerRespuestaSRI(cep.getComprobanteElectronico().getClaveAccesoComprobanteElectronico());

            } catch (Exception ex) {
                Logger.getLogger(ComprobantesPendientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (respuesta_pro != null) {
            if (respuesta_pro.getEstado().equals("1") || respuesta_pro.getEstado().equals("2")) {
                estado = "OK";
            }
        }
        return estado;
    }

}
