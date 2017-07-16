/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.servicios.ec;


import com.egastos.firma.sri.produccion.RespuestaSRI;
import com.egastos.utilidades.JDOMUtil;
import com.wspro.autorizacioncomprobantes.Autorizacion;
import com.wspro.autorizacioncomprobantes.Mensaje;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ArchivoRespuestaSRI {



    public void generarArchivoRespuesta(RespuestaSRI _respuesta, String _direccionArchivoDestino) {
        try {
            if (_respuesta != null) {
                Element respuestaAutorizacion = new Element("RespuestaAutorizacionComprobante");
                Document respuesta = new Document(respuestaAutorizacion);

                respuesta.setRootElement(respuestaAutorizacion);
                Document d = null;
                if (_respuesta.getClaveAccesoConsultada() != null) {
                    respuesta.getRootElement().addContent(new Element("claveAccesoConsultada").setText(_respuesta.getClaveAccesoConsultada()));
                }
                if (_respuesta.getNumeroComprobantes() != null) {
                    respuesta.getRootElement().addContent(new Element("numeroComprobantes").setText(_respuesta.getNumeroComprobantes()));
                }
                Element autorizaciones = new Element("autorizaciones");
                respuesta.getRootElement().addContent(autorizaciones);
                for (Autorizacion a : _respuesta.getAutorizaciones()) {
                    Element autorizacion = new Element("autorizacion");
                    if (a.getEstado() != null) {
                        autorizacion.addContent(new Element("estado").setText(a.getEstado()));
                    }
                    if (a.getNumeroAutorizacion() != null) {
                        autorizacion.addContent(new Element("numeroAutorizacion").setText(a.getNumeroAutorizacion()));
                    }
                    if (a.getFechaAutorizacion() != null) {
                        autorizacion.addContent(new Element("fechaAutorizacion").setText(a.getFechaAutorizacion().toXMLFormat()));
                    }
                    if (a.getAmbiente() != null) {
                        autorizacion.addContent(new Element("ambiente").setText(a.getAmbiente()));
                    }
                    if (a.getComprobante() != null) {
                        d = JDOMUtil.stringToDocument(a.getComprobante());
                        CDATA cdata = new CDATA("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + JDOMUtil.elementToString(d.getRootElement()));
                        Element comprobante = new Element("comprobante");
                        comprobante.addContent(cdata);
                        autorizacion.addContent(comprobante);
                    }
                    if (a.getMensajes() != null && a.getMensajes().getMensaje() != null && !a.getMensajes().getMensaje().isEmpty()) {
                        Element mensajes = new Element("mensajes");
                        autorizacion.addContent(mensajes);
                        for (Mensaje m : a.getMensajes().getMensaje()) {
                            Element mensaje = new Element("mensaje");
                            if (m.getIdentificador() != null) {
                                mensaje.addContent(new Element("identificador").setText(m.getIdentificador()));
                            }
                            if (m.getMensaje() != null) {
                                String mensajesSRI = m.getMensaje();
                                if (m.getInformacionAdicional() != null) {
                                    mensajesSRI += m.getInformacionAdicional();
                                }
                                mensaje.addContent(new Element("mensaje").setText(mensajesSRI));
                            }
                            if (m.getTipo() != null) {
                                mensaje.addContent(new Element("tipo").setText(m.getTipo()));
                            }
                            mensajes.addContent(mensaje);
                        }
                    }
                    autorizaciones.addContent(autorizacion);
                }
                Format format = Format.getPrettyFormat();
                XMLOutputter xmloutputter = new XMLOutputter(format);
                Document serializado = JDOMUtil.stringToDocument(JDOMUtil.decodeEscapes(JDOMUtil.documentToString(respuesta)));

                // Serializamos el document parseado  
                String docStr = xmloutputter.outputString(serializado);
                File arch = new File(_direccionArchivoDestino);
                OutputStream out = new FileOutputStream(arch);
                Closeable resource = out;
                Writer writer = null;
                try {
                    writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
                    resource = writer;
                    writer.write(docStr);
                    writer.flush();

                } finally {

                    resource.close();

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ArchivoRespuestaSRI.class.getName()).log(Level.SEVERE, "Error al generar el archivo de Respuesta del SRI", ex);          
            ex.printStackTrace();
        }
    }

    public String obtenerComprobanteCData(byte[] respuesta) {
        String comprobante_string = null;
        if (respuesta != null) {
            org.w3c.dom.Document document = null;

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.isNamespaceAware();
                factory.setIgnoringComments(true);
                DocumentBuilder builder = null;

                builder = factory.newDocumentBuilder();
                InputStream is = new ByteArrayInputStream(respuesta);
                document = builder.parse(is);
            } catch (Exception ex) {
                Logger.getLogger(ArchivoRespuestaSRI.class.getName()).log(Level.SEVERE, null, ex);

            }
            if (document != null) {
                NodeList nodoComprobante = document.getElementsByTagName("comprobante");
                for (int l = 0; l < nodoComprobante.getLength(); l++) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) nodoComprobante.item(l);
                    Node child = element.getFirstChild();
                    if (child instanceof CharacterData) {
                        CharacterData cd = (CharacterData) child;
                        comprobante_string = cd.getData();
                        break;
                    }
                }

            }
        }
        return comprobante_string;
    }

}
