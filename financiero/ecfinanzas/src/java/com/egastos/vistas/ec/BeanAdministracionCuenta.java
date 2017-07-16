/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.vistas.ec;



import com.egastos.utilidades.Validaciones;
import com.egastos.utilidades.AES256;

import com.egastos.utilidades.ControlSesion;
import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.MensajesPrimefaces;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


@ManagedBean
@ViewScoped
public class BeanAdministracionCuenta implements Serializable{


    private UsuarioAcceso usuarioAcceso;
    private String nombre;
    private String apellido;
    private String correoPrincipal;
    private String correoAdicional;
    private String telefonoPrincipal;
    private String telefonoAdicional;
    private String contraseniaActual;
    private String contraseniaNueva;
    private String confirmaNuevaContrasenia;
    private String contraseniaGuardada;
    private boolean tipoUsuarioClienteEmpresa;
    private boolean tipoUsuarioInterno;
    private String identificacionUsuario;
    private String nombreUsuario;
    private String nombreUsuarioClienteEmpresa;
//    private String claveUsuarioClienteEmpresa;
    private String nombreComercialClienteEmpresa;
    private String razonSocialClienteEmpresa;
    private String direccionClienteEmpresa;
    private String correoPrincipalClienteEmpresa;
    private String telefonoPrincipalClienteEmpresa;
    private boolean obligadoContabilidadClienteEmpresa;
    private String numeroResolucionClienteEmpresa;
    private String rucEmpresa;

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public String getNombreUsuarioClienteEmpresa() {
        return nombreUsuarioClienteEmpresa;
    }

    public void setNombreUsuarioClienteEmpresa(String nombreUsuarioClienteEmpresa) {
        this.nombreUsuarioClienteEmpresa = nombreUsuarioClienteEmpresa;
    }

//    public String getClaveUsuarioClienteEmpresa() {
//        return claveUsuarioClienteEmpresa;
//    }
//
//    public void setClaveUsuarioClienteEmpresa(String claveUsuarioClienteEmpresa) {
//        this.claveUsuarioClienteEmpresa = claveUsuarioClienteEmpresa;
//    }

    public String getNombreComercialClienteEmpresa() {
        return nombreComercialClienteEmpresa;
    }

    public void setNombreComercialClienteEmpresa(String nombreComercialClienteEmpresa) {
        this.nombreComercialClienteEmpresa = nombreComercialClienteEmpresa;
    }

    public String getRazonSocialClienteEmpresa() {
        return razonSocialClienteEmpresa;
    }

    public void setRazonSocialClienteEmpresa(String razonSocialClienteEmpresa) {
        this.razonSocialClienteEmpresa = razonSocialClienteEmpresa;
    }

    public String getDireccionClienteEmpresa() {
        return direccionClienteEmpresa;
    }

    public void setDireccionClienteEmpresa(String direccionClienteEmpresa) {
        this.direccionClienteEmpresa = direccionClienteEmpresa;
    }

    public String getCorreoPrincipalClienteEmpresa() {
        return correoPrincipalClienteEmpresa;
    }

    public void setCorreoPrincipalClienteEmpresa(String correoPrincipalClienteEmpresa) {
        this.correoPrincipalClienteEmpresa = correoPrincipalClienteEmpresa;
    }

    public String getTelefonoPrincipalClienteEmpresa() {
        return telefonoPrincipalClienteEmpresa;
    }

    public void setTelefonoPrincipalClienteEmpresa(String telefonoPrincipalClienteEmpresa) {
        this.telefonoPrincipalClienteEmpresa = telefonoPrincipalClienteEmpresa;
    }

    public boolean isObligadoContabilidadClienteEmpresa() {
        return obligadoContabilidadClienteEmpresa;
    }

    public void setObligadoContabilidadClienteEmpresa(boolean obligadoContabilidadClienteEmpresa) {
        this.obligadoContabilidadClienteEmpresa = obligadoContabilidadClienteEmpresa;
    }

    public String getNumeroResolucionClienteEmpresa() {
        return numeroResolucionClienteEmpresa;
    }

    public void setNumeroResolucionClienteEmpresa(String numeroResolucionClienteEmpresa) {
        this.numeroResolucionClienteEmpresa = numeroResolucionClienteEmpresa;
    }

    public boolean isTipoUsuarioClienteEmpresa() {
        return tipoUsuarioClienteEmpresa;
    }

    public final void setTipoUsuarioClienteEmpresa(boolean tipoUsuarioClienteEmpresa) {
        this.tipoUsuarioClienteEmpresa = tipoUsuarioClienteEmpresa;
    }

    public boolean isTipoUsuarioInterno() {
        return tipoUsuarioInterno;
    }

    public final void setTipoUsuarioInterno(boolean tipoUsuarioInterno) {
        this.tipoUsuarioInterno = tipoUsuarioInterno;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getIdentificacionUsuario() {
        return identificacionUsuario;
    }

    public void setIdentificacionUsuario(String identificacionUsuario) {
        this.identificacionUsuario = identificacionUsuario;
    }

    public String getContraseniaGuardada() {
        return contraseniaGuardada;
    }

    public void setContraseniaGuardada(String contraseniaGuardada) {
        this.contraseniaGuardada = contraseniaGuardada;
    }

    public String getContraseniaActual() {
        return contraseniaActual;
    }

    public void setContraseniaActual(String contraseniaActual) {
        this.contraseniaActual = contraseniaActual;
    }

    public String getContraseniaNueva() {
        return contraseniaNueva;
    }

    public void setContraseniaNueva(String contraseniaNueva) {
        this.contraseniaNueva = contraseniaNueva;
    }

    public String getConfirmaNuevaContrasenia() {
        return confirmaNuevaContrasenia;
    }

    public void setConfirmaNuevaContrasenia(String confirmaNuevaContrasenia) {
        this.confirmaNuevaContrasenia = confirmaNuevaContrasenia;
    }

    public String getTelefonoAdicional() {
        return telefonoAdicional;
    }

    public void setTelefonoAdicional(String telefonoAdicional) {
        this.telefonoAdicional = telefonoAdicional;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
    }

    public String getCorreoAdicional() {
        return correoAdicional;
    }

    public void setCorreoAdicional(String correoAdicional) {
        this.correoAdicional = correoAdicional;
    }

    public String getCorreoPrincipal() {
        return correoPrincipal;
    }

    public void setCorreoPrincipal(String correoPrincipal) {
        this.correoPrincipal = correoPrincipal;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UsuarioAcceso getUsuarioAcceso() {
        return usuarioAcceso;
    }

    public void setUsuarioAcceso(UsuarioAcceso usuarioAcceso) {
        this.usuarioAcceso = usuarioAcceso;
    }


    /**
     * Creates a new instance of BeanAdministracionCuenta
     */
    public BeanAdministracionCuenta() {
        try {
            ControlSesion ms = new ControlSesion();
            if (ms.obtenerEstadoSesionUsuario()) {
                if (ms.obtenerTipoUsuario().equals("1")) {
                    setTipoUsuarioInterno(true);
                    setTipoUsuarioClienteEmpresa(false);
                    usuarioAcceso = this.instanciarDAO().obtenerUsuarioAccesoPorId(Integer.parseInt(ms.obtenerIdUsuarioSesionActiva()));
                    contraseniaGuardada = usuarioAcceso.getClaveUsuarioAcceso();
                    identificacionUsuario = usuarioAcceso.getIdentificacionUsuario();
                    nombreUsuario = usuarioAcceso.getNombreUsuarioAcceso();
                    nombre = usuarioAcceso.getNombreUsuario();
                    apellido = usuarioAcceso.getApellidoUsuario();
                    telefonoPrincipal = usuarioAcceso.getTelefonoPrincipalUsuario();
                    if (usuarioAcceso.getTelefonoAdicionalUsuario() != null && !usuarioAcceso.getTelefonoAdicionalUsuario().equals("")) {
                        telefonoAdicional = usuarioAcceso.getTelefonoAdicionalUsuario();
                    } else {
                        telefonoAdicional = "N/A";
                    }
                    correoPrincipal = usuarioAcceso.getCorreoPrincipalUsuario();

                    if (usuarioAcceso.getCorreoAdicionalUsuario() != null && !usuarioAcceso.getCorreoAdicionalUsuario().equals("")) {
                        correoAdicional = usuarioAcceso.getCorreoAdicionalUsuario();
                    } else {
                        correoAdicional = "N/A";
                    }
                } else if (ms.obtenerTipoUsuario().equals("2")) {
                    setTipoUsuarioInterno(false);
                    setTipoUsuarioClienteEmpresa(true);
                   
                }

            }else{
                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
        } catch (Exception e) {
            Logger.getLogger(BeanAdministracionCuenta.class.getName()).log(Level.SEVERE, "Error al cargar la información del usuario", e);
        }
    }
    
    public void validarNombre() {

        if (nombre != null && !nombre.equals("")) {
            if (!Validaciones.isNum(nombre)) {
                boolean actualizado = this.instanciarDAO().actualizarNombreUsuario(usuarioAcceso.getIdUsuario(), nombre);
                if (actualizado) {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su nombre ha sido actualizado.");

                } else {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su nombre.");
                }
            } else {
                MensajesPrimefaces.  mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El nombre no puede contener números.");
                nombre = usuarioAcceso.getNombreUsuario();
            }
        } else {
              MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El nombre no puede estar vacío.");
            nombre = usuarioAcceso.getNombreUsuario();
        }

    }
    
    private DAOUsuarioAcceso instanciarDAO() {
        DAOUsuarioAcceso dao_usuario = null;
        try {
            dao_usuario = new DAOUsuarioAcceso();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_usuario;

    }
    
    public void validarApellido() {

        if (apellido != null && !apellido.equals("")) {
            if (!Validaciones.isNum(apellido)) {
                boolean actualizado = this.instanciarDAO().actualizarApellidoUsuario(usuarioAcceso.getIdUsuario(), apellido);
                if (actualizado) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su apellido ha sido actualizado.");

                } else {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su apellido.");
                }
            } else {
                 MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El apellido no puede contener números.");
                apellido = usuarioAcceso.getApellidoUsuario();
            }
        } else {
              MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El apellido no puede estar vacío.");
            apellido = usuarioAcceso.getApellidoUsuario();
        }

    }
    
    public void validarCorreoPrincipal() {

        if (correoPrincipal != null && !correoPrincipal.equals("")) {
            if (Validaciones.isEmail(correoPrincipal)) {
                boolean actualizado = this.instanciarDAO().actualizarCorreoPrincipalUsuario(usuarioAcceso.getIdUsuario(), correoPrincipal);
                if (actualizado) {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su correo principal ha sido actualizado.");

                } else {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su correo principal.");
                }
            } else {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El correo ingresado no es válido.");
                correoPrincipal = usuarioAcceso.getCorreoPrincipalUsuario();
            }
        } else {
             MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_INFO, "El correo no puede estar vacío.");
            correoPrincipal = usuarioAcceso.getCorreoPrincipalUsuario();
        }

    }
    
    public void validarCorreoAdicional() {

        if (correoAdicional != null && !correoAdicional.equals("")) {
            if (Validaciones.isEmail(correoAdicional)) {
                boolean actualizado = this.instanciarDAO().actualizarCorreoAdicionalUsuario(usuarioAcceso.getIdUsuario(), correoAdicional);
                if (actualizado) {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su correo adicional ha sido actualizado.");

                } else {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su correo adicional.");
                }
            } else {
                 MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El correo ingresado no es válido.");
                if (usuarioAcceso.getCorreoAdicionalUsuario() != null) {
                    correoAdicional = usuarioAcceso.getCorreoAdicionalUsuario();
                } else {
                    correoAdicional = "N/A";
                }
            }
        } else {
            if (usuarioAcceso.getCorreoAdicionalUsuario() != null) {
                correoAdicional = usuarioAcceso.getCorreoAdicionalUsuario();
            } else {
                correoAdicional = "N/A";
            }
        }

    }
    
    public void validarTelefonoPrincipal() {

        if (telefonoPrincipal != null && !telefonoPrincipal.equals("")) {
            if (Validaciones.isNum(telefonoPrincipal)) {
                boolean actualizado = this.instanciarDAO().actualizarTelefonoPrincipalUsuario(usuarioAcceso.getIdUsuario(), telefonoPrincipal);
                if (actualizado) {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su teléfono principal ha sido actualizado.");

                } else {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su teléfono principal.");
                }
            } else {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El teléfono no puede contener letras.");
                telefonoPrincipal = usuarioAcceso.getTelefonoPrincipalUsuario();
            }
        } else {
             MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El teléfono no puede estar vacío.");
            telefonoPrincipal = usuarioAcceso.getTelefonoPrincipalUsuario();
        }

    }
    
    public void validarTelefonoAdicional() {

        if (telefonoAdicional != null && !telefonoAdicional.equals("")) {
            if (Validaciones.isNum(telefonoAdicional)) {
                boolean actualizado = this.instanciarDAO().actualizarTelefonoAdicionalUsuario(usuarioAcceso.getIdUsuario(), telefonoAdicional);
                if (actualizado) {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su teléfono adicional ha sido actualizado.");

                } else {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su teléfono adicional.");
                }
            } else {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El teléfono no puede contener letras.");
                if (usuarioAcceso.getTelefonoAdicionalUsuario() != null) {
                    telefonoAdicional = usuarioAcceso.getTelefonoAdicionalUsuario();
                } else {
                    telefonoAdicional = "N/A";
                }
            }
        } else {
            if (usuarioAcceso.getTelefonoAdicionalUsuario() != null) {
                telefonoAdicional = usuarioAcceso.getTelefonoAdicionalUsuario();
            } else {
                telefonoAdicional = "N/A";
            }
        }

    }
    public void validarCambioClave() {
        ControlSesion cs = new ControlSesion();
        try {
            if (contraseniaActual == null || contraseniaActual.equals("") || contraseniaNueva == null || contraseniaNueva.equals("") || confirmaNuevaContrasenia == null || confirmaNuevaContrasenia.equals("")) {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Son obligatorios todos los campos.");
            } else {

                if (!contraseniaGuardada.equals(AES256.toAES256(contraseniaActual))) {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La contraseña actual no es correcta.");
                } else {
                    if (contraseniaNueva.length() < 8 || confirmaNuevaContrasenia.length() < 8) {
                          MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La contraseña debe tener un mínimo de 8 caracteres.");
                    } else {
                        if (!contraseniaNueva.equals(confirmaNuevaContrasenia)) {
                              MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Las contraseñas no coinciden.");
                        } else {
                            if (AES256.toAES256(contraseniaNueva).equals(contraseniaGuardada)) {
                                 MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No puede ingresar la misma contraseña.");
                            } else {
                                try {
                                    boolean actualizada = false;
                                    if (cs.obtenerTipoUsuario().equals("1")) {
                                        actualizada = this.instanciarDAO().actualizarClaveUsuarioAcceso(usuarioAcceso.getIdUsuario(), contraseniaNueva);
                                    } 
                                    if (actualizada) {
                                         MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su contraseña se ha actualizado correctamente.");
                                    } else {
                                         MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su contraseña.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
