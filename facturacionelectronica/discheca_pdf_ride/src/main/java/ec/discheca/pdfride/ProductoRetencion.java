/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.pdfride;


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
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.retencion.ImpuestoRetencion;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase que construye el PDF de una retención.
 *
 * @Ricardo Delgado
 */
public class ProductoRetencion extends Producto {

    public ProductoRetencion(ComprobanteElectronico comprobante, byte[] logoImgBytes, String numAutorizacion, String fechaAutorizacion) {
        super(comprobante, logoImgBytes, numAutorizacion, fechaAutorizacion);
    }

    /**
     * Función que permite construir el PDF de una retención.
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
        cell.addElement(seccionDetalleRetencion());
        cell.setPaddingBottom(10f);
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
       // cell.setBorder(PdfPCell.NO_BORDER);
        cell.setPaddingTop(20f);
        cell.addElement(seccionInformacionAdicional());
        table.addCell(cell);

        cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setBorder(PdfPCell.NO_BORDER);
        //cell.setPaddingLeft(40f);
        //cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.addElement(null);
        table.addCell(cell);
        
        
        document.add(table);

        document.close();
        return baos.toByteArray();

    }

    /**
     * Sección que muestra los detalles que posee una retención.
     *
     * @return
     */
    public Paragraph seccionDetalleRetencion() {
        Paragraph resultado = new Paragraph();

        PdfPTable table = new PdfPTable(new float[]{1, 1.4f, 0.9f, 0.9f, 1.0f, 1, 1, 1,0.8f});
        table.setSplitLate(true);
        table.setHeaderRows(1);
        table.setWidthPercentage(100f);
//        table.getDefaultCell().setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(new Phrase("Comprobante", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Número", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Fecha Emisión", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Ejercicio Fiscal", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Base Imponible", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Impuesto", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Código Impuesto", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Porcentaje Retención", Fuentes.$TITULO_INFORMACION_DETALLE));
        table.addCell(new Phrase("Valor Retenido", Fuentes.$TITULO_INFORMACION_DETALLE));

        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);

        List<ImpuestoRetencion> impuestosRetencionLista = comprobante.ConstruirComprobanteRetencion().getImpuestos();
        ImpuestoRetencion impuestoRetencion = null;

        for (int i = 0; i < impuestosRetencionLista.size(); i++) {
            impuestoRetencion = impuestosRetencionLista.get(i);
            table.addCell(new Phrase(Valores.getNombreComprobante(impuestoRetencion.getCodDocSustento()), Fuentes.$NORMAL_INFORMACION_DETALLE));
            table.addCell(new Phrase(impuestoRetencion.getNumDocSustento(), Fuentes.$NORMAL_INFORMACION_DETALLE));
            table.addCell(new Phrase(impuestoRetencion.getFechaEmisionDocSustento(), Fuentes.$NORMAL_INFORMACION_DETALLE));
            //Ejercicio fiscal
            table.addCell(new Phrase(comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getPeriodoFiscal(), Fuentes.$NORMAL_INFORMACION_DETALLE));
            table.addCell(new Phrase(impuestoRetencion.getBaseImponible(), Fuentes.$NORMAL_INFORMACION_DETALLE));
            //Cambiar por el nombre del impuesto
            table.addCell(new Phrase(Valores.getImpuestoRetener(impuestoRetencion.getCodigo()), Fuentes.$NORMAL_INFORMACION_DETALLE));
            table.addCell(new Phrase(impuestoRetencion.getCodigoRetencion(), Fuentes.$NORMAL_INFORMACION_DETALLE));
            table.addCell(new Phrase(impuestoRetencion.getPorcentajeRetener() + "%", Fuentes.$NORMAL_INFORMACION_DETALLE));
            table.addCell(new Phrase(impuestoRetencion.getValorRetenido(), Fuentes.$NORMAL_INFORMACION_DETALLE));

        }
        table.setSpacingAfter(3f);
        resultado.add(table);
        return resultado;
    }
}
