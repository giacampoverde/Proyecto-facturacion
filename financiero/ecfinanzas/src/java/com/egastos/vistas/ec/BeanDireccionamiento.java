/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;


import com.egastos.utilidades.ControlSesion;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;


@ManagedBean
@ViewScoped
public class BeanDireccionamiento implements Serializable {

    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
    HttpServletRequest request = (HttpServletRequest) context.getRequest();

    public void direccionarRegistro() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/ru.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanDireccionamiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void direccionarRecuperacionClave() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/recuperacion-clave.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanDireccionamiento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void direccionarLogin() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanDireccionamiento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    

    public void direccionSinSesion() {
        ControlSesion ms = new ControlSesion();
        ms.cerrarSesion();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/sin-sesion.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanDireccionamiento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
