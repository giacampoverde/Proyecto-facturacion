/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.comprobanteelectronico.esquema.comprobantebase;

import java.io.Serializable;

/**
 *
 * @Ricardo Delgado
 */
public class InformacionAdicional implements Serializable {

    private String nombre;
    private String valor;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
