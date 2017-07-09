/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.utilidades;


import discheca.utilidades.TransformadorArchivos;
import ec.discheca.modelo.AsignacionComprobanteElectronico;
import ec.discheca.modelo.Auditoria;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import jxl.CellView;
import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 *
 * @author Usuario
 */
public class Reportes {

    public void generarReporteExelRecibidos(List<AsignacionComprobanteElectronico> comporbantesrecibios) throws Exception {

        FileOutputStream archivo = null;

        try {

            String fileName;
            fileName = "" + new Date().toString();
            String pathArchivoGenerado;
           pathArchivoGenerado = "C:\\Reportes\\" + "reportesPRODUCCIONRecibidos08-04-2015c.xls";
            archivo = new FileOutputStream(pathArchivoGenerado);
            try {

                //crear el libro de excel
                File f = new File(pathArchivoGenerado);
                WritableWorkbook libro = Workbook.createWorkbook(f);
                //crea la hoja de excel
                WritableSheet hoja = libro.createSheet("Hoja de Datos", 0);
                //para crear estilo, este da el formato
                //WritableFont times16font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, true);
                WritableFont times16font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true, UnderlineStyle.SINGLE, jxl.format.Colour.BLUE2);

                // creo el formato par ser seteado en la celda
                WritableCellFormat formato = new WritableCellFormat(times16font);
                // se cera la celda en la 
                // Label label3 = new Label(5, 0, "REPORTE PERFILES", formato);
                Label label0 = new Label(0, 0, "Nombre Comercial", formato);
                hoja.addCell(label0);
                Label label1 = new Label(1, 0, "Ruc", formato);
                hoja.addCell(label1);
                Label label2 = new Label(2, 0, " Codigo Establecimiento", formato);
                hoja.addCell(label2);
                Label label3 = new Label(3, 0, "Punto Emision", formato);
                hoja.addCell(label3);
                Label label4 = new Label(4, 0, "Secuencial", formato);
                hoja.addCell(label4);
                Label label5 = new Label(5, 0, "Fecha Emision", formato);
                hoja.addCell(label5);
                Label label6 = new Label(6, 0, "Numero Autorizacion", formato);
                hoja.addCell(label6);
                Label label7 = new Label(7, 0, "Clave Acceso", formato);
                hoja.addCell(label7);
                Label label8 = new Label(8, 0, "Valor", formato);
                hoja.addCell(label8);

                int val = 0;
                String aux = null;
                String aux1 = null;
                String aux2 = null;
                String aux3 = null;
                String aux4 = null;
                String aux5 = null;
                String aux6 = null;
                String aux7 = null;
                String aux8 = null;
                  Double valorsumado=new Double(0);
                for (int i = 0; i < comporbantesrecibios.size(); i++) {
                    double iva = 0;
                    double subTotalSinIva = 0;
                    double totalNoSujetoIva = 0;
                    double subTotal0 = 0;

                    String totalSinImpuestos = "";
                    String totalDescuento = "";
                    String propina = "";
                    String valorTotal = "";

                    ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico comprobantefinal = TransformadorArchivos.byteCompr(comporbantesrecibios.get(i).getComprobanteElectronico().getComprobanteFirmadoComprobanteElectronico(), null);
                    valorTotal = comprobantefinal.ConstruirFactura().getInformacionFactura().getImporteTotal();

                    val = i + 1;
                     
                    for (int j = 0; j <= 13; j++) {
                     

                        if (j == 0) {
                            aux = comporbantesrecibios.get(i).getComprobanteElectronico().getNombreComercialEmisorComprobanteElectronico();
                            if (aux != null && !aux.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux = "";
                        }
                        if (j == 1) {
                            aux1 = "" + comporbantesrecibios.get(i).getComprobanteElectronico().getRucEmisorComprobanteElectronico();
                            if (aux1 != null && !aux1.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux1));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux1 = "";
                        }
                        if (j == 2) {
                            if (comporbantesrecibios.get(i).getComprobanteElectronico() != null) {
                                aux2 = "" + comporbantesrecibios.get(i).getComprobanteElectronico().getCodigoEstablecimientoComprobanteElectronico();
//                                
                            }
                            if (aux2 != null && !aux2.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux2));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux2 = "";
                        }
                        if (j == 3) {
                            if (comporbantesrecibios.get(i).getComprobanteElectronico() != null) {

                                aux3 = "" + comporbantesrecibios.get(i).getComprobanteElectronico().getPuntoEmisionComprobanteElectronico();
                            }
                            if (aux3 != null && !aux3.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux3));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux2 = "";
                        }
                        if (j == 4) {
                            if (comporbantesrecibios.get(i).getComprobanteElectronico() != null) {
                                aux4 = "" + comporbantesrecibios.get(i).getComprobanteElectronico().getSecuencialComprobanteElectronico();
                            }
                            if (aux4 != null && !aux4.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux4));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux4 = "";
                        }
                        if (j == 5) {
                            if (comporbantesrecibios.get(i).getComprobanteElectronico() != null) {
                                aux5 = "" + comporbantesrecibios.get(i).getComprobanteElectronico().getFechaEmisionComprobanteElectronico();
                            }
                            if (aux5 != null && !aux5.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux5));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux5 = "";
                        }
                        if (j == 6) {
                            if (comporbantesrecibios.get(i).getComprobanteElectronico() != null) {
                                aux6 = "" + comporbantesrecibios.get(i).getComprobanteElectronico().getNumeroAutorizacionComprobanteElectronico();
                               
                            }
                            if (aux6 != null && !aux6.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux6));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux6 = "";
                        }
                        if (j == 7) {
                            if (comporbantesrecibios.get(i).getComprobanteElectronico() != null) {
                                aux7 = "" + comporbantesrecibios.get(i).getComprobanteElectronico().getClaveAccesoComprobanteElectronico();
                               
                            }
                            if (aux7 != null && !aux7.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux7));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux7 = "";
                        }
                        //fecha
                        if (j == 8) {
                            if (comporbantesrecibios.get(i).getComprobanteElectronico() != null) {
                                aux8 = "" + valorTotal;
                                    valorsumado=valorsumado+Double.parseDouble(valorTotal);
                            }
                            if (aux8 != null && !aux8.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux8));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux8 = "";
                        }
                    }

                }

                for (int b = 0; b <= 13; b++) {
                    CellView cv = hoja.getColumnView(b);
                    cv.setAutosize(true);
                    hoja.setColumnView(b, cv);
                }
                Label label20 = new Label(0, val+2, "VALOR TOTAL "+String.format("%.4f",valorsumado) , formato);
                hoja.addCell(label20);
                libro.write();
                libro.close();
                String nombreArchivo = "ReporteSolicitados-" + fileName + "-.xls";
                byte[] bytesAnexo = TransformadorArchivos.archArrayB(f);
                FacesContext faces = FacesContext.getCurrentInstance();
                HttpServletResponse response = bajarArchivo(faces, bytesAnexo, nombreArchivo);
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                archivo.close();
            } catch (IOException ex) {
                Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    
      public void generarReporteregistros(List<Auditoria>historial) throws Exception {

        FileOutputStream archivo = null;

        try {

            String fileName;
            fileName = "" + new Date().toString();
            String pathArchivoGenerado;
           pathArchivoGenerado = "C:\\Reportes\\" + "historialEventos.xls";
            archivo = new FileOutputStream(pathArchivoGenerado);
            try {

                //crear el libro de excel
                File f = new File(pathArchivoGenerado);
                WritableWorkbook libro = Workbook.createWorkbook(f);
                //crea la hoja de excel
                WritableSheet hoja = libro.createSheet("Hoja de Datos", 0);
                //para crear estilo, este da el formato
                //WritableFont times16font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, true);
                WritableFont times16font = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, true, UnderlineStyle.SINGLE, jxl.format.Colour.BLUE2);

                // creo el formato par ser seteado en la celda
                WritableCellFormat formato = new WritableCellFormat(times16font);
                // se cera la celda en la 
                // Label label3 = new Label(5, 0, "REPORTE PERFILES", formato);
                Label label0 = new Label(0, 0, "Nombres", formato);
                hoja.addCell(label0);
                Label label1 = new Label(1, 0, "Mensaje", formato);
                hoja.addCell(label1);
                Label label2 = new Label(2, 0, " Fecha", formato);
                hoja.addCell(label2);
               
                int val = 0;
                String aux = null;
                String aux1 = null;
                String aux2 = null;
          
                for (int i = 0; i < historial.size(); i++) {
                   
                    val = i + 1;
                     
                    for (int j = 0; j <= 2; j++) {
                        // fecha del comprobante

                        if (j == 0) {
                             if (historial.get(i).getUsuarioAcceso() != null) {
                                aux = "" + historial.get(i).getUsuarioAcceso().getNombreUsuario();
//                               
                            }
                            aux = historial.get(i).getUsuarioAcceso().getNombreUsuario();
                            if (aux != null && !aux.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux = "";
                        }
                        if (j == 1) {
                            if (historial.get(i).getMensajeTransaccion() != null) {
                                aux1 = "" + historial.get(i).getMensajeTransaccion();
//                               
                            }
                            aux1 = "" + historial.get(i).getMensajeTransaccion();
                            if (aux1 != null && !aux1.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux1));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux1 = "";
                        }
                        if (j == 2) {
                            if (historial.get(i).getFechaHora() != null) {
                                aux2 = "" + historial.get(i).getFechaHora();
//                               
                            }
                            if (aux2 != null && !aux2.equals("")) {
                                hoja.addCell(new jxl.write.Label(j, val, aux2));
                            } else {
                                hoja.addCell(new jxl.write.Label(j, val, "N/A"));
                            }
                            aux2 = "";
                        }
                       
                     
                    }

                }

                for (int b = 0; b <= 2; b++) {
                    CellView cv = hoja.getColumnView(b);
                    cv.setAutosize(true);
                    hoja.setColumnView(b, cv);
                }
                libro.write();
                libro.close();
                String nombreArchivo = "ReporteSolicitados-" + fileName + "-.xls";
                byte[] bytesAnexo = TransformadorArchivos.archArrayB(f);
                FacesContext faces = FacesContext.getCurrentInstance();
                HttpServletResponse response = bajarArchivo(faces, bytesAnexo, nombreArchivo);
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                archivo.close();
            } catch (IOException ex) {
                Logger.getLogger(Reportes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    
    
    public HttpServletResponse bajarArchivo(FacesContext faces, byte[] archivoByte, String nombreArchivo) {

        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.setContentType("application/xls");
        response.setContentLength(archivoByte.length);
        response.setHeader("Content-disposition", "attachment; filename=" + nombreArchivo);
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(archivoByte);

        } catch (IOException e) {
            //Logger.getLogger(BeanAdminPerfiles.class.getName()).log(Level.FATAL, "Error, no se pudo bajar el archivo", e);
        }

        return response;
    }

}
