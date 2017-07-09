/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.firma.sri.pruebas;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.wscer.autorizacioncomprobantes.Autorizacion;
import ec.discheca.firma.constantes.Errores;
import ec.discheca.firma.firmaxades.AccesoCertificado;
import ec.discheca.firma.firmaxades.FirmaXades;
import ec.discheca.validadorXSD.ValidacionXSD;
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
    public ec.discheca.firma.sri.produccion.RespuestaSRI toRespuestaSRIProduccion(RespuestaSRI _respuestaComprobanteSRI) {
        ec.discheca.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRIPro = new ec.discheca.firma.sri.produccion.RespuestaSRI();
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

    public ec.discheca.firma.sri.pruebas.RespuestaSRI ConsultarComrpobanteElectronico(ComprobanteElectronico _comprobante) {

        ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaComprobanteSRI = new ec.discheca.firma.sri.pruebas.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();

        try {

            ec.discheca.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new ec.discheca.firma.sri.pruebas.ConsultaComprobante();
            ec.discheca.firma.sri.pruebas.EnvioComprobante envioComprobante = new ec.discheca.firma.sri.pruebas.EnvioComprobante();

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

    public ec.discheca.firma.sri.pruebas.RespuestaSRI ConsultarComrpobanteClaveAcceso(String claveAcceso) {

        ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaComprobanteSRI = new ec.discheca.firma.sri.pruebas.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");

        try {

            ec.discheca.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new ec.discheca.firma.sri.pruebas.ConsultaComprobante();
            ec.discheca.firma.sri.pruebas.EnvioComprobante envioComprobante = new ec.discheca.firma.sri.pruebas.EnvioComprobante();

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

    public ec.discheca.firma.sri.pruebas.RespuestaSRI autorizarComprobantesElectronicos(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, boolean enviar_a_firmar, String _version_xml) {

        ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaComprobanteSRI = new ec.discheca.firma.sri.pruebas.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        File comprobante_sin_firma = null;

        String infoAdicional = "";

        try {

            ValidacionXSD validadorXSD = new ValidacionXSD();
            FirmaXades firmaXades = new FirmaXades();

            ec.discheca.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new ec.discheca.firma.sri.pruebas.ConsultaComprobante();
            ec.discheca.firma.sri.pruebas.EnvioComprobante envioComprobante = new ec.discheca.firma.sri.pruebas.EnvioComprobante();
            boolean banderavalidacionXSD = false;
            boolean banderaFirma = false;
            String estado = "";
            boolean conexion_uno = false;
            boolean conexion_dos = false;
            boolean errorSRI = false;

            // solamente se hace la referencia al archivo que ya existe
            comprobante_sin_firma = new File(_direccion_archivo_a_firmar);
            if (comprobante_sin_firma != null) {
                if (_version_xml != null) {

                    /**
                     * Se verifica la versión del comprobante (1.0.0 o 1.1.0)
                     * para validarlo con el XSD que corresponda
                     */
                    if (_version_xml.equals("1.0.0")) {
                        banderavalidacionXSD = validadorXSD.validarComprobantePorTipo(comprobante_sin_firma, tipoDocumento);
                        try {
                            System.out.println("Validación con 2 decimales: " + validadorXSD.getMensajeResumenXSD());
                        } catch (Exception e) {
                            Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con dos decimales del XSD.");
                        }
                    } else {
                        banderavalidacionXSD = validadorXSD.validarComprobantePorTipoVersion1110(comprobante_sin_firma, tipoDocumento);
                        try {
                            System.out.println("Validación con 4 decimales: " + validadorXSD.getMensajeResumenXSD());
                        } catch (Exception e) {
                            Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con 4 decimales del XSD.");
                        }

                    }
                    if (banderavalidacionXSD) {
                        respuestaComprobanteSRI.setEstado("9");
                        if (enviar_a_firmar) {
                            banderaFirma = firmaXades.firmar(_acceso_certificado, _direccion_archivo_a_firmar, _path_salida, _nombre_archivo_salida, _ruc_emisor);
                            if (banderaFirma) {

                                File comprobante_firmado = new File(_path_salida + File.separator + _nombre_archivo_salida);
                                int intentos_uno = 0;
                                while (!conexion_uno) {
                                    errorSRI = false;
                                    intentos_uno++;
                                    if (intentos_uno == $MAX_INTENTOS) {
                                        break;
                                    }
                                    try {

                                        estado = envioComprobante.envioComprobante(comprobante_firmado);
                                        conexion_uno = true;
                                    } catch (SocketException ex) {
                                        System.err.println(ex);
                                        errorSRI = true;

                                    } catch (SOAPFaultException ex) {
                                        System.err.println(ex);

                                        errorSRI = true;
                                    } catch (Exception ex) {
                                        System.err.println(ex);
                                        errorSRI = true;

                                    }

                                    if (errorSRI) {
                                        Thread.sleep((long) 1000 * $RETARDO);
                                    }
                                }
                                if (conexion_uno) {
                                    if (estado.equals("RECIBIDO")) {
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
                                    } else {
                                        if (estado.equals("CA_PROCESAMIENTO")) {
                                            respuestaComprobanteSRI.setEstado("10");
                                            respuestaComprobanteSRI.setMensaje(Errores.$CA_Procesamiento);
                                        } else {
                                            respuestaComprobanteSRI.setEstado("11");
                                            respuestaComprobanteSRI.setMensaje(Errores.$FallaBanderaEnvio + estado);
                                        }
                                    }
                                } else {
                                    if (errorSRI) {
                                        respuestaComprobanteSRI.setEstado("6");
                                        respuestaComprobanteSRI.setMensaje(Errores.$FallaErrorSRI);

                                    } else {
                                        respuestaComprobanteSRI.setEstado("6");
                                        respuestaComprobanteSRI.setMensaje(Errores.$FallaEnvioComprobante + " - Se han hecho " + (intentos_uno - 1) + " intentos.");
                                    }
                                }
                            } else {
                                respuestaComprobanteSRI.setMensaje(Errores.$FallaFirmaComprobante);
                            }
                        }
                    } else {
                        respuestaComprobanteSRI.setInformacionAdicional("Clave de Acceso:" + _comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + "|" + "Error: " + validadorXSD.getMensajeResumenXSD());
                        respuestaComprobanteSRI.setMensaje(Errores.$ErrorXSDEstructura);
                    }
                }
            } else {
                respuestaComprobanteSRI.setMensaje(Errores.$CreacionComprobante);
            }
        } catch (Exception ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }

    public ec.discheca.firma.sri.pruebas.RespuestaSRI autorizarComprobantesElectronicosFloral(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, boolean enviar_a_firmar, String _version_xml) {

        ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaComprobanteSRI = new ec.discheca.firma.sri.pruebas.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        File comprobante_sin_firma = null;

        String infoAdicional = "";

        try {

            ValidacionXSD validadorXSD = new ValidacionXSD();
            FirmaXades firmaXades = new FirmaXades();

            ec.discheca.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new ec.discheca.firma.sri.pruebas.ConsultaComprobante();
            ec.discheca.firma.sri.pruebas.EnvioComprobante envioComprobante = new ec.discheca.firma.sri.pruebas.EnvioComprobante();
            boolean banderavalidacionXSD = false;
            boolean banderaFirma = false;
            String estado = "";
            boolean conexion_uno = false;
            boolean conexion_dos = false;
            boolean errorSRI = false;

            comprobante_sin_firma = new File(_direccion_archivo_a_firmar);
            if (comprobante_sin_firma != null) {
                if (_version_xml != null) {

                    /**
                     * Se verifica la versión del comprobante (1.0.0 o 1.1.0)
                     * para validarlo con el XSD que corresponda
                     */
                    if (_version_xml.equals("1.0.0")) {
                        banderavalidacionXSD = validadorXSD.validarComprobantePorTipoFloral(comprobante_sin_firma, tipoDocumento);
                        try {
                            System.out.println("Validación con 2 decimales: " + validadorXSD.getMensajeResumenXSD());
                        } catch (Exception e) {
                            Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con dos decimales del XSD.");
                        }
                    } else {
                        banderavalidacionXSD = validadorXSD.validarComprobantePorTipoVersion1110(comprobante_sin_firma, tipoDocumento);
                        try {
                            System.out.println("Validación con 4 decimales: " + validadorXSD.getMensajeResumenXSD());
                        } catch (Exception e) {
                            Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con 4 decimales del XSD.");
                        }

                    }
                    if (banderavalidacionXSD) {
                        respuestaComprobanteSRI.setEstado("9");
                        if (enviar_a_firmar) {
                            banderaFirma = firmaXades.firmar(_acceso_certificado, _direccion_archivo_a_firmar, _path_salida, _nombre_archivo_salida, _ruc_emisor);
                            if (banderaFirma) {

                                File comprobante_firmado = new File(_path_salida + File.separator + _nombre_archivo_salida);
                                int intentos_uno = 0;
                                while (!conexion_uno) {
                                    errorSRI = false;
                                    intentos_uno++;
                                    if (intentos_uno == $MAX_INTENTOS) {
                                        break;
                                    }
                                    try {

                                        estado = envioComprobante.envioComprobante(comprobante_firmado);
                                        conexion_uno = true;
                                    } catch (SocketException ex) {
                                        System.err.println(ex);
                                        errorSRI = true;

                                    } catch (SOAPFaultException ex) {
                                        System.err.println(ex);

                                        errorSRI = true;
                                    } catch (Exception ex) {
                                        System.err.println(ex);
                                        errorSRI = true;

                                    }

                                    if (errorSRI) {
                                        Thread.sleep((long) 1000 * $RETARDO);
                                    }
                                }
                                if (conexion_uno) {
                                    if (estado.equals("RECIBIDO")) {
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
                                    } else {
                                        if (estado.equals("CA_PROCESAMIENTO")) {
                                            respuestaComprobanteSRI.setEstado("10");
                                            respuestaComprobanteSRI.setMensaje(Errores.$CA_Procesamiento);
                                        } else {
                                            respuestaComprobanteSRI.setEstado("11");
                                            respuestaComprobanteSRI.setMensaje(Errores.$FallaBanderaEnvio + estado);
                                        }
                                    }
                                } else {
                                    if (errorSRI) {
                                        respuestaComprobanteSRI.setEstado("6");
                                        respuestaComprobanteSRI.setMensaje(Errores.$FallaErrorSRI);

                                    } else {
                                        respuestaComprobanteSRI.setEstado("6");
                                        respuestaComprobanteSRI.setMensaje(Errores.$FallaEnvioComprobante + " - Se han hecho " + (intentos_uno - 1) + " intentos.");
                                    }
                                }
                            } else {
                                respuestaComprobanteSRI.setMensaje(Errores.$FallaFirmaComprobante);
                            }
                        }
                    } else {
                        respuestaComprobanteSRI.setInformacionAdicional("Clave de Acceso:" + _comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + "|" + "Error: " + validadorXSD.getMensajeResumenXSD());
                        respuestaComprobanteSRI.setMensaje(Errores.$ErrorXSDEstructura);
                    }
                }
            } else {
                respuestaComprobanteSRI.setMensaje(Errores.$CreacionComprobante);
            }
        } catch (IOException | SAXException | InterruptedException ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }

    public boolean firmaryValidarComprobantesElectronicos(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, String _version_xml) {

        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        File comprobante_sin_firma = null;
        boolean banderavalidacionXSD = false;
        boolean banderaFirma = false;

        try {

            ValidacionXSD validadorXSD = new ValidacionXSD();
            FirmaXades firmaXades = new FirmaXades();

            comprobante_sin_firma = new File(_direccion_archivo_a_firmar);
            if (comprobante_sin_firma != null) {

                /**
                 * Se verifica la versión del comprobante (1.0.0 o 1.1.0) para
                 * validarlo con el XSD que corresponda
                 */
                if (_version_xml.equals("1.0.0")) {
                    banderavalidacionXSD = validadorXSD.validarComprobantePorTipo(comprobante_sin_firma, tipoDocumento);
                    try {
                        System.out.println("Validación con 2 decimales: " + validadorXSD.getMensajeResumenXSD());
                    } catch (Exception e) {
                        Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con dos decimales del XSD.");
                    }
                } else {
                    banderavalidacionXSD = validadorXSD.validarComprobantePorTipoVersion1110(comprobante_sin_firma, tipoDocumento);
                    try {
                        System.out.println("Validación con 4 decimales: " + validadorXSD.getMensajeResumenXSD());
                    } catch (Exception e) {
                        Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con 4 decimales del XSD.");
                    }

                }

                if (banderavalidacionXSD) {

                    banderaFirma = firmaXades.firmar(_acceso_certificado, _direccion_archivo_a_firmar, _path_salida, _nombre_archivo_salida, _ruc_emisor);

                }
            }
        } catch (IOException | SAXException e) {

        }
        return banderaFirma && banderavalidacionXSD;
    }

    public ec.discheca.firma.sri.produccion.RespuestaSRI firmaryValidarComprobantes(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, String _version_xml) {

        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        File comprobante_sin_firma = null;
        boolean banderavalidacionXSD = false;
        boolean banderaFirma = false;
        ec.discheca.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new ec.discheca.firma.sri.produccion.RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        try {

            ValidacionXSD validadorXSD = new ValidacionXSD();
            FirmaXades firmaXades = new FirmaXades();

            comprobante_sin_firma = new File(_direccion_archivo_a_firmar);
            if (comprobante_sin_firma != null) {

                /**
                 * Se verifica la versión del comprobante (1.0.0 o 1.1.0) para
                 * validarlo con el XSD que corresponda
                 */
                if (_version_xml.equals("1.0.0")) {
                    banderavalidacionXSD = validadorXSD.validarComprobantePorTipo(comprobante_sin_firma, tipoDocumento);
                    try {
                        System.out.println("Validación con 2 decimales: " + validadorXSD.getMensajeResumenXSD());
                    } catch (Exception e) {
                        Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con dos decimales del XSD.");
                    }
                } else {
                    banderavalidacionXSD = validadorXSD.validarComprobantePorTipoVersion1110(comprobante_sin_firma, tipoDocumento);
                    try {
                        System.out.println("Validación con 4 decimales: " + validadorXSD.getMensajeResumenXSD());
                    } catch (Exception e) {
                        Logger.getLogger(ec.discheca.firma.sri.pruebas.FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con 4 decimales del XSD.");
                    }
                }
                if (banderavalidacionXSD) {
                    banderaFirma = firmaXades.firmar(_acceso_certificado, _direccion_archivo_a_firmar, _path_salida, _nombre_archivo_salida, _ruc_emisor);
                    if (banderaFirma) {
                        respuestaComprobanteSRI.setEstado("3");
                        respuestaComprobanteSRI.setMensaje(Errores.$SRIMantenimientoClavesNormales);

                    }
                }
            }
        } catch (IOException | SAXException e) {

        }
        return respuestaComprobanteSRI;
    }
    
    /**
     * Método que realiza el proceso de autorización para los comprobantes previamente firmados.
     * @param comprobante_firmado
     * @param _clave_acceso_comprobante
     * @return 
     */
        public RespuestaSRI autorizarComprobantesElectronicosFirmados(byte[] comprobante_firmado, String _clave_acceso_comprobante) {

        RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
//        respuestaComprobanteSRI.setEstado("4");

        String infoAdicional = "";
        try {
            ConsultaComprobante consultaAutorizacion = new ConsultaComprobante();
            try {
                consultaAutorizacion.obtenerRespuestaSRI(_clave_acceso_comprobante);
            } catch (Exception ex) {
                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.SEVERE, null, ex);
            }
            EnvioComprobante envioComprobante = new EnvioComprobante();
            String estado = "";
            boolean conexion_uno = false;
            boolean conexion_dos = false;
            boolean errorSRI = false;
            respuestaComprobanteSRI.setEstado("9");
            int intentos_uno = 0;
            while (!conexion_uno) {
                errorSRI = false;
                intentos_uno++;
                if (intentos_uno == $MAX_INTENTOS) {
                    break;
                }
                try {
                    estado = envioComprobante.envioComprobanteFirmado(comprobante_firmado);
                    conexion_uno = true;
                } catch (SocketException ex) {
                    System.err.println(ex);
                    errorSRI = true;

                } catch (SOAPFaultException ex) {
                    System.err.println(ex);

                    errorSRI = true;
                } catch (Exception ex) {
                    System.err.println(ex);
                    errorSRI = true;

                }

                if (errorSRI) {
                    Thread.sleep((long) 1000 * $RETARDO);
                }
            }
            if (conexion_uno) {
                if (estado.equals("RECIBIDO")) {
                    int intentos_dos = 0;
                    while (!conexion_dos) {
                        intentos_dos++;
                        if (intentos_dos == $MAX_INTENTOS + 1) {
                            break;
                        }
                        try {
                            respuestaComprobanteSRI = consultaAutorizacion.obtenerRespuestaSRI(_clave_acceso_comprobante);
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
                } else {
                    if (estado.equals("CA_PROCESAMIENTO")) {
                        respuestaComprobanteSRI.setEstado("10");
                        respuestaComprobanteSRI.setMensaje(Errores.$CA_Procesamiento);
                    } else {
                        respuestaComprobanteSRI.setEstado("11");
                        respuestaComprobanteSRI.setMensaje(Errores.$FallaBanderaEnvio + estado);
                    }
                }
            } else {
                if (errorSRI) {
                    respuestaComprobanteSRI.setEstado("6");
                    respuestaComprobanteSRI.setMensaje(Errores.$FallaErrorSRI);

                } else {
                    respuestaComprobanteSRI.setEstado("6");
                    respuestaComprobanteSRI.setMensaje(Errores.$FallaEnvioComprobante + " - Se han hecho " + (intentos_uno - 1) + " intentos.");
                }
            }
        } catch (InterruptedException ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }

    
    public static void main(String args[]) throws Exception {
     
        

    }
}
