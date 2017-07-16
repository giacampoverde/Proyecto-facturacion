/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import java.io.Serializable;
import java.util.Enumeration;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author
 */
public class ControlSesion implements Serializable {

    private HttpSession sesion;

    public ControlSesion() {
        sesion = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
    }

    public ControlSesion(HttpSession _sesion) {
        sesion = _sesion;
    }

    public HttpSession getSesion() {
        return sesion;
    }

    public void setSesion(HttpSession _sesion) {
        this.sesion = _sesion;
    }

    private String getIdUsuario() {
        return sesion.getAttribute("idUsuario").toString();
    }

    private void setIdUsuario(String _idUsuario) {
        sesion.setAttribute("idUsuario", _idUsuario);
    }

    private String getIdPerfil() {
        return sesion.getAttribute("idPerfil").toString();
    }

    private void setIdPerfil(String _idPerfil) {
        sesion.setAttribute("idPerfil", _idPerfil);
    }

    private String getNombreUsuario() {
        return sesion.getAttribute("nombreUsuario").toString();
    }

    private void setNombreUsuario(String _nombreUsuario) {
        sesion.setAttribute("nombreUsuario", _nombreUsuario);
    }

    private String getRucEmpresa() {
        return sesion.getAttribute("rucEmpresa").toString();
    }

    public void setRucEmpresa(String _rucEmpresa) {
        sesion.setAttribute("rucEmpresa", _rucEmpresa);
    }

    private String getIdentificacionUsuarioInterno() {
        return sesion.getAttribute("identificacionUsuario").toString();
    }

    public void setIdentificacionUsuarioInterno(String _identificacionUsuario) {
        sesion.setAttribute("identificacionUsuario", _identificacionUsuario);
    }

    public void setPrimerInfoUsuario(String _primerInfoUsuario) {
        sesion.setAttribute("primerInfoUsuario", _primerInfoUsuario);
    }

    public void setSegundaInfoUsuario(String _segundaInfoUsuario) {
        sesion.setAttribute("segundaInfoUsuario", _segundaInfoUsuario);
    }

    private String getPrimerInfoUsuario() {
        return sesion.getAttribute("primerInfoUsuario").toString();
    }

    private String getSegundaInfoUsuario() {
        return sesion.getAttribute("segundaInfoUsuario").toString();
    }
    
        private String getTipoUsuario() {
        return sesion.getAttribute("tipoUsuario").toString();
    }

    public void setTipoUsuario(String _tipoUsuario) {
        sesion.setAttribute("tipoUsuario", _tipoUsuario);
    }

    /**
     * Método que recibe los parámetros del usuario que inicia sesión
     *
     * @param _nombreUsuario
     * @param _idPerfil
     * @param _idUsuario
     * @param _primerInfoUsuario
     * @param _segundaInfoUsuario
     * @param _identificacionUsuario
     * @param _rucEmpresaEmisor
     * @param _tipoUsuario
     */
    public void iniciarSesion(String _nombreUsuario, String _idPerfil, String _idUsuario, String _primerInfoUsuario, String _segundaInfoUsuario, String _identificacionUsuario, String _rucEmpresaEmisor, String _tipoUsuario) {
        setNombreUsuario(_nombreUsuario);
        setIdPerfil(_idPerfil);
        setIdUsuario(_idUsuario);
        setPrimerInfoUsuario(_primerInfoUsuario);
        setSegundaInfoUsuario(_segundaInfoUsuario);
        setIdentificacionUsuarioInterno(_identificacionUsuario);
        setRucEmpresa(_rucEmpresaEmisor);
        setTipoUsuario(_tipoUsuario);
    }
    public void iniciaSesionTemporal( String _idUsuario){
         setIdUsuario(_idUsuario);
    }
 public String obtenerSegundaInfoUsuarioSesionActiva() {
        if (obtenerEstadoSesionUsuario()) {
            return getSegundaInfoUsuario();
        } else {
            return null;
        }
    }
    /**
     * Metodo que elimina los atributos de la sesion http
     */
    public void cerrarSesion() {
        Enumeration<String> enume = sesion.getAttributeNames();
        while (enume.hasMoreElements()) {
            sesion.removeAttribute(enume.nextElement());
        }
    }

    /**
     * Obtiene el nombre de usuario en sesion
     *
     * @return el nombre de usuario
     */
    public String obtenerNombreUsuarioSesionActiva() {
        if (obtenerEstadoSesionUsuario()) {
            return getNombreUsuario();
        } else {
            return null;
        }
    }

    /**
     * Obtiene el id del perfil del usuario en sesion
     *
     * @return idPerfil de usuario
     */
    public String obtenerIdPerfilUsuarioSesionActiva() {
        if (obtenerEstadoSesionUsuario()) {
            return getIdPerfil();
        } else {
            return null;
        }
    }

    /**
     * Obtiene el id del usuario en sesion
     *
     * @return obtenerIdUsuarioSesionActiva en sesion
     */
    public String obtenerIdUsuarioSesionActiva() {
        if (obtenerEstadoSesionUsuario()) {
            return getIdUsuario();
        } else {
            return null;
        }
    }

    public String obtenerIdentificacionUsuario() {
        if (obtenerEstadoSesionUsuario()) {
            return getIdentificacionUsuarioInterno();
        } else {
            return null;
        }
    }

    public String obtenerRUCEmpresaSesionActiva() {
        if (obtenerEstadoSesionUsuario()) {
            return getRucEmpresa();
        } else {
            return null;
        }
    }

    public String obtenerPrimerInfoUsuarioSesionActiva() {
        if (obtenerEstadoSesionUsuario()) {
            return getPrimerInfoUsuario();
        } else {
            return null;
        }
    }

    public String obteneSegundaInfoUsuarioSesionActiva() {
        if (obtenerEstadoSesionUsuario()) {
            return getSegundaInfoUsuario();
        } else {
            return null;
        }
    }

    /**
     * Obtiene el estado de la sesion
     *
     * @return true si existe sesion, de lo contrario false
     */
    public boolean obtenerEstadoSesionUsuario() {
        boolean success = false;
        if (sesion != null) {
            try {
                if (getIdUsuario() != null && !getIdUsuario().equals("")) {
                    success = true;
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return success;
    }
    
    public String obtenerTipoUsuario() {

        if (obtenerEstadoSesionUsuario()) {
            return getTipoUsuario();
        } else {
            return null;
        }

    }
}
