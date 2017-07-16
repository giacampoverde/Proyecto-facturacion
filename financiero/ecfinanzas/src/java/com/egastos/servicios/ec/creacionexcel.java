/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.servicios.ec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author CONTABILIDAD
 */
public class creacionexcel {

    /**
     * @param args the command line arguments
     */
    public  void generarexcel() throws FileNotFoundException, IOException {
        List<col> lista = new ArrayList<col>();
        FileInputStream file = new FileInputStream(new File("C:\\4.xls"));

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

                        break;

                    case Cell.CELL_TYPE_BOOLEAN:

                        System.out.println(celda.getBooleanCellValue());

                        break;

                }

            }

        }

        

        col nombre = new col();
        nombre.setColumna(5);
        nombre.setFila(5);
        nombre.setValor("Ricardo Oswaldo Delgado Ganzino");
        col cedula = new col();
        cedula.setColumna(5);
        cedula.setFila(6);
        cedula.setValor("1721817714");
        col fecha = new col();
        fecha.setColumna(5);
        fecha.setFila(7);
        fecha.setValor("10/07/2017");
//        lista.add(nombre);
        lista.add(cedula);
        lista.add(fecha);

        escribirexcel("C:\\4.xls", lista);
    }

    public static void escribirexcel(String ruta, List<col> valor) {
        try {

            InputStream inp = new FileInputStream(ruta);
            org.apache.poi.ss.usermodel.Workbook wb = new HSSFWorkbook(inp);
           
            for (int i = 0; i < valor.size(); i++) {
                 FileOutputStream fileOut = new FileOutputStream(ruta);
                Sheet sheet = wb.getSheetAt(0);
                Row row = sheet.getRow(valor.get(i).getFila());
                Cell cell = row.getCell(valor.get(i).getColumna());
                /*if (cell == null) {
            cell = row.createCell(3);
            }*/
                cell.setCellType(Cell.CELL_TYPE_STRING);
                cell.setCellValue(valor.get(i).getValor());

                // Write the output to a file
                wb.write(fileOut);
                 fileOut.close();
            }
           
        } catch (IOException ex) {
            Logger.getLogger(creacionexcel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void writeExcel(String excel_file, String sheet_name, int row, int column, Double value) {

        String cellData = new String();

        try {

            Workbook target_workbook = Workbook.getWorkbook(new File(excel_file));

            WritableWorkbook workbook = Workbook.createWorkbook(new File(excel_file), target_workbook);

            target_workbook.close();

            WritableSheet sheet = workbook.getSheet(sheet_name);

            jxl.write.Number number = new jxl.write.Number(column, row, value);

            sheet.addCell(number);

            workbook.write();

            workbook.close();

        } catch (Exception e) {

            System.out.println("writeExcel ->" + e);

        }

    }

}
