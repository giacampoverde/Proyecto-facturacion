/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOPagos;
import com.egastos.dao.ec.DAOPlanesPago;
import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.Pagos;
import com.egastos.modelo.ec.Planespago;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.MensajesPrimefaces;
import com.egastos.utilidades.Valores;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ricar
 */
@ManagedBean
@ViewScoped
public class BeanProcesoPagos implements Serializable {

    public UsuarioAcceso usuarios;
    private String numeroCuentaCliente2;
    private String numeroTransferencia;
    private String valorPagado;
    private List<Planespago> planesPago;
    private Integer planId;
    private Planespago planSeleccionado;
    private String cuentaP;
    private String cuenta2;
    private String cuenta3;
    private String cuentaSeleccionada;

    public String getNumeroTransferencia() {
        return numeroTransferencia;
    }

    public void setNumeroTransferencia(String numeroTransferencia) {
        this.numeroTransferencia = numeroTransferencia;
    }

    public String getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(String cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public String getCuentaP() {
        return cuentaP;
    }

    public void setCuentaP(String cuentaP) {
        this.cuentaP = cuentaP;
    }

    public String getCuenta2() {
        return cuenta2;
    }

    public void setCuenta2(String cuenta2) {
        this.cuenta2 = cuenta2;
    }

    public String getCuenta3() {
        return cuenta3;
    }

    public void setCuenta3(String cuenta3) {
        this.cuenta3 = cuenta3;
    }

    public UsuarioAcceso getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(UsuarioAcceso usuarios) {
        this.usuarios = usuarios;
    }

    public Planespago getPlanSeleccionado() {
        return planSeleccionado;
    }

    public void setPlanSeleccionado(Planespago planSeleccionado) {
        this.planSeleccionado = planSeleccionado;
    }

    public void registrarPago() {
        ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
        try {
            if (planId == null) {
                
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Porfavor seleccione el plan por el cual pago.");
                return;
            } else {
                for (Planespago plan : planesPago) {
                    if (plan.getIdPlanesPago().equals(planId)) {
                        planSeleccionado = plan;
                    }
                }
                if (cuentaSeleccionada == null) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Porfavor seleccione la cuanta a la cual le realizo el pago.");
                    return;
                } else {
                    if (numeroCuentaCliente2.equals("")) {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Porfavor ingrese el numero de cuenta de donde realizo el pago.");
                        return;
                    }
//                    }else{
////                      if(numeroTransferencia.equals("")){
////                          MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Porfavor ingrese el numero de .");
////                          return;
////                      }
//                    }
                }
            }
            Date nuevaFecha = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(nuevaFecha);
            cal.add(Calendar.DATE, planSeleccionado.getMeses());
            nuevaFecha = cal.getTime();
            Pagos pagos = new Pagos();
            pagos.setEstadopago("revision");
            pagos.setFechaPago(new Date());
            pagos.setFechacaduca(nuevaFecha);
            pagos.setNumeroCuentaOrigen(numeroCuentaCliente2);
            pagos.setNumeroCuenta(cuentaSeleccionada);
            pagos.setNumeroTransferencia(numeroTransferencia);
            pagos.setValorPago(planSeleccionado.getValor());
            pagos.setUsuarioAcceso(usuarios);
            pagos.setPlanespago(planSeleccionado);
            //pagos imagen
//            if(imagen){
//                
//            }

            pagos.setImagenTransferencia(cuentaSeleccionada);
            DAOPagos daoPagos = new DAOPagos();
            boolean respuesta = daoPagos.InsertarPago(pagos);
            if (respuesta) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletRequest request = (HttpServletRequest) context.getRequest();
                FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/mensajeFinalPago.xhtml");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BeanProcesoPagos() {
        try {
            ControlSesion ms = new ControlSesion();
            DAOUsuarioAcceso usuario = new DAOUsuarioAcceso();
            DAOPlanesPago daoPlanes = new DAOPlanesPago();

            usuarios = usuario.obtenerUsuarioAccesoPorId(Integer.parseInt(ms.obtenerIdUsuarioSesionActiva()));
            planesPago = daoPlanes.obtenerPlanesPago();
            cuentaP = Valores.numeroCuentaEmpresa;
            cuenta2 = Valores.numeroCuentaEmpresa;
            cuenta3 = Valores.numeroCuentaEmpresa;
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DirigirBeanProcesoPagos() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/pagos.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RegresarBeanProcesoPaginaPagos() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNumeroCuentaCliente2() {
        return numeroCuentaCliente2;
    }

    public void setNumeroCuentaCliente2(String numeroCuentaCliente2) {
        this.numeroCuentaCliente2 = numeroCuentaCliente2;
    }


    public String getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(String valorPagado) {
        this.valorPagado = valorPagado;
    }

    public List<Planespago> getPlanesPago() {
        return planesPago;
    }

    public void setPlanesPago(List<Planespago> planesPago) {
        this.planesPago = planesPago;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

}
