/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.firma.sri.pruebas;


import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.wscer.autorizacioncomprobantes.Autorizacion;
import com.egastos.firma.constantes.Errores;
import com.egastos.firma.firmaxades.AccesoCertificado;
import com.egastos.firma.firmaxades.FirmaXades;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;
import org.xml.sax.SAXException;

/**
 *
* @Ricardo Delgado
 */
public class FacturacionElectronica {

    public static int $MAX_INTENTOS = 3;
    public static int $RETARDO = 1;

    /**
     * Metodo que me realiza un parsing de RespuestaSRI de pruebas a
     * RespuestaSRI de produccion
     *
     * @param _respuestaComprobanteSRI Objeto RespuestaSRI de pruebas
     * @return RespuestaSRI de produccion
     */
    public com.egastos.firma.sri.produccion.RespuestaSRI toRespuestaSRIProduccion(RespuestaSRI _respuestaComprobanteSRI) {
        com.egastos.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRIPro = new com.egastos.firma.sri.produccion.RespuestaSRI();
        if (_respuestaComprobanteSRI != null) {
            respuestaComprobanteSRIPro.setNumeroComprobantes(_respuestaComprobanteSRI.getNumeroComprobantes());
            respuestaComprobanteSRIPro.setAmbiente(_respuestaComprobanteSRI.getAmbiente());
            respuestaComprobanteSRIPro.setClaveAccesoConsultada(_respuestaComprobanteSRI.getClaveAccesoConsultada());
            respuestaComprobanteSRIPro.setComprobante(_respuestaComprobanteSRI.getComprobante());
            respuestaComprobanteSRIPro.setFechaAutorizacion(_respuestaComprobanteSRI.getFechaAutorizacion());
            respuestaComprobanteSRIPro.setInformacionAdicional(_respuestaComprobanteSRI.getInformacionAdicional());
            respuestaComprobanteSRIPro.setMensaje(_respuestaComprobanteSRI.getMensaje());
            respuestaComprobanteSRIPro.setNumeroAutorizacion(_respuestaComprobanteSRI.getNumeroAutorizacion());
            respuestaComprobanteSRIPro.setNumeroIdentificadorMensaje(_respuestaComprobanteSRI.getNumeroIdentificadorMensaje());
            respuestaComprobanteSRIPro.setTipoMensaje(_respuestaComprobanteSRI.getTipoMensaje());
            respuestaComprobanteSRIPro.setEstado(_respuestaComprobanteSRI.getEstado());
            com.wspro.autorizacioncomprobantes.Autorizacion autorizacionPro = new com.wspro.autorizacioncomprobantes.Autorizacion();
            //Autorizacion de pruebas a Autorizacion de produccion
            if (_respuestaComprobanteSRI.getAutorizacionObtenida() != null) {
                Autorizacion autorizacionPruebas = _respuestaComprobanteSRI.getAutorizacionObtenida();

                autorizacionPro.setAmbiente(autorizacionPruebas.getAmbiente());
                autorizacionPro.setComprobante(autorizacionPruebas.getComprobante());
                autorizacionPro.setEstado(autorizacionPruebas.getEstado());
                autorizacionPro.setFechaAutorizacion(autorizacionPruebas.getFechaAutorizacion());
                autorizacionPro.setNumeroAutorizacion(autorizacionPruebas.getNumeroAutorizacion());
                if (autorizacionPruebas.getMensajes() != null && autorizacionPruebas.getMensajes().getMensaje()!=null && !autorizacionPruebas.getMensajes().getMensaje().isEmpty()) {
                    com.wscer.autorizacioncomprobantes.Mensaje mensaje = autorizacionPruebas.getMensajes().getMensaje().get(0);
                    com.wspro.autorizacioncomprobantes.Mensaje mensajePro = new com.wspro.autorizacioncomprobantes.Mensaje();
                    mensajePro.setIdentificador(mensaje.getIdentificador());
                    mensajePro.setMensaje(mensaje.getMensaje());
                    mensajePro.setTipo(mensaje.getTipo());
                    mensajePro.setInformacionAdicional(mensaje.getInformacionAdicional());
                    com.wspro.autorizacioncomprobantes.Autorizacion.Mensajes m = new com.wspro.autorizacioncomprobantes.Autorizacion.Mensajes();
                    m.getMensaje().add(mensajePro);
                    autorizacionPro.setMensajes(m);

                }

                List<com.wspro.autorizacioncomprobantes.Autorizacion> autorizacionesSRIPro = new ArrayList<com.wspro.autorizacioncomprobantes.Autorizacion>();
                if (_respuestaComprobanteSRI.getAutorizaciones() != null) {
                    for (Autorizacion a : _respuestaComprobanteSRI.getAutorizaciones()) {
                        com.wspro.autorizacioncomprobantes.Autorizacion autorizacionSRIPro = new com.wspro.autorizacioncomprobantes.Autorizacion();
                        autorizacionSRIPro.setAmbiente(a.getAmbiente());
                        autorizacionSRIPro.setComprobante(a.getComprobante());
                        autorizacionSRIPro.setEstado(a.getEstado());
                        autorizacionSRIPro.setFechaAutorizacion(a.getFechaAutorizacion());
                        if (a.getMensajes().getMensaje() != null && a.getMensajes().getMensaje()!=null && !a.getMensajes().getMensaje().isEmpty()) {
                            com.wscer.autorizacioncomprobantes.Mensaje mensaje = a.getMensajes().getMensaje().get(0);
                            com.wspro.autorizacioncomprobantes.Mensaje mensajePro = new com.wspro.autorizacioncomprobantes.Mensaje();
                            mensajePro.setIdentificador(mensaje.getIdentificador());
                            mensajePro.setMensaje(mensaje.getMensaje());
                            mensajePro.setTipo(mensaje.getTipo());
                            mensajePro.setInformacionAdicional(mensaje.getInformacionAdicional());
                            com.wspro.autorizacioncomprobantes.Autorizacion.Mensajes m = new com.wspro.autorizacioncomprobantes.Autorizacion.Mensajes();
                            m.getMensaje().add(mensajePro);
                            autorizacionSRIPro.setMensajes(m);

                        }

                        autorizacionSRIPro.setNumeroAutorizacion(a.getNumeroAutorizacion());
                        autorizacionesSRIPro.add(autorizacionSRIPro);
                    }
                }

                respuestaComprobanteSRIPro.setAutorizacionObtenida(autorizacionPro);
                respuestaComprobanteSRIPro.setAutorizaciones(autorizacionesSRIPro);
            }
        }

        return respuestaComprobanteSRIPro;
    }

    public com.egastos.firma.sri.pruebas.RespuestaSRI ConsultarComrpobanteElectronico(ComprobanteElectronico _comprobante) {

        com.egastos.firma.sri.pruebas.RespuestaSRI respuestaComprobanteSRI = new com.egastos.firma.sri.pruebas.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();

        try {

            com.egastos.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new com.egastos.firma.sri.pruebas.ConsultaComprobante();
            com.egastos.firma.sri.pruebas.EnvioComprobante envioComprobante = new com.egastos.firma.sri.pruebas.EnvioComprobante();

            String estado = "";
            boolean conexion_uno = false;
            boolean conexion_dos = false;
            boolean errorSRI = false;

            respuestaComprobanteSRI.setEstado("9");
            int intentos_uno = 0;

            int intentos_dos = 0;
            while (!conexion_dos) {
                intentos_dos++;
                if (intentos_dos == $MAX_INTENTOS + 1) {
                    break;
                }
                try {
                    respuestaComprobanteSRI = consultaAutorizacion.obtenerRespuestaSRI(_comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso());
                    conexion_dos = true;
                } catch (Exception ex) {

                }
                if (!conexion_dos) {
                    Thread.sleep((long) 1000 * $RETARDO);
                }
            }
            if (!conexion_dos) {
                respuestaComprobanteSRI.setEstado("5");
                respuestaComprobanteSRI.setMensaje(Errores.$FallaConsultaAutorizacion);
            }

        } catch (Exception ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }

    public com.egastos.firma.sri.pruebas.RespuestaSRI ConsultarComrpobanteClaveAcceso(String claveAcceso) {

        com.egastos.firma.sri.pruebas.RespuestaSRI respuestaComprobanteSRI = new com.egastos.firma.sri.pruebas.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");

        try {

            com.egastos.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new com.egastos.firma.sri.pruebas.ConsultaComprobante();
            com.egastos.firma.sri.pruebas.EnvioComprobante envioComprobante = new com.egastos.firma.sri.pruebas.EnvioComprobante();

            boolean conexion_dos = false;

            respuestaComprobanteSRI.setEstado("9");
            int intentos_uno = 0;

            int intentos_dos = 0;
            while (!conexion_dos) {
                intentos_dos++;
                if (intentos_dos == $MAX_INTENTOS + 1) {
                    break;
                }
                try {
                    respuestaComprobanteSRI = consultaAutorizacion.obtenerRespuestaSRI(claveAcceso);
                    conexion_dos = true;
                } catch (Exception ex) {

                }
                if (!conexion_dos) {
                    Thread.sleep((long) 1000 * $RETARDO);
                }
            }
            if (!conexion_dos) {
                respuestaComprobanteSRI.setEstado("5");
                respuestaComprobanteSRI.setMensaje(Errores.$FallaConsultaAutorizacion);
            }

        } catch (Exception ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }

    public com.egastos.firma.sri.pruebas.RespuestaSRI autorizarComprobantesElectronicos(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, boolean enviar_a_firmar, String _version_xml) {

        com.egastos.firma.sri.pruebas.RespuestaSRI respuestaComprobanteSRI = new com.egastos.firma.sri.pruebas.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String infoAdicional = "";

        try {
            com.egastos.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new com.egastos.firma.sri.pruebas.ConsultaComprobante();
            boolean conexion_dos = false;
       
            // solamente se hace la referencia al archivo que ya existe         
                       respuestaComprobanteSRI.setEstado("9");
                                        int intentos_dos = 0;
                                        while (!conexion_dos) {
                                            intentos_dos++;
                                            if (intentos_dos == $MAX_INTENTOS + 1) {
                                                break;
                                            }
                                            try {
                                                respuestaComprobanteSRI = consultaAutorizacion.obtenerRespuestaSRI(_comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso());
                                                conexion_dos = true;
                                            } catch (Exception ex) {
                                                infoAdicional += ex.getMessage();
                                            }
                                            if (!conexion_dos) {
                                                Thread.sleep((long) 1000 * $RETARDO);
                                            }
                                        }
                                        if (!conexion_dos) {
                                            respuestaComprobanteSRI.setEstado("5");
                                            respuestaComprobanteSRI.setMensaje(Errores.$FallaConsultaAutorizacion);
                                        }
                                 
                              
                          
                        
                 
                
           
        } catch (Exception ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }



  
    public static void main(String args[]) throws Exception {
     
        

    }
}
