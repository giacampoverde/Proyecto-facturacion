/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOPagos;
import com.egastos.dao.ec.DAOPlanesPago;
import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.Pagos;
import com.egastos.modelo.ec.Planespago;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.MensajesPrimefaces;
import com.egastos.utilidades.Valores;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ricar
 */
@ManagedBean
@ViewScoped
public class BeanProcesoPagos implements Serializable {

    public UsuarioAcceso usuarios;
    private String numeroCuentaCliente2;
    private String numeroTransferencia;
    private String valorPagado;
    private List<Planespago> planesPago;
    private Integer planId;
    private Planespago planSeleccionado;
    private String cuentaP;
    private String cuenta2;
    private String cuenta3;
    private String cuentaSeleccionada;
    private UploadedFile file;

    public String getNumeroTransferencia() {
        return numeroTransferencia;
    }

    public void foto() {
        Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.INFO, "Imagen Fotos.");
        if (!file.getFileName().equals("")) {
            if (file.getFileName().substring((file.getFileName().length() - 3), file.getFileName().length()).equals("png")
                    || file.getFileName().substring((file.getFileName().length() - 3), file.getFileName().length()).equals("PNG")
                    || file.getFileName().substring((file.getFileName().length() - 3), file.getFileName().length()).equals("Png")
                    || file.getFileName().substring((file.getFileName().length() - 3), file.getFileName().length()).equals("jpg")
                    || file.getFileName().substring((file.getFileName().length() - 3), file.getFileName().length()).equals("JPG")
                    || file.getFileName().substring((file.getFileName().length() - 3), file.getFileName().length()).equals("Jpg")) {
                try {
                    File file2 = new File("C:\\transferencias\\asd.png");
                    int width = 200;
                    int height = 200;
                    
                    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                    
                    Graphics2D g = image.createGraphics();
                    ImageIO.write(image, "png", file2);
                } catch (IOException ex) {
                    Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                file = new DefaultUploadedFile();
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Formato de archivo incorrecto");
            }
        } else {
            file = new DefaultUploadedFile();
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No ha cargador un archivo");
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void setNumeroTransferencia(String numeroTransferencia) {
        this.numeroTransferencia = numeroTransferencia;
    }

    public String getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(String cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public String getCuentaP() {
        return cuentaP;
    }

    public void setCuentaP(String cuentaP) {
        this.cuentaP = cuentaP;
    }

    public String getCuenta2() {
        return cuenta2;
    }

    public void setCuenta2(String cuenta2) {
        this.cuenta2 = cuenta2;
    }

    public String getCuenta3() {
        return cuenta3;
    }

    public void setCuenta3(String cuenta3) {
        this.cuenta3 = cuenta3;
    }

    public UsuarioAcceso getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(UsuarioAcceso usuarios) {
        this.usuarios = usuarios;
    }

    public Planespago getPlanSeleccionado() {
        return planSeleccionado;
    }

    public void setPlanSeleccionado(Planespago planSeleccionado) {
        this.planSeleccionado = planSeleccionado;
    }

    public void registrarPago() {
        ControlSesion ms = new ControlSesion();
        if (!ms.obtenerEstadoSesionUsuario()) {

            BeanDireccionamiento nosesion = new BeanDireccionamiento();
            nosesion.direccionarLogin();
        }
        try {
            if (planId == null) {

                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Porfavor seleccione el plan por el cual pago.");
                return;
            } else {
                for (Planespago plan : planesPago) {
                    if (plan.getIdPlanesPago().equals(planId)) {
                        planSeleccionado = plan;
                    }
                }
                if (cuentaSeleccionada == null) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Porfavor seleccione la cuanta a la cual le realizo el pago.");
                    return;
                } else {
                    if (numeroCuentaCliente2.equals("")) {
                        MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Porfavor ingrese el numero de cuenta de donde realizo el pago.");
                        return;
                    }
//                    }else{
////                      if(numeroTransferencia.equals("")){
////                          MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Porfavor ingrese el numero de .");
////                          return;
////                      }
//                    }
                }
            }
            Date nuevaFecha = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(nuevaFecha);
            cal.add(Calendar.MONTH, planSeleccionado.getMeses());
            nuevaFecha = cal.getTime();
            Pagos pagos = new Pagos();
            pagos.setEstadopago("revision");
            pagos.setFechaPago(new Date());
            pagos.setFechacaduca(nuevaFecha);
            pagos.setNumeroCuentaOrigen(numeroCuentaCliente2);
            pagos.setNumeroCuenta(cuentaSeleccionada);
            pagos.setNumeroTransferencia(numeroTransferencia);
            pagos.setValorPago(planSeleccionado.getValor());
            pagos.setUsuarioAcceso(usuarios);
            pagos.setPlanespago(planSeleccionado);
            //pagos imagen
//            if(imagen){
//                
//            }

            pagos.setImagenTransferencia(cuentaSeleccionada);
            DAOPagos daoPagos = new DAOPagos();
            boolean respuesta = daoPagos.InsertarPago(pagos);
            if (respuesta) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletRequest request = (HttpServletRequest) context.getRequest();
                FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/mensajeFinalPago.xhtml");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public BeanProcesoPagos() {
        try {
            ControlSesion ms = new ControlSesion();
            DAOUsuarioAcceso usuario = new DAOUsuarioAcceso();
            DAOPlanesPago daoPlanes = new DAOPlanesPago();

            usuarios = usuario.obtenerUsuarioAccesoPorId(Integer.parseInt(ms.obtenerIdUsuarioSesionActiva()));
            planesPago = daoPlanes.obtenerPlanesPago();
            cuentaP = Valores.VALOR_NUMERO_CUENTA1;
            cuenta2 = Valores.VALOR_NUMERO_CUENTA2;
            cuenta3 = Valores.VALOR_NUMERO_CUENTA3;
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DirigirBeanProcesoPagos() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/pagos.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void RegresarBeanProcesoPaginaPagos() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest) context.getRequest();
            FacesContext.getCurrentInstance().getExternalContext().redirect(request.getContextPath() + "/login.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(BeanProcesoPagos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getNumeroCuentaCliente2() {
        return numeroCuentaCliente2;
    }

    public void setNumeroCuentaCliente2(String numeroCuentaCliente2) {
        this.numeroCuentaCliente2 = numeroCuentaCliente2;
    }

    public String getValorPagado() {
        return valorPagado;
    }

    public void setValorPagado(String valorPagado) {
        this.valorPagado = valorPagado;
    }

    public List<Planespago> getPlanesPago() {
        return planesPago;
    }

    public void setPlanesPago(List<Planespago> planesPago) {
        this.planesPago = planesPago;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

}
