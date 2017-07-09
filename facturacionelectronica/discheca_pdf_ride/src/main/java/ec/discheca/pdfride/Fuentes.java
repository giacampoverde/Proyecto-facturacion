/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.pdfride;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;

/**
 * Clase que contiene todas las fuentes utilizadas para la construcción del PDF.
 * @author 
 */
public class Fuentes {
    
   
    /**
     * Font para información tributaria
     */
    public static final Font $NORMAL_INFORMACION_TRIBUTARIA = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_INFORMACION_TRIBUTARIA = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    /**
     * Font para información empresa
     */
    public static final Font $NORMAL_INFORMACION_EMPRESA = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_INFORMACION_EMPRESA = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    // 
    /**
     * Font para información Cliente
     */
    public static final Font $NORMAL_INFORMACION_CLIENTE = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_INFORMACION_CLIENTE = new Font(FontFamily.HELVETICA, 8, Font.BOLD, BaseColor.BLACK);
    // 
    /**
     * Font para los detalles
     */
    public static final Font $NORMAL_INFORMACION_DETALLE = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_INFORMACION_DETALLE = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
     /**
     * Font para los totales
     */
    public static final Font $NORMAL_INFORMACION_TOTALES = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_INFORMACION_TOTALES = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    /**
     * Font para los informacion adicionales
     */
    public static final Font $NORMAL_INFORMACION_ADICIONAL = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_INFORMACION_ADICIONAL = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
    /**
     * Font Nota de Credito
     * Font comprobante asociado
     */
    public static final Font $NORMAL_COMPROBANTE_ASOCIADO = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_COMPROBANTE_ASOCIADO = new Font(FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.BLACK);

    public static final Font $NORMAL_VALIDEZ = new Font(FontFamily.HELVETICA, 6, Font.NORMAL, BaseColor.BLACK);
    public static final Font $TITULO_VALIDEZ= new Font(FontFamily.HELVETICA, 6, Font.BOLD, BaseColor.BLACK);
}
