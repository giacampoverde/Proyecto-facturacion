/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.comprobanteelectronico.esquema.factura;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @Ricardo Delgado
 */
public class InformacionFactura implements Serializable {

    private String fechaEmision;
    private String dirEstablecimiento;
    private String contribuyenteEspecial;
    private String obligadoContabilidad;
    private String tipoIdentificacionComprador;
    private String guiaRemision;
    private String razonSocialComprador;
    private String identificacionComprador;
    private String direccionComprador;
    private String totalSinImpuestos;
    private String totalDescuento;
    private String propina;
    private String importeTotal;
    private String moneda;
    private String codDocReembolso;
    private String totalComprobantesReembolso;
    private String totalBaseImponibleReembolso;
    private String totalImpuestoReembolso;
    private String comercioExterior;
    private String incoTermFactura;
    private String lugarIncoTerm;
    private String paisOrigen;
    private String puertoEmbarque;
    private String puertoDestino;
    private String paisDestino;
    private String paisAdquisicion;
    private String incoTermTotalSinImpuestos;
    private String fleteInternacional;
    private String seguroInternacional;
    private String gastosAduaneros;
    private String gastosTransporteOtros;
    private List<ImpuestoComprobanteElectronico> TotalConImpuesto;
    private List<Pagos> pagos;

    public List<ImpuestoComprobanteElectronico> getTotalConImpuesto() {
        return TotalConImpuesto;
    }

    public void setTotalConImpuesto(List<ImpuestoComprobanteElectronico> TotalConImpuesto) {
        this.TotalConImpuesto = TotalConImpuesto;
    }

    public String getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(String contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public String getDirEstablecimiento() {
        return dirEstablecimiento;
    }

    public void setDirEstablecimiento(String dirEstablecimiento) {
        this.dirEstablecimiento = dirEstablecimiento;
    }

    public String getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getGuiaRemision() {
        return guiaRemision;
    }

    public void setGuiaRemision(String guiaRemision) {
        this.guiaRemision = guiaRemision;
    }

    public String getIdentificacionComprador() {
        return identificacionComprador;
    }

    public void setIdentificacionComprador(String identificacionComprador) {
        this.identificacionComprador = identificacionComprador;
    }

    public String getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(String importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getPropina() {
        return propina;
    }

    public void setPropina(String propina) {
        this.propina = propina;
    }

    public String getRazonSocialComprador() {
        return razonSocialComprador;
    }

    public void setRazonSocialComprador(String razonSocialComprador) {
        this.razonSocialComprador = razonSocialComprador;
    }

    public String getTipoIdentificacionComprador() {
        return tipoIdentificacionComprador;
    }

    public void setTipoIdentificacionComprador(String tipoIdentificacionComprador) {
        this.tipoIdentificacionComprador = tipoIdentificacionComprador;
    }

    public String getDireccionComprador() {
        return direccionComprador;
    }

    public void setDireccionComprador(String direccionComprador) {
        this.direccionComprador = direccionComprador;
    }        

    public String getTotalDescuento() {
        return totalDescuento;
    }

    public void setTotalDescuento(String totalDescuento) {
        this.totalDescuento = totalDescuento;
    }

    public String getTotalSinImpuestos() {
        return totalSinImpuestos;
    }

    public void setTotalSinImpuestos(String totalSinImpuestos) {
        this.totalSinImpuestos = totalSinImpuestos;
    }

    public String getCodDocReembolso() {
        return codDocReembolso;
    }

    public void setCodDocReembolso(String codDocReembolso) {
        this.codDocReembolso = codDocReembolso;
    }

    public String getTotalComprobantesReembolso() {
        return totalComprobantesReembolso;
    }

    public void setTotalComprobantesReembolso(String totalComprobantesReembolso) {
        this.totalComprobantesReembolso = totalComprobantesReembolso;
    }

    public String getTotalBaseImponibleReembolso() {
        return totalBaseImponibleReembolso;
    }

    public void setTotalBaseImponibleReembolso(String totalBaseImponibleReembolso) {
        this.totalBaseImponibleReembolso = totalBaseImponibleReembolso;
    }

    public String getTotalImpuestoReembolso() {
        return totalImpuestoReembolso;
    }

    public void setTotalImpuestoReembolso(String totalImpuestoReembolso) {
        this.totalImpuestoReembolso = totalImpuestoReembolso;
    }

    public List<Pagos> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pagos> pagos) {
        this.pagos = pagos;
    }

    public String getComercioExterior() {
        return comercioExterior;
    }

    public void setComercioExterior(String comercioExterior) {
        this.comercioExterior = comercioExterior;
    }

    public String getIncoTermFactura() {
        return incoTermFactura;
    }

    public void setIncoTermFactura(String incoTermFactura) {
        this.incoTermFactura = incoTermFactura;
    }

    public String getLugarIncoTerm() {
        return lugarIncoTerm;
    }

    public void setLugarIncoTerm(String lugarIncoTerm) {
        this.lugarIncoTerm = lugarIncoTerm;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getPuertoEmbarque() {
        return puertoEmbarque;
    }

    public void setPuertoEmbarque(String puertoEmbarque) {
        this.puertoEmbarque = puertoEmbarque;
    }

    public String getPuertoDestino() {
        return puertoDestino;
    }

    public void setPuertoDestino(String puertoDestino) {
        this.puertoDestino = puertoDestino;
    }

    public String getPaisDestino() {
        return paisDestino;
    }

    public void setPaisDestino(String paisDestino) {
        this.paisDestino = paisDestino;
    }

    public String getPaisAdquisicion() {
        return paisAdquisicion;
    }

    public void setPaisAdquisicion(String paisAdquisicion) {
        this.paisAdquisicion = paisAdquisicion;
    }

    public String getIncoTermTotalSinImpuestos() {
        return incoTermTotalSinImpuestos;
    }

    public void setIncoTermTotalSinImpuestos(String incoTermTotalSinImpuestos) {
        this.incoTermTotalSinImpuestos = incoTermTotalSinImpuestos;
    }

    public String getFleteInternacional() {
        return fleteInternacional;
    }

    public void setFleteInternacional(String fleteInternacional) {
        this.fleteInternacional = fleteInternacional;
    }

    public String getSeguroInternacional() {
        return seguroInternacional;
    }

    public void setSeguroInternacional(String seguroInternacional) {
        this.seguroInternacional = seguroInternacional;
    }

    public String getGastosAduaneros() {
        return gastosAduaneros;
    }

    public void setGastosAduaneros(String gastosAduaneros) {
        this.gastosAduaneros = gastosAduaneros;
    }

    public String getGastosTransporteOtros() {
        return gastosTransporteOtros;
    }

    public void setGastosTransporteOtros(String gastosTransporteOtros) {
        this.gastosTransporteOtros = gastosTransporteOtros;
    }
    
    
    
    
    
}
