/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.presentacion;



import discheca.utilidades.Validaciones;
import ec.discheca.configuracion.AES256;

import ec.discheca.configuracion.ControlSesion;
import ec.discheca.dao.DAOClienteEmpresa;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Empresa;
import ec.discheca.modelo.UsuarioAcceso;
import ec.discheca.utilidades.MensajesPrimefaces;
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
    private Empresa clienteEmpresa;
    private String rucEmpresa;

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public Empresa getClienteEmpresa() {
        return clienteEmpresa;
    }

    public void setClienteEmpresa(Empresa clienteEmpresa) {
        this.clienteEmpresa = clienteEmpresa;
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
                    clienteEmpresa = this.instanciarDAOClienteEmpresa().obtenerClienteEmpresaPorId(ms.obtenerRUCEmpresaSesionActiva());
                    rucEmpresa = clienteEmpresa.getIdEmpresa();
                    contraseniaGuardada = clienteEmpresa.getClaveUsuarioClienteEmpresa();
                    identificacionUsuario = clienteEmpresa.getIdEmpresa();
                    nombreUsuarioClienteEmpresa = clienteEmpresa.getNombreUsuarioClienteEmpresa();
                    nombreComercialClienteEmpresa = clienteEmpresa.getNombreComercialClienteEmpresa();
                    razonSocialClienteEmpresa = clienteEmpresa.getRazonSocialClienteEmpresa();
                    telefonoPrincipal = clienteEmpresa.getTelefonoPrincipalClienteEmpresa();
                    direccionClienteEmpresa = clienteEmpresa.getDireccionClienteEmpresa();
                    correoPrincipal = clienteEmpresa.getCorreoPrincipalClienteEmpresa();

                    if (clienteEmpresa.getNumeroResolucionClienteEmpresa() != null && !clienteEmpresa.getNumeroResolucionClienteEmpresa().equals("")) {
                        numeroResolucionClienteEmpresa = clienteEmpresa.getNumeroResolucionClienteEmpresa();
                    } else {
                        numeroResolucionClienteEmpresa = "N/A";
                    }
                    if (clienteEmpresa.getObligadoContabilidadClienteEmpresa() != null) {
                        obligadoContabilidadClienteEmpresa = clienteEmpresa.getObligadoContabilidadClienteEmpresa();
                    } else {
                        obligadoContabilidadClienteEmpresa = false;
                    }
                }

            }
        } catch (Exception e) {
            Logger.getLogger(BeanAdministracionCuenta.class.getName()).log(Level.SEVERE, "Error al cargar la información del usuario", e);
        }
    }
    
    private DAOClienteEmpresa instanciarDAOClienteEmpresa() {
        DAOClienteEmpresa dao_cliente_empresa = null;
        try {
            dao_cliente_empresa = new DAOClienteEmpresa();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionCuenta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_cliente_empresa;

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
    
    public void validarNombreComercialEmpresa() {

        if (nombreComercialClienteEmpresa != null && !nombreComercialClienteEmpresa.equals("")) {

            boolean actualizado = this.instanciarDAOClienteEmpresa().actualizarNombreComercial(clienteEmpresa.getIdEmpresa(), nombreComercialClienteEmpresa);
            if (actualizado) {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El nombre comercial ha sido actualizado.");

            } else {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar el nombre comercial.");
            }

        } else {
              MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El nombre comercial no puede estar vacío.");
            nombreComercialClienteEmpresa = clienteEmpresa.getNombreComercialClienteEmpresa();
        }

    }

    public void validarRazonSocialEmpresa() {

        if (razonSocialClienteEmpresa != null && !razonSocialClienteEmpresa.equals("")) {

            boolean actualizado = this.instanciarDAOClienteEmpresa().actualizarRazonSocial(clienteEmpresa.getIdEmpresa(), razonSocialClienteEmpresa);
            if (actualizado) {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "La razón social ha sido actualizada.");

            } else {
                 MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar la razón social.");
            }

        } else {
              MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "La razón social no puede estar vacía.");
            razonSocialClienteEmpresa = clienteEmpresa.getRazonSocialClienteEmpresa();
        }

    }

    public void validarObligadoContabilidad() {

        boolean actualizado = this.instanciarDAOClienteEmpresa().actualizarObligadoContabilidad(clienteEmpresa.getIdEmpresa(), obligadoContabilidadClienteEmpresa);
        if (actualizado) {
              MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Se ha cambiado el estado de obligado a llevar contabilidad.");

        } else {
              MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar el estado de obligado a llevar contabilidad.");
        }

    }

    public void validarCorreoPrincipalEmpresa() {

        if (correoPrincipalClienteEmpresa != null && !correoPrincipalClienteEmpresa.equals("")) {
            if (Validaciones.isEmail(correoPrincipalClienteEmpresa)) {
                boolean actualizado = this.instanciarDAOClienteEmpresa().actualizarCorreo(clienteEmpresa.getIdEmpresa(), correoPrincipalClienteEmpresa);
                if (actualizado) {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su correo principal ha sido actualizado.");

                } else {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su correo principal.");
                }
            } else {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El correo ingresado no es válido.");
                correoPrincipalClienteEmpresa = clienteEmpresa.getCorreoPrincipalClienteEmpresa();
            }
        } else {
             MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_INFO, "El correo no puede estar vacío.");
            correoPrincipalClienteEmpresa = clienteEmpresa.getCorreoPrincipalClienteEmpresa();
        }

    }

    public void validarTelefonoPrincipalEmpresa() {

        if (telefonoPrincipalClienteEmpresa != null && !telefonoPrincipalClienteEmpresa.equals("")) {
            if (Validaciones.isNum(telefonoPrincipalClienteEmpresa)) {
                boolean actualizado = this.instanciarDAOClienteEmpresa().actualizarTelefono(clienteEmpresa.getIdEmpresa(), telefonoPrincipalClienteEmpresa);
                if (actualizado) {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_INFO, "Su teléfono principal ha sido actualizado.");

                } else {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar su teléfono principal.");
                }
            } else {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El teléfono no puede contener letras.");
                telefonoPrincipalClienteEmpresa = clienteEmpresa.getTelefonoPrincipalClienteEmpresa();
            }
        } else {
             MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El teléfono no puede estar vacío.");
            telefonoPrincipalClienteEmpresa = clienteEmpresa.getTelefonoPrincipalClienteEmpresa();
        }

    }

    public void validarNumeroResolucion() {

        if (numeroResolucionClienteEmpresa != null && !numeroResolucionClienteEmpresa.equals("")) {
            if (Validaciones.isNum(numeroResolucionClienteEmpresa)) {

                boolean actualizado = this.instanciarDAOClienteEmpresa().actualizarNumeroResolucion(clienteEmpresa.getIdEmpresa(), numeroResolucionClienteEmpresa);
                if (actualizado) {
                      MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "El número de resolución ha sido actualizado.");

                } else {
                     MensajesPrimefaces. mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al actualizar el número de resolución.");
                }
            } else {
                  MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El número de resolución no puede contener letras.");
                numeroResolucionClienteEmpresa = clienteEmpresa.getNumeroResolucionClienteEmpresa();
            }
        } else {
            if (clienteEmpresa.getNumeroResolucionClienteEmpresa() != null && !clienteEmpresa.getNumeroResolucionClienteEmpresa().equals("")) {
                numeroResolucionClienteEmpresa = clienteEmpresa.getNumeroResolucionClienteEmpresa();
            } else {
                numeroResolucionClienteEmpresa = "N/A";
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
                                    } else if (cs.obtenerTipoUsuario().equals("2")) {
                                        actualizada = this.instanciarDAOClienteEmpresa().actualizarClaveUsuarioClienteEmpresa(clienteEmpresa.getIdEmpresa(), contraseniaNueva);
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
