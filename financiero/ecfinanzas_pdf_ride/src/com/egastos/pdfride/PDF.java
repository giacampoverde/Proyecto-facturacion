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
 *
 * @author Ricardo Delgado
 */
public class PDF {

    /**
     * Construye el la visualización del comprobante en PDF.
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
        String fechaAutorizacionSRI = "";

        if (fechaAutorizacion != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            fechaAutorizacionSRI = simpleDateFormat.format(fechaAutorizacion);
        } else {
            fechaAutorizacionSRI = "No Disponible";
        }
        if (numAutorizacion == null) {
            numAutorizacion = "No Disponible";
        } else if (numAutorizacion.equals("")) {
            numAutorizacion = "No Disponible";
        }
        switch (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()) {
            case Valores.$FACTURA:
                ProductoFactura productoFactura = new ProductoFactura(comprobante, logoByte, numAutorizacion, fechaAutorizacionSRI);
                pdfByte = productoFactura.crearPdf();
                break;
            case Valores.$NOTA_CREDITO:
                ProductoNotaCredito productoNotaCredito = new ProductoNotaCredito(comprobante, logoByte, numAutorizacion, fechaAutorizacionSRI);
                pdfByte = productoNotaCredito.crearPdf();
                break;
          
            case Valores.$COMPROBANTES_RETENCION:
                ProductoRetencion productoRetencion = new ProductoRetencion(comprobante, logoByte, numAutorizacion, fechaAutorizacionSRI);
                pdfByte = productoRetencion.crearPdf();
                break;
           
            default:
                return null;
        }
        return pdfByte;
    }

}
