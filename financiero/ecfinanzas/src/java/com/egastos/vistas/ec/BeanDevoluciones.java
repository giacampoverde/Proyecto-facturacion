/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOAsignacionComprobanteElectronico;
import com.egastos.servicios.ec.col;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.Utilidades;
import com.egastos.utilidades.Valores;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author ricar
 */
@ManagedBean
@ViewScoped
public class BeanDevoluciones {
    private String seleccionPeriodoTiempo;
    private String fechaInicialS;
    private String fechaFinalS;
    private Date fechaInicial;
    private Date fechaFinal;
    private Date fechaSeleccionadaInicio;
    private Date fechaActual;
    private boolean apareceRago = false;

    public Date getFechaSeleccionadaInicio() {
        return fechaSeleccionadaInicio;
    }

    public void setFechaSeleccionadaInicio(Date fechaSeleccionadaInicio) {
        this.fechaSeleccionadaInicio = fechaSeleccionadaInicio;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public boolean isApareceRago() {
        return apareceRago;
    }

    public void setApareceRago(boolean apareceRago) {
        this.apareceRago = apareceRago;
    }

    public void abrirDialogRangoFechas() {
        if (seleccionPeriodoTiempo != null) {
            if (seleccionPeriodoTiempo.equals("6")) {
                fechaInicial = new Date();
                fechaInicialS = fechaFormateada(fechaInicial);
                fechaFinal = new Date();
                fechaFinalS = fechaFormateada(fechaFinal);
                apareceRago = true;
            } else {
                seleccionPeriodoTiempo = "1";
                apareceRago = false;
            }
        }
    }
    public String fechaFormateada(Date _fecha) {
        if (_fecha == null) {
            _fecha = new Date();
        }
        return Utilidades.obtenerFechaFormatoddMMyyyy(_fecha);
    }
    public void generarExcel() {
        ControlSesion ms = new ControlSesion();
        try {
            List<cmp> comprobantes = new ArrayList<cmp>();
//            DAOAsignacionComprobanteElectronico dao=new DAOAsignacionComprobanteElectronico();
//            dao.buscarCriteriaComprobantesVariosParametros(new ArrayList<String>(),"", null, ms.obtenerRUCEmpresaSesionActiva(), fechaSeleccionadaInicio, fechaActual,"","","","","");
            for (int i = 0; i < 10; i++) {
                cmp comp = new cmp();
                comp.setNo("133323");
                comp.setRucprove("1721817714001");
                comp.setNofactura("001-001-111");
                comp.setDiaemision("12");
                comp.setMesemision("07");
                comp.setAnioemision("2017");
                comp.setIvasolicitado(new Double("12.55"));
                comp.setIcesolicitado(new Double("9.55"));
                comprobantes.add(comp);
            }
            List<col> lista = new ArrayList<col>();
            FileInputStream file = new FileInputStream(new File( Valores.VALOR_RUTAEXCEL));

            // Crear el objeto que tendra el libro de Excel
            HSSFWorkbook workbook = new HSSFWorkbook(file);

            /*
            20
            * Obtenemos la primera pestaña a la que se quiera procesar indicando el indice.
            21
            * Una vez obtenida la hoja excel con las filas que se quieren leer obtenemos el iterator
            22
            * que nos permite recorrer cada una de las filas que contiene.
            23
             */
            for (int i = 1; i < comprobantes.size(); i++) {
                HSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();

                Row row;

                // Recorremos todas las filas para mostrar el contenido de cada celda
                while (rowIterator.hasNext()) {

                    row = rowIterator.next();

                    // Obtenemos el iterator que permite recorres todas las celdas de una fila
                    Iterator<Cell> cellIterator = row.cellIterator();

                    Cell celda;

                    while (cellIterator.hasNext()) {

                        celda = cellIterator.next();

                        // Dependiendo del formato de la celda el valor se debe mostrar como String, Fecha, boolean, entero...
                        switch (celda.getCellType()) {

                            case Cell.CELL_TYPE_NUMERIC:

                                if (HSSFDateUtil.isCellDateFormatted(celda)) {

                                    System.out.println(celda.getDateCellValue());

                                } else {

                                    System.out.println(celda.getNumericCellValue());

                                }

                                System.out.println(celda.getNumericCellValue());

                                break;

                            case Cell.CELL_TYPE_STRING:
                                System.out.println("columna " + celda.getColumnIndex() + " file " + celda.getRowIndex());
                                System.out.println(celda.getStringCellValue());
                                if (celda.getStringCellValue().equals("No.")) {
                                    col no = new col();
                                    no.setColumna(celda.getColumnIndex());
                                    no.setFila(celda.getRowIndex() + i);
                                    no.setValor(comprobantes.get(i).getNo());
                                    no.setTipo("1");
                                    lista.add(no);
                                }
                                if (celda.getStringCellValue().equals("RUC PROVEEDOR")) {
                                    col rucpr = new col();
                                    rucpr.setColumna(celda.getColumnIndex());
                                    rucpr.setFila(celda.getRowIndex() + i);
                                    rucpr.setValor(comprobantes.get(i).getRucprove());
                                    rucpr.setTipo("1");
                                    lista.add(rucpr);
                                }
                                if (celda.getStringCellValue().startsWith("No DE FACTURA")) {
                                    col nofac = new col();
                                    nofac.setColumna(celda.getColumnIndex());
                                    nofac.setFila(celda.getRowIndex() + i);
                                    nofac.setValor(comprobantes.get(i).getNofactura());
                                    nofac.setTipo("1");
                                    lista.add(nofac);
                                }
//                            if (celda.getStringCellValue().equals("Fecha emisión")) {
//                                col dia = new col();
//                                dia.setColumna(celda.getColumnIndex());
//                                dia.setFila(celda.getRowIndex() + i);
//                                dia.setValor(comprobantes.get(i).getNo());
//                                lista.add(dia);
//                            }
                                if (celda.getStringCellValue().equals("IVA SOLICITADO")) {
                                    col mes = new col();
                                    mes.setColumna(celda.getColumnIndex());
                                    mes.setFila(celda.getRowIndex() + i);
                                    mes.setValor("" + comprobantes.get(i).getIvasolicitado());
                                    mes.setTipo("2");
                                    lista.add(mes);
                                }
                                if (celda.getStringCellValue().equals("ICE SOLICITADO")) {
                                    col anio = new col();
                                    anio.setColumna(celda.getColumnIndex());
                                    anio.setFila(celda.getRowIndex() + i);
                                    anio.setValor("" + comprobantes.get(i).getIcesolicitado());
                                    anio.setTipo("2");
                                    lista.add(anio);
                                }
                                if (celda.getStringCellValue().equals("DÍA")) {
                                    col ivasol = new col();
                                    ivasol.setColumna(celda.getColumnIndex());
                                    ivasol.setFila(celda.getRowIndex() + i);
                                    ivasol.setValor(comprobantes.get(i).getDiaemision());
                                    ivasol.setTipo("1");
                                    lista.add(ivasol);
                                }
                                if (celda.getStringCellValue().equals("MES")) {
                                    col icesol = new col();
                                    icesol.setColumna(celda.getColumnIndex());
                                    icesol.setFila(celda.getRowIndex() + i);
                                    icesol.setValor(comprobantes.get(i).getMesemision());
                                    icesol.setTipo("1");
                                    lista.add(icesol);
                                }
                                if (celda.getStringCellValue().equals("AÑO")) {
                                    col icesol = new col();
                                    icesol.setColumna(celda.getColumnIndex());
                                    icesol.setFila(celda.getRowIndex() + i);
                                    icesol.setValor(comprobantes.get(i).getAnioemision());
                                    icesol.setTipo("1");
                                    lista.add(icesol);
                                }

                                break;

                            case Cell.CELL_TYPE_BOOLEAN:

                                System.out.println(celda.getBooleanCellValue());

                                break;

                        }

                    }

                }

            }
            col nombre = new col();
            nombre.setColumna(5);
            nombre.setFila(5);
            nombre.setValor("Ricardo Oswaldo Delgado Ganzino");
            nombre.setTipo("1");
            col cedula = new col();
            cedula.setColumna(5);
            cedula.setFila(6);
            cedula.setValor("1721817714");
            cedula.setTipo("1");
            col fecha = new col();
            fecha.setColumna(5);
            fecha.setFila(7);
            fecha.setValor("10/07/2017");
            fecha.setTipo("1");
            col totaliva = new col();
            totaliva.setColumna(3);
            totaliva.setFila(54);
            totaliva.setValor("sum");
            totaliva.setTipo("1");
            col totalice = new col();
            totalice.setColumna(4);
            totalice.setFila(54);
            totalice.setValor("aum");
            totalice.setTipo("1");
            lista.add(nombre);
            lista.add(cedula);
            lista.add(fecha);
            lista.add(totaliva);
            lista.add(totalice);

            escribirexcel( Valores.VALOR_RUTAEXCEL, lista);
            FileInputStream input;
            byte[] data = null;
            try {
                input = new FileInputStream(new File( Valores.VALOR_RUTAEXCEL));
                data = new byte[input.available()];
                input.read(data);
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            FacesContext faces = FacesContext.getCurrentInstance();
            HttpServletResponse response = bajarArchivoRespuesta(faces,data, "devoluciones.xls");

            faces.responseComplete();
        } catch (IOException ex) {
            Logger.getLogger(BeanDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(BeanDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HttpServletResponse bajarArchivoRespuesta(FacesContext _faces, byte[] _archivoByte, String _nombreArchivo) {

        HttpServletResponse response = (HttpServletResponse) _faces.getExternalContext().getResponse();
        response.setContentType("application/x-download");
        response.setContentLength(_archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + _nombreArchivo);
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(_archivoByte);
            out.flush();
            out.close();
            response.flushBuffer();
        } catch (IOException e) {
        }

        return response;
    }

    public static void escribirexcel(String ruta, List<col> valor) {
        try {

            for (int i = 0; i < valor.size(); i++) {
                InputStream inp = new FileInputStream(ruta);
                org.apache.poi.ss.usermodel.Workbook wb = new HSSFWorkbook(inp);
                FileOutputStream fileOut = new FileOutputStream(ruta);

                Sheet sheet = wb.getSheetAt(0);
                Row row = sheet.getRow(valor.get(i).getFila());
                Cell cell = row.getCell(valor.get(i).getColumna());
                /*if (cell == null) {
            cell = row.createCell(3);
            }*/
                if (valor.get(i).getTipo().equals("2")) {
                    cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                    cell.setCellValue(Double.parseDouble(valor.get(i).getValor()));
                } else {
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cell.setCellValue(valor.get(i).getValor());
                }

                // Write the output to a file
                wb.write(fileOut);
                fileOut.close();
            }

        } catch (IOException ex) {
            Logger.getLogger(BeanDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getSeleccionPeriodoTiempo() {
        return seleccionPeriodoTiempo;
    }

    public void setSeleccionPeriodoTiempo(String seleccionPeriodoTiempo) {
        this.seleccionPeriodoTiempo = seleccionPeriodoTiempo;
    }

    public String getFechaInicialS() {
        return fechaInicialS;
    }

    public void setFechaInicialS(String fechaInicialS) {
        this.fechaInicialS = fechaInicialS;
    }

    public String getFechaFinalS() {
        return fechaFinalS;
    }

    public void setFechaFinalS(String fechaFinalS) {
        this.fechaFinalS = fechaFinalS;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

}
