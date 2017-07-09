/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.pdfride;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;

/**
 *
 * @author Ricardo Delgado
 */
public class HeaderFooter extends PdfPageEventHelper{
   byte[] imageInBytes ;
   byte[] imageInBytes2 ;
    public HeaderFooter( byte[] imageInBytes ,byte[] imageInBytes2 ){
    this.imageInBytes=imageInBytes;
    this.imageInBytes2=imageInBytes2;
    }

          

 public void onEndPage(PdfWriter writer, Document document) {
     
            Image imghead = null;
            PdfContentByte cbhead=null;
        Image imghead2 = null;
            PdfContentByte cbhead2=null;

            try {
                imghead = Image.getInstance(this.imageInBytes);
                imghead.setAbsolutePosition(0, 0);
                imghead.setAlignment(Image.ALIGN_CENTER);
                imghead.scalePercent(35f);

                cbhead = writer.getDirectContent();
                PdfTemplate tp = cbhead.createTemplate(600, 150); //el área destinada para el encabezado
                tp.addImage(imghead);
            
            
            
            
             imghead2 = Image.getInstance(this.imageInBytes2);
                imghead2.setAbsolutePosition(0, 0);
                imghead2.setAlignment(Image.ALIGN_CENTER);
                imghead2.scalePercent(35f);

                cbhead2 = writer.getDirectContent();
                PdfTemplate tp2 = cbhead2.createTemplate(600, 150); //el área destinada para el encabezado
                tp2.addImage(imghead2);
                 cbhead2.addTemplate(tp2,20, 0);
            
            

                cbhead.addTemplate(tp,20, 794);//posición del témplate derecha y abajo

            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            Phrase headPhraseImg = new Phrase(cbhead + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL));
                   Phrase headPhraseImg2 = new Phrase(cbhead2 + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL));
////  ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Generación De Reporte  "+Utilidades.obtenerFechaFormatoddMMyyyy(this.Inicial)+" -- "+Utilidades.obtenerFechaFormatoddMMyyyy(this.Final)), 430,830,0);
// 
 }
  public void onStartPage(PdfWriter writer, Document document) {
       
            Image imghead = null;
            PdfContentByte cbhead=null;
                  Image imghead2 = null;
            PdfContentByte cbhead2=null;

            try {
                imghead = Image.getInstance(this.imageInBytes);
                imghead.setAbsolutePosition(0, 0);
                imghead.setAlignment(Image.ALIGN_CENTER);
                imghead.scalePercent(35f);

                cbhead = writer.getDirectContent();
                PdfTemplate tp = cbhead.createTemplate(600, 150); //el área destinada para el encabezado
                tp.addImage(imghead);

                cbhead.addTemplate(tp,20, 94);//posición del témplate derecha y abajo
                
                
                imghead2 = Image.getInstance(this.imageInBytes);
                imghead2.setAbsolutePosition(0, 0);
                imghead2.setAlignment(Image.ALIGN_CENTER);
                imghead2.scalePercent(35f);

                cbhead2 = writer.getDirectContent();
                PdfTemplate tp2 = cbhead.createTemplate(600, 150); //el área destinada para el encabezado
                tp2.addImage(imghead);

                cbhead.addTemplate(tp,20, 94);//posición del témplate derecha y abajo
                 cbhead2.addTemplate(tp2,20, 944);//posición del témplate derecha y abajo
                
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }

            Phrase headPhraseImg = new Phrase(cbhead + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL));
            Phrase headPhraseImg2 = new Phrase(cbhead2 + "", FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.NORMAL));
    }
}
