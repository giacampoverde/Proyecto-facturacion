/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.pdfride;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Clase que construye un PDF de una factura.
 *
 *@Ricardo Delgado
 */
public class ProductoFactura extends Producto {

    public ProductoFactura(ComprobanteElectronico comprobante, byte[] logoImgBytes, String numAutorizacion, String fechaAutorizacion) {
        super(comprobante, logoImgBytes, numAutorizacion, fechaAutorizacion);
    }

    /**
     * Función que se debe llamar para construir el PDF de una factura.
     *
     * @return retorna el pdf en bytes.
     * @throws IOException
     * @throws DocumentException
     * @throws SQLException
     */
    public byte[] crearPdf()
            throws IOException, DocumentException, SQLException {
        // step 1
        Document document = new Document();
        // step 2
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        // step 3
        document.open();
        // step 4
        PdfContentByte pdfContentByte = writer.getDirectContent();

        PdfPTable table = new PdfPTable(1);

        PdfPCell cell = null;
        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(2);
        cell.addElement(seccionValidezJuridica());
        table.addCell(cell);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setSpacingAfter(15f);

        document.add(table);

        table = new PdfPTable(2);
        table.setSplitLate(false);
        table.setWidthPercentage(100);
        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.addElement(ubicarLogo());
        cell.addElement(seccionInformacionEmpresa());
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.addElement(seccionInformacionTributaria(pdfContentByte));
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(2);
        cell.addElement(seccionInformacionCliente());
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setColspan(2);
        cell.addElement(seccionDetalleFacturaNotaCredito());
        cell.setPaddingBottom(10f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.addElement(new Paragraph());
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.addElement(seccionTotales());
        table.addCell(cell);

        document.add(table);
        PdfPTable tabla=new PdfPTable(1);
        tabla.setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla.addCell(seccionInformacionAdicional());
        document.add(tabla);

        document.close();

        return baos.toByteArray();

    }
}
