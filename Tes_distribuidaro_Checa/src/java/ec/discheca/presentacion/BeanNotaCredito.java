/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Validaciones;
import ec.discheca.claveAcceso.ClaveAcceso;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.comprobanteelectronico.construccionxml.GeneradorComprobanteElectronicoXML;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.InformacionTributariaComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.factura.Detalle;
import ec.discheca.comprobanteelectronico.esquema.notacredito.InformacionNotaCredito;
import ec.discheca.comprobanteelectronico.esquema.implementacion.ImplementacionNotaCredito;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOClienteEmpresa;
import ec.discheca.dao.DAOComprobanteElectronico;
import ec.discheca.dao.DAOReceptor;
import ec.discheca.dao.DAOSecuencial;
import ec.discheca.dao.DAOTarifasImpuesto;
import ec.discheca.modelo.Empresa;
import ec.discheca.modelo.Receptor;
import ec.discheca.modelo.Secuencial;
import ec.discheca.modelo.TarifasImpuesto;
import ec.discheca.serviciofacturacion.AlmacenamientoComprobanteElectronico;
import ec.discheca.serviciofacturacion.Calculos;
import ec.discheca.serviciofacturacion.FirmaAutorizacion;
import ec.discheca.firma.sri.produccion.RespuestaSRI;
import discheca.utilidades.TransformadorArchivos;
import discheca.utilidades.Utilidades;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOPermiso1;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Producto;
import ec.discheca.serviciofacturacion.Notificacion;
import ec.discheca.utilidades.MensajesPrimefaces;
import static ec.discheca.utilidades.MensajesPrimefaces.mostrarMensajeDialog;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.context.RequestContext;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@ManagedBean
@ViewScoped
public class BeanNotaCredito {

    private boolean tab1;
    private boolean tab2;
    private boolean tab3;
    private boolean tab4;
    private boolean tab5;
    private Integer indexTab;

    private boolean panelDetalle;
    /*Datos Emisor*/
    Empresa emisor;
    private String numResolucionEmisor;
    private String contabilidadEmisor;
    private String tipoAmbienteEmisor;

//    private List<Clientes> listaClientes;
    private Empresa clienteSeleccionado;
    private String idBuscar;

//    private ClienteEmpresa clienteEmpresaReceptor;
    private Receptor clienteEmpresaReceptor;

    private String rise;

    private String codigoDocModificado;

    private String numDocModificado;

    private Date fechaEmisionDocSustento;

    private String valorModificacion;

    private String motivo;

    private Empresa clienteEmpresaEmisor;

    private ImplementacionNotaCredito notacredito_electronica;

    /**
     * Atributos para ingresar un detalle adicional
     */
    private ObjetoListaDetallesPresentacion detalleSeleccionado;
    private InformacionAdicional informacionAdicionalDetalleSeleccionado;
    private BigDecimal cantidad;
    private String descripcion;
    private String precioUnitario;
    private BigDecimal descuento;
    private boolean tieneDescuento;
    private String impuestoIVASeleccionado;
    private String impuestoICESeleccionado;

    /**
     * Información adicional para el detalle de la notacredito
     */
    private List<InformacionAdicional> detallesAdicionales;
    private String nombreInfoAdicionalDetalle;
    private String valorInfoAdicionalDetalle;

    private Secuencial secuencial;
    private Integer codigoSecuencialSeleccionado;

    private List<Secuencial> secuenciales;

    private List<Receptor> receptores;

    private List<InformacionAdicional> informacion_adicional;

    /*Datos Receptor*/
    private int identificacion;
    private int tipoIdentificacion;
    private String razonSocialComprador;
    private String identificacionComprador;
    private Date fechaEmision;
    private String establecimiento;
    private String puntoEmision;

    private String Apagar = "false";
    private String apagarfirmado = "false";
    private boolean botonFirmar;

    private String codigoPrincipal;
    private String codigoAuxiliar;
    private BigDecimal precioUnitarioDetalle;
    private BigDecimal totalSinImpuestoDetalle;
    private BigDecimal descuentoDetalle;
    private List<TarifasImpuesto> listaTarifaImpuestoIVA;
    private Integer idImpuestoIVASeleccionado;
    private List<TarifasImpuesto> listaTarifaImpuestoICE;
    private Integer idImpuestoICESeleccionado;
    private List<ObjetoListaDetallesPresentacion> listaDetalles = new ArrayList<ObjetoListaDetallesPresentacion>();
    private ObjetoListaDetallesPresentacion oDetalleSeleccionado;
    private List<ImpuestoComprobanteElectronico> listaImpuestoDetalle = new ArrayList<ImpuestoComprobanteElectronico>();
    //Datos ComprobanteElectronico
    BigDecimal subtotal12C = BigDecimal.ZERO;
    BigDecimal subtotal0C = BigDecimal.ZERO;
    BigDecimal subtotalNoIvaC = BigDecimal.ZERO;
    BigDecimal subtotalC = BigDecimal.ZERO;
    BigDecimal totalDescuentoComprobanteC = BigDecimal.ZERO;
    BigDecimal valorICEC = BigDecimal.ZERO;
    BigDecimal iva12C = BigDecimal.ZERO;
    BigDecimal valorTotalC = BigDecimal.ZERO;
    BigDecimal propina = BigDecimal.ZERO;
    private boolean datosCliente;

    private List<InformacionAdicional> listaDetallesAdicionales = new ArrayList<InformacionAdicional>();
    //Datos Informacion Adicional ComprobanteElectronico
    private boolean infoAdicional;
    private String nombreInfoAdicional;
    private String valorInfoAdicional;
    private List<InformacionAdicional> listaInfoAdicional = new ArrayList<InformacionAdicional>();
    String direccion;
    private String mensajeError = "";
    private InformacionAdicional infoAdicionalSeleccionada;
    ObjetoListaDetallesPresentacion dSeleccionado;
    private Integer opcionesDescuento;
    //variables para modificar el detalle
    private String codigoPrincipalE;
    private String codigoAuxiliarE;
    private BigDecimal cantidadE;
    private String descripcionE;
    private BigDecimal precioUnitarioDetalleE;

    private List<Producto> productos;
    private List<Producto> productosbusqueda;
    private List<Producto> productosTotales;
    private Producto producto = new Producto();
    private Producto productoSeleccionado;
    private String valorSelecionado;
    private boolean DatosDetalles;
    private String edicionDescripcion;
    private BigDecimal edicionCantidad;
    private BigDecimal edicionPrecioUnitario;
    private BigDecimal edicionDescuento;
    private String edicionValorDetalle;
    private String mensajeResultado;

    @PostConstruct
    void initialiseSession() {
        FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    }

    public BeanNotaCredito() {
        guardarLogRegistros("Acceso al modulo generacion Nota De Credito");
        cantidad = new BigDecimal(BigInteger.ONE);
        DatosDetalles = false;
        this.setIndexTab(0);
        this.habilitarTab(1);
        emisor = new Empresa();
        ControlSesion manejoSesion = new ControlSesion();
        if (manejoSesion.obtenerIdPerfilUsuarioSesionActiva().equals("3")) {
            botonFirmar = Boolean.TRUE;
        } else {
            botonFirmar = Boolean.FALSE;
        }
        panelDetalle = false;
        datosCliente = false;
        tieneDescuento = false;
        opcionesDescuento = 1;
        fechaEmision = new Date();
        informacion_adicional = new ArrayList<InformacionAdicional>();
        detallesAdicionales = new ArrayList<InformacionAdicional>();
        detalleSeleccionado = null;
        oDetalleSeleccionado = null;
        identificacion = 7;
        cargarReceptores();
        listarTipoImpuesto();
        cargarDatosEmisor();
        cargarProductos();

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

    public void limpiarovalores() {
        nombreInfoAdicionalDetalle = null;
        valorInfoAdicionalDetalle = null;
    }

    public void verificarEdicionDetalle() {
        if (detalleSeleccionado != null) {
            setEdicionDescripcion(detalleSeleccionado.getDetalleComprobante().getDescripcion());
            setEdicionCantidad(new BigDecimal(detalleSeleccionado.getDetalleComprobante().getCantidad()));
            setEdicionPrecioUnitario(new BigDecimal(detalleSeleccionado.getDetalleComprobante().getPrecioUnitario()));
            setEdicionDescuento(new BigDecimal(detalleSeleccionado.getDetalleComprobante().getDescuento()));
            RequestContext.getCurrentInstance().execute("PF('detalle-cantidades').show();");
        }
    }

    public void cargarProductos() {
        try {
            DAOPermiso1 permiso = new DAOPermiso1();
            productosTotales = permiso.obtenerPermiso();
        } catch (Exception ex) {
            Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String obtenerSecuencialActivoCredito(String secuencial) {
        return Validaciones.completarSecuencial(secuencial);
    }

    public List<String> autoCompleteProductos(String query) {
        List<String> clientes_filtrados = new ArrayList<String>();
        for (int i = 0; i < this.productosTotales.size(); i++) {
            Producto cliente_tmp = this.productosTotales.get(i);
            if (cliente_tmp.getCodigoProducto().toLowerCase().startsWith(query)) {
                clientes_filtrados.add(cliente_tmp.getCodigoProducto());
            }
        }
        return clientes_filtrados;
    }

    /**
     * Método que valida el detalle de la factura antes de ingresarlo
     */
//    public boolean validarDetalle() {
//        boolean respuesta = true;
//        if(producto!=null){
//            codigoPrincipal=producto.getCodigoProducto();
//            descripcion=producto.getDescripcion();
//            precioUnitario=new BigDecimal(valorSelecionado);  
//        }
//        
//        if (codigoAuxiliar != null && !codigoAuxiliar.trim().equals("")) {
//            if (codigoPrincipal == null || codigoPrincipal.trim().equals("")) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un Código Principal antes de ingresar un Código Auxiliar.");
//                respuesta = false;
//            }
//        }
//        if (respuesta) {
//            if (descripcion == null || descripcion.trim().equals("")) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una Descripción para el Detalle.");
//                respuesta = false;
//            } else if (cantidad == null || (cantidad.compareTo(BigDecimal.ZERO) < 1)) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una cantidad mayor a cero.");
//                respuesta = false;
//            } else if (!Validaciones.validarNumero(cantidad.toString()) || Utilidades.BorrarCaract(cantidad.toString(), "\\.").length() > 18) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La cantidad debe ser un número de máximo 18 dígitos.");
//                respuesta = false;
//            } else if (cantidad.scale() > 6) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La cantidad debe ser un número con máximo 6 decimales..");
//                respuesta = false;
//            } else if (precioUnitario == null || (precioUnitario.compareTo(BigDecimal.ZERO) < 1)) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar una precio mayor a cero.");
//                respuesta = false;
//            } else if (!Validaciones.validarNumero(precioUnitario.toString()) || Utilidades.BorrarCaract(precioUnitario.toString(), "\\.").length() > 18) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número de máximo 18 dígitos.");
//                respuesta = false;
//            } else if (precioUnitario.scale() > 6) {
//                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio debe ser un número con máximo 6 decimales.");
//                respuesta = false;
//            } else if (descuento.compareTo(BigDecimal.ZERO) == 1) {
//                if (Utilidades.BorrarCaract(descuento.toString(), "\\.").length() > 14) {
//                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El descuento debe tener máximo 14 dígitos.");
//                    respuesta = false;
//                }
//            }
//        }
//        return respuesta;
//    }
    public boolean validarDatosBusquedaDocumento() {
        boolean aux = true;
        boolean aux2 = true;
        boolean aux3 = true;
        if (fechaEmisionDocSustento == null) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar una fecha de emisión del Documento de Sustento.");
            aux = false;
        }
        if (codigoDocModificado.equals("-1")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el tipo de Documento de Sustento.");
            aux2 = false;
        }
        if (numDocModificado == null || numDocModificado.trim().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el número del Documento de Sustento.");
            aux3 = false;
        }
        return aux && aux2 && aux3;

    }

    public boolean buscarDocumentoAsociadoANotaCredito() {
        codigoDocModificado = "01";
        ControlSesion ms = new ControlSesion();

        boolean existeDocumentoSustento = false;

        if (validarDatosBusquedaDocumento()) {
            String numero[] = numDocModificado.split("-");
            try {
//                ObjetoListaDetallesPresentacion odp = new ObjetoListaDetallesPresentacion();

                DAOComprobanteElectronico dAOComprobante = new DAOComprobanteElectronico();
                Integer codigoDocumentoSus = Integer.parseInt(codigoDocModificado);
                Calendar fechaTmp = Calendar.getInstance();
                fechaTmp.setTime(fechaEmisionDocSustento);
                Integer _dia = fechaTmp.get(Calendar.DAY_OF_MONTH);
                Integer _mes = fechaTmp.get(Calendar.MONTH) + 1;
                Integer _anio = fechaTmp.get(Calendar.YEAR);
                ec.discheca.modelo.ComprobanteElectronico comprobanteDeSustento = dAOComprobante.obtenerComprobatePorCodigoEstablecimientoPuntoEmisionSecuencialRUCFechaEmision(
                        numero[0], numero[1], numero[2], ms.obtenerRUCEmpresaSesionActiva(), codigoDocumentoSus, _dia, _mes, _anio);
                if (comprobanteDeSustento != null) {
//                    DAODocumento doc = new DAODocumento();
//                    Documento documentoAsociado = doc.obtenerUno(comprobanteDeSustento.getIdComprobante());
                    //AQUI DEBERIA SER DIRECTORIO TEMPORAL
                    ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico comprobante_sri = TransformadorArchivos.byteCompr(comprobanteDeSustento.getComprobanteFirmadoComprobanteElectronico(), Valores.VALOR_DIRECTORIO_CREACION_XMLS + RandomStringUtils.randomNumeric(9));
                    if (comprobante_sri != null) {
                        ec.discheca.comprobanteelectronico.esquema.implementacion.ImplementacionFactura facturaAsociadaANotaCredito = comprobante_sri.ConstruirFactura();
                        if (facturaAsociadaANotaCredito != null) {
                            existeDocumentoSustento = true;

                            listaDetalles.clear();
                            for (Detalle det : facturaAsociadaANotaCredito.getDetalles()) {
                                BigDecimal cantidad = new BigDecimal(det.getCantidad());
                                BigDecimal desceunto = new BigDecimal(det.getDescuento());
                                //validacion para decimales en 2 rd
                                det.setCantidad(cantidad.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                                det.setDescuento(desceunto.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                                Random r = new Random();
                                ObjetoListaDetallesPresentacion odp = new ObjetoListaDetallesPresentacion();
                                odp.setIdentificador(r.nextInt());
                                odp.setDetalleComprobante(det);
                                listaDetalles.add(odp);
                                calcularTotales();

                            }
                            setTipoIdentificacion(Integer.parseInt(facturaAsociadaANotaCredito.getInformacionFactura().getTipoIdentificacionComprador()));
                            setIdentificacion(getTipoIdentificacion());
                            setRazonSocialComprador(facturaAsociadaANotaCredito.getInformacionFactura().getRazonSocialComprador());
                            setIdentificacionComprador(facturaAsociadaANotaCredito.getInformacionFactura().getIdentificacionComprador());
                        }
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha encontrado un comprobante asociado a este número." + numDocModificado + " , fecha de emisión y tipo de documento ingresado. ");

                }
            } catch (Exception ex) {
                Logger.getLogger(BeanNotaCredito.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        }

        return existeDocumentoSustento;
    }

    private DAOClienteEmpresa instanciarDAOClienteEmpresa() {
        DAOClienteEmpresa dao_cliente_empresa = null;
        try {
            dao_cliente_empresa = new DAOClienteEmpresa();
        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_cliente_empresa;
    }

    /**
     * Método que trae todos los registros en receptor del RUC o cédula para
     * luego llenar el metodo Autocomplete
     */
    private void cargarReceptores() {
        try {
            DAOReceptor dr = new DAOReceptor();
            receptores = dr.obtenerReceptor();
        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que busca un cliente/empresa receptor segun lo ingresado en el
     * autocomplete
     */
    public List<String> autoCompletarRUCCedulas(String query) {
        List<String> clientes_filtrados = new ArrayList<String>();
        for (int i = 0; i < this.receptores.size(); i++) {
            Receptor cliente_tmp = this.receptores.get(i);
            if (cliente_tmp.getRucReceptor().toLowerCase().startsWith(query)) {
                clientes_filtrados.add(cliente_tmp.getRucReceptor());
            }
        }
        return clientes_filtrados;
    }

    public List<String> autoCompleteProductosDescripcion(String query) {
        List<String> clientes_filtrados = new ArrayList<String>();
        for (int i = 0; i < this.productosTotales.size(); i++) {
            Producto cliente_tmp = this.productosTotales.get(i);
            if (cliente_tmp.getDescripcion().toLowerCase().startsWith(query)) {
                clientes_filtrados.add(cliente_tmp.getDescripcion());
            }
        }
        return clientes_filtrados;
    }

    public void busquedaDeProductos() {
        try {
            DAOPermiso1 permiso = new DAOPermiso1();
            if (codigoPrincipal != null && !codigoPrincipal.equals("") || descripcion != null && !descripcion.equals("")) {
                productos = permiso.buscarProductosInternosVarios("1", codigoPrincipal, descripcion);
                if (productos.size() > 0) {
                    cantidad = new BigDecimal(BigInteger.ONE);
                    producto = productos.get(0);
                    DatosDetalles = true;
                } else {
                    producto = new Producto();
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Producto no Encotrado.");
                }
            } else {
                producto = new Producto();
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ingrese un Codigo.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void busquedaDeProductos2() {

        try {
            DAOPermiso1 permiso = new DAOPermiso1();
            if (codigoPrincipal != null && !codigoPrincipal.equals("") || descripcion != null && !descripcion.equals("")) {
                productosbusqueda = permiso.buscarProductosInternoslike("1", codigoPrincipal, descripcion);
                if (productosbusqueda.size() > 0) {
                    RequestContext.getCurrentInstance().execute("PF('dialogDetalleAdicionalWV3').show();");

                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se encontraron productos.");
                }
            } else {

                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ingrese un Codigo.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que busca un cliente/empresa receptor de la notacredito por medio
     * del RUC o cédula
     */
    public void buscarClienteEmpresaReceptor() {
        ControlSesion cs = new ControlSesion();
        if (identificacionComprador != null && !identificacionComprador.equals("")) {
            try {
                Receptor r;
                DAOReceptor dr = new DAOReceptor();
                r = dr.obtenerReceptorPorIdentificacion(identificacionComprador);
                if (r != null) {
                    razonSocialComprador = r.getRazonSocialReceptor();
                } else {
                    razonSocialComprador = "";
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
//            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un RUC/Cédula.");
        }
    }

    /**
     * Método que elimina un detalle adicional del detalle de la notacredito
     */
    public void eliminarDetalleAdicional() {
        if (detallesAdicionales != null && !detallesAdicionales.isEmpty()) {
            detallesAdicionales.remove(informacionAdicionalDetalleSeleccionado);
        }
    }

    /**
     * Método que borra los campos para agregar un nuevo detalle
     */
    public void borrarCamposNuevoDetalle() {
        cantidad = null;
        descuento = null;
        impuestoIVASeleccionado = null;
        impuestoICESeleccionado = null;
    }

    /**
     * Método que verifica si existen ingresados como máximo 3 detalles
     * adicionales en el detalle y muestra el diálogo
     */
    public void verificarIngresoDetallesAdicionales() {
        if (detallesAdicionales != null && detallesAdicionales.size() < 3) {
            nombreInfoAdicionalDetalle = null;
            valorInfoAdicionalDetalle = null;
            RequestContext.getCurrentInstance().execute("PF('wv-detalle-adicional').show();");
        }

    }

    /**
     * Método que agrega un detalle adicional al detalle de la notacredito y
     * valida que el nombre y el valor no estén nulos y vacíos
     */
    public void agregarDetallesAdicionales() {
        if (nombreInfoAdicionalDetalle != null && !nombreInfoAdicionalDetalle.equals("") && valorInfoAdicionalDetalle != null && !valorInfoAdicionalDetalle.equals("")) {
            InformacionAdicional ia = new InformacionAdicional();
            ia.setNombre(nombreInfoAdicionalDetalle);
            ia.setValor(valorInfoAdicionalDetalle);
            detallesAdicionales.add(ia);

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El nombre y valor son obligatorios.");
        }
        RequestContext.getCurrentInstance().execute("PF('wv-detalle-adicional').hide();");
    }

    /**
     * Método que valida la cantidad y el impuesto seleccionado IVA o ICE, y
     * agrega un nuevo detalle a la lista de detalles
     */
    public void agregarDetalleNotaCredito() {
        ObjetoListaDetallesPresentacion odp = new ObjetoListaDetallesPresentacion();
        codigoPrincipal = producto.getCodigoProducto();
        descripcion = producto.getDescripcion();
        if (cantidad != null && !cantidad.equals(BigDecimal.ZERO)) {
            if (!valorSelecionado.equals("")) {
                this.precioUnitario = valorSelecionado;
                if (Validaciones.validarNumero(cantidad.toString())) {
                    /**
                     * Se verifica que la cantidad tenga máximo 6 decimales
                     */
                    if (cantidad.scale() <= 6) {
                        if (idImpuestoIVASeleccionado == -1) {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar al menos un impuesto.");
                        } else {

                            BigDecimal precio_total_sin_impuesto = BigDecimal.ZERO;
                            Detalle detalle = new Detalle();
                            detalle.setCodigoPrincipal(codigoPrincipal);
                            detalle.setDescripcion(descripcion);
                            cantidad = cantidad.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                            detalle.setCantidad(cantidad.toString());
                            //precio unitario selecionado
                            detalle.setPrecioUnitario(valorSelecionado);
                            /**
                             * Se verifica que hay ingresado un descuento, si
                             * hay descuento se le resta del resultado de la
                             * cantidad X precioUnitario
                             */
                            if (descuento != null && !descuento.equals(BigDecimal.ZERO)) {
                                if (opcionesDescuento.equals(2)) {
                                    descuento = descuento.multiply(cantidad.multiply(new BigDecimal(this.precioUnitario)));
                                    descuento = descuento.divide(new BigDecimal("100.00"));
                                }
                                descuento = descuento.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                                precio_total_sin_impuesto = cantidad.multiply(new BigDecimal(this.precioUnitario)).subtract(descuento);
                                detalle.setDescuento(descuento.toString());
                            } else {

                                descuento = descuento.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                                detalle.setDescuento(descuento.toString());
                                precio_total_sin_impuesto = cantidad.multiply(new BigDecimal(this.precioUnitario));
                            }
                            InformacionAdicional unidad = new InformacionAdicional();
                            unidad.setNombre("U.Medida");
                            unidad.setValor(producto.getUnidadMedida());
                            detallesAdicionales.add(unidad);
                            if (!detallesAdicionales.isEmpty()) {
                                detalle.setDetallesAdicionales(detallesAdicionales);
                            }
                            /**
                             * Se redonda el precio_total_sin_impuesto para que
                             * queden a dos decimales
                             */
                            precio_total_sin_impuesto = precio_total_sin_impuesto.setScale(2, BigDecimal.ROUND_HALF_EVEN);
                            detalle.setPrecioTotalSinImpuesto(precio_total_sin_impuesto.toString());
                            try {
                                Calculos c = new Calculos();
                                detalle.setImpuestos(c.obtenerImpuestosComprobante(idImpuestoIVASeleccionado.toString(), "-1", cantidad, new BigDecimal(precioUnitario), descuento));
//                                detalle.setImpuestos(c.obtenerImpuestosComprobante(idImpuestoIVASeleccionado.toString(), idImpuestoICESeleccionado.toString(), cantidad, new BigDecimal(precioUnitario), descuento));
//                            detalles.add(detalle);

                                Random r = new Random();
                                odp.setIdentificador(r.nextInt());
                                odp.setDetalleComprobante(detalle);
                                listaDetalles.add(odp);

                                calcularTotales();
                                encerarDetalles();

                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Detalle agregado.");
                                RequestContext.getCurrentInstance().update("tablaDetalles");
                                panelIngresarNuevoDetalleCerrar();
                            } catch (Exception ex) {
                                Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La cantidad debe ser un número con máximo 6 decimales.");
                    }

                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La cantidad debe ser un número.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Selecione un precio para el producto");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor ingrese la cantidad.");
        }

    }

    public void calcularTotales() {
        Double subtotal12 = 0.0;
        Double subtotal0 = 0.0;
        Double subtotalNoIva = 0.0;
        Double subtotal = 0.0;
        Double totalDescuentoComprobante = 0.0;
        Double valorICE = 0.0;
        Double iva12 = 0.0;
        BigDecimal valorTotal = new BigDecimal(0.0);
        if (this.listaDetalles != null || this.listaDetalles.size() > 0) {
            for (ObjetoListaDetallesPresentacion odt : this.listaDetalles) {
                Detalle dt = odt.getDetalleComprobante();
                List<ImpuestoComprobanteElectronico> listaImpuestos = dt.getImpuestos();
                for (int j = 0; j < listaImpuestos.size(); j++) {
                    // cambio de 12 a 14
                    if ((new BigDecimal(listaImpuestos.get(j).getTarifa())).compareTo(new BigDecimal(14.00)) == 0) {
                        subtotal12 = subtotal12 + (new Double(listaImpuestos.get(j).getBaseImponible().toString()));
                        subtotal12 = redondear(subtotal12);
                    }
                    if ((new BigDecimal(listaImpuestos.get(j).getTarifa())).compareTo(new BigDecimal(0.00)) == 0 && !listaImpuestos.get(j).getCodigoPorcentaje().equals("6")) {
                        subtotal0 = subtotal0 + (new Double(listaImpuestos.get(j).getBaseImponible().toString()));
                        subtotal0 = redondear(subtotal0);
                    }
                    if (listaImpuestos.get(j).getCodigoPorcentaje().equals("6")) {
                        subtotalNoIva = subtotalNoIva + (new Double(listaImpuestos.get(j).getBaseImponible().toString()));
                        subtotalNoIva = redondear(subtotalNoIva);
                    }
                    if (is_codigo_ICE(listaImpuestos.get(j).getCodigoPorcentaje())) {
                        valorICE = valorICE + (new Double(listaImpuestos.get(j).getBaseImponible().toString()));
                        valorICE = redondear(valorICE);
                    }
                }
                if (dt.getDescuento() != null) {
                    BigDecimal desc = new BigDecimal(dt.getDescuento());
                    totalDescuentoComprobante = totalDescuentoComprobante + (new Double(desc.toString()));
                }
            }
            if (subtotal12 != 0) {
                subtotal = subtotal + subtotal12;
                subtotal = redondear(subtotal);
                //cambio a 14
                iva12 = subtotal12 * 0.14;
                iva12 = redondear(iva12);
            }
            if (subtotal0 != 0) {
                subtotal = subtotal + subtotal0;
                subtotal = redondear(subtotal);
            }

            if (subtotalNoIva != 0) {
                subtotal = subtotal + subtotalNoIva;
                subtotal = redondear(subtotal);
            }

            if (subtotal != 0 || iva12 != 0 || valorICE != 0) {
                Double suma = subtotal + iva12 + valorICE;
                valorTotal = BigDecimal.valueOf(suma);
                valorTotal = BigDecimal.valueOf(redondear(valorTotal.doubleValue()));
            }

            if (valorTotal.compareTo(BigDecimal.ZERO) != 0 && propina != null) {
                valorTotal = valorTotal.add(propina);
                valorTotal = BigDecimal.valueOf(redondear(Double.valueOf(valorTotal.toString())));
            }

        }
        valorTotal = BigDecimal.valueOf(redondear(Double.valueOf(valorTotal.toString())));
        setValorTotalC(valorTotal);
        setSubtotalC(BigDecimal.valueOf(subtotal));
        setSubtotal0C(BigDecimal.valueOf(subtotal0));
        setSubtotal12C(BigDecimal.valueOf(subtotal12));
        setSubtotalNoIvaC(BigDecimal.valueOf(subtotalNoIva));
        setTotalDescuentoComprobanteC(BigDecimal.valueOf(totalDescuentoComprobante));
        setValorICEC(BigDecimal.valueOf(valorICE));
        iva12 = redondear(iva12);
        setIva12C(BigDecimal.valueOf(iva12));

        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:itSubTotal12");
        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:itSubTotal0");
        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:itSubTotalNoIva");
        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:itSubTotal");
        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:itTotalDescuento");
        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:itValorIce");
        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:iva12C");
        RequestContext.getCurrentInstance().update("formGenerarNotaCredito:valorTotalC");
    }

    public boolean is_codigo_ICE(String codigo) {
        List<String> codigos_ICE = new ArrayList<String>();
        codigos_ICE.add("3023");
        codigos_ICE.add("3051");
        codigos_ICE.add("3610");
        codigos_ICE.add("3620");
        codigos_ICE.add("3630");
        codigos_ICE.add("3640");
        codigos_ICE.add("3073");
        codigos_ICE.add("3072");
        codigos_ICE.add("3074");
        codigos_ICE.add("3075");
        codigos_ICE.add("3077");
        codigos_ICE.add("3078");
        codigos_ICE.add("3079");
        codigos_ICE.add("3080");
        codigos_ICE.add("3171");
        codigos_ICE.add("3172");
        codigos_ICE.add("3173");
        codigos_ICE.add("3174");
        codigos_ICE.add("3175");
        codigos_ICE.add("3176");
        codigos_ICE.add("3081");
        codigos_ICE.add("3092");
        codigos_ICE.add("3650");
        codigos_ICE.add("3660");
        codigos_ICE.add("3011");
        codigos_ICE.add("3021");
        codigos_ICE.add("3031");
        codigos_ICE.add("3041");

        for (int i = 0; i < codigos_ICE.size(); i++) {
            if (codigos_ICE.get(i).equals(codigo)) {
                return true;
            }
        }
        return false;

    }

    private List<InformacionAdicional> cargarInformacionAdicional() throws Exception {
        informacion_adicional = new ArrayList<InformacionAdicional>();
        if (listaInfoAdicional != null && listaInfoAdicional.size() > 0) {
            for (InformacionAdicional ia : listaInfoAdicional) {
                informacion_adicional.add(ia);
            }

        }
        return informacion_adicional;
    }

    public void panelIngresarNuevoDetalleAbrir() {
        encerarDetalles();
        panelDetalle = true;
    }

    public void panelIngresarNuevoDetalleCerrar() {
        encerarDetalles();
        panelDetalle = false;
        DatosDetalles = false;
    }

    public void encerarDetalles() {
        codigoPrincipal = "";
        codigoAuxiliar = "";
        descripcion = "";
        detallesAdicionales = new ArrayList<InformacionAdicional>();
        tieneDescuento = false;
        cantidad = new BigDecimal(BigInteger.ZERO);
        precioUnitarioDetalle = new BigDecimal(BigInteger.ZERO);
        precioUnitario = "";
        descuentoDetalle = new BigDecimal(BigInteger.ZERO);
        descuento = new BigDecimal(BigInteger.ZERO);
        listaTarifaImpuestoICE = new ArrayList<TarifasImpuesto>();
        listaTarifaImpuestoIVA = new ArrayList<TarifasImpuesto>();
        listarTipoImpuesto();
    }

    public void ingresarDatosUsuario() {
        if (identificacion == 07) {
            datosCliente = false;
        } else if (identificacion == 1) {
            datosCliente = true;
        }
    }

    public boolean generarNotaCreditoElectronica() {
        boolean notacredito_generada = false;
        try {

            if (clienteEmpresaReceptor != null && clienteEmpresaEmisor != null) {

                notacredito_electronica = new ImplementacionNotaCredito();

                InformacionTributariaComprobanteElectronico informacion_tributaria = this.cargarInformacionTributaria();

                InformacionNotaCredito informacion_notacredito = this.cargarInformacionNotaCredito();

                List<InformacionAdicional> informacion_adicional = this.cargarInformacionAdicional();

                ClaveAcceso clave_acceso = new ClaveAcceso();

                notacredito_electronica.setInformacionTributariaComprobanteElectronico(informacion_tributaria);
                notacredito_electronica.setInformacionNotaCredito(informacion_notacredito);
                notacredito_electronica.setDetalles(obtenerDetalles());
                notacredito_electronica.setInformacionAdicional(informacion_adicional);

                informacion_tributaria.setClaveAcceso(clave_acceso.obtenerClaveDeAcceso(notacredito_electronica, RandomStringUtils.randomNumeric(8)));

                notacredito_electronica.setInformacionTributariaComprobanteElectronico(informacion_tributaria);
                notacredito_generada = true;

            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe buscar el receptor del comprobante.");
            }
        } catch (Exception e) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, e);
        }
        return notacredito_generada;
    }

    public void firmarYAutorizar() {
        guardarLogRegistros("Firmar y Autorizar Nota De Credito");
        cambiarSecuencialActivo();
        if (clienteEmpresaReceptor != null && clienteEmpresaEmisor != null) {
            boolean notacredito_generada = false;

            notacredito_generada = this.generarNotaCreditoElectronica();

            if (notacredito_generada) {
                GeneradorComprobanteElectronicoXML notacredito_electronica_xml = new GeneradorComprobanteElectronicoXML();

                if (Valores.VALOR_DIRECTORIO_CREACION_XMLS != null) {
                    String fecha_hoy = Utilidades.obtenerFechaEnFormatoAnioMesDia(new Date());
                    String directorio_notacredito_hoy = Valores.VALOR_DIRECTORIO_CREACION_XMLS + fecha_hoy.replace("/", File.separator);
                    boolean creado_directorio_hoy = false;
                    if (!new File(directorio_notacredito_hoy).exists()) {
                        try {
                            FileUtils.forceMkdir(new File(directorio_notacredito_hoy));
                            creado_directorio_hoy = true;
                        } catch (IOException ex) {
                            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        creado_directorio_hoy = true;
                    }
                    if (creado_directorio_hoy) {
                        String directorio_notacredito_hoy_hora_min_segs = directorio_notacredito_hoy + File.separator + Utilidades.obtenerHHmmss(new Date());

                        try {
                            FileUtils.forceMkdir(new File(directorio_notacredito_hoy_hora_min_segs));
                            File comprobante_electronico = notacredito_electronica_xml.genArchivoObjComprobante(notacredito_electronica, directorio_notacredito_hoy_hora_min_segs + File.separator + notacredito_electronica.getInformacionTributariaComprobanteElectronico().getClaveAcceso(), "1.1.0");
                            if (comprobante_electronico != null) {
                                RespuestaSRI respuesta = null;
                                FirmaAutorizacion fa = new FirmaAutorizacion();
                                guardarLogRegistros("Enviar nota de credito al Sri clave de acceso: " + notacredito_electronica.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + " secuencial: " + notacredito_electronica.getInformacionTributariaComprobanteElectronico().getSecuencial());
                                respuesta = fa.firmarYAutorizar(comprobante_electronico, directorio_notacredito_hoy_hora_min_segs, notacredito_electronica.getInformacionTributariaComprobanteElectronico().getClaveAcceso().concat(".xml"), notacredito_electronica, Valores.FACTURA);
                                if (respuesta != null) {
                                    AlmacenamientoComprobanteElectronico ace = new AlmacenamientoComprobanteElectronico();
                                    Long secuencial_actualizado = Long.parseLong(secuencial.getSecuencialNotaCreditoSecuencial()) + 1;
                                    if (respuesta.getEstado().equals("1") || respuesta.getEstado().equals("2")) {
                                        this.instanciarDAOSecuencial().actualizarSecuencialPorTipoComprobante(secuencial.getIdSecuencial(), String.valueOf(secuencial_actualizado), Valores.NOTA_CREDITO);

                                        boolean guardado = ace.guardarComprobanteElectronico(respuesta, notacredito_electronica, Valores.FACTURA, directorio_notacredito_hoy_hora_min_segs + File.separator, "");
                                        if (guardado) {
                                            if (respuesta.getEstado().equals("1")) {
                                                mensajeResultado = "COMPROBANTE AUTORIZADO GUARDADO EN EL REPOSITORIO.";
                                                RequestContext.getCurrentInstance().execute("PF('autorizadoNoautorizado').show();");
//                                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE AUTORIZADO GUARDADO EN EL REPOSITORIO.");
                                                /**
                                                 * Envío de correo
                                                 */
                                                Notificacion n = new Notificacion();
                                                boolean notificado = n.enviarNotificacion(respuesta, directorio_notacredito_hoy_hora_min_segs + File.separator + "R_" + respuesta.getClaveAccesoConsultada() + "-.xml", "ricardo.telcomp@hotmail.com");
                                            } else if (respuesta.getEstado().equals("2")) {
                                                mensajeResultado = "COMPROBANTE NO AUTORIZADO GUARDADO EN EL REPOSITORIO. ERROR:" + respuesta.getMensaje();
//                                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE NO AUTORIZADO GUARDADO EN EL REPOSITORIO. ERROR:" + respuesta.getMensaje());
                                                RequestContext.getCurrentInstance().execute("PF('autorizadoNoautorizado').show();");
                                            }
                                        }
                                    } else if (respuesta.getEstado().equals("6")) {
                                        mensajeResultado = "COMPROBANTE NO ENVIADO AL SRI REVISE SU CONEXION A INTERNET. por favor enviar nuevamente.";
//                                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE NO ENVIADO AL SRI. El sistema lo enviará posteriormente y se le notificará.");
                                        RequestContext.getCurrentInstance().execute("PF('noenviado').show();");
                                    } else if (respuesta.getEstado().equals("5")) {
                                        mensajeResultado = "COMPROBANTE SIN RESPUESTA DEL SRI. ERROR SRI. por favor enviar nuevamente.";
//                                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE SIN RESPUESTA DEL 
//                                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE SIN RESPUESTA DEL SRI. ERROR. El sistema lo enviará posteriormente y se le notificará.");
                                        RequestContext.getCurrentInstance().execute("PF('noenviado').show();");
                                    } else if (respuesta.getEstado().equals("10")) {
                                        mensajeResultado = "COMPROBANTE CON CLAVE DE ACCESO EN PROCESAMIENTO. El sistema lo enviará posteriormente y se le notificará.";
//                                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE CON CLAVE DE ACCESO EN PROCESAMIENTO. El sistema lo enviará posteriormente y se le notificará.");
                                        RequestContext.getCurrentInstance().execute("PF('autorizadoNoautorizado').show();");
                                    } else if (respuesta.getEstado().equals("11")) {
                                        mensajeResultado = "COMPROBANTE DEVUELTO. " + respuesta.getMensaje();
                                        //  MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE DEVUELTO.");
                                        RequestContext.getCurrentInstance().execute("PF('noenviado').show();");
                                    }
                                    if (respuesta.getEstado().equals("6") || respuesta.getEstado().equals("5") || respuesta.getEstado().equals("10")) {
                                        this.instanciarDAOSecuencial().actualizarSecuencialPorTipoComprobante(secuencial.getIdSecuencial(), String.valueOf(secuencial_actualizado), Valores.NOTA_CREDITO);
                                        ace.guardarComprobanteElectronicoPendiente(respuesta, notacredito_electronica, respuesta.getEstado(), respuesta.getMensaje(), directorio_notacredito_hoy_hora_min_segs + File.separator);
                                    }
                                    /**
                                     * Se muestra el dialog con el comprobante
                                     * de pago para imprimir
                                     */
                                }
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }
        }

    }

    public void direccionamiento() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/nota-credito.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarDatosEmisor() {
        try {
            ControlSesion ms = new ControlSesion();
            Empresa emp = this.instanciarDAOClienteEmpresa().obtenerClienteEmpresaPorId(ms.obtenerRUCEmpresaSesionActiva());
//            ClienteEmpresa emp = this.instanciarDAOClienteEmpresa().obtenerClienteEmpresaPorId("1720579273");

            if (emp != null) {
                this.emisor.setIdEmpresa(emp.getIdEmpresa());
                this.clienteEmpresaEmisor = emp;
                if (emp.getNombreComercialClienteEmpresa() != null) {
                    this.emisor.setNombreComercialClienteEmpresa(emp.getNombreComercialClienteEmpresa());
                }
                if (emp.getRazonSocialClienteEmpresa() != null) {
                    this.emisor.setRazonSocialClienteEmpresa(emp.getRazonSocialClienteEmpresa());
                }
                if (emp.getDireccionClienteEmpresa() != null) {
                    this.emisor.setDireccionClienteEmpresa(emp.getDireccionClienteEmpresa());
                }

                this.setTipoAmbienteEmisor(Valores.AMBIENTE);

                if (emp.getNumeroResolucionClienteEmpresa() != null) {
                    this.emisor.setNumeroResolucionClienteEmpresa(emp.getNumeroResolucionClienteEmpresa());
                }

                if (emp.getObligadoContabilidadClienteEmpresa() != null) {
                    if (emp.getObligadoContabilidadClienteEmpresa().equals(Boolean.TRUE)) {
                        this.setContabilidadEmisor("SI");
                    } else {
                        this.setContabilidadEmisor("NO");
                    }
                }
                if (emp.getTelefonoPrincipalClienteEmpresa() != null) {
                    this.emisor.setTelefonoPrincipalClienteEmpresa(emp.getTelefonoPrincipalClienteEmpresa());
                }
                DAOSecuencial ds = new DAOSecuencial();
                Secuencial s = new Secuencial();
                secuenciales = ds.obtenerSecuencialesPorRucYAmbiente(emp.getIdEmpresa(), Valores.AMBIENTE);
                s = ds.obtenerSecuencialActivoPorRucYAmbiente(emp.getIdEmpresa(), Valores.AMBIENTE);
                if (s != null) {
                    this.secuencial = s;
                    this.codigoSecuencialSeleccionado = this.secuencial.getIdSecuencial();
                } else {
                    //Mensaje Momentaneo//
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No tiene registrado un secuencial, por favor registrelo en cambiar datos.");
                }
                ordenarSecuencialesPorActivo();

            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No existe información de la empresa emisora.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ordenarSecuencialesPorActivo() {
        if (this.secuencial != null) {
            for (int i = 0; i < secuenciales.size(); i++) {
                if (secuenciales.get(i).equals(secuencial)) {
                    if (i == 0) {
                        break;
                    } else {
                        Secuencial aux = secuenciales.get(0);
                        secuenciales.set(0, secuencial);
                        secuenciales.set(i, aux);
                    }
                }
            }
        }
    }

    public boolean desactivarSecuencialActivo() {
        boolean respuesta = true;
        try {
            if (secuenciales != null) {
                for (Secuencial sec : secuenciales) {
                    if (sec.getEstadoSecuencial() == true) {
                        DAOSecuencial ds = new DAOSecuencial();
                        respuesta = ds.actualizarEstadoSecuencial(sec.getIdSecuencial(), false);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public void cambiarSecuencialActivo() {
        if (!secuenciales.get(0).getIdSecuencial().equals(codigoSecuencialSeleccionado)) {
            if (desactivarSecuencialActivo()) {
                DAOSecuencial ds;
                try {
                    ds = new DAOSecuencial();
                    boolean actualizado = ds.actualizarEstadoSecuencial(codigoSecuencialSeleccionado, true);
                    if (actualizado) {
                        this.secuencial = ds.obtenerSecuencialPorId(codigoSecuencialSeleccionado);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void listarTipoImpuesto() {
        try {
            DAOTarifasImpuesto daotarifas = new DAOTarifasImpuesto();
            List<TarifasImpuesto> listTI = daotarifas.obtenerImpuestosRenta();
            listaTarifaImpuestoICE = daotarifas.obtenerImpuestoICE();
            listaTarifaImpuestoIVA = new ArrayList<TarifasImpuesto>();
            listaTarifaImpuestoIVA = daotarifas.obtenerImpuestoIVA();
//            for (TarifasImpuesto tid : listTI) {
//                BigDecimal porcentajeAux = new BigDecimal(tid.getPorcentajeTarifaImpuesto());
//                if (tid.getCodigoTarifaImpuesto().equals("0") || tid.getCodigoTarifaImpuesto().equals("6") || (tid.getCodigoTarifaImpuesto().equals("2") && porcentajeAux.compareTo(new BigDecimal(12.00)) == 0)) {
//                    listaTarifaImpuestoIVA.add(tid);
//                }
//            }
        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean validarConsumidorFinal() {
        boolean success = false;
        if (identificacion == 7 && valorTotalC.compareTo(new BigDecimal(200)) == 1) {
        } else {
            success = true;
        }
        return success;
    }

    public boolean validarPropina() {
        boolean valor = false;
        if (propina != null && propina.compareTo(BigDecimal.ZERO) != 0) {
            if (subtotalC != null) {
                BigDecimal aux = subtotalC.multiply(new BigDecimal(0.10));
                if (propina.compareTo(aux) == 1) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El valor de propina es mayor del permitido.");
                } else {
                    valor = true;
                }

            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No existe un valor de subtotal.");
            }
        } else {
            valor = true;
        }
        return valor;
    }

    private InformacionTributariaComprobanteElectronico cargarInformacionTributaria() {
        /**
         * Información Tributaria para generar NotaCredito Electrónica
         */
        ControlSesion cs = new ControlSesion();
        InformacionTributariaComprobanteElectronico informacion_tributaria = null;
        try {
            informacion_tributaria = new InformacionTributariaComprobanteElectronico();

            informacion_tributaria.setAmbiente(Valores.AMBIENTE);
            informacion_tributaria.setCodDoc(Valores.NOTA_CREDITO);
            informacion_tributaria.setDirMatriz(clienteEmpresaEmisor.getDireccionClienteEmpresa());
            informacion_tributaria.setRazonSocial(clienteEmpresaEmisor.getRazonSocialClienteEmpresa());
            /**
             * Obtiene el último secuencial guardado en la base de datos;
             */
            informacion_tributaria.setCodigoEstablecimiento(secuencial.getCodigoEstablecimientoSecuencial());
            informacion_tributaria.setPuntoEmision(secuencial.getPuntoEmisionSecuencial());
            informacion_tributaria.setSecuencial(Validaciones.completarSecuencial(secuencial.getSecuencialNotaCreditoSecuencial()));
            informacion_tributaria.setNombreComercial(clienteEmpresaEmisor.getNombreComercialClienteEmpresa());
            informacion_tributaria.setRuc(clienteEmpresaEmisor.getIdEmpresa());
            informacion_tributaria.setTipoEmision(Valores.VALOR_TIPO_EMISION);
        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
        return informacion_tributaria;
    }

    private DAOSecuencial instanciarDAOSecuencial() {
        DAOSecuencial dao_secuencial = null;
        try {
            dao_secuencial = new DAOSecuencial();
        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_secuencial;
    }

    /**
     * Método que carga la información del receptor de la notacredito y totales
     *
     * @return InformacionNotaCredito
     * @throws Exception
     */
    private InformacionNotaCredito cargarInformacionNotaCredito() throws Exception {
        Calculos calculos = new Calculos();
        InformacionNotaCredito informacion_notacredito = new InformacionNotaCredito();
        informacion_notacredito.setFechaEmision(Utilidades.obtenerFechaFormatoddMMyyyy(fechaEmision));
        informacion_notacredito.setDirEstablecimiento(clienteEmpresaEmisor.getDireccionClienteEmpresa());
        if (clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa() != null && !clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa().equals("")) {
            informacion_notacredito.setContribuyenteEspecial(clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa());
        }
        informacion_notacredito.setObligadoContabilidad(Validaciones.obtenerObligadoContabilidad(clienteEmpresaEmisor.getObligadoContabilidadClienteEmpresa().toString()));
        informacion_notacredito.setTipoIdentificacionComprador(Validaciones.comprTipoId(clienteEmpresaReceptor.getRucReceptor()));
        informacion_notacredito.setRazonSocialComprador(clienteEmpresaReceptor.getRazonSocialReceptor());
        informacion_notacredito.setIdentificacionComprador(clienteEmpresaReceptor.getRucReceptor());
        informacion_notacredito.setRise(rise);
        informacion_notacredito.setCodDocModificado(codigoDocModificado);
        informacion_notacredito.setNumDocModificado(numDocModificado);
        informacion_notacredito.setFechaEmisionDocSustento(Utilidades.obtenerFechaFormatoddMMyyyy(fechaEmisionDocSustento));
        informacion_notacredito.setTotalSinImpuestos(calculos.obtenerTotalSinImpuestos(obtenerDetalles()).toString());
        List<ImpuestoComprobanteElectronico> total_con_impuestos = calculos.obtenerTotalConImpuestos(obtenerDetalles());
        informacion_notacredito.setValorModificacion(calculos.obtenerImporteTotal2(total_con_impuestos).toString());
        informacion_notacredito.setTotalConImpuesto(total_con_impuestos);
        informacion_notacredito.setMotivo(motivo);
        informacion_notacredito.setMoneda(Valores.MONEDA);
        return informacion_notacredito;
    }

    public void obtenerImpuestos() {
        if (detalleSeleccionado != null) {
            listaImpuestoDetalle = new ArrayList<ImpuestoComprobanteElectronico>(detalleSeleccionado.getDetalleComprobante().getImpuestos());
        }
    }

    public void obtenerDetallesAdicionales() {
        if (detalleSeleccionado != null) {
            listaDetallesAdicionales = new ArrayList<InformacionAdicional>(detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales());
            dSeleccionado = detalleSeleccionado;
        }
    }

    public List<Detalle> obtenerDetalles() {
        if (this.listaDetalles != null && this.listaDetalles.size() > 0) {
            List<Detalle> lista = new ArrayList<Detalle>();
            for (ObjetoListaDetallesPresentacion odt : this.listaDetalles) {
                lista.add(odt.getDetalleComprobante());
            }
            return lista;
        }
        return null;
    }

    public void eliminarImpuesto() {
        if (listaDetalles.size() > 0 && detalleSeleccionado != null) {
            listaDetalles.remove(detalleSeleccionado);
            calcularTotales();
        }
    }

    public boolean validarCantidadDecimales(BigDecimal numero, Integer cantidadDecimales) {
        String numeroAux;
        String[] partesNumero;
        numeroAux = numero.toString();
        if (numeroAux.contains(".")) {
            partesNumero = numeroAux.split("\\.");
            return partesNumero[1].length() <= cantidadDecimales;
        } else {
            return true;
        }
    }

    public boolean validacionesInformacionNotaCredito() {
        boolean respuesta = true;
        tipoIdentificacion = 1;
        if (fechaEmision == null) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar una fecha de emisión.");
            respuesta = false;
        } else if (fechaEmisionDocSustento == null) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar una fecha de emisión del Documento de Sustento.");
            respuesta = false;
        } else if (fechaEmisionDocSustento.after(fechaEmision)) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La fecha de Emisión de la Nota de Crédito no puede ser antes de de fecha del Documento de Sustento.");
            respuesta = false;
        } else if (codigoDocModificado.equals("-1")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el tipo de Documento de Sustento.");
            respuesta = false;
        } else if (numDocModificado == null || numDocModificado.trim().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el número del Documento de Sustento.");
            respuesta = false;
        } else if (motivo.trim().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un motivo.");
            respuesta = false;
            //temporal

        }
        if (razonSocialComprador == null || razonSocialComprador.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo de la razón social del comprador no puede estar vacío.");
            respuesta = false;
        }
        if (respuesta) {
            this.clienteEmpresaReceptor = new Receptor();
            this.clienteEmpresaReceptor.setRucReceptor(identificacionComprador);
            this.clienteEmpresaReceptor.setRazonSocialReceptor(razonSocialComprador);
            this.siguiente(1);
        }

        return respuesta;
    }

    public void validacionesDetallesNotaCredito() {
        apagarfirmado = "false";
        if (listaDetalles.size() > 0 && validarPropina()) {
            if (validarConsumidorFinal()) {
                this.siguiente(2);
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El comprobante no puede tener un valor mayor a 200 cuando es consumidor final.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha ingresado el detalle de la notacredito.");
        }
    }

    public void generarRIDE() {
        try {
            cambiarSecuencialActivo();
            if (clienteEmpresaReceptor != null && clienteEmpresaEmisor != null) {
                boolean notacredito_generada = false;
                try {

                    notacredito_generada = this.generarNotaCreditoElectronica();

                    if (notacredito_generada) {
                        FacesContext faces = FacesContext.getCurrentInstance();
                        DAOComprobanteElectronico dce = new DAOComprobanteElectronico();
                        byte[] pdfByte = dce.generarPDFComprobante(this.notacredito_electronica);
                        HttpServletResponse response = visualizarPDF(faces, pdfByte, this.notacredito_electronica);
                        faces.responseComplete();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(BeanNotaCredito.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(BeanNotaCredito.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public HttpServletResponse visualizarPDF(FacesContext faces, byte[] archivoByte, ComprobanteElectronico comprobante) {
        HttpServletResponse response = (HttpServletResponse) faces.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + comprobante.getInformacionTributariaComprobanteElectronico().getRuc() + " " + comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + ".pdf");
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(archivoByte);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public boolean validacionesEmisor() {
        boolean aux = false;
        boolean aux1 = false;
        boolean aux2 = false;

        if (this.emisor.getNombreComercialClienteEmpresa() == null || this.emisor.getNombreComercialClienteEmpresa().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo de nombre comercial no puede estar vacío.");
        } else {
            aux = true;
        }

        if (this.emisor.getRazonSocialClienteEmpresa() == null || this.emisor.getRazonSocialClienteEmpresa().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo de la razón social no puede estar vacío.");
        } else {
            aux1 = true;
        }

        if (this.emisor.getDireccionClienteEmpresa() == null || this.emisor.getDireccionClienteEmpresa().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo de la dirección matríz no puede estar vacío.");
        } else {
            aux2 = true;
        }

        if (aux && aux1 && aux2) {
            this.siguiente(1);
            return true;
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Existe un error de validación.");
            return false;
        }
    }

    public void ingresarDetalleAdicional() {
        if (detallesAdicionales.size() < 3) {
            if (nombreInfoAdicionalDetalle != null && !nombreInfoAdicionalDetalle.equals("") && valorInfoAdicionalDetalle != null && !valorInfoAdicionalDetalle.equals("")) {
                InformacionAdicional da = new InformacionAdicional();
                da.setNombre(nombreInfoAdicionalDetalle);
                da.setValor(valorInfoAdicionalDetalle);
                detallesAdicionales.add(da);
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El detalle adicional se ha agregado.");
                RequestContext.getCurrentInstance().execute("PF('dialogDetalleAdicionalWV').hide()");
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Los campos no pueden estar vacíos.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pueden agregar más detalles adicionales por este detalle.");
        }
        nombreInfoAdicionalDetalle = "";
        valorInfoAdicionalDetalle = "";
    }

    public void ingresarInformacionAdicional() {
        if (infoAdicional) {
            nombreInfoAdicional = "CORREO";
            if (listaInfoAdicional.size() < 15) {
                if (nombreInfoAdicional != null && !nombreInfoAdicional.equals("") && valorInfoAdicional != null && !valorInfoAdicional.equals("")) {
                    boolean respuesta = false;

                    if (!Validaciones.isEmail(valorInfoAdicional)) {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo  válido.");
                    } else {
                        respuesta = true;
                    }
                    if (respuesta) {
                        InformacionAdicional infoAd = new InformacionAdicional();

                        infoAd.setNombre(nombreInfoAdicional);
                        infoAd.setValor(valorInfoAdicional);
                        listaInfoAdicional.add(infoAd);
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La información adicional no puede estar vacío.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No puede ingresar más información adicional para el comprobante.");
            }
            nombreInfoAdicional = "";
            valorInfoAdicional = "";
        }
    }

    public void eliminarInfoAdicional() {
        if (listaInfoAdicional.size() > 0 && infoAdicionalSeleccionada != null) {
            listaInfoAdicional.remove(infoAdicionalSeleccionada);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Editado", ((InformacionAdicional) event.getObject()).getNombre());
        RequestContext.getCurrentInstance().showMessageInDialog(msg);
    }

    public void onRowEdit2(RowEditEvent event) {
        idImpuestoICESeleccionado = 0;
        idImpuestoIVASeleccionado = 0;
        Calculos c = new Calculos();
        FacesMessage msg = new FacesMessage("Editado", ((ImpuestoComprobanteElectronico) event.getObject()).getCodigo());
        List<ImpuestoComprobanteElectronico> listamodif;
        try {

            String a = ((ImpuestoComprobanteElectronico) event.getObject()).getCodigo();

            if (a.startsWith("3")) {
                idImpuestoICESeleccionado = Integer.parseInt(a);
                listamodif = c.obtenerImpuestosComprobante(idImpuestoIVASeleccionado.toString(), idImpuestoICESeleccionado.toString(), new BigDecimal(detalleSeleccionado.getDetalleComprobante().getCantidad()), new BigDecimal(detalleSeleccionado.getDetalleComprobante().getPrecioUnitario()), new BigDecimal(detalleSeleccionado.getDetalleComprobante().getDescuento()));
                ((ImpuestoComprobanteElectronico) event.getObject()).setCodigo(listamodif.get(0).getCodigo());
                ((ImpuestoComprobanteElectronico) event.getObject()).setCodigoPorcentaje(listamodif.get(0).getCodigoPorcentaje());
                ((ImpuestoComprobanteElectronico) event.getObject()).setTarifa(listamodif.get(0).getTarifa());
                ((ImpuestoComprobanteElectronico) event.getObject()).setValor(listamodif.get(0).getValor());
                ((ImpuestoComprobanteElectronico) event.getObject()).setBaseImponible(listamodif.get(0).getBaseImponible());
            } else {

                idImpuestoIVASeleccionado = Integer.parseInt(a);
                listamodif = c.obtenerImpuestosComprobante(idImpuestoIVASeleccionado.toString(), idImpuestoICESeleccionado.toString(), new BigDecimal(detalleSeleccionado.getDetalleComprobante().getCantidad()), new BigDecimal(detalleSeleccionado.getDetalleComprobante().getPrecioUnitario()), new BigDecimal(detalleSeleccionado.getDetalleComprobante().getDescuento()));
                ((ImpuestoComprobanteElectronico) event.getObject()).setCodigo(listamodif.get(0).getCodigo());
                ((ImpuestoComprobanteElectronico) event.getObject()).setCodigoPorcentaje(listamodif.get(0).getCodigoPorcentaje());
                ((ImpuestoComprobanteElectronico) event.getObject()).setTarifa(listamodif.get(0).getTarifa());
                ((ImpuestoComprobanteElectronico) event.getObject()).setValor(listamodif.get(0).getValor());
                ((ImpuestoComprobanteElectronico) event.getObject()).setBaseImponible(listamodif.get(0).getBaseImponible());
            }

            for (int i = 0; i < detalleSeleccionado.getDetalleComprobante().getImpuestos().size(); i++) {
                for (int j = 0; j < listamodif.size(); j++) {
                    if (listamodif.get(j).getCodigo().equals(detalleSeleccionado.getDetalleComprobante().getImpuestos().get(i).getCodigo())) {
                        detalleSeleccionado.getDetalleComprobante().getImpuestos().set(i, listamodif.get(j));
                    }
                }

            }
            calcularTotales();
        } catch (Exception ex) {
            Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        RequestContext.getCurrentInstance().showMessageInDialog(msg);
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private void actualizarPrecioTotalSinImpuesto() {
        Calculos c = new Calculos();
        BigDecimal precio_total_sin_impuestos = new BigDecimal(detalleSeleccionado.getDetalleComprobante().getCantidad()).multiply(new BigDecimal(detalleSeleccionado.getDetalleComprobante().getPrecioUnitario())).subtract(new BigDecimal(detalleSeleccionado.getDetalleComprobante().getDescuento()));
        precio_total_sin_impuestos = precio_total_sin_impuestos.setScale(2, RoundingMode.HALF_EVEN);
        List<ImpuestoComprobanteElectronico> impuestos = detalleSeleccionado.getDetalleComprobante().getImpuestos();
        BigDecimal valor = BigDecimal.ZERO;
        if (impuestos != null) {
            for (ImpuestoComprobanteElectronico i : impuestos) {
                if (i.getCodigoPorcentaje().equals("2") && i.getCodigo().equals("2")) {
                    valor = new BigDecimal(c.obtenerValor12PorCiento(precio_total_sin_impuestos.toString()));
                }
                i.setBaseImponible(precio_total_sin_impuestos.toString());
                i.setValor(valor.toString());
            }
        }
        detalleSeleccionado.getDetalleComprobante().setPrecioTotalSinImpuesto(precio_total_sin_impuestos.toString());
        detalleSeleccionado.getDetalleComprobante().setImpuestos(impuestos);

    }

    public void actualizarDescripcion() {

        if (edicionDescripcion != null && !edicionDescripcion.trim().equals("")) {
            detalleSeleccionado.getDetalleComprobante().setDescripcion(edicionDescripcion);
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La descripción no puede estar vacía.");
        }

    }

    public void actualizarCantidad() {

        if (edicionCantidad.signum() == 1 && edicionCantidad.scale() <= 6) {
            edicionCantidad = edicionCantidad.setScale(2, BigDecimal.ROUND_DOWN);
            detalleSeleccionado.getDetalleComprobante().setCantidad(edicionCantidad.toString());

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La cantidad ingresada no es válida.");
        }

        actualizarPrecioTotalSinImpuesto();
        calcularTotales();
    }

    public void actualizarPrecioUnitario() {

        if (edicionPrecioUnitario.signum() == 1 && edicionPrecioUnitario.scale() <= 6) {
            detalleSeleccionado.getDetalleComprobante().setPrecioUnitario(edicionPrecioUnitario.toString());
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio unitario ingresado no es válido.");
        }
        actualizarPrecioTotalSinImpuesto();
        calcularTotales();

    }

    public void actualizarDescuento() {

        if (edicionDescuento.signum() == 1 && edicionDescuento.scale() <= 6) {
            edicionDescuento = edicionDescuento.setScale(2, BigDecimal.ROUND_DOWN);
            detalleSeleccionado.getDetalleComprobante().setDescuento(edicionDescuento.toString());
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El precio unitario ingresado no es válido.");
        }
        actualizarPrecioTotalSinImpuesto();
        calcularTotales();

    }

    public void actualizarvalorDetalle(InformacionAdicional infoselec) {

        if (edicionValorDetalle != null && !edicionValorDetalle.trim().equals("")) {
            for (int i = 0; i < detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales().size(); i++) {
                if (detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales().get(i).equals(infoselec)) {
                    InformacionAdicional nue = new InformacionAdicional();
                    nue.setNombre(detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales().get(i).getNombre());
                    nue.setValor(edicionValorDetalle);
                    detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales().set(i, nue);
                }
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La descripción no puede estar vacía.");
        }

    }

    public void borrarBusqueda() {
        nombreInfoAdicionalDetalle = null;
    }

    public void ingresarDetalleAdicional2() {
        List<InformacionAdicional> infoadi = detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales();
        if (infoadi == null) {
            infoadi = new ArrayList<InformacionAdicional>();
        }
        if (infoadi.size() < 3) {
            if (nombreInfoAdicionalDetalle != null && !nombreInfoAdicionalDetalle.equals("") && valorInfoAdicionalDetalle != null && !valorInfoAdicionalDetalle.equals("")) {
                InformacionAdicional da = new InformacionAdicional();
                da.setNombre(nombreInfoAdicionalDetalle);
                da.setValor(valorInfoAdicionalDetalle);
                infoadi.add(da);

                detalleSeleccionado.getDetalleComprobante().setDetallesAdicionales(infoadi);
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El detalle adicional se ha agregado.");

            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Los campos no pueden estar vacíos.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pueden agregar más detalles adicionales por este detalle.");
        }
        nombreInfoAdicionalDetalle = "";
        valorInfoAdicionalDetalle = "";
    }

    public void eliminarInfoAdicional2() {
        if (detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales().size() > 0 && detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales() != null) {
            detalleSeleccionado.getDetalleComprobante().getDetallesAdicionales().remove(infoAdicionalSeleccionada);
        }
    }

    public void llenarProducto() {

        try {

            if (productoSeleccionado != null) {
                cantidad = new BigDecimal(BigInteger.ONE);
                cantidad = cantidad.setScale(2, BigDecimal.ROUND_DOWN);
                descuento = new BigDecimal(BigInteger.ZERO);
                descuento = descuento.setScale(2, BigDecimal.ROUND_DOWN);
                valorSelecionado = "";

                producto = productoSeleccionado;
                DatosDetalles = true;
                RequestContext.getCurrentInstance().execute("PF('dialogDetalleAdicionalWV3').hide();");
            } else {

                producto = new Producto();
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Seleccione Un Producto.");
            }

        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void borrarBusqueda2() {
        valorInfoAdicionalDetalle = null;
    }

    public static double redondear(double numero) {
        return Math.round(numero * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public void habilitarTab(Integer numeroTab) {
        this.inhabilitarTabs();
        switch (numeroTab) {
            case 1: {
                this.tab1 = true;
                break;
            }
            case 2: {
                this.tab2 = true;
                break;
            }
            case 3: {
                this.tab3 = true;
                break;
            }
            case 4: {
                this.tab4 = true;
                break;
            }
            case 5: {
                this.tab5 = true;
                break;
            }
        }

    }

    public void inhabilitarTabs() {
        this.tab1 = false;
        this.tab2 = false;
        this.tab3 = false;
        this.tab4 = false;
        this.tab5 = false;
    }

    public void siguiente(Integer indexTab) {
        this.indexTab = indexTab;
        this.habilitarTab(indexTab + 1);
    }

    public void atras() {
        this.indexTab = this.indexTab - 1;
        this.habilitarTab(indexTab + 1);
    }

    public boolean isTab1() {
        return tab1;
    }

    public void setTab1(boolean tab1) {
        this.tab1 = tab1;
    }

    public boolean isTab2() {
        return tab2;
    }

    public void setTab2(boolean tab2) {
        this.tab2 = tab2;
    }

    public boolean isTab3() {
        return tab3;
    }

    public void setTab3(boolean tab3) {
        this.tab3 = tab3;
    }

    public boolean isTab4() {
        return tab4;
    }

    public void setTab4(boolean tab4) {
        this.tab4 = tab4;
    }

    public boolean isTab5() {
        return tab5;
    }

    public void setTab5(boolean tab5) {
        this.tab5 = tab5;
    }

    public Integer getIndexTab() {
        return indexTab;
    }

    public void setIndexTab(Integer indexTab) {
        this.indexTab = indexTab;
    }

    public Empresa getEmisor() {
        return emisor;
    }

    public void setEmisor(Empresa emisor) {
        this.emisor = emisor;
    }

    public String getTipoAmbienteEmisor() {
        return tipoAmbienteEmisor;
    }

    public void setTipoAmbienteEmisor(String tipoAmbienteEmisor) {
        this.tipoAmbienteEmisor = tipoAmbienteEmisor;
    }

    public String getContabilidadEmisor() {
        return contabilidadEmisor;
    }

    public void setContabilidadEmisor(String contabilidadEmisor) {
        this.contabilidadEmisor = contabilidadEmisor;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
    }

    public boolean isDatosCliente() {
        return datosCliente;
    }

    public void setDatosCliente(boolean datosCliente) {
        this.datosCliente = datosCliente;
    }

    public int getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(int tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoPrincipal() {
        return codigoPrincipal;
    }

    public void setCodigoPrincipal(String codigoPrincipal) {
        this.codigoPrincipal = codigoPrincipal;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioUnitarioDetalle() {
        return precioUnitarioDetalle;
    }

    public void setPrecioUnitarioDetalle(BigDecimal precioUnitarioDetalle) {
        this.precioUnitarioDetalle = precioUnitarioDetalle;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Integer getOpcionesDescuento() {
        return opcionesDescuento;
    }

    public void setOpcionesDescuento(Integer opcionesDescuento) {
        this.opcionesDescuento = opcionesDescuento;
    }

    public BigDecimal getDescuentoDetalle() {
        return descuentoDetalle;
    }

    public void setDescuentoDetalle(BigDecimal descuentoDetalle) {
        this.descuentoDetalle = descuentoDetalle;
    }

    public String getImpuestoIVASeleccionado() {
        return impuestoIVASeleccionado;
    }

    public void setImpuestoIVASeleccionado(String impuestoIVASeleccionado) {
        this.impuestoIVASeleccionado = impuestoIVASeleccionado;
    }

    public String getImpuestoICESeleccionado() {
        return impuestoICESeleccionado;
    }

    public void setImpuestoICESeleccionado(String impuestoICESeleccionado) {
        this.impuestoICESeleccionado = impuestoICESeleccionado;
    }

    public List<TarifasImpuesto> getListaTarifaImpuestoIVA() {
        return listaTarifaImpuestoIVA;
    }

    public void setListaTarifaImpuestoIVA(List<TarifasImpuesto> listaTarifaImpuestoIVA) {
        this.listaTarifaImpuestoIVA = listaTarifaImpuestoIVA;
    }

    public Integer getIdImpuestoIVASeleccionado() {
        return idImpuestoIVASeleccionado;
    }

    public void setIdImpuestoIVASeleccionado(Integer idImpuestoIVASeleccionado) {
        this.idImpuestoIVASeleccionado = idImpuestoIVASeleccionado;
    }

    public List<TarifasImpuesto> getListaTarifaImpuestoICE() {
        return listaTarifaImpuestoICE;
    }

    public void setListaTarifaImpuestoICE(List<TarifasImpuesto> listaTarifaImpuestoICE) {
        this.listaTarifaImpuestoICE = listaTarifaImpuestoICE;
    }

    public Integer getIdImpuestoICESeleccionado() {
        return idImpuestoICESeleccionado;
    }

    public void setIdImpuestoICESeleccionado(Integer idImpuestoICESeleccionado) {
        this.idImpuestoICESeleccionado = idImpuestoICESeleccionado;
    }

//    public Detalle getDetalleSeleccionado() {
//        return detalleSeleccionado;
//    }
//
//    public void setDetalleSeleccionado(Detalle detalleSeleccionado) {
//        this.detalleSeleccionado = detalleSeleccionado;
//    }
    public ObjetoListaDetallesPresentacion getDetalleSeleccionado() {
        return detalleSeleccionado;
    }

    public void setDetalleSeleccionado(ObjetoListaDetallesPresentacion detalleSeleccionado) {
        this.detalleSeleccionado = detalleSeleccionado;
    }

    public List<ObjetoListaDetallesPresentacion> getListaDetalles() {
        return listaDetalles;
    }

    public void setListaDetalles(List<ObjetoListaDetallesPresentacion> listaDetalles) {
        this.listaDetalles = listaDetalles;
    }

    public List<ImpuestoComprobanteElectronico> getListaImpuestoDetalle() {
        return listaImpuestoDetalle;
    }

    public void setListaImpuestoDetalle(List<ImpuestoComprobanteElectronico> listaImpuestoDetalle) {
        this.listaImpuestoDetalle = listaImpuestoDetalle;
    }

    public List<InformacionAdicional> getDetallesAdicionales() {
        return detallesAdicionales;
    }

    public void setDetallesAdicionales(List<InformacionAdicional> detallesAdicionales) {
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

    public boolean isTieneDescuento() {
        return tieneDescuento;
    }

    public void setTieneDescuento(boolean tieneDescuento) {
        this.tieneDescuento = tieneDescuento;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Empresa getClienteSeleccionado() {
        return clienteSeleccionado;
    }

    public void setClienteSeleccionado(Empresa clienteSeleccionado) {
        this.clienteSeleccionado = clienteSeleccionado;
    }

    public BigDecimal getSubtotal12C() {
        return subtotal12C;
    }

    public void setSubtotal12C(BigDecimal subtotal12C) {
        this.subtotal12C = subtotal12C;
    }

    public BigDecimal getSubtotal0C() {
        return subtotal0C;
    }

    public void setSubtotal0C(BigDecimal subtotal0C) {
        this.subtotal0C = subtotal0C;
    }

    public BigDecimal getSubtotalNoIvaC() {
        return subtotalNoIvaC;
    }

    public void setSubtotalNoIvaC(BigDecimal subtotalNoIvaC) {
        this.subtotalNoIvaC = subtotalNoIvaC;
    }

    public BigDecimal getSubtotalC() {
        return subtotalC;
    }

    public void setSubtotalC(BigDecimal subtotalC) {
        this.subtotalC = subtotalC;
    }

    public BigDecimal getTotalDescuentoComprobanteC() {
        return totalDescuentoComprobanteC;
    }

    public void setTotalDescuentoComprobanteC(BigDecimal totalDescuentoComprobanteC) {
        this.totalDescuentoComprobanteC = totalDescuentoComprobanteC;
    }

    public BigDecimal getValorICEC() {
        return valorICEC;
    }

    public void setValorICEC(BigDecimal valorICEC) {
        this.valorICEC = valorICEC;
    }

    public BigDecimal getPropina() {
        return propina;
    }

    public void setPropina(BigDecimal propina) {
        this.propina = propina;
    }

    public BigDecimal getIva12C() {
        return iva12C;
    }

    public void setIva12C(BigDecimal iva12C) {
        this.iva12C = iva12C;
    }

    public BigDecimal getValorTotalC() {
        return valorTotalC;
    }

    public void setValorTotalC(BigDecimal valorTotalC) {
        this.valorTotalC = valorTotalC;
    }

    public Secuencial getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Secuencial secuencial) {
        this.secuencial = secuencial;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public boolean isInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(boolean infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public String getNombreInfoAdicional() {
        return nombreInfoAdicional;
    }

    public void setNombreInfoAdicional(String nombreInfoAdicional) {
        this.nombreInfoAdicional = nombreInfoAdicional;
    }

    public String getValorInfoAdicional() {
        return valorInfoAdicional;
    }

    public void setValorInfoAdicional(String valorInfoAdicional) {
        this.valorInfoAdicional = valorInfoAdicional;
    }

    public List<InformacionAdicional> getListaInfoAdicional() {
        return listaInfoAdicional;
    }

    public void setListaInfoAdicional(List<InformacionAdicional> listaInfoAdicional) {
        this.listaInfoAdicional = listaInfoAdicional;
    }

    public InformacionAdicional getInfoAdicionalSeleccionada() {
        return infoAdicionalSeleccionada;
    }

    public void setInfoAdicionalSeleccionada(InformacionAdicional infoAdicionalSeleccionada) {
        this.infoAdicionalSeleccionada = infoAdicionalSeleccionada;
    }

    public String getApagarfirmado() {
        return apagarfirmado;
    }

    public void setApagarfirmado(String apagarfirmado) {
        this.apagarfirmado = apagarfirmado;
    }

    public ObjetoListaDetallesPresentacion getoDetalleSeleccionado() {
        return oDetalleSeleccionado;
    }

    public void setoDetalleSeleccionado(ObjetoListaDetallesPresentacion oDetalleSeleccionado) {
        this.oDetalleSeleccionado = oDetalleSeleccionado;
    }

    public boolean isPanelDetalle() {
        return panelDetalle;
    }

    public void setPanelDetalle(boolean panelDetalle) {
        this.panelDetalle = panelDetalle;
    }

    public List<InformacionAdicional> getListaDetallesAdicionales() {
        return listaDetallesAdicionales;
    }

    public void setListaDetallesAdicionales(List<InformacionAdicional> listaDetallesAdicionales) {
        this.listaDetallesAdicionales = listaDetallesAdicionales;
    }

    public ObjetoListaDetallesPresentacion getdSeleccionado() {
        return dSeleccionado;
    }

    public void setdSeleccionado(ObjetoListaDetallesPresentacion dSeleccionado) {
        this.dSeleccionado = dSeleccionado;
    }

    public List<Secuencial> getSecuenciales() {
        return secuenciales;
    }

    public void setSecuenciales(List<Secuencial> secuenciales) {
        this.secuenciales = secuenciales;
    }

    public Integer getCodigoSecuencialSeleccionado() {
        return codigoSecuencialSeleccionado;
    }

    public void setCodigoSecuencialSeleccionado(Integer codigoSecuencialSeleccionado) {
        this.codigoSecuencialSeleccionado = codigoSecuencialSeleccionado;
    }

    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    public String getCodigoDocModificado() {
        return codigoDocModificado;
    }

    public void setCodigoDocModificado(String codigoDocModificado) {
        this.codigoDocModificado = codigoDocModificado;
    }

    public Date getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public String getValorModificacion() {
        return valorModificacion;
    }

    public void setValorModificacion(String valorModificacion) {
        this.valorModificacion = valorModificacion;
    }

    public String getNumDocModificado() {
        return numDocModificado;
    }

    public void setNumDocModificado(String numDocModificado) {
        this.numDocModificado = numDocModificado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public boolean isDatosDetalles() {
        return DatosDetalles;
    }

    public void setDatosDetalles(boolean DatosDetalles) {
        this.DatosDetalles = DatosDetalles;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getValorSelecionado() {
        return valorSelecionado;
    }

    public void setValorSelecionado(String valorSelecionado) {
        this.valorSelecionado = valorSelecionado;
    }

    public InformacionAdicional getInformacionAdicionalDetalleSeleccionado() {
        return informacionAdicionalDetalleSeleccionado;
    }

    public void setInformacionAdicionalDetalleSeleccionado(InformacionAdicional informacionAdicionalDetalleSeleccionado) {
        this.informacionAdicionalDetalleSeleccionado = informacionAdicionalDetalleSeleccionado;
    }

    public List<InformacionAdicional> getInformacion_adicional() {
        return informacion_adicional;
    }

    public void setInformacion_adicional(List<InformacionAdicional> informacion_adicional) {
        this.informacion_adicional = informacion_adicional;
    }

    public String getEdicionDescripcion() {
        return edicionDescripcion;
    }

    public void setEdicionDescripcion(String edicionDescripcion) {
        this.edicionDescripcion = edicionDescripcion;
    }

    public BigDecimal getEdicionCantidad() {
        return edicionCantidad;
    }

    public void setEdicionCantidad(BigDecimal edicionCantidad) {
        this.edicionCantidad = edicionCantidad;
    }

    public BigDecimal getEdicionPrecioUnitario() {
        return edicionPrecioUnitario;
    }

    public void setEdicionPrecioUnitario(BigDecimal edicionPrecioUnitario) {
        this.edicionPrecioUnitario = edicionPrecioUnitario;
    }

    public BigDecimal getEdicionDescuento() {
        return edicionDescuento;
    }

    public void setEdicionDescuento(BigDecimal edicionDescuento) {
        this.edicionDescuento = edicionDescuento;
    }

    public String getEdicionValorDetalle() {
        return edicionValorDetalle;
    }

    public void setEdicionValorDetalle(String edicionValorDetalle) {
        this.edicionValorDetalle = edicionValorDetalle;
    }

    public String getMensajeResultado() {
        return mensajeResultado;
    }

    public void setMensajeResultado(String mensajeResultado) {
        this.mensajeResultado = mensajeResultado;
    }

    public Receptor getClienteEmpresaReceptor() {
        return clienteEmpresaReceptor;
    }

    public void setClienteEmpresaReceptor(Receptor clienteEmpresaReceptor) {
        this.clienteEmpresaReceptor = clienteEmpresaReceptor;
    }

    public List<Producto> getProductosbusqueda() {
        return productosbusqueda;
    }

    public void setProductosbusqueda(List<Producto> productosbusqueda) {
        this.productosbusqueda = productosbusqueda;
    }

    public Producto getProductoSeleccionado() {
        return productoSeleccionado;
    }

    public void setProductoSeleccionado(Producto productoSeleccionado) {
        this.productoSeleccionado = productoSeleccionado;
    }

    public void onRowSelect(SelectEvent event) {

    }

    public void onRowUnselect(UnselectEvent event) {

    }
}
