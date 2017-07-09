/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOPerfil;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.UsuarioAcceso;
import ec.discheca.utilidades.MensajesPrimefaces;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@ViewScoped
public class BeanLogin implements Serializable {

    private String nombreUsuario;
    private String claveUsuario;

    public void validarUsuario() {
        try {
            DAOAuditoria daoaudito = new DAOAuditoria();;
            Auditoria insertAudi = new Auditoria();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            DAOUsuarioAcceso daoUsuarioAcceso = null;
            ControlSesion cs = new ControlSesion();

            if (nombreUsuario != null && claveUsuario != null && !nombreUsuario.equals("") && !claveUsuario.equals("")) {
                try {
                    daoUsuarioAcceso = new DAOUsuarioAcceso();

                    UsuarioAcceso usuario = daoUsuarioAcceso.validarUsuarioAcceso(this.nombreUsuario, this.claveUsuario);

                    if (usuario != null) {

                        if (usuario.getEstadoUsuario().equals("1")) {
                            DAOPerfil dp = new DAOPerfil();
                            if (usuario.getPerfil().getDescripcionPerfil().equals("Usuario Interno") || usuario.getPerfil().getDescripcionPerfil().equals("administrador")) {
                                insertAudi.setFecha(new Date());
                                insertAudi.setFechaHora(new Date());
                                insertAudi.setMensajeTransaccion("Inicio De Sesion");
                                insertAudi.setUsuarioAcceso(usuario);
                                daoaudito.insertarRegistro(insertAudi);
                                cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), Valores.VALOR_RUC_EMISOR, "1");
                            } else {
                                insertAudi.setFecha(new Date());
                                insertAudi.setFechaHora(new Date());
                                insertAudi.setMensajeTransaccion("Inicio De Sesion");
                                insertAudi.setUsuarioAcceso(usuario);
                                daoaudito.insertarRegistro(insertAudi);
                                cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), usuario.getIdentificacionUsuario(), "1");
                            }

                            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/inicio.xhtml");

                        } else if (usuario.getEstadoUsuario().equals("3")) {
                            if (usuario.getPerfil().getDescripcionPerfil().equals("Usuario Interno") || usuario.getPerfil().getDescripcionPerfil().equals("administrador")) {
                                insertAudi.setFecha(new Date());
                                insertAudi.setFechaHora(new Date());
                                insertAudi.setMensajeTransaccion("Inicio De Sesion");
                                insertAudi.setUsuarioAcceso(usuario);
                                daoaudito.insertarRegistro(insertAudi);
                                cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), Valores.VALOR_RUC_EMISOR, "1");
                            } else {
                                insertAudi.setFecha(new Date());
                                insertAudi.setFechaHora(new Date());
                                insertAudi.setMensajeTransaccion("Inicio De Sesion");
                                insertAudi.setUsuarioAcceso(usuario);
                                daoaudito.insertarRegistro(insertAudi);
                                cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), usuario.getIdentificacionUsuario(), "1");
                            }
                            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/cambio-clave.xhtml");
                        } else if (usuario.getEstadoUsuario().equals("2")) {
                            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Su cuenta ha sido desactivada.");
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Sus datos de nombre de usuario o contraseña están incorrectos.");

                    }
                } catch (Exception ex) {
                    Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (nombreUsuario == null && claveUsuario == null) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Por favor ingrese su nombre de usuario y clave para continuar.");
            } else if (nombreUsuario.equals("") && claveUsuario.equals("")) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Por favor ingrese su nombre de usuario y clave para continuar.");
            } else if (nombreUsuario.equals("")) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Por favor ingrese su nombre de usuario para continuar.");
            } else if (claveUsuario.equals("")) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Por favor ingrese su clave para continuar.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Creates a new instance of BeanLogin
     */
    public BeanLogin() {

    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

}
