/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.pdfride;

import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que gestiona toda la construcción del PDF.
 * @author 
 */
public class FactoriaPDF {

    /**
     * Genera el PDF usando la información del Objeto ComprobanteElectronico
     *
     * @param comprobante el comprobante electrónico
     * @param logoByte el logo de la empresa que emitió el comprobante
     * @param numAutorizacion número de autorización del comprobante
     * @param fechaAutorizacion fecha de autorización del comprobante
     * @return
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public byte[] construirPDFComprobante(ComprobanteElectronico comprobante, byte[] logoByte, String numAutorizacion, Date fechaAutorizacion) throws IOException, DocumentException, SQLException {

        byte[] pdfByte = null;
        String fAutSRI = "";

        if (fechaAutorizacion != null) {
            SimpleDateFormat simpDForm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            fAutSRI = simpDForm.format(fechaAutorizacion);
        } else {
            fAutSRI = "No Disponible";
        }
        if (numAutorizacion == null) {
            numAutorizacion = "No Disponible";
        } else if (numAutorizacion.equals("")) {
            numAutorizacion = "No Disponible";
        }
        switch (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()) {
            case Valores.$FACTURA:
                ProductoFactura prodFac = new ProductoFactura(comprobante, logoByte, numAutorizacion, fAutSRI);
                pdfByte = prodFac.crearPdf();
                break;
            case Valores.$NOTA_CREDITO:
                ProductoNotaCredito prodNCred = new ProductoNotaCredito(comprobante, logoByte, numAutorizacion, fAutSRI);
                pdfByte = prodNCred.crearPdf();
                break;
            
            case Valores.$COMPROBANTES_RETENCION:
                ProductoRetencion prodRetencion = new ProductoRetencion(comprobante, logoByte, numAutorizacion, fAutSRI);
                pdfByte = prodRetencion.crearPdf();
                break;
           
            default:
                return null;
        }
        return pdfByte;
    }
}
