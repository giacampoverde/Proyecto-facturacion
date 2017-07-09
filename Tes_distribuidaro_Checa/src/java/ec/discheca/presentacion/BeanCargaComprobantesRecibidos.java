///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package ec.bigdata.tcs.presentacion;
//
//import ec.bigdata.tcs.configuracion.Configuraciones;
//import ec.bigdata.tcs.configuracion.ControlSesion;
//import ec.bigdata.tcs.dao.DAOComprobanteElectronico;
//import ec.bigdata.tcs.modelo.ComprobanteElectronico;
//import ec.bigdata.tcs.serviciofacturacion.AlmacenamientoComprobanteElectronico;
//import ec.bigdata.tcs.utilidades.Correo;
//
//import ec.bigdata.tcs.utilidades.MensajesPrimefaces;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.Serializable;
//import java.io.StringReader;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import org.primefaces.event.FileUploadEvent;
//import org.w3c.dom.CharacterData;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
///**
// *
// * @author Usuario
// */
//@ManagedBean
//@ViewScoped
//public class BeanCargaComprobantesRecibidos implements Serializable {
//
//    File txtContactos;
//    List<String[]> listacontenedora = new ArrayList<String[]>();
//    ;
//    Integer contadordeClavesAcceso;
//    Integer contadordeInsertado;
//    List<String> correos;
//
//    public void cargaxml(FileUploadEvent event) throws Exception {
//        ControlSesion ms = new ControlSesion();
//
//        try {
//            txtContactos = new File(event.getFile().getFileName());
//            FileOutputStream fos = new FileOutputStream(txtContactos);
//            fos.write(event.getFile().getContents());
//            fos.flush();
//            fos.close();
//            lecturaxml(event.getFile().getContents());
//        } catch (IOException ex) {
//        }
//
//    }
//
//    public void cargatxtclavesAcceso(FileUploadEvent event) throws Exception {
//        correos = new ArrayList<String>();
////        if (!Configuraciones.VALOR_CORREORECEPCIONERRORES.equals("")) {
////            correos.add(Configuraciones.VALOR_CORREORECEPCIONERRORES);
////        }
////        if (!Configuraciones.VALOR_CORREORECEPCIONERRORES2.equals("")) {
////            correos.add(Configuraciones.VALOR_CORREORECEPCIONERRORES2);
////        }
////        if (!Configuraciones.VALOR_CORREORECEPCIONERRORES3.equals("")) {
////            correos.add(Configuraciones.VALOR_CORREORECEPCIONERRORES3);
////        }
//        correos.add("ricardo_telcomp@hotmail.com");
//        ControlSesion ms = new ControlSesion();
//
//        try {
//            txtContactos = new File(event.getFile().getFileName());
//            FileOutputStream fos = new FileOutputStream(txtContactos);
//            fos.write(event.getFile().getContents());
//            fos.flush();
//            fos.close();
//            lecturaArchivoTxt();
//        } catch (IOException ex) {
//        }
//
//    }
//
//    public void lecturaArchivoTxt() {
//        try {
//            boolean almacenado = false;
//            DAOComprobanteElectronico daocomrpobante = new DAOComprobanteElectronico();
//            String detallesError = " <br> ";
//            Correo envio = new Correo("1");
//            int contadorclavesAcceso = 0;
//            boolean respuestaAlprocesar = false;
//            List<String> lineastxt = obtenerlineasAccesoTxt();
//            for (int i = 0; i < lineastxt.size(); i++) {
//                String claveAcceso = validarClaveAccesoNombre(lineastxt.get(i) + ".");
//                if (!claveAcceso.equals("")) {
//                    Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "lina del archivo txt a procesar. " + i);
//                    Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "claveAcceso a procesar." + claveAcceso);
//                    ComprobanteElectronico respuestacomprobante = daocomrpobante.obtenerComprobatePorCA(claveAcceso);
//                    if (respuestacomprobante == null) {
//                        respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveAcceso);
//                    } else {
//                        almacenado = true;
//                        detallesError = detallesError + " YA SE ENCUENTRA ALMACENADO EL COMRPOBANTE CON CLAVE DE ACCESO: " + claveAcceso + " "+" LINEA: "+i+"<br>";
////                        envio.enviarMailNotificacion(correos, "YA SE ENCUENTRA ALMACENADO EL COMRPOBANTE CON CLAVE DE ACCESO: " + claveAcceso, "Error en carga masiva claves de acceso Comprobantes Recibidos");
////                        MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El comprobante ya se encuentra almacenado .");
//                    }
//                    if (!almacenado) {
//                        if (!respuestaAlprocesar) {
//                            contadorclavesAcceso++;
//                            //enviar correo la clave de acceso no se pudo almacenar
//                            detallesError = detallesError + " ERROR AL INSERTAR COMPROBANTE CON CLAVE DE ACCESO: " + claveAcceso + "  "+" LINEA: "+i+"<br>";
//////                            envio.enviarMailNotificacion(correos, "ERROR AL INSERTAR COMPROBANTE CON CLAVE DE ACCESO " + claveAcceso, "Error en carga masiva claves de acceso Comprobantes Recibidos");
//                            // MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Almacenado Correctamente.");
//                        } else {
//                            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "Error al almacenar el archivo.");
//                        }
//                    }
////                else {
////                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El Comprobante no se pudo amlacenar.");
////                }
//                } else {
//                    detallesError = detallesError + " EL CONTENIDO NO CORRESPONDE A UNA CLAVE DE ACCESO  "+" LINEA: "+i+"<br>";
//                    Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "La linea no corresponde a una clave de acceso .");
//                }
//            }
//            if (lineastxt.size() > 0) {
//                if (contadorclavesAcceso == lineastxt.size()) {
//                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Archivo Procesado exitosamente.");
//                } else {
//                    envio.enviarMail(correos, detallesError, "Error en carga masiva claves de acceso Comprobantes Recibidos");
//                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se proceso todo el contenido del txt, el detalle de los errores fue eviando a su correo.");
//                }
//            } else {
//                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El Archivo esta vacio.");
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public List<String> obtenerlineasAccesoTxt() {
//        List<String> clavesAcceso = new ArrayList<String>();
//        BufferedWriter bw = null;
//        String sFichero = null;
//        File fichero = null;
//        Correo a = new Correo("1");
//
//        String fileName;
//        fileName = "" + new Date().toString();
//
//        listacontenedora = new ArrayList<String[]>();
//        Boolean respuesta = Boolean.FALSE;
//        if (txtContactos != null) {
//            //sFichero = Valores.carpetaparaclienteserroneos + "ERR - " + fileName + txtContactos.getName();
//            sFichero = "" + "ERR - " + fileName + txtContactos.getName();
//            fichero = new File(sFichero);
//
//        }
//        if (txtContactos != null && !txtContactos.getName().equals("")) {
//
//            InputStreamReader fr = null;
//            try {
//                fr = new InputStreamReader(new FileInputStream(txtContactos), "UTF8");
//            } catch (UnsupportedEncodingException ex) {
//                Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "Error en la lectura txt.");
//            } catch (FileNotFoundException ex) {
//
//            }
//            BufferedReader br = new BufferedReader(fr);
//            String texto = "";
//
//            try {
//                while ((texto = br.readLine()) != null) {
//                    clavesAcceso.add(texto);
//
//                }
//            } catch (IOException ex) {
//                Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "Error en la lectura txt.");
//                Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return clavesAcceso;
//    }
//
//    public void lecturaxml(byte[] evento) {
//        try {
//            String claveDeAcceso = "";
//            boolean respuestaAlprocesar = false;
//            boolean yaalmacenado = false;
//            DAOComprobanteElectronico daocomrpobante = new DAOComprobanteElectronico();
//            claveDeAcceso = validarClaveAccesoNombre(txtContactos.getName());
//            if (claveDeAcceso.equals("")) {
//                claveDeAcceso = obtenerclaveDeAccesoXml(evento);
//                if (!claveDeAcceso.equals("")) {
//
//                    Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "clave De Acceso a Procesar." + claveDeAcceso);
//
//                    ComprobanteElectronico respuestacomprobante = daocomrpobante.obtenerComprobatePorCA(claveDeAcceso);
//                    if (respuestacomprobante == null) {
//                        respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveDeAcceso);
//                    } else {
//                        yaalmacenado = true;
//                        MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El comprobante ya se encuentra almacenado .");
//                    }
//
//                }
//            } else {
//                Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "clave De Acceso a Procesar." + claveDeAcceso);
//                ComprobanteElectronico respuestacomprobante = daocomrpobante.obtenerComprobatePorCA(claveDeAcceso);
//                if (respuestacomprobante == null) {
//                    respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveDeAcceso);
//                } else {
//                    yaalmacenado = true;
//                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El comprobante ya se encuentra almacenado .");
//                }
//            }
//            if (!yaalmacenado) {
//                if (respuestaAlprocesar) {
//                    Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "Almacenado Correctamente.");
//                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Almacenado Correctamente.");
//                } else {
//                    Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.INFO, "El Comprobante no se pudo amlacenar.");
//                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El Comprobante no se pudo amlacenar.");
//                }
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public static boolean ProcesoAlmacenamientoClaveAcceso(String ClaveAcceso) {
//        boolean guardada = false;
//        try {
//
//            AlmacenamientoComprobanteElectronico ac = new AlmacenamientoComprobanteElectronico();
//            guardada = ac.guardarPorClaveDeAcceso(ClaveAcceso);
//
//        } catch (Exception ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return guardada;
//    }
//
//    public static String validarClaveAccesoNombre(String nombreArchivo) {
//        String claveAcceso = "";
//        String[] validarClave = nombreArchivo.split("\\.");
//        if (validarClave.length > 0) {
//
//            if (validarClave[0].length() == 49 && solonumeros(validarClave[0])) {
//                claveAcceso = validarClave[0];
//            }
//        }
//        return claveAcceso;
//    }
//
//    public static boolean solonumeros(String claveAcceso) {
//
//        if (!(claveAcceso.matches("^[0-9]{49}$"))) {
//            return false;
//        }
//        return true;
//    }
//
//    public static String obtenerclaveDeAccesoXml(byte[] part) {
//        File archivo = new File("xmldescargar.xml");
//        Document documentotrasformado = null;
//        String claveAcceso = "";
//
//        NodeList respuesta1 = null;
//        String respuesta = null;
//        Document xml = null;
//        OutputStream out;
//        try {
//            out = new FileOutputStream(archivo);
//            out.write(part);
//            out.close();
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, "No se pudo leer el comprobante.");
//            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
//        } catch (Exception ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, "No se pudo leer el comprobante.");
//            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
//        }
//
//        try {
//
//            DocumentBuilder dBuilder = null;
//            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            xml = dBuilder.parse(archivo);
//
//        } catch (ParserConfigurationException ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
//        } catch (SAXException ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
//        } catch (IOException ex) {
//            Logger.getLogger(BeanCargaComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
//            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
//        }
//        if (xml != null) {
//            respuesta1 = xml.getElementsByTagName("claveAcceso");
//        }
//        String claveclaveDeAccesosinsri = null;
//
//        if (respuesta1 != null && respuesta1.getLength() > 0) {
//            claveclaveDeAccesosinsri = respuesta1.item(0).getTextContent();
//        }
//        if (xml != null && claveclaveDeAccesosinsri == null) {
//            NodeList nodoComprobante = xml.getElementsByTagName("comprobante");
//            for (int l = 0; l < nodoComprobante.getLength(); l++) {
//                org.w3c.dom.Element element = (org.w3c.dom.Element) nodoComprobante.item(l);
//                Node child = element.getFirstChild();
//                if (child instanceof CharacterData) {
//                    CharacterData cd = (CharacterData) child;
//                    respuesta = cd.getData();
//                    break;
//                }
//            }
//        } else {
//            claveAcceso = claveclaveDeAccesosinsri;
//        }
//        if (respuesta != null) {
//            documentotrasformado = convertStringToDocument(respuesta);
//            if (documentotrasformado != null) {
//                claveAcceso = documentotrasformado.getElementsByTagName("claveAcceso").item(0).getTextContent();
//
//            } else {
//
//                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Erro en el Documento.");
//            }
//        }
//        return claveAcceso;
//
//    }
//
//    private static Document convertStringToDocument(String xmlStr) {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder;
//        try {
//            builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
//            return doc;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
