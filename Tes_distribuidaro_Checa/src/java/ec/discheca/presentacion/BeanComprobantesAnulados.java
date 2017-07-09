/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Utilidades;
import discheca.utilidades.Validaciones;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAsignacionComprobanteElectronico;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOComprobanteElectronico;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.AsignacionComprobanteElectronico;
import ec.discheca.modelo.ComprobanteElectronico;
import ec.discheca.utilidades.Descarga;
import ec.discheca.firma.sri.pruebas.FacturacionElectronica;
import ec.discheca.modelo.Auditoria;
import ec.discheca.utilidades.MensajesPrimefaces;
import ec.discheca.utilidades.RIDE;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class BeanComprobantesAnulados implements Serializable {

    private String claveAcceso;
    LazyDataModel<AsignacionComprobanteElectronico> comprobantesAnuladosLazy;
    private List<AsignacionComprobanteElectronico> comprobantesElectronicosSeleccionados;
    private AsignacionComprobanteElectronico comprobanteElectronicoSeleccionado;
    private String seleccionPeriodoTiempo;
    private Date fechaInicial;
    private Date fechaFinal;
    private String secuencialBusqueda;
    private String rucReceptorBusqueda;
    private String tipoDocumento;
    private List<String> ambiente;
    private String estado;
    private Date fechaSeleccionadaInicio;
    private Date fechaActual;

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

    public LazyDataModel<AsignacionComprobanteElectronico> getComprobantesAnuladosLazy() {
        return comprobantesAnuladosLazy;
    }

    public void setComprobantesAnuladosLazy(LazyDataModel<AsignacionComprobanteElectronico> comprobantesAnuladosLazy) {
        this.comprobantesAnuladosLazy = comprobantesAnuladosLazy;
    }

    public String getClaveAcceso() {
        return claveAcceso;
    }

    public void setClaveAcceso(String claveAcceso) {
        this.claveAcceso = claveAcceso;
    }

    /**
     * Creates a new instance of BeanComprobantesAnulados
     */
    public BeanComprobantesAnulados() {
         guardarLogRegistros("Acceso al modulo  Comprobantes Anulados");
        cargarTablaComprobantesAnulados();
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

    public void mostrarconfimacion() {
        if (claveAcceso != null && !claveAcceso.equals("")) {
            RequestContext.getCurrentInstance().execute("PF('confirmacionFirmaAutorizacion').show();");
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El campo clave de acceso no peude estar vacio");
        }
    }

    public void actualizarComprobanteAnulado() {
        if (claveAcceso != null && !claveAcceso.equals("")) {
            if (Validaciones.isNum(claveAcceso)) {
                if (claveAcceso.length() == 49) {
                    try {

                        ec.discheca.firma.sri.pruebas.RespuestaSRI respuesta_pruebas = null;
                        ec.discheca.firma.sri.produccion.RespuestaSRI respuesta_pro = null;
                        ec.discheca.firma.sri.pruebas.ConsultaComprobante consulta_prueba = new ec.discheca.firma.sri.pruebas.ConsultaComprobante();
                        ec.discheca.firma.sri.produccion.ConsultaComprobantePro consulta_pro = new ec.discheca.firma.sri.produccion.ConsultaComprobantePro();
                        FacturacionElectronica fe = new FacturacionElectronica();
                        String ambiente = claveAcceso.substring(23, 24);
                        if (ambiente.equals(Valores.AMBIENTE)) {

                            DAOComprobanteElectronico dao = new DAOComprobanteElectronico();

                            ComprobanteElectronico comprobante = dao.obtenerComprobatePorCA(claveAcceso);
                            if (comprobante != null) {
                                boolean actualizado = dao.actualizarComprobanteAnulado(comprobante.getIdComprobanteElectronico());
                                if (actualizado) {
                                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Comprobante anulado correctamente.");
                                    cargarTablaComprobantesAnulados();
                                } else {
                                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar el comprobante electrónico de AUTORIZADO a ANULADO.");
                                }

                            } else {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No existe un comprobante electrónico guardado con esta clave de acceso.");
                            }

                        } else {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La clave de acceso ingresada no corresponde al ambiente actual.");
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(BeanComprobantesAnulados.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El número de la clave de acceso debe ser de 49 dígitos.");
                }

            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La clave de acceso debe ser un número.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha ingresado una clave de acceso.");
        }
    }

    public final void cargarTablaComprobantesAnulados() {
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            comprobantesAnuladosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<AsignacionComprobanteElectronico> comprobantesElectronicos = new ArrayList<AsignacionComprobanteElectronico>();
                    DAOAsignacionComprobanteElectronico dao_ace = null;
                    try {
                        dao_ace = new DAOAsignacionComprobanteElectronico();
                        comprobantesElectronicos = dao_ace.obtenerComprobantesElectronicosAnuladosPorRUC(ms.obtenerRUCEmpresaSesionActiva(), first, pageSize);
                        this.setRowCount(dao_ace.obtenerTotalComprobantesElectronicosAnuladosPorRUC(ms.obtenerRUCEmpresaSesionActiva()).intValue());
                    } catch (Exception ex) {
                        Logger.getLogger(BeanComprobantesAnulados.class.getName()).log(Level.SEVERE, null, ex);
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

    public void descargarRIDE() {
        if (comprobanteElectronicoSeleccionado != null) {

            FacesContext faces = FacesContext.getCurrentInstance();

            try {
                RIDE descarga = new RIDE();
                byte[] ride = descarga.construirPDFRIDE(comprobanteElectronicoSeleccionado.getComprobanteElectronico());
                HttpServletResponse response = descarga.visualizarRIDE(faces, ride, comprobanteElectronicoSeleccionado.getComprobanteElectronico());
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobantesAnulados.class.getName()).log(Level.SEVERE, null, ex);
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al descargar el PDF-RIDE.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
        }

    }

    public void descargarRespuestaXML() {
        if (comprobanteElectronicoSeleccionado != null) {

            FacesContext faces = FacesContext.getCurrentInstance();

            try {
                RIDE descarga = new RIDE();
                HttpServletResponse response = descarga.bajarArchivoRespuesta(faces, comprobanteElectronicoSeleccionado.getComprobanteElectronico().getArchivoRespuestaSricomprobanteElectronico(), comprobanteElectronicoSeleccionado.getComprobanteElectronico().getClaveAccesoComprobanteElectronico().concat(".xml"));
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobantesAnulados.class.getName()).log(Level.SEVERE, null, ex);
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al descargar el XML.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
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

    public void buscarComprobantesAnulados() {
        this.obtenerFechas();
        ambiente = new ArrayList<String>();
        ambiente.add(Valores.AMBIENTE);
        estado = "12";
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            comprobantesAnuladosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<AsignacionComprobanteElectronico> comprobantesElectronicos = new ArrayList<AsignacionComprobanteElectronico>();
                    DAOAsignacionComprobanteElectronico dao_ace = null;
                    try {
                        dao_ace = new DAOAsignacionComprobanteElectronico();
                        comprobantesElectronicos = dao_ace.buscarComprobantesVariosParametros(ambiente, secuencialBusqueda, ms.obtenerRUCEmpresaSesionActiva(), rucReceptorBusqueda, fechaSeleccionadaInicio, fechaActual, estado, first, pageSize, tipoDocumento);
                        this.setRowCount(dao_ace.obtenerTotalComprobantesVariosParametros(ambiente, secuencialBusqueda, ms.obtenerRUCEmpresaSesionActiva(), rucReceptorBusqueda, fechaSeleccionadaInicio, fechaActual, estado, tipoDocumento).intValue());
                    } catch (Exception ex) {
                        Logger.getLogger(BeanComprobantesAnulados.class.getName()).log(Level.SEVERE, null, ex);
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

    public void reiniciarParametros() {
        rucReceptorBusqueda = null;
        secuencialBusqueda = null;
        seleccionPeriodoTiempo = null;
        fechaSeleccionadaInicio = null;
        fechaActual = null;
        tipoDocumento = "-1";
        cargarTablaComprobantesAnulados();
    }

    public void bajarZIP() {
        if (comprobantesElectronicosSeleccionados != null && !comprobantesElectronicosSeleccionados.isEmpty()) {
            Descarga d = new Descarga();
            String nombre_archivo = "Facturacion-Comprobantes-Electronicos-zip-" + Utilidades.obtenerHHmmss(new Date());
            byte[] zipBytes = d.generarZIP(nombre_archivo, comprobantesElectronicosSeleccionados);
            if (zipBytes != null) {
                FacesContext faces = FacesContext.getCurrentInstance();

                HttpServletResponse response = d.descargarArchivo(faces, zipBytes, nombre_archivo + ".zip");
                faces.responseComplete();
            } else {
                Logger.getLogger(BeanComprobantesAnulados.class.getName()).log(Level.SEVERE, null, "");
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al bajar el archivo comprimido.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar al menos un comprobante.");
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
}
