/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.firma.sri.produccion;

import com.wspro.autorizacioncomprobantes.Autorizacion;
import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
* @Ricardo Delgado
 */
public class RespuestaSRI implements Serializable {

    private String claveAccesoConsultada;
    private String numeroComprobantes;
    private String estado;
    private String numeroAutorizacion;
    private GregorianCalendar fechaAutorizacion;
    private String ambiente;
    private String comprobante;//Comprobante electrónico en String
    private String numeroIdentificadorMensaje;
    private String mensaje;
    private String informacionAdicionalMensaje;//Información del mensaje
    private String tipoMensaje; //Tipos de Errores,Advertencia,Información   
    private List<Autorizacion> autorizaciones;
    private Autorizacion autorizacionObtenida;
    private String nombreArchivo;

    public String getNumeroComprobantes() {
        return numeroComprobantes;
    }

    public void setNumeroComprobantes(String numeroComprobantes) {
        this.numeroComprobantes = numeroComprobantes;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getClaveAccesoConsultada() {
        return claveAccesoConsultada;
    }

    public void setClaveAccesoConsultada(String claveAccesoConsultada) {
        this.claveAccesoConsultada = claveAccesoConsultada;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public GregorianCalendar getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(GregorianCalendar fechaAutorizacion) {
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getNumeroIdentificadorMensaje() {
        return numeroIdentificadorMensaje;
    }

    public void setNumeroIdentificadorMensaje(String identificador) {
        this.numeroIdentificadorMensaje = identificador;
    }

    public String getInformacionAdicional() {
        return informacionAdicionalMensaje;
    }

    public void setInformacionAdicional(String informacionAdicional) {
        this.informacionAdicionalMensaje = informacionAdicional;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNumeroAutorizacion() {
        return numeroAutorizacion;
    }

    public void setNumeroAutorizacion(String numeroAutorizacion) {
        this.numeroAutorizacion = numeroAutorizacion;
    }

    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipo) {
        this.tipoMensaje = tipo;
    }

    public String getInformacionAdicionalMensaje() {
        return informacionAdicionalMensaje;
    }

    public void setInformacionAdicionalMensaje(String informacionAdicionalMensaje) {
        this.informacionAdicionalMensaje = informacionAdicionalMensaje;
    }

    public List<Autorizacion> getAutorizaciones() {
        return autorizaciones;
    }

    public void setAutorizaciones(List<Autorizacion> autorizaciones) {
        this.autorizaciones = autorizaciones;
    }

    public Autorizacion getAutorizacionObtenida() {
        return autorizacionObtenida;
    }

    public void setAutorizacionObtenida(Autorizacion autorizacionObtenida) {
        this.autorizacionObtenida = autorizacionObtenida;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

}
