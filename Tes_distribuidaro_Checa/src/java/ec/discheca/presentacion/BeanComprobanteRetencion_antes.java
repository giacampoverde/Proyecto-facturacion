/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Utilidades;
import discheca.utilidades.Validaciones;
import ec.discheca.claveAcceso.ClaveAcceso;
import ec.discheca.pdfride.PDF;
import ec.discheca.comprobanteelectronico.construccionxml.GeneradorComprobanteElectronicoXML;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.InformacionTributariaComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.implementacion.ImplementacionRetencion;
import ec.discheca.comprobanteelectronico.esquema.retencion.ImpuestoRetencion;
import ec.discheca.comprobanteelectronico.esquema.retencion.InformacionComprobanteRetencion;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOClienteEmpresa;
import ec.discheca.dao.DAOComprobanteElectronico;
import ec.discheca.dao.DAOReceptor;
import ec.discheca.dao.DAOSecuencial;
import ec.discheca.dao.DAOTarifasImpuesto;
import ec.discheca.dao.DAOTipoComprobanteElectronico;
import ec.discheca.dao.DAOTipoImpuesto;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Empresa;
import ec.discheca.modelo.Secuencial;
import ec.discheca.modelo.TarifasImpuesto;
import ec.discheca.modelo.TipoComprobanteElectronico;
import ec.discheca.modelo.TipoImpuesto;
import ec.discheca.serviciofacturacion.AlmacenamientoComprobanteElectronico;
import ec.discheca.serviciofacturacion.FirmaAutorizacion;
import ec.discheca.utilidades.MensajesPrimefaces;
import ec.discheca.firma.sri.produccion.RespuestaSRI;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Receptor;
import ec.discheca.serviciofacturacion.Notificacion;
import ec.discheca.utilidades.RIDE;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ricardo Delgado
 */
@ManagedBean
@ViewScoped
public class BeanComprobanteRetencion_antes implements Serializable {

    private boolean tab1;
    private boolean tab2;
    private boolean tab3;
    private boolean tab4;
    private boolean tab5;
    private Integer indexTab;
    private String nombreComercialEmisor;
    private String razonSocialEmisor;
    private String direccionMatrizEmisor;
    private static boolean siguiente = true;
    private String direccionEstablecimientoE;
    private String telefonoEmisor;
    private String codigoEstablecimientoEmisor;
    private String codigoPuntoEmision;
    private String codigoAnexo;
    private String numResolucionEmisor;
    private byte[] logoEmisor;
    private String contabilidadEmisor;
    private String tipoAmbienteEmisor;
    private String obligadoContabilidad;
    private boolean datosCliente;
    private int identificacion;
    private int tipoIdentificacion;
    private String razonSocialComprador;
    private String identificacionComprador;
    private Date fechaEmision;
    private List<String> listaMeses;
    private String mesSeleccionado;
    private List<String> listaAnios;
    private String anioSeleccionado;
    private HashMap<String, String> sustentoTributario;
    /*Datos Impuestos*/
    private BigDecimal baseImponible;
    private BigDecimal valorRetenido;
    private String numDocuSustento;
    private Date fechaEmisionDocSustento;
    private String documentoSustentoSeleccionado;
    private String tipoComprobanteSeleccionado;
    private HashMap<String, String> tiposComprobante;
    private Integer tipoImpuestoSeleccionado;
    private List<TipoImpuesto> listaTipoImpuesto;

    private TarifasImpuesto tarifaImpuestoSeleccionadoObjeto;
    private Integer tarifaImpuestoSeleccionado;
    private TipoComprobanteElectronico documentoSeleccionadoObjeto;
    private Integer documentoSeleccionado;
    private List<TipoComprobanteElectronico> listaDocumentos;

    private BigDecimal baseImponibleE;
    private BigDecimal valorRetenidoE;
    private String porcentajeRetenidoE;
    private String tipoImpuestoE;
    private String numeroDocSustentoE;
    private String codigoDocSustentoE;
    private Date fechaDocSustentoE;
    private String retencionE;

    ///////////////////////////////////////////////
    private String codigoImpuestoRetencion;
    private String codigoImpuesto;
    private List<ImpuestoRetencion> impuestos;
    private ImpuestoRetencion impuestosRetencionSeleccionado;
    private List<TarifasImpuesto> impuestosRetenciones;
    private BigDecimal porcentajeARetener;
    private boolean impuestoVariable;
    private String codDocSustento;
    private String numeroDocSustento;

    private String codigoEstablecimiento;
    private String puntoEmision;
//    private String secuencial;
    private Secuencial secuencial;
    private String apagarfirmado = "false";
    private boolean infoAdicional;

    private String nombreAdicional;
    private String valorAdicional;
    private List<InformacionAdicional> listaInfoAdicional = new ArrayList<InformacionAdicional>();
    InformacionAdicional infoAdicionalSeleccionada;

    Empresa clienteEmpresaEmisor;
    private ImplementacionRetencion comprobante_retencion;
    private List<Secuencial> secuenciales;
    private Integer codigoSecuencialSeleccionado;

    private String correoReceptor;
    private String telefonoreceptor;
    private String correo2Receptor;
    private String direccionReceptor;
    private List<Receptor> receptores;
    private String mensajeResultado;

    public BeanComprobanteRetencion_antes() {
        guardarLogRegistros("Acceso al modulo generacion Retencion");
        fechaEmision = new Date();
        listarPeriodoFiscal();
        cargarDatosEmisor();
        listarTipoImpuestos();
        cargarReceptores();
        impuestos = new ArrayList<ImpuestoRetencion>();
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

    /**
     * Método que trae todos los registros en receptor del RUC o cédula para
     * luego llenar el metodo Autocomplete
     */
    private void cargarReceptores() {
        try {
            DAOReceptor dr = new DAOReceptor();
            receptores = dr.obtenerReceptor();
        } catch (Exception ex) {
            Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
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

    public void listarPeriodoFiscal() {
        Calendar c = Calendar.getInstance();

        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        listaMeses = new ArrayList<String>();

        listaMeses.add("01 - Enero");
        listaMeses.add("02 - Febrero");
        listaMeses.add("03 - Marzo");
        listaMeses.add("04 - Abril");
        listaMeses.add("05 - Mayo");
        listaMeses.add("06 - Junio");
        listaMeses.add("07 - Julio");
        listaMeses.add("08 - Agosto");
        listaMeses.add("09 - Septiembre");
        listaMeses.add("10 - Octubre");
        listaMeses.add("11 - Noviembre");
        listaMeses.add("12 - Diciembre");

        listaAnios = new ArrayList<String>();

        for (int i = anio; i < anio + 30; i++) {
            listaAnios.add(String.valueOf(i));
        }
        mesSeleccionado = listaMeses.get(mes);
        anioSeleccionado = listaAnios.get(0);
    }

    public void cargarDatosEmisor() {
        ControlSesion ms = new ControlSesion();
        try {
            DAOClienteEmpresa clienteempresa = new DAOClienteEmpresa();
            clienteEmpresaEmisor = clienteempresa.obtenerClienteEmpresaPorId(ms.obtenerRUCEmpresaSesionActiva());
            if (clienteEmpresaEmisor != null) {
                if (clienteEmpresaEmisor.getNombreComercialClienteEmpresa() != null) {
                    setNombreComercialEmisor(clienteEmpresaEmisor.getNombreComercialClienteEmpresa());
                }
                if (clienteEmpresaEmisor.getRazonSocialClienteEmpresa() != null) {
                    setRazonSocialEmisor(clienteEmpresaEmisor.getRazonSocialClienteEmpresa());
                }
                if (clienteEmpresaEmisor.getDireccionClienteEmpresa() != null) {
                    setDireccionMatrizEmisor(clienteEmpresaEmisor.getDireccionClienteEmpresa());
                    setDireccionEstablecimientoE(clienteEmpresaEmisor.getDireccionClienteEmpresa());
                }
//                setTipoAmbienteEmisor(Valores.ambiente);
                setTipoAmbienteEmisor("1");

                if (clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa() != null) {
                    setNumResolucionEmisor(clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa());
                }
                if (clienteEmpresaEmisor.getObligadoContabilidadClienteEmpresa() != null) {
                    if (clienteEmpresaEmisor.getObligadoContabilidadClienteEmpresa().equals(Boolean.TRUE)) {
                        setObligadoContabilidad("SI");
                    } else {
                        setObligadoContabilidad("NO");
                    }
                }
                if (clienteEmpresaEmisor.getTelefonoPrincipalClienteEmpresa() != null) {
                    this.clienteEmpresaEmisor.setTelefonoPrincipalClienteEmpresa(clienteEmpresaEmisor.getTelefonoPrincipalClienteEmpresa());
                    setTelefonoEmisor(clienteEmpresaEmisor.getTelefonoPrincipalClienteEmpresa());
                }

                DAOSecuencial ds = new DAOSecuencial();
                Secuencial s = new Secuencial();
                secuenciales = ds.obtenerSecuencialesPorRucYAmbiente(clienteEmpresaEmisor.getIdEmpresa(), Valores.AMBIENTE);
                s = ds.obtenerSecuencialActivoPorRucYAmbiente(clienteEmpresaEmisor.getIdEmpresa(), Valores.AMBIENTE);
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
            Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
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

    public boolean validacionesEmisor() {
        boolean aux = false;
        boolean aux1 = false;
        boolean aux2 = false;
        boolean aux3 = false;
        boolean aux4 = false;
        boolean valoresCorrectos = false;

        if (nombreComercialEmisor == null || nombreComercialEmisor.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo nombre comercial no puede estar vacío.");
        } else {
            aux = true;
        }

        if (razonSocialEmisor == null || razonSocialEmisor.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo razón social no puede estar vacío.");
        } else {
            aux1 = true;
        }

        if (direccionMatrizEmisor == null || direccionMatrizEmisor.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo dirección matríz no puede estar vacío.");
        } else {
            aux2 = true;
        }

        if (tipoAmbienteEmisor == null || tipoAmbienteEmisor.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "debe seleccionar un valor para el campo: tipo de ambiente.");
        } else {
            aux3 = true;
        }

        if (aux && aux1 && aux2 && aux3) {

//            RequestContext.getCurrentInstance().execute("wGenerarCRetencion.next()");
            this.siguiente(1);
            valoresCorrectos = true;

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Existe campos incorrectos.");

        }
        return valoresCorrectos;
    }

    public boolean validacionesReceptor() {

        boolean valor = false;
        boolean aux = false;
        boolean aux1 = false;
        boolean aux2 = false;
        boolean aux3 = false;

        if (fechaEmision == null) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar una fecha.");
        } else {
            aux2 = true;
        }
        if (mesSeleccionado != null && !mesSeleccionado.equals("Seleccione") && !mesSeleccionado.equals("")) {
            if (anioSeleccionado != null && !anioSeleccionado.equals("Seleccione") && !anioSeleccionado.equals("")) {
                String periodoFiscal = mesSeleccionado.substring(0, 1) + "/" + anioSeleccionado;
                if (periodoFiscal == null) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un periodo fiscal.");
                } else {
                    aux3 = true;
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha seleccionado el año para el campo periodo fiscal.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha seleccionado el mes para el campo periodo fiscal.");
        }

        if (razonSocialComprador == null || razonSocialComprador.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo de la razón social comprador no puede estar vacío.");
        } else {
            aux = true;
        }
        if (tipoIdentificacion != 4 && tipoIdentificacion != 5 && tipoIdentificacion != 6) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un tipo de identificación.");
        } else {
            if (identificacionComprador == null || identificacionComprador.equals("")) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo CI/RUC/Pasaporte del receptor no puede estar vacío.");
            } else {
                if (tipoIdentificacion == 4) {
                    if (!Validaciones.validarRucPersonaNatural(identificacionComprador)) {
                        if (!Validaciones.validarRucSociedadPrivada(identificacionComprador)) {
                            if (!Validaciones.validarRucSociedadPublica(identificacionComprador)) {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El RUC ingresado es incorrecto.");
                            } else {
                                aux1 = true;
                            }
                        } else {
                            aux1 = true;
                        }
                    } else {
                        aux1 = true;
                    }

                } else if (tipoIdentificacion == 5) {
                    if (!Validaciones.validarCedula(identificacionComprador)) {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La cédula de identidad ingresada es incorrecta.");
                    } else {
                        aux1 = true;
                    }
                } else if (tipoIdentificacion == 6) {

                    aux1 = true;
                }
            }
        }
        if (aux && aux1 && aux2 && aux3) {
            valor = true;
            this.siguiente(2);

        } else {
            valor = false;
        }

        return valor;
    }

    public void validacionesImpuestos() {

        if (impuestos.isEmpty()) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se han ingresado los impuestos.");
        } else {

            this.siguiente(3);
        }
    }

    public void validacionessiguinete() {

        this.siguiente(3);

    }

    public void validacionessiguinete2() {

        this.siguiente(5);

    }

    public void cargarImpuestos() {
        try {
            DAOTarifasImpuesto dao_impuestos = new DAOTarifasImpuesto();
            if (codigoImpuesto.equals("1")) {
                impuestosRetenciones = dao_impuestos.obtenerImpuestosRenta();
            } else if (codigoImpuesto.equals("2")) {
                impuestosRetenciones = dao_impuestos.obtenerImpuestosIVARenta();
            } else if (codigoImpuesto.equals("6")) {
                impuestosRetenciones = dao_impuestos.obtenerImpuestosISD();
            }
            codigoImpuestoRetencion = impuestosRetenciones.get(0).getCodigoTarifaImpuesto();
            verificarImpuestoVariable();
//            porcentajeARetener = new BigDecimal(impuestosRetenciones.get(0).getPorcentajeTarifaImpuesto());
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verificarImpuestoVariable() {
        if (codigoImpuestoRetencion != null) {
            try {
                String codigoImpuestoAux = codigoImpuesto;
                DAOTarifasImpuesto dao_impuestos = new DAOTarifasImpuesto();
                if (codigoImpuesto.equals("2")) {
                    codigoImpuestoAux = "1";
                }
                TarifasImpuesto ti = dao_impuestos.obtenerImpuestoPorTarifaYTipoImpuesto(codigoImpuestoRetencion, Integer.parseInt(codigoImpuestoAux));
                if (ti != null) {
                    porcentajeARetener = new BigDecimal(ti.getPorcentajeTarifaImpuesto());
                    impuestoVariable = !ti.isVariableTarifaImpuesto();
                    calcularValorRetenido();
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método que calcula el valor a retener según el porcentaje de retención
     */
    public void calcularValorRetenido() {
        if (baseImponible != null) {
            if (Validaciones.validarNumero(baseImponible.toString())) {
                if (baseImponible.scale() <= 2) {
                    valorRetenido = baseImponible.multiply(porcentajeARetener.divide(new BigDecimal(100.00)));
                    valorRetenido.setScale(2, RoundingMode.HALF_EVEN);
                } else {

                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La base imponible debe tener máximo dos decimales.");
                }
            } else {

                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La base imponible debe ser un número.");
            }
        }
    }
//    public void listarTarifaImpuesto() throws Exception {
//        DAOTarifasImpuesto daotarifa = new DAOTarifasImpuesto();
////        tarifaImpuestoSeleccionadoObjeto=daotarifa.obtenerTarifaporIdTarifa(tarifaImpuestoSeleccionado);
//        
//        if (tipoImpuestoSeleccionado != null) {
//            impuestosRetenciones = new ArrayList<TarifasImpuesto>();
//            if (tipoImpuestoSeleccionado.equals("2")) {
////                listaTarifaImpuesto = .listarTarifaTotalImpuestosRetencionIva();
//                if (impuestosRetenciones.size() > 0) {
//                    porcentajeARetener = impuestosRetenciones.get(0).getPorcentajeTarifaImpuesto();
//                    tarifaImpuestoSeleccionadoObjeto = impuestosRetenciones.get(0);
//                } else {
//                      porcentajeARetener= "0";
//                }
//            }
//            if (tipoImpuestoSeleccionado.equals("1")) {
////                listaTarifaImpuesto = HandlerModuloSostenible.listarTarifaTotalImpuestosRetencionRenta();
//                if (impuestosRetenciones.size() > 0) {
//                    porcentajeARetener = impuestosRetenciones.get(0).getPorcentajeTarifaImpuesto().toString();
//                    tarifaImpuestoSeleccionadoObjeto = impuestosRetenciones.get(0);
//                } else {
//                    porcentajeARetener = "0";
//                }
//            }
//            if (tipoImpuestoSeleccionado.equals("6")) {
////                listaTarifaImpuesto = HandlerModuloSostenible.listarTarifaTotalImpuestosRetencionIsd();
//                if (impuestosRetenciones.size() > 0) {
//                    porcentajeARetener = impuestosRetenciones.get(0).getPorcentajeTarifaImpuesto().toString();
//                    tarifaImpuestoSeleccionadoObjeto = impuestosRetenciones.get(0);
//                } else {
//                    porcentajeARetener = "0";
//                }
//            }
//            cambiarPorcentajeARetener();
//        }
//    }

//    public void cambiarPorcentaje() {
//        tarifaImpuestoSeleccionadoObjeto.setPorcentajeTarifaImpuesto(porcentajeRetenido);
////        tarifaImpuestoSeleccionado.setPorcentajeTarifaImpuesto(porcentajeRetenido);
////        tarifaImpuestoSeleccionado.setPorcentajeTarifaImpuestoAux(new BigDecimal(porcentajeRetenido));
//        calcularValor();
//    }
    private DAOSecuencial instanciarDAOSecuencial() {
        DAOSecuencial dao_secuencial = null;
        try {
            dao_secuencial = new DAOSecuencial();
        } catch (Exception ex) {
            Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_secuencial;
    }

    /**
     * Método que verifica el cambio del porcentaje de retención
     */
    public void verificarCambioPorcentajeARetener() {
        if (porcentajeARetener != null) {
            if (Validaciones.validarNumero(porcentajeARetener.toString())) {
                if (porcentajeARetener.scale() <= 2) {
                    calcularValorRetenido();
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje a retener debe tener máximo dos decimales.");

                }
            } else {

                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El porcentaje a retener debe ser un número.");
            }
        }
    }
//    public void cambiarPorcentajeARetener() {
//        setPorcentajeRetenido(tarifaImpuestoSeleccionadoObjeto.getPorcentajeTarifaImpuesto().toString());
////        setPorcentajeRetenido(tarifaImpuestoSeleccionado.getPorcentajeTarifaImpuesto().toString());
////        setEstadoVariable(!tarifaImpuestoSeleccionado.getEstadoVariable);
//        calcularValor();
//    }

//    public void calcularValor() {
//        if (tarifaImpuestoSeleccionado != null) {
//            if (baseImponible != null) {
////                valorRetenido = Validaciones.redondear(baseImponible * (Double.parseDouble(tarifaImpuestoSeleccionado.getPorcentajeTarifaImpuesto().toString()) / 100));
//                valorRetenido = Validaciones.redondear(baseImponible * (Double.parseDouble(tarifaImpuestoSeleccionadoObjeto.getPorcentajeTarifaImpuesto().toString()) / 100));
//            } else {
//                 MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha ingresado valor en el campo base imponible.");
//            }
//        } else {
//             MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado una tarifa de impuesto.");
//        }
//    }
//      /**
//     * Método que calcula el valor a retener según el porcentaje de retención
//     */
    public void limpiarImpuestos() {
        baseImponible = BigDecimal.ZERO;
        valorRetenido = BigDecimal.ZERO;
        porcentajeARetener = BigDecimal.ZERO;
        numeroDocSustento = "";
        codDocSustento = "";
        fechaEmisionDocSustento = null;
        documentoSustentoSeleccionado = null;
        tipoComprobanteSeleccionado = null;
        sustentoTributario = null;
        tiposComprobante = null;
        sustentoTributario = Utilidades.obtSustCompr();
        obtenerTiposComprobante();
        codigoImpuesto = "2";
        cargarImpuestos();
    }

    public void obtenerTiposComprobante() {
        if (documentoSustentoSeleccionado != null) {
            tiposComprobante = Utilidades.obtTipoCompr().get(documentoSustentoSeleccionado);
        }
    }

    public void siguiente(Integer indexTab) {
        this.indexTab = indexTab;
        this.habilitarTab(indexTab + 1);
    }

    public void atras() {
        this.indexTab = this.indexTab - 1;
        this.habilitarTab(indexTab + 1);
    }

    public void inhabilitarTabs() {
        this.tab1 = false;
        this.tab2 = false;
        this.tab3 = false;
        this.tab4 = false;
        this.tab5 = false;
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

    public void eliminarImpuesto() {
        if (impuestosRetencionSeleccionado != null) {
            if (impuestos.size() > 0) {
                impuestos.remove(impuestosRetencionSeleccionado);
            }
        }
    }

    public void onEdit() {
        if (impuestosRetencionSeleccionado != null) {
            setPorcentajeRetenidoE(impuestosRetencionSeleccionado.getPorcentajeRetener());
            setBaseImponibleE(new BigDecimal(impuestosRetencionSeleccionado.getBaseImponible()));
            setValorRetenidoE(new BigDecimal(impuestosRetencionSeleccionado.getValorRetenido()));
            setTipoImpuestoE(impuestosRetencionSeleccionado.getCodigoRetencion());
            setNumeroDocSustentoE(impuestosRetencionSeleccionado.getNumDocSustento());
            if (impuestosRetencionSeleccionado.getCodDocSustento().equals("01")) {
                setCodigoDocSustentoE("Factura");
            }
            if (impuestosRetencionSeleccionado.getCodDocSustento().equals("04")) {
                setCodigoDocSustentoE("Nota de Crédito");
            }
            if (impuestosRetencionSeleccionado.getCodDocSustento().equals("05")) {
                setCodigoDocSustentoE("Nota de Débito");
            }
            if (impuestosRetencionSeleccionado.getCodDocSustento().equals("06")) {
                setCodigoDocSustentoE("Guía de Remisión");
            }
            if (impuestosRetencionSeleccionado.getCodDocSustento().equals("07")) {
                setCodigoDocSustentoE("Comprobante de Retención");
            }
            String fechaEmisionDS;
            DateFormat formato = new SimpleDateFormat("MM/dd/yyyy");
            fechaEmisionDS = formato.format(impuestosRetencionSeleccionado.getFechaEmisionDocSustento());
            setFechaDocSustentoE(new Date(fechaEmisionDS));
//            setRetencionE(totalImpuestoSeleccionado.getTarifaTotalImpuesto().getCodigoTarifa);
        }
    }

    public void agregarImpuesto() {
        if (valorRetenido != null) {
            if (baseImponible.compareTo(BigDecimal.ZERO) == 1) {
                if (Validaciones.validarNumero(valorRetenido.toString())) {
                    if (codDocSustento != null && !codDocSustento.equals("")) {
                        if (Validaciones.isNum(codDocSustento) && codDocSustento.length() <= 2) {
                            if (numeroDocSustento != null && !numeroDocSustento.equals("")) {
                                if (fechaEmisionDocSustento != null) {
                                    ImpuestoRetencion impuesto = new ImpuestoRetencion();
                                    impuesto.setCodigo(codigoImpuesto);
                                    impuesto.setCodigoRetencion(codigoImpuestoRetencion);
                                    impuesto.setBaseImponible(baseImponible.toString());
                                    impuesto.setPorcentajeRetener(porcentajeARetener.toString());
                                    impuesto.setValorRetenido(valorRetenido.toString());
                                    impuesto.setCodDocSustento(codDocSustento);

                                    impuesto.setNumDocSustento(numeroDocSustento.replace("-", ""));
                                    impuesto.setFechaEmisionDocSustento(Utilidades.obtenerFechaFormatoddMMyyyy(fechaEmisionDocSustento));
                                    impuestos.add(impuesto);
                                    RequestContext.getCurrentInstance().execute("PF('dialogIngresarImpuestoRetencionWV').hide();");
                                } else {
                                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La fecha de emisión de documento de sustento no puede estar vacío.");
                                }
                            } else {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El número de documento de sustento no puede estar vacío.");
                            }
                        } else {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El código de documento de sustento debe ser un número de dos dígitos.");
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El código de documento de sustento no puede estar vacío.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor revise los valores de base imponible y porcentaje a retener.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El valor de base imponible debe ser mayor a cero.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor revise los valores de base imponible y porcentaje a retener.");
        }
    }
////    public boolean ingresarImpuesto() {
////      
////        boolean bandera = false;
////        boolean aux1 = false;
////        boolean aux2 = false;
////        boolean aux3 = false;
////        boolean aux4 = false;
////        boolean aux5 = false;
////        boolean aux6 = false;
////      
////        ImpuestoRetencion ti = new ImpuestoRetencion();
////        ti.setCodigo(codigoImpuesto);
////        ti.setCodigoRetencion(codigoImpuestoRetencion);
//////        ti.setCodigoRetencion(codigoAnexo);
////        if (baseImponible != null && baseImponible>0) {
////            ti.setBaseImponible(baseImponible.toString());
////            aux1 = true;
////        } else {
////             MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo base imponible no puede estar vacío");
////        }
////
////        if (documentoSeleccionado != null) {
////            
////            ti.setCodDocSustento(documentoSeleccionado.toString());
//////            ti.setRcodigoDocumentoSustento(documentoSeleccionado.getCodigoTipoDocumento());
////            aux2 = true;
////        } else {
////             MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha seleccionado ningún documento asociado.");
////        }
////
////        if (fechaEmisionDocSustento != null) {
//////            ti.setRfechaEmisionDocumentoSustento(fechaEmisionDocSustento);
////            ti.setFechaEmisionDocSustento(fechaEmisionDocSustento.toString());
////            aux3 = true;
////        } else {
////             MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "No se ha seleccionado ningún valor para el campo fecha emisión documento sustento.");
////        }
////        if (numDocuSustento != null && !numDocuSustento.equals("")) {
//////            ti.setRnumerodocumentoSustento(Utilidades.BorrarCaract(numDocuSustento, "-"));
////            ti.setNumDocSustento(Utilidades.BorrarCaract(numDocuSustento, "-"));
////            aux4 = true;
////        } else {
////              MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "No se ha seleccionado ningún valor para el campo código documento sustento.");
////        }
////
//////        if (tarifaImpuestoSeleccionado != null) {
//////           
//////            ti.setTarifaTotalImpuesto(tarifaImpuestoSeleccionado);
//////            ti.getTarifaTotalImpuesto().setPorcentajeTarifaImpuestoAux(new BigDecimal(porcentajeRetenido));
//////            aux5 = true;
//////        } else {
//////             MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha selecionado ningún valor para el campo código impuesto.");
//////        }
////       if(porcentajeARetener!=null){
////           ti.setPorcentajeRetener(porcentajeARetener);
////       }
////   
////        if (valorRetenido != null && valorRetenido>0) {
////            ti.setValorRetenido(valorRetenido.toString());
////       
////            aux6 = true;
////        } else {
////             MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "valor retenido no calculado.");
////        }
////        if (aux1 && aux2  && aux6) {
////            impuestos.add(ti);
//////           RequestContext.getCurrentInstance().execute("dialogIngresarImpuestoRetencionWV.hide()");
////            bandera = true;
////        } else {
////             MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se agregó el impuesto. Verificar que los valores a ingresar sean correctos.");
////        }
////        return bandera;
////    }

    public void listarTipoImpuestos() {
        try {
            DAOTipoImpuesto daotipimpuesto = new DAOTipoImpuesto();
            DAOTipoComprobanteElectronico daotipoComprobante = new DAOTipoComprobanteElectronico();
            DAOTarifasImpuesto daotarifas = new DAOTarifasImpuesto();
            listaTipoImpuesto = new ArrayList<TipoImpuesto>();
            listaTipoImpuesto = daotipimpuesto.ObtenerTipoImpuesto();
//            for (TipoImpuesto tipo : listaTipoImpuesto) {
//                if (tipo.getIdTipoImpuesto().equals(1)) {
//                    tipo.setIdTipoImpuesto(2);
//                } else if (tipo.getIdTipoImpuesto().equals(2)) {
//                    tipo.setIdTipoImpuesto(1);
//                }
//            }
            TipoImpuesto temporal = listaTipoImpuesto.get(0);
            TipoImpuesto temporal2 = listaTipoImpuesto.get(1);

            listaTipoImpuesto.remove(3);
            listaTipoImpuesto.remove(2);
            listaTipoImpuesto.set(1, temporal);
            listaTipoImpuesto.set(0, temporal2);
            listaDocumentos = new ArrayList<TipoComprobanteElectronico>();
            TipoComprobanteElectronico tipo = daotipoComprobante.obtenerTipoComprobanteElectronicoPorCodigo("01");

            listaDocumentos.add(tipo);

            impuestosRetenciones = new ArrayList<TarifasImpuesto>();
//            impuestosRetenciones = daotarifas.obtenerTarifasporCodigoImpuesto(listaTipoImpuesto.get(0).getIdTipoImpuesto());
            impuestosRetenciones = daotarifas.obtenerImpuestosIVARenta();
            if (impuestosRetenciones.size() > 0) {
                porcentajeARetener = new BigDecimal(impuestosRetenciones.get(0).getPorcentajeTarifaImpuesto());

                tarifaImpuestoSeleccionadoObjeto = impuestosRetenciones.get(0);
//                tarifaImpuestoSeleccionado = listaTarifaImpuesto.get(0);
            } else {
                porcentajeARetener = new BigDecimal(0);

            }
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ingresarInformacionAdicional() {
        if (infoAdicional) {
            if (listaInfoAdicional.size() < 15) {
                if (nombreAdicional != null && !nombreAdicional.equals("") && valorAdicional != null && !valorAdicional.equals("")) {
                    InformacionAdicional infoAd = new InformacionAdicional();
                    infoAd.setNombre(nombreAdicional);
                    infoAd.setValor(valorAdicional);
                    listaInfoAdicional.add(infoAd);
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La información adicional no puede estar vacía.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No puede ingresar más información adicional para el comprobante.");
            }
            nombreAdicional = "";
            valorAdicional = "";
        }
    }

    public void eliminarInfoAdicional() {
        if (listaInfoAdicional.size() > 0 && infoAdicionalSeleccionada != null) {
            listaInfoAdicional.remove(infoAdicionalSeleccionada);
        }
    }

    /**
     * Método que busca un cliente/empresa receptor de la factura por medio del
     * RUC o cédula
     */
    public void buscarClienteEmpresaReceptor() {

        ControlSesion cs = new ControlSesion();
        if (identificacionComprador != null && !identificacionComprador.equals("")) {
            try {
                Receptor r;
                DAOReceptor dr = new DAOReceptor();
                r = dr.obtenerReceptorPorIdentificacion(identificacionComprador);
                if (r != null) {
                    if (r.getRazonSocialReceptor() != null) {
                        razonSocialComprador = r.getRazonSocialReceptor();
                    } else {
                        razonSocialComprador = "";
                    }
                    if (r.getTelefono() != null) {
                        telefonoreceptor = r.getTelefono();
                    } else {
                        telefonoreceptor = "";
                    }
                    if (r.getCorreo() != null) {
                        correoReceptor = r.getCorreo();
                    } else {
                        correoReceptor = "";
                    }
                    if (r.getCorreoAdicional() != null) {
                        correo2Receptor = r.getCorreoAdicional();
                    } else {
                        correo2Receptor = "";
                    }

                    if (r.getDireccion() != null) {
                        direccionReceptor = r.getDireccion();
                    } else {
                        direccionReceptor = "";
                    }
                    if (r.getRucReceptor().length() == 10 && Validaciones.validarCedula(r.getRucReceptor())) {
                        tipoIdentificacion = 5;
                    } else {

                        if (!Validaciones.validarRucPersonaNatural(r.getRucReceptor())) {
                            if (!Validaciones.validarRucSociedadPrivada(r.getRucReceptor())) {
                                if (!Validaciones.validarRucSociedadPublica(r.getRucReceptor())) {
                                    tipoIdentificacion = 6;
                                } else {
                                    tipoIdentificacion = 4;
                                }
                            } else {
                                tipoIdentificacion = 4;
                            }
                        } else {
                            tipoIdentificacion = 4;
                        }

                    }

                } else {
                    razonSocialComprador = "";
                    telefonoreceptor = "";
                    correoReceptor = "";
                    correo2Receptor = "";
                    direccionReceptor = "";
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
//            mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un RUC/Cédula.");
        }
    }

    private InformacionTributariaComprobanteElectronico cargarInformacionTributaria() {
        /**
         * Información Tributaria para generar la retención
         */
        ControlSesion cs = new ControlSesion();
        InformacionTributariaComprobanteElectronico informacion_tributaria = null;
        try {
            informacion_tributaria = new InformacionTributariaComprobanteElectronico();

            informacion_tributaria.setAmbiente(Valores.AMBIENTE);
            informacion_tributaria.setCodDoc(Valores.COMPROBANTE_RETENCION);
            informacion_tributaria.setDirMatriz(clienteEmpresaEmisor.getDireccionClienteEmpresa());
            informacion_tributaria.setRazonSocial(clienteEmpresaEmisor.getRazonSocialClienteEmpresa());
            /**
             * Obtiene el último secuencial guardado en la base de datos;
             */

//            secuencial = this.instanciarDAOSecuencial().obtenerSecuencialPorRucYAmbiente(cs.obtenerRUCEmpresaSesionActiva());
            informacion_tributaria.setCodigoEstablecimiento(secuencial.getCodigoEstablecimientoSecuencial());
            informacion_tributaria.setPuntoEmision(secuencial.getPuntoEmisionSecuencial());
            informacion_tributaria.setSecuencial(Validaciones.completarSecuencial(secuencial.getSecuencialRetencionSecuencial()));
            informacion_tributaria.setNombreComercial(clienteEmpresaEmisor.getNombreComercialClienteEmpresa());
            informacion_tributaria.setRuc(clienteEmpresaEmisor.getIdEmpresa());
            informacion_tributaria.setTipoEmision(Valores.VALOR_TIPO_EMISION);
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return informacion_tributaria;
    }

    private InformacionComprobanteRetencion cargarInformacionRetencion() throws Exception {
        InformacionComprobanteRetencion informacion_retencion = null;
        if (!mesSeleccionado.equals("-1") && !anioSeleccionado.equals("-1")) {

            informacion_retencion = new InformacionComprobanteRetencion();
            informacion_retencion.setFechaEmision(Utilidades.obtenerFechaFormatoddMMyyyy(new Date()));
            informacion_retencion.setDirEstablecimiento(clienteEmpresaEmisor.getDireccionClienteEmpresa());
            if (clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa() != null && !clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa().equals("")) {
                informacion_retencion.setContribuyenteEspecial(clienteEmpresaEmisor.getNumeroResolucionClienteEmpresa());
            }
            informacion_retencion.setFechaEmision(Utilidades.obtenerFechaFormatoddMMyyyy(new Date()));
            informacion_retencion.setObligadoContabilidad(Validaciones.obtenerObligadoContabilidad(clienteEmpresaEmisor.getObligadoContabilidadClienteEmpresa().toString()));
            informacion_retencion.setTipoIdentificacionSujetoRetenido(Validaciones.comprTipoId(identificacionComprador));
            informacion_retencion.setRazonSocialSujetoRetenido(razonSocialComprador.toString());
            informacion_retencion.setIdentificacionSujetoRetenido(identificacionComprador);
            informacion_retencion.setPeriodoFiscal(mesSeleccionado.substring(0, 2).concat("/").concat(anioSeleccionado));
        }
        return informacion_retencion;
    }
//      private List<InformacionAdicional> cargarInformacionAdicional() throws Exception {
//        List<InformacionAdicional> informacion_adicional = new ArrayList<InformacionAdicional>();
//        InformacionAdicional ia = new InformacionAdicional();
//
//        if (telefonoEmisor != null && !clienteEmpresaReceptor.getTelefonoPrincipalClienteEmpresa().equals("") && !clienteEmpresaReceptor.getTelefonoPrincipalClienteEmpresa().equals("N/A")) {
//            ia.setNombre("TELEFONO");
//            ia.setValor(clienteEmpresaReceptor.getTelefonoPrincipalClienteEmpresa());
//            informacion_adicional.add(ia);
//        }
//
//        if (clienteEmpresaReceptor.getCorreoPrincipalClienteEmpresa() != null && !clienteEmpresaReceptor.getCorreoPrincipalClienteEmpresa().equals("") && !clienteEmpresaReceptor.getCorreoPrincipalClienteEmpresa().equals("N/A")) {
//            ia = new InformacionAdicional();
//            ia.setNombre("CORREO");
//            ia.setValor(clienteEmpresaReceptor.getCorreoPrincipalClienteEmpresa());
//            informacion_adicional.add(ia);
//        }
//
//        return informacion_adicional;
//    }

    public boolean generarRetencion() {
        boolean valida_retencion = false;
//        if (clienteEmpresaEmisor != null && clienteEmpresaReceptor != null) {
        if (clienteEmpresaEmisor != null) {

            try {
                comprobante_retencion = new ImplementacionRetencion();

                InformacionTributariaComprobanteElectronico informacion_tributaria = this.cargarInformacionTributaria();

                InformacionComprobanteRetencion informacion_comprobante_retencion = this.cargarInformacionRetencion();

                List<InformacionAdicional> informacion_adicional = listaInfoAdicional;

                ClaveAcceso clave_acceso = new ClaveAcceso();

                comprobante_retencion.setInformacionTributariaComprobanteElectronico(informacion_tributaria);
                comprobante_retencion.setImpuestos(impuestos);
                comprobante_retencion.setInformacionComprobanteRetencion(informacion_comprobante_retencion);

                comprobante_retencion.setInformacionAdicional(informacion_adicional);

                informacion_tributaria.setClaveAcceso(clave_acceso.obtenerClaveDeAcceso(comprobante_retencion, RandomStringUtils.randomNumeric(8)));

                comprobante_retencion.setInformacionTributariaComprobanteElectronico(informacion_tributaria);
                valida_retencion = true;

            } catch (Exception e) {
                Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, e);
            }

        }
        return valida_retencion;
    }

    public void generarRIDE() {
        cambiarSecuencialActivo();

        boolean validado_retencion = false;

//        if (emp != null && clienteEmpresaReceptor != null) {
        if (clienteEmpresaEmisor != null) {
            try {

                validado_retencion = this.generarRetencion();

                if (validado_retencion) {

                    FacesContext faces = FacesContext.getCurrentInstance();
                    DAOComprobanteElectronico dce = new DAOComprobanteElectronico();
                    byte[] pdfByte = dce.generarPDFComprobante(this.comprobante_retencion);
                    HttpServletResponse response = visualizarPDF(faces, pdfByte, this.comprobante_retencion);
                    faces.responseComplete();
                    PDF pdf_ride = new PDF();
                    RIDE visualiza_ride = new RIDE();
                    ///      byte[] logo_empresa = this.instanciarDAOClienteEmpresa().obtenerLogoClienteEmpresaPorId(clienteEmpresaEmisor.getIdClienteEmpresa());
                    byte[] logo_empresa = null;

                    byte[] ride = pdf_ride.construirPDFComprobante(comprobante_retencion, logo_empresa, null, null);
                    visualiza_ride.visualizarRIDE(faces, ride, comprobante_retencion);
                    faces.responseComplete();
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                    Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(BeanAdminSecuenciales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public void firmarYAutorizar() {
        guardarLogRegistros("Firmar y Autorizar Retencion");
        cambiarSecuencialActivo();

//        if (clienteEmpresaEmisor != null && clienteEmpresaReceptor != null) {
        if (clienteEmpresaEmisor != null) {
            boolean validado_retencion = false;
            validado_retencion = this.generarRetencion();

            if (validado_retencion) {
                GeneradorComprobanteElectronicoXML retencion_xml = new GeneradorComprobanteElectronicoXML();

                if (Valores.VALOR_DIRECTORIO_CREACION_XMLS != null) {
                    String fecha_hoy = Utilidades.obtenerFechaEnFormatoAnioMesDia(new Date());
                    String directorio_retencion_hoy = Valores.VALOR_DIRECTORIO_CREACION_XMLS + fecha_hoy.replace("/", File.separator);
                    boolean creado_directorio_hoy = false;
                    if (!new File(directorio_retencion_hoy).exists()) {
                        try {
                            FileUtils.forceMkdir(new File(directorio_retencion_hoy));
                            creado_directorio_hoy = true;
                        } catch (IOException ex) {
                            Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        creado_directorio_hoy = true;
                    }
                    if (creado_directorio_hoy) {
                        String directorio_retencion_hoy_hora_min_segs = directorio_retencion_hoy + File.separator + Utilidades.obtenerHHmmss(new Date());
                        try {
                            FileUtils.forceMkdir(new File(directorio_retencion_hoy_hora_min_segs));
                            File comprobante_electronico = retencion_xml.genArchivoObjComprobante(comprobante_retencion, directorio_retencion_hoy_hora_min_segs + File.separator + comprobante_retencion.getInformacionTributariaComprobanteElectronico().getClaveAcceso(), "1.0.0");
                            if (comprobante_electronico != null) {
                                RespuestaSRI respuesta = null;
                                FirmaAutorizacion fa = new FirmaAutorizacion();
                                 guardarLogRegistros("Enviar nota de credito al Sri clave de acceso: " + comprobante_retencion.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + " secuencial: " + comprobante_retencion.getInformacionTributariaComprobanteElectronico().getSecuencial());
                                respuesta = fa.firmarYAutorizar(comprobante_electronico, directorio_retencion_hoy_hora_min_segs, comprobante_retencion.getInformacionTributariaComprobanteElectronico().getClaveAcceso().concat(".xml"), comprobante_retencion, Valores.COMPROBANTE_RETENCION);
                                if (respuesta != null) {
                                    AlmacenamientoComprobanteElectronico ace = new AlmacenamientoComprobanteElectronico();

                                    Long secuencial_actualizado = Long.parseLong(secuencial.getSecuencialRetencionSecuencial())+1;
                                    if (respuesta.getEstado().equals("1") || respuesta.getEstado().equals("2")) {
                                        this.instanciarDAOSecuencial().actualizarSecuencialPorTipoComprobante(secuencial.getIdSecuencial(), String.valueOf(secuencial_actualizado), Valores.COMPROBANTE_RETENCION);
                                        boolean guardado = ace.guardarComprobanteElectronico(respuesta, comprobante_retencion, Valores.COMPROBANTE_RETENCION, directorio_retencion_hoy_hora_min_segs + File.separator, "");
                                        if (guardado) {
                                            if (respuesta.getEstado().equals("1")) {
                                                mensajeResultado = "COMPROBANTE AUTORIZADO GUARDADO EN EL REPOSITORIO.";
                                                RequestContext.getCurrentInstance().execute("PF('autorizadoNoautorizado').show();");
//                                                 MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE AUTORIZADO GUARDADO EN EL REPOSITORIO.");
                                                Notificacion n = new Notificacion();
                                                n.enviarNotificacion(respuesta, directorio_retencion_hoy_hora_min_segs + File.separator + "R_" + respuesta.getClaveAccesoConsultada() + "-.xml", "ricardo.telcomp@hotmail.com");
                                            } else if (respuesta.getEstado().equals("2")) {
                                                RequestContext.getCurrentInstance().execute("PF('autorizadoNoautorizado').show();");
                                                mensajeResultado = "COMPROBANTE NO AUTORIZADO GUARDADO EN EL REPOSITORIO. ERROR:" + respuesta.getMensaje();
                                                // MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE NO AUTORIZADO GUARDADO EN EL REPOSITORIO. ERROR:" + respuesta.getMensaje());
                                            }
                                        }
                                    } else if (respuesta.getEstado().equals("6")) {
                                        RequestContext.getCurrentInstance().execute("PF('noenviado').show();");
                                        mensajeResultado = "COMPROBANTE NO ENVIADO AL SRI REVISE SU CONEXION A INTERNET. por favor enviar nuevamente.";
//                                         MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE NO ENVIADO AL SRI. El sistema lo enviará posteriormente y se le notificará.");
                                    } else if (respuesta.getEstado().equals("5")) {
                                        RequestContext.getCurrentInstance().execute("PF('noenviado').show();");
                                        mensajeResultado = "COMPROBANTE SIN RESPUESTA DEL SRI. ERROR SRI. por favor enviar nuevamente.";
                                        //  MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE SIN RESPUESTA DEL SRI. ERROR. El sistema lo enviará posteriormente y se le notificará.");
                                    } else if (respuesta.getEstado().equals("10")) {
                                        RequestContext.getCurrentInstance().execute("PF('autorizadoNoautorizado').show();");
                                        mensajeResultado = "COMPROBANTE CON CLAVE DE ACCESO EN PROCESAMIENTO. El sistema lo enviará posteriormente y se le notificará.";
                                        // MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE CON CLAVE DE ACCESO EN PROCESAMIENTO. El sistema lo enviará posteriormente y se le notificará.");
                                    } else if (respuesta.getEstado().equals("11")) {
                                        RequestContext.getCurrentInstance().execute("PF('noenviado').show();");
                                        mensajeResultado = "COMPROBANTE DEVUELTO. " + respuesta.getMensaje();
                                        // MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "COMPROBANTE DEVUELTO.");
                                    }
                                    if (respuesta.getEstado().equals("6") || respuesta.getEstado().equals("5") || respuesta.getEstado().equals("10") || respuesta.getEstado().equals("11")) {
                                        this.instanciarDAOSecuencial().actualizarSecuencialPorTipoComprobante(secuencial.getIdSecuencial(), String.valueOf(secuencial_actualizado), Valores.COMPROBANTE_RETENCION);
                                        ace.guardarComprobanteElectronicoPendiente(respuesta, comprobante_retencion, respuesta.getEstado(), respuesta.getMensaje(), directorio_retencion_hoy_hora_min_segs + File.separator);
                                    }
//                                    RequestContext.getCurrentInstance().execute("PF('wv-comprobante-pago').show();");
                                }
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(BeanComprobanteRetencion_antes.class.getName()).log(Level.SEVERE, null, ex);
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
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/retencion.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void verificarInfoAdicional() {
        if (!infoAdicional) {
            listaInfoAdicional = new ArrayList<InformacionAdicional>();
        }
    }

    public String obtenerSecuencialActivoRetencion(String secuencial) {
        return Validaciones.completarSecuencial(secuencial);
    }

    public String getMensajeResultado() {
        return mensajeResultado;
    }

    public void setMensajeResultado(String mensajeResultado) {
        this.mensajeResultado = mensajeResultado;
    }

    public String getNombreComercialEmisor() {
        return nombreComercialEmisor;
    }

    public void setNombreComercialEmisor(String nombreComercialEmisor) {
        this.nombreComercialEmisor = nombreComercialEmisor;
    }

    public String getRazonSocialEmisor() {
        return razonSocialEmisor;
    }

    public void setRazonSocialEmisor(String razonSocialEmisor) {
        this.razonSocialEmisor = razonSocialEmisor;
    }

    public String getDireccionMatrizEmisor() {
        return direccionMatrizEmisor;
    }

    public void setDireccionMatrizEmisor(String direccionMatrizEmisor) {
        this.direccionMatrizEmisor = direccionMatrizEmisor;
    }

    public static boolean isSiguiente() {
        return siguiente;
    }

    public static void setSiguiente(boolean siguiente) {
        BeanComprobanteRetencion_antes.siguiente = siguiente;
    }

    public String getDireccionEstablecimientoE() {
        return direccionEstablecimientoE;
    }

    public void setDireccionEstablecimientoE(String direccionEstablecimientoE) {
        this.direccionEstablecimientoE = direccionEstablecimientoE;
    }

    public String getTelefonoEmisor() {
        return telefonoEmisor;
    }

    public void setTelefonoEmisor(String telefonoEmisor) {
        this.telefonoEmisor = telefonoEmisor;
    }

    public String getCodigoEstablecimientoEmisor() {
        return codigoEstablecimientoEmisor;
    }

    public void setCodigoEstablecimientoEmisor(String codigoEstablecimientoEmisor) {
        this.codigoEstablecimientoEmisor = codigoEstablecimientoEmisor;
    }

    public String getCodigoPuntoEmision() {
        return codigoPuntoEmision;
    }

    public void setCodigoPuntoEmision(String codigoPuntoEmision) {
        this.codigoPuntoEmision = codigoPuntoEmision;
    }

    public String getCodigoAnexo() {
        return codigoAnexo;
    }

    public void setCodigoAnexo(String codigoAnexo) {
        this.codigoAnexo = codigoAnexo;
    }

    public String getNumResolucionEmisor() {
        return numResolucionEmisor;
    }

    public void setNumResolucionEmisor(String numResolucionEmisor) {
        this.numResolucionEmisor = numResolucionEmisor;
    }

    public byte[] getLogoEmisor() {
        return logoEmisor;
    }

    public void setLogoEmisor(byte[] logoEmisor) {
        this.logoEmisor = logoEmisor;
    }

    public String getContabilidadEmisor() {
        return contabilidadEmisor;
    }

    public void setContabilidadEmisor(String contabilidadEmisor) {
        this.contabilidadEmisor = contabilidadEmisor;
    }

    public String getTipoAmbienteEmisor() {
        return tipoAmbienteEmisor;
    }

    public void setTipoAmbienteEmisor(String tipoAmbienteEmisor) {
        this.tipoAmbienteEmisor = tipoAmbienteEmisor;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public boolean isDatosCliente() {
        return datosCliente;
    }

    public void setDatosCliente(boolean datosCliente) {
        this.datosCliente = datosCliente;
    }

    public int getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(int identificacion) {
        this.identificacion = identificacion;
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

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public List<String> getListaMeses() {
        return listaMeses;
    }

    public void setListaMeses(List<String> listaMeses) {
        this.listaMeses = listaMeses;
    }

    public String getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(String mesSeleccionado) {
        this.mesSeleccionado = mesSeleccionado;
    }

    public List<String> getListaAnios() {
        return listaAnios;
    }

    public void setListaAnios(List<String> listaAnios) {
        this.listaAnios = listaAnios;
    }

    public String getAnioSeleccionado() {
        return anioSeleccionado;
    }

    public void setAnioSeleccionado(String anioSeleccionado) {
        this.anioSeleccionado = anioSeleccionado;
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

    public HashMap<String, String> getSustentoTributario() {
        return sustentoTributario;
    }

    public void setSustentoTributario(HashMap<String, String> sustentoTributario) {
        this.sustentoTributario = sustentoTributario;
    }

    public Integer getTipoImpuestoSeleccionado() {
        return tipoImpuestoSeleccionado;
    }

    public void setTipoImpuestoSeleccionado(Integer tipoImpuestoSeleccionado) {
        this.tipoImpuestoSeleccionado = tipoImpuestoSeleccionado;
    }

    public List<TipoImpuesto> getListaTipoImpuesto() {
        return listaTipoImpuesto;
    }

    public void setListaTipoImpuesto(List<TipoImpuesto> listaTipoImpuesto) {
        this.listaTipoImpuesto = listaTipoImpuesto;
    }

    public BigDecimal getPorcentajeARetener() {
        return porcentajeARetener;
    }

    public void setPorcentajeARetener(BigDecimal porcentajeARetener) {
        this.porcentajeARetener = porcentajeARetener;
    }

    public List<TarifasImpuesto> getImpuestosRetenciones() {
        return impuestosRetenciones;
    }

    public void setImpuestosRetenciones(List<TarifasImpuesto> impuestosRetenciones) {
        this.impuestosRetenciones = impuestosRetenciones;
    }

    public Integer getTarifaImpuestoSeleccionado() {
        return tarifaImpuestoSeleccionado;
    }

    public void setTarifaImpuestoSeleccionado(Integer tarifaImpuestoSeleccionado) {
        this.tarifaImpuestoSeleccionado = tarifaImpuestoSeleccionado;
    }

    public BigDecimal getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(BigDecimal baseImponible) {
        this.baseImponible = baseImponible;
    }

    public BigDecimal getValorRetenido() {
        return valorRetenido;
    }

    public void setValorRetenido(BigDecimal valorRetenido) {
        this.valorRetenido = valorRetenido;
    }

    public boolean isImpuestoVariable() {
        return impuestoVariable;
    }

    public void setImpuestoVariable(boolean impuestoVariable) {
        this.impuestoVariable = impuestoVariable;
    }

    public Integer getDocumentoSeleccionado() {
        return documentoSeleccionado;
    }

    public void setDocumentoSeleccionado(Integer documentoSeleccionado) {
        this.documentoSeleccionado = documentoSeleccionado;
    }

    public List<TipoComprobanteElectronico> getListaDocumentos() {
        return listaDocumentos;
    }

    public void setListaDocumentos(List<TipoComprobanteElectronico> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public Date getFechaEmisionDocSustento() {
        return fechaEmisionDocSustento;
    }

    public void setFechaEmisionDocSustento(Date fechaEmisionDocSustento) {
        this.fechaEmisionDocSustento = fechaEmisionDocSustento;
    }

    public List<ImpuestoRetencion> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<ImpuestoRetencion> impuestos) {
        this.impuestos = impuestos;
    }

    public ImpuestoRetencion getImpuestosRetencionSeleccionado() {
        return impuestosRetencionSeleccionado;
    }

    public void setImpuestosRetencionSeleccionado(ImpuestoRetencion impuestosRetencionSeleccionado) {
        this.impuestosRetencionSeleccionado = impuestosRetencionSeleccionado;
    }

    public String getDocumentoSustentoSeleccionado() {
        return documentoSustentoSeleccionado;
    }

    public void setDocumentoSustentoSeleccionado(String documentoSustentoSeleccionado) {
        this.documentoSustentoSeleccionado = documentoSustentoSeleccionado;
    }

    public String getTipoComprobanteSeleccionado() {
        return tipoComprobanteSeleccionado;
    }

    public void setTipoComprobanteSeleccionado(String tipoComprobanteSeleccionado) {
        this.tipoComprobanteSeleccionado = tipoComprobanteSeleccionado;
    }

    public HashMap<String, String> getTiposComprobante() {
        return tiposComprobante;
    }

    public void setTiposComprobante(HashMap<String, String> tiposComprobante) {
        this.tiposComprobante = tiposComprobante;
    }

    public BigDecimal getBaseImponibleE() {
        return baseImponibleE;
    }

    public void setBaseImponibleE(BigDecimal baseImponibleE) {
        this.baseImponibleE = baseImponibleE;
    }

    public BigDecimal getValorRetenidoE() {
        return valorRetenidoE;
    }

    public void setValorRetenidoE(BigDecimal valorRetenidoE) {
        this.valorRetenidoE = valorRetenidoE;
    }

    public String getPorcentajeRetenidoE() {
        return porcentajeRetenidoE;
    }

    public void setPorcentajeRetenidoE(String porcentajeRetenidoE) {
        this.porcentajeRetenidoE = porcentajeRetenidoE;
    }

    public String getTipoImpuestoE() {
        return tipoImpuestoE;
    }

    public void setTipoImpuestoE(String tipoImpuestoE) {
        this.tipoImpuestoE = tipoImpuestoE;
    }

    public String getNumeroDocSustentoE() {
        return numeroDocSustentoE;
    }

    public void setNumeroDocSustentoE(String numeroDocSustentoE) {
        this.numeroDocSustentoE = numeroDocSustentoE;
    }

    public String getCodigoDocSustentoE() {
        return codigoDocSustentoE;
    }

    public void setCodigoDocSustentoE(String codigoDocSustentoE) {
        this.codigoDocSustentoE = codigoDocSustentoE;
    }

    public Date getFechaDocSustentoE() {
        return fechaDocSustentoE;
    }

    public void setFechaDocSustentoE(Date fechaDocSustentoE) {
        this.fechaDocSustentoE = fechaDocSustentoE;
    }

    public String getRetencionE() {
        return retencionE;
    }

    public void setRetencionE(String retencionE) {
        this.retencionE = retencionE;
    }

    public String getCodigoImpuestoRetencion() {
        return codigoImpuestoRetencion;
    }

    public void setCodigoImpuestoRetencion(String codigoImpuestoRetencion) {
        this.codigoImpuestoRetencion = codigoImpuestoRetencion;
    }

    public String getCodigoImpuesto() {
        return codigoImpuesto;
    }

    public void setCodigoImpuesto(String codigoImpuesto) {
        this.codigoImpuesto = codigoImpuesto;
    }

    public String getCodDocSustento() {
        return codDocSustento;
    }

    public void setCodDocSustento(String codDocSustento) {
        this.codDocSustento = codDocSustento;
    }

    public String getNumeroDocSustento() {
        return numeroDocSustento;
    }

    public void setNumeroDocSustento(String numeroDocSustento) {
        this.numeroDocSustento = numeroDocSustento;
    }

    public String getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(String codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public Secuencial getSecuencial() {
        return secuencial;
    }

    public void setSecuencial(Secuencial secuencial) {
        this.secuencial = secuencial;
    }

    public String getApagarfirmado() {
        return apagarfirmado;
    }

    public void setApagarfirmado(String apagarfirmado) {
        this.apagarfirmado = apagarfirmado;
    }

    public boolean isInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(boolean infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public String getNombreAdicional() {
        return nombreAdicional;
    }

    public void setNombreAdicional(String nombreAdicional) {
        this.nombreAdicional = nombreAdicional;
    }

    public String getValorAdicional() {
        return valorAdicional;
    }

    public void setValorAdicional(String valorAdicional) {
        this.valorAdicional = valorAdicional;
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

    public Integer getCodigoSecuencialSeleccionado() {
        return codigoSecuencialSeleccionado;
    }

    public void setCodigoSecuencialSeleccionado(Integer codigoSecuencialSeleccionado) {
        this.codigoSecuencialSeleccionado = codigoSecuencialSeleccionado;
    }

    public List<Secuencial> getSecuenciales() {
        return secuenciales;
    }

    public void setSecuenciales(List<Secuencial> secuenciales) {
        this.secuenciales = secuenciales;
    }

    public String getCorreoReceptor() {
        return correoReceptor;
    }

    public void setCorreoReceptor(String correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    public String getTelefonoreceptor() {
        return telefonoreceptor;
    }

    public void setTelefonoreceptor(String telefonoreceptor) {
        this.telefonoreceptor = telefonoreceptor;
    }

    public String getCorreo2Receptor() {
        return correo2Receptor;
    }

    public void setCorreo2Receptor(String correo2Receptor) {
        this.correo2Receptor = correo2Receptor;
    }

    public String getDireccionReceptor() {
        return direccionReceptor;
    }

    public void setDireccionReceptor(String direccionReceptor) {
        this.direccionReceptor = direccionReceptor;
    }

}
