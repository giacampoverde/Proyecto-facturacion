/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.correo.Correo;
import com.egastos.utilidades.Validaciones;
import com.egastos.utilidades.AES256;
import com.egastos.utilidades.Valores;
import com.egastos.dao.ec.DAOPerfil;
import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.Perfil;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.MensajesPrimefaces;
import java.io.Serializable;
import java.util.ArrayList;
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
import org.primefaces.context.RequestContext;

@ManagedBean
@ViewScoped
public class BeanRegistro implements Serializable {

    private boolean esUsuario;
    private Integer opcionEsUsuario;
    private String direccionSecuencial;
    private String puntoEmision;
    private String codigoEstablecimiento;
    private UsuarioAcceso usuarioNuevo;
    private boolean agregarSecuencial;
    private String confirmacionClave;
    private String confirmacionClaveEmpresa;
    private String indexAsociado;

    public BeanRegistro() {
        indexAsociado = "";
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        if (request.getParameter("pag") != null) {
            indexAsociado = request.getParameter("pag");
        }
        this.opcionEsUsuario = 1;
        this.esUsuario = true;
        this.usuarioNuevo = new UsuarioAcceso();
    }

    public boolean validarDatosUsuario() {
        boolean respuesta = true;
        if (this.usuarioNuevo.getIdentificacionUsuario() == null || this.usuarioNuevo.getIdentificacionUsuario().trim().equals("")) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar el número de identificación.");
            respuesta = false;
        } else if (this.usuarioNuevo.getNombreUsuario() == null || this.usuarioNuevo.getNombreUsuario().trim().equals("")) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar sus nombres.");
            respuesta = false;
        } else if (!Validaciones.isTexto(this.usuarioNuevo.getNombreUsuario())) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar solo letras en el campo Nombres.");
            respuesta = false;
        } else if (this.usuarioNuevo.getApellidoUsuario() == null || this.usuarioNuevo.getApellidoUsuario().trim().equals("")) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar sus apellidos.");
            respuesta = false;
        } else if (!Validaciones.isTexto(this.usuarioNuevo.getApellidoUsuario())) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar solo letras en el campo Apellidos.");
            respuesta = false;
        } else if (this.usuarioNuevo.getTelefonoPrincipalUsuario() == null || this.usuarioNuevo.getTelefonoPrincipalUsuario().trim().equals("")) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número de teléfono.");
            respuesta = false;
        } else if (!Validaciones.isNum(this.usuarioNuevo.getTelefonoPrincipalUsuario())) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar solo números en el campo Teléfono Principal.");
            respuesta = false;
        } else if (this.usuarioNuevo.getTelefonoAdicionalUsuario() == null && this.usuarioNuevo.getTelefonoAdicionalUsuario().trim().equals("")) {
            if (!Validaciones.isNum(this.usuarioNuevo.getTelefonoAdicionalUsuario())) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar solo números en el campo Teléfono Adicional.");
                respuesta = false;
            }
        } else if (this.usuarioNuevo.getCorreoPrincipalUsuario() == null || this.usuarioNuevo.getCorreoPrincipalUsuario().trim().equals("")) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo eléctrónico.");
            respuesta = false;
        } else if (!Validaciones.isEmail(this.usuarioNuevo.getCorreoPrincipalUsuario())) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo eléctrónico principal válido.");
            respuesta = false;
        } else if (this.usuarioNuevo.getCorreoAdicionalUsuario() != null && !this.usuarioNuevo.getCorreoAdicionalUsuario().trim().equals("")) {
            if (!Validaciones.isEmail(this.usuarioNuevo.getCorreoAdicionalUsuario())) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo eléctrónico adicional válido.");
                respuesta = false;
            }
        } else if (this.usuarioNuevo.getClaveUsuarioAcceso() == null || this.usuarioNuevo.getClaveUsuarioAcceso().trim().equals("")) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar una contraseña.");
            respuesta = false;
        } else if (this.usuarioNuevo.getClaveUsuarioAcceso().trim().length() < 8) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La contraseña ingresada debe tener mínimo 8 caracteres.");
            respuesta = false;
        } else if (!this.usuarioNuevo.getClaveUsuarioAcceso().equals(this.confirmacionClave)) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La contraseña ingresada no coincide con la confirmación de la misma.");
            respuesta = false;
        }
        return respuesta;
    }

    private boolean validarIdentificacion(String identificacion) {
        boolean validado = false;
        DAOUsuarioAcceso dao_usuario_acceso = null;
        try {
            dao_usuario_acceso = new DAOUsuarioAcceso();

            if (identificacion != null && !identificacion.equals("")) {

                if (Validaciones.isNum(identificacion)) {
                    if (identificacion.length() == 10) {
                        if (Validaciones.validarCedula(identificacion)) {

                            UsuarioAcceso ua1 = null;
                            UsuarioAcceso ua2 = null;
                            try {
                                ua1 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion);
                                ua2 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion + "001");
                            } catch (Exception ex) {
                                Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (ua1 == null && ua2 == null) {
                                validado = true;
                            } else {
                                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La identificación ya se encuentra registrada por otro usuario/empresa.");
                            }
                        } else {
                            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El número de cédula no es válido.");
                        }
                    } else if (identificacion.length() == 13) {
                        if (!Validaciones.validarRucPersonaNatural(identificacion)) {
                            if (!Validaciones.validarRucSociedadPrivada(identificacion)) {
                                if (!Validaciones.validarRucSociedadPublica(identificacion)) {
                                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El RUC no es válido.");
                                } else {
                                    validado = true;
                                }
                            } else {
                                validado = true;
                            }
                        } else {
                            validado = true;
                        }
                        if (validado) {
                            UsuarioAcceso ua1 = null;
                            UsuarioAcceso ua2 = null;
                            try {
                                ua1 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion);
                                ua2 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion.substring(0, 10));
                            } catch (Exception ex) {
                                Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (ua1 == null && ua2 == null) {
                                validado = true;
                            } else {
                                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La identificación ya se encuentra registrada por otro usuario.");
                                validado = false;
                            }
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La identificación ingresada no pertenece a una Cédula/RUC válido.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La identificación debe ser un número.");
                }

            } else {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La identificación Cédula/RUC no puede estar vacía.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validado;
    }

    public void registrarUsuario() {

        if (validarDatosUsuario()) {
            if (validarIdentificacion(usuarioNuevo.getIdentificacionUsuario())) {
                try {
                    DAOPerfil daoperfiles = new DAOPerfil();
                    DAOUsuarioAcceso daousuario = new DAOUsuarioAcceso();
                    UsuarioAcceso usuarioRegistrado;
                    Perfil perfilusado = daoperfiles.obtenerPerfilPorNombre("UsuarioExterno");
                    List<String> contenido = new ArrayList<String>();
                    contenido.add(usuarioNuevo.getIdentificacionUsuario());
                    contenido.add(usuarioNuevo.getClaveUsuarioAcceso());
                    usuarioNuevo.setClaveUsuarioAcceso(AES256.toAES256(usuarioNuevo.getClaveUsuarioAcceso()));
                    usuarioNuevo.setNombreUsuarioAcceso(usuarioNuevo.getIdentificacionUsuario());
                    usuarioNuevo.setFechaRegistroUsuario(new Date());
                    usuarioNuevo.setEstadoUsuario("1");
                    usuarioNuevo.setVisitas(0);
                    usuarioNuevo.setMensajeOpcional("1");
                    //Asignar perfil
                    usuarioNuevo.setPerfil(perfilusado);
                    usuarioRegistrado = daousuario.insertarUsuarioAcceso(usuarioNuevo);
                    //Enviar Correo

                    // MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El registro se ha completado con exito.");
                    if (usuarioRegistrado != null) {
                        Correo envio = new Correo("1");

//                     envio.enviarMailReguistroecFinanzas(direccionSecuencial, _contenido, puntoEmision, puntoEmision, puntoEmision, imagenes)
                        envio.enviarCorreoRegistro(this.usuarioNuevo.getCorreoPrincipalUsuario(), contenido);

                        if (!this.usuarioNuevo.getCorreoAdicionalUsuario().equals("")) {
//                        Util.enviarCorreoRegistro(this.usuarioNuevo.getCorreoAdicionalUsuario());
                        }
                        vaciarCamposUsuario();
                        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                        HttpServletRequest request = (HttpServletRequest) context.getRequest();
                        FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/mensajeFinalRegistro.xhtml");
                    }else{
                       MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El registro se ha completado con exito.");
                       RequestContext.getCurrentInstance().update("todo");
                    }
                            RequestContext.getCurrentInstance().update("todo");
//                    RequestContext.getCurrentInstance().update("pgRegistro");
//                    RequestContext.getCurrentInstance().update("pgRegistroEmpresa");
                } catch (Exception ex) {
                    Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void vaciarCamposUsuario() {
        usuarioNuevo = new UsuarioAcceso();
        this.confirmacionClave = "";
    }

    public void vaciarCamposEmpresa() {
        this.confirmacionClaveEmpresa = "";
        this.puntoEmision = "";
        this.codigoEstablecimiento = "";
        this.direccionSecuencial = "";
    }

//    public void cambioRadioButtonRegistro() {
//        if (this.opcionEsUsuario == 2) {
//            this.esUsuario = false;
//        } else {
//            this.esUsuario = true;
//        }
//    }
    public boolean isEsUsuario() {
        return esUsuario;
    }

    public void setEsUsuario(boolean esUsuario) {
        this.esUsuario = esUsuario;
    }

    public Integer getOpcionEsUsuario() {
        return opcionEsUsuario;
    }

    public void setOpcionEsUsuario(Integer opcionEsUsuario) {
        this.opcionEsUsuario = opcionEsUsuario;
    }

    public UsuarioAcceso getUsuarioNuevo() {
        return usuarioNuevo;
    }

    public void setUsuarioNuevo(UsuarioAcceso usuarioNuevo) {
        this.usuarioNuevo = usuarioNuevo;
    }

    public String getConfirmacionClave() {
        return confirmacionClave;
    }

    public void setConfirmacionClave(String confirmacionClave) {
        this.confirmacionClave = confirmacionClave;
    }

    public String getIndexAsociado() {
        return indexAsociado;
    }

    public void setIndexAsociado(String indexAsociado) {
        this.indexAsociado = indexAsociado;
    }

    public String getConfirmacionClaveEmpresa() {
        return confirmacionClaveEmpresa;
    }

    public void setConfirmacionClaveEmpresa(String confirmacionClaveEmpresa) {
        this.confirmacionClaveEmpresa = confirmacionClaveEmpresa;
    }

    public String getDireccionSecuencial() {
        return direccionSecuencial;
    }

    public void setDireccionSecuencial(String direccionSecuencial) {
        this.direccionSecuencial = direccionSecuencial;
    }

    public String getPuntoEmision() {
        return puntoEmision;
    }

    public void setPuntoEmision(String puntoEmision) {
        this.puntoEmision = puntoEmision;
    }

    public String getCodigoEstablecimiento() {
        return codigoEstablecimiento;
    }

    public void setCodigoEstablecimiento(String codigoEstablecimiento) {
        this.codigoEstablecimiento = codigoEstablecimiento;
    }

    public boolean isAgregarSecuencial() {
        return agregarSecuencial;
    }

    public void setAgregarSecuencial(boolean agregarSecuencial) {
        this.agregarSecuencial = agregarSecuencial;
    }

}
