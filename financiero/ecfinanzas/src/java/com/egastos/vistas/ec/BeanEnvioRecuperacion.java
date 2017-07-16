/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.vistas.ec;



import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.MensajesPrimefaces;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Ricardo Delgado
 */
@ManagedBean
@ViewScoped
public class BeanEnvioRecuperacion implements Serializable{

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getConfirmacionClave() {
        return confirmacionClave;
    }

    public void setConfirmacionClave(String confirmacionClave) {
        this.confirmacionClave = confirmacionClave;
    }

    String idUsuario;
    private UsuarioAcceso usuario;
    private String clave;
    private String confirmacionClave;
    /**
     * Creates a new instance of BeanEnvioRecuperacion
     */
    public BeanEnvioRecuperacion() {
        String respuestaurl = "";
        String idUsuario;
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest) context.getRequest();
        
//        System.out.print(request.getParameter("cli"));
        if (request.getParameter("cli") != null) {
            try {
                respuestaurl = request.getParameter("cli");
                String resultado1 = respuestaurl.substring(6, respuestaurl.length());
                idUsuario = resultado1.substring(0, (resultado1.length() - 6));
                DAOUsuarioAcceso dua = new DAOUsuarioAcceso();
                usuario = dua.obtenerUsuarioAccesoPorId(Integer.parseInt(idUsuario));
            } catch (Exception ex) {
                Logger.getLogger(BeanEnvioRecuperacion.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public void cambiarClave() throws Exception {
        DAOUsuarioAcceso dua = new DAOUsuarioAcceso();
        if (clave.equals("") || confirmacionClave.equals("")) {
            if (clave.equals("")) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_WARN, "El campo Clave no puede estar vacío.");
            }
            if (confirmacionClave.equals("")) {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_WARN, "El campo Confirmación de Clave no puede estar vacío.");
            }
        } else {
            if (clave.equals(confirmacionClave)) {
                if (clave.trim().length() >= 8) {
                    if(usuario==null){
                        ControlSesion a=new ControlSesion();
                        usuario=new UsuarioAcceso();
                        usuario.setIdUsuario(Integer.parseInt(a.obtenerIdUsuarioSesionActiva()));
                    }
                    if (dua.actualizarClaveUsuarioAcceso(usuario.getIdUsuario(), clave)) {
//                        dua.actualizarEstadoUsuario(usuario.getIdUsuario(),"1");
                         MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_WARN, "Se ha actualizado la clave correctamente.");
                         FacesContext.getCurrentInstance().getExternalContext().redirect("mensajecambioClave.xhtml");
                    } else {
                         MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_WARN, "No se ha podido actualizar la clave.");
                    }
                } else {
                     MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_WARN, "La clave debe tener por lo menos 8 caracteres.");
                }
            } else {
                 MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Los datos ingresados en Clave y Confirmación de Clave no coinciden.");
            }
        }
    }
    
}
