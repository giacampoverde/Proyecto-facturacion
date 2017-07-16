/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

/**
 *
 * @author
 */
/*
 * This file is made available under the terms of the LGPL licence.
 * This licence can be retrieved from http://www.gnu.org/copyleft/lesser.html.
 * The source remains the property of the YAWL Foundation.  The YAWL Foundation is a
 * collaboration of individuals and organisations who are committed to improving
 * workflow technology.
 */
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JDOMUtil {

    /**
     * *************************************************************************
     */
    public static String documentToString(Document doc) {
        if (doc == null) {
            return null;
        }
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        return out.outputString(doc);
    }

    /**
     * *************************************************************************
     */
    public static String elementToString(Element e) {
        if (e == null) {
            return null;
        }
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        return out.outputString(e);
    }

    /**
     * *************************************************************************
     */


    /**
     * *************************************************************************
     */
    public static Document stringToDocument(String s) {
        try {
            if (s == null) {
                return null;
            }
            return new SAXBuilder().build(new StringReader(s));
        } catch (JDOMException jde) {
            Logger.getLogger(JDOMUtil.class.getName()).log(Level.SEVERE, null, jde);
            jde.printStackTrace();
            return null;
        } catch (IOException ioe) {
            Logger.getLogger(JDOMUtil.class.getName()).log(Level.SEVERE, null, ioe);
            return null;
        }
    }

    /**
     * *************************************************************************
     */


    /**
     * *************************************************************************
     */


    /**
     * *************************************************************************
     */
    /**
     * saves a JDOM Document to a file
     */


    /**
     * *************************************************************************
     */


    /**
     * *************************************************************************
     */


    public static String decodeEscapes(String s) {
        if (s == null) {
            return s;
        }
        return s.replaceAll("&amp;", "&")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&apos;", "'");
    }

    /**
     * *************************************************************************
     */






    public static String obtenerVersionXML(String direccion_nombre_documento) {
        String version = null;
        try {

            SAXBuilder builder = new SAXBuilder();
            File file = new File(direccion_nombre_documento);
            Document documentJDOM = builder.build(file);
            Element elementoRaiz = documentJDOM.getRootElement();
            version = elementoRaiz.getAttributeValue("version");

        } catch (JDOMException ex) {
            Logger.getLogger(JDOMUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JDOMUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return version;
    }
     public static String obtenerVersionXML(File file) {
        String version = null;
        try {

            SAXBuilder builder = new SAXBuilder();
           
            Document documentJDOM = builder.build(file);
            Element elementoRaiz = documentJDOM.getRootElement();
            version = elementoRaiz.getAttributeValue("version");

        } catch (JDOMException ex) {
            Logger.getLogger(JDOMUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JDOMUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return version;
    }

    public static void main(String args[]) {
        
    }
}
