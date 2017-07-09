/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Validaciones;
import ec.discheca.configuracion.AES256;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOClienteEmpresa;
import ec.discheca.dao.DAOPerfil;
import ec.discheca.dao.DAOSecuencial;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Empresa;
import ec.discheca.modelo.Perfil;
import ec.discheca.modelo.Secuencial;
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
    private Empresa empresaNueva;
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
        this.empresaNueva = new Empresa();
        this.empresaNueva.setObligadoContabilidadClienteEmpresa(false);

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
        } else if (this.usuarioNuevo.getTelefonoAdicionalUsuario() != null && !this.usuarioNuevo.getTelefonoAdicionalUsuario().trim().equals("")) {
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
        DAOClienteEmpresa dao_cliente_empresa = null;
        DAOUsuarioAcceso dao_usuario_acceso = null;
        try {
            dao_cliente_empresa = new DAOClienteEmpresa();
            dao_usuario_acceso = new DAOUsuarioAcceso();

            if (identificacion != null && !identificacion.equals("")) {

                if (Validaciones.isNum(identificacion)) {
                    if (identificacion.length() == 10) {
                        if (Validaciones.validarCedula(identificacion)) {

                            Empresa cliente_empresa1 = null;
                            Empresa cliente_empresa2 = null;
                            UsuarioAcceso ua1 = null;
                            UsuarioAcceso ua2 = null;
                            try {
                                cliente_empresa1 = dao_cliente_empresa.obtenerClienteEmpresaPorId(identificacion + "001");
                                cliente_empresa2 = dao_cliente_empresa.obtenerClienteEmpresaPorId(identificacion);
                                ua1 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion);
                                ua2 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion + "001");
                            } catch (Exception ex) {
                                Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (cliente_empresa1 == null && cliente_empresa2 == null && ua1 == null && ua2 == null) {
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
                           Empresa cliente_empresa1 = null;
                            Empresa cliente_empresa2 = null;
                            UsuarioAcceso ua1 = null;
                            UsuarioAcceso ua2 = null;
                            try {
                                cliente_empresa1 = dao_cliente_empresa.obtenerClienteEmpresaPorId(identificacion);
                                cliente_empresa2 = dao_cliente_empresa.obtenerClienteEmpresaPorId(identificacion.substring(0, 10));
                                ua1 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion);
                                ua2 = dao_usuario_acceso.obtenerUsuarioPorRucCedula(identificacion.substring(0, 10));
                            } catch (Exception ex) {
                                Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (cliente_empresa1 == null && cliente_empresa2 == null && ua1 == null && ua2 == null) {
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
                    Perfil perfilusado = daoperfiles.obtenerPerfilPorNombre("Usuario Externo");
                    usuarioNuevo.setClaveUsuarioAcceso(AES256.toAES256(usuarioNuevo.getClaveUsuarioAcceso()));
                    usuarioNuevo.setNombreUsuarioAcceso(usuarioNuevo.getIdentificacionUsuario());
                    usuarioNuevo.setFechaRegistroUsuario(new Date());
                    usuarioNuevo.setEstadoUsuario("1");
                    //Asignar perfil
                    usuarioNuevo.setPerfil(perfilusado);
                    usuarioRegistrado = daousuario.insertarUsuarioAcceso(usuarioNuevo);
                    //Enviar Correo
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El registro se ha completado con exito.");
                   //Util.enviarCorreoRegistro(this.usuarioNuevo.getCorreoPrincipalUsuario());
                    if (!this.usuarioNuevo.getCorreoAdicionalUsuario().equals("")) {
//                        Util.enviarCorreoRegistro(this.usuarioNuevo.getCorreoAdicionalUsuario());
                    }
                    vaciarCamposUsuario();
                    RequestContext.getCurrentInstance().update("pgRegistro");
                    RequestContext.getCurrentInstance().update("pgRegistroEmpresa");
                } catch (Exception ex) {
                    Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    public void registrarEmpresa() {

        if (validarDatosEmpresa()) {
            try {
                DAOPerfil daoperfiles = new DAOPerfil();
                DAOClienteEmpresa daoempresa = new DAOClienteEmpresa();
                //Se comenta hasta saber a que empresa se debe asignar, por el momento se asigna una empresa prueba
                Perfil perfilusado = daoperfiles.obtenerPerfilPorNombre("externo");
                empresaNueva.setClaveUsuarioClienteEmpresa(AES256.toAES256(this.empresaNueva.getClaveUsuarioClienteEmpresa()));
                empresaNueva.setNombreUsuarioClienteEmpresa(this.empresaNueva.getIdEmpresa());
                empresaNueva.setFechaRegistroClienteEmpresa(new Date());
                empresaNueva.setEstadoClienteEmpresa(true);
                //Asignar perfil
             
                if (daoempresa.insertarCliente(this.empresaNueva)) {
                    if (!this.direccionSecuencial.trim().equals("")) {
                        DAOSecuencial daoSecuencial = new DAOSecuencial();
                        Secuencial secuencial = new Secuencial(this.empresaNueva, "1", "1", "1", "1", "1", this.puntoEmision, this.codigoEstablecimiento, this.direccionSecuencial, true, Valores.AMBIENTE);
                        if (!daoSecuencial.insertarSecuencial(secuencial)) {
                             MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se ha podido insertar el secuencial. Intente insertando una vez que haya inciado sesión.");
                        }
                    }
                    //Enviar Correo
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El registro se ha completado con exito.");
//                        Util.enviarCorreoRegistro(this.empresaNueva.getCorreoPrincipalClienteEmpresa());
                    vaciarCamposEmpresa();
                    RequestContext.getCurrentInstance().update("pgRegistro");
                    RequestContext.getCurrentInstance().update("pgRegistroEmpresa");
                } else {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se ha insertado la empresa.");
                }

            } catch (Exception ex) {
                Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void vaciarCamposUsuario() {
        usuarioNuevo = new UsuarioAcceso();
        this.confirmacionClave = "";
    }

    public void vaciarCamposEmpresa() {
        empresaNueva = new Empresa();
        this.confirmacionClaveEmpresa = "";
        this.puntoEmision = "";
        this.codigoEstablecimiento = "";
        this.direccionSecuencial = "";
    }

    public boolean validarDatosEmpresa() {
        boolean respuesta = true;
        if (this.empresaNueva.getIdEmpresa() == null || this.empresaNueva.getIdEmpresa().trim().equals("")) {
             MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar el número de RUC de la Empresa.");
            respuesta = false;
        } else if (this.empresaNueva.getIdEmpresa().trim().length() != 13) {
             MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El RUC de la empresa debe tener 13 dígitos.");
            respuesta = false;
        } else if (validarIdentificacion(this.empresaNueva.getIdEmpresa())) {
            if (this.empresaNueva.getRazonSocialClienteEmpresa() == null || this.empresaNueva.getRazonSocialClienteEmpresa().trim().equals("")) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar la Razón Social de la Empresa.");
                respuesta = false;
            } else if (!Validaciones.isTexto(this.empresaNueva.getRazonSocialClienteEmpresa())) {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar solo letras en el campo Razón Social.");
                respuesta = false;
            } else if (this.empresaNueva.getNombreComercialClienteEmpresa() == null || this.empresaNueva.getNombreComercialClienteEmpresa().trim().equals("")) {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar el Nombre Comercial de la Empresa.");
                respuesta = false;
            } else if (this.empresaNueva.getDireccionClienteEmpresa() == null || this.empresaNueva.getDireccionClienteEmpresa().trim().equals("")) {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar la Dirección de la Empresa.");
                respuesta = false;
            } else if (this.empresaNueva.getCorreoPrincipalClienteEmpresa() == null || this.empresaNueva.getCorreoPrincipalClienteEmpresa().trim().equals("")) {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo eléctrónico de la Empresa.");
                respuesta = false;
            } else if (!Validaciones.isEmail(this.empresaNueva.getCorreoPrincipalClienteEmpresa())) {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo electrónico válido.");
                respuesta = false;
            } else if (this.empresaNueva.getTelefonoPrincipalClienteEmpresa() == null || this.empresaNueva.getTelefonoPrincipalClienteEmpresa().trim().equals("")) {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un número de teléfono de la Empresa.");
                respuesta = false;
            } else if (!Validaciones.isNum(this.empresaNueva.getTelefonoPrincipalClienteEmpresa())) {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar solo números en el campo Teléfono.");
                respuesta = false;
            } else if (!this.direccionSecuencial.trim().equals("") || !this.puntoEmision.trim().equals("") || !this.codigoEstablecimiento.trim().equals("")) {
                if (this.direccionSecuencial.trim().equals("")) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar la Dirección de Establecimiento.");
                    respuesta = false;
                } else if (this.puntoEmision.trim().equals("")) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar el Punto de Emisión.");
                    respuesta = false;
                } else if (!Validaciones.isNum(this.puntoEmision.trim()) || this.puntoEmision.trim().length() < 3) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un Punto de Emisión de 3 Dígidtos. Ejemplo: 001.");
                    respuesta = false;
                } else if (this.codigoEstablecimiento.trim().equals("")) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar el Código de Establecimiento.");
                    respuesta = false;
                } else if (!Validaciones.isNum(this.codigoEstablecimiento.trim()) || this.codigoEstablecimiento.trim().length() < 3) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar un Código de Establecimiento de 3 Dígidtos. Ejemplo: 002.");
                    respuesta = false;
                }
            }
            if (respuesta) {
                if (this.empresaNueva.getClaveUsuarioClienteEmpresa() == null || this.empresaNueva.getClaveUsuarioClienteEmpresa().trim().equals("")) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe ingresar una contraseña.");
                    respuesta = false;
                } else if (this.empresaNueva.getClaveUsuarioClienteEmpresa().trim().length() < 8) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La contraseña ingresada debe tener mínimo 8 caracteres.");
                    respuesta = false;
                } else if (!this.empresaNueva.getClaveUsuarioClienteEmpresa().equals(this.confirmacionClaveEmpresa)) {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La contraseña ingresada no coincide con la confirmación de la misma.");
                    respuesta = false;
                }
            }
        } else {
            respuesta = false;
        }
        return respuesta;
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

    public Empresa getEmpresaNueva() {
        return empresaNueva;
    }

    public void setEmpresaNueva(Empresa empresaNueva) {
        this.empresaNueva = empresaNueva;
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
