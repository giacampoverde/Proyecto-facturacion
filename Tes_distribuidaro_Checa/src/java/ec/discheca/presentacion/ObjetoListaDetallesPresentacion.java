/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import ec.discheca.comprobanteelectronico.esquema.factura.Detalle;


public class ObjetoListaDetallesPresentacion {

    private Detalle detalleComprobante;
    private Integer identificador;

    public ObjetoListaDetallesPresentacion() {
    }

    public ObjetoListaDetallesPresentacion(Detalle detalleComprobante, Integer identificador) {
        this.detalleComprobante = detalleComprobante;
        this.identificador = identificador;
    }

    public Detalle getDetalleComprobante() {
        return detalleComprobante;
    }

    public void setDetalleComprobante(Detalle detalleComprobante) {
        this.detalleComprobante = detalleComprobante;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public void setIdentificador(Integer identificador) {
        this.identificador = identificador;
    }
}
