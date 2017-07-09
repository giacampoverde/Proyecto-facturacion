/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import ec.discheca.configuracion.ControlSesion;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOPerfil;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Perfil;
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

@ManagedBean
@ViewScoped
public class BeanAdministracionPerfiles implements Serializable {

    private List<Perfil> perfilesActuales;
    private Perfil perfilSeleccionado;
    private String descripcionPerfil;
    private String nombrePerfil;
    private String nombreEdicion;
    private String descripcionEdicion;

    public String getNombreEdicion() {
        return nombreEdicion;
    }

    public void setNombreEdicion(String nombreEdicion) {
        this.nombreEdicion = nombreEdicion;
    }

    public String getDescripcionEdicion() {
        return descripcionEdicion;
    }

    public void setDescripcionEdicion(String descripcionEdicion) {
        this.descripcionEdicion = descripcionEdicion;
    }

    public String getDescripcionPerfil() {
        return descripcionPerfil;
    }

    public void setDescripcionPerfil(String descripcionPerfil) {
        this.descripcionPerfil = descripcionPerfil;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }

    public Perfil getPerfilSeleccionado() {
        return perfilSeleccionado;
    }

    public void setPerfilSeleccionado(Perfil perfilSeleccionado) {
        this.perfilSeleccionado = perfilSeleccionado;
    }

    public List<Perfil> getPerfilesActuales() {
        return perfilesActuales;
    }

    public void setPerfilesActuales(List<Perfil> perfilesActuales) {
        this.perfilesActuales = perfilesActuales;
    }

    /**
     * Creates a new instance of BeanAdministracionPerfiles
     */
    public BeanAdministracionPerfiles() {
        try {

            guardarLogRegistros("Acceso al modulo Administracion Perfil");

            cargarTabla();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionPerfiles.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void onRowSelect(SelectEvent event) {

    }

    public void onRowUnselect(UnselectEvent event) {

    }

    private void cargarTabla() {
        DAOPerfil dao_perfiles = null;
        try {
            dao_perfiles = this.instanciarDAO();
            perfilesActuales = dao_perfiles.obtenerPerfil();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionPerfiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DAOPerfil instanciarDAO() {
        DAOPerfil dao_perfiles = null;
        try {
            dao_perfiles = new DAOPerfil();

        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionPerfiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_perfiles;
    }

    public void mostrarDialogNuevo() {
        reiniciarValoresNuevoPerfil();
        RequestContext.getCurrentInstance().execute("PF('nuevoPerfil').show();");
        RequestContext.getCurrentInstance().execute("PF('editarPerfil').hide();");
        RequestContext.getCurrentInstance().execute("PF('estadoPerfil').hide();");
    }

    private void reiniciarValoresNuevoPerfil() {
        nombrePerfil = null;
        descripcionPerfil = null;
    }

    public void mostrarDialogEditar() {
        if (perfilSeleccionado != null) {
//            identificadorPerfilEdicion=perfilSeleccionado.getCodigoPerfil();
            cargarInformacionEdicionPerfil();
            RequestContext.getCurrentInstance().execute("PF('editarPerfil').show();");
            RequestContext.getCurrentInstance().execute("PF('nuevoPerfil').hide();");
            RequestContext.getCurrentInstance().execute("PF('estadoPerfil').hide();");

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla.");
        }
    }

    private void cargarInformacionEdicionPerfil() {
        if (perfilSeleccionado != null) {
            setNombreEdicion(perfilSeleccionado.getNombrePerfil());
            setDescripcionEdicion(perfilSeleccionado.getDescripcionPerfil());
        }
    }

    public void mostrarDialogEstadoPerfil() {
        if (perfilSeleccionado != null) {
            cargarInformacionEdicionPerfil();
            RequestContext.getCurrentInstance().execute("PF('estadoPerfil').show();");
            RequestContext.getCurrentInstance().execute("PF('nuevoPerfil').hide();");
            RequestContext.getCurrentInstance().execute("PF('editarPerfil').hide();");
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un registro de la tabla.");
        }
    }

    public void guardarNuevoPerfil() {
        if ((nombrePerfil == null || nombrePerfil.equals("")) && (descripcionPerfil == null || descripcionPerfil.equals(""))) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el nombre del perfil y la descripción para registrarlo.");
        } else if (nombrePerfil == null || nombrePerfil.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El nombre del perfil es requerido.");
        } else if (descripcionPerfil == null || descripcionPerfil.equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La descripción es requerida.");
        } else {
            try {
                perfilesActuales = this.instanciarDAO().obtenerPerfil();
                Perfil p = this.instanciarDAO().obtenerPerfilPorNombre(nombrePerfil);
                if (p == null) {
                    boolean guardado = this.instanciarDAO().guardarPerfil(nombrePerfil, descripcionPerfil);
                    if (guardado) {
                        guardarLogRegistros("Creacion de un nuevo Perfil");
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El perfil ha sido guardado.");
                        cargarTabla();
                        RequestContext.getCurrentInstance().execute("PF('nuevoPerfil').hide();");
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El perfil no ha sido guardado.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El perfil ya existe.");
                }

            } catch (Exception ex) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error.");
                Logger.getLogger(BeanAdministracionPerfiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void validarEdicionNombre() {

        if (nombreEdicion != null && !nombreEdicion.equals("")) {
            Perfil p = null;
            try {
                p = this.instanciarDAO().obtenerPerfilPorNombre(nombreEdicion);
            } catch (Exception ex) {
                Logger.getLogger(BeanAdministracionPerfiles.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (p == null) {
                boolean actualizado = this.instanciarDAO().actualizarNombrePerfil(perfilSeleccionado.getIdPerfil(), nombreEdicion);
                if (actualizado) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Nombre del perfil actualizado.");
                    cargarTabla();
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar el nombre del perfil.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El perfil ya existe.");
                nombreEdicion = perfilSeleccionado.getNombrePerfil();
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El nombre del perfil no puede estar vacío.");
            nombreEdicion = perfilSeleccionado.getNombrePerfil();
        }
    }

    public void validarEdicionDescripcionPerfil() {

        if (descripcionEdicion != null && !descripcionEdicion.equals("")) {
            boolean actualizado = this.instanciarDAO().actualizarDescripcionPerfil(perfilSeleccionado.getIdPerfil(), descripcionEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Descripción del perfil actualizado.");
                cargarTabla();
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar la descripción del perfil.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "La descripción del perfil no puede estar vacía.");
            nombreEdicion = perfilSeleccionado.getDescripcionPerfil();
        }

    }

    public void cambiarEstadoPerfil() {
        boolean nuevo_estado = false;
        if (perfilSeleccionado.getEstadoPerfil()) {
            nuevo_estado = false;
        } else {
            nuevo_estado = true;
        }
        boolean actualizado = this.instanciarDAO().cambiarEstadoPerfil(perfilSeleccionado.getIdPerfil(), nuevo_estado);
        if (actualizado) {
            try {
                guardarLogRegistros("Creacion de un nuevo perfil");
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Ha cambiado el estado del perfil.");
                cargarTabla();
                RequestContext.getCurrentInstance().execute("PF('estadoPerfil').hide();");
                quitarSeleccionDatatable();
            } catch (Exception ex) {
                Logger.getLogger(BeanAdministracionPerfiles.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha cambiado el estado del perfil");
        }
    }

    private void quitarSeleccionDatatable() {
        RequestContext.getCurrentInstance().execute("PF('dt-perfiles').unselectAllRows();");
        perfilSeleccionado = null;
    }
}
