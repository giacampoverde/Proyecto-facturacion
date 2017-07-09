/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.TransformadorArchivos;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;

import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOComprobanteElectronico;
import ec.discheca.serviciofacturacion.AlmacenamientoComprobanteElectronico;
import ec.discheca.utilidades.MensajesPrimefaces;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.primefaces.event.FileUploadEvent;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Ricardo
 */
@ManagedBean
@ViewScoped
public class BeanDescargaClaveAcceso {

    List<String[]> listacontenedora = new ArrayList<String[]>();
    File txtContactos;
    Integer contadordeClavesAcceso;
    Integer contadordeInsertado;
    List<String> correos = new ArrayList<String>();

    public BeanDescargaClaveAcceso() {
//        if (Valores.ambiente.equals("1")) {
//            correos.add(Valores.correo_ebox);
//        } else {
//            correos.add("mromero@ccdc-ec.com");
//        }
    }

    public void clavesAcceso(FileUploadEvent event) throws Exception {
        ControlSesion ms = new ControlSesion();

        try {
            txtContactos = new File(event.getFile().getFileName());
            FileOutputStream fos = new FileOutputStream(txtContactos);
            fos.write(event.getFile().getContents());
            fos.flush();
            fos.close();
//            liecturAarchivoTxt();
        } catch (IOException ex) {
        }

    }

    public void xml(FileUploadEvent event) throws Exception {
        ControlSesion ms = new ControlSesion();

        try {
            txtContactos = new File(event.getFile().getFileName());
            FileOutputStream fos = new FileOutputStream(txtContactos);
            fos.write(event.getFile().getContents());
            fos.flush();
            fos.close();
            lecturaxml(event);
        } catch (IOException ex) {
        }

    }

    public void lecturaxml(FileUploadEvent event) {
        BufferedWriter bw = null;
        String sFichero = null;
        File fichero = null;
        String claveDeAcceso = "";
        String fileName;
        fileName = "" + txtContactos.getName();
        String[] validarClave = fileName.split("\\.");
        int validacionenviocorreo = 0;
        Document documentotrasformado = null;
        if (validarClave.length > 0) {
            if (validarClave[0].length() == 49) {
                claveDeAcceso = validarClave[0];
            }
        }
        if (claveDeAcceso.equals("")) {
            try {
                DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                File archivo = new File(Valores.rutaRecepcion + "xmldescargar.xml");
                NodeList respuesta1 = null;
                String respuesta = null;
                Document xml = null;
                try {
                    OutputStream out = new FileOutputStream(archivo);
                    try {
                        out.write(event.getFile().getContents());
                    } catch (IOException ex) {
                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        out.close();
                    } catch (IOException ex) {
                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        xml = dBuilder.parse(archivo);
                    } catch (SAXException ex) {
                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    String validacionnombre = fileName;

                    respuesta1 = xml.getElementsByTagName("claveAcceso");
                    String claveclaveDeAccesosinsri = null;

                    if (respuesta1 != null && respuesta1.getLength() > 0) {
                        claveclaveDeAccesosinsri = respuesta1.item(0).getTextContent();
                    }
                    if (xml != null && claveclaveDeAccesosinsri == null) {
                        NodeList nodoComprobante = xml.getElementsByTagName("comprobante");
                        for (int l = 0; l < nodoComprobante.getLength(); l++) {
                            org.w3c.dom.Element element = (org.w3c.dom.Element) nodoComprobante.item(l);
                            Node child = element.getFirstChild();
                            if (child instanceof CharacterData) {
                                CharacterData cd = (CharacterData) child;
                                respuesta = cd.getData();
                                break;
                            }
                        }
                    } else {
                        claveDeAcceso = claveclaveDeAccesosinsri;
                    }
                    try {
                        if (respuesta != null) {
                            documentotrasformado = convertStringToDocument(respuesta);
                            if (documentotrasformado != null) {
                                claveDeAcceso = documentotrasformado.getElementsByTagName("claveAcceso").item(0).getTextContent();

                            } else {
                                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error en el documento");

                                System.out.println("error en el documento" + fileName);
                            }
                        } else {
                            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error en el documento");
                            System.out.println("error en el documento" + fileName);
                        }

                        if (claveDeAcceso != null && !claveDeAcceso.equals("")) {
                            validacionenviocorreo = 1;
                            System.out.println(fileName + "clave de acceso " + claveDeAcceso);

                            ComprobanteElectronico c = null;
                            ec.discheca.firma.sri.produccion.ConsultaComprobantePro consulta_pro = new ec.discheca.firma.sri.produccion.ConsultaComprobantePro();
                            ec.discheca.firma.sri.produccion.RespuestaSRI respuestaSri = null;
                            try {
                                respuestaSri = consulta_pro.obtenerRespuestaSRI(claveDeAcceso);
                            } catch (Exception ex) {
                                Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (respuestaSri != null && !respuestaSri.getEstado().equals("5")) {
                                System.out.println(respuestaSri.getClaveAccesoConsultada());
                                try {
                                    c = TransformadorArchivos.byteCompr(respuestaSri.getComprobante().getBytes("UTF-8"), Valores.serv_directorio_xmls + respuestaSri.getClaveAccesoConsultada());
                                } catch (UnsupportedEncodingException ex) {
                                    Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                byte[] respuesta_byte = TransformadorArchivos.archArrayB(new File(Valores.serv_directorio_xmls + "xmldescargar" + ".xml"));
                                DAOComprobanteElectronico comprobantes = null;
                                try {
                                    comprobantes = new DAOComprobanteElectronico();
                                } catch (Exception ex) {
                                    Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                ec.discheca.modelo.ComprobanteElectronico respuestarepetido = null;
                                try {
                                    respuestarepetido = comprobantes.obtenerComprobatePorCA(claveDeAcceso);
                                } catch (Exception ex) {
                                    Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                if (respuestarepetido == null) {
                                    AlmacenamientoComprobanteElectronico ace = new AlmacenamientoComprobanteElectronico();
                                    if (ace.guardarComprobanteElectronico(respuestaSri, c, c.getInformacionTributariaComprobanteElectronico().getCodDoc(), "direccion","nombre")) {

                                        System.out.println("comprobante con clave de acceso " + respuestaSri.getClaveAccesoConsultada() + " insertado correctamnete");
                                        MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Comprobante almacenado exitosamente");

                                    } else {
                                        System.out.println("ERROR AL INSERTAR COMPROBANTE CON CLAVE DE ACCESO " + respuestaSri.getClaveAccesoConsultada() + " linea ");
                                       MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al insertar el comprobante");
                                    }
                                } else {
                                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Este comprobante ya se ecuentra almacenado");
                                }
                            } else {
                                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Sri no tiene una respuesta");
                                System.out.println("Sri no tiene una respusta");
                            }
                        } else {
                            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error en el comprobante");
                            System.out.println("Clave de acceso no encontrada " + fileName);

                        }
                    } catch (IllegalAccessError ex) {

                        System.out.println("comprobante no encontrado en respuesta" + ex);
                    }
                    ComprobanteElectronico c = null;

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ParserConfigurationException ex) {
                Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

            if (claveDeAcceso != null && !claveDeAcceso.equals("")) {
                validacionenviocorreo = 1;
                System.out.println(fileName + "clave de acceso " + claveDeAcceso);

                ComprobanteElectronico c = null;
                ec.discheca.firma.sri.produccion.ConsultaComprobantePro consulta_pro = new ec.discheca.firma.sri.produccion.ConsultaComprobantePro();
                ec.discheca.firma.sri.produccion.RespuestaSRI respuestaSri = null;
                try {
                    respuestaSri = consulta_pro.obtenerRespuestaSRI(claveDeAcceso);
                } catch (Exception ex) {
                    Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (respuestaSri != null && !respuestaSri.getEstado().equals("5")) {
                    System.out.println(respuestaSri.getClaveAccesoConsultada());
                    try {
                        c = TransformadorArchivos.byteCompr(respuestaSri.getComprobante().getBytes("UTF-8"), Valores.serv_directorio_xmls + respuestaSri.getClaveAccesoConsultada());
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    byte[] respuesta_byte = TransformadorArchivos.archArrayB(new File(Valores.serv_directorio_xmls + "xmldescargar" + ".xml"));
                    DAOComprobanteElectronico comprobantes = null;
                    try {
                        comprobantes = new DAOComprobanteElectronico();
                    } catch (Exception ex) {
                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   ec.discheca.modelo.ComprobanteElectronico respuestarepetido = null;
                    try {
                        respuestarepetido = comprobantes.obtenerComprobatePorCA(claveDeAcceso);
                    } catch (Exception ex) {
                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (respuestarepetido == null) {
                        AlmacenamientoComprobanteElectronico ace = new AlmacenamientoComprobanteElectronico();
                        if (ace.guardarComprobanteElectronico(respuestaSri, c, c.getInformacionTributariaComprobanteElectronico().getCodDoc(),"direccion","nombre")) {

                            System.out.println("comprobante con clave de acceso " + respuestaSri.getClaveAccesoConsultada() + " insertado correctamnete");
                            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Comprobante almacenado exitosamente");

                        } else {
                            System.out.println("ERROR AL INSERTAR COMPROBANTE CON CLAVE DE ACCESO " + respuestaSri.getClaveAccesoConsultada() + " linea ");
                           MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al insertar el comprobante");
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Este comprobante ya se ecuentra almacenado");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Sri no tiene una respuesta");
                    System.out.println("Sri no tiene una respusta");
                }
            } else {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error en el comprobante");
                System.out.println("Clave de acceso no encontrada " + fileName);

            }

        }
    }

//    public void liecturAarchivoTxt() {
//        BufferedWriter bw = null;
//        String sFichero = null;
//        File fichero = null;
////        Correo a = new Correo();
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
//
//            } catch (FileNotFoundException ex) {
//
//            }
//            BufferedReader br = new BufferedReader(fr);
//            String texto = "";
//            contadordeClavesAcceso = 0;
//            contadordeInsertado = 0;
//            try {
//                while ((texto = br.readLine()) != null) {
//                    System.out.println("////////////////////////////////////////////////");
//                    contadordeClavesAcceso++;
//                    System.out.println("clave de acceso procesada: " + texto);
//
//                    ec.bigdata.firmamultihilo.sri.produccion.ConsultaComprobantePro consulta_pro = new ec.bigdata.firmamultihilo.sri.produccion.ConsultaComprobantePro();
//                    ec.bigdata.firmamultihilo.sri.produccion.RespuestaSRI respuestaSri = null;
//                    try {
//                        respuestaSri = consulta_pro.obtenerRespuestaSRI(texto);
//                    } catch (Exception ex) {
//                        Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
//                        System.out.println("sin respuesta Sri clave acceso " + texto + " linea " + contadordeClavesAcceso);
//                    }
//
//                    if (respuestaSri.getEstado().equals("1") || respuestaSri.getEstado().equals("2")) {
//
//                        ComprobanteElectronico c = TransformadorArchivos.byteCompr(respuestaSri.getComprobante().getBytes("UTF-8"), Valores.serv_directorio_xmls + respuestaSri.getClaveAccesoConsultada());
//
//                        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//                        File archivo = new File(Valores.serv_directorio_xmls + "xmldescargar.xml");
//                        NodeList respuesta1 = null;
//
//                        Document xml = null;
//
//                        OutputStream out = new FileOutputStream(archivo);
//                        try {
//                            out.write(respuestaSri.getComprobante().getBytes("UTF-8"));
//                        } catch (IOException ex) {
//                            Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        try {
//                            out.close();
//                        } catch (IOException ex) {
//                            Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        try {
//                            xml = dBuilder.parse(archivo);
//                        } catch (SAXException ex) {
//                            Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (IOException ex) {
//                            Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//
//                        byte[] respuesta_byte = TransformadorArchivos.archArrayB(new File(Valores.serv_directorio_xmls + "xmldescargar" + ".xml"));
//                        //verificarsiyaexiste
//                        DAOComprobante comprobantes = new DAOComprobante();
//                        ec.bigdata.repositorio.modelo.Comprobante respuestarepetido = null;
//                        respuestarepetido = comprobantes.obtenerComprobatePorCA(texto);
//                        if (respuestarepetido == null) {
//                            if (HandlerRepositorio.insertarComprobante(respuestaSri, c, c.getInformacionTributariaComprobanteElectronico().getCodDoc(), respuesta_byte, null, null)) {
//                                contadordeInsertado++;
//                                System.out.println("comprobante con clave de acceso " + respuestaSri.getClaveAccesoConsultada() + " insertado correctamnete" + " linea " + contadordeClavesAcceso);
//                                // }
//                            } else {
//                                a.enviarMailNotificacion(correos, "ERROR AL INSERTAR COMPROBANTE CON CLAVE DE ACCESO " + respuestaSri.getClaveAccesoConsultada(), "Error en carga masiva claves de acceso");
//                                System.out.println("ERROR AL INSERTAR COMPROBANTE CON CLAVE DE ACCESO " + respuestaSri.getClaveAccesoConsultada() + " linea " + contadordeClavesAcceso);
//                            }
//                        } else {
//                            System.out.println("EL COMPROBANTE CON CLAVE DE ACCESO " + respuestaSri.getClaveAccesoConsultada() + "YA ESTA INSERTADO" + "  linea" + contadordeClavesAcceso);
//                        }
//                    } else {
//
//                        a.enviarMailNotificacion(correos, "ERROR SIN RESPUESTA SRI ESTADO DIFERENTE DE 1 Y 2 clave de acceso: " + texto, "Error en carga masiva claves de acceso");
//                        System.out.println("ERROR SIN RESPUESTA SRI ESTADO DIFERENTE DE 1 Y 2 " + texto + " linea " + contadordeClavesAcceso);
//                    }
//                }
//                mostrarMensaje(FacesMessage.SEVERITY_INFO, "Proceso Completado Con Exito ");
//                System.out.println("numero de claves de Acceso procesadas " + contadordeClavesAcceso);
//                System.out.println("numero de claves de Acceso guardadas " + contadordeInsertado);
//            } catch (IOException ex) {
//                //  Logger.getLogger(BeanCargaClientes.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (Exception ex) {
//                Logger.getLogger(BeanDescargaClaveAcceso.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            try {
//                br.close();
//            } catch (IOException ex) {
//                // Logger.getLogger(BeanCargaClientes.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//
//    }

    public void mostrarMensaje(FacesMessage.Severity severityMessage, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severityMessage, "Mensaje:", mensaje));
    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
