/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.pdfride;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.discheca.comprobanteelectronico.esquema.factura.Detalle;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase padre la cual contiene información común entre los diferentes tipos de
 * comprobantes
 *
 * @author Ricardo Delgado
 */
public class Producto {

    ComprobanteElectronico comprobante;
    byte[] logoImgBytes;
    String numAutorizacion;
    String fechaAutorizacion;
    String ambientePruebas = "1";
    String ambienteProduccion = "2";
    String emisionNormal = "1";
    String emisionIndisponibilidad = "2";

    public Producto(ComprobanteElectronico comprobante, byte[] logoImgBytes, String numAutorizacion, String fechaAutorizacion) {
        this.comprobante = comprobante;
        this.logoImgBytes = logoImgBytes;
        this.numAutorizacion = numAutorizacion;
        this.fechaAutorizacion = fechaAutorizacion;
    }

    /**
     * Se especifica la sección donde se incluye información solamente de la
     * empresa que emite el comprobante.
     *
     * @return
     * @throws BadElementException
     * @throws MalformedURLException
     * @throws IOException
     */
    public PdfPTable seccionInformacionEmpresa() throws BadElementException, MalformedURLException, IOException {

        String razonSocial = comprobante.getInformacionTributariaComprobanteElectronico().getRazonSocial();
        String dirMatriz = comprobante.getInformacionTributariaComprobanteElectronico().getDirMatriz();
        String dirEstablecimiento = "";
        String contribuyenteEspecial = "";
        String obligadoLlevarContabilidad = "";
        switch (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()) {
            case "01":
                dirEstablecimiento = comprobante.ConstruirFactura().getInformacionFactura().getDirEstablecimiento();
                contribuyenteEspecial = comprobante.ConstruirFactura().getInformacionFactura().getContribuyenteEspecial();
                obligadoLlevarContabilidad = comprobante.ConstruirFactura().getInformacionFactura().getObligadoContabilidad();
                break;
            case "04":
                dirEstablecimiento = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getDirEstablecimiento();
                contribuyenteEspecial = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getContribuyenteEspecial();
                obligadoLlevarContabilidad = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getObligadoContabilidad();
                break;
            case "07":
                dirEstablecimiento = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getDirEstablecimiento();
                contribuyenteEspecial = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getContribuyenteEspecial();
                obligadoLlevarContabilidad = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getObligadoContabilidad();
                break;
            default:
                dirEstablecimiento = "";
                contribuyenteEspecial = "";
                obligadoLlevarContabilidad = "";
                break;
        }
        if (dirEstablecimiento == null) {
            dirEstablecimiento = "";
        }
        if (contribuyenteEspecial == null) {
            contribuyenteEspecial = "";
        }
        if (obligadoLlevarContabilidad == null) {
            obligadoLlevarContabilidad = "NO";
        }
        PdfPTable tablaInfoEmpresa = new PdfPTable(new float[]{1, 1});
        tablaInfoEmpresa.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tablaInfoEmpresa.setWidthPercentage(100);
        tablaInfoEmpresa.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tablaInfoEmpresa.getDefaultCell().setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        tablaInfoEmpresa.setSpacingAfter(1);
        tablaInfoEmpresa.setSpacingBefore(2);

        PdfPTable tablaContenedora = new PdfPTable(1);
        tablaContenedora.setWidthPercentage(100);
        tablaContenedora.getDefaultCell().setBackgroundColor(BaseColor.WHITE);

        PdfPCell celda = new PdfPCell(new Paragraph(razonSocial, Fuentes.$TITULO_INFORMACION_EMPRESA));
        celda.setColspan(2);
        celda.setBorder(PdfPCell.NO_BORDER);
        tablaInfoEmpresa.addCell(celda);
        tablaInfoEmpresa.addCell(new Paragraph(""));
        tablaInfoEmpresa.addCell(new Paragraph(""));
        tablaInfoEmpresa.addCell(new Paragraph(""));
        tablaInfoEmpresa.addCell(new Paragraph(""));
        tablaInfoEmpresa.addCell(new Paragraph("Dir Matriz: ", Fuentes.$NORMAL_INFORMACION_EMPRESA));
        tablaInfoEmpresa.addCell(new Paragraph(dirMatriz, Fuentes.$NORMAL_INFORMACION_EMPRESA));
        tablaInfoEmpresa.addCell(new Paragraph("Dir Sucursal: ", Fuentes.$NORMAL_INFORMACION_EMPRESA));
        tablaInfoEmpresa.addCell(new Paragraph(dirEstablecimiento, Fuentes.$NORMAL_INFORMACION_EMPRESA));
        if (!contribuyenteEspecial.equals("")) {
            tablaInfoEmpresa.addCell(new Paragraph("Contribuyente Especial N° ", Fuentes.$NORMAL_INFORMACION_EMPRESA));
            tablaInfoEmpresa.addCell(new Paragraph(contribuyenteEspecial, Fuentes.$NORMAL_INFORMACION_EMPRESA));
        }
        tablaInfoEmpresa.addCell(new Paragraph("OBLIGADO A LLEVAR CONTABILIDAD: ", Fuentes.$NORMAL_INFORMACION_EMPRESA));
        tablaInfoEmpresa.addCell(new Paragraph(obligadoLlevarContabilidad, Fuentes.$NORMAL_INFORMACION_EMPRESA));

        tablaContenedora.addCell(tablaInfoEmpresa);
        return tablaContenedora;
    }

    public Paragraph ubicarLogo() throws BadElementException, IOException {
        Paragraph resultado = new Paragraph();

        if (logoImgBytes != null) {
            Image empresaImg = Image.getInstance(logoImgBytes);
            PdfPTable tablaImagen = new PdfPTable(1);
            tablaImagen.setWidthPercentage(50);
            tablaImagen.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            tablaImagen.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

            tablaImagen.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            tablaImagen.addCell(empresaImg);
            resultado.add(tablaImagen);
        }
        return resultado;
    }

    /**
     * Se especifica la sección donde se especifica información tributaria el
     * emisor del comprobante.
     *
     * @param pdfContentByte
     * @return
     * @throws MalformedURLException
     * @throws BadElementException
     * @throws IOException
     */
    public PdfPTable seccionInformacionTributaria(PdfContentByte pdfContentByte) throws MalformedURLException, BadElementException, IOException {

        String ruc = comprobante.getInformacionTributariaComprobanteElectronico().getRuc();
        String identificadorComprobante = comprobante.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento() + "-"
                + comprobante.getInformacionTributariaComprobanteElectronico().getPuntoEmision() + "-"
                + comprobante.getInformacionTributariaComprobanteElectronico().getSecuencial();
        String ambiente = comprobante.getInformacionTributariaComprobanteElectronico().getAmbiente();
        if (ambiente.equals(ambientePruebas)) {
            ambiente = "PRUEBAS";
        } else if (ambiente.equals(ambienteProduccion)) {
            ambiente = "PRODUCCIÓN";
        } else {
            ambiente = "";
        }

        String emision = comprobante.getInformacionTributariaComprobanteElectronico().getTipoEmision();
        if (emision.equals(emisionNormal)) {
            emision = "NORMAL";
        } else if (emision.equals(emisionIndisponibilidad)) {
            emision = "INDISPONIBILIDAD DEL SISTEMA";
        } else {
            emision = "";
        }
        String tipoComprobante = "";
        switch (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()) {
            case "01":
                tipoComprobante = "FACTURA";
                break;
            case "04":
                tipoComprobante = "NOTA DE CRÉDITO";
                break;
            case "05":
                tipoComprobante = "NOTA DE DÉBITO";
                break;
            case "06":
                tipoComprobante = "GUÍA DE REMISIÓN";
                break;
            case "07":
                tipoComprobante = "COMPROBANTE DE RETENCIÓN";
                break;
            default:
                tipoComprobante = "";
                break;
        }

        PdfPTable tablaInfoTributaria = new PdfPTable(1);
        tablaInfoTributaria.setWidthPercentage(100);
        tablaInfoTributaria.getDefaultCell().setPadding(5);
        tablaInfoTributaria.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tablaInfoTributaria.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tablaInfoTributaria.addCell(new Paragraph("RUC.: " + ruc, Fuentes.$TITULO_INFORMACION_TRIBUTARIA));
        tablaInfoTributaria.addCell(new Paragraph(tipoComprobante + " N° " + identificadorComprobante, Fuentes.$TITULO_INFORMACION_TRIBUTARIA));
        tablaInfoTributaria.addCell(new Paragraph("NÚMERO DE AUTORIZACIÓN", Fuentes.$TITULO_INFORMACION_TRIBUTARIA));

        tablaInfoTributaria.addCell(new Paragraph(numAutorizacion, Fuentes.$NORMAL_INFORMACION_TRIBUTARIA));
        tablaInfoTributaria.addCell(new Paragraph("FECHA Y HORA DE AUTORIZACIÓN: " + fechaAutorizacion, Fuentes.$TITULO_INFORMACION_TRIBUTARIA));

        tablaInfoTributaria.addCell(new Paragraph("AMBIENTE: " + ambiente, Fuentes.$NORMAL_INFORMACION_TRIBUTARIA));
        tablaInfoTributaria.addCell(new Paragraph("EMISIÓN: " + emision, Fuentes.$NORMAL_INFORMACION_TRIBUTARIA));

        String claveAcceso = comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso();
        //codigo de barras
        Barcode128 uccEan128 = new Barcode128();
        uccEan128.setCodeType(Barcode.CODE128);
        uccEan128.setCode(claveAcceso);
        Image img = uccEan128.createImageWithBarcode(pdfContentByte, BaseColor.BLACK,
                BaseColor.BLACK);
        tablaInfoTributaria.addCell(new Paragraph("CLAVE DE ACCESO", Fuentes.$TITULO_INFORMACION_TRIBUTARIA));
        tablaInfoTributaria.addCell(img);
        return tablaInfoTributaria;
    }

    /**
     * Se especifica la sección donde se encuentra información del cliente que
     * recibe el comprobante.
     *
     * @return
     */
    public Paragraph seccionInformacionCliente() {

        String razonSocialNombres = "";
        String rucCI = "";
        String fechaEmision = "";
        String direccionComprador = "";
        switch (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()) {
            case "01":
                razonSocialNombres = comprobante.ConstruirFactura().getInformacionFactura().getRazonSocialComprador();
                rucCI = comprobante.ConstruirFactura().getInformacionFactura().getIdentificacionComprador();
                fechaEmision = comprobante.ConstruirFactura().getInformacionFactura().getFechaEmision();
                if (comprobante.ConstruirFactura().getInformacionFactura().getDireccionComprador() != null && !comprobante.ConstruirFactura().getInformacionFactura().getDireccionComprador().equals("")) {
                    direccionComprador = comprobante.ConstruirFactura().getInformacionFactura().getDireccionComprador();
                }
                break;
            case "04":
                razonSocialNombres = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getRazonSocialComprador();
                rucCI = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getIdentificacionComprador();
                fechaEmision = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getFechaEmision();
                break;

            case "07":
                razonSocialNombres = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getRazonSocialSujetoRetenido();
                rucCI = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getIdentificacionSujetoRetenido();
                fechaEmision = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getFechaEmision();
                break;
            default:
                razonSocialNombres = "";
                rucCI = "";
                fechaEmision = "";
                break;
        }

        Paragraph resultado = new Paragraph();

        resultado.setSpacingBefore(20f);
        resultado.setSpacingAfter(15f);

        PdfPTable tablaTotales = new PdfPTable(new float[]{1.2f, 1f});
        tablaTotales.setHorizontalAlignment(Element.ALIGN_LEFT);
        tablaTotales.setWidthPercentage(80);
        tablaTotales.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tablaTotales.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")
                || comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("04")
                || comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("05")
                || comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("07")) {

            tablaTotales.addCell(new Phrase("INFORMACIÓN CLIENTE", Fuentes.$TITULO_INFORMACION_CLIENTE));
            tablaTotales.addCell(new Phrase("", Fuentes.$TITULO_INFORMACION_CLIENTE));

            tablaTotales.addCell(new Phrase("Razón Social / Nombres y Apellidos", Fuentes.$NORMAL_INFORMACION_CLIENTE));
            tablaTotales.addCell(new Phrase(razonSocialNombres, Fuentes.$NORMAL_INFORMACION_CLIENTE));

            if (!direccionComprador.equals("")) {
                tablaTotales.addCell(new Phrase("Dirección comprador", Fuentes.$NORMAL_INFORMACION_CLIENTE));
                tablaTotales.addCell(new Phrase(direccionComprador, Fuentes.$NORMAL_INFORMACION_CLIENTE));
            }

            tablaTotales.addCell(new Phrase("RUC / CI", Fuentes.$NORMAL_INFORMACION_CLIENTE));
            tablaTotales.addCell(new Phrase(rucCI, Fuentes.$NORMAL_INFORMACION_CLIENTE));

            tablaTotales.addCell(new Phrase("Fecha Emisión", Fuentes.$NORMAL_INFORMACION_CLIENTE));
            tablaTotales.addCell(new Phrase(fechaEmision, Fuentes.$NORMAL_INFORMACION_CLIENTE));

        }
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
            String guiaRemision = comprobante.ConstruirFactura().getInformacionFactura().getGuiaRemision();
            tablaTotales.addCell(new Phrase("Guía Remisión", Fuentes.$NORMAL_INFORMACION_CLIENTE));
            tablaTotales.addCell(new Phrase(guiaRemision, Fuentes.$NORMAL_INFORMACION_CLIENTE));

        }

        resultado.add(tablaTotales);

        return resultado;

    }

    /**
     * Se especifica información compartida que debe haber en el PDF entre la
     * Factura y Nota de crédito
     *
     * @return
     */
    public Paragraph seccionDetalleFacturaNotaCredito() {
     
        boolean es_exportacion = false;
        int tamanoLista = 0;
        List<Detalle> listaDetalle = new ArrayList<Detalle>();
        String unidad_medida = "";
        switch (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()) {
            case "01":
                tamanoLista = comprobante.ConstruirFactura().getDetalles().size();
                listaDetalle = comprobante.ConstruirFactura().getDetalles();

              
                if (comprobante.ConstruirFactura().getInformacionFactura().getComercioExterior() != null) {
                    if (!comprobante.ConstruirFactura().getInformacionFactura().getComercioExterior().equals("")) {
                        es_exportacion = true;
                    }
                }
                break;
            case "04":
                tamanoLista = comprobante.ConstruirNotaCredito().getDetalles().size();
                listaDetalle = comprobante.ConstruirNotaCredito().getDetalles();
                break;
        }
        for (Detalle d : listaDetalle) {

            List<InformacionAdicional> ias = d.getDetallesAdicionales();
            if (ias != null && !ias.isEmpty()) {
                for (InformacionAdicional ia : ias) {
                    if (ia.getNombre().equals("U.Medida")) {
                        unidad_medida = ia.getValor();
                        break;
                    }
                }
            }
            break;
        }

        Paragraph resultado = new Paragraph();

        PdfPTable table = null;
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("04")) {
            table = new PdfPTable(new float[]{4, 0.7f, 1, 1, 1});
        } else {
           if (es_exportacion) {
                table = new PdfPTable(new float[]{1, 4, 0.7f, 1, 1, 1});
            } else {
                table = new PdfPTable(new float[]{4, 0.7f, 1, 1, 1, 1});

            }

        }

        table.setSplitLate(true);
        table.setWidthPercentage(100f);
        table.setHeaderRows(1);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
          if (es_exportacion) {
                table.addCell(new Phrase("Código principal", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Descripción / Detalles Adicionales", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Cant.", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Precio Unitario", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Descuento", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Precio Total", Fuentes.$TITULO_INFORMACION_DETALLE));
            } else {
                table.addCell(new Phrase("Descripción / Detalles Adicionales", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Cant", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("UM", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Precio Unitario", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Descuento", Fuentes.$TITULO_INFORMACION_DETALLE));
                table.addCell(new Phrase("Precio Total", Fuentes.$TITULO_INFORMACION_DETALLE));

            }
        } else {
            table.addCell(new Phrase("Descripción / Detalles Adicionales", Fuentes.$TITULO_INFORMACION_DETALLE));
            table.addCell(new Phrase("Cant.", Fuentes.$TITULO_INFORMACION_DETALLE));
            table.addCell(new Phrase("Precio Unitario", Fuentes.$TITULO_INFORMACION_DETALLE));
            table.addCell(new Phrase("Descuento", Fuentes.$TITULO_INFORMACION_DETALLE));
            table.addCell(new Phrase("Precio Total", Fuentes.$TITULO_INFORMACION_DETALLE));

        }

        table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);

        StringBuilder detallesAdicionales = null;
        for (int i = 0; i < tamanoLista; i++) {
            detallesAdicionales = new StringBuilder(" ");
            Detalle detalle = listaDetalle.get(i);

            if (detalle.getDetallesAdicionales() != null) {
                for (int y = 0; y < detalle.getDetallesAdicionales().size(); y++) {
                     {
                        detallesAdicionales.append(" / ");
                        detallesAdicionales.append(detalle.getDetallesAdicionales().get(y).getNombre());
                        detallesAdicionales.append(" / ");
                        detallesAdicionales.append(detalle.getDetallesAdicionales().get(y).getValor());

                    }
                }
            }

            if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {

               if (es_exportacion) {
                
                    table.addCell(new Phrase(detalle.getCodigoPrincipal(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getDescripcion(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getCantidad(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getPrecioUnitario(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getDescuento(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getPrecioTotalSinImpuesto(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                } else {
                    table.addCell(new Phrase(detalle.getDescripcion(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getCantidad(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(unidad_medida, Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getPrecioUnitario(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getDescuento(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                    table.addCell(new Phrase(detalle.getPrecioTotalSinImpuesto(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                }
            } else {
                table.addCell(new Phrase(detalle.getDescripcion() + detallesAdicionales, Fuentes.$NORMAL_INFORMACION_DETALLE));
                table.addCell(new Phrase(detalle.getCantidad(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                table.addCell(new Phrase(detalle.getPrecioUnitario(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                table.addCell(new Phrase(detalle.getDescuento(), Fuentes.$NORMAL_INFORMACION_DETALLE));
                table.addCell(new Phrase(detalle.getPrecioTotalSinImpuesto(), Fuentes.$NORMAL_INFORMACION_DETALLE));
            }

        }

        table.setSpacingAfter(3f);
        resultado.add(table);
        return resultado;
    }

    /**
     * Tabla que obtiene información en cuanto a los totales que debe exisitir
     * en el PDF.
     *
     * @return
     */
    public PdfPTable seccionTotales() {
        List<ImpuestoComprobanteElectronico> impuestoComprobanteLista = new ArrayList<ImpuestoComprobanteElectronico>();
        ImpuestoComprobanteElectronico impuestoComprobante = null;
        double iva = 0;
        double subTotalSinIva = 0;
        double ice = 0;
        double totalNoSujetoIva = 0;
        double subTotal0 = 0;

        String totalSinImpuestos = "";
        String totalDescuento = "";
        String propina = "";
        String valorTotal = "";
        switch (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()) {
            case "01":
                impuestoComprobanteLista = comprobante.ConstruirFactura().getInformacionFactura().getTotalConImpuesto();
                totalSinImpuestos = comprobante.ConstruirFactura().getInformacionFactura().getTotalSinImpuestos();
                totalDescuento = comprobante.ConstruirFactura().getInformacionFactura().getTotalDescuento();
                propina = comprobante.ConstruirFactura().getInformacionFactura().getPropina();
                valorTotal = comprobante.ConstruirFactura().getInformacionFactura().getImporteTotal();
                break;
            case "04":
                impuestoComprobanteLista = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getTotalConImpuesto();
                totalSinImpuestos = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getTotalSinImpuestos();
                valorTotal = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getValorModificacion();
                break;
            case "05":
               
                break;
        }

        for (int i = 0; i < impuestoComprobanteLista.size(); i++) {
            impuestoComprobante = impuestoComprobanteLista.get(i);
            if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("0")) {
                subTotal0 = Double.parseDouble(impuestoComprobante.getBaseImponible());
            } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("6")) {
                totalNoSujetoIva = Double.parseDouble(impuestoComprobante.getBaseImponible());
                //se cambia de dos a3
            } else if (impuestoComprobante.getCodigo().equals("2") && impuestoComprobante.getCodigoPorcentaje().equals("3")) {
                iva = Double.parseDouble(impuestoComprobante.getValor());
                subTotalSinIva = Double.parseDouble(impuestoComprobante.getBaseImponible());
            } else if (impuestoComprobante.getCodigo().equals("3")) {
                ice = Double.parseDouble(impuestoComprobante.getValor());
            }

        }

        PdfPTable tablaTotales = new PdfPTable(new float[]{3, 1});
        tablaTotales.setHorizontalAlignment(Element.ALIGN_RIGHT);
        tablaTotales.setWidthPercentage(88);
        tablaTotales.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tablaTotales.getDefaultCell().setBorder(PdfPCell.NO_BORDER);

        tablaTotales.addCell(new Phrase("SUBTOTAL IVA 14%", Fuentes.$NORMAL_INFORMACION_TOTALES));
        tablaTotales.addCell(new Phrase(String.valueOf(subTotalSinIva), Fuentes.$NORMAL_INFORMACION_TOTALES));

        tablaTotales.addCell(new Phrase("SUBTOTAL IVA 0%", Fuentes.$NORMAL_INFORMACION_TOTALES));
        tablaTotales.addCell(new Phrase(String.valueOf(subTotal0), Fuentes.$NORMAL_INFORMACION_TOTALES));

        tablaTotales.addCell(new Phrase("SUBTOTAL No sujeto de IVA", Fuentes.$NORMAL_INFORMACION_TOTALES));
        tablaTotales.addCell(new Phrase(String.valueOf(totalNoSujetoIva), Fuentes.$NORMAL_INFORMACION_TOTALES));

        tablaTotales.addCell(new Phrase("SUBTOTAL", Fuentes.$NORMAL_INFORMACION_TOTALES));
        tablaTotales.addCell(new Phrase(totalSinImpuestos, Fuentes.$NORMAL_INFORMACION_TOTALES));

        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
            tablaTotales.addCell(new Phrase("DESCUENTO", Fuentes.$NORMAL_INFORMACION_TOTALES));
            tablaTotales.addCell(new Phrase(totalDescuento, Fuentes.$NORMAL_INFORMACION_TOTALES));
        }
//        tablaTotales.addCell(new Phrase("ICE", Fuentes.$NORMAL_INFORMACION_TOTALES));
//        tablaTotales.addCell(new Phrase(String.valueOf(ice), Fuentes.$NORMAL_INFORMACION_TOTALES));
        tablaTotales.addCell(new Phrase("IVA 14%", Fuentes.$NORMAL_INFORMACION_TOTALES));
        tablaTotales.addCell(new Phrase(String.valueOf(iva), Fuentes.$NORMAL_INFORMACION_TOTALES));

        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
//            tablaTotales.addCell(new Phrase("PROPINA", Fuentes.$NORMAL_INFORMACION_TOTALES));
//            tablaTotales.addCell(new Phrase(propina, Fuentes.$NORMAL_INFORMACION_TOTALES));
        }
        tablaTotales.addCell(new Phrase("VALOR TOTAL", Fuentes.$TITULO_INFORMACION_TOTALES));
        tablaTotales.addCell(new Phrase(valorTotal, Fuentes.$TITULO_INFORMACION_TOTALES));

        return tablaTotales;
    }

    /**
     * Se especifica información adicional que debe tener el comprobante en el
     * PDF.
     *
     * @return
     */
    public PdfPTable seccionInformacionAdicional() {
        Paragraph resultado = new Paragraph();

        PdfPTable tabla = new PdfPTable(2);

        tabla.setWidthPercentage(100);
        tabla.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
        tabla.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
        tabla.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        tabla.getDefaultCell().setPadding(4);

        List<InformacionAdicional> informacionAdicionalLista = comprobante.getInformacionAdicional();
        PdfPCell celda = new PdfPCell(new Phrase("Información Adicional", Fuentes.$TITULO_INFORMACION_ADICIONAL));
        celda.setColspan(2);
        celda.setBorder(PdfPCell.NO_BORDER);
        tabla.addCell(celda);
        for (int i = 0; i < informacionAdicionalLista.size(); i++) {
            tabla.addCell(new Phrase(informacionAdicionalLista.get(i).getNombre(), Fuentes.$TITULO_INFORMACION_ADICIONAL));
            tabla.addCell(new Phrase(informacionAdicionalLista.get(i).getValor(), Fuentes.$NORMAL_INFORMACION_ADICIONAL));
        }

        return tabla;
    }

    //Seccion la información impresa no tiene validez
    public Paragraph seccionValidezJuridica() {
        Paragraph resultado = new Paragraph();
        return resultado;
    }
}
