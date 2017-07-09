
import ec.discheca.configuracion.AES256;
import ec.discheca.dao.DAOClientes;
import ec.discheca.dao.DAOPerfil;
import ec.discheca.dao.DAOReceptor;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Perfil;
import ec.discheca.modelo.Receptor;
import ec.discheca.modelo.UsuarioAcceso;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Usuario
 */
public class LeerExcel {

    private List<casecliente> listadec = new ArrayList<casecliente>();

    public void leerexcel() throws IOException {

        FileInputStream file = new FileInputStream(new File("C:\\Users\\Usuario\\Desktop\\TESIS\\Tesis Pruebas Descargas\\Xls\\cli.xls"));

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

            int i = 0;
            casecliente nuevo = new casecliente();
            while (cellIterator.hasNext()) {
                i++;
                celda = cellIterator.next();

                // Dependiendo del formato de la celda el valor se debe mostrar como String, Fecha, boolean, entero...
                switch (celda.getCellType()) {

                    case Cell.CELL_TYPE_NUMERIC:

                        if (HSSFDateUtil.isCellDateFormatted(celda)) {

                            System.out.println(celda.getDateCellValue());

                        } else {
                            if (i == 2) {

                                nuevo.setNombresCliente("" + celda.getNumericCellValue());

                            } else {
                                if (i == 3) {
                                    nuevo.setRucCliente("" + celda.getNumericCellValue());

                                } else {
                                    if (i == 4) {
                                        nuevo.setDireccionCliente("" + celda.getNumericCellValue());
                                    } else {

                                        if (i == 5) {

                                            nuevo.setTelefonocliente("" + celda.getNumericCellValue());

                                        } else {
                                            if (i == 6) {
                                                nuevo.setCorreo("" + celda.getNumericCellValue());
                                            }

                                        }
                                    }
                                }

                            }

//                            System.out.println(celda.getNumericCellValue());
                        }
                        if (i == 2) {

                            nuevo.setNombresCliente("" + celda.getNumericCellValue());

                        } else {
                            if (i == 3) {
                                nuevo.setRucCliente("" + celda.getNumericCellValue());

                            } else {
                                if (i == 4) {
                                    nuevo.setDireccionCliente("" + celda.getNumericCellValue());
                                } else {

                                    if (i == 5) {

                                        nuevo.setTelefonocliente("" + celda.getNumericCellValue());

                                    } else {
                                        if (i == 6) {
                                            nuevo.setCorreo("" + celda.getNumericCellValue());
                                        }

                                    }
                                }
                            }

                        }

//                        System.out.println(celda.getNumericCellValue());
                        break;

                    case Cell.CELL_TYPE_STRING:

                        if (i == 2) {

                            nuevo.setNombresCliente(celda.getStringCellValue());

                        } else {
                            if (i == 3) {
                                nuevo.setRucCliente(celda.getStringCellValue());

                            } else {
                                if (i == 4) {
                                    nuevo.setDireccionCliente(celda.getStringCellValue());
                                } else {

                                    if (i == 5) {

                                        nuevo.setTelefonocliente(celda.getStringCellValue());

                                    } else {
                                        if (i == 6) {
                                            nuevo.setCorreo("" + celda.getStringCellValue());
                                        }

                                    }
                                }
                            }

                        }

//                        System.out.println(celda.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:

                        System.out.println(celda.getBooleanCellValue());
                        break;
                }

            }
            listadec.add(nuevo);
        }

        workbook.close();
        for (int i = 0; i < listadec.size(); i++) {
            System.out.println("contador " + i);
            Receptor nuevoCliente = new Receptor();
            nuevoCliente.setRucReceptor(listadec.get(i).getRucCliente());
            nuevoCliente.setRazonSocialReceptor(listadec.get(i).getNombresCliente());
            nuevoCliente.setTelefono(listadec.get(i).getTelefonocliente());
            nuevoCliente.setDireccion(listadec.get(i).getDireccionCliente());
            nuevoCliente.setCorreo(listadec.get(i).getCorreo());
            nuevoCliente.setCorreoAdicional("");
            nuevoCliente.setEstado("1");

            try {
                DAOReceptor daoreceptor = new DAOReceptor();
                DAOClientes daoclientes = new DAOClientes();
                Receptor clientGuardado = daoclientes.insertarClienteUsuario(nuevoCliente);
                if (clientGuardado != null) {
                    DAOPerfil daoperfiles = new DAOPerfil();
                    UsuarioAcceso usuario_interno = new UsuarioAcceso();
                    Perfil perfilUsuario = daoperfiles.obtenerPerfilPorCodigo("Usuario Externo");
                    usuario_interno.setIdentificacionUsuario(clientGuardado.getRucReceptor());
                    usuario_interno.setNombreUsuarioAcceso(clientGuardado.getRucReceptor());
                    if (perfilUsuario != null) {
                        usuario_interno.setPerfil(perfilUsuario);
                    } else {
                        System.out.println("El usuario no tiene perfil.");
                    }
                    String clave = AES256.toAES256(clientGuardado.getRucReceptor());
                    usuario_interno.setClaveUsuarioAcceso(clave);
                    usuario_interno.setNombreUsuario(clientGuardado.getRazonSocialReceptor());
                    usuario_interno.setTelefonoPrincipalUsuario(clientGuardado.getTelefono());
                    usuario_interno.setTelefonoAdicionalUsuario("N/A");
                    usuario_interno.setCorreoPrincipalUsuario(clientGuardado.getCorreo());
                    if (clientGuardado.getCorreoAdicional() != null && !clientGuardado.getCorreoAdicional().equals("")) {
                        usuario_interno.setCorreoAdicionalUsuario(clientGuardado.getCorreoAdicional());
                    } else {
                        usuario_interno.setCorreoAdicionalUsuario("N/A");
                    }
                    usuario_interno.setEstadoUsuario("3");
                    usuario_interno.setFechaRegistroUsuario(new Date());
                    DAOUsuarioAcceso dao_usuarios = new DAOUsuarioAcceso();
                    UsuarioAcceso guardado_usuario = dao_usuarios.insertarUsuarioAcceso(usuario_interno);
                    if (guardado_usuario != null) {
//                        enviarCorreoRestablecerClave(guardado_usuario);
//                        cargarDatatableConCliente();
//                         guardarLogRegistros("nuevo cliente registrado");
//                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Se ha agregado el nuevo cliente.");
//                        this.clientes.add(this.nuevoCliente);
//                        cerrarDialogoNuevo();
//                        Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.INFO, "El usuario: " + cs.obtenerNombreUsuarioSesionActiva() + " ha registrado al cliente: " + this.nuevoCliente.getRucReceptor() + ".");
                    } else {
//                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el nuevo cliente.");
                    }

                } else {
//                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el nuevo cliente.");
                }
            } catch (Exception ex) {
//                Logger.getLogger(BeanAdministracionClientes.class
//                        .getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
