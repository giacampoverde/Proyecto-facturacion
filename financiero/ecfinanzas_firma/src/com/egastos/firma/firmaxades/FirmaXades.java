/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.firma.firmaxades;

import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
* @Ricardo Delgado
 */
public class FirmaXades {

    private DataToSign createDataToSign(String _archivo_a_firmar) {
        DataToSign datosAFirmar = new DataToSign();

        datosAFirmar.setXadesFormat(es.mityc.javasign.EnumFormatoFirma.XAdES_BES);

        datosAFirmar.setEsquema(XAdESSchemas.XAdES_132);
        datosAFirmar.setXMLEncoding("UTF-8");
        datosAFirmar.setEnveloped(true);
        datosAFirmar.addObject(new ObjectToSign(new InternObjectToSign("comprobante"), "contenido comprobante", null, "text/xml", null));
        datosAFirmar.setParentSignNode("comprobante");
        Document docToSign = null;
        try {
            docToSign = getDocument(_archivo_a_firmar);
            datosAFirmar.setDocument(docToSign);
        } catch (SAXException ex) {
            Logger.getLogger(FirmaXades.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FirmaXades.class.getName()).log(Level.SEVERE, null, ex);
        }

        return datosAFirmar;
    }

    private Document getDocument(String resource) throws SAXException, IOException {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        /* Para que no se altere el orden de los atributos */
        dbf.setNamespaceAware(true);
        try {
            File file = new File(resource);
            InputStream inputStream = new FileInputStream(file);
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(new InputSource(new InputStreamReader(inputStream, "UTF-8")));
        } catch (ParserConfigurationException ex) {
            System.err.println("Error al parsear el documento");
            ex.printStackTrace();

        } catch (SAXException ex) {
            System.err.println("Error al parsear el documento");
            ex.printStackTrace();

        } catch (IOException ex) {
            System.err.println("Error al parsear el documento");
            ex.printStackTrace();

        } catch (IllegalArgumentException ex) {
            System.err.println("Error al parsear el documento");
            ex.printStackTrace();

        }
        return doc;
    }

    public boolean firmar(AccesoCertificado _acceso_certificado, String _archivo_a_firmar, String _path_salida, String _nombre_archivo_salida, String _ruc_emisor) {

        CertUtils cert_utils = new CertUtils();
        boolean firmado = false;
        String ruc_certificado = "";
        //se valida que el certificado pertenezca a una de las 3 entidades de certificacion
        if (_acceso_certificado.getCertificate().getIssuerDN().getName().contains("SECURITY DATA S.A")) {
            ruc_certificado = cert_utils.getRucSecurityData(_acceso_certificado.getCertificate());
        } else if (_acceso_certificado.getCertificate().getIssuerDN().getName().contains("BANCO CENTRAL DEL ECUADOR")) {
            ruc_certificado = cert_utils.getRucBCE(_acceso_certificado.getCertificate());
        } else if (_acceso_certificado.getCertificate().getIssuerDN().getName().contains("ANF")) {
            ruc_certificado = cert_utils.getRuc(_acceso_certificado.getCertificate());
        }
        Pattern p = Pattern.compile(_ruc_emisor);
        Matcher m = p.matcher(ruc_certificado);
        if (m.find()) {
            /*
             * Creación del objeto que contiene tanto los datos a firmar como la
             * configuración del tipo de firma
             */
            DataToSign dataToSign = createDataToSign(_archivo_a_firmar);
            /*
             * Creación del objeto encargado de realizar la firma
             */
            FirmaXML firma = new FirmaXML();

            // Firmamos el documento
            Document docSigned = null;
            try {

                Object[] res = firma.signFile(_acceso_certificado.getCertificate(), dataToSign, _acceso_certificado.getPrivateKey(), _acceso_certificado.getProvider());
                docSigned = (Document) res[0];
                firmado = true;
            } catch (Exception ex) {
                System.err.println("Error realizando la firma");
                ex.printStackTrace();
            }
            if (firmado) {
                boolean escrito = false;

                // Guardamos la firma a un fichero en el home del usuario
                String filePath = _path_salida + File.separator + _nombre_archivo_salida;
                System.out.println("Archivo firmado guardado en: " + filePath);

                escrito = cert_utils.escribirDocumentoSeguro(docSigned, filePath);
                if (escrito) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            System.out.println("El ruc del certificado no corresponde al del ruc del emisor " + "RUC EMISOR:" + _ruc_emisor + " RUC CERTIFICADO:" + ruc_certificado);
            return false;
        }
    }

        }
