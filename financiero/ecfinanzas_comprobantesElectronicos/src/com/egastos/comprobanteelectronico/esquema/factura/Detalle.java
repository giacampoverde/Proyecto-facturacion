/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.comprobanteelectronico.esquema.factura;

import com.egastos.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import com.egastos.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @Ricardo Delgado
 */
public class Detalle implements Serializable {

    private String codigoPrincipal;
    private String codigoAuxiliar;
    private String descripcion;
    private String cantidad;
    private String precioUnitario;
    private String descuento;
    private String precioTotalSinImpuesto;
    private List<ImpuestoComprobanteElectronico> impuestos;
    private List<InformacionAdicional> detallesAdicionales;

    public List<ImpuestoComprobanteElectronico> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<ImpuestoComprobanteElectronico> impuestos) {
        this.impuestos = impuestos;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoAuxiliar() {
        return codigoAuxiliar;
    }

    public void setCodigoAuxiliar(String codigoAuxiliar) {
        this.codigoAuxiliar = codigoAuxiliar;
    }

    public String getCodigoPrincipal() {
        return codigoPrincipal;
    }

    public void setCodigoPrincipal(String codigoPrincipal) {
        this.codigoPrincipal = codigoPrincipal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getPrecioTotalSinImpuesto() {
        return precioTotalSinImpuesto;
    }

    public void setPrecioTotalSinImpuesto(String precioTotalSinImpuesto) {
        this.precioTotalSinImpuesto = precioTotalSinImpuesto;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public List<InformacionAdicional> getDetallesAdicionales() {
        return detallesAdicionales;
    }

    public void setDetallesAdicionales(List<InformacionAdicional> detallesAdicionales) {
        this.detallesAdicionales = detallesAdicionales;
    }

}
