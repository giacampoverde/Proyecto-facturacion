/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utils;

import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionNotaCredito;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionRetencion;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class TransformadorBytes {

    static String tempDir = "C:/tmp/";

    public static ComprobanteElectronico parseCompr(String pathArchivo) {
        try {
            String tCompr = "";

            ImplementacionFactura facComprXML = new ImplementacionFactura();
            facComprXML.ObjetoComprobanteAXML(pathArchivo);
            tCompr = facComprXML.getInformacionTributariaComprobanteElectronico().getCodDoc();
            //factura
            switch (tCompr) {
                case "01":
                    return facComprXML;
                case "04":
                    ImplementacionNotaCredito nCredXML = new ImplementacionNotaCredito();
                    nCredXML.ObjetoComprobanteAXML(pathArchivo);
                    return nCredXML;
                case "07":
                    ImplementacionRetencion retXML = new ImplementacionRetencion();
                    retXML.ObjetoComprobanteAXML(pathArchivo);
                    return retXML;
                default:
                    return null;
            }
        } catch (Exception e) {
            System.out.println("Error:" + pathArchivo);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean writeToFile(byte[] array, String fileName) {
        boolean success = false;
        // String pathFile = carpeta_temporal + fileName;
        String pathFile = "C:\\pdf\\" + fileName;
        BufferedOutputStream bs = null;
        try {
            FileOutputStream fs = new FileOutputStream(new File(pathFile));
            bs = new BufferedOutputStream(fs);
            bs.write(array);
            bs.close();
            bs = null;
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bs != null) {
            try {
                bs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }
}
