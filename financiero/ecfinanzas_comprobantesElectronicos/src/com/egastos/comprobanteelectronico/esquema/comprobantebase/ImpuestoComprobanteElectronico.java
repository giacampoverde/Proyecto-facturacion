package com.egastos.comprobanteelectronico.esquema.comprobantebase;

import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @Ricardo Delgado
 */
public class ImpuestoComprobanteElectronico implements Serializable {

    private String codigo;
    private String codigoPorcentaje;
    private String baseImponible;
    private String tarifa;
    private String valor;

    public ImpuestoComprobanteElectronico(String codigo, String codigoPorcentaje, String baseImponible, String tarifa, String valor) {
        this.codigo = codigo;
        this.codigoPorcentaje = codigoPorcentaje;
        this.baseImponible = baseImponible;
        this.tarifa = tarifa;
        this.valor = valor;
    }

    public ImpuestoComprobanteElectronico() {
    }

    public String getBaseImponible() {
        return baseImponible;
    }

    public void setBaseImponible(String baseImponible) {
        this.baseImponible = baseImponible;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigoPorcentaje() {
        return codigoPorcentaje;
    }

    public void setCodigoPorcentaje(String codigoPorcentaje) {
        this.codigoPorcentaje = codigoPorcentaje;
    }

    public String getTarifa() {
        return tarifa;
    }

    public void setTarifa(String tarifa) {
        this.tarifa = tarifa;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
