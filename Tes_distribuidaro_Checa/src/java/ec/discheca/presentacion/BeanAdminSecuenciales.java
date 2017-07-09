/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Validaciones;
import ec.discheca.utilidades.MensajesPrimefaces;

import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOClienteEmpresa;
import ec.discheca.dao.DAOSecuencial;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.Empresa;
import ec.discheca.modelo.Secuencial;
import java.io.Serializable;
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
public class BeanAdminSecuenciales implements Serializable {

    /**
     * Creates a new instance of BeanAdminSecuenciales
     */
    private List<Secuencial> secuenciales;

    private Secuencial secuencialSeleccionado;

    private String codigoEstablecimientoEdicion;

    private String puntoEmisionEdicion;

    private String direccionEstablecimientoEdicion;

    private boolean cambiaEstadoSecuencial;

    private String info_empresa;

    private Secuencial nuevoSecuencial;
    private String sefac;
    private String senc;
    private String sere;

    public BeanAdminSecuenciales() {
        guardarLogRegistros("Acceso al modulo Administracion Secuenciales");
        cargarDatatableConSecuenciales();
        this.nuevoSecuencial = new Secuencial();
    }

    public void limpiarvalores() {
        sefac = "";
        senc = "";
        sere = "";
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
     * Método que carga el datatable con los secuenciales por establecimiento y
     * punto de emisión que tiene la empresa que ha iniciado sesión
     */
    private void cargarDatatableConSecuenciales() {
        try {
            ControlSesion ms = new ControlSesion();
            if (ms.obtenerEstadoSesionUsuario() == true) {
                secuenciales = this.instanciarDAO().obtenerSecuencialesPorRuc(ms.obtenerRUCEmpresaSesionActiva());
                if (secuenciales != null && !secuenciales.isEmpty()) {
                    info_empresa = secuenciales.get(0).getEmpresa().getNombreComercialClienteEmpresa() + " RUC: " + secuenciales.get(0).getEmpresa().getIdEmpresa();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(BeanAdminSecuenciales.class.getName()).log(Level.SEVERE, "Error al cargar la información del usuario", e);
        }
    }

    private void obtenerSecuencialPorId() {
        try {
            secuencialSeleccionado = this.instanciarDAO().obtenerSecuencialPorId(secuencialSeleccionado.getIdSecuencial());
        } catch (Exception ex) {
            Logger.getLogger(BeanAdminSecuenciales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *  /**
     * Método que instancia el DAOSecuenciales con la sesión de Hibernate.
     *
     * @return Objeto de tipo DAOSecuenciales
     *
     */
    private DAOSecuencial instanciarDAO() {
        DAOSecuencial dao_secuenciales = null;
        try {
            dao_secuenciales = new DAOSecuencial();
        } catch (Exception ex) {
            Logger.getLogger(BeanAdminSecuenciales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dao_secuenciales;
    }

    /**
     * Método para validar la edición del código de establecimiento.
     */
    public void validarEdicionCodigoEstablecimiento() {

        if (codigoEstablecimientoEdicion != null && !codigoEstablecimientoEdicion.equals("")) {
            if (Validaciones.isNum(codigoEstablecimientoEdicion)) {
                if (codigoEstablecimientoEdicion.length() == 3) {
                    if (!validarSecuencialRepetido(secuencialSeleccionado.getPuntoEmisionSecuencial(), codigoEstablecimientoEdicion)) {

                        boolean actualizado = this.instanciarDAO().actualizarCodigoEstablecimiento(secuencialSeleccionado.getIdSecuencial(), codigoEstablecimientoEdicion);
                        if (actualizado) {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Código de establecimiento actualizado.");
                            puntoEmisionEdicion = "";
                            cargarDatatableConSecuenciales();
                        } else {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar el código de establecimiento.");
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ya ha registrado anteriormente un secuencial con el mismo Código de Estblecimiento y Punto de Emisión.");
                    }

                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El código de establecimiento debe tener 3 dígitos.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El código de establecimiento debe ser un número.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El código de establecimiento no puede estar vacío.");
            obtenerSecuencialPorId();
            codigoEstablecimientoEdicion = secuencialSeleccionado.getCodigoEstablecimientoSecuencial();
        }

    }

    /**
     * Método para validar la edición punto de emisión.
     */
    public void validarEdicionPuntoEmision() {

        if (puntoEmisionEdicion != null && !puntoEmisionEdicion.equals("")) {
            if (Validaciones.isNum(puntoEmisionEdicion)) {
                if (puntoEmisionEdicion.length() == 3) {
                    if (!validarSecuencialRepetido(puntoEmisionEdicion, secuencialSeleccionado.getCodigoEstablecimientoSecuencial())) {
                        boolean actualizado = this.instanciarDAO().actualizarPuntoEmision(secuencialSeleccionado.getIdSecuencial(), puntoEmisionEdicion);
                        if (actualizado) {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Punto de emisión actualizado.");
                            codigoEstablecimientoEdicion = "";
                            cargarDatatableConSecuenciales();
                        } else {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar el punto de emisión.");
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ya ha registrado anteriormente un secuencial con el mismo Código de Estblecimiento y Punto de Emisión.");
                    }

                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El punto de emisión debe tener 3 dígitos.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El punto de emisión debe ser un número.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El punto de emisión no puede estar vacío.");
            obtenerSecuencialPorId();
            puntoEmisionEdicion = secuencialSeleccionado.getPuntoEmisionSecuencial();
        }
    }

    /**
     * Método para validar que el secuencial no se repita
     */
    public boolean validarSecuencialRepetido(String puntoEmision, String codigoEstablecimiento) {
        boolean respuesta = false;
        for (Secuencial sec : secuenciales) {
            if (sec.getPuntoEmisionSecuencial().equals(puntoEmision) && sec.getCodigoEstablecimientoSecuencial().equals(codigoEstablecimiento)) {
                respuesta = true;
                break;
            }
        }
        return respuesta;
    }

    public void actualizarSecuenciales() {
        boolean actualizado = this.instanciarDAO().datossecuanlesPersonales(secuencialSeleccionado.getIdSecuencial(), sefac, senc, sere);
        if (actualizado) {
            cargarDatatableConSecuenciales();
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Numero de secuencial actualizado correctamente.");
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Error al actualizar.");
        }
    }

    /**
     * Método para validar la edición de la dirección del secuencial
     */
    public void validarEdicionDireccion() {

        if (direccionEstablecimientoEdicion != null && !direccionEstablecimientoEdicion.equals("")) {

            boolean actualizado = this.instanciarDAO().actualizarDireccion(secuencialSeleccionado.getIdSecuencial(), direccionEstablecimientoEdicion);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Dirección actualizada.");
                cargarDatatableConSecuenciales();
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar la dirección.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La dirección no puede estar vacía.");
            obtenerSecuencialPorId();
            direccionEstablecimientoEdicion = secuencialSeleccionado.getDireccionSecuencial();
        }

    }

    public boolean desactivarSecuencialActivo() {
        ControlSesion cs = new ControlSesion();
        boolean respuesta = true;
        try {
            List<Secuencial> lista = this.instanciarDAO().obtenerSecuencialesPorRucYAmbiente(cs.obtenerRUCEmpresaSesionActiva(), Valores.AMBIENTE);
            if (lista != null) {
                for (Secuencial sec : lista) {
                    if (sec.getEstadoSecuencial() == true) {
                        respuesta = this.instanciarDAO().actualizarEstadoSecuencial(sec.getIdSecuencial(), false);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanAdminSecuenciales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    public void validarCambioEstadoSecuencial() {
        boolean desactivado = false;
        String mensaje2 = "";
        if (cambiaEstadoSecuencial == true) {
            desactivado = desactivarSecuencialActivo();
//            mensaje2 = "El secuencial se ha activado y se han desactivado los otros.";
            mensaje2 = "El secuencial se ha activado.";
        } else {
            desactivado = true;
        }
        if (desactivado) {
            boolean actualizado = this.instanciarDAO().actualizarEstadoSecuencial(secuencialSeleccionado.getIdSecuencial(), cambiaEstadoSecuencial);
            if (actualizado) {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Estado del secuencial actualizado. " + mensaje2);
                cargarDatatableConSecuenciales();
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar el secuencial.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al actualizar el secuencial. No se ha podido desactivar el secuencial activo");
        }

    }

    public void cargarEstadoActualSecuencial() {
        if (secuencialSeleccionado != null) {
            puntoEmisionEdicion = "";
            codigoEstablecimientoEdicion = "";
            setCambiaEstadoSecuencial(secuencialSeleccionado.getEstadoSecuencial().booleanValue());
            RequestContext.getCurrentInstance().execute("PF('wv-dialog-actualizar-secuencial').show();");
        }
    }

    public void insertarSecuencial() {
        if (this.nuevoSecuencial.getCodigoEstablecimientoSecuencial().trim().equals("") || !Validaciones.isNum(this.nuevoSecuencial.getCodigoEstablecimientoSecuencial()) || this.nuevoSecuencial.getCodigoEstablecimientoSecuencial().length() < 3) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el Código de Establecimiento de 3 dígitos. Ejemplo: 001.");
        } else if (this.nuevoSecuencial.getPuntoEmisionSecuencial().trim().equals("") || !Validaciones.isNum(this.nuevoSecuencial.getPuntoEmisionSecuencial()) || this.nuevoSecuencial.getPuntoEmisionSecuencial().length() < 3) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar el Punto de Emisión de 3 dígitos. Ejemplo: 002.");
        } else if (this.nuevoSecuencial.getDireccionSecuencial().trim().equals("")) {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar la Dirección.");
        } else {
            if (!validarSecuencialRepetido(this.nuevoSecuencial.getPuntoEmisionSecuencial(), this.nuevoSecuencial.getCodigoEstablecimientoSecuencial())) {
                ControlSesion ms = new ControlSesion();
                this.nuevoSecuencial.setAmbienteSecuencial(Valores.AMBIENTE);
                this.nuevoSecuencial.setSecuencialFacturaSecuencial("1");
                this.nuevoSecuencial.setSecuencialGuiaRemisionSecuencial("1");
                this.nuevoSecuencial.setSecuencialNotaCreditoSecuencial("1");
                this.nuevoSecuencial.setSecuencialNotaDebitoSecuencial("1");
                this.nuevoSecuencial.setSecuencialRetencionSecuencial("1");
                try {
                    Secuencial aux = this.instanciarDAO().obtenerSecuencialActivoPorRucYAmbiente(ms.obtenerRUCEmpresaSesionActiva(), Valores.AMBIENTE);
                    if (aux != null) {
                        this.nuevoSecuencial.setEstadoSecuencial(false);
                    } else {
                        this.nuevoSecuencial.setEstadoSecuencial(true);
                    }
                    DAOClienteEmpresa dce = new DAOClienteEmpresa();
                    Empresa ce = dce.obtenerClienteEmpresaPorId(ms.obtenerRUCEmpresaSesionActiva());
                    this.nuevoSecuencial.setEmpresa(ce);
                    if (!this.instanciarDAO().insertarSecuencial(this.nuevoSecuencial)) {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo agregar el nuevo secuencial.");
                    } else {
                        guardarLogRegistros("Registro un nuevo Secuencial");
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Se ha agregado el nuevo secuencial.");
                        this.secuenciales.add(this.nuevoSecuencial);
                        cerrarDialogoNuevo();
                    }
                } catch (Exception ex) {
                    Logger.getLogger(BeanAdminSecuenciales.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ya ha registrado anteriormente un secuencial con el mismo Código de Estblecimiento y Punto de Emisión.");
            }
        }
    }

    public void abrirDialogoNuevo() {
        this.nuevoSecuencial = new Secuencial();
        RequestContext.getCurrentInstance().execute("PF('wv-dialog-nuevo-secuencial').show();");
    }

    public void cerrarDialogoNuevo() {
        RequestContext.getCurrentInstance().execute("PF('wv-dialog-nuevo-secuencial').hide();");
    }

    public List<Secuencial> getSecuenciales() {
        return secuenciales;
    }

    public void setSecuenciales(List<Secuencial> secuenciales) {
        this.secuenciales = secuenciales;
    }

    public Secuencial getSecuencialSeleccionado() {
        return secuencialSeleccionado;
    }

    public void setSecuencialSeleccionado(Secuencial secuencialSeleccionado) {
        this.secuencialSeleccionado = secuencialSeleccionado;
    }

    public String getCodigoEstablecimientoEdicion() {
        return codigoEstablecimientoEdicion;
    }

    public void setCodigoEstablecimientoEdicion(String codigoEstablecimientoEdicion) {
        this.codigoEstablecimientoEdicion = codigoEstablecimientoEdicion;
    }

    public String getPuntoEmisionEdicion() {
        return puntoEmisionEdicion;
    }

    public void setPuntoEmisionEdicion(String puntoEmisionEdicion) {
        this.puntoEmisionEdicion = puntoEmisionEdicion;
    }

    public String getDireccionEstablecimientoEdicion() {
        return direccionEstablecimientoEdicion;
    }

    public void setDireccionEstablecimientoEdicion(String direccionEstablecimientoEdicion) {
        this.direccionEstablecimientoEdicion = direccionEstablecimientoEdicion;
    }

    public boolean isCambiaEstadoSecuencial() {
        return cambiaEstadoSecuencial;
    }

    public void setCambiaEstadoSecuencial(boolean cambiaEstadoSecuencial) {
        this.cambiaEstadoSecuencial = cambiaEstadoSecuencial;
    }

    public String getInfo_empresa() {
        return info_empresa;
    }

    public void setInfo_empresa(String info_empresa) {
        this.info_empresa = info_empresa;
    }

    public Secuencial getNuevoSecuencial() {
        return nuevoSecuencial;
    }

    public void setNuevoSecuencial(Secuencial nuevoSecuencial) {
        this.nuevoSecuencial = nuevoSecuencial;
    }

    public String getSefac() {
        return sefac;
    }

    public void setSefac(String sefac) {
        this.sefac = sefac;
    }

    public String getSenc() {
        return senc;
    }

    public void setSenc(String senc) {
        this.senc = senc;
    }

    public String getSere() {
        return sere;
    }

    public void setSere(String sere) {
        this.sere = sere;
    }

}
