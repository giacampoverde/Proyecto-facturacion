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
public class BeanSesionUsuario implements Serializable {

    /**
     * Creates a new instance of BeanSesionUsuario
     */
    private String nombreUsuario;
    private String primerInfoUsuario;
    private String segundaInfoUsuario;
    
    private String milisegudosDeCierre;

    public BeanSesionUsuario() {
       milisegudosDeCierre="1740000";
//         milisegudosDeCierre="60000";
        try {
            String primer_info_usuario = "";
            String segunda_info_usuario = "";
            ControlSesion ms = new ControlSesion();
            if (ms.obtenerEstadoSesionUsuario() == true) {
                setNombreUsuario(ms.obtenerNombreUsuarioSesionActiva());
                if (ms.obtenerPrimerInfoUsuarioSesionActiva().length() >= 20) {
                    primer_info_usuario = ms.obtenerPrimerInfoUsuarioSesionActiva().substring(0, 20).concat("...");

                } else {
                    primer_info_usuario = ms.obtenerPrimerInfoUsuarioSesionActiva();
                }
                setPrimerInfoUsuario(primer_info_usuario);
                if (ms.obtenerSegundaInfoUsuarioSesionActiva().length() >= 20) {
                    segunda_info_usuario = ms.obtenerSegundaInfoUsuarioSesionActiva().substring(0, 20).concat("...");

                } else {
                    segunda_info_usuario = ms.obtenerSegundaInfoUsuarioSesionActiva();
                }
                setSegundaInfoUsuario(segunda_info_usuario);
            }
        } catch (Exception e) {

        }

    }

    public void irPaginaInformacionUsuarioSesion() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/AdministracionCuentaUsuario.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void irPaginaInformacionGeneral() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/info-general.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void irPaginacambioClave() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/claveInterno.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void irCuenta() {

        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();

        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/cuenta.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void cerrarSesion() {
        ControlSesion ms = new ControlSesion();
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        ms.cerrarSesion();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPrimerInfoUsuario() {
        return primerInfoUsuario;
    }

    public void setPrimerInfoUsuario(String primerInfoUsuario) {
        this.primerInfoUsuario = primerInfoUsuario;
    }

    public String getSegundaInfoUsuario() {
        return segundaInfoUsuario;
    }

    public void setSegundaInfoUsuario(String segundaInfoUsuario) {
        this.segundaInfoUsuario = segundaInfoUsuario;
    }

    public String getMilisegudosDeCierre() {
        return milisegudosDeCierre;
    }

    public void setMilisegudosDeCierre(String milisegudosDeCierre) {
        this.milisegudosDeCierre = milisegudosDeCierre;
    }

    

}
