/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOAsignacionComprobanteElectronico;
import com.egastos.dao.ec.DAOComprobanteElectronico;
import com.egastos.dao.ec.DAOReceptor;
import com.egastos.modelo.ec.ComprobanteElectronico;
import com.egastos.modelo.ec.Receptor;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.MensajesPrimefaces;
import com.egastos.utilidades.Utilidades;
import com.egastos.utilidades.Valores;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author ricar
 */
@ManagedBean
@ViewScoped
public class beanReportes implements Serializable {

    private Date fechaInicial;
    private Date fechaFinal;
    private Date fechaSeleccionadaInicio;
    private Date fechaActual;
    private String seleccionPeriodoTiempo;
    private String opcionValores;
    private String valorInicial;
    private String valorFinal;
    private boolean apareceRago = false;
    private String fechaInicialS;
    private String fechaFinalS;
    private boolean apareceValores = false;
    private String rucEmpresa;
    private String detalleEmpresa;

    public beanReportes() {
        ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
        seleccionPeriodoTiempo = "4";
        this.obtenerFechas();
    }
public void verempresa(){
  if(!rucEmpresa.equals("")){
      try {
          DAOComprobanteElectronico comprobante=new DAOComprobanteElectronico();
          List<ComprobanteElectronico> respuesta = comprobante.obtenerComprobantesElectronicosPorRUC(rucEmpresa, 1, 1);
         if(respuesta.size()>0){
          detalleEmpresa= respuesta.get(0).getRazonSocialEmisorComprobanteElectronico();
      }else{
          detalleEmpresa="No se encontro empresa con ese ruc";
         }
        
      } catch (Exception ex) {
          Logger.getLogger(beanReportes.class.getName()).log(Level.SEVERE, null, ex);
      }
              }else{
                detalleEmpresa="Ingrese un Ruc";
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Campo ruc obligatorio");
   
              }
}
    public void generarReporte(int valor) throws Exception {
        try {

            FacesContext faces = FacesContext.getCurrentInstance();
            Connection a;
            //pruebas
            
           a = DriverManager.getConnection(Valores.VALOR_CONEXION,Valores.VALOR_USUARIOREPORTE,Valores.VALOR_CONTRAREPORTE);
                //produccion roler1721
           Map<String, Object> parametros = new HashMap<String, Object>();
            ControlSesion sesion = new ControlSesion();
         
           DAOReceptor receptor=new DAOReceptor();
            Receptor rescep = receptor.obtenerReceptorPorIdentificacion(sesion.obtenerRUCEmpresaSesionActiva());
             parametros.put("cedula",rescep.getIdReceptor());
            
            this.obtenerFechas();
            parametros.put("fechaIni", fechaInicialS);
            parametros.put("fechaFin", fechaFinalS);
            if(valorInicial!=null&& valorFinal!=null){
            parametros.put("valini", valorInicial);
            parametros.put("valfin", valorFinal);
            }else{
            parametros.put("valini",0);
            parametros.put("valfin",1000000);   
            }
            
//            DAOComprobanteElectronico asd = new DAOComprobanteElectronico();
//            List<ComprobanteElectronico> ompro = asd.obtenerComprobanteElectronico();
//        reporte = (JasperReport) JRLoader.loadObjectFromFile("C:\\Users\\Carlos\\Documents\\NetBeansProjects\\Prueba\\src\\Reportes\\Zonas.jasper");
//        JasperPrint print = JasperFillManager.fillReport(reporte, null, a);
//            Collection listOfUser = null;
//            JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(ompro);
//           asdasd dddd=new asdasd();
//           for(int i=0;i<ompro.size();i++){
//           dddd.addParticipante(ompro.get(i));
//           }
//            JasperPrint jasperPrint = JasperFillManager.fillReport("C:\\reportesEgastos\\report.jasper", null, a);
   
//pruebas
JasperPrint jasperPrint=null;
    if(valor==1){
    jasperPrint = JasperFillManager.fillReport(Valores.VALOR_REPORTETOTAL, parametros, a);
    }
    if(valor==2){
    jasperPrint = JasperFillManager.fillReport(Valores.VALOR_REPORTESECCION, parametros, a);   
    }
    if(valor==3){
         parametros.put("rucempresa",rucEmpresa);
   jasperPrint = JasperFillManager.fillReport(Valores.VALOR_REPORTEEMPRESA, parametros, a);   
    }
//produccion
//JasperPrint jasperPrint=null;
//    if(valor==1){
//    jasperPrint = JasperFillManager.fillReport("/opt/reportes/report.jasper", parametros, a);
//    }
//    if(valor==2){
//    jasperPrint = JasperFillManager.fillReport("/opt/reportes/reportSecciones.jasper", parametros, a);   
//    }
//    if(valor==3){
//         parametros.put("rucempresa",rucEmpresa);
//   jasperPrint = JasperFillManager.fillReport("/opt/reportes/reportPorEmpresa.jasper", parametros, a);   
//    }
//         


           
           
           
           
           
           
   ///////////////////////////////////////////////////////////////        
//            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
////            httpServletResponse.addHeader("Content-disposition", "attachment; filename=report.pdf");
//           
//            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();   
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            byte[] bites = JasperExportManager.exportReportToPdf(jasperPrint);
            if (bites.length == 919) {

                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No existen Datos Revise Los Filtos De Busqueda");

            } else {
                visualizarRIDE(faces, bites, "mensual");
                FacesContext.getCurrentInstance().responseComplete();
            }

        } catch (JRException ex) {
            Logger.getLogger(beanReportes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(beanReportes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void obtenerFechas() {
      
        Calendar calendario = Calendar.getInstance();
        Calendar calendario_final_actual = Calendar.getInstance();
        fechaActual = calendario_final_actual.getTime();
       
        if (seleccionPeriodoTiempo != null && !seleccionPeriodoTiempo.equals("")) {

            if (seleccionPeriodoTiempo.equals("2")) {
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
                fechaInicialS = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionadaInicio);
                fechaFinalS = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionadaInicio);
            } else if (seleccionPeriodoTiempo.equals("3")) {
                int semana = calendario.get(Calendar.WEEK_OF_YEAR);
                semana--;
                calendario.set(Calendar.WEEK_OF_YEAR, semana);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
                fechaInicialS = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionadaInicio);
            } else if (seleccionPeriodoTiempo.equals("4")) {
                int mes = calendario.get(Calendar.MONTH);
                mes--;
                calendario.set(Calendar.MONTH, mes);
                calendario.set(Calendar.DAY_OF_MONTH, 1);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
                fechaInicialS = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionadaInicio);
                calendario_final_actual.set(Calendar.DAY_OF_MONTH, 1);
                calendario_final_actual.add(Calendar.DAY_OF_MONTH, -1);
                fechaActual = calendario_final_actual.getTime();
                fechaFinalS = new SimpleDateFormat("yyyy-MM-dd").format(fechaActual);

            } else if (seleccionPeriodoTiempo.equals("5")) {
                int mes = calendario.get(Calendar.MONTH);
                mes = mes - 6;
                calendario.set(Calendar.MONTH, mes);
                calendario.set(Calendar.DAY_OF_MONTH, 1);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
                fechaInicialS = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionadaInicio);
            } else if (seleccionPeriodoTiempo.equals("6")) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    fechaInicial = formatter.parse(fechaInicialS);
//                    fechaInicialS = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionadaInicio);
                } catch (ParseException ex) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error Fecha Inicial (dia/mes/anio).");
                }
                try {
                    fechaFinal = formatter.parse(fechaFinalS);
//                    fechaFinalS = new SimpleDateFormat("yyyy-MM-dd").format(fechaFinal);
                } catch (ParseException ex) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error Fecha Final (dia/mes/anio).");
                }
                if (fechaInicial.after(fechaFinal)) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error en rango de fechas (dia/mes/anio).");
                    return;
                }
                fechaSeleccionadaInicio = fechaInicial;
                fechaInicialS = new SimpleDateFormat("yyyy-MM-dd").format(fechaSeleccionadaInicio);
                fechaActual = fechaFinal;
                fechaFinalS = new SimpleDateFormat("yyyy-MM-dd").format(fechaActual);
            } else {
                  fechaInicialS = "2001-01-01";
                 fechaFinalS = new SimpleDateFormat("yyyy-MM-dd").format(fechaActual);
//                fechaSeleccionadaInicio = null;
//                fechaInicialS = "";
//                fechaActual = null;
//                fechaFinalS = "";
            }
        }

    }

    public HttpServletResponse visualizarRIDE(FacesContext _faces, byte[] _archivoByte, String _comprobante) {
        HttpServletResponse response = (HttpServletResponse) _faces.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(_archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + _comprobante + ".pdf");
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(_archivoByte);
            out.flush();
            out.close();
            response.flushBuffer();
        } catch (IOException e) {
        }
        return response;
    }

    public void abrirValores() {
        if (opcionValores.equals("1")) {
            apareceValores = false;
            valorInicial = "";
            valorFinal = "";
        }
        if (opcionValores.equals("2")) {
            apareceValores = false;
            valorInicial = "0";
            valorFinal = "100";

        }
        if (opcionValores.equals("4")) {
            apareceValores = false;
            valorInicial = "100";
            valorFinal = "500";
        }
        if (opcionValores.equals("5")) {
            apareceValores = false;
            valorInicial = "500";
            valorFinal = null;
        }
        if (opcionValores.equals("6")) {
            valorInicial = "0";
            valorFinal = "0";
            apareceValores = true;
        }
    }

    public void abrirDialogRangoFechas() {
        if (seleccionPeriodoTiempo != null) {
            if (seleccionPeriodoTiempo.equals("6")) {
                fechaInicial = new Date();
                fechaInicialS = fechaFormateada(fechaInicial);
                fechaFinal = new Date();
                fechaFinalS = fechaFormateada(fechaFinal);
                apareceRago = true;
            } else {
                seleccionPeriodoTiempo = "1";
                apareceRago = false;
            }
        }
    }

    public String fechaFormateada(Date _fecha) {
        if (_fecha == null) {
            _fecha = new Date();
        }
        return Utilidades.obtenerFechaFormatoddMMyyyy(_fecha);
    }

    public String getOpcionValores() {
        return opcionValores;
    }

    public void setOpcionValores(String opcionValores) {
        this.opcionValores = opcionValores;
    }

    public String getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(String valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getSeleccionPeriodoTiempo() {
        return seleccionPeriodoTiempo;
    }

    public void setSeleccionPeriodoTiempo(String seleccionPeriodoTiempo) {
        this.seleccionPeriodoTiempo = seleccionPeriodoTiempo;
    }

    public boolean isApareceRago() {
        return apareceRago;
    }

    public void setApareceRago(boolean apareceRago) {
        this.apareceRago = apareceRago;
    }

    public String getFechaInicialS() {
        return fechaInicialS;
    }

    public void setFechaInicialS(String fechaInicialS) {
        this.fechaInicialS = fechaInicialS;
    }

    public String getFechaFinalS() {
        return fechaFinalS;
    }

    public void setFechaFinalS(String fechaFinalS) {
        this.fechaFinalS = fechaFinalS;
    }

    public boolean isApareceValores() {
        return apareceValores;
    }

    public void setApareceValores(boolean apareceValores) {
        this.apareceValores = apareceValores;
    }

    public String getRucEmpresa() {
        return rucEmpresa;
    }

    public void setRucEmpresa(String rucEmpresa) {
        this.rucEmpresa = rucEmpresa;
    }

    public String getDetalleEmpresa() {
        return detalleEmpresa;
    }

    public void setDetalleEmpresa(String detalleEmpresa) {
        this.detalleEmpresa = detalleEmpresa;
    }

}
