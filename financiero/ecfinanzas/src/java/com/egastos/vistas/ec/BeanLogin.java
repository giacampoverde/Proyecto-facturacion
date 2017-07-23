/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.*;
import com.egastos.modelo.ec.Auditoria;
import com.egastos.modelo.ec.Pagos;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.MensajesPrimefaces;
import com.egastos.utilidades.Valores;
import es.mpsistemas.util.fechas.Fecha;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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

                        daoUsuarioAcceso.actualizarVisitas(usuario.getIdUsuario(),usuario.getVisitas()+1);

                        if (usuario.getEstadoUsuario().equals("1")) {

                            DAOPerfil dp = new DAOPerfil();
                            if (usuario.getPerfil().getDescripcionPerfil().equals("Usuario Interno") || usuario.getPerfil().getDescripcionPerfil().equals("UsuarioAdministrador")) {
                                DAOAsignacionComprobanteElectronico a = new DAOAsignacionComprobanteElectronico();
                                Long numeroComprobantes = a.obtenerTotalComprobantesElectronicosRuecibidosPorRUC(nombreUsuario);
                                if (numeroComprobantes > Integer.parseInt(Valores.VALOR_NUMERO_MAXIMO_COMPROBANTES)) {
                                    DAOPagos pag = new DAOPagos();
                                    Pagos pago = pag.ObtenerPagos(usuario.getIdUsuario(),"pagado");
                                    if (pag != null) {
                                        Fecha actual = new Fecha(new Date());
                                        Fecha vence = new Fecha(pago.getFechaPago());
                                        if (vence.antesOrEquals(actual)) {
                                            cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), "Valores.VALOR_RUC_EMISOR", "1");
                                        } else {
                                            //update a la tabla pagos a estado vencido
                                            //update a la tabla usuario a estado inactivo y mesaje acorde a la razon
                                        }
                                    }
                                } else {
                                    cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), "Valores.VALOR_RUC_EMISOR", "1");

                                    FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/gestionPagos.xhtml");
                                }

                            } else {
                                DAOAsignacionComprobanteElectronico a = new DAOAsignacionComprobanteElectronico();
                                Long numeroComprobantes = a.obtenerTotalComprobantesElectronicosRuecibidosPorRUC(nombreUsuario);
                                if (numeroComprobantes > Integer.parseInt(Valores.VALOR_NUMERO_MAXIMO_COMPROBANTES)) {
                                    DAOPagos pag = new DAOPagos();
                                    Pagos pago = pag.ObtenerPagos(usuario.getIdUsuario(),"pagado");
                                    if (pag != null) {
                                        Fecha actual = new Fecha(new Date());
                                        Fecha vence = new Fecha(pago.getFechacaduca());
                                        if (actual.antesOrEquals(vence)) {

                                            cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), usuario.getIdentificacionUsuario(), "1");
                                            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/inicio.xhtml");
                                        } else {
                                            DAOUsuarioAcceso usuarioAcceso=new DAOUsuarioAcceso();
                                            usuarioAcceso.actualizarestado(usuario.getIdUsuario(),"4");
                                            usuarioAcceso.actualizarMensaje(usuario.getIdUsuario(),"Estimado cliente el periodo por el cual usted pago a finalizado.");
                                            cs.iniciaSesionTemporal(""+usuario.getIdUsuario());
                                            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/mensajePagos.xhtml");
                                            //update a la tabla pagos a estado vencido
                                            //update a la tabla usuario a estado inactivo y mesaje acorde a la razon
                                        }
                                    }else{
                                        DAOUsuarioAcceso usuarioAcceso=new DAOUsuarioAcceso();
                                        usuarioAcceso.actualizarestado(usuario.getIdUsuario(),"4");
                                        usuarioAcceso.actualizarMensaje(usuario.getIdUsuario(),"Estimado cliente su limite de almacenamiento  de 50 comprobantes se ha completado. ");
                                        cs.iniciaSesionTemporal(""+usuario.getIdUsuario());
                                        FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/mensajePagos.xhtml");

                                        //Cargar pantalla de Pagos
                                    }
                                } else {
                                    cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), usuario.getIdentificacionUsuario(), "1");
                                    FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/inicio.xhtml");
                                }
                            }

                        } else if (usuario.getEstadoUsuario().equals("3")) {
                            if (usuario.getPerfil().getDescripcionPerfil().equals("Usuario Interno") || usuario.getPerfil().getDescripcionPerfil().equals("administrador")) {
                                insertAudi.setFecha(new Date());
                                insertAudi.setFechaHora(new Date());
                                insertAudi.setMensajeTransaccion("Inicio De Sesion");
                                insertAudi.setUsuarioAcceso(usuario);
                                daoaudito.insertarRegistro(insertAudi);
                                cs.iniciarSesion(usuario.getNombreUsuarioAcceso(), usuario.getPerfil().getIdPerfil().toString(), usuario.getIdUsuario().toString(), usuario.getNombreUsuario(), usuario.getApellidoUsuario(), usuario.getIdentificacionUsuario(), "Valores.VALOR_RUC_EMISOR", "1");
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
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Su cuenta ha sido desactivada.");
                        }else if(usuario.getEstadoUsuario().equals("4")){
                            DAOPagos pag=new DAOPagos();
                            Pagos pago=pag.ObtenerPagos(usuario.getIdUsuario(),"revision");
//                               cs.iniciaSesionTemporal(""+usuario.getIdUsuario());
                            if(pago!=null){
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Estimado cliente su pago se encuentra en estado de revisión.");
                            }else{
                                cs.iniciaSesionTemporal(""+usuario.getIdUsuario());
                                FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/mensajePagos.xhtml");
                            }
                            //PAGO VENCIDO
                            //el usuario esta inactivo por falta de pago
                            //mostrar una pantalla para que realice el pago
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Nombre de usuario o contraseña están incorrectos.");

                    }
                } catch (Exception ex) {
                    Logger.getLogger(BeanLogin.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (nombreUsuario == null && claveUsuario == null) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor ingrese sus datos para continuar.");
            } else if (nombreUsuario.equals("") && claveUsuario.equals("")) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor ingrese sus datos para continuar.");
            } else if (nombreUsuario.equals("")) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor ingrese su  usuario para continuar.");
            } else if (claveUsuario.equals("")) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor ingrese su clave para continuar.");
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
