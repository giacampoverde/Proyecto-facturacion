/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.utilidades;


import discheca.utilidades.TransformadorArchivos;
import ec.discheca.pdfride.PDF;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.firma.sri.produccion.RespuestaSRI;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOClienteEmpresa;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;

public class RIDE {
    public HttpServletResponse visualizarRIDE(FacesContext _faces, byte[] _archivoByte, ComprobanteElectronico _comprobante) {
        HttpServletResponse response = (HttpServletResponse) _faces.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(_archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + _comprobante.getInformacionTributariaComprobanteElectronico().getRuc() + " " + _comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso() + ".pdf");
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(_archivoByte);
            out.flush();
            out.close();
            response.flushBuffer();
        } catch (IOException e) {
        }
        return response;
    }

    /**
     *
     * @param _faces
     * @param _archivoByte
     * @param _comprobante
     * @return
     */
    public HttpServletResponse visualizarRIDE(FacesContext _faces, byte[] _archivoByte, ec.discheca.modelo.ComprobanteElectronico _comprobante) {
        HttpServletResponse response = (HttpServletResponse) _faces.getExternalContext().getResponse();
        response.setContentType("application/pdf");
        response.setContentLength(_archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + _comprobante.getRucEmisorComprobanteElectronico() + " " + _comprobante.getClaveAccesoComprobanteElectronico() + ".pdf");
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(_archivoByte);
            out.flush();
            out.close();
            response.flushBuffer();
        } catch (IOException e) {
        }
        return response;
    }

    /**
     *
     * @param _faces
     * @param _archivoByte
     * @param _nombreArchivo
     * @return
     */
    public HttpServletResponse bajarArchivoRespuesta(FacesContext _faces, byte[] _archivoByte, String _nombreArchivo) {

        HttpServletResponse response = (HttpServletResponse) _faces.getExternalContext().getResponse();
        response.setContentType("application/x-download");
        response.setContentLength(_archivoByte.length);
        response.setHeader("Content-disposition", "inline; filename=" + _nombreArchivo);
        try {
            ServletOutputStream out;
            out = response.getOutputStream();
            out.write(_archivoByte);
            out.flush();
            out.close();
            response.flushBuffer();
        } catch (IOException e) {
        }

        return response;
    }

    public byte[] construirPDFRIDE(ec.discheca.modelo.ComprobanteElectronico _comprobanteElectronico) {
        DAOClienteEmpresa dao_cliente_empresa = null;
        byte[] logo_empresa = null;
        PDF pdf_ride = new PDF();
        byte[] ride = null;

        try {
            dao_cliente_empresa = new DAOClienteEmpresa();

            logo_empresa = dao_cliente_empresa.obtenerLogoClienteEmpresaPorId(_comprobanteElectronico.getRucEmisorComprobanteElectronico());
            ComprobanteElectronico c = TransformadorArchivos.byteCompr(_comprobanteElectronico.getComprobanteFirmadoComprobanteElectronico(), Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS + RandomStringUtils.randomAlphanumeric(10));
            ride = pdf_ride.construirPDFComprobante(c, logo_empresa, _comprobanteElectronico.getNumeroAutorizacionComprobanteElectronico(), _comprobanteElectronico.getFechaAutorizacionComprobanteElectronico());

        } catch (Exception ex) {
            Logger.getLogger(RIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ride;
    }

    public byte[] construirPDFRIDE(RespuestaSRI _respuestaSRI, ComprobanteElectronico c) {
        DAOClienteEmpresa dao_cliente_empresa = null;
        byte[] logo_empresa = null;
        PDF pdf_ride = new PDF();
        byte[] ride = null;

        try {
            dao_cliente_empresa = new DAOClienteEmpresa();
          
            logo_empresa = dao_cliente_empresa.obtenerLogoClienteEmpresaPorId(c.getInformacionTributariaComprobanteElectronico().getRuc());

            ride = pdf_ride.construirPDFComprobante(c, logo_empresa, _respuestaSRI.getNumeroAutorizacion(), _respuestaSRI.getFechaAutorizacion().getTime());

        } catch (Exception ex) {
            Logger.getLogger(RIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ride;
    }

    public byte[] construirPDFRIDEContingencia(ComprobanteElectronico c) {
        DAOClienteEmpresa dao_cliente_empresa = null;
        byte[] logo_empresa = null;
        PDF pdf_ride = new PDF();
        byte[] ride = null;

        try {
            dao_cliente_empresa = new DAOClienteEmpresa();
            ControlSesion ms = new ControlSesion();
            logo_empresa = dao_cliente_empresa.obtenerLogoClienteEmpresaPorId(ms.obtenerRUCEmpresaSesionActiva());

            ride = pdf_ride.construirPDFComprobante(c, logo_empresa, c.getInformacionTributariaComprobanteElectronico().getClaveAcceso(), new Date());

        } catch (Exception ex) {
            Logger.getLogger(RIDE.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ride;
    }
}
