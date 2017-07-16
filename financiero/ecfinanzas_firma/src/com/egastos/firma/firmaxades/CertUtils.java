/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.firma.firmaxades;

import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
* @Ricardo Delgado
 */
public class CertUtils {

    public KeyStore getKeyStore(String directorio_certificado, char[] passwd_key_store) {
        KeyStore ks = null;
        try {
            try {
                String extension = directorio_certificado.substring(directorio_certificado.lastIndexOf('.') + 1, directorio_certificado.length());
                if (extension.equals("ubr")) {
                    ks = KeyStore.getInstance("UBER", "BC");
                } else {
                    ks = KeyStore.getInstance("PKCS12");
                }
            } catch (NoSuchProviderException ex) {
                Logger.getLogger(CertUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
            ks.load(new FileInputStream(directorio_certificado), passwd_key_store);
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            Logger.getLogger(CertUtils.class.getName(), "Error al obtener el almacen de llaves del certificado");
        }
        return ks;
    }

    public String getAlias(KeyStore keyStore) {
        String alias = null;
        Enumeration nombres;
        try {
            nombres = keyStore.aliases();

            while (nombres.hasMoreElements()) {
                String tmpAlias = (String) nombres.nextElement();
                if (keyStore.isKeyEntry(tmpAlias)) {
                    alias = tmpAlias;
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return alias;
    }

    public boolean escribirDocumento(Document document, String directorio_destino) {

        boolean documento_escrito = false;
        try {
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(directorio_destino));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            transformer.transform(source, result);
            documento_escrito = true;
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return documento_escrito;
    }

    public boolean escribirDocumentoSeguro(Document document, String directorio_destino) {

        boolean documento_escrito = false;
        try {
            FileOutputStream fos = new FileOutputStream(directorio_destino);
            UtilidadTratarNodo.saveDocumentToOutputStream(document, fos);
           fos.flush();
           fos.close();
            
            documento_escrito = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CertUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CertUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return documento_escrito;
    }

    public Document getDocument(String resource) throws SAXException, IOException {
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

    public String getRuc(X509Certificate certificado) {
        byte[] oidB = null;
        String oidStr = null;
        try {
            oidB = certificado.getExtensionValue("1.3.6.1.4.1.37442.3.11");
            oidStr = new String(oidB);
        } catch (Exception e) {
            System.err.println("Error al obtener el RUC");
        }

        return oidStr;
    }

    public String getRucBCE(X509Certificate certificado) {
        byte[] oidB = null;
        String oidStr = null;
        try {
            oidB = certificado.getExtensionValue("1.3.6.1.4.1.37947.3.11");
            oidStr = new String(oidB);
            return oidStr;
        } catch (Exception e) {
            return null;
        }
    }
    
      public String getRucSecurityData(X509Certificate certificado) {
        byte[] oidB = null;
        String oidStr = null;
        try {
            oidB = certificado.getExtensionValue("1.3.6.1.4.1.37746.3.11");
            oidStr = new String(oidB);
            return oidStr;
        } catch (Exception e) {
            return null;
        }
    }

}
