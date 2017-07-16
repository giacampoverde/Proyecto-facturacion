/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.comprobanteelectronico.construccionxml;

import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionRetencion;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionNotaCredito;
import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import com.egastos.comprobanteelectronico.utilidades.ValoresEstaticos;
import java.io.*;

/**
 *
 * @Ricardo Delgado
 */

public class GeneradorComprobanteElectronicoXML {

    /**
     * Genera un archivo a partir del objeto ComprobanteElectronico enviado
     *
     * @param _codigoTipoComprobante Código del tipo de comprobante que se
     * creará
     * @param _comprobante Objeto tipo ComprobanteElectronico que contiene los
     * datos del archivo
     * @param _direccionCoNombreArchivoEscritura Ruta completa con el nombre de
     * archivo
     * @return Objeto de tipo File que fue creado por la función
     */
    public File comprGeneracion(String _codigoTipoComprobante, ComprobanteElectronico _comprobante, String _direccionCoNombreArchivoEscritura) {
        File comprobante;
        switch (_codigoTipoComprobante) {
            case ValoresEstaticos.FACTURA:
                ImplementacionFactura facXML = (ImplementacionFactura) _comprobante;
                comprobante = facXML.ObjetoComprobanteAXML(_direccionCoNombreArchivoEscritura, "1.0.0");
                break;
            case ValoresEstaticos.NOTA_CREDITO:
                ImplementacionNotaCredito nCredXML = (ImplementacionNotaCredito) _comprobante;
                comprobante = nCredXML.ObjetoComprobanteAXML(_direccionCoNombreArchivoEscritura, "1.0.0");
                break;
            case ValoresEstaticos.COMPROBANTE_RETENCION:
                ImplementacionRetencion cRetXML = _comprobante.ConstruirComprobanteRetencion();
                comprobante = cRetXML.ObjetoComprobanteAXML(_direccionCoNombreArchivoEscritura, "1.0.0");
                break;
            default:
                comprobante = null;
                break;
        }
        return comprobante;
    }

    /**
     * Método que construye un archivo a partir de un objeto
     * ComprobanteElectronico
     *
     * @param _comprobante Objeto con los datos del comprobante , de tipo
     * ComprobanteElectronico
     * @param _direccionCoNombreArchivoEscritura Directorio donde se creará el
     * archivo
     * @param _version Versión del XML 1.1.0 o 1.0.0
     * @return Objeto de tipo File con la información del comprobante
     */
    public File genArchivoObjComprobante(ComprobanteElectronico _comprobante, String _direccionCoNombreArchivoEscritura, String _version) {
        File comprobante = null;
        String identificador = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        switch (identificador) {
            case ValoresEstaticos.FACTURA:
                ImplementacionFactura facXML = (ImplementacionFactura) _comprobante;
                comprobante = facXML.ObjetoComprobanteAXML(_direccionCoNombreArchivoEscritura + ".xml", _version);
                break;
            case ValoresEstaticos.NOTA_CREDITO:
                ImplementacionNotaCredito nCredXML = (ImplementacionNotaCredito) _comprobante;
                comprobante = nCredXML.ObjetoComprobanteAXML(_direccionCoNombreArchivoEscritura + ".xml", _version);
                break;
            case ValoresEstaticos.COMPROBANTE_RETENCION:
                ImplementacionRetencion cRetXML = _comprobante.ConstruirComprobanteRetencion();
                comprobante = cRetXML.ObjetoComprobanteAXML(_direccionCoNombreArchivoEscritura + ".xml", _version);
                break;
            default:
                comprobante = null;
                break;
        }
        return comprobante;
    }

}
