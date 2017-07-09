package ec.discheca.firma.sri.produccion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.firma.constantes.Errores;
import ec.discheca.firma.firmaxades.AccesoCertificado;
import ec.discheca.firma.firmaxades.FirmaXades;
import ec.discheca.validadorXSD.ValidacionXSD;
import java.io.File;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.soap.SOAPFaultException;

/**
 *
* @Ricardo Delgado
 */
public class FacturacionElectronica {

    public static int $MAX_INTENTOS = 3;
    public static int $RETARDO = 1;

    public ec.discheca.firma.sri.produccion.RespuestaSRI autorizarComprobantesElectronicos(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, boolean enviar_a_firmar, String _version_xml) {

        ec.discheca.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        File comprobante_sin_firma = null;

        String infoAdicional = "";
        try {

            ValidacionXSD validadorXSD = new ValidacionXSD();
            ConsultaComprobantePro consulta = new ConsultaComprobantePro();
            EnvioComprobantePro envio = new EnvioComprobantePro();
            FirmaXades firmaXades = new FirmaXades();
            boolean banderavalidacionXSD = false;
            boolean banderaFirma = false;
            boolean banderaEnvio = false;
            boolean conexion_uno = false;
            boolean conexion_dos = false;
            boolean errorSRI = false;
            String estado = "";
            // solamente se hace la referencia al archivo que ya existe
            comprobante_sin_firma = new File(_direccion_archivo_a_firmar);
            if (comprobante_sin_firma != null) {
                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.INFO, "Comprobante a firmar: " + comprobante_sin_firma.getName());

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
                            Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con dos decimales del XSD.");
                        }
                    } else {
                        banderavalidacionXSD = validadorXSD.validarComprobantePorTipoVersion1110(comprobante_sin_firma, tipoDocumento);
                        try {
                            System.out.println("Validación con 4 decimales: " + validadorXSD.getMensajeResumenXSD());
                        } catch (Exception e) {
                            Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con 4 decimales del XSD.");
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
                                    if (intentos_uno == $MAX_INTENTOS + 1) {
                                        break;
                                    }
                                    try {

                                        estado = envio.envioComprobante(comprobante_firmado);
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
                                                respuestaComprobanteSRI = consulta.obtenerRespuestaSRI(_comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso());
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
                                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.WARNING, "EL COMPROBANTE NO SE PUDO FIRMAR: " + comprobante_sin_firma.getName());
                                respuestaComprobanteSRI.setMensaje(Errores.$FallaFirmaComprobante);
                            }
                        }
                    } else {
                        Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.WARNING, "ERROR VALIDACIÓN XSD, ARCHIVO: " + comprobante_sin_firma.getName() + " ERROR: " + validadorXSD.getMensajeResumenXSD());
                        respuestaComprobanteSRI.setInformacionAdicional("Clave de Acceso:" + _comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + "|" + "Error: " + validadorXSD.getMensajeResumenXSD());
                        respuestaComprobanteSRI.setMensaje(Errores.$ErrorXSDEstructura);
                    }
                }
            } else {
                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.WARNING, "NO EXISTE EL ARCHIVO A FIRMAR: " + _direccion_archivo_a_firmar);
                respuestaComprobanteSRI.setMensaje(Errores.$CreacionComprobante);
            }
        } catch (Exception ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }
  public ec.discheca.firma.sri.produccion.RespuestaSRI autorizarComprobantesElectronicosFloral(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, boolean enviar_a_firmar, String _version_xml) {

        ec.discheca.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        File comprobante_sin_firma = null;

        String infoAdicional = "";
        try {

            ValidacionXSD validadorXSD = new ValidacionXSD();
            ConsultaComprobantePro consulta = new ConsultaComprobantePro();
            EnvioComprobantePro envio = new EnvioComprobantePro();
            FirmaXades firmaXades = new FirmaXades();
            boolean banderavalidacionXSD = false;
            boolean banderaFirma = false;
            boolean banderaEnvio = false;
            boolean conexion_uno = false;
            boolean conexion_dos = false;
            boolean errorSRI = false;
            String estado = "";
            // solamente se hace la referencia al archivo que ya existe
            comprobante_sin_firma = new File(_direccion_archivo_a_firmar);
            if (comprobante_sin_firma != null) {
                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.INFO, "Comprobante a firmar: " + comprobante_sin_firma.getName());

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
                            Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con dos decimales del XSD.");
                        }
                    } else {
                        banderavalidacionXSD = validadorXSD.validarComprobantePorTipoVersion1110(comprobante_sin_firma, tipoDocumento);
                        try {
                            System.out.println("Validación con 4 decimales: " + validadorXSD.getMensajeResumenXSD());
                        } catch (Exception e) {
                            Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.INFO, "No tiene mensaje de resumen la validaci\u00f3n con 4 decimales del XSD.");
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
                                    if (intentos_uno == $MAX_INTENTOS + 1) {
                                        break;
                                    }
                                    try {

                                        estado = envio.envioComprobante(comprobante_firmado);
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
                                                respuestaComprobanteSRI = consulta.obtenerRespuestaSRI(_comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso());
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
                                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.WARNING, "EL COMPROBANTE NO SE PUDO FIRMAR: " + comprobante_sin_firma.getName());
                                respuestaComprobanteSRI.setMensaje(Errores.$FallaFirmaComprobante);
                            }
                        }
                    } else {
                        Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.WARNING, "ERROR VALIDACIÓN XSD, ARCHIVO: " + comprobante_sin_firma.getName() + " ERROR: " + validadorXSD.getMensajeResumenXSD());
                        respuestaComprobanteSRI.setInformacionAdicional("Clave de Acceso:" + _comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + "|" + "Error: " + validadorXSD.getMensajeResumenXSD());
                        respuestaComprobanteSRI.setMensaje(Errores.$ErrorXSDEstructura);
                    }
                }
            } else {
                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.WARNING, "NO EXISTE EL ARCHIVO A FIRMAR: " + _direccion_archivo_a_firmar);
                respuestaComprobanteSRI.setMensaje(Errores.$CreacionComprobante);
            }
        } catch (Exception ex) {

            respuestaComprobanteSRI.setMensaje(ex.getMessage());
        }

        return respuestaComprobanteSRI;
    }
    public ec.discheca.firma.sri.produccion.RespuestaSRI ConsultaComrpobanteElectronico(
              ComprobanteElectronico _comprobante) {

        ec.discheca.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
     

        try {

            
            ConsultaComprobantePro consulta = new ConsultaComprobantePro();
            EnvioComprobantePro envio = new EnvioComprobantePro();
           
            
            boolean banderaEnvio = false;
            boolean conexion_uno = false;
            boolean conexion_dos = false;
            boolean errorSRI = false;
            String estado = "";
            // solamente se hace la referencia al archivo que ya existe
         
                
                        respuestaComprobanteSRI.setEstado("9");
                 
                       

                               
                          
                           
                     
                                        int intentos_dos = 0;
                                        while (!conexion_dos) {
                                            intentos_dos++;
                                            if (intentos_dos == $MAX_INTENTOS + 1) {
                                                break;
                                            }
                                            try {
                                                respuestaComprobanteSRI = consulta.obtenerRespuestaSRI(_comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso());
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
 
     public ec.discheca.firma.sri.produccion.RespuestaSRI ConsultaComrpobanteclaveAcceso(
              String claveAcceso) {

        ec.discheca.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        
     

        try {

            
            ConsultaComprobantePro consulta = new ConsultaComprobantePro();
            
           
            
           
            boolean conexion_dos = false;
         
          
         
                
                        respuestaComprobanteSRI.setEstado("9");
                 
                       

                               
                          
                           
                     
                                        int intentos_dos = 0;
                                        while (!conexion_dos) {
                                            intentos_dos++;
                                            if (intentos_dos == $MAX_INTENTOS + 1) {
                                                break;
                                            }
                                            try {
                                                respuestaComprobanteSRI = consulta.obtenerRespuestaSRI(claveAcceso);
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

            // solamente se hace la referencia al archivo que ya existe
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
        } catch (Exception e) {

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

            // solamente se hace la referencia al archivo que ya existe
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
        } catch (Exception e) {

        }
        return respuestaComprobanteSRI;
    }
    
     public RespuestaSRI autorizarComprobantesElectronicosFirmados(byte[] comprobante_firmado, String _clave_acceso_comprobante) {

        RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");

        String infoAdicional = "";

        try {

            ConsultaComprobantePro consultaAutorizacion = new ConsultaComprobantePro();
            try {
                consultaAutorizacion.obtenerRespuestaSRI(_clave_acceso_comprobante);
            } catch (Exception ex) {
                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.SEVERE, null, ex);
            }
            EnvioComprobantePro envioComprobante = new EnvioComprobantePro();
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


    public static void main(String args[]) {

    }
}
