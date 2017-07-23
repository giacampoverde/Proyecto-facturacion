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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Clase que construye el PDF de una nota de crédito.
 *
 * @Ricardo Delgado 
 */
public class ProductoNotaCredito extends Producto {

    public ProductoNotaCredito(ComprobanteElectronico comprobante, byte[] logoImgBytes, String numAutorizacion, String fechaAutorizacion) {
        super(comprobante, logoImgBytes, numAutorizacion, fechaAutorizacion);
    }

    /**
     * Función que se llama para poder construir el pdf de una nota de crédito.
     *
     * @return
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
        //table.setSpacingBefore(50f);

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
        cell.addElement(seccionComprobanteAsociado());
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
        cell.setBorderWidth(1);
        cell.addElement(seccionInformacionAdicional());
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);        
        cell.addElement(seccionTotales());
        table.addCell(cell);

        document.add(table);

        document.close();
        return baos.toByteArray();
    }

    /**
     * Sección que muestra información sobre el comprobante asociado de un
     * comprobante.
     *
     * @return
     */
    public Paragraph seccionComprobanteAsociado() {
        Paragraph resultado = new Paragraph();

        resultado.setSpacingAfter(15f);

        PdfPTable tablaTotales = new PdfPTable(new float[]{1.6f, 1});
        //PdfPTable tablaTotales = new PdfPTable(2);

        tablaTotales.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaTotales.setWidthPercentage(70);
        tablaTotales.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tablaTotales.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        tablaTotales.addCell(new Phrase("COMPROBANTE ASOCIADO", Fuentes.$TITULO_INFORMACION_CLIENTE));
        tablaTotales.addCell(new Phrase("", Fuentes.$TITULO_INFORMACION_CLIENTE));

        tablaTotales.addCell(new Phrase("Comprobante Modificado", Fuentes.$NORMAL_INFORMACION_CLIENTE));
        tablaTotales.addCell(new Phrase(Valores.getNombreComprobante(comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getCodDocModificado()) + " " + comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getNumDocModificado(), Fuentes.$NORMAL_INFORMACION_CLIENTE));
        comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getNumDocModificado();
        tablaTotales.addCell(new Phrase("Fecha Emisión", Fuentes.$NORMAL_INFORMACION_CLIENTE));
        tablaTotales.addCell(new Phrase(comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getFechaEmisionDocSustento(), Fuentes.$NORMAL_INFORMACION_CLIENTE));

        tablaTotales.addCell(new Phrase("Razón de Modificación", Fuentes.$NORMAL_INFORMACION_CLIENTE));
        Phrase p = new Phrase();
        p.add(comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getMotivo());
        p.setFont(Fuentes.$NORMAL_INFORMACION_CLIENTE);
        tablaTotales.addCell(p);

        resultado.add(tablaTotales);

        return resultado;
    }
}
