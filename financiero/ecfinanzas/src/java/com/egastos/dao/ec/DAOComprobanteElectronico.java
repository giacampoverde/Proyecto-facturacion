/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.dao.ec;
import com.egastos.modelo.ec.AsignacionComprobanteElectronico;
import com.egastos.modelo.ec.ComprobanteElectronico;
import com.egastos.modelo.ec.TipoComprobanteElectronico;
import com.egastos.pdfride.PDF;
import com.egastos.utilidades.DAO;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ricardo Delgado
 */
public class DAOComprobanteElectronico extends DAO {

    public boolean actualizarEstadoComprobantePendiente(Integer _idComprobante, String _claveAcceso, String _numeroAutorizacion, Date _fechaAutorizacion, String _estado, String _mensaje, byte[] _comprobante, byte[] _respuesta) throws Exception {
        boolean actualizado = false;
        ComprobanteElectronico ce = (ComprobanteElectronico) currentSession.load(ComprobanteElectronico.class, _idComprobante);
        if (ce != null) {
            ce.setClaveAccesoComprobanteElectronico(_claveAcceso);
            ce.setEstadoComprobanteElectronico(_estado);
            ce.setNumeroAutorizacionComprobanteElectronico(_numeroAutorizacion);
            ce.setFechaAutorizacionComprobanteElectronico(_fechaAutorizacion);
            ce.setMensajeComprobanteElectronico(_mensaje);
            ce.setComprobanteFirmadoComprobanteElectronico(_comprobante);
            ce.setArchivoRespuestaSricomprobanteElectronico(_respuesta);
            
            currentSession.merge(ce);
            actualizado = true;
        }
        return actualizado;
    }

    public DAOComprobanteElectronico() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOComprobanteElectronico(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOComprobanteElectronico(DAO dao) throws Exception {
        super(dao);
    }

    public List<ComprobanteElectronico> obtenerComprobanteElectronico() {
        Query q = currentSession.createQuery("from ComprobanteElectronico");
        return q.list();
    }

    public boolean insertarComprobanteElectronico(ComprobanteElectronico comprobante) {
        currentSession.save(comprobante);
        return true;
    }

    public ComprobanteElectronico obtenerComprobatePorCodigoEstablecimientoPuntoEmisionSecuencialRUCFechaEmision(String _codigoEstablecimiento, String _puntoEmision, String _secuencial, String _rucEmisor, Integer _codigoDocumento, Integer _diaEmision, Integer _mesEmision, Integer _anioEmision) throws Exception {

        Query q = currentSession.createQuery("from ComprobanteElectronico where "
                + "codigoEstablecimientoComprobanteElectronico=:_codigoEstablecimiento"
                + " and puntoEmisionComprobanteElectronico=:_puntoEmision "
                + " and estadoComprobanteElectronico=:_estado"
                + " and secuencialComprobanteElectronico=:_secuencial"
                + " and rucEmisorComprobanteElectronico=:_rucEmisor"
                + " and tipoComprobanteElectronico.idTipoComprobanteElectronico=:_codigoDocumento"
                + " and DAY(fechaEmisionComprobanteElectronico)=:_diaEmision"
                + " and MONTH(fechaEmisionComprobanteElectronico)=:_mesEmision"
                + " and YEAR(fechaEmisionComprobanteElectronico)=:_anioEmision");
        q.setParameter("_codigoEstablecimiento", _codigoEstablecimiento);
        q.setParameter("_puntoEmision", _puntoEmision);
        q.setParameter("_secuencial", _secuencial);
        q.setParameter("_rucEmisor", _rucEmisor);
        q.setParameter("_codigoDocumento", _codigoDocumento);
        q.setParameter("_diaEmision", _diaEmision);
        q.setParameter("_mesEmision", _mesEmision);
        q.setParameter("_anioEmision", _anioEmision);
        q.setParameter("_estado", "1");

        return (ComprobanteElectronico) q.uniqueResult();
    }

    public boolean actualizarClaveAccesoComprobante(String id, String claveAcceso) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, claveAcceso);
            comprobante.setClaveAccesoComprobanteElectronico(claveAcceso);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarCodigoEstablecimientoComprobante(String id, String codigoEstablecimiento) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, codigoEstablecimiento);
            comprobante.setCodigoEstablecimientoComprobanteElectronico(codigoEstablecimiento);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarPuntoEmisionComprobante(String id, String puntoEmision) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, puntoEmision);
            comprobante.setPuntoEmisionComprobanteElectronico(puntoEmision);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarSecuencialComprobante(String id, String secuencial) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, secuencial);
            comprobante.setSecuencialComprobanteElectronico(secuencial);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarNumeroAutorizacionComprobante(String id, String numeroAutorizacion) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, numeroAutorizacion);
            comprobante.setNumeroAutorizacionComprobanteElectronico(numeroAutorizacion);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarRUCEmisorComprobante(String id, String rucEmisor) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, rucEmisor);
            comprobante.setRucEmisorComprobanteElectronico(rucEmisor);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarNombreComercialEmisorComprobante(String id, String nombreComercialEmisor) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, nombreComercialEmisor);
            comprobante.setNombreComercialEmisorComprobanteElectronico(nombreComercialEmisor);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarRazonSocialEmisorComprobante(String id, String razonSocialEmisor) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, razonSocialEmisor);
            comprobante.setRazonSocialEmisorComprobanteElectronico(razonSocialEmisor);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarAmbienteComprobante(String id, String ambiente) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, ambiente);
            comprobante.setAmbienteComprobanteElectronico(ambiente);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarEstadoComprobante(String id, String estado) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, estado);
            comprobante.setEstadoComprobanteElectronico(estado);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarMensajeComprobante(String id, String mensaje) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, mensaje);
            comprobante.setMensajeComprobanteElectronico(mensaje);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarFechaEmisionComprobante(String id, Date fechaEmision) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, fechaEmision);
            comprobante.setFechaEmisionComprobanteElectronico(fechaEmision);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarFechaAutorizacionComprobante(String id, Date fechaAutorizacion) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, fechaAutorizacion);
            comprobante.setFechaAutorizacionComprobanteElectronico(fechaAutorizacion);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarValorTotalFacturaComprobante(String id, String valorTotalFactura) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, valorTotalFactura);
            comprobante.setValorTotalFacturaComprobanteElectronico(Double.parseDouble(valorTotalFactura));
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarArchivoRespuestasSRIComprobante(String id, byte[] respuestaSRI) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, respuestaSRI);
            comprobante.setArchivoRespuestaSricomprobanteElectronico(respuestaSRI);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarComprobanteFirmado(String id, byte[] comprobanteFirmado) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, comprobanteFirmado);
            comprobante.setComprobanteFirmadoComprobanteElectronico(comprobanteFirmado);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarNotificadoCorreo(String id, Boolean notificadoCorreo) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, notificadoCorreo);
            comprobante.setNotificadoCorreo(notificadoCorreo);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarVistoEmisorCorreo(String id, Boolean vistoEmisor) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, vistoEmisor);
            comprobante.setVistoEmisorComprobanteElectronico(vistoEmisor);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarTipoPagoCorreo(String id, String tipoPago) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, tipoPago);
            comprobante.setTipoPagoComprobanteElectronico(tipoPago);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarNumeroDocumentoTransferencia(String id, String numeroDocumentoTranfererncia) {
        boolean actualizar = false;

        try {
            ComprobanteElectronico comprobante = (ComprobanteElectronico) currentSession.load(id, numeroDocumentoTranfererncia);
            comprobante.setNumeroDocumentoTransferencia(numeroDocumentoTranfererncia);
            currentSession.merge(comprobante);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public List<ComprobanteElectronico> obtenerComprobantesElectronicosPorRUC(String _rucEmpresa, Integer _firstResult, Integer _maxResult) throws Exception {
        Query q = currentSession.createQuery("from ComprobanteElectronico where rucEmisorComprobanteElectronico=:_rucEmpresa");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setFirstResult(_firstResult);
        q.setMaxResults(_maxResult);
        return q.list();
    }

    public Long obtenerTotalComprobantesElectronicosPorRUC(String _rucEmpresa) throws Exception {
        Query q = currentSession.createQuery("select count(*) from ComprobanteElectronico where rucEmisorComprobanteElectronico=:_rucEmpresa");
        q.setParameter("_rucEmpresa", _rucEmpresa);

        return (Long) q.uniqueResult();
    }

    public ComprobanteElectronico obtenerComprobatePorCA(String ca) throws Exception {

        Query q = currentSession.createQuery("from ComprobanteElectronico where claveAccesoComprobanteElectronico=:ca");
        q.setParameter("ca", ca);

        return (ComprobanteElectronico) q.uniqueResult();
    }

    public List busquedaPorParametros(int idUsuario, int estadoAsignacion, int idTipoAsignacion, String emisor, List ambiente, int tipo, String secuencial, String claveAcceso, BigDecimal total, int estadoAutorizacion, int orden, String rucReceptor, Date fechaIni, Date fechaFin, Integer limiteIn, Integer limiteS) {
        Criteria criteriaQuery = consultaBusquedaPorParametros(idUsuario, estadoAsignacion, idTipoAsignacion, emisor, ambiente, tipo, secuencial, claveAcceso, total, estadoAutorizacion, orden, rucReceptor, fechaIni, fechaFin);
        criteriaQuery.setFirstResult(limiteIn);
        criteriaQuery.setMaxResults(limiteS);
        return criteriaQuery.list();

    }

    public Long numeroBusquedaPorParametros(int idUsuario, int estadoAsignacion, int idTipoAsignacion, String emisor, List ambiente, int tipo, String secuencial, String claveAcceso, BigDecimal total, int estadoAutorizacion, int orden, String rucReceptor, Date fechaIni, Date fechaFin) {
        Criteria criteriaQuery = consultaBusquedaPorParametros(idUsuario, estadoAsignacion, idTipoAsignacion, emisor, ambiente, tipo, secuencial, claveAcceso, total, estadoAutorizacion, orden, rucReceptor, fechaIni, fechaFin);
        Long valor = Long.parseLong(criteriaQuery.setProjection(Projections.rowCount()).uniqueResult().toString());
        return valor;
    }

    private Criteria consultaBusquedaPorParametros(int idUsuario, int estadoAsignacion, int idTipoAsignacion, String emisor, List ambiente, int tipo, String secuencial, String claveAcceso, BigDecimal total, int estadoAutorizacion, int orden, String rucReceptor, Date fechaIni, Date fechaFin) {

        Criteria criteriaQuery = currentSession.createCriteria(AsignacionComprobanteElectronico.class, "asignacion");
        criteriaQuery.createAlias("asignacion.comprobanteElectronico", "comprobanteElectronico");
        criteriaQuery.createAlias("asignacion.usuario", "usuario");
        criteriaQuery.createAlias("asignacion.rucReceptorAsignacionComprobanteElectronico", rucReceptor);
        criteriaQuery.add(Restrictions.eq("comprobanteElectronico.estadoComprobanteElectronico", estadoAsignacion));
        criteriaQuery.add(Restrictions.eq("asignacion.tipoAsignacion.idTipoAsignacion", idTipoAsignacion));

        if (!emisor.equals("")) {
            criteriaQuery.add(Restrictions.eq("comprobante.rucEmisorComprobanteElectronico", emisor));
        }

        if (idUsuario > 0) {
            criteriaQuery.add(Restrictions.eq("usuario.idUsuario", idUsuario));
        }

//        if ((!rucReceptor.trim().equals("") || rucReceptor != null) && rucReceptor.length() > 0) {
//            if (Validaciones.isNum(rucReceptor)) {
//                criteriaQuery.add(Restrictions.eq("receptor.rucReceptor", rucReceptor));
//            } else {
//                criteriaQuery.add(Restrictions.like("receptor.razonSocialReceptor", "%" + rucReceptor + "%"));
//            }
//
//        }
        if (!secuencial.equals("")) {
            criteriaQuery.add(Restrictions.like("comprobante.secuencialComprobanteElectronico", "%" + secuencial + "%"));
        }

        if (claveAcceso != null && !claveAcceso.equals("")) {
            criteriaQuery.add(Restrictions.like("comprobante.claveAcceso", "%" + claveAcceso + "%"));
        }

        if (total != null) {
            BigDecimal limS = total.add(BigDecimal.ONE);
            criteriaQuery.add(Restrictions.between("comprobante.valorTotal", total, limS));
        }
        if (fechaFin != null && fechaIni != null) {
            criteriaQuery.add(Restrictions.between("comprobanteElectronico.fechaEmisionComprobanteElectronico", fechaIni, fechaFin));
        }

        if (tipo > 0) {
            criteriaQuery.add(Restrictions.eq("comprobante.tipoComprobante.idTipoComprobante", tipo));

        }

        if (estadoAutorizacion > 0) {
            criteriaQuery.add(Restrictions.eq("comprobante.estado", estadoAutorizacion));
        }

        if (ambiente != null && !ambiente.isEmpty()) {
            criteriaQuery.add(Restrictions.in("comprobanteElectronico.ambienteComprobanteElectronico", ambiente));
        }

        if (orden == 1) {
            criteriaQuery.addOrder(Order.desc("comprobante.fechaAutorizacion"));
        }
        if (orden == 2) {
            criteriaQuery.addOrder(Order.asc("comprobante.fechaAutorizacion"));
        }
        if (orden == 3) {
            criteriaQuery.addOrder(Order.desc("comprobante.secuencial"));
        }
        if (orden == 4) {
            criteriaQuery.addOrder(Order.asc("comprobante.secuencial"));
        }

        return criteriaQuery;

    }

    public byte[] generarPDFComprobante(com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico comprobante) throws Exception {
        byte[] pdfByte = null;
        byte[] logoEmpresa = null;

        PDF pdf_ride = new PDF();

//        //Se obtiene el ruc del emisor para enviar la imagen
//        DAOClienteEmpresa dce = new DAOClienteEmpresa();

//        logoEmpresa = dce.obtenerLogoClienteEmpresaPorId(comprobante.getInformacionTributariaComprobanteElectronico().getRuc());

        pdfByte = pdf_ride.construirPDFComprobante(comprobante, logoEmpresa, null, null);

        return pdfByte;

    }

    public ComprobanteElectronico guardarComprobanteElectronico(String _claveAcceso, String _codigoEstablecimiento,
            String _puntoEmision, String _secuencial, String _numeroAutorizacion, String _nombreComercialEmisor,
            String _razonSocialEmisor, String _rucEmisor, String _ambiente, String _estado, String _mensaje, Date _fechaEmision,
            Date _fechaAutorizacion, String _valorTotal, TipoComprobanteElectronico _tipoComprobante, byte[] _respuestaSRI,
            byte[] _comprobanteFirmado,String seccion) throws Exception {

        ComprobanteElectronico comprobante_electronico = new ComprobanteElectronico();
        comprobante_electronico.setClaveAccesoComprobanteElectronico(_claveAcceso);
        comprobante_electronico.setCodigoEstablecimientoComprobanteElectronico(_codigoEstablecimiento);
        comprobante_electronico.setPuntoEmisionComprobanteElectronico(_puntoEmision);
        comprobante_electronico.setSecuencialComprobanteElectronico(_secuencial);
        comprobante_electronico.setNumeroAutorizacionComprobanteElectronico(_numeroAutorizacion);
        comprobante_electronico.setNombreComercialEmisorComprobanteElectronico(_nombreComercialEmisor);
        comprobante_electronico.setRazonSocialEmisorComprobanteElectronico(_razonSocialEmisor);
        comprobante_electronico.setRucEmisorComprobanteElectronico(_rucEmisor);
        comprobante_electronico.setAmbienteComprobanteElectronico(_ambiente);
        comprobante_electronico.setEstadoComprobanteElectronico(_estado);
        comprobante_electronico.setMensajeComprobanteElectronico(_mensaje);
        comprobante_electronico.setFechaEmisionComprobanteElectronico(_fechaEmision);
        comprobante_electronico.setFechaAutorizacionComprobanteElectronico(_fechaAutorizacion);
        comprobante_electronico.setSeccion(seccion);

        if (_valorTotal != null) {
            comprobante_electronico.setValorTotalFacturaComprobanteElectronico(Double.parseDouble(_valorTotal));
        }

        comprobante_electronico.setTipoComprobanteElectronico(_tipoComprobante);
        comprobante_electronico.setArchivoRespuestaSricomprobanteElectronico(_respuestaSRI);
        comprobante_electronico.setComprobanteFirmadoComprobanteElectronico(_comprobanteFirmado);
        comprobante_electronico.setVistoEmisorComprobanteElectronico(Boolean.FALSE);
        return (ComprobanteElectronico) currentSession.merge(comprobante_electronico);

    }

    public boolean actualizarComprobanteAnulado(Integer _id) {
        boolean actualizado = false;
        ComprobanteElectronico c = (ComprobanteElectronico) currentSession.load(ComprobanteElectronico.class, _id);
        if (c != null) {
            c.setEstadoComprobanteElectronico("12");
            currentSession.merge(c);
            actualizado = true;
        }
        return actualizado;

    }

    public ComprobanteElectronico guardarComprobanteElectronicoPendiente(String _claveAcceso, String _codigoEstablecimiento, String _puntoEmision, String _secuencial, String _nombreComercialEmisor,
            String _razonSocialEmisor, String _rucEmisor, String _ambiente, String _estado, String _mensaje, Date _fechaEmision,
            String _valorTotal, TipoComprobanteElectronico _tipoComprobante) throws Exception {

        ComprobanteElectronico comprobante_electronico = new ComprobanteElectronico();
        comprobante_electronico.setClaveAccesoComprobanteElectronico(_claveAcceso);
        comprobante_electronico.setCodigoEstablecimientoComprobanteElectronico(_codigoEstablecimiento);
        comprobante_electronico.setPuntoEmisionComprobanteElectronico(_puntoEmision);
        comprobante_electronico.setSecuencialComprobanteElectronico(_secuencial);
        comprobante_electronico.setNombreComercialEmisorComprobanteElectronico(_nombreComercialEmisor);
        comprobante_electronico.setRazonSocialEmisorComprobanteElectronico(_razonSocialEmisor);
        comprobante_electronico.setRucEmisorComprobanteElectronico(_rucEmisor);
        comprobante_electronico.setAmbienteComprobanteElectronico(_ambiente);
        comprobante_electronico.setEstadoComprobanteElectronico(_estado);
        comprobante_electronico.setMensajeComprobanteElectronico(_mensaje);
        comprobante_electronico.setFechaEmisionComprobanteElectronico(_fechaEmision);

        if (_valorTotal != null) {
            comprobante_electronico.setValorTotalFacturaComprobanteElectronico(Double.parseDouble(_valorTotal));
        }

        comprobante_electronico.setTipoComprobanteElectronico(_tipoComprobante);
        return (ComprobanteElectronico) currentSession.merge(comprobante_electronico);

    }
}
