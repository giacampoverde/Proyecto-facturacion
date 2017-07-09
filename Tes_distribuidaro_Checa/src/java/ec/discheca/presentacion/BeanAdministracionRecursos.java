/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import ec.discheca.configuracion.ControlSesion;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOPerfil;
import ec.discheca.dao.DAOPermiso;
import ec.discheca.dao.DAORecursoPantalla;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Perfil;
import ec.discheca.modelo.Permiso;
import ec.discheca.modelo.Recurso;
import ec.discheca.utilidades.MensajesPrimefaces;

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

@ManagedBean
@ViewScoped
public class BeanAdministracionRecursos implements Serializable {

    private Integer perfilSeleccionado;
    private List<Perfil> perfiles;
    private List<Permiso> permisos;
    private String nombrePerfilSeleccionado;
    private Integer recursoSeleccionado;
    private List<Recurso> recursos;
    private Permiso permisoSeleccionado;
    private List<Permiso> permisosHijos;

    public List<Permiso> getPermisosHijos() {
        return permisosHijos;
    }

    public void setPermisosHijos(List<Permiso> permisosHijos) {
        this.permisosHijos = permisosHijos;
    }

    public Permiso getPermisoSeleccionado() {
        return permisoSeleccionado;
    }

    public void setPermisoSeleccionado(Permiso permisoSeleccionado) {
        this.permisoSeleccionado = permisoSeleccionado;
    }

    public List<Recurso> getRecursos() {
        return recursos;
    }

    public void setRecursos(List<Recurso> recursos) {
        this.recursos = recursos;
    }

    public Integer getRecursoSeleccionado() {
        return recursoSeleccionado;
    }

    public void setRecursoSeleccionado(Integer recursoSeleccionado) {
        this.recursoSeleccionado = recursoSeleccionado;
    }

    public String getNombrePerfilSeleccionado() {
        return nombrePerfilSeleccionado;
    }

    public void setNombrePerfilSeleccionado(String nombrePerfilSeleccionado) {
        this.nombrePerfilSeleccionado = nombrePerfilSeleccionado;
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    public Integer getPerfilSeleccionado() {
        return perfilSeleccionado;
    }

    public void setPerfilSeleccionado(Integer perfilSeleccionado) {
        this.perfilSeleccionado = perfilSeleccionado;
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
     * Creates a new instance of BeanAdministracionRecursos
     */
    public BeanAdministracionRecursos() {
        try {
            guardarLogRegistros("Acceso al modulo Administracion Recursos");
            cargarPermisos();
            perfiles = this.instanciarDAOPerfil().obtenerPerfilactivos();
            recursos = this.instanciarDAORecurso().obtenerPantallas();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public final void cargarPermisos() {
        if (perfilSeleccionado == null) {
            perfilSeleccionado = 8;
        }
        try {
            permisos = this.instanciarDAOPermiso().obtenerPermisosPorPerfil(perfilSeleccionado);
            nombrePerfilSeleccionado = this.instanciarDAOPerfil().obtenerPerfilPorId(perfilSeleccionado).getNombrePerfil();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private DAOPermiso instanciarDAOPermiso() {
        DAOPermiso dao_permiso = null;

        try {
            dao_permiso = new DAOPermiso();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_permiso;
    }

    private DAOPerfil instanciarDAOPerfil() {
        DAOPerfil dao_perfil = null;

        try {
            dao_perfil = new DAOPerfil();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_perfil;
    }

    public void asignarPermiso() {
        if (perfilSeleccionado != null && recursoSeleccionado != null) {
            try {
                Permiso p = this.instanciarDAOPermiso().obtenerPermisosPorPerfilRecurso(perfilSeleccionado, recursoSeleccionado);
                if (p == null) {
                    /**
                     * Recurso de la página hija.
                     */
                    Recurso recurso = this.instanciarDAORecurso().obtenerRecursoPorId(recursoSeleccionado);
                    Perfil perfil = this.instanciarDAOPerfil().obtenerPerfilPorId(perfilSeleccionado);
                    /**
                     * Se verifica si el recurso padre ya está asignado al
                     * permiso.
                     */
                    Recurso recurso_padre = this.instanciarDAORecurso().obtenerRecursoPorId(recurso.getRecurso().getIdRecurso());
                    Permiso permiso_recurso_padre = this.instanciarDAOPermiso().obtenerPermisosPorPerfilRecurso(perfilSeleccionado, recurso.getRecurso().getIdRecurso());
                    boolean asignado_permiso_recurso_padre = false;
                    if (permiso_recurso_padre == null) {
                        asignado_permiso_recurso_padre = this.instanciarDAOPermiso().asignarPermiso(perfil, recurso_padre);
                    } else {
                        asignado_permiso_recurso_padre = true;
                    }
                    if (asignado_permiso_recurso_padre) {
                        boolean asignado_recurso = this.instanciarDAOPermiso().asignarPermiso(perfil, recurso);
                        if (asignado_recurso) {
                            guardarLogRegistros("Ha asignado un permiso");
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El permiso ha sido asignado.");
                            cargarPermisos();
                        } else {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al asignar el permiso.");
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al asignar el permiso.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Este permiso ya se encuentra asignado.");
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanAdministracionRecursos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private DAORecursoPantalla instanciarDAORecurso() {
        DAORecursoPantalla dao_recurso = null;

        try {
            dao_recurso = new DAORecursoPantalla();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_recurso;
    }

    public void mostrarDialogSegunTipoRecurso() {
        if (permisoSeleccionado != null) {
            if (permisoSeleccionado.getRecurso().getRecurso() == null) {
                if (permisoSeleccionado.getRecurso().getPestaniaRecurso()) {
                    obtenerRecursosHijos();
                    RequestContext.getCurrentInstance().execute("PF('wv-confirma-eliminado-pestania-hijos').show();");

                }
            } else {
                RequestContext.getCurrentInstance().execute("PF('wv-confirma-eliminado-hijos').show();");
            }
        }
    }

    private void obtenerRecursosHijos() {
        try {
            permisosHijos = this.instanciarDAOPermiso().obtenerRecursosPorPermisosHijos(permisoSeleccionado.getRecurso().getIdRecurso(), permisoSeleccionado.getPerfil().getIdPerfil());
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionRecursos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eliminarPermiso() {
        boolean eliminado = false;
        if (permisoSeleccionado != null) {
            if (permisoSeleccionado.getRecurso().getRecurso() == null) {
                ArrayList<Boolean> eliminados = new ArrayList<Boolean>();
                if (permisoSeleccionado.getRecurso().getPestaniaRecurso()) {
                    obtenerRecursosHijos();
                    if (!permisosHijos.isEmpty()) {
                        permisosHijos.add(permisoSeleccionado);
                        for (Permiso p : permisosHijos) {
                            eliminado = this.instanciarDAOPermiso().eliminarPermiso(p);
                            eliminados.add(eliminado);
                        }
                    } else {
                        eliminado = this.instanciarDAOPermiso().eliminarPermiso(permisoSeleccionado);
                        eliminados.add(eliminado);
                    }

                    if (!eliminados.contains(false)) {
                        guardarLogRegistros("Ha eliminado un permiso");
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El permiso ha sido eliminado.");
                        RequestContext.getCurrentInstance().execute("PF('wv-confirma-eliminado-pestania-hijos').hide();");
                        cargarPermisos();
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al eliminar el permiso.");
                        RequestContext.getCurrentInstance().execute("PF('wv-confirma-eliminado-pestania-hijos').hide();");
                    }
                }
            } else {
                eliminado = this.instanciarDAOPermiso().eliminarPermiso(permisoSeleccionado);
                if (eliminado) {
                    guardarLogRegistros("Ha eliminado un permiso");
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El permiso ha sido eliminado.");
                    RequestContext.getCurrentInstance().execute("PF('wv-confirma-eliminado-hijos').hide();");
                    cargarPermisos();
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al eliminar el permiso.");
                    RequestContext.getCurrentInstance().execute("PF('wv-confirma-eliminado-hijos').hide();");
                }
            }

        }

    }
}
