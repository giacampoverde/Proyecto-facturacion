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
public class BeanReporte2 implements Serializable {

    List<AsignacionComprobanteElectronico> comprobantesEmitidos;
    private String messelecionado="0";
    private String anoSeleccioado="2016";

    public BeanReporte2() {
    obtenerComprobantes();
    }

    public void obtenerComprobantes() {
        try {
            ControlSesion ms = new ControlSesion();
            DAOAsignacionComprobanteElectronico daoAsignacion = new DAOAsignacionComprobanteElectronico();
            comprobantesEmitidos = daoAsignacion.obtenerComprobantesElectronicosAutorizadosPorRUCMEs(ms.obtenerRUCEmpresaSesionActiva(), messelecionado,anoSeleccioado, "01");
        } catch (Exception ex) {
            Logger.getLogger(BeanReporte2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generacionreporte() {
        try {
            guardarLogRegistros("Acceso al modulo  Generacion Reporte Ventas");
            ControlSesion ms = new ControlSesion();
            DAOAsignacionComprobanteElectronico daoAsignacion = new DAOAsignacionComprobanteElectronico();

            comprobantesEmitidos = daoAsignacion.obtenerComprobantesElectronicosAutorizadosPorRUCMEs(ms.obtenerRUCEmpresaSesionActiva(), messelecionado,anoSeleccioado, "01");
            if (comprobantesEmitidos.size() > 0) {
                Reportes report = new Reportes();
                report.generarReporteExelRecibidos(comprobantesEmitidos);
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se han encontrado comprobantes.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanReporte2.class.getName()).log(Level.SEVERE, null, ex);
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

    public String getAnoSeleccioado() {
        return anoSeleccioado;
    }

    public void setAnoSeleccioado(String anoSeleccioado) {
        this.anoSeleccioado = anoSeleccioado;
    }

    public List<AsignacionComprobanteElectronico> getComprobantesEmitidos() {
        return comprobantesEmitidos;
    }

    public void setComprobantesEmitidos(List<AsignacionComprobanteElectronico> comprobantesEmitidos) {
        this.comprobantesEmitidos = comprobantesEmitidos;
    }

}
