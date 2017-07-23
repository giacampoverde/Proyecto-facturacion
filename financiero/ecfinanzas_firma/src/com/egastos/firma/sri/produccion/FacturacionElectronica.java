package com.egastos.firma.sri.produccion;
import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.egastos.firma.constantes.Errores;
import com.egastos.firma.firmaxades.AccesoCertificado;
import com.egastos.firma.firmaxades.FirmaXades;
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

    public com.egastos.firma.sri.produccion.RespuestaSRI autorizarComprobantesElectronicos(String _direccion_archivo_a_firmar,
            String _path_salida, String _nombre_archivo_salida, ComprobanteElectronico _comprobante,
            AccesoCertificado _acceso_certificado, String _ruc_emisor, boolean enviar_a_firmar, String _version_xml) {

        com.egastos.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
        respuestaComprobanteSRI.setEstado("4");
        String tipoDocumento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        File comprobante_sin_firma = null;

        String infoAdicional = "";
        try {
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


                Logger.getLogger(FacturacionElectronica.class.getName()).log(Level.INFO, "Comprobante a firmar: " + comprobante_sin_firma.getName());

         
             

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
 
    public com.egastos.firma.sri.produccion.RespuestaSRI ConsultaComrpobanteElectronico(
              ComprobanteElectronico _comprobante) {

        com.egastos.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
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
 
     public com.egastos.firma.sri.produccion.RespuestaSRI ConsultaComrpobanteclaveAcceso(
              String claveAcceso) {

        com.egastos.firma.sri.produccion.RespuestaSRI respuestaComprobanteSRI = new RespuestaSRI();
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
