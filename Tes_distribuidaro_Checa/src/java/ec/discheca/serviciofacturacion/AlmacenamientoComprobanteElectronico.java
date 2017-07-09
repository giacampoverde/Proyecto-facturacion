/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.serviciofacturacion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.factura.Detalle;
import ec.discheca.firma.sri.produccion.ConsultaComprobantePro;
import ec.discheca.firma.sri.produccion.FacturacionElectronica;
import ec.discheca.firma.sri.produccion.RespuestaSRI;
import ec.discheca.configuracion.HibernateSessionHandler;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAsignacionComprobanteElectronico;
import ec.discheca.dao.DAOComprobanteElectronico;
import ec.discheca.dao.DAOComprobanteElectronicoPendiente;
import ec.discheca.dao.DAODetalle;
import ec.discheca.dao.DAOReceptor;
import ec.discheca.dao.DAOTipoComprobanteElectronico;
import ec.discheca.modelo.AsignacionComprobanteElectronico;
import ec.discheca.modelo.Receptor;
import ec.discheca.modelo.TipoComprobanteElectronico;

import discheca.utilidades.TransformadorArchivos;
import discheca.utilidades.Utilidades;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.RandomStringUtils;

public class AlmacenamientoComprobanteElectronico {

    private Date fecha_autorizacion;
    private Date fecha_emision;
    private String ambiente;
    private String razon_social_emisor;
    private String nombre_comercial_emisor;
    private String estado;
    private String ruc_emisor;
    private String clave_acceso;
    private String establecimiento;
    private String punto_emision;
    private String secuencial;
    private String numero_autorizacion;
    private String importe_total;

    private String ruc_receptor;
    private String razon_social_receptor;
    private TipoComprobanteElectronico tipo_comprobante_electronico;

    public boolean guardarComprobanteElectronicoPendiente(ec.discheca.firma.sri.produccion.RespuestaSRI _respuestaSRI, ComprobanteElectronico _comprobante,
            String _estado, String _mensaje, String _direccionArchivoFirmado) {

        boolean guardado_pendiente = false;
        this.llenarValores(_respuestaSRI, _comprobante);
        DAOComprobanteElectronico dao_comprobante_electronico = null;
        ec.discheca.modelo.ComprobanteElectronico ce_guardado = null;

        try {
            dao_comprobante_electronico = new DAOComprobanteElectronico();
            ce_guardado = dao_comprobante_electronico.guardarComprobanteElectronicoPendiente(clave_acceso, establecimiento, punto_emision, secuencial, nombre_comercial_emisor, razon_social_emisor, ruc_emisor, ambiente, estado, _mensaje, fecha_emision, importe_total, tipo_comprobante_electronico);
        } catch (Exception ex) {
            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ce_guardado != null) {

            /**
             * Si el comprobante es una factura se guardan los detalles
             */
            DAODetalle dao_detalle = null;
            try {
                dao_detalle = new DAODetalle();
            } catch (Exception ex) {
                Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
                List<Detalle> detalles = _comprobante.ConstruirFactura().getDetalles();
                if (detalles != null && !detalles.isEmpty()) {
                    for (Detalle d : detalles) {

                        try {

                            dao_detalle.guardarDetalle(d.getCodigoPrincipal(), d.getDescripcion(), d.getCantidad(), d.getPrecioUnitario(), ce_guardado);
                        } catch (Exception ex) {
                            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            Receptor r = null;
            DAOReceptor dao_receptor = null;
            try {
                dao_receptor = new DAOReceptor();
                r = dao_receptor.obtenerReceptorPorIdentificacion(ruc_receptor);
                if (r == null) {
                    r = dao_receptor.guardarReceptor(ruc_receptor, razon_social_receptor);
                }
            } catch (Exception ex) {
                Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
            }
            DAOAsignacionComprobanteElectronico dao_asignacion_ce = null;
            AsignacionComprobanteElectronico asignacion_ce_guardado = null;
            try {
                dao_asignacion_ce = new DAOAsignacionComprobanteElectronico();
                asignacion_ce_guardado = dao_asignacion_ce.guardarAsignacionComprobanteElectronico(ce_guardado, r);
            } catch (Exception ex) {
                Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (asignacion_ce_guardado != null) {

                DAOComprobanteElectronicoPendiente dao_cep = null;
                try {
                    dao_cep = new DAOComprobanteElectronicoPendiente();
                    guardado_pendiente = dao_cep.guardarComprobantePendiente(ce_guardado, new Date(), _direccionArchivoFirmado, _mensaje, _estado);

                } catch (Exception ex) {
                    Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return guardado_pendiente;

    }

    public boolean guardarComprobanteElectronico(ec.discheca.firma.sri.produccion.RespuestaSRI _respuestaSRI,
            ComprobanteElectronico _comprobanElectronico, String _tipo, String _directorio_factura_hoy_hora_min_segs, String nombre
    ) {

        boolean guardado_comprobante_electronico_bd = false;
        ArchivoRespuestaSRI archivo_respuesta = new ArchivoRespuestaSRI();
        archivo_respuesta.generarArchivoRespuesta(_respuestaSRI, _directorio_factura_hoy_hora_min_segs + "R_" + _respuestaSRI.getClaveAccesoConsultada() + "-" + nombre + ".xml");

        this.llenarValores(_respuestaSRI, _comprobanElectronico);

        byte[] comprobante_bytes = null;
        byte[] respuesta_byte = null;
        try {
            comprobante_bytes = _respuestaSRI.getComprobante().getBytes("UTF-8");
            respuesta_byte = TransformadorArchivos.archArrayB(new File(_directorio_factura_hoy_hora_min_segs + "R_" + _respuestaSRI.getClaveAccesoConsultada() + "-" + nombre + ".xml"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
        }

        String observacion = "";
        if (_respuestaSRI.getEstado().equals("1")) {
            observacion = "AUTORIZADO ";
        } else {
            observacion = "NO AUTORIZADO ";
        }
        if (_respuestaSRI.getMensaje() != null) {
            observacion += _respuestaSRI.getMensaje();
            if (_respuestaSRI.getInformacionAdicional() != null && !_respuestaSRI.getInformacionAdicional().equals("")) {
                observacion += _respuestaSRI.getInformacionAdicional();
            }
        }
        DAOComprobanteElectronico dao_comprobante_electronico = null;
        ec.discheca.modelo.ComprobanteElectronico ce_guardado = null;

        try {
            dao_comprobante_electronico = new DAOComprobanteElectronico();
            // solo almacena comprobanes autorizados
            if (_respuestaSRI.getEstado().equals("1")||_respuestaSRI.getEstado().equals("2")) {
                ce_guardado = dao_comprobante_electronico.guardarComprobanteElectronico(clave_acceso, establecimiento, punto_emision, secuencial, numero_autorizacion,
                        nombre_comercial_emisor, razon_social_emisor, ruc_emisor, ambiente, estado, observacion, fecha_emision, fecha_autorizacion, importe_total,
                        tipo_comprobante_electronico,
                        respuesta_byte, comprobante_bytes);
            }
//            ce_guardado = dao_comprobante_electronico.guardarComprobanteElectronico(clave_acceso, establecimiento, punto_emision,secuencial, numero_autorizacion, nombre_comercial_emisor, razon_social_emisor, ruc_emisor, ambiente, estado, observacion, fecha_emision, fecha_autorizacion, importe_total, tipo_comprobante_electronico, respuesta_byte,comprobante_bytes, _formaPago,_numeroDocumento);
        } catch (Exception ex) {
            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (ce_guardado != null) {

            /**
             * Si el comprobante es una factura se guardan los detalles
             */
            DAODetalle dao_detalle = null;
            try {
                dao_detalle = new DAODetalle();
            } catch (Exception ex) {
                Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (_comprobanElectronico.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
                List<Detalle> detalles = _comprobanElectronico.ConstruirFactura().getDetalles();
                if (detalles != null && !detalles.isEmpty()) {
                    for (Detalle d : detalles) {

                        try {

                            dao_detalle.guardarDetalle(d.getCodigoPrincipal(), d.getDescripcion(), d.getCantidad(), d.getPrecioUnitario(), ce_guardado);
                        } catch (Exception ex) {
                            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            }
            Receptor r = null;
            DAOReceptor dao_receptor = null;
            try {
                dao_receptor = new DAOReceptor();
                r = dao_receptor.obtenerReceptorPorIdentificacion(ruc_receptor);
                if (r == null) {

                    r = dao_receptor.guardarReceptor(ruc_receptor, razon_social_receptor);
                }
            } catch (Exception ex) {
                Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
            }

            DAOAsignacionComprobanteElectronico dao_asignacion_ce = null;
            AsignacionComprobanteElectronico asignacion_ce_guardado = null;
            try {
                dao_asignacion_ce = new DAOAsignacionComprobanteElectronico();
                asignacion_ce_guardado = dao_asignacion_ce.guardarAsignacionComprobanteElectronico(ce_guardado, r);
                guardado_comprobante_electronico_bd = true;
            } catch (Exception ex) {
                Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return guardado_comprobante_electronico_bd;

    }

    private void llenarValores(RespuestaSRI _respuesta, ComprobanteElectronico _comprobante) {
        if (_respuesta != null) {
            if (_respuesta.getEstado().equals("1") || _respuesta.getEstado().equals("2")) {
                numero_autorizacion = _respuesta.getNumeroAutorizacion();
                fecha_autorizacion = _respuesta.getFechaAutorizacion().getTime();
            }
        }
        fecha_emision = Utilidades.obtenerFechaEmisionComprobante(_comprobante);
        ambiente = _comprobante.getInformacionTributariaComprobanteElectronico().getAmbiente();
        razon_social_emisor = _comprobante.getInformacionTributariaComprobanteElectronico().getRazonSocial();
        nombre_comercial_emisor = _comprobante.getInformacionTributariaComprobanteElectronico().getNombreComercial();
        estado = _respuesta.getEstado();
        ruc_emisor = _comprobante.getInformacionTributariaComprobanteElectronico().getRuc();
        clave_acceso = _comprobante.getInformacionTributariaComprobanteElectronico().getClaveAcceso();
        establecimiento = _comprobante.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento();
        punto_emision = _comprobante.getInformacionTributariaComprobanteElectronico().getPuntoEmision();
        secuencial = _comprobante.getInformacionTributariaComprobanteElectronico().getSecuencial();

        importe_total = null;
        String tipo = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        if (tipo.equals(Valores.FACTURA)) {
            ruc_receptor = _comprobante.ConstruirFactura().getInformacionFactura().getIdentificacionComprador();
            razon_social_receptor = _comprobante.ConstruirFactura().getInformacionFactura().getRazonSocialComprador();
            importe_total = _comprobante.ConstruirFactura().getInformacionFactura().getImporteTotal();
        } else if (tipo.equals(Valores.NOTA_CREDITO)) {
            ruc_receptor = _comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getIdentificacionComprador();
            razon_social_receptor = _comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getRazonSocialComprador();
            importe_total = _comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getValorModificacion();
        
        } else if (tipo.equals(Valores.COMPROBANTE_RETENCION)) {
            ruc_receptor = _comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getIdentificacionSujetoRetenido();
            razon_social_receptor = _comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getRazonSocialSujetoRetenido();

        }

        DAOTipoComprobanteElectronico dao_tipo_comprobante_electronico = null;
        try {
            dao_tipo_comprobante_electronico = new DAOTipoComprobanteElectronico();
            tipo_comprobante_electronico = dao_tipo_comprobante_electronico.obtenerTipoComprobanteElectronicoPorCodigo(tipo);
        } catch (Exception ex) {
            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean guardarPorClaveDeAcceso(String _claveAcceso) {
        boolean guardado = false;
        RespuestaSRI respuesta = null;
        FacturacionElectronica facturacion_produccion = new FacturacionElectronica();
        ec.discheca.firma.sri.pruebas.FacturacionElectronica facturacion_pruebas = new ec.discheca.firma.sri.pruebas.FacturacionElectronica();
        ec.discheca.firma.sri.pruebas.RespuestaSRI respuestaPruebas = null;
        ConsultaComprobantePro consulta_pro = new ConsultaComprobantePro();
        if (_claveAcceso != null) {
            ec.discheca.firma.sri.pruebas.ConsultaComprobante consultaAutorizacion = new ec.discheca.firma.sri.pruebas.ConsultaComprobante();
            try {
//                respuestaPruebas = consultaAutorizacion.obtenerRespuestaSRI(_claveAcceso);
//                respuesta = facturacion_pruebas.toRespuestaSRIProduccion(respuestaPruebas);
                //web service produccion para pruebas de comprobante recibidos
                respuesta = consulta_pro.obtenerRespuestaSRI(_claveAcceso);
                if (respuesta.getEstado().equals("1") || respuesta.getEstado().equals("2")) {
                    DAOComprobanteElectronico dao_comprobante_electronico = null;
                    ec.discheca.modelo.ComprobanteElectronico ce = null;
                    try {
                        dao_comprobante_electronico = new DAOComprobanteElectronico();
                        ce = dao_comprobante_electronico.obtenerComprobatePorCA(_claveAcceso);
                    } catch (Exception ex) {
                        Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (ce == null) {

                        ComprobanteElectronico c = TransformadorArchivos.byteCompr(respuesta.getComprobante().getBytes("UTF-8"), Valores.VALOR_DIRECTORIO_CREACION_XMLS + RandomStringUtils.randomAlphanumeric(10).concat(".xml"));

                        guardado = guardarComprobanteElectronico(respuesta, c, c.getInformacionTributariaComprobanteElectronico().getCodDoc(), Valores.VALOR_DIRECTORIO_CREACION_XMLS, "");

                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return guardado;
    }

    public boolean actualizarEstadoComprobantePendiente(ec.discheca.firma.sri.produccion.RespuestaSRI _respuestaSRI, byte[] _comprobante, byte[] _respuesta) {
        boolean actualizado_estado = false;
        try {
            DAOComprobanteElectronico dao_comprobante_electronico = null;
            dao_comprobante_electronico = new DAOComprobanteElectronico();
            String observacion = "";
            if (_respuestaSRI.getEstado().equals("1")) {
                observacion = "AUTORIZADO";
            } else {
                observacion = "NO AUTORIZADO";
            }
            if (_respuestaSRI.getMensaje() != null) {
                observacion += _respuestaSRI.getMensaje();
                if (_respuestaSRI.getInformacionAdicional() != null && !_respuestaSRI.getInformacionAdicional().equals("")) {
                    observacion += _respuestaSRI.getInformacionAdicional();
                }
            }

            ec.discheca.modelo.ComprobanteElectronico ce = dao_comprobante_electronico.obtenerComprobatePorCA(_respuestaSRI.getClaveAccesoConsultada());
            if (ce != null) {
                actualizado_estado = dao_comprobante_electronico.actualizarEstadoComprobantePendiente(ce.getIdComprobanteElectronico(), _respuestaSRI.getClaveAccesoConsultada(), _respuestaSRI.getNumeroAutorizacion(), _respuestaSRI.getFechaAutorizacion().getTime(), _respuestaSRI.getEstado(), observacion, _comprobante, _respuesta);
            }

        } catch (Exception ex) {
            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
        }
        return actualizado_estado;
    }

    public static void main(String args[]) {
        try {
            Valores.init();
            HibernateSessionHandler hsh = new HibernateSessionHandler();

            AlmacenamientoComprobanteElectronico ace = new AlmacenamientoComprobanteElectronico();
            ace.guardarPorClaveDeAcceso("0508201501179254716400110010020000001725069744911");
            hsh.close();
        } catch (Exception ex) {
            Logger.getLogger(AlmacenamientoComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
