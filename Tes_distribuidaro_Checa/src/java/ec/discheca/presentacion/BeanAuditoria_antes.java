/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Utilidades;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOClientes;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Receptor;
import ec.discheca.utilidades.MensajesPrimefaces;
import ec.discheca.utilidades.Reportes;
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
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class BeanAuditoria_antes implements Serializable {

    private String seleccionPeriodoTiempo;
    private Date fechaInicial;
    private Date fechaFinal;
    LazyDataModel<Auditoria> registrosLazy;
    private String rucReceptorBusqueda;
    private Date fechaSeleccionadaInicio;
    private Date fechaActual;
    private Auditoria regitroSeleccionado;
    private List<Receptor> clientes;
    private ControlSesion cs;

    private void cargarDatatableConCliente() {
        try {
            if (cs.obtenerEstadoSesionUsuario() == true) {
                clientes = this.instanciarDAO().obtenerTodosCliente();

            }
        } catch (Exception e) {
            Logger.getLogger(BeanAuditoria_antes.class.getName()).log(Level.SEVERE, "Error al cargar los clientes", e);
        }
    }

    public void generacionreporte() {
        try {
            List<Auditoria> registros = new ArrayList<Auditoria>();
            DAOAuditoria daoAuditoria = new DAOAuditoria();
            registros = daoAuditoria.buscarRegistrosVariosParaReporte(rucReceptorBusqueda, fechaSeleccionadaInicio, fechaActual);
            if (registros.size() > 0) {
                Reportes report = new Reportes();
                report.generarReporteregistros(registros);
            } else {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se han encontrado registros.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanReporte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    public String getRucReceptorBusqueda() {
        return rucReceptorBusqueda;
    }

    public void setRucReceptorBusqueda(String rucReceptorBusqueda) {
        this.rucReceptorBusqueda = rucReceptorBusqueda;
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
     * Creates a new instance of BeanComprobantesEmitidos
     */
    public BeanAuditoria_antes() {
        guardarLogRegistros("Acceso al modulo Auditoria");
        cs = new ControlSesion();
        cargarDatatableConCliente();

        cargarregistros();
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

    public void borrarBusqueda() {
        rucReceptorBusqueda = null;

    }

    public List<String> autocompletarUsuariosruc(String query) {
        List<String> usuarios_internos = new ArrayList<String>();
        for (int i = 0; i < this.clientes.size(); i++) {
            Receptor usuario = this.clientes.get(i);
            if (usuario.getRucReceptor().toLowerCase().startsWith(query)) {
                usuarios_internos.add(usuario.getRucReceptor());
            }
        }
        return usuarios_internos;
    }

    public final void cargarregistros() {
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            registrosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<Auditoria> registrosaud = new ArrayList<Auditoria>();
                    DAOAuditoria dao_ace = null;
                    try {
                        dao_ace = new DAOAuditoria();
                        registrosaud = dao_ace.buscarRegistrosVariosParametros(null, null, null, first, pageSize);
                        this.setRowCount(dao_ace.obtenerTotalRegistrosVariosParametros(null, null, null).intValue());

                    } catch (Exception ex) {
                        Logger.getLogger(BeanAuditoria_antes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return registrosaud;
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

                public Object getRowKey(Auditoria ace) {
                    return ace != null ? ace.getIdauditoria() : null;
                }

                @Override
                public Auditoria getRowData(String rowKey) {
                    List<Auditoria> aces = (List<Auditoria>) getWrappedData();
                    Integer value = Integer.parseInt(rowKey);

                    for (Auditoria ace : aces) {
                        if (ace.getIdauditoria() == value) {
                            return ace;
                        }
                    }

                    return null;
                }
            };
        }
    }

    public void buscarRegistros() {
        this.obtenerFechas();
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            registrosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<Auditoria> registrosaud = new ArrayList<Auditoria>();
                    DAOAuditoria dao_ace = null;
                    try {
                        dao_ace = new DAOAuditoria();
                        registrosaud = dao_ace.buscarRegistrosVariosParametros(rucReceptorBusqueda, fechaSeleccionadaInicio, fechaActual, first, pageSize);
                        this.setRowCount(dao_ace.obtenerTotalRegistrosVariosParametros(rucReceptorBusqueda, fechaSeleccionadaInicio, fechaActual).intValue());

                    } catch (Exception ex) {
                        Logger.getLogger(BeanAuditoria_antes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return registrosaud;
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

                public Object getRowKey(Auditoria ace) {
                    return ace != null ? ace.getIdauditoria() : null;
                }

                @Override
                public Auditoria getRowData(String rowKey) {
                    List<Auditoria> aces = (List<Auditoria>) getWrappedData();
                    Integer value = Integer.parseInt(rowKey);

                    for (Auditoria ace : aces) {
                        if (ace.getIdauditoria() == value) {
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
        seleccionPeriodoTiempo = null;
        fechaSeleccionadaInicio = null;
        fechaActual = null;
        fechaInicial = null;
        fechaFinal = null;
        cargarregistros();
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

    public LazyDataModel<Auditoria> getRegistrosLazy() {
        return registrosLazy;
    }

    public void setRegistrosLazy(LazyDataModel<Auditoria> registrosLazy) {
        this.registrosLazy = registrosLazy;
    }

    public Auditoria getRegitroSeleccionado() {
        return regitroSeleccionado;
    }

    public void setRegitroSeleccionado(Auditoria regitroSeleccionado) {
        this.regitroSeleccionado = regitroSeleccionado;
    }

    private DAOClientes instanciarDAO() {
        DAOClientes dao_clientes = null;
        try {
            dao_clientes = new DAOClientes();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_clientes;
    }

}
