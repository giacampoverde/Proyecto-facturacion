package com.egastos.modelo.ec;
// Generated 20/06/2017 21:43:07 by Hibernate Tools 4.3.1



/**
 * Codigosverificacion generated by hbm2java
 */
public class Codigosverificacion  implements java.io.Serializable {


     private Integer idcodigosVerificacion;
     private Pagos pagos;
     private String codigoVerifcacion;
     private String estado;

    public Codigosverificacion() {
    }

	
    public Codigosverificacion(Pagos pagos) {
        this.pagos = pagos;
    }
    public Codigosverificacion(Pagos pagos, String codigoVerifcacion, String estado) {
       this.pagos = pagos;
       this.codigoVerifcacion = codigoVerifcacion;
       this.estado = estado;
    }
   
    public Integer getIdcodigosVerificacion() {
        return this.idcodigosVerificacion;
    }
    
    public void setIdcodigosVerificacion(Integer idcodigosVerificacion) {
        this.idcodigosVerificacion = idcodigosVerificacion;
    }
    public Pagos getPagos() {
        return this.pagos;
    }
    
    public void setPagos(Pagos pagos) {
        this.pagos = pagos;
    }
    public String getCodigoVerifcacion() {
        return this.codigoVerifcacion;
    }
    
    public void setCodigoVerifcacion(String codigoVerifcacion) {
        this.codigoVerifcacion = codigoVerifcacion;
    }
    public String getEstado() {
        return this.estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }




}

