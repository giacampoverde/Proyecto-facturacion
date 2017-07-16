/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.comprobanteelectronico.esquema.factura;

import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Ricardo Delgado
 */
public abstract class FormatoFactura extends ComprobanteElectronico {

    protected InformacionFactura informacionFactura;
    protected List<Detalle> detalles;
    public FormatoFactura() {
        super();
        this.informacionFactura = new InformacionFactura();
        this.detalles = new ArrayList<>();  
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    public InformacionFactura getInformacionFactura() {
        return informacionFactura;
    }

    public void setInformacionFactura(InformacionFactura informacionFactura) {
        this.informacionFactura = informacionFactura;
    }     
    
}
