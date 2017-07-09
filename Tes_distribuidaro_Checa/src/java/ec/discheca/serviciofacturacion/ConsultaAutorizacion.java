/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.serviciofacturacion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;

import ec.discheca.firma.sri.produccion.FacturacionElectronica;
import ec.discheca.firma.sri.produccion.RespuestaSRI;
import ec.discheca.configuracion.Valores;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConsultaAutorizacion {

    public RespuestaSRI consulta(ComprobanteElectronico _comprobante) {

        RespuestaSRI respuesta = null;
        try {

            FacturacionElectronica facturacion_produccion = new FacturacionElectronica();
            ec.discheca.firma.sri.pruebas.FacturacionElectronica facturacion_pruebas = new ec.discheca.firma.sri.pruebas.FacturacionElectronica();
            ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaPruebas = null;

            if (Valores.AMBIENTE.equals("1")) {

                respuestaPruebas = facturacion_pruebas.ConsultarComrpobanteElectronico(_comprobante);
                respuesta = facturacion_pruebas.toRespuestaSRIProduccion(respuestaPruebas);
            } else {
                respuesta = facturacion_produccion.ConsultaComrpobanteElectronico(_comprobante);

            }

        } catch (Exception e) {
            Logger.getLogger(ConsultaAutorizacion.class.getName()).log(Level.SEVERE, null, e);
        }
        return respuesta;
    }

    public RespuestaSRI consultaClaveAcceo(String claveAcceso, int ambiente) {
       String ambiente2=ambiente+"";
        RespuestaSRI respuesta = null;
        try {

            FacturacionElectronica facturacion_produccion = new FacturacionElectronica();
            ec.discheca.firma.sri.pruebas.FacturacionElectronica facturacion_pruebas = new ec.discheca.firma.sri.pruebas.FacturacionElectronica();
            ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaPruebas = null;

            if (ambiente2.equals("1")) {

                respuestaPruebas = facturacion_pruebas.ConsultarComrpobanteClaveAcceso(claveAcceso);
                respuesta = facturacion_pruebas.toRespuestaSRIProduccion(respuestaPruebas);
            } else {
                respuesta = facturacion_produccion.ConsultaComrpobanteclaveAcceso(claveAcceso);

            }

        } catch (Exception e) {
            Logger.getLogger(ConsultaAutorizacion.class.getName()).log(Level.SEVERE, null, e);
        }
        return respuesta;
    }

}
