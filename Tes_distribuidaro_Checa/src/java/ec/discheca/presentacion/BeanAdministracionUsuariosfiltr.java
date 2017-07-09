/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Validaciones;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOClienteEmpresa;
import ec.discheca.dao.DAOPerfil;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Empresa;
import ec.discheca.modelo.Perfil;
import ec.discheca.modelo.UsuarioAcceso;
import ec.discheca.utilidades.AES256;
import ec.discheca.utilidades.MensajesPrimefaces;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Usuario
 */
@ManagedBean
@ViewScoped
public class BeanAdministracionUsuariosfiltr implements Serializable {

    private ControlSesion cs;
    private List<UsuarioAcceso> usuarios;
    private UsuarioAcceso nuevoUsuario;
    private String info_empresa;
    private UsuarioAcceso usuarioselec;
    private String nombre;
    private boolean estadoActualUsuario;
    private List<Perfil> listaperfiles;
    private Integer perfilSelecionado;
    private String identificacion;
    private String nombreUsuario;
    private String nombres;
    private String apellidos;
    private String telefonoPrincipal;
    private String telefonoAdicional;
    private String correoPrincipal;
    private String correoAdicional;
    private String identificacionEdicion;
    private String nombreUsuarioEdicion;
    private String nombreEdicion;
    private String apellidoEdicion;
    private String telefonoPrincipalEdicion;
    private String telefonoAdicionalEdicion;
    private String correoPrincipalEdicion;
    private String correoAdicionalEdicion;

    public BeanAdministracionUsuariosfiltr() {
        cs = new ControlSesion();
        guardarLogRegistros("Acceso al modulo Administracion Clientes");

        Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.INFO, "El usuario: " + cs.obtenerNombreUsuarioSesionActiva() + " ha entrado a administración de clientes.");
        cargarDatatableConCliente();
        this.nuevoUsuario = new UsuarioAcceso();
    }

    public void guardarLogRegistros(String mensaje) {
        try {
            ControlSesion sesion = new ControlSesion();
            DAOUsuarioAcceso daoacceso = new DAOUsuarioAcceso();
            DAOAuditoria auditoria = new DAOAuditoria();
            Auditoria insertAudi = new Auditoria();
            insertAudi.setFecha(new Date());
            insertAudi.setFechaHora(new Date());
            insertAudi.setMensajeTransaccion(mensaje);
            insertAudi.setUsuarioAcceso(daoacceso.obtenerUsuarioAccesoPorId(Integer.parseInt(sesion.obtenerIdUsuarioSesionActiva())));
            auditoria.insertarRegistro(insertAudi);
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarDatatableConCliente() {
        try {
            if (cs.obtenerEstadoSesionUsuario() == true) {
                usuarios = this.instanciarDAO().obtenerUsuario();
                if (usuarios != null && !usuarios.isEmpty()) {
                    for (int i = 0; i < usuarios.size(); i++) {
                        usuarios.get(i).getPerfil().getNombrePerfil();
                    }
                    info_empresa = Valores.VALOR_RUC_EMISOR;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.SEVERE, "Error al cargar los clientes", e);
        }
    }

    /**
     *  /**
     * Método que instancia el DAOCliente con la sesión de Hibernate.
     *
     * @return Objeto de tipo DAOCliente
     *
     */
    private DAOUsuarioAcceso instanciarDAO() {
        DAOUsuarioAcceso dao_usuario = null;
        try {
            dao_usuario = new DAOUsuarioAcceso();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_usuario;
    }

    private boolean validarNombreUsuarioNuevo(String _nombreUsuario) {
        boolean validado = false;
        if (_nombreUsuario != null && !_nombreUsuario.equals("")) {
            try {

                DAOUsuarioAcceso dao_usuario_acceso = new DAOUsuarioAcceso();
                UsuarioAcceso ua = dao_usuario_acceso.obtenerUsuarioAccesoPorNombre(_nombreUsuario);
                if (ua != null) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El nombre del usuario ya existe.");
                } else {
                    validado = true;
                }

            } catch (Exception ex) {
                Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El nombre del usuario no puede estar vacío.");
        }
        return validado;
    }

    private boolean validarNombreUsuario(String _nombreUsuario) {
        boolean validado = false;
        if (_nombreUsuario != null && !_nombreUsuario.equals("")) {
            try {
                if (!_nombreUsuario.equals(usuarioselec.getNombreUsuarioAcceso())) {
                    DAOUsuarioAcceso dao_usuario_acceso = new DAOUsuarioAcceso();
                    UsuarioAcceso ua = dao_usuario_acceso.obtenerUsuarioAccesoPorNombre(_nombreUsuario);
                    if (ua != null) {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El nombre del usuario ya existe.");
                    } else {
                        validado = true;
                    }
                } else {
                    validado = true;
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El nombre del usuario no puede estar vacío.");
        }
        return validado;
    }

    private boolean validarTelefonoPrincipal(String _telefono) {
        boolean validado = false;
        if (_telefono != null && !_telefono.equals("")) {
            if (Validaciones.isNum(_telefono)) {
                validado = true;
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El teléfono debe ser un número.");
            }
        } else {

            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el teléfono principal.");
        }
        return validado;
    }

    private boolean validarEmailPrincipal(String _email) {
        boolean validado = false;
        if (_email != null && !_email.equals("")) {
            if (Validaciones.isEmail(_email)) {
                validado = true;
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El email no es válido.");
            }
        } else {

            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el correo principal.");
        }
        return validado;
    }

    private boolean validarTelefonoAdicional(String _telefono) {
        boolean validado = false;
        if (_telefono != null && !_telefono.equals("")) {
            if (Validaciones.isNum(_telefono)) {
                validado = true;
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El teléfono adicional debe ser un número.");
            }
        } else {
            validado = true;
        }
        return validado;
    }

    private boolean validarEmailAdicional(String _email) {
        boolean validado = false;
        if (_email != null && !_email.equals("")) {
            if (Validaciones.isEmail(_email)) {
                validado = true;
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El email adicional no es válido.");
            }
        } else {

            validado = true;
        }
        return validado;
    }

    private boolean validarApellidos(String _apellidos) {
        boolean validado = false;
        if (_apellidos != null && !_apellidos.equals("")) {
            validado = true;
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el apellido.");
        }
        return validado;
    }

    private void borrarInputextNuevoUsuario() {
        identificacion = null;
        nombreUsuario = null;
        nombres = null;
        apellidos = null;
        correoPrincipal = null;
        correoAdicional = null;
        telefonoPrincipal = null;
        telefonoAdicional = null;

    }

    public void mostrarDialogNuevo() {
        borrarInputextNuevoUsuario();
        RequestContext.getCurrentInstance().execute("PF('nuevoUsuario').show();");

    }

    public void guardarNuevoUsuarioInterno() {
        String mensaje_sin_perfi = "";
        if (validarIdentificacion() && validarNombreUsuarioNuevo(nombreUsuario) && validarNombres(nombres) && validarApellidos(apellidos) && validarEmailPrincipal(correoPrincipal)
                && validarEmailAdicional(correoAdicional) && validarTelefonoPrincipal(telefonoPrincipal) && validarTelefonoAdicional(telefonoAdicional)) {
            try {
                DAOPerfil daoperfiles = new DAOPerfil();
                Perfil perfilUsuario = daoperfiles.obtenerPerfilPorCodigo("Usuario Interno");
                ControlSesion sesion = new ControlSesion();
                UsuarioAcceso usuario_interno = new UsuarioAcceso();
                usuario_interno.setIdentificacionUsuario(identificacion);
                usuario_interno.setNombreUsuarioAcceso(nombreUsuario);
                if (perfilUsuario != null) {
                    usuario_interno.setPerfil(perfilUsuario);
                } else {
                    mensaje_sin_perfi = "El usuario no tiene perfil.";
                }
                String clave = AES256.toAES256(nombreUsuario);
                usuario_interno.setClaveUsuarioAcceso(clave);
                usuario_interno.setNombreUsuario(nombres);
                usuario_interno.setApellidoUsuario(apellidos);
                usuario_interno.setTelefonoPrincipalUsuario(telefonoPrincipal);
                if (telefonoAdicional != null && !telefonoAdicional.equals("")) {
                    usuario_interno.setTelefonoAdicionalUsuario(telefonoAdicional);
                } else {
                    usuario_interno.setTelefonoAdicionalUsuario("N/A");
                }
                usuario_interno.setCorreoPrincipalUsuario(correoPrincipal);
                if (correoAdicional != null && !correoAdicional.equals("")) {
                    usuario_interno.setCorreoAdicionalUsuario(correoAdicional);
                } else {
                    usuario_interno.setCorreoAdicionalUsuario("N/A");
                }
                usuario_interno.setEstadoUsuario("1");
                usuario_interno.setFechaRegistroUsuario(new Date());
                DAOUsuarioAcceso dao_usuarios = new DAOUsuarioAcceso();
                UsuarioAcceso guardado_usuario = dao_usuarios.insertarUsuarioAcceso(usuario_interno);
                if (guardado_usuario != null) {
                    guardarLogRegistros("creacion de usuarios");
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Usuario guardado." + mensaje_sin_perfi);
                    cargarDatatableConCliente();
                    RequestContext.getCurrentInstance().execute("PF('nuevoUsuario').hide();");
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El usuario no ha sido guardado.");
                }

            } catch (Exception ex) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al guardar el usuario cliente/empresa.");
//                Logger.getLogger(BeanAdminPerfiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public void actualizarUsuario(Integer _opcion) {
        String estado = "";
        UsuarioAcceso UsuarioActualizado = new UsuarioAcceso();
        boolean actualizar = false;
        UsuarioActualizado = usuarioselec;

        switch (_opcion) {
            case 1:
                actualizar = validarNombreUsuario(nombreUsuarioEdicion);
                if (actualizar) {
                    UsuarioActualizado.setNombreUsuarioAcceso(this.nombreUsuarioEdicion);
                }
                break;
            case 2:
                actualizar = validarNombres(nombreEdicion);
                if (actualizar) {
                    UsuarioActualizado.setNombreUsuario(this.nombreEdicion);
                }
                break;
            case 3:
                actualizar = validarApellidos(apellidoEdicion);
                if (actualizar) {
                    UsuarioActualizado.setApellidoUsuario(this.apellidoEdicion);
                }
                break;
            case 4:
                actualizar = validarTelefonoPrincipal(telefonoPrincipalEdicion);
                if (actualizar) {
                    UsuarioActualizado.setTelefonoPrincipalUsuario(this.telefonoPrincipalEdicion);
                }
                break;
            case 5:
                actualizar = validarTelefonoAdicional(telefonoAdicionalEdicion);
                if (actualizar) {
                    UsuarioActualizado.setTelefonoAdicionalUsuario(this.telefonoAdicionalEdicion);
                }
                break;
            case 6:
                actualizar = validarEmailPrincipal(correoPrincipalEdicion);
                if (actualizar) {
                    UsuarioActualizado.setCorreoPrincipalUsuario(this.correoPrincipalEdicion);
                }
                break;
            case 7:
                actualizar = validarEmailPrincipal(correoAdicionalEdicion);
                if (actualizar) {
                    UsuarioActualizado.setCorreoAdicionalUsuario(this.correoAdicionalEdicion);
                }
                break;
            case 8:

                if (estadoActualUsuario) {
                    estado = "1";
                    UsuarioActualizado.setEstadoUsuario(estado);
                    actualizar = true;
                    guardarLogRegistros("Modificacion estado usuario");
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "estado actualizado correctamente");
                } else {
                    estado = "2";
                    UsuarioActualizado.setEstadoUsuario(estado);
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "estado actualizado correctamente");
                    actualizar = true;
                }

            default:
        }
        UsuarioAcceso actualizado = this.instanciarDAO().actualizarUsuario(usuarioselec.getIdUsuario(), UsuarioActualizado);
        if (actualizado != null) {
            usuarioselec.setNombreUsuarioAcceso(nombreUsuarioEdicion);
            usuarioselec.setNombreUsuario(nombreEdicion);
            usuarioselec.setApellidoUsuario(apellidoEdicion);
            usuarioselec.setTelefonoPrincipalUsuario(telefonoPrincipalEdicion);
            usuarioselec.setTelefonoAdicionalUsuario(telefonoAdicionalEdicion);
            usuarioselec.setCorreoPrincipalUsuario(correoPrincipalEdicion);
            usuarioselec.setCorreoAdicionalUsuario(correoAdicionalEdicion);
            usuarioselec.setEstadoUsuario(estado);
            cargarDatatableConCliente();
        }

    }

    public void validarActualizacionEstadoUsuario() {
        String estado = "";
        if (estadoActualUsuario) {
            estado = "1";
        } else {
            estado = "2";
        }
        boolean actualizado = this.instanciarDAO().actualizarEstadoUsuario(usuarioselec.getIdUsuario(), estado);
        if (actualizado) {
            cargarDatatableConCliente();
            guardarLogRegistros("Cambio en estado de usuario");
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Estado actual del usuario actualizado.");
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Estado actual del usuario no actualizado.");
        }

    }

    public void mostrarDialogPerfil() {
        if (usuarioselec != null) {
//            System.out.println(usuarioselec.getPerfil().getNombrePerfil());
            try {
                DAOPerfil daoperfiles = new DAOPerfil();

                listaperfiles = daoperfiles.obtenerPerfilesParaAsignacion();
                for (int i = 0; i < listaperfiles.size(); i++) {
                    if (listaperfiles.get(i).getNombrePerfil().equals(usuarioselec.getPerfil().getNombrePerfil())) {
                        Perfil per = listaperfiles.get(0);
                        listaperfiles.set(i, per);
                        listaperfiles.set(0, usuarioselec.getPerfil());
                    }

                }
                RequestContext.getCurrentInstance().execute("PF('editarUsuarioPerfil').show();");
            } catch (Exception ex) {
                Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla para editar perfil .");
        }
    }

    public void actualizarPerfil() {
        try {
            DAOUsuarioAcceso dao_usuarios = new DAOUsuarioAcceso();
            DAOPerfil daoperfiles = new DAOPerfil();

            boolean actualizado = dao_usuarios.actualizarPerfilUsuario(usuarioselec.getIdUsuario(), daoperfiles.obtenerPerfilPorId(perfilSelecionado));
            if (actualizado) {
                guardarLogRegistros("Cambio en perfil de usuario");
                usuarioselec.setPerfil(daoperfiles.obtenerPerfilPorId(perfilSelecionado));
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Perfil actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Perfil no actualizado.");
            }
            cargarDatatableConCliente();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarCliente() {
        if (usuarioselec != null) {
            setNombreUsuarioEdicion(usuarioselec.getNombreUsuarioAcceso());
            setNombreEdicion(usuarioselec.getNombreUsuario());
            setApellidoEdicion(usuarioselec.getApellidoUsuario());
            setTelefonoPrincipalEdicion(usuarioselec.getTelefonoPrincipalUsuario());
            setTelefonoAdicionalEdicion(usuarioselec.getTelefonoAdicionalUsuario());
            setCorreoPrincipalEdicion(usuarioselec.getCorreoPrincipalUsuario());
            setCorreoAdicionalEdicion(usuarioselec.getCorreoAdicionalUsuario());
            if (usuarioselec.getEstadoUsuario().equals("1")) {
                setEstadoActualUsuario(Boolean.TRUE);
            } else {
                setEstadoActualUsuario(Boolean.FALSE);
            }
            abrirDialogoEditar();
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla.");
        }
    }

    public void abrirDialogoEditar() {
        RequestContext.getCurrentInstance().execute("PF('wv-editar-cliente').show();");
    }

    private boolean validarIdentificacion() {
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
                                Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (cliente_empresa1 == null && cliente_empresa2 == null && ua1 == null && ua2 == null) {
                                validado = true;
                            } else {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación ya se encuentra registrada por otro usuario.");
                            }
                        } else {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El número de cédula no es válido.");
                        }
                    } else if (identificacion.length() == 13) {
                        if (!Validaciones.validarRucPersonaNatural(identificacion)) {
                            if (!Validaciones.validarRucSociedadPrivada(identificacion)) {
                                if (!Validaciones.validarRucSociedadPublica(identificacion)) {
                                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El RUC no es válido.");
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
                                Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (cliente_empresa1 == null && cliente_empresa2 == null && ua1 == null && ua2 == null) {
                                validado = true;
                            } else {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación ya se encuentra registrada por otro usuario.");
                                validado = false;
                            }
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación ingresada no es Cédula ni RUC.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación debe ser un número.");
                }

            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación Cédula/RUC no puede estar vacía.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return validado;
    }

    private boolean validarNombres(String _nombres) {
        boolean validado = false;
        if (_nombres != null && !_nombres.equals("")) {
            validado = true;
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el nombre.");
        }
        return validado;
    }

    public boolean validarNombreCliente(String _nombre) {
        boolean respuesta = false;
        if (_nombre != null && !_nombre.trim().equals("")) {
            if (Validaciones.isTexto(_nombre)) {
                respuesta = true;
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Solo puede ingresar letras en Razón Social/Nombres.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar la Razón Social/Nombres del cliente.");
        }
        return respuesta;
    }

    public void onRowSelect(SelectEvent event) {

    }

    public void onRowUnselect(UnselectEvent event) {

    }

    public List<UsuarioAcceso> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<UsuarioAcceso> usuarios) {
        this.usuarios = usuarios;
    }

    public UsuarioAcceso getUsuarioselec() {
        return usuarioselec;
    }

    public void setUsuarioselec(UsuarioAcceso usuarioselec) {
        this.usuarioselec = usuarioselec;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Perfil> getListaperfiles() {
        return listaperfiles;
    }

    public void setListaperfiles(List<Perfil> listaperfiles) {
        this.listaperfiles = listaperfiles;
    }

    public Integer getPerfilSelecionado() {
        return perfilSelecionado;
    }

    public void setPerfilSelecionado(Integer perfilSelecionado) {
        this.perfilSelecionado = perfilSelecionado;
    }

    public UsuarioAcceso getNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(UsuarioAcceso nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    public boolean isEstadoActualUsuario() {
        return estadoActualUsuario;
    }

    public void setEstadoActualUsuario(boolean estadoActualUsuario) {
        this.estadoActualUsuario = estadoActualUsuario;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefonoPrincipal() {
        return telefonoPrincipal;
    }

    public void setTelefonoPrincipal(String telefonoPrincipal) {
        this.telefonoPrincipal = telefonoPrincipal;
    }

    public String getTelefonoAdicional() {
        return telefonoAdicional;
    }

    public void setTelefonoAdicional(String telefonoAdicional) {
        this.telefonoAdicional = telefonoAdicional;
    }

    public String getCorreoPrincipal() {
        return correoPrincipal;
    }

    public void setCorreoPrincipal(String correoPrincipal) {
        this.correoPrincipal = correoPrincipal;
    }

    public String getCorreoAdicional() {
        return correoAdicional;
    }

    public void setCorreoAdicional(String correoAdicional) {
        this.correoAdicional = correoAdicional;
    }

    public String getIdentificacionEdicion() {
        return identificacionEdicion;
    }

    public void setIdentificacionEdicion(String identificacionEdicion) {
        this.identificacionEdicion = identificacionEdicion;
    }

    public String getNombreUsuarioEdicion() {
        return nombreUsuarioEdicion;
    }

    public void setNombreUsuarioEdicion(String nombreUsuarioEdicion) {
        this.nombreUsuarioEdicion = nombreUsuarioEdicion;
    }

    public String getNombreEdicion() {
        return nombreEdicion;
    }

    public void setNombreEdicion(String nombreEdicion) {
        this.nombreEdicion = nombreEdicion;
    }

    public String getApellidoEdicion() {
        return apellidoEdicion;
    }

    public void setApellidoEdicion(String apellidoEdicion) {
        this.apellidoEdicion = apellidoEdicion;
    }

    public String getTelefonoPrincipalEdicion() {
        return telefonoPrincipalEdicion;
    }

    public void setTelefonoPrincipalEdicion(String telefonoPrincipalEdicion) {
        this.telefonoPrincipalEdicion = telefonoPrincipalEdicion;
    }

    public String getTelefonoAdicionalEdicion() {
        return telefonoAdicionalEdicion;
    }

    public void setTelefonoAdicionalEdicion(String telefonoAdicionalEdicion) {
        this.telefonoAdicionalEdicion = telefonoAdicionalEdicion;
    }

    public String getCorreoPrincipalEdicion() {
        return correoPrincipalEdicion;
    }

    public void setCorreoPrincipalEdicion(String correoPrincipalEdicion) {
        this.correoPrincipalEdicion = correoPrincipalEdicion;
    }

    public String getCorreoAdicionalEdicion() {
        return correoAdicionalEdicion;
    }

    public void setCorreoAdicionalEdicion(String correoAdicionalEdicion) {
        this.correoAdicionalEdicion = correoAdicionalEdicion;
    }

}
