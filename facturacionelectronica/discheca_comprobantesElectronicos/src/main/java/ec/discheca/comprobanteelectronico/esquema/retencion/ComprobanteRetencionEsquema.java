/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.comprobanteelectronico.esquema.retencion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Ricardo Delgado
 */
public abstract class ComprobanteRetencionEsquema extends ComprobanteElectronico {

    protected InformacionComprobanteRetencion informacionComprobanteRetencion;
    protected List<ImpuestoRetencion> impuestos;

    public ComprobanteRetencionEsquema() {
        super();
        informacionComprobanteRetencion = new InformacionComprobanteRetencion();
        impuestos = new ArrayList<>();
    }

    public List<ImpuestoRetencion> getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(List<ImpuestoRetencion> impuestos) {
        this.impuestos = impuestos;
    }

    public InformacionComprobanteRetencion getInformacionComprobanteRetencion() {
        return informacionComprobanteRetencion;
    }

    public void setInformacionComprobanteRetencion(InformacionComprobanteRetencion infoComprRetencion) {
        this.informacionComprobanteRetencion = infoComprRetencion;
    }
}
