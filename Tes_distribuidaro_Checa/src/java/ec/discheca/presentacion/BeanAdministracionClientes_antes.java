/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Validaciones;
import ec.discheca.configuracion.AES256;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.correo.Correo;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOClientes;
import ec.discheca.dao.DAOPerfil;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Perfil;
import ec.discheca.modelo.Receptor;
import ec.discheca.modelo.UsuarioAcceso;
import static ec.discheca.presentacion.BeanRecuperarClave.enviarCorreoRestablecerClave;
import ec.discheca.utilidades.MensajesPrimefaces;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
 * @Ricardo Delgado
 */
@ManagedBean
@ViewScoped
public class BeanAdministracionClientes_antes implements Serializable {

    /**
     * Creates a new instance of BeanAdmininistracionCliente
     */
    private List<Receptor> clientes;

    private Receptor clienteselec;

    private String info_empresa;

    private Receptor nuevoCliente;

    private Receptor clienteEdicion;

    private String ruc;
    private String nombre;
    private String telefono;
    private String direccion;
    private String correo;
    private String correoAdicional;
    private Integer tipoIdentificacion;
    private ControlSesion cs;
    private String estadobusqueda;
    private String cedulaBusqueda;
    private boolean estadoActualUsuario;

    public void borrarBusqueda() {
        cedulaBusqueda = null;
        nombre = null;
    }
//rebisar  esta dando fallas

    public BeanAdministracionClientes_antes() {
        cs = new ControlSesion();
        guardarLogRegistros("Acceso al modulo Administracion Clientes");
        tipoIdentificacion = 5;
        Logger.getLogger(BeanAdministracionClientes_antes.class.getName()).log(Level.INFO, "El usuario: " + cs.obtenerNombreUsuarioSesionActiva() + " ha entrado a administración de clientes.");
        cargarDatatableConCliente();
        this.nuevoCliente = new Receptor();
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

    /**
     *
     * Método que carga el datatable con los clientes por establecimiento y
     * punto de emisión que tiene la empresa que ha iniciado sesión
     */
    public void busquedaDeClientes() {
        try {
            DAOClientes clien = new DAOClientes();
            if (cedulaBusqueda != null) {
                clientes = clien.buscarclientesInternosVarios(estadobusqueda, cedulaBusqueda, nombre);
            } else {
                cargarDatatableConCliente();
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes_antes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargarDatatableConCliente() {
        try {
            if (cs.obtenerEstadoSesionUsuario() == true) {
                clientes = this.instanciarDAO().obtenerTodosCliente();
                if (clientes != null && !clientes.isEmpty()) {
                    info_empresa = Valores.VALOR_RUC_EMISOR;
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BeanAdministracionClientes_antes.class.getName()).log(Level.SEVERE, "Error al cargar los clientes", e);
        }
    }

    private void obtenerClientePorId() {
        try {
            clienteselec = this.instanciarDAO().obtenerClientePorId(clienteselec.getIdReceptor());
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes_antes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *  /**
     * Método que instancia el DAOCliente con la sesión de Hibernate.
     *
     * @return Objeto de tipo DAOCliente
     *
     */
    private DAOClientes instanciarDAO() {
        DAOClientes dao_clientes = null;
        try {
            dao_clientes = new DAOClientes();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionClientes_antes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_clientes;
    }

    public void cargarEstadoActualCliente() {
        if (clienteselec != null) {
            clienteEdicion = new Receptor();
            clienteEdicion.setRucReceptor(clienteselec.getRucReceptor());
            clienteEdicion.setRazonSocialReceptor(clienteselec.getRazonSocialReceptor());
            clienteEdicion.setTelefono(clienteselec.getTelefono());
            clienteEdicion.setCorreo(clienteselec.getCorreo());
            clienteEdicion.setCorreoAdicional(clienteselec.getCorreoAdicional());
            RequestContext.getCurrentInstance().execute("PF('wv-editar-cliente').show();");
        }
    }

    public boolean validarDatosCliente() {
        return validarIdentificacionCliente(nuevoCliente.getRucReceptor()) && validarNombreCliente(nuevoCliente.getRazonSocialReceptor())
                && validarTelefonoCliente(nuevoCliente.getTelefono()) && validarDireccionCliente(nuevoCliente.getDireccion())
                && validarCorreoCliente(nuevoCliente.getCorreo(), "principal") && validarCorreoCliente(nuevoCliente.getCorreoAdicional(), "adicional");
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

    public boolean validarDireccionCliente(String _direccion) {
        boolean respuesta = false;
        if (_direccion != null && !_direccion.trim().equals("")) {
            respuesta = true;
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar la Dirección del cliente.");
        }
        return respuesta;
    }

    public boolean validarTelefonoCliente(String _telefono) {
        boolean respuesta = false;
        if (_telefono != null && !_telefono.trim().equals("")) {
            if (Validaciones.isNum(_telefono)) {
                respuesta = true;
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar solo números en el campo teléfono.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el número de teléfono del cliente.");
        }
        return respuesta;
    }

    public boolean validarCorreoCliente(String _correo, String _tipo) {
        boolean respuesta = false;
        if (_correo != null && !_correo.trim().equals("")) {
            if (!Validaciones.isEmail(_correo)) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo " + _tipo + " válido.");
            } else {
                respuesta = true;
            }
        } else {
            if (_tipo.equals("principal")) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un correo para el cliente.");
            } else {
                respuesta = true;
            }
        }
        return respuesta;
    }

    public boolean validarIdentificacionCliente(String _identificacion) {
        boolean respuesta = true;
        if (!tipoIdentificacion.equals(4) && !tipoIdentificacion.equals(5) && !tipoIdentificacion.equals(6)) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar el tipo de identificación del cliente.");
            respuesta = false;
        } else if (_identificacion == null || _identificacion.trim().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar la identificación del cliente.");
            respuesta = false;
        } else {
            respuesta = validarIdentificacion(_identificacion);
        }
        return respuesta;
    }

    public void borrarParametros() {
        this.ruc = null;
        this.nombre = null;
        this.telefono = null;
        this.direccion = null;
        this.correo = null;
        this.correoAdicional = null;
    }

    public List<String> autocompletarUsuariosruc(String query) {
        List<String> usuarios_internos = new ArrayList<String>();
        for (int i = 0; i < this.clientes.size(); i++) {
            Receptor usuario = this.clientes.get(i);
            if (usuario.getRucReceptor().toLowerCase().startsWith(query)) {
                usuarios_internos.add(usuario.getRucReceptor());
            }
        }
        return usuarios_internos;
    }

    public List<String> autocompletarUsuariosNombres(String query) {
        List<String> usuarios_internos = new ArrayList<String>();
        for (int i = 0; i < this.clientes.size(); i++) {
            Receptor usuario = this.clientes.get(i);
            if (usuario.getRazonSocialReceptor().toLowerCase().startsWith(query)) {
                usuarios_internos.add(usuario.getRazonSocialReceptor());
            }
        }
        return usuarios_internos;
    }

    public void cerrarDatos() {
        RequestContext.getCurrentInstance().execute("PF('wv-nuevo-cliente').hide()");
    }

    public void asignarTipoIdentificacionParaEdicion() {
        /**
         * Método para llamar al tipo de idnetificación para la edición, puesto
         * que el componente inputplace que contiene la varibale de la
         * identficación no verifia primero si se cambia de tipo de
         * identificación al modificar
         */
    }

    public void insertarCliente() {
        nuevoCliente = new Receptor();
        nuevoCliente.setRucReceptor(this.ruc);
        nuevoCliente.setRazonSocialReceptor(this.nombre);
        nuevoCliente.setTelefono(this.telefono);
        nuevoCliente.setDireccion(this.direccion);
        nuevoCliente.setCorreo(this.correo);
        nuevoCliente.setCorreoAdicional(this.correoAdicional);
        nuevoCliente.setEstado("1");
        if (validarDatosCliente()) {
            try {
                Receptor clientGuardado = this.instanciarDAO().insertarClienteUsuario(this.nuevoCliente);
                if (clientGuardado != null) {
                    DAOPerfil daoperfiles = new DAOPerfil();
                    UsuarioAcceso usuario_interno = new UsuarioAcceso();
                    Perfil perfilUsuario = daoperfiles.obtenerPerfilPorCodigo("Usuario Externo");
                    usuario_interno.setIdentificacionUsuario(clientGuardado.getRucReceptor());
                    usuario_interno.setNombreUsuarioAcceso(clientGuardado.getRucReceptor());
                    if (perfilUsuario != null) {
                        usuario_interno.setPerfil(perfilUsuario);
                    } else {
                        System.out.println("El usuario no tiene perfil.");
                    }
                    String clave = AES256.toAES256(clientGuardado.getRucReceptor());
                    usuario_interno.setClaveUsuarioAcceso(clave);
                    usuario_interno.setNombreUsuario(clientGuardado.getRazonSocialReceptor());
                    usuario_interno.setApellidoUsuario(clientGuardado.getRazonSocialReceptor());
                    usuario_interno.setTelefonoPrincipalUsuario(clientGuardado.getTelefono());
                    usuario_interno.setTelefonoAdicionalUsuario("N/A");

                    usuario_interno.setCorreoPrincipalUsuario(clientGuardado.getCorreo());
                    if (clientGuardado.getCorreoAdicional() != null && !clientGuardado.getCorreoAdicional().equals("")) {
                        usuario_interno.setCorreoAdicionalUsuario(clientGuardado.getCorreoAdicional());
                    } else {
                        usuario_interno.setCorreoAdicionalUsuario("N/A");
                    }
                    usuario_interno.setEstadoUsuario("3");
                    usuario_interno.setFechaRegistroUsuario(new Date());
                    DAOUsuarioAcceso dao_usuarios = new DAOUsuarioAcceso();
                    UsuarioAcceso guardado_usuario = dao_usuarios.insertarUsuarioAcceso(usuario_interno);
                    if (guardado_usuario != null) {
                        enviarCorreoRestablecerClave(guardado_usuario);
                        cargarDatatableConCliente();
                        guardarLogRegistros("nuevo cliente registrado");
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Se ha agregado el nuevo cliente.");
                        this.clientes.add(this.nuevoCliente);
                        cerrarDialogoNuevo();
                        Logger.getLogger(BeanAdministracionClientes_antes.class.getName()).log(Level.INFO, "El usuario: " + cs.obtenerNombreUsuarioSesionActiva() + " ha registrado al cliente: " + this.nuevoCliente.getRucReceptor() + ".");
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el nuevo cliente.");
                    }

                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el nuevo cliente.");
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanAdministracionClientes_antes.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public static boolean enviarCorreoRestablecerClave(UsuarioAcceso _usuario) {
        boolean respuesta = false;
        try {
            List<String> contenido = new ArrayList<String>();
            List<String> imagenes = new ArrayList<String>();
            String url;
            Correo correo = new Correo("1");
            url = "Para seguir con el proceso de restablecer clave haga <br/><br/>Si el link no funciona por favor copie la siguiente URL en la barra de navegación :<br/>";

            /*Se agrega el nombre de ususario y url para el contenido de la plantilla*/
            contenido.add(_usuario.getNombreUsuarioAcceso());
            contenido.add(_usuario.getIdentificacionUsuario());

            /*Se agregan las imagenes para colocarlas en la plantilla*/
            imagenes.add("cab");
            imagenes.add("pie");

            /*Se realiza el envio de correo*/
            if (_usuario.getCorreoPrincipalUsuario() != null && !_usuario.getCorreoPrincipalUsuario().equals("")) {
                respuesta = correo.enviarMailReguistroCheca(_usuario.getCorreoPrincipalUsuario(), contenido, Valores.VALOR_PLANTILLA_HTML_CORREO + File.separator + "registroUsuario.html", Valores.VALOR_PLANTILLA_IMAGENES_CORREO + File.separator, "Bienvenido Al Portal DisCheca", imagenes);
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanRecuperarClave.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public void editarCliente() {
        if (clienteselec != null) {
            setRuc(clienteselec.getRucReceptor());
            setNombre(clienteselec.getRazonSocialReceptor());
            if (clienteselec.getTelefono() != null && !clienteselec.getTelefono().equals("")) {
                setTelefono(clienteselec.getTelefono());

            } else {
                setTelefono("N/A");
            }
            if (clienteselec.getDireccion() != null && !clienteselec.getDireccion().equals("")) {
                setDireccion(clienteselec.getDireccion());
            } else {
                setDireccion("N/A");
            }
            if (clienteselec.getCorreo() != null && !clienteselec.getCorreo().equals("")) {
                setCorreo(clienteselec.getCorreo());
            } else {
                setCorreo("N/A");
            }

            if (clienteselec.getCorreoAdicional() != null && !clienteselec.getCorreoAdicional().equals("")) {
                setCorreoAdicional(clienteselec.getCorreoAdicional());
            } else {
                setCorreoAdicional("N/A");
            }
            if (this.ruc.length() == 13 && (Validaciones.validarRucPersonaNatural(this.ruc) || Validaciones.validarRucSociedadPrivada(this.ruc) || Validaciones.validarRucSociedadPublica(this.ruc))) {
                tipoIdentificacion = 4;
            } else if (this.ruc.length() == 10 && Validaciones.validarCedula(this.ruc)) {
                tipoIdentificacion = 5;
            } else {
                tipoIdentificacion = 6;
            }
            if (clienteselec.getEstado().equals("1")) {
                setEstadoActualUsuario(Boolean.TRUE);
            } else {
                setEstadoActualUsuario(Boolean.FALSE);
            }

            abrirDialogoEditar();
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla.");
        }
    }

    public void actualizarCliente(Integer _opcion) {
        String estado = "";
        Receptor clienteActualizado = new Receptor();
        boolean actualizar = false;
        clienteActualizado.setRucReceptor(clienteselec.getRucReceptor());
        clienteActualizado.setRazonSocialReceptor(clienteselec.getRazonSocialReceptor());
        clienteActualizado.setTelefono(clienteselec.getTelefono());
        clienteActualizado.setDireccion(clienteselec.getDireccion());
        clienteActualizado.setCorreo(clienteselec.getCorreo());
        clienteActualizado.setCorreoAdicional(clienteselec.getCorreoAdicional());
        switch (_opcion) {
            case 1:
                actualizar = validarIdentificacionCliente(this.ruc);
                if (actualizar) {
                    clienteActualizado.setRucReceptor(this.ruc);
                }
                break;
            case 2:
                actualizar = validarNombreCliente(this.nombre);
                if (actualizar) {
                    clienteActualizado.setRazonSocialReceptor(this.nombre);
                }
                break;
            case 3:
                actualizar = validarTelefonoCliente(this.telefono);
                if (actualizar) {
                    clienteActualizado.setTelefono(this.telefono);
                }
                break;
            case 4:
                actualizar = validarDireccionCliente(this.direccion);
                if (actualizar) {
                    clienteActualizado.setDireccion(this.direccion);
                }
                break;
            case 5:
                actualizar = validarCorreoCliente(this.correo, "principal");
                if (actualizar) {
                    clienteActualizado.setCorreo(this.correo);
                }
                break;
            case 6:
                actualizar = validarCorreoCliente(this.correoAdicional, "adicional");
                if (actualizar) {
                    clienteActualizado.setCorreoAdicional(this.correoAdicional);
                }
                break;
            case 7:

                if (estadoActualUsuario) {
                    estado = "1";
                     clienteActualizado.setEstado(estado);
                    actualizar = true;
                    guardarLogRegistros("Modificacion estado cliente");
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "estado actualizado correctamente");
                } else {
                    estado = "2";
                    clienteActualizado.setEstado(estado);
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "estado actualizado correctamente");
                    actualizar = true;
                }

                

                break;
            default:

                actualizar = validarIdentificacionCliente(this.ruc) && validarNombreCliente(this.nombre)
                        && validarDireccionCliente(this.direccion) && validarTelefonoCliente(this.telefono)
                        && validarCorreoCliente(this.correo, "principal")
                        && validarCorreoCliente(this.correoAdicional, "adicional");
                if (actualizar) {
                    clienteActualizado.setRucReceptor(this.ruc);
                    clienteActualizado.setRazonSocialReceptor(this.nombre);
                    clienteActualizado.setDireccion(this.direccion);
                    clienteActualizado.setTelefono(this.telefono);
                    clienteActualizado.setCorreo(this.correo);
                    clienteActualizado.setCorreoAdicional(this.correoAdicional);
                    clienteActualizado.setEstado(estado);
                }
                break;
        }
        if (actualizar) {
            if (estadoActualUsuario) {
                estado = "1";
                clienteActualizado.setEstado(estado);
                actualizar = true;
                guardarLogRegistros("Modificacion estado cliente");
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "estado actualizado correctamente");
            } else {
                estado = "2";
                clienteActualizado.setEstado(estado);
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "estado actualizado correctamente");
//                    actualizar = true;
            }
            Receptor actualizado = this.instanciarDAO().actualizarCliente(clienteselec.getIdReceptor(), clienteActualizado);
            if (actualizado != null && actualizado.getIdReceptor() != null) {
//                clienteselec.setRucReceptor(actualizado.getRucReceptor());
                clienteselec.setRucReceptor(actualizado.getRucReceptor());
                clienteselec.setRazonSocialReceptor(actualizado.getRazonSocialReceptor());
                clienteselec.setTelefono(actualizado.getTelefono());
                clienteselec.setDireccion(actualizado.getDireccion());
                clienteselec.setCorreo(actualizado.getCorreo());
                clienteselec.setCorreoAdicional(actualizado.getCorreoAdicional());
                clienteselec.setEstado(estado);
                guardarLogRegistros("Actualizacion datos cliente");
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Cliente actualizado.");
                Logger.getLogger(BeanAdministracionClientes_antes.class.getName()).log(Level.INFO, "El usuario: " + cs.obtenerNombreUsuarioSesionActiva() + " ha modificado al cliente: " + this.clienteselec.getRucReceptor() + ".");
                cargarDatatableConCliente();
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "No se pudo actualizar el cliente");
            }
        }
    }

    public void abrirDialogoEditar() {
        RequestContext.getCurrentInstance().execute("PF('wv-editar-cliente').show();");
    }

    public void cerrarDialogoEditar() {
        RequestContext.getCurrentInstance().execute("PF('wv-editar-cliente').hide();");
    }

    public void abrirDialogoNuevo() {
        this.nuevoCliente = new Receptor();
        tipoIdentificacion = 5;
        borrarParametros();
        RequestContext.getCurrentInstance().execute("PF('wv-nuevo-cliente').show();");
    }

    public void cerrarDialogoNuevo() {
        RequestContext.getCurrentInstance().execute("PF('wv-nuevo-cliente').hide();");
    }

    private boolean validarIdentificacion(String _identificacion) {
        boolean validado = false;
        DAOClientes dao_clientes = null;
        try {
            dao_clientes = new DAOClientes();

            if (_identificacion != null && !_identificacion.trim().equals("")) {
                if (tipoIdentificacion.equals(6)) {
                    validado = true;
                } else if (Validaciones.isNum(_identificacion)) {
                    if (_identificacion.length() <= 13) {
                        if (tipoIdentificacion.equals(5)) {
                            if (_identificacion.length() == 10 && Validaciones.validarCedula(_identificacion)) {
                                validado = true;
                            } else {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El número de cédula no es válido.");
                            }
                        } else if (tipoIdentificacion.equals(4)) {
                            if (!Validaciones.validarRucPersonaNatural(_identificacion)) {
                                if (!Validaciones.validarRucSociedadPrivada(_identificacion)) {
                                    if (!Validaciones.validarRucSociedadPublica(_identificacion)) {
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
                        }
                        if (tipoIdentificacion.equals(6)) {
                            validado = true;
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación no debe exceder de 13 números dígitos.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación debe ser un número.");
                }

            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación Cédula/RUC/Pasaporte no puede estar vacía.");
            }
            if (validado) {
                Receptor clienteTmp = new Receptor();
                try {
                    clienteTmp = dao_clientes.obtenerClientePorIdentificacionYRucEmpresa(_identificacion);
                } catch (Exception ex) {
                    Logger.getLogger(BeanRegistro.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (clienteTmp != null) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La identificación ya se encuentra registrada por otro cliente de la empresa.");
                    validado = false;
                } else {
                    validado = true;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanRegistro.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return validado;
    }

    public void onRowSelect(SelectEvent event) {

    }

    public void onRowUnselect(UnselectEvent event) {

    }

    public List<Receptor> getCliente() {
        return clientes;
    }

    public void setCliente(List<Receptor> clientes) {
        this.clientes = clientes;
    }

    public String getInfo_empresa() {
        return info_empresa;
    }

    public void setInfo_empresa(String info_empresa) {
        this.info_empresa = info_empresa;
    }

    public Receptor getNuevoCliente() {
        return nuevoCliente;
    }

    public void setNuevoCliente(Receptor nuevoCliente) {
        this.nuevoCliente = nuevoCliente;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCorreoAdicional() {
        return correoAdicional;
    }

    public void setCorreoAdicional(String correoAdicional) {
        this.correoAdicional = correoAdicional;
    }

    public List<Receptor> getClientes() {
        return clientes;
    }

    public void setClientes(List<Receptor> clientes) {
        this.clientes = clientes;
    }

    public Receptor getClienteEdicion() {
        return clienteEdicion;
    }

    public void setClienteEdicion(Receptor clienteEdicion) {
        this.clienteEdicion = clienteEdicion;
    }

    public Integer getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(Integer tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getEstadobusqueda() {
        return estadobusqueda;
    }

    public void setEstadobusqueda(String estadobusqueda) {
        this.estadobusqueda = estadobusqueda;
    }

    public String getCedulaBusqueda() {
        return cedulaBusqueda;
    }

    public void setCedulaBusqueda(String cedulaBusqueda) {
        this.cedulaBusqueda = cedulaBusqueda;
    }

    public boolean isEstadoActualUsuario() {
        return estadoActualUsuario;
    }

    public void setEstadoActualUsuario(boolean estadoActualUsuario) {
        this.estadoActualUsuario = estadoActualUsuario;
    }

    public Receptor getClienteselec() {
        return clienteselec;
    }

    public void setClienteselec(Receptor clienteselec) {
        this.clienteselec = clienteselec;
    }

}
