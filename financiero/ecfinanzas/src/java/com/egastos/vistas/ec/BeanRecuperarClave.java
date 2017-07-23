/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;




import com.egastos.correo.Correo;
import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.MensajesPrimefaces;
import com.egastos.utilidades.MetodosDeUtilidad;
import com.egastos.utilidades.Valores;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@ViewScoped
public class BeanRecuperarClave implements Serializable {

    private String nombreUsuario;

    /**
     * Creates a new instance of BeanRecuperarClave
     */
    public BeanRecuperarClave() {
//        ControlSesion ms = new ControlSesion();
//            if (!ms.obtenerEstadoSesionUsuario()) {
//
//                BeanDireccionamiento nosesion=new BeanDireccionamiento();
//                nosesion.direccionarLogin();
//            }
    }

    public void enviarCorreoRecuperacion() {
        try {
            UsuarioAcceso usuarioTmp = null;
            DAOUsuarioAcceso usuarioDao = new DAOUsuarioAcceso();
            if (!this.nombreUsuario.trim().equals("")) {
                try {
//                    usuarioTmp = usuarioDao.obtenerUsuarioAccesoPorNombre(nombreUsuario);
                      usuarioTmp = usuarioDao.obtenerUsuarioPorRucCedula(nombreUsuario);
                    if (usuarioTmp == null) {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El usuario ingresado no existe.");
                    } else {
                       enviarCorreoRestablecerClave(usuarioTmp);
                        FacesContext.getCurrentInstance().getExternalContext().redirect("recuperar-clave1.xhtml");
                    }
                } catch (Exception ex) {
                    Logger.getLogger(BeanRecuperarClave.class.getName()).log(Level.SEVERE, null, ex);
                }

            }else{
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un numero de cedula.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanRecuperarClave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean enviarCorreoRestablecerClave(UsuarioAcceso _usuario) {
        boolean respuesta = false;
        try {
            List<String> contenido = new ArrayList<String>();
            List<String> imagenes = new ArrayList<String>();
            String url;
            Correo correo = new Correo("1");

            /*Se obtienen dos claves aleatorias*/
            String contrasena = MetodosDeUtilidad.generarcodigoenvioclave();
            String contraseña2 = MetodosDeUtilidad.generarcodigoenvioclave();

            /*Se obtiene el dominio para ponerlo en la url*/
            String dominio = MetodosDeUtilidad.obtenerDominiourl();
            url = "Para seguir con el proceso de restablecer clave haga <a href='" + dominio + "verificacion?cli=" + contrasena + _usuario.getIdUsuario() + contraseña2 + "'>Clic Aqui</a><br/><br/>Si el link no funciona por favor copie la siguiente URL en la barra de navegación :<br/>" + dominio + "verificacion?cli=" + contrasena + _usuario.getIdUsuario() + contraseña2;

            /*Se agrega el nombre de ususario y url para el contenido de la plantilla*/
            contenido.add(_usuario.getNombreUsuarioAcceso());
            contenido.add(url);

            /*Se agregan las imagenes para colocarlas en la plantilla*/
            imagenes.add("cab");
            imagenes.add("pie");

            /*Se realiza el envio de correo*/
            if (_usuario.getCorreoPrincipalUsuario() != null && !_usuario.getCorreoPrincipalUsuario().equals("")) {
                respuesta = correo.enviarMailRecuperacionClave(_usuario.getCorreoPrincipalUsuario(), contenido, Valores.VALOR_PLANTILLA_HTML_CORREO + File.separator + "recuperacion_clave.html", Valores.VALOR_PLANTILLA_IMAGENES_CORREO + File.separator, "Restablecer Clave", imagenes);
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanRecuperarClave.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

}
