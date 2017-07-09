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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean
@ViewScoped
public class BeanAdministracionUsuarios implements Serializable {

    private String idBusqueda;
    private List<UsuarioAcceso> usuariosInternos;
    private String estadoUsuarioInterno;
    private LazyDataModel<UsuarioAcceso> listaUsuariosLazy;
    private UsuarioAcceso usuarioSeleccionado;
    private List<UsuarioAcceso> listaUsuarios;
    private String identificacionEdicion;
    private String nombreUsuarioEdicion;
    private String nombreEdicion;
    private String apellidoEdicion;
    private String telefonoPrincipalEdicion;
    private String telefonoAdicionalEdicion;
    private String correoPrincipalEdicion;
    private String correoAdicionalEdicion;
    private List<Perfil> listaperfiles;
    private boolean estadoActualUsuario;
    private String identificacion;
    private String nombreUsuario;
    private String nombres;
    private String apellidos;
    private String telefonoPrincipal;
    private String telefonoAdicional;
    private String correoPrincipal;
    private String correoAdicional;
    private Integer perfilSelecionado;

    public BeanAdministracionUsuarios() {
        guardarLogRegistros("Acceso al modulo Administracion Usuario");
        CargaUsuariosInternosAsignados();
       // cargarDatatableConUsuarios();
    }
  public void onRowSelect(SelectEvent event) {

    }

    public void onRowUnselect(UnselectEvent event) {

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

    public void validarSeleccion() {
        if (usuarioSeleccionado != null) {
            RequestContext.getCurrentInstance().execute(" PF('detalleUsuario').show();");

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla para visualizar .");
        }
    }

    public List<String> autocompletarUsuarios(String query) {
        List<String> usuarios_internos = new ArrayList<String>();
        for (int i = 0; i < this.listaUsuarios.size(); i++) {
            UsuarioAcceso usuario = this.listaUsuarios.get(i);
            if (usuario.getIdentificacionUsuario().toLowerCase().startsWith(query)) {
                usuarios_internos.add(usuario.getIdentificacionUsuario());
            }
        }
        return usuarios_internos;
    }

    public List<String> autocompletarUsuariosNombres(String query) {
        List<String> usuarios_internos = new ArrayList<String>();
        for (int i = 0; i < this.listaUsuarios.size(); i++) {
            UsuarioAcceso usuario = this.listaUsuarios.get(i);
            if (usuario.getNombreUsuario().toLowerCase().startsWith(query)) {
                usuarios_internos.add(usuario.getNombreUsuario());
            }
        }
        return usuarios_internos;
    }

    private void cargarDatatableConUsuarios() {
       ControlSesion  cs = new ControlSesion();
        try {
            if (cs.obtenerEstadoSesionUsuario() == true) {
                usuariosInternos = this.instanciarDAO().obtenerUsuario();
              
            }
        } catch (Exception e) {
            Logger.getLogger(BeanAdministracionClientes.class.getName()).log(Level.SEVERE, "Error al cargar los clientes", e);
        }
    }

    public void CargaUsuariosInternosAsignados() {
        try {

            listaUsuariosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion sesion = new ControlSesion();
                    listaUsuarios = new ArrayList<UsuarioAcceso>();
                    try {
                        String identificacion = "";
                        if (idBusqueda != null && !idBusqueda.trim().equals("")) {
                            identificacion = idBusqueda.substring(0, idBusqueda.lastIndexOf('-'));
                        }
                        DAOUsuarioAcceso usuario = new DAOUsuarioAcceso();
                        estadoUsuarioInterno = "-1";
                        listaUsuarios = usuario.buscarUsuariosInternos(estadoUsuarioInterno, first, pageSize);

                        this.setRowCount(usuario.obtenerTotalUsuariosInternosPorCriteria(estadoUsuarioInterno).intValue());

                    } catch (Exception e) {

                    }
                    return listaUsuarios;
                }

                @Override
                public void setRowIndex(int rowIndex
                ) {
                    if (rowIndex == -1 || getPageSize() == 0) {
                        super.setRowIndex(-1);
                    } else {
                        super.setRowIndex(rowIndex % getPageSize());
                    }
                }

                public Object getRowKey(UsuarioAcceso ce) {
                    return ce != null ? ce.getIdUsuario() : null;
                }

                @Override
                public UsuarioAcceso getRowData(String rowKey) {
                    List<UsuarioAcceso> laue = (List<UsuarioAcceso>) getWrappedData();
                    Integer value = Integer.valueOf(rowKey);

                    for (UsuarioAcceso aue : laue) {
                        if (aue.getIdUsuario().equals(value)) {
                            return aue;
                        }
                    }
                    return null;
                }
            };
        } catch (Exception ex) {

        }
    }

    public void buscarUsuariosInternosAsignados() {
        try {

            listaUsuariosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion sesion = new ControlSesion();
                    listaUsuarios = new ArrayList<UsuarioAcceso>();
                    try {
                        String identificacion = "";
//                        if (idBusqueda != null && !idBusqueda.trim().equals("")) {
//                            identificacion = idBusqueda.substring(0, idBusqueda.lastIndexOf('-'));
//                        }
                        DAOUsuarioAcceso usuario = new DAOUsuarioAcceso();

                        listaUsuarios = usuario.buscarUsuariosInternosVariosParametros(estadoUsuarioInterno, idBusqueda, nombres, first, pageSize);

                        this.setRowCount(usuario.obtenerTotalUsuariosInternosPorCriteriaComprobantesVariosParametros(estadoUsuarioInterno, idBusqueda, nombres).intValue());

                    } catch (Exception e) {

                    }
                    return listaUsuarios;
                }

                @Override
                public void setRowIndex(int rowIndex
                ) {
                    if (rowIndex == -1 || getPageSize() == 0) {
                        super.setRowIndex(-1);
                    } else {
                        super.setRowIndex(rowIndex % getPageSize());
                    }
                }

                public Object getRowKey(UsuarioAcceso ce) {
                    return ce != null ? ce.getIdUsuario() : null;
                }

                @Override
                public UsuarioAcceso getRowData(String rowKey) {
                    List<UsuarioAcceso> laue = (List<UsuarioAcceso>) getWrappedData();
                    Integer value = Integer.valueOf(rowKey);

                    for (UsuarioAcceso aue : laue) {
                        if (aue.getIdUsuario().equals(value)) {
                            return aue;
                        }
                    }
                    return null;
                }
            };
        } catch (Exception ex) {

        }
    }

    public void mostrarDialogEditar() {
        if (usuarioSeleccionado != null) {
            cargarInformacionEdicionUsuario();
            RequestContext.getCurrentInstance().execute("PF('editarUsuario').show();");
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla para editar .");
        }
    }

    public void mostrarDialogPerfil() {
        if (usuarioSeleccionado != null) {
            System.out.println(usuarioSeleccionado.getPerfil().getNombrePerfil());
            try {
                DAOPerfil daoperfiles = new DAOPerfil();

                listaperfiles = daoperfiles.obtenerPerfilesParaAsignacion();
                for (int i = 0; i < listaperfiles.size(); i++) {
                    if (listaperfiles.get(i).getNombrePerfil().equals(usuarioSeleccionado.getPerfil().getNombrePerfil())) {
                        Perfil per = listaperfiles.get(0);
                        listaperfiles.set(i, per);
                        listaperfiles.set(0, usuarioSeleccionado.getPerfil());
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

    private void cargarInformacionEdicionUsuario() {
        if (usuarioSeleccionado != null) {
            setIdentificacionEdicion(usuarioSeleccionado.getIdentificacionUsuario());
            setNombreUsuarioEdicion(usuarioSeleccionado.getNombreUsuarioAcceso());
            setNombreEdicion(usuarioSeleccionado.getNombreUsuario());
            setApellidoEdicion(usuarioSeleccionado.getApellidoUsuario());
            setTelefonoPrincipalEdicion(usuarioSeleccionado.getTelefonoPrincipalUsuario());
            setTelefonoAdicionalEdicion(usuarioSeleccionado.getTelefonoAdicionalUsuario());
            setCorreoPrincipalEdicion(usuarioSeleccionado.getCorreoPrincipalUsuario());
            setCorreoAdicionalEdicion(usuarioSeleccionado.getCorreoAdicionalUsuario());
            if (usuarioSeleccionado.getEstadoUsuario().equals("1")) {
                setEstadoActualUsuario(Boolean.TRUE);
            } else {
                setEstadoActualUsuario(Boolean.FALSE);
            }

        }
    }

    public void mostrarDialogNuevo() {
        borrarInputextNuevoUsuario();
        RequestContext.getCurrentInstance().execute("PF('nuevoUsuario').show();");
        RequestContext.getCurrentInstance().execute("PF('editarUsuario').hide();");
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

    private boolean validarApellidos(String _apellidos) {
        boolean validado = false;
        if (_apellidos != null && !_apellidos.equals("")) {
            validado = true;
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el apellido.");
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

    private void obtenerAsignacionUsuarioPorId() {
        try {
            DAOUsuarioAcceso dao = new DAOUsuarioAcceso();
            usuarioSeleccionado = dao.obtenerUsuarioAccesoPorId(usuarioSeleccionado.getIdUsuario());

        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void validarEdicionNombre() {
        if (validarNombres(nombreEdicion)) {
            boolean actualizado = this.instanciarDAO().actualizarNombreUsuario(usuarioSeleccionado.getIdUsuario(), nombreEdicion);
            if (actualizado) {
               
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Nombre actualizado.");
                 cargarDatatableConUsuarios();
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Nombre no actualizado.");
            }
        } else {
//            obtenerAsignacionUsuarioPorId();
            nombreEdicion = usuarioSeleccionado.getNombreUsuario();
        }
    }

    public void validarEdicionTelefonoPrincipal() {
        if (validarTelefonoPrincipal(telefonoPrincipalEdicion)) {
            boolean actualizado = this.instanciarDAO().actualizarTelefonoPrincipalUsuario(usuarioSeleccionado.getIdUsuario(), telefonoPrincipalEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Teléfono principal actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Teléfono principal no actualizado.");
            }
        } else {
            obtenerAsignacionUsuarioPorId();
            telefonoPrincipalEdicion = usuarioSeleccionado.getTelefonoPrincipalUsuario();
        }
    }

    public void validarEdicionApellido() {
        if (validarApellidos(apellidoEdicion)) {
            boolean actualizado = this.instanciarDAO().actualizarApellidoUsuario(usuarioSeleccionado.getIdUsuario(), apellidoEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Apellido actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Apellido no actualizado.");
            }
        } else {
            obtenerAsignacionUsuarioPorId();
            apellidoEdicion = usuarioSeleccionado.getApellidoUsuario();
        }
    }

    public void validarEdicionTelefonoAdicional() {
        if (validarTelefonoAdicional(telefonoAdicionalEdicion)) {
            if (telefonoAdicionalEdicion == null || telefonoAdicionalEdicion.equals("")) {
                telefonoAdicionalEdicion = "N/A";
            }
            boolean actualizado = this.instanciarDAO().actualizarTelefonoAdicionalUsuario(usuarioSeleccionado.getIdUsuario(), telefonoAdicionalEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Teléfono adicional actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Teléfono adicional no actualizado.");
            }
        } else {
            obtenerAsignacionUsuarioPorId();
            telefonoAdicionalEdicion = usuarioSeleccionado.getTelefonoAdicionalUsuario();
        }
    }

    public void validarEdicionNombreUsuario() {
        if (validarNombreUsuario(nombreUsuarioEdicion)) {
            boolean actualizado = this.instanciarDAO().actualizarNombreUsuarioAcceso(usuarioSeleccionado.getIdUsuario(), nombreUsuarioEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Nombre de usuario actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Nombre de usuario no actualizado.");
            }
        } else {
            obtenerAsignacionUsuarioPorId();
            nombreUsuarioEdicion = usuarioSeleccionado.getNombreUsuarioAcceso();
        }
    }

    private DAOUsuarioAcceso instanciarDAO() {
        DAOUsuarioAcceso dao_usuario_acceso = null;
        try {
            dao_usuario_acceso = new DAOUsuarioAcceso();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_usuario_acceso;
    }

    private boolean validarNombreUsuario(String _nombreUsuario) {
        boolean validado = false;
        if (_nombreUsuario != null && !_nombreUsuario.equals("")) {
            try {
                if (!_nombreUsuario.equals(usuarioSeleccionado.getNombreUsuarioAcceso())) {
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

    public void validarEdicionCorreoPrincipal() {
        if (validarEmailPrincipal(correoPrincipalEdicion)) {
            boolean actualizado = this.instanciarDAO().actualizarCorreoPrincipalUsuario(usuarioSeleccionado.getIdUsuario(), correoPrincipalEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Correo principal actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Correo principal no actualizado.");
            }
        } else {
            obtenerAsignacionUsuarioPorId();
            correoPrincipalEdicion = usuarioSeleccionado.getCorreoPrincipalUsuario();
        }
    }

    public void validarEdicionCorreoAdicional() {
        if (validarEmailPrincipal(correoAdicionalEdicion)) {
            boolean actualizado = this.instanciarDAO().actualizarCorreoAdicionalUsuario(usuarioSeleccionado.getIdUsuario(), correoAdicionalEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Correo adicional actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Correo adicional no actualizado.");
            }
        } else {
            obtenerAsignacionUsuarioPorId();
            correoAdicionalEdicion = usuarioSeleccionado.getCorreoPrincipalUsuario();
        }
    }

    public void validarActualizacionEstadoUsuario() {
        String estado = "";
        if (estadoActualUsuario) {
            estado = "1";
        } else {
            estado = "2";
        }
        boolean actualizado = this.instanciarDAO().actualizarEstadoUsuario(usuarioSeleccionado.getIdUsuario(), estado);
        if (actualizado) {
            guardarLogRegistros("Cambio en estado de usuario");
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Estado actual del usuario actualizado.");
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Estado actual del usuario no actualizado.");
        }

    }

    public void deSeleccionarTabla() {
        RequestContext.getCurrentInstance().execute("PF('detalleUsuario').hide();");
        RequestContext.getCurrentInstance().execute("PF('wv-usuarios-internos').unselectAllRows();");
//        usuarioSeleccionado = null;
    }

    public void actualizarPerfil() {
        try {
            DAOUsuarioAcceso dao_usuarios = new DAOUsuarioAcceso();
            DAOPerfil daoperfiles = new DAOPerfil();

            boolean actualizado = dao_usuarios.actualizarPerfilUsuario(usuarioSeleccionado.getIdUsuario(), daoperfiles.obtenerPerfilPorId(perfilSelecionado));
            if (actualizado) {
                guardarLogRegistros("Cambio en perfil de usuario");
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Perfil actualizado.");
            } else {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Perfil no actualizado.");
            }
            buscarUsuariosInternosAsignados();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void borrarBusqueda() {
        idBusqueda = null;
        nombres = null;
    }

    public String getIdBusqueda() {
        return idBusqueda;
    }

    public void setIdBusqueda(String idBusqueda) {
        this.idBusqueda = idBusqueda;
    }

    public List<UsuarioAcceso> getUsuariosInternos() {
        return usuariosInternos;
    }

    public void setUsuariosInternos(List<UsuarioAcceso> usuariosInternos) {
        this.usuariosInternos = usuariosInternos;
    }

    public String getEstadoUsuarioInterno() {
        return estadoUsuarioInterno;
    }

    public void setEstadoUsuarioInterno(String estadoUsuarioInterno) {
        this.estadoUsuarioInterno = estadoUsuarioInterno;
    }

    public LazyDataModel<UsuarioAcceso> getListaUsuariosLazy() {
        return listaUsuariosLazy;
    }

    public void setListaUsuariosLazy(LazyDataModel<UsuarioAcceso> listaUsuariosLazy) {
        this.listaUsuariosLazy = listaUsuariosLazy;
    }

    public UsuarioAcceso getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(UsuarioAcceso usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public List<UsuarioAcceso> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<UsuarioAcceso> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
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

    public boolean isEstadoActualUsuario() {
        return estadoActualUsuario;
    }

    public void setEstadoActualUsuario(boolean estadoActualUsuario) {
        this.estadoActualUsuario = estadoActualUsuario;
    }

    public List<Perfil> getListaperfiles() {
        return listaperfiles;
    }

    public void setListaperfiles(List<Perfil> listaperfiles) {
        this.listaperfiles = listaperfiles;
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

    public Integer getPerfilSelecionado() {
        return perfilSelecionado;
    }

    public void setPerfilSelecionado(Integer perfilSelecionado) {
        this.perfilSelecionado = perfilSelecionado;
    }

}
