/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOAsignacionComprobanteElectronico;
import com.egastos.dao.ec.DAOAuditoria;
import com.egastos.dao.ec.DAOPagos;
import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.AsignacionComprobanteElectronico;
import com.egastos.modelo.ec.Auditoria;
import com.egastos.modelo.ec.Pagos;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.Utilidades;
import com.egastos.utilidades.MensajesPrimefaces;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class BeanGestionVisitas implements Serializable {

    private String seleccionPeriodoTiempo;
    private String fechaInicialS;
    private String fechaFinalS;
    private Date fechaInicial;
    private Date fechaFinal;
    LazyDataModel<UsuarioAcceso> pagosLazy;
    private List<UsuarioAcceso> pagosSeleccionados;
    private UsuarioAcceso pagoSeleccionado;
    private String estado;
    private Date fechaSeleccionadaInicio;
    private Date fechaActual;
    List<String> correos;
    List<String[]> listacontenedora = new ArrayList<String[]>();
    private boolean apareceRago = false;
    private boolean apareceValores = false;
    private String nombreEmpresa;
    private String valorInicial;
    private String valorFinal;
    private String opcionValores;
    private Integer ano;
    private Integer mes;
    private Integer dia;
    private String rucempresa;
    private String detalleFactua;
    private Double valorfactura;
    private String nombreProvedor;
    private List<UsuarioAcceso> usuarioAcceso; 
    
    public String getNombreProvedor() {
        return nombreProvedor;
    }

    public void setNombreProvedor(String nombreProvedor) {
        this.nombreProvedor = nombreProvedor;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getRucempresa() {
        return rucempresa;
    }

    public void setRucempresa(String rucempresa) {
        this.rucempresa = rucempresa;
    }

    public String getDetalleFactua() {
        return detalleFactua;
    }

    public void setDetalleFactua(String detalleFactua) {
        this.detalleFactua = detalleFactua;
    }

    public Double getValorfactura() {
        return valorfactura;
    }

    public void setValorfactura(Double valorfactura) {
        this.valorfactura = valorfactura;
    }

    /**
     * Creates a new instance of BeanComprobantesRecibidos
     */
    public BeanGestionVisitas() {
        ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
        
        try {
        
        
//            guardarLogRegistros("Acceso al modulo  Comprobantes Recibidos");
//DAOUsuarioAcceso daousuario=new DAOUsuarioAcceso();
//usuarioAcceso=daousuario.obtenerUsuario();
                    cargarTablaPagos();
//            cargarTablaPagos();
        } catch (Exception ex) {
            Logger.getLogger(BeanGestionVisitas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     public void actualzarCerrar(){
        try {
            
            ControlSesion control=new ControlSesion();
            control.obtenerIdUsuarioSesionActiva();
            DAOUsuarioAcceso usuarioAc=new DAOUsuarioAcceso();
            usuarioAc.actualizarEstaMensaje(Integer.parseInt(control.obtenerIdUsuarioSesionActiva()),"2");
            
            RequestContext.getCurrentInstance().execute("PF('dlg2').hide();");
            
            
//          MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "MOSTRAR MENSAJE DE QUE SE DEBE ALMACENAR ANTES DE CUMPLIR 3 MESES .");
        } catch (Exception ex) {
            Logger.getLogger(BeanGestionVisitas.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Logger.getLogger(BeanGestionVisitas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean solonumeros(String cedula) {

        if (!(cedula.matches("^[0-9]{49}$"))) {
            return false;
        }
        return true;
    }

    public final void cargarTablaPagos() {
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            pagosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
//                    ControlSesion ms = new ControlSesion();
//                    List<UsuarioAcceso> pagos = new ArrayList<UsuarioAcceso>();
                    DAOUsuarioAcceso dao_ace = null;
                    try {
                        dao_ace = new DAOUsuarioAcceso();
                        // Integer _firstResult, Integer _maxResults,String _cliente,Date _fechaSeleccionadaInicio, Date _fechaActual, String _estado,String valorInicial, String valorFinal
                        pagosSeleccionados = dao_ace.buscarUsuariosInternosVariosParametros(nombreEmpresa,"",valorInicial, valorFinal,first, pageSize);                     
                        this.setRowCount(dao_ace.obtenerTotalUsuariosInternosPorCriteriaComprobantesVariosParametros(nombreEmpresa,"",valorInicial, valorFinal).intValue());                    
                    } catch (Exception ex) {
                        Logger.getLogger(BeanGestionVisitas.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return pagosSeleccionados;
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

                public Object getRowKey(UsuarioAcceso ace) {
                    return ace != null ? ace.getIdUsuario(): null;
                }

                @Override
                public UsuarioAcceso getRowData(String rowKey) {
                    List<UsuarioAcceso> aces = (List<UsuarioAcceso>) getWrappedData();
                    Integer value = Integer.valueOf(rowKey);

                    for (UsuarioAcceso ace : aces) {
                        if (ace.getIdUsuario().equals(value)) {
                            return ace;
                        }
                    }

                    return null;
                }
            };
        }
    }

    public void abrirValores() {
        if (opcionValores.equals("1")) {
            apareceValores = false;
            valorInicial = "";
            valorFinal = "";
        }
        if (opcionValores.equals("2")) {
            apareceValores = false;
            valorInicial = "0";
            valorFinal = "100";

        }
        if (opcionValores.equals("4")) {
            apareceValores = false;
            valorInicial = "100";
            valorFinal = "500";
        }
        if (opcionValores.equals("5")) {
            apareceValores = false;
            valorInicial = "500";
            valorFinal = null;
        }
        if (opcionValores.equals("6")) {
            valorInicial = "0";
            valorFinal = "0";
            apareceValores = true;
        }
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

    public void cerrarDialogRangoFechas() {

        RequestContext.getCurrentInstance().execute("PF('wv-periodo-fechas').hide();");

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
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    fechaInicial = formatter.parse(fechaInicialS);
                } catch (ParseException ex) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error Fecha Inicial (dia/mes/anio).");
                }
                try {
                    fechaFinal = formatter.parse(fechaFinalS);
                } catch (ParseException ex) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error Fecha Final (dia/mes/anio).");
                }
                if (fechaInicial.after(fechaFinal)) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error en rango de fechas (dia/mes/anio).");
                    return;
                }
                fechaSeleccionadaInicio = fechaInicial;
                fechaActual = fechaFinal;
            } else {
                fechaSeleccionadaInicio = null;
                fechaActual = null;
            }
        }

    }

    public void reiniciarParametros() {
        seleccionPeriodoTiempo = null;
        fechaSeleccionadaInicio = null;
        cargarTablaPagos();
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

    public boolean isApareceRago() {
        return apareceRago;
    }

    public void setApareceRago(boolean apareceRago) {
        this.apareceRago = apareceRago;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(String valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }

    public boolean isApareceValores() {
        return apareceValores;
    }

    public void setApareceValores(boolean apareceValores) {
        this.apareceValores = apareceValores;
    }

    public String getOpcionValores() {
        return opcionValores;
    }

    public void setOpcionValores(String opcionValores) {
        this.opcionValores = opcionValores;
    }

    public LazyDataModel<UsuarioAcceso> getPagosLazy() {
        return pagosLazy;
    }

    public void setPagosLazy(LazyDataModel<UsuarioAcceso> pagosLazy) {
        this.pagosLazy = pagosLazy;
    }

    public UsuarioAcceso getPagoSeleccionado() {
        return pagoSeleccionado;
    }

    public void setPagoSeleccionado(UsuarioAcceso pagoSeleccionado) {
        this.pagoSeleccionado = pagoSeleccionado;
    }

    public List<UsuarioAcceso> getUsuarioAcceso() {
        return usuarioAcceso;
    }

    public void setUsuarioAcceso(List<UsuarioAcceso> usuarioAcceso) {
        this.usuarioAcceso = usuarioAcceso;
    }

  

}
