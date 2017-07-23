/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.ControlSesion;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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
public class BeanMensajeProcesoPagos implements Serializable{
    public UsuarioAcceso usuarios;

    public UsuarioAcceso getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(UsuarioAcceso usuarios) {
        this.usuarios = usuarios;
    }
    @PostConstruct
    public void iniciar(){
        ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }  
    }
    
    public BeanMensajeProcesoPagos(){
        try {
            ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
            
            DAOUsuarioAcceso usuario=new DAOUsuarioAcceso();
            usuarios =usuario.obtenerUsuarioAccesoPorId(Integer.parseInt(ms.obtenerIdUsuarioSesionActiva()));
        } catch (Exception ex) {
            Logger.getLogger(BeanMensajeProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void  dirigirBeanProcesoPagos(){
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/pagos.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(BeanMensajeProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void  RegresarBeanProcesoPaginaPagos(){
        try {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(BeanMensajeProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
