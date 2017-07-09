/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Utilidades;
import discheca.utilidades.Validaciones;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOPermiso1;
import ec.discheca.dao.DAOProductos;
import ec.discheca.dao.DAOTarifasImpuesto;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Detalleadicional;
import ec.discheca.modelo.Producto;
import ec.discheca.modelo.TarifasImpuesto;
import ec.discheca.utilidades.MensajesPrimefaces;
import static ec.discheca.utilidades.MensajesPrimefaces.mostrarMensajeDialog;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Ricardo Delgado
 */
@ManagedBean
@ViewScoped
public class BeanAdministracionProductosServicios implements Serializable {

    /**
     * Creates a new instance of BeanAdmininistracionProductos
     */
    private List<Producto> productos;

    private Producto productoSeleccionado;

    private boolean estado;

    private String info_empresa;

    private Producto nuevoProducto;

    private Producto productoEdicion;

    private List<TarifasImpuesto> listaTarifaImpuestoIVA;

    private String idImpuestoIVASeleccionado;

    private List<TarifasImpuesto> listaTarifaImpuestoICE;

    private String idImpuestoICESeleccionado;

    /* Información adicional para el detalle de la factura*/
    private Detalleadicional informacionAdicionalDetalleSeleccionado;
    private List<Detalleadicional> detallesAdicionales;
    private String nombreInfoAdicionalDetalle;
    private String valorInfoAdicionalDetalle;
    private String codigoProducto;
    private String descripcion = "";
    private String unidadMedida;
    private BigDecimal precioCosto;
    private BigDecimal porcentajeVenta;
    private BigDecimal porcentajeVentaDos;
    private BigDecimal precioVenta;
    private String texto;
    private BigDecimal precioVentaDos;
    private String cantidad;
    private String codigoProductoBusqueda;
    private String estadoBusqueda;

    public BeanAdministracionProductosServicios() {
        guardarLogRegistros("Acceso al modulo Administracion Productos");
        cargarDatatableConProductos();
//        precioCosto = new BigDecimal(BigInteger.ZERO);
//        precioCosto.setScale(4, BigDecimal.ROUND_HALF_UP);
//        porcentajeVenta = new BigDecimal(BigInteger.ZERO);
//        porcentajeVenta.setScale(4, BigDecimal.ROUND_HALF_UP);
//        porcentajeVentaDos = new BigDecimal(BigInteger.ZERO);
//        porcentajeVentaDos.setScale(4, BigDecimal.ROUND_HALF_UP);
//        precioVenta = new BigDecimal(BigInteger.ZERO);
//        precioVenta.setScale(4, BigDecimal.ROUND_HALF_UP);
//        precioVentaDos = new BigDecimal(BigInteger.ZERO);
//        precioVentaDos.setScale(4, BigDecimal.ROUND_HALF_UP);

        this.nuevoProducto = new Producto();
        detallesAdicionales = new ArrayList<Detalleadicional>();
        listarTipoImpuesto();
    }

    public void guardarLogRegistros(String mensaje) {
        try {
            ControlSesion sesion = new ControlSesion();
            DAOUsuarioAcceso daoacceso = new DAOUsuarioAcceso();
            DAOAuditoria auditoria = new DAOAuditoria();
            Auditoria insertAudi = new Auditoria();
            insertAudi.setFecha(new Date());
            insertAudi.setFechaHora(new Date());
            insertAudi.setMensajeTransaccion(mensaje);
            insertAudi.setUsuarioAcceso(daoacceso.obtenerUsuarioAccesoPorId(Integer.parseInt(sesion.obtenerIdUsuarioSesionActiva())));
            auditoria.insertarRegistro(insertAudi);
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarDialogEstadoPerfil() {
        if (productoSeleccionado != null) {

            RequestContext.getCurrentInstance().execute("PF('estadoPerfil').show();");

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla.");
        }
    }

    public void cambiarEstadoProducto() {
        DAOPermiso1 prod = null;

        try {
            prod = new DAOPermiso1();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
        }
        String nuevo_estado = "0";
        if (productoSeleccionado.getEstado().equals("1")) {
            nuevo_estado = "0";
        } else {
            nuevo_estado = "1";
        }
        boolean actualizado = prod.cambiarEstadoProducto(productoSeleccionado.getIdproducto(), nuevo_estado);
        if (actualizado) {
            guardarLogRegistros("Cambio  estado de producto");
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Ha cambiado el estado del producto.");
            cargarDatatableConProductos();
            RequestContext.getCurrentInstance().execute("PF('estadoPerfil').hide();");

            quitarSeleccionDatatable(nuevo_estado);

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha cambiado el estado del producto");
        }
    }

    private void quitarSeleccionDatatable(String estado) {
//        RequestContext.getCurrentInstance().execute("PF('dt-productos').unselectAllRows();");
        productoSeleccionado.setEstado(estado);
    }

    /**
     *
     * Método que carga el datatable con los productos por establecimiento y
     * punto de emisión que tiene la empresa que ha iniciado sesión
     */
    private void cargarDatatableConProductos() {
        try {
            ControlSesion ms = new ControlSesion();
            DAOPermiso1 prod = new DAOPermiso1();
            productos = prod.obtenerPermiso();
            if (productos != null && !productos.isEmpty()) {
//                    info_empresa = productos.get(0).getEmpresa().getNombreComercialClienteEmpresa() + " RUC: " + productos.get(0).getClienteEmpresa().getIdClienteEmpresa();
            }

        } catch (Exception e) {
            Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, "Error al cargar los productos", e);
        }
    }

    public void handleKeyEvent() {
        texto = precioCosto.toString().toUpperCase();
        if (porcentajeVenta != null || porcentajeVentaDos != null) {
            if (porcentajeVenta != null) {
                String texto2 = porcentajeVenta.toString().toUpperCase();
                BigDecimal valormutilplica = new BigDecimal(texto2).divide(new BigDecimal(100));
                precioVenta = precioCosto;
                precioVenta = precioVenta.add(precioVenta.multiply(valormutilplica));
                precioVenta = precioVenta.setScale(4, BigDecimal.ROUND_DOWN);

            }
            if (porcentajeVentaDos != null) {
                String texto3 = porcentajeVentaDos.toString().toUpperCase();
                BigDecimal valormutilplica = new BigDecimal(texto3).divide(new BigDecimal(100));
                precioVentaDos = precioCosto;
                precioVentaDos = precioVentaDos.add(precioVentaDos.multiply(valormutilplica));
                precioVentaDos = precioVentaDos.setScale(4, BigDecimal.ROUND_DOWN);
            }
        } else {
            precioVenta = new BigDecimal(texto);
            precioVenta = precioVenta.setScale(4, BigDecimal.ROUND_DOWN);
            precioVentaDos = new BigDecimal(texto);
            precioVentaDos = precioVentaDos.setScale(4, BigDecimal.ROUND_DOWN);
        }
    }

    public void handleKeyEvent2() {
        String texto2 = porcentajeVenta.toString().toUpperCase();
        if (!(new BigDecimal(texto2).compareTo(BigDecimal.ZERO) < 1)) {
            BigDecimal valormutilplica = new BigDecimal(texto2).divide(new BigDecimal(100));
            precioVenta = precioCosto;
            precioVenta = precioVenta.add(precioVenta.multiply(valormutilplica));
            precioVenta = precioVenta.setScale(4, BigDecimal.ROUND_DOWN);
        } else {
            precioVenta = precioCosto;
            precioVenta = precioVenta.setScale(4, BigDecimal.ROUND_DOWN);
        }

    }

    public void handleKeyEvent3() {
        String texto3 = porcentajeVentaDos.toString().toUpperCase();
        if (!(new BigDecimal(texto3).compareTo(BigDecimal.ZERO) < 1)) {
            BigDecimal valormutilplica = new BigDecimal(texto3).divide(new BigDecimal(100));
            precioVentaDos = precioCosto;
            precioVentaDos = precioVentaDos.add(precioVentaDos.multiply(valormutilplica));
            precioVentaDos = precioVentaDos.setScale(4, BigDecimal.ROUND_DOWN);

        } else {
            precioVentaDos = precioCosto;
            precioVentaDos = precioVentaDos.setScale(4, BigDecimal.ROUND_DOWN);
        }

    }

    public void listarTipoImpuesto() {
        try {
            DAOTarifasImpuesto daotarifas = new DAOTarifasImpuesto();
            listaTarifaImpuestoICE = daotarifas.obtenerImpuestoICE();
            listaTarifaImpuestoIVA = daotarifas.obtenerImpuestoIVA();

        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que verifica si existen ingresados como máximo 3 detalles
     * adicionales en el detalle y muestra el diálogo
     */
    public void verificarIngresoDetallesAdicionales() {
        if (detallesAdicionales != null && detallesAdicionales.size() < 3) {
            nombreInfoAdicionalDetalle = null;
            valorInfoAdicionalDetalle = null;
            RequestContext.getCurrentInstance().execute("PF('dialogDetalleAdicionalWV').show();");
        } else {
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ha ingresado el máximo de Detalles Adicionales permitidos.");
        }
    }

    private void obtenerProductoPorId() {
//        try {
//            productoSeleccionado = this.instanciarDAO().obtenerProductoPorId(productoSeleccionado.getIdproducto());
//        } catch (Exception ex) {
//            Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     *  /**
     * Método que instancia el DAOProductos con la sesión de Hibernate.
     *
     * @return Objeto de tipo DAOProductos
     *
     */
    private DAOProductos instanciarDAO() {
        DAOProductos dao_productos = null;
//        try {
//            dao_productos = new DAOProductos();
//        } catch (Exception ex) {
//            Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return dao_productos;
    }

    /**
     * Método para validar que el producto no se repita
     *
     * @param _descripcion
     * @param _precioUnitario
     * @return
     */
    public boolean validarProductoRepetido(String _descripcion, String _codigoProducto) {
        boolean respuesta = false;
        for (Producto producto : productos) {
            if (producto.getDescripcion().equals(_descripcion) && producto.getCodigoProducto().equals(_codigoProducto)) {
                respuesta = true;
                break;
            }
        }
        return respuesta;
    }

    /**
     * Método para validar que el producto no se repita
     *
     * @param _descripcion
     * @param _precioUnitario
     * @return
     */
    public boolean validarProductoRepetido2(String _codigoProducto) {
        boolean respuesta = false;
        for (Producto producto : productos) {
            if (producto.getCodigoProducto().equals(_codigoProducto)) {
                respuesta = true;
                break;
            }
        }
        return respuesta;
    }

    public void cargarEstadoActualProducto() {
//        if (productoSeleccionado != null) {
//            productoEdicion = new Producto();
//            productoEdicion.setCodigoProducto(productoSeleccionado.getCodigoProducto());
//            productoEdicion.setDescripcion(productoSeleccionado.getDescripcion());
//            productoEdicion.setUnidadMedida(productoSeleccionado.getUnidadMedida());
//            productoEdicion.setCantidad(productoSeleccionado.getCantidad());
//            productoEdicion.setPrecioCosto(productoSeleccionado.getPrecioCosto());
//            productoEdicion.setPorcentajeVenta(productoSeleccionado.getPorcentajeVenta());
//            productoEdicion.setPrecioVenta(productoSeleccionado.getPrecioVenta());
//            productoEdicion.setPorcentajeVentaDos(productoSeleccionado.getPorcentajeVentaDos());
//            productoEdicion.setPrecioVentaDos(productoSeleccionado.getPrecioVentaDos());
//            RequestContext.getCurrentInstance().execute("PF('wv-dialog-actualizar-producto').show();");
//        }
    }

    /**
     * Método que valida el detalle de la factura antes de ingresarlo
     */
    public boolean validarProducto() {

        boolean respuesta = false;
        try {
            DAOPermiso1 dp = new DAOPermiso1();
            ControlSesion cs = new ControlSesion();
            if (codigoProducto == null || codigoProducto.trim().equals("")) {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un Código Principal para el Producto.");

            } else if (dp.obtenerProductoPorCodigoPrincipal(codigoProducto) != null) {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El Código Principal ingresado ya pertenece a otro producto. Ingrese otro.");
            } else if (descripcion == null || descripcion.trim().equals("")) {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una Descripción para el Producto.");
            } else if (precioCosto.compareTo(BigDecimal.ZERO) < 1) {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una precio mayor a cero.");
            } else if (Utilidades.BorrarCaract(precioCosto.toString(), "\\.").length() > 18) {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número de máximo 18 dígitos.");
            } else if (precioCosto.scale() > 4) {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número con máximo 4 decimales.");
            } else if (validarProductoRepetido(nuevoProducto.getDescripcion(), nuevoProducto.getCodigoProducto())) {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ya existe un producto registrado con la misma descripción y precio.");
            } else {
                respuesta = true;

            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public void actualizarProducto(Integer _opcion) {
        try {
            Producto productoActualizado = new Producto();
            DAOPermiso1 a = new DAOPermiso1();
            boolean actualizar = false;
            boolean actualizarDetalle = false;
            productoActualizado.setIdproducto(productoSeleccionado.getIdproducto());
//            productoActualizado.setCodigoProducto(productoSeleccionado.getCodigoProducto());
            productoActualizado.setCantidad(productoSeleccionado.getCantidad());
//            productoActualizado.setDescripcion(productoSeleccionado.getDescripcion());
            productoActualizado.setPrecioCosto(productoSeleccionado.getPrecioCosto());
            productoActualizado.setPorcentajeVenta(productoSeleccionado.getPorcentajeVenta());
            productoActualizado.setPorcentajeVentaDos(productoSeleccionado.getPorcentajeVentaDos());
            productoActualizado.setPrecioVenta(productoSeleccionado.getPrecioVenta());
            productoActualizado.setPrecioVentaDos(productoSeleccionado.getPrecioVentaDos());
            productoActualizado.setUnidadMedida(productoSeleccionado.getUnidadMedida());
            switch (_opcion) {
                case 1:
                    if (codigoProducto == null || codigoProducto.trim().equals("")) {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un Código Principal para el Producto.");
                    } else {

                        productoActualizado.setCodigoProducto(codigoProducto);
                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, " Código Principal actualizado.");
                    }
                    break;
                case 2:
                    if (descripcion == null || descripcion.trim().equals("")) {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una descripcion para el Producto.");
                    } else {
                        productoActualizado.setDescripcion(descripcion);
                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Descripcion actualizada.");
                    }
                    break;
                case 3:
                    if (unidadMedida == null || unidadMedida.trim().equals("")) {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una unidad de Medida para el Producto.");
                    } else {
                        productoActualizado.setUnidadMedida(unidadMedida);
                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, " Unidad de Medida actualizada.");
                    }
                    break;
//                case 4:
//                    if (cantidad == null || cantidad.trim().equals("")) {
//                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una cantidad para el Producto.");
//                    } else {
//                        productoActualizado.setCantidad(cantidad);
//                    }
//                    break;
                case 4:
                    boolean aux4 = true;
                    if (precioCosto != null) {
                        if (!precioCosto.equals("")) {
                            if (Validaciones.esDecimal(precioCosto.toString())) {
                                if (precioCosto.compareTo(BigDecimal.ZERO) < 1) {
                                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un precio mayor a cero.");
                                    aux4 = false;
                                } else {
                                    if (Utilidades.BorrarCaract(precioCosto.toString(), "\\.").length() > 18) {
                                        aux4 = false;
                                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número de máximo 18 dígitos.");
                                    } else {
                                        //validacion decimales antes 6
                                        if (precioCosto.scale() > 4) {
                                            aux4 = false;
                                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número con máximo 4 decimales.");
                                        }
                                    }
                                }
                            } else {
                                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el precio.");
                            }

                        } else {
                            aux4 = false;
                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo precio es obligatorio.");
                        }
                        if (aux4) {
                            mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, " Precio actualizado.");
                            productoActualizado.setPrecioCosto(precioCosto.setScale(4, BigDecimal.ROUND_DOWN).toString());
                            BigDecimal valor1 = precioCosto.add(precioCosto.multiply(porcentajeVenta.divide(new BigDecimal(100))));
                            BigDecimal valor2 = precioCosto.add(precioCosto.multiply(porcentajeVentaDos.divide(new BigDecimal(100))));
                            productoActualizado.setPrecioVenta(valor1.setScale(4, BigDecimal.ROUND_DOWN).toString());
                            productoActualizado.setPrecioVentaDos(valor2.setScale(4, BigDecimal.ROUND_DOWN).toString());
                            RequestContext.getCurrentInstance().execute("PF('wv-dialog-editar-producto').hide();");
                        }

                    } else {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un precioCosto para el Producto.");
                    }

                    break;

                case 5:
                    boolean aux5 = true;
                    if (porcentajeVenta != null) {
                        if (!porcentajeVenta.equals("")) {
                            if (Validaciones.esDecimal(porcentajeVenta.toString())) {
                                if (porcentajeVenta.compareTo(BigDecimal.ZERO) < 0) {
                                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un porcentaje mayor a cero.");
                                    aux5 = false;
                                } else {
                                    if (Utilidades.BorrarCaract(porcentajeVenta.toString(), "\\.").length() > 18) {
                                        aux5 = false;
                                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje  debe ser un número de máximo 18 dígitos.");
                                    } else {
                                        //validacion decimales antes 6
                                        if (porcentajeVenta.scale() > 4) {
                                            aux5 = false;
                                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje debe ser un número con máximo 4 decimales.");
                                        }
                                    }
                                }
                            } else {
                                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el porcentaje.");
                            }

                        } else {
                            aux5 = false;
                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo porcentaje es obligatorio.");
                        }
                        if (aux5) {
                            productoActualizado.setPorcentajeVenta(porcentajeVenta.setScale(4, BigDecimal.ROUND_DOWN).toString());
                            mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, " Porcentaje de venta actualizado.");
                            BigDecimal valor1 = precioCosto.add(precioCosto.multiply(porcentajeVenta.divide(new BigDecimal(100))));
                            productoActualizado.setPrecioVenta(valor1.setScale(4, BigDecimal.ROUND_DOWN).toString());
                            RequestContext.getCurrentInstance().execute("PF('wv-dialog-editar-producto').hide();");
                        }

                    } else {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un porcentaje para el Producto.");
                    }

                    break;

//                    if (porcentajeVenta == null) {
//                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un porcentajeVenta para el Producto.");
//                    } else {
//
//                        productoActualizado.setPorcentajeVenta("" + porcentajeVenta);
//                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, " Porcentaje de Venta actualizado.");
//                        BigDecimal valor1 = precioCosto.add(precioCosto.multiply(porcentajeVenta.divide(new BigDecimal(100))));
//                        productoActualizado.setPrecioVenta("" + valor1);
//                        RequestContext.getCurrentInstance().execute("PF('wv-dialog-editar-producto').hide();");
//                    }
//                    break;
                case 6:
                    boolean aux6 = true;
                    if (porcentajeVentaDos != null) {
                        if (!porcentajeVentaDos.equals("")) {
                            if (Validaciones.esDecimal(porcentajeVentaDos.toString())) {
                                if (porcentajeVentaDos.compareTo(BigDecimal.ZERO) < 0) {
                                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un porcentaje mayor a cero.");
                                    aux6 = false;
                                } else {
                                    if (Utilidades.BorrarCaract(porcentajeVentaDos.toString(), "\\.").length() > 18) {
                                        aux6 = false;
                                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje  debe ser un número de máximo 18 dígitos.");
                                    } else {
                                        //validacion decimales antes 6
                                        if (porcentajeVentaDos.scale() > 4) {
                                            aux6 = false;
                                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje debe ser un número con máximo 4 decimales.");
                                        }
                                    }
                                }
                            } else {
                                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el porcentaje.");
                            }

                        } else {
                            aux6 = false;
                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo porcentaje es obligatorio.");
                        }
                        if (aux6) {
                            mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, " Porcentaje de venta dos actualizado.");
                            productoActualizado.setPorcentajeVentaDos(porcentajeVentaDos.setScale(4, BigDecimal.ROUND_DOWN).toString());
                            BigDecimal valor2 = precioCosto.add(precioCosto.multiply(porcentajeVentaDos.divide(new BigDecimal(100))));
                            productoActualizado.setPrecioVentaDos(valor2.setScale(4, BigDecimal.ROUND_DOWN).toString());
                            RequestContext.getCurrentInstance().execute("PF('wv-dialog-editar-producto').hide();");
                        }

                    } else {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un porcentaje para el Producto.");
                    }

                    break;

//                    if (porcentajeVentaDos == null) {
//                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un porcentajeVentaDos para el Producto.");
//                    } else {
//                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, " Porcentaje de venta dos actualizado.");
//                        productoActualizado.setPorcentajeVentaDos("" + porcentajeVentaDos);
//                        BigDecimal valor2 = precioCosto.add(precioCosto.multiply(porcentajeVentaDos.divide(new BigDecimal(100))));
//                        productoActualizado.setPrecioVentaDos("" + valor2);
//                        RequestContext.getCurrentInstance().execute("PF('wv-dialog-editar-producto').hide();");
//                    }
//                    break;
                case 7:
                    if (precioVenta == null) {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un precioVenta para el Producto.");
                    } else {
                        productoActualizado.setPrecioVenta("" + precioVenta);
                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Precio de Venta actualizado.");

                    }
                    break;
                case 8:
                    if (precioVentaDos == null) {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un precioVentaDos para el Producto.");
                    } else {
                        productoActualizado.setPrecioVentaDos("" + precioVentaDos);
                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Precio de Venta actualizado.");
                    }
                    break;
                default:
                    actualizar = validarProducto();
                    if (actualizar) {
                        productoActualizado.setIdproducto(productoSeleccionado.getIdproducto());
//                        productoActualizado.setCodigoProducto(codigoProducto);
//                        productoActualizado.setDescripcion(descripcion);
//                        productoActualizado.setPrecioCosto(precioCosto + "");

                        productoActualizado.setCodigoProducto(codigoProducto);
                        productoActualizado.setCantidad(cantidad);
                        productoActualizado.setDescripcion(descripcion);
                        productoActualizado.setPrecioCosto("" + precioCosto);
                        productoActualizado.setPorcentajeVenta("" + porcentajeVenta);
                        productoActualizado.setPorcentajeVentaDos("" + porcentajeVentaDos);
                        productoActualizado.setPrecioVenta("" + precioVenta);
                        productoActualizado.setPrecioVentaDos("" + precioVentaDos);
                        productoActualizado.setUnidadMedida("" + unidadMedida);

                    }
                    break;
            }
            if (!validarProductoRepetido2(productoActualizado.getCodigoProducto())) {

                for (Producto producto : productos) {
                    if (producto.getIdproducto().equals(productoActualizado.getIdproducto())) {
                        Producto actualizars = a.actualizarProducto2(productoSeleccionado.getIdproducto(), productoActualizado);
                        if (actualizars != null) {
                            productoSeleccionado.setCodigoProducto(actualizars.getCodigoProducto());
                            productoSeleccionado.setCantidad(actualizars.getCantidad());
                            productoSeleccionado.setDescripcion(actualizars.getDescripcion());
                            productoSeleccionado.setPrecioCosto(actualizars.getPrecioCosto());
                            productoSeleccionado.setPorcentajeVenta(actualizars.getPorcentajeVenta());
                            productoSeleccionado.setPorcentajeVentaDos(actualizars.getPorcentajeVentaDos());
                            productoSeleccionado.setPrecioVenta(actualizars.getPrecioVenta());
                            productoSeleccionado.setPrecioVentaDos(actualizars.getPrecioVentaDos());
                            productoSeleccionado.setUnidadMedida(actualizars.getUnidadMedida());

                            cargarDatatableConProductos();
                            RequestContext.getCurrentInstance().update(":form-editar-producto:pg-editar-producto");
                        }
                        break;
                    }
                }
            } else if (actualizarDetalle == false) {
//                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha podido realizar la modificación debido a que otro producto contiene la misma descripción y precio unitario.");
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha podido realizar la modificación debido a que otro producto contiene el precio unitario.");

            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que guarda el producto*
     */
    public boolean validarDatosProducto() {
        boolean aux = true;
        boolean aux2 = true;
        boolean aux3 = true;
        boolean aux4 = true;
        boolean aux5 = true;
        boolean aux6 = true;
        boolean aux7 = true;
        boolean aux8 = true;
        boolean aux9 = true;
        boolean respuesta = false;

        nuevoProducto = new Producto();
        nuevoProducto.setEstado("1");
        nuevoProducto.setCantidad("1");
        if (codigoProducto != null && !codigoProducto.trim().equals("")) {
            nuevoProducto.setCodigoProducto(codigoProducto);
        } else {
            aux = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo codigo es obligatorio.");
        }
        if (descripcion != null && !descripcion.trim().equals("")) {
            nuevoProducto.setDescripcion(descripcion);
        } else {
            aux2 = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El camp descripcion es obligatorio.");
        }
        if (unidadMedida != null && !unidadMedida.trim().equals("")) {
            nuevoProducto.setUnidadMedida(unidadMedida);
        } else {
            aux3 = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo unidad de medida es obligatio.");
        }

        if (precioCosto != null || !precioCosto.equals("")) {
            if (Validaciones.esDecimal(precioCosto.toString())) {
                if (precioCosto.compareTo(BigDecimal.ZERO) < 1) {
                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un precio mayor a cero.");
                    aux4 = false;
                } else {
                    if (Utilidades.BorrarCaract(precioCosto.toString(), "\\.").length() > 18) {
                        aux4 = false;
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número de máximo 18 dígitos.");
                    } else {
                        //validacion decimales antes 6
                        if (precioCosto.scale() > 4) {
                            aux4 = false;
                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número con máximo 4 decimales.");
                        }
                    }
                }
            } else {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el precio.");
            }

        } else {
            aux4 = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo precio es obligatorio.");
        }

        if (precioVenta != null || !precioVenta.equals("")) {
            if (Validaciones.esDecimal(precioVenta.toString())) {
                if (precioVenta.compareTo(BigDecimal.ZERO) < 1) {
                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un precio venta  mayor a cero.");
                    aux5 = false;
                } else {
                    if (Utilidades.BorrarCaract(precioVenta.toString(), "\\.").length() > 18) {
                        aux5 = false;
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio venta debe ser un número de máximo 18 dígitos.");
                    } else {
                        if (precioVenta.scale() > 4) {
                            aux5 = false;
                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio venta debe ser un número con máximo 4 decimales.");
                        }
                    }
                }
            } else {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el precio venta.");
            }

        } else {
            aux5 = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo precio venta es obligatorio.");
        }

        if (precioVentaDos != null || !precioVentaDos.equals("")) {
            if (Validaciones.esDecimal(precioVentaDos.toString())) {
                if (precioVentaDos.compareTo(BigDecimal.ZERO) < 1) {
                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un precio venta dos  mayor a cero.");
                    aux6 = false;
                } else {
                    if (Utilidades.BorrarCaract(precioVentaDos.toString(), "\\.").length() > 18) {
                        aux6 = false;
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio venta dos debe ser un número de máximo 18 dígitos.");
                    } else {
                        if (precioVentaDos.scale() > 4) {
                            aux6 = false;
                            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio venta dos debe ser un número con máximo 4 decimales.");
                        }
                    }
                }
            } else {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el precio venta dos.");
            }

        } else {
            aux6 = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo precio venta dos es obligatorio.");
        }

        if (porcentajeVenta != null || !porcentajeVenta.equals("")) {
            if (Validaciones.esDecimal(porcentajeVenta.toString())) {

                if (Utilidades.BorrarCaract(porcentajeVenta.toString(), "\\.").length() > 18) {
                    aux7 = false;
                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje de venta debe ser un número de máximo 18 dígitos.");
                } else {
                    if (porcentajeVenta.scale() > 4) {
                        aux7 = false;
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje de venta debe ser un número con máximo 4 decimales.");
                    }
                }

            } else {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el porcentaje de venta.");
            }

        } else {
            aux7 = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo procentaje de venta es obligatorio.");
        }

        if (porcentajeVentaDos != null || !porcentajeVentaDos.equals("")) {
            if (Validaciones.esDecimal(porcentajeVenta.toString())) {

                if (Utilidades.BorrarCaract(porcentajeVentaDos.toString(), "\\.").length() > 18) {
                    aux8 = false;
                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje de venta dos debe ser un número de máximo 18 dígitos.");
                } else {
                    if (porcentajeVentaDos.scale() > 4) {
                        aux8 = false;
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje de venta dos debe ser un número con máximo 4 decimales.");
                    }
                }

            } else {
                mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para el porcentaje de venta dos.");
            }

        } else {
            aux8 = false;
            mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo porcentaje de venta dos es obligatorio.");
        }

//        if (cantidad != null && !cantidad.equals("")) {
//            if (Validaciones.esDecimal(porcentajeVenta.toString())) {
//                if (new BigDecimal(cantidad).compareTo(BigDecimal.ZERO) < 1) {
//                    aux9 = false;
//                    mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una cantidad  mayor a cero.");
//                }
//            } else {
//                aux9 = false;
//                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número válido para la cantidad.");
//            }
//            
//        } else {
//            aux9 = false;
//            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El campo cantidad es obligatorio.");
//        }
        if (aux4) {
            nuevoProducto.setPrecioCosto(precioCosto.setScale(4, BigDecimal.ROUND_DOWN).toString());

        }
        if (aux5) {

            nuevoProducto.setPrecioVenta(precioVenta.setScale(4, BigDecimal.ROUND_DOWN).toString());

        }

        if (aux6) {

            nuevoProducto.setPrecioVentaDos(precioVentaDos.setScale(4, BigDecimal.ROUND_DOWN).toString());
        }

        if (aux7) {
            nuevoProducto.setPorcentajeVenta(porcentajeVenta.setScale(4, BigDecimal.ROUND_DOWN).toString());
        }
        if (aux8) {

            nuevoProducto.setPorcentajeVentaDos(porcentajeVentaDos.setScale(4, BigDecimal.ROUND_DOWN).toString());
        }
//        if (aux9) {
//            nuevoProducto.setCantidad("" + cantidad);
//        }
        if (aux && aux2 && aux3 && aux4 && aux5 && aux6 && aux7 && aux8) {
            respuesta = true;
        }

        return respuesta;
    }

    public void aceptarGuardarNuevoProducto() {
        try {
            DAOPermiso1 dp = new DAOPermiso1();
            Producto productorepetido = dp.obtenerProductoPorCodigoPrincipal(codigoProducto);
            if (productorepetido == null) {
                if (validarDatosProducto()) {

                    Producto productoAux = dp.insertarProducto(nuevoProducto);
                    if (productoAux != null) {
                        guardarLogRegistros("Agrego nuevo producto");
                        mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Se ha agregado el producto.");
                        cargarDatatableConProductos();
                        cerrarGuardarNuevoProducto();
                    } else {
                        mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha podido agregar el producto a la base de datos.");
                    }

                }
            } else {
                mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El producto con el codigo: " + codigoProducto + " ya se encuntra registrado.");

            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void insertarProducto() {
//        nuevoProducto = new Producto();
//        nuevoProducto.setCodigoPrincipal(this.codigoPrincipal);
//        nuevoProducto.setCodigoAuxiliar(this.codigoAuxiliar);
//        nuevoProducto.setDescripcion(this.descripcion);
//        nuevoProducto.setPrecioUnitario(this.precioUnitario.toString());
//        nuevoProducto.setEstado(true);
//        if (validarProducto()) {
//            try {
//                ControlSesion cs = new ControlSesion();
//                DAOClienteEmpresa dce = new DAOClienteEmpresa();
//                ClienteEmpresa ce = dce.obtenerClienteEmpresaPorId(cs.obtenerRUCEmpresaSesionActiva());
//                this.nuevoProducto.setClienteEmpresa(ce);
//                if (this.instanciarDAO().insertarProducto(this.nuevoProducto) == null) {
//                    mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el nuevo producto.");
//                } else {
//                    mostrarMensaje(FacesMessage.SEVERITY_INFO, "Se ha agregado el nuevo producto.");
//                    this.productos.add(this.nuevoProducto);
//                    cerrarDialogoNuevo();
//                }
//            } catch (Exception ex) {
//                Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }

    }

    public void onRowSelect(SelectEvent event) {

    }

    public void onRowUnselect(UnselectEvent event) {

    }

    /*Método para verificar si es un nuevo producto*/
    public boolean verificarNuevoProducto() {
        boolean respuesta = true;
        try {
            ControlSesion cs = new ControlSesion();
            DAOPermiso1 dp = new DAOPermiso1();
            Producto producto = dp.obtenerProductoporId(codigoProducto);
            if (producto == null) {
                respuesta = false;

            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    /*Método que cancela el guardado del producto*/
    public void cerrarGuardarNuevoProducto() {
        RequestContext.getCurrentInstance().execute("PF('wv-dialog-nuevo-producto').hide()");
        encerarDetalles();
    }

    public void encerarDetalles() {

        codigoProducto = "";
        descripcion = "";
//        precioCosto = new BigDecimal("0.0000");
        precioVenta = new BigDecimal("0.0000");
        precioVentaDos = new BigDecimal("0.0000");
//        porcentajeVenta = new BigDecimal("0.0000");
//        porcentajeVentaDos = new BigDecimal("0.0000");
        unidadMedida = "";

        idImpuestoIVASeleccionado = listaTarifaImpuestoIVA.get(0).getCodigoTarifaImpuesto();
        idImpuestoICESeleccionado = "-1";

    }

    /**
     * Método que elimina un detalle adicional del detalle de la factura
     */
    public void eliminarDetalleAdicional() {
        if (detallesAdicionales != null && !detallesAdicionales.isEmpty()) {
            detallesAdicionales.remove(informacionAdicionalDetalleSeleccionado);
        }
    }

    public void borrarBusquedaCodigo() {
        codigoProductoBusqueda = null;
        descripcion = null;
        estadoBusqueda = null;
    }

    public void editarProducto() {
        if (productoSeleccionado != null) {
            Producto producto = new Producto();
            ControlSesion cs = new ControlSesion();
            if (productoSeleccionado != null) {
                setCodigoProducto(productoSeleccionado.getCodigoProducto());
                setDescripcion(productoSeleccionado.getDescripcion());
                setPrecioCosto(new BigDecimal(productoSeleccionado.getPrecioCosto()));
                setPrecioVenta(new BigDecimal(productoSeleccionado.getPrecioVenta()));
                setPrecioVentaDos(new BigDecimal(productoSeleccionado.getPrecioVentaDos()));
                setPorcentajeVenta(new BigDecimal(productoSeleccionado.getPorcentajeVenta()));
                setPorcentajeVentaDos(new BigDecimal(productoSeleccionado.getPorcentajeVentaDos()));
                setUnidadMedida(productoSeleccionado.getUnidadMedida());
                RequestContext.getCurrentInstance().execute("PF('wv-dialog-editar-producto').show();");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla.");
        }

//        try {
//            if (productoSeleccionado != null) {
//                Producto producto = new Producto();
//                ControlSesion cs = new ControlSesion();
//                DAODetalleAdicional dda = new DAODetalleAdicional();
//                setCodigoPrincipal(productoSeleccionado.getCodigoPrincipal());
//                if (productoSeleccionado.getCodigoAuxiliar() != null && !productoSeleccionado.getCodigoAuxiliar().equals("")) {
//                    setCodigoAuxiliar(productoSeleccionado.getCodigoAuxiliar());
//                } else {
//                    setCodigoAuxiliar("N/A");
//                }
//                setDescripcion(productoSeleccionado.getDescripcion());
//                setPrecioUnitario(new BigDecimal(productoSeleccionado.getPrecioUnitario()));
//                setEstado(productoSeleccionado.getEstado());
//                idImpuestoIVASeleccionado = productoSeleccionado.getCodigoIva();
//                if (productoSeleccionado.getCodigoIce() != null) {
//                    idImpuestoICESeleccionado = productoSeleccionado.getCodigoIce();
//                }
//                this.detallesAdicionales = dda.obtenerDetallesAdicionalesporIdProducto(productoSeleccionado.getIdproducto());;
//                RequestContext.getCurrentInstance().execute("PF('wv-dialog-editar-producto').show();");
//            } else {
//                mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public List<String> autoCompleteProductos(String query) {
        List<String> clientes_filtrados = new ArrayList<String>();
        for (int i = 0; i < this.productos.size(); i++) {
            Producto cliente_tmp = this.productos.get(i);
            if (cliente_tmp.getCodigoProducto().toLowerCase().startsWith(query)) {
                clientes_filtrados.add(cliente_tmp.getCodigoProducto());
            }
        }
        return clientes_filtrados;
    }

    public List<String> autoCompleteProductosDescripcion(String query) {
        List<String> clientes_filtrados = new ArrayList<String>();
        for (int i = 0; i < this.productos.size(); i++) {
            Producto cliente_tmp = this.productos.get(i);
            if (cliente_tmp.getDescripcion().toLowerCase().startsWith(query)) {
                clientes_filtrados.add(cliente_tmp.getDescripcion());
            }
        }
        return clientes_filtrados;
    }

    public void buscarProductos() {
        try {
            DAOPermiso1 dp = new DAOPermiso1();
            if (codigoProductoBusqueda != null) {
                productos = dp.buscarProductosInternosVarios(estadoBusqueda, codigoProductoBusqueda, descripcion);
            } else {
                cargarDatatableConProductos();
//            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un codigo para el producto.");    

            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void abrirDialogoNuevo() {
        this.nuevoProducto = new Producto();
        encerarDetalles();
        RequestContext.getCurrentInstance().execute("PF('wv-dialog-nuevo-producto').show();");
    }

    public void cerrarDialogoNuevo() {
        RequestContext.getCurrentInstance().execute("PF('wv-dialog-nuevo-producto').hide();");
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public String getInfo_empresa() {
        return info_empresa;
    }

    public void setInfo_empresa(String info_empresa) {
        this.info_empresa = info_empresa;
    }

    public Producto getNuevoProducto() {
        return nuevoProducto;
    }

    public void setNuevoProducto(Producto nuevoProducto) {
        this.nuevoProducto = nuevoProducto;
    }

    public List<TarifasImpuesto> getListaTarifaImpuestoIVA() {
        return listaTarifaImpuestoIVA;
    }

    public void setListaTarifaImpuestoIVA(List<TarifasImpuesto> listaTarifaImpuestoIVA) {
        this.listaTarifaImpuestoIVA = listaTarifaImpuestoIVA;
    }

    public String getIdImpuestoIVASeleccionado() {
        return idImpuestoIVASeleccionado;
    }

    public void setIdImpuestoIVASeleccionado(String idImpuestoIVASeleccionado) {
        this.idImpuestoIVASeleccionado = idImpuestoIVASeleccionado;
    }

    public String getIdImpuestoICESeleccionado() {
        return idImpuestoICESeleccionado;
    }

    public void setIdImpuestoICESeleccionado(String idImpuestoICESeleccionado) {
        this.idImpuestoICESeleccionado = idImpuestoICESeleccionado;
    }

    public List<TarifasImpuesto> getListaTarifaImpuestoICE() {
        return listaTarifaImpuestoICE;
    }

    public void setListaTarifaImpuestoICE(List<TarifasImpuesto> listaTarifaImpuestoICE) {
        this.listaTarifaImpuestoICE = listaTarifaImpuestoICE;
    }

    public Producto getProductoEdicion() {
        return productoEdicion;
    }

    public void setProductoEdicion(Producto productoEdicion) {
        this.productoEdicion = productoEdicion;
    }

    public Detalleadicional getInformacionAdicionalDetalleSeleccionado() {
        return informacionAdicionalDetalleSeleccionado;
    }

    public void setInformacionAdicionalDetalleSeleccionado(Detalleadicional informacionAdicionalDetalleSeleccionado) {
        this.informacionAdicionalDetalleSeleccionado = informacionAdicionalDetalleSeleccionado;
    }

    public List<Detalleadicional> getDetallesAdicionales() {
        return detallesAdicionales;
    }

    public void setDetallesAdicionales(List<Detalleadicional> detallesAdicionales) {
        this.detallesAdicionales = detallesAdicionales;
    }

    public String getNombreInfoAdicionalDetalle() {
        return nombreInfoAdicionalDetalle;
    }

    public void setNombreInfoAdicionalDetalle(String nombreInfoAdicionalDetalle) {
        this.nombreInfoAdicionalDetalle = nombreInfoAdicionalDetalle;
    }

    public String getValorInfoAdicionalDetalle() {
        return valorInfoAdicionalDetalle;
    }

    public void setValorInfoAdicionalDetalle(String valorInfoAdicionalDetalle) {
        this.valorInfoAdicionalDetalle = valorInfoAdicionalDetalle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

    public BigDecimal getPorcentajeVenta() {
        return porcentajeVenta;
    }

    public void setPorcentajeVenta(BigDecimal porcentajeVenta) {
        this.porcentajeVenta = porcentajeVenta;
    }

    public BigDecimal getPorcentajeVentaDos() {
        return porcentajeVentaDos;
    }

    public void setPorcentajeVentaDos(BigDecimal porcentajeVentaDos) {
        this.porcentajeVentaDos = porcentajeVentaDos;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public BigDecimal getPrecioVentaDos() {
        return precioVentaDos;
    }

    public void setPrecioVentaDos(BigDecimal precioVentaDos) {
        this.precioVentaDos = precioVentaDos;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoProductoBusqueda() {
        return codigoProductoBusqueda;
    }

    public void setCodigoProductoBusqueda(String codigoProductoBusqueda) {
        this.codigoProductoBusqueda = codigoProductoBusqueda;
    }

    public String getEstadoBusqueda() {
        return estadoBusqueda;
    }

    public void setEstadoBusqueda(String estadoBusqueda) {
        this.estadoBusqueda = estadoBusqueda;
    }

}
