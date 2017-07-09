/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.comprobanteelectronico.esquema.notacredito;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.factura.Detalle;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Ricardo Delgado
 */
public abstract class NotaCreditoEsquema extends ComprobanteElectronico {

    protected InformacionNotaCredito informacionNotaCredito;
    protected List<Detalle> detalles;

    public NotaCreditoEsquema() {
        super();
        informacionNotaCredito = new InformacionNotaCredito();
        detalles = new ArrayList<>();
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    public InformacionNotaCredito getInformacionNotaCredito() {
        return informacionNotaCredito;
    }

    public void setInformacionNotaCredito(InformacionNotaCredito informacionNotaCredito) {
        this.informacionNotaCredito = informacionNotaCredito;
    }
}
