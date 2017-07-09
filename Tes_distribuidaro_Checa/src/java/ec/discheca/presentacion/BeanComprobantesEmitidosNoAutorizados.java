/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.presentacion;

import discheca.utilidades.TransformadorArchivos;
import discheca.utilidades.Utilidades;
import ec.discheca.pdfride.PDF;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAsignacionComprobanteElectronico;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOClienteEmpresa;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.AsignacionComprobanteElectronico;
import ec.discheca.modelo.Auditoria;
import ec.discheca.utilidades.Descarga;
import ec.discheca.utilidades.RIDE;
import ec.discheca.utilidades.MensajesPrimefaces;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;


@ManagedBean
@ViewScoped
public class BeanComprobantesEmitidosNoAutorizados implements Serializable{

    private String seleccionPeriodoTiempo;
    private Date fechaInicial;
    private Date fechaFinal;
    private String secuencialBusqueda;
    private String rucReceptorBusqueda;
    private String tipoDocumento;
    private List<String> ambiente;
    private String estado;
    LazyDataModel<AsignacionComprobanteElectronico> comprobantesEmitidosLazy;
    private Date fechaSeleccionadaInicio;
    private Date fechaActual;
    private List<AsignacionComprobanteElectronico> comprobantesElectronicosSeleccionados;
    private AsignacionComprobanteElectronico comprobanteElectronicoSeleccionado;

    public AsignacionComprobanteElectronico getComprobanteElectronicoSeleccionado() {
        return comprobanteElectronicoSeleccionado;
    }

    public void setComprobanteElectronicoSeleccionado(AsignacionComprobanteElectronico comprobanteElectronicoSeleccionado) {
        this.comprobanteElectronicoSeleccionado = comprobanteElectronicoSeleccionado;
    }

    public List<AsignacionComprobanteElectronico> getComprobantesElectronicosSeleccionados() {
        return comprobantesElectronicosSeleccionados;
    }

    public void setComprobantesElectronicosSeleccionados(List<AsignacionComprobanteElectronico> comprobantesElectronicosSeleccionados) {
        this.comprobantesElectronicosSeleccionados = comprobantesElectronicosSeleccionados;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public Date getFechaSeleccionadaInicio() {
        return fechaSeleccionadaInicio;
    }

    public void setFechaSeleccionadaInicio(Date fechaSeleccionadaInicio) {
        this.fechaSeleccionadaInicio = fechaSeleccionadaInicio;
    }

    public LazyDataModel<AsignacionComprobanteElectronico> getComprobantesEmitidosLazy() {
        return comprobantesEmitidosLazy;
    }

    public void setComprobantesEmitidosLazy(LazyDataModel<AsignacionComprobanteElectronico> comprobantesEmitidosLazy) {
        this.comprobantesEmitidosLazy = comprobantesEmitidosLazy;
    }

    public List<String> getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(List<String> ambiente) {
        this.ambiente = ambiente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRucReceptorBusqueda() {
        return rucReceptorBusqueda;
    }

    public void setRucReceptorBusqueda(String rucReceptorBusqueda) {
        this.rucReceptorBusqueda = rucReceptorBusqueda;
    }

    public String getSecuencialBusqueda() {
        return secuencialBusqueda;
    }

    public void setSecuencialBusqueda(String secuencialBusqueda) {
        this.secuencialBusqueda = secuencialBusqueda;
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

    public String getSeleccionPeriodoTiempo() {
        return seleccionPeriodoTiempo;
    }

    public void setSeleccionPeriodoTiempo(String seleccionPeriodoTiempo) {
        this.seleccionPeriodoTiempo = seleccionPeriodoTiempo;
    }
    /**
     * Creates a new instance of BeanComprobantesEmitidosNoAutorizados
     */
    public BeanComprobantesEmitidosNoAutorizados() {
        guardarLogRegistros("Acceso al modulo  Comprobantes Emitidos no Autorizados");
        cargarTablaComprobantesEmitidosNoAutorizados();
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
     
    public final void cargarTablaComprobantesEmitidosNoAutorizados() {
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            comprobantesEmitidosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<AsignacionComprobanteElectronico> comprobantesElectronicos = new ArrayList<AsignacionComprobanteElectronico>();
                    DAOAsignacionComprobanteElectronico dao_ace = null;
                    try {
                        dao_ace = new DAOAsignacionComprobanteElectronico();
                        comprobantesElectronicos = dao_ace.obtenerComprobantesElectronicosNoAutorizadosPorRUC(ms.obtenerRUCEmpresaSesionActiva(), first, pageSize);
                        this.setRowCount(dao_ace.obtenerTotalComprobantesElectronicosNoAutorizadosPorRUC(ms.obtenerRUCEmpresaSesionActiva()).intValue());
                    } catch (Exception ex) {
                        Logger.getLogger(BeanComprobantesEmitidosNoAutorizados.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return comprobantesElectronicos;
                }

                @Override
                public void setRowIndex(int rowIndex
                ) {
                    if (rowIndex == -1 || getPageSize() == 0) {
                        super.setRowIndex(-1);
                    } else {
                        super.setRowIndex(rowIndex % getPageSize());
                    }
                }

                public Object getRowKey(AsignacionComprobanteElectronico ace) {
                    return ace != null ? ace.getIdAsignacionComprobanteElectronico() : null;
                }

                @Override
                public AsignacionComprobanteElectronico getRowData(String rowKey) {
                    List<AsignacionComprobanteElectronico> aces = (List<AsignacionComprobanteElectronico>) getWrappedData();
                    Integer value = Integer.valueOf(rowKey);

                    for (AsignacionComprobanteElectronico ace : aces) {
                        if (ace.getIdAsignacionComprobanteElectronico().equals(value)) {
                            return ace;
                        }
                    }

                    return null;
                }
            };
        }
    }
    
    public void abrirDialogRangoFechas() {
        if (seleccionPeriodoTiempo != null) {
            if (seleccionPeriodoTiempo.equals("6")) {
                fechaInicial = new Date();
                fechaFinal = new Date();
                RequestContext.getCurrentInstance().execute("PF('wv-periodo-fechas').show();");
            } else {
                seleccionPeriodoTiempo = "1";
            }
        }
    }
    
    public String fechaFormateada(Date _fecha) {
        if (_fecha == null) {
            _fecha = new Date();
        }
        return Utilidades.obtenerFechaFormatoddMMyyyy(_fecha);
    }
    
    public void cerrarDialogRangoFechas() {

        RequestContext.getCurrentInstance().execute("PF('wv-periodo-fechas').hide();");

    }
    
    public void buscarComprobantesEmitidosAutorizados() {
        this.obtenerFechas();
        ambiente = new ArrayList<String>();
        ambiente.add(Valores.AMBIENTE);
        estado = "2";
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            comprobantesEmitidosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<AsignacionComprobanteElectronico> comprobantesElectronicos = new ArrayList<AsignacionComprobanteElectronico>();
                    DAOAsignacionComprobanteElectronico dao_ace = null;
                    try {
                        dao_ace = new DAOAsignacionComprobanteElectronico();
                        comprobantesElectronicos = dao_ace.buscarComprobantesVariosParametros(ambiente, secuencialBusqueda, ms.obtenerRUCEmpresaSesionActiva(), rucReceptorBusqueda, fechaSeleccionadaInicio, fechaActual, estado, first, pageSize,tipoDocumento);
                        this.setRowCount(dao_ace.obtenerTotalComprobantesVariosParametros(ambiente, secuencialBusqueda, ms.obtenerRUCEmpresaSesionActiva(), rucReceptorBusqueda, fechaSeleccionadaInicio, fechaActual, estado,tipoDocumento).intValue());
                    } catch (Exception ex) {
                        Logger.getLogger(BeanComprobantesEmitidosNoAutorizados.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return comprobantesElectronicos;
                }

                @Override
                public void setRowIndex(int rowIndex
                ) {
                    if (rowIndex == -1 || getPageSize() == 0) {
                        super.setRowIndex(-1);
                    } else {
                        super.setRowIndex(rowIndex % getPageSize());
                    }
                }

                public Object getRowKey(AsignacionComprobanteElectronico ace) {
                    return ace != null ? ace.getIdAsignacionComprobanteElectronico() : null;
                }

                @Override
                public AsignacionComprobanteElectronico getRowData(String rowKey) {
                    List<AsignacionComprobanteElectronico> aces = (List<AsignacionComprobanteElectronico>) getWrappedData();
                    Integer value = Integer.valueOf(rowKey);

                    for (AsignacionComprobanteElectronico ace : aces) {
                        if (ace.getIdAsignacionComprobanteElectronico().equals(value)) {
                            return ace;
                        }
                    }

                    return null;
                }
            };
        }
    }
    
    private void obtenerFechas() {
        Calendar calendario = Calendar.getInstance();
        Calendar calendario_final_actual = Calendar.getInstance();
        fechaActual = calendario_final_actual.getTime();
        if (seleccionPeriodoTiempo != null && !seleccionPeriodoTiempo.equals("")) {

            if (seleccionPeriodoTiempo.equals("2")) {
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
            } else if (seleccionPeriodoTiempo.equals("3")) {
                int semana = calendario.get(Calendar.WEEK_OF_YEAR);
                semana--;
                calendario.set(Calendar.WEEK_OF_YEAR, semana);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
            } else if (seleccionPeriodoTiempo.equals("4")) {
                int mes = calendario.get(Calendar.MONTH);
                mes--;
                calendario.set(Calendar.MONTH, mes);
                calendario.set(Calendar.DAY_OF_MONTH, 1);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
                calendario_final_actual.set(Calendar.DAY_OF_MONTH, 1);
                calendario_final_actual.add(Calendar.DAY_OF_MONTH, -1);
                fechaActual = calendario_final_actual.getTime();

            } else if (seleccionPeriodoTiempo.equals("5")) {
                int mes = calendario.get(Calendar.MONTH);
                mes = mes - 6;
                calendario.set(Calendar.MONTH, mes);
                calendario.set(Calendar.DAY_OF_MONTH, 1);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
            } else if (seleccionPeriodoTiempo.equals("6")) {

                fechaSeleccionadaInicio = fechaInicial;
                fechaActual = fechaFinal;
            } else {
                fechaSeleccionadaInicio = null;
                fechaActual = null;
            }
        }

    }
    
    public void descargarRIDE() {
        if (comprobanteElectronicoSeleccionado != null) {

            FacesContext faces = FacesContext.getCurrentInstance();

            try {
                byte[] logo_empresa = this.instanciarDAOClienteEmpresa().obtenerLogoClienteEmpresaPorId(comprobanteElectronicoSeleccionado.getComprobanteElectronico().getRucEmisorComprobanteElectronico());
                PDF pdf_ride=new PDF();
                ComprobanteElectronico c = TransformadorArchivos.byteCompr(comprobanteElectronicoSeleccionado.getComprobanteElectronico().getComprobanteFirmadoComprobanteElectronico(), Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS + RandomStringUtils.randomAlphanumeric(10));
                byte[] ride = pdf_ride.construirPDFComprobante(c, logo_empresa, comprobanteElectronicoSeleccionado.getComprobanteElectronico().getNumeroAutorizacionComprobanteElectronico(), comprobanteElectronicoSeleccionado.getComprobanteElectronico().getFechaAutorizacionComprobanteElectronico());
                RIDE descarga = new RIDE();
                HttpServletResponse response = descarga.visualizarRIDE(faces, ride, comprobanteElectronicoSeleccionado.getComprobanteElectronico());
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobantesEmitidosNoAutorizados.class.getName()).log(Level.SEVERE, null, ex);
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al descargar el PDF-RIDE.");
            }

        } else {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
        }

    }
    
    private DAOClienteEmpresa instanciarDAOClienteEmpresa() {
        DAOClienteEmpresa dao_cliente_empresa = null;
        try {
            dao_cliente_empresa = new DAOClienteEmpresa();
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobantesEmitidosNoAutorizados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_cliente_empresa;
    }
    
    public void descargarRespuestaXML() {
        if (comprobanteElectronicoSeleccionado != null) {

            FacesContext faces = FacesContext.getCurrentInstance();

            try {
                RIDE descarga = new RIDE();
                HttpServletResponse response = descarga.bajarArchivoRespuesta(faces, comprobanteElectronicoSeleccionado.getComprobanteElectronico().getArchivoRespuestaSricomprobanteElectronico(), comprobanteElectronicoSeleccionado.getComprobanteElectronico().getClaveAccesoComprobanteElectronico().concat(".xml"));
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobantesEmitidosNoAutorizados.class.getName()).log(Level.SEVERE, null, ex);
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al descargar el XML.");
            }

        } else {
            MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
        }

    }
    
    public void mostrarErrorNoAutorizado(){
        if(comprobanteElectronicoSeleccionado!=null){
            RequestContext.getCurrentInstance().execute("PF('wv-error-no-autorizacion').show();");
        }
    }
    
    public void bajarZIP() {
        if (comprobantesElectronicosSeleccionados != null && !comprobantesElectronicosSeleccionados.isEmpty()) {
            Descarga d = new Descarga();
            String nombre_archivo = "Facturar-Comprobantes-Electronicos-NoAutorizados-zip-" + Utilidades.obtenerHHmmss(new Date());
            byte[] zipBytes = d.generarZIP(nombre_archivo, comprobantesElectronicosSeleccionados);
            if (zipBytes != null) {
                FacesContext faces = FacesContext.getCurrentInstance();

                HttpServletResponse response = d.descargarArchivo(faces, zipBytes, nombre_archivo + ".zip");
                faces.responseComplete();
            } else {
                Logger.getLogger(BeanComprobantesEmitidosNoAutorizados.class.getName()).log(Level.SEVERE, null, "");
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al bajar el archivo comprimido.");
            }

        } else {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe seleccionar al menos un comprobante.");
        }

    }
    
    public void reiniciarParametros() {
        rucReceptorBusqueda = null;
        secuencialBusqueda = null;
        seleccionPeriodoTiempo = null;
        fechaSeleccionadaInicio = null;
        fechaActual = null;
        tipoDocumento = "-1";
        cargarTablaComprobantesEmitidosNoAutorizados();
    }
}
