/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOAsignacionComprobanteElectronico;
import com.egastos.dao.ec.DAOReceptor;
import com.egastos.modelo.ec.AsignacionComprobanteElectronico;
import com.egastos.modelo.ec.Receptor;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.MensajesPrimefaces;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 * @author ricar
 */
@ManagedBean
@ViewScoped
public class BeanImpuestoRenta implements Serializable {

    public Double sueldo;
    private String periodoFiscal;
     private double porcetajeIess ;
      private double   ingresomensualneto;
       private double  ingresoAnual;
       private double  baseImponible;

    public void impuestoRenta() {
        ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
        try {
    
            DAOAsignacionComprobanteElectronico obtenerComprobantes = new DAOAsignacionComprobanteElectronico();
            if (!periodoFiscal.equals("")) {

                List<AsignacionComprobanteElectronico> comprobanteselectronicosVivienda = obtenerComprobantes.obtenerComprobantesElectronicosAutorizadosRecibidosPoranoSeccion(ms.obtenerRUCEmpresaSesionActiva(), periodoFiscal, "VIVIENDA");
                double vivienda = 0.0;
                if (comprobanteselectronicosVivienda.size() > 0) {

                    for (int i = 0; i < comprobanteselectronicosVivienda.size(); i++) {
                        vivienda = vivienda + comprobanteselectronicosVivienda.get(i).getComprobanteElectronico().getValorTotalFacturaComprobanteElectronico();

                    }
                } else {
                    vivienda = 0.0;
                }
                List<AsignacionComprobanteElectronico> comprobanteselectronicosEducacion = obtenerComprobantes.obtenerComprobantesElectronicosAutorizadosRecibidosPoranoSeccion(ms.obtenerRUCEmpresaSesionActiva(), periodoFiscal, "EDUCACION");
                double educacion = 0.0;
                if (comprobanteselectronicosEducacion.size() > 0) {

                    for (int i = 0; i < comprobanteselectronicosEducacion.size(); i++) {
                        educacion = educacion + comprobanteselectronicosEducacion.get(i).getComprobanteElectronico().getValorTotalFacturaComprobanteElectronico();

                    }
                } else {
                    educacion = 0.0;
                }
                List<AsignacionComprobanteElectronico> comprobanteselectronicosAlimentacion = obtenerComprobantes.obtenerComprobantesElectronicosAutorizadosRecibidosPoranoSeccion(ms.obtenerRUCEmpresaSesionActiva(), periodoFiscal, "ALIMENTACION");
                double alimentacion = 0.0;
                if (comprobanteselectronicosAlimentacion.size() > 0) {

                    for (int i = 0; i < comprobanteselectronicosAlimentacion.size(); i++) {
                        alimentacion = alimentacion + comprobanteselectronicosAlimentacion.get(i).getComprobanteElectronico().getValorTotalFacturaComprobanteElectronico();

                    }
                } else {
                    alimentacion = 0.0;
                }
                List<AsignacionComprobanteElectronico> comprobanteselectronicosVestimenta = obtenerComprobantes.obtenerComprobantesElectronicosAutorizadosRecibidosPoranoSeccion(ms.obtenerRUCEmpresaSesionActiva(), periodoFiscal, "VESTIMENTA");
                double vestimenta = 0.0;
                if (comprobanteselectronicosVestimenta.size() > 0) {

                    for (int i = 0; i < comprobanteselectronicosVestimenta.size(); i++) {
                        vestimenta = vestimenta + comprobanteselectronicosVestimenta.get(i).getComprobanteElectronico().getValorTotalFacturaComprobanteElectronico();

                    }
                } else {
                    vestimenta = 0.0;
                }
                List<AsignacionComprobanteElectronico> comprobanteselectronicosSalud = obtenerComprobantes.obtenerComprobantesElectronicosAutorizadosRecibidosPoranoSeccion(ms.obtenerRUCEmpresaSesionActiva(), periodoFiscal, "SALUD");
                double salud = 0.0;
                if (comprobanteselectronicosSalud.size() > 0) {

                    for (int i = 0; i < comprobanteselectronicosSalud.size(); i++) {
                        salud = salud + comprobanteselectronicosSalud.get(i).getComprobanteElectronico().getValorTotalFacturaComprobanteElectronico();

                    }
                } else {
                    salud = 0.0;
                }
                  if(vivienda>3630.25){
                      vivienda=3630.25;
                  }
                  if(educacion>3630.25){
                      educacion=3630.25;
                  }
                  if(alimentacion>3630.25){
                      alimentacion=3630.25;
                  }
                  if(vestimenta>3630.25){
                      vestimenta=3630.25;
                  }
                  if(salud>14521){
                      salud=14521;
                  }
                  double sumatoria = (double) Math.round((salud + vivienda + educacion + alimentacion + vestimenta) * 100) / 100;
                double resultado = calculardatos(((double) Math.round((sueldo) * 100) / 100), sumatoria);
                String diaPago=diadePago(ms.obtenerRUCEmpresaSesionActiva());
                System.out.println("Dia de Pago "+diaPago);
                generarReporte(ms.obtenerRUCEmpresaSesionActiva(),""+periodoFiscal,""+((double) Math.round((sueldo) * 100) / 100),""+((double) Math.round((porcetajeIess) * 100) / 100),""+((double) Math.round((ingresomensualneto) * 100) / 100),""+((double) Math.round((ingresoAnual) * 100) / 100),""+sumatoria,""+((double) Math.round((baseImponible) * 100) / 100),""+((double) Math.round((resultado) * 100) / 100),""+diaPago);
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe ingresar un periodo fiscal.");
            }

        } catch (Exception ex) {
            Logger.getLogger(BeanImpuestoRenta.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void generarReporte(String cedula,String PeriodoFiscal,String sueldoMensual,String aporteIess,String ingresoMensualNeto,String ingresonetoanual,String gastosDeducibles,String baseImponible,String totalPagar,String fechaPago){
        try {
            
            FacesContext faces = FacesContext.getCurrentInstance();
            Connection a;
            //pruebas
           // a = DriverManager.getConnection("jdbc:mysql://localhost:3306/egastos?zeroDateTimeBehavior=convertToNull", "root", "root");
           a = DriverManager.getConnection("jdbc:mysql://181.215.96.132:3306/egastos?zeroDateTimeBehavior=convertToNull", "E%finanz@s", "E%finanz@s2017");
           
           ControlSesion sesion = new ControlSesion();
            DAOReceptor receptor=new DAOReceptor();
            Receptor rescep = receptor.obtenerReceptorPorIdentificacion(sesion.obtenerRUCEmpresaSesionActiva());
            Map<String, Object> parametros = new HashMap<String, Object>();
            parametros.put("cedula",rescep.getIdReceptor());
            parametros.put("periodo",PeriodoFiscal);
             parametros.put("sueldo",sueldoMensual);
             parametros.put("aporteIess",aporteIess);
             parametros.put("ingresoMuensual",ingresoMensualNeto);
             parametros.put("ingresoanual",ingresonetoanual);
             parametros.put("gastosdeducibles",gastosDeducibles);
             parametros.put("baseimponible",baseImponible);
             parametros.put("totalPagar",totalPagar);
             parametros.put("fechaPago",fechaPago);
             parametros.put("cedulaComple",cedula);
            JasperPrint jasperPrint=null;
             JasperPrint jasperPrint2=null;
            //pruebas
//            jasperPrint = JasperFillManager.fillReport("C:\\reportesEgastos\\Renta.jasper", parametros, a);
                //pro
              jasperPrint = JasperFillManager.fillReport("/opt/reportes/Renta.jasper", parametros, a);
              byte[] bites = JasperExportManager.exportReportToPdf(jasperPrint);
            if (bites.length == 919) {
                
//              jasperPrint2 = JasperFillManager.fillReport("C:\\reportesEgastos\\Renta2.jasper", parametros);
//              jasperPrint2 = JasperFillManager.fillReport("C:\\reportesEgastos\\Renta2.jasper", parametros);
//                byte[] bites2 = JasperExportManager.exportReportToPdf(jasperPrint2);
//                visualizarRIDE(faces, bites2, "ImpuestoRenta");
                FacesContext.getCurrentInstance().responseComplete();
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No existen Datos Revise Los Filtos De Busqueda");

            } else {
                visualizarRIDE(faces, bites, "ImpuestoRenta");
                FacesContext.getCurrentInstance().responseComplete();
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanImpuestoRenta.class.getName()).log(Level.SEVERE, null, ex);
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

    public String diadePago(String ruc){
        String fecha = null;
        if(ruc.length()==10){
            ruc+="001";
        }
       
        String resultado = ruc.substring(8, 9);
        if(resultado.equals("1")){
            fecha="10 de marzo";
        }
        if(resultado.equals("2")){
            fecha="12 de marzo";
        }
        if(resultado.equals("3")){
            fecha="14 de marzo";
        }
        if(resultado.equals("4")){
            fecha="16 de marzo";
        }
        if(resultado.equals("5")){
            fecha="18 de marzo";
        }
        if(resultado.equals("6")){
           fecha="20 de marzo"; 
        }
        if(resultado.equals("7")){
            fecha="22 de marzo";
        }
        if(resultado.equals("8")){
           fecha="24 de marzo"; 
        }
        if(resultado.equals("9")){
           fecha="26 de marzo"; 
        }
        if(resultado.equals("0")){
           fecha="28 de marzo"; 
        }
        return fecha;
    }
    public double  calculardatos(double sueldo, double deducible) {
         porcetajeIess =sueldo* (9.45 / 100);
         ingresomensualneto = (sueldo - ((double) Math.round((porcetajeIess) * 100) / 100));
         ingresoAnual = (((double) Math.round((ingresomensualneto) * 100) / 100) * 12);
         baseImponible = (((double) Math.round((ingresoAnual) * 100) / 100) - deducible);
        double impuestoFraccionBasica = 0;
        double impuestoFraccionExcendete = 0;
        double minimo = 0;
        double maximo=0;
        if (baseImponible >= 0 && baseImponible < 11290) {
            minimo=0;
            maximo=11290;
            impuestoFraccionBasica = 0;
            impuestoFraccionExcendete = 0;
        }
        if (baseImponible >= 11290 && baseImponible < 14390) {
            minimo=11290;
            maximo=14390;
            impuestoFraccionBasica = 0;
            impuestoFraccionExcendete = (new Double(5)/new Double(100));
        }
        if (baseImponible >= 14390 && baseImponible < 17990) {
            minimo=14390;
            maximo=17990;
            impuestoFraccionBasica = 155;
            impuestoFraccionExcendete = (new Double(10)/new Double(100));
        }
        if (baseImponible >= 17990 && baseImponible < 21600) {
            minimo=17990;
            maximo=21600;
            impuestoFraccionBasica = 515;
            impuestoFraccionExcendete = (new Double(12)/new Double(100));
        }
        if (baseImponible >= 21600 && baseImponible < 43190) {
            minimo=21600;
            maximo=43190;
            impuestoFraccionBasica = 948;
            impuestoFraccionExcendete = (new Double(15)/new Double(100));
        }
        if (baseImponible >= 43190 && baseImponible < 64770) {
            minimo=43190;
            maximo=64770;
            impuestoFraccionBasica = 4187;
            impuestoFraccionExcendete = (new Double(20)/new Double(100));
        }
        if (baseImponible >= 64770 && baseImponible < 86370) {
             minimo=64770;
            maximo=86370;
            impuestoFraccionBasica = 8503;
            impuestoFraccionExcendete = (new Double(25)/new Double(100));
        }
        if (baseImponible >= 86370 && baseImponible < 115140) {
            minimo=86370;
            maximo=115140;
            impuestoFraccionBasica = 13903;
            impuestoFraccionExcendete = (new Double(30)/new Double(100));
        }
        if (baseImponible >= 115140) {
            minimo=115140;
            impuestoFraccionBasica = 22534;
            impuestoFraccionExcendete = (new Double(35)/new Double(100));
        }
            double resultado=(((double) Math.round((baseImponible) * 100) / 100) -minimo);
            double resultado2=resultado*impuestoFraccionExcendete;
            double resultadofinal=impuestoFraccionBasica+((double) Math.round((resultado2) * 100) / 100);
            System.out.println("impuesto a la renta "+((double) Math.round((resultadofinal) * 100) / 100));
            return resultadofinal;
    }

    public Double getSueldo() {
        return sueldo;
    }

    public void setSueldo(Double sueldo) {
        this.sueldo = sueldo;
    }

    public String getPeriodoFiscal() {
        return periodoFiscal;
    }

    public void setPeriodoFiscal(String periodoFiscal) {
        this.periodoFiscal = periodoFiscal;
    }

}
