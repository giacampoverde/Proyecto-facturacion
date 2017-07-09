/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.serviciofacturacion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;

import ec.discheca.firma.firmaxades.AccesoCertificado;
import ec.discheca.firma.sri.produccion.FacturacionElectronica;
import ec.discheca.firma.sri.produccion.RespuestaSRI;
import ec.discheca.configuracion.AES256;
import ec.discheca.configuracion.JDOMUtil;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOCertificado;
import ec.discheca.modelo.Certificado;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FirmaAutorizacion {

    public RespuestaSRI firmarYAutorizar(File _archivoComprobanteElectronicoSinFirma, String _directorioDeFirma, String _nombreArchivoFirmado, ComprobanteElectronico _comprobante, String _tipoComprobante) {
        String version = JDOMUtil.obtenerVersionXML(_archivoComprobanteElectronicoSinFirma.getAbsolutePath());
        RespuestaSRI respuesta = null;
        try {
            DAOCertificado dao_certificado = new DAOCertificado();
            //obtenemos el certificado electronico almacenado en la base
            Certificado certificado = dao_certificado.obtenerCertificadoPorRUC(_comprobante.getInformacionTributariaComprobanteElectronico().getRuc());
            FacturacionElectronica facturacion_produccion = new FacturacionElectronica();
            ec.discheca.firma.sri.pruebas.FacturacionElectronica facturacion_pruebas = new ec.discheca.firma.sri.pruebas.FacturacionElectronica();
            ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaPruebas = null;

            if (certificado != null) {
                AccesoCertificado acceso_certificado = new AccesoCertificado(certificado.getRutaCertificado(), AES256.toMessage(certificado.getClaveCertificado()), AES256.toMessage(certificado.getClaveCertificado()));
                boolean existe_acceso = acceso_certificado.accederCertificado();
                if (existe_acceso) {
                    if (Valores.AMBIENTE.equals("1")) {

                        respuestaPruebas = facturacion_pruebas.autorizarComprobantesElectronicos(_archivoComprobanteElectronicoSinFirma.getAbsolutePath(), _directorioDeFirma, _nombreArchivoFirmado, _comprobante, acceso_certificado, _comprobante.getInformacionTributariaComprobanteElectronico().getRuc(), true, version);
                        respuesta = facturacion_pruebas.toRespuestaSRIProduccion(respuestaPruebas);
                    } else {
                        respuesta = facturacion_produccion.autorizarComprobantesElectronicos(_archivoComprobanteElectronicoSinFirma.getAbsolutePath(), _directorioDeFirma, _nombreArchivoFirmado, _comprobante, acceso_certificado, _comprobante.getInformacionTributariaComprobanteElectronico().getRuc(), true, version);
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(FirmaAutorizacion.class.getName()).log(Level.SEVERE, null, e);
        }
        return respuesta;
    }



}
