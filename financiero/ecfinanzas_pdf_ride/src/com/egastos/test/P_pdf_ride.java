/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.test;

import com.itextpdf.text.DocumentException;
import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.egastos.pdfride.FactoriaPDF;
import com.egastos.utils.TransformadorBytes;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Ricardo Delgado
 */
public class P_pdf_ride {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, DocumentException, SQLException {
        // TODO code application logic here
        ComprobanteElectronico compro = TransformadorBytes.parseCompr("C:\\RicardoDelgado\\archivosSalida\\AYA_01_CLIE_10EF_001015_000000893_17072015_082729.xml");
//        byte[] comprobanteByte = TransformadorBytes.fileToByteArray(new File("C:\\RicardoDelgado\\archivosSalida\\IOKA_04_CLIE_001011_000000073_30042015_093802.xml"));
//        Comprobante compranbate = TransformadorBytes.byteToComprobante(comprobanteByte, "temp1.xml");
//
        FactoriaPDF factoria = new FactoriaPDF();
       byte[] pdfByte = factoria.construirPDFComprobante(compro, null, "XX", null);
        TransformadorBytes.writeToFile(pdfByte,"comprobante.pdf");
       // TransformadorBytes.writeToFile(pdfByte, "comprobante.pdf");

    }
}
