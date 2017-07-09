/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import ec.discheca.configuracion.ControlSesion;
import ec.discheca.dao.DAOAsignacionComprobanteElectronico;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.AsignacionComprobanteElectronico;
import ec.discheca.modelo.Auditoria;
import ec.discheca.utilidades.MensajesPrimefaces;
import ec.discheca.utilidades.Reportes;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class BeanReporte implements Serializable {

    List<AsignacionComprobanteElectronico> comprobantesRecibidos;
    private String messelecionado = "0";
    private String anioSeleccionado = "2016";

    public BeanReporte() {
        obtenerComprobantes();
    }

    public void obtenerComprobantes() {
        try {
            ControlSesion ms = new ControlSesion();
            DAOAsignacionComprobanteElectronico daoAsignacion = new DAOAsignacionComprobanteElectronico();
            comprobantesRecibidos = daoAsignacion.obtenerComprobantesElectronicosAutorizadosRecibidosPormes(ms.obtenerRUCEmpresaSesionActiva(), messelecionado,anioSeleccionado);
        } catch (Exception ex) {
            Logger.getLogger(BeanReporte2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generacionreporte() {
        try {
            guardarLogRegistros("Acceso al modulo  Generacion Reporte Compras");
            ControlSesion ms = new ControlSesion();
            DAOAsignacionComprobanteElectronico daoAsignacion = new DAOAsignacionComprobanteElectronico();

            comprobantesRecibidos = daoAsignacion.obtenerComprobantesElectronicosAutorizadosRecibidosPormes(ms.obtenerRUCEmpresaSesionActiva(), messelecionado,anioSeleccionado);
            if (comprobantesRecibidos.size() > 0) {
                Reportes report = new Reportes();
                report.generarReporteExelRecibidos(comprobantesRecibidos);
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se han encontrado comprobantes.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanReporte.class.getName()).log(Level.SEVERE, null, ex);
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

    public String getMesselecionado() {
        return messelecionado;
    }

    public void setMesselecionado(String messelecionado) {
        this.messelecionado = messelecionado;
    }

    public List<AsignacionComprobanteElectronico> getComprobantesRecibidos() {
        return comprobantesRecibidos;
    }

    public void setComprobantesRecibidos(List<AsignacionComprobanteElectronico> comprobantesRecibidos) {
        this.comprobantesRecibidos = comprobantesRecibidos;
    }

    public String getAnioSeleccionado() {
        return anioSeleccionado;
    }

    public void setAnioSeleccionado(String anioSeleccionado) {
        this.anioSeleccionado = anioSeleccionado;
    }

}
