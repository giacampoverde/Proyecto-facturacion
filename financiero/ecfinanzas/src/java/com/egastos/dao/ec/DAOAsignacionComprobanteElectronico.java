/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.dao.ec;

import com.egastos.modelo.ec.AsignacionComprobanteElectronico;
import com.egastos.modelo.ec.ComprobanteElectronico;
import com.egastos.modelo.ec.Receptor;
import com.egastos.utilidades.DAO;

import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class DAOAsignacionComprobanteElectronico extends DAO {

    public DAOAsignacionComprobanteElectronico() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOAsignacionComprobanteElectronico(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOAsignacionComprobanteElectronico(DAO dao) throws Exception {
        super(dao);
    }

    public List<AsignacionComprobanteElectronico> obtenerComprobantesElectronicosAutorizadosRecibidosPormes(String _rucEmpresa, String messelecionado, String anio) throws Exception {
        Query q = currentSession.createQuery("from AsignacionComprobanteElectronico ace where ace.receptor.rucReceptor=:_rucEmpresa and ace.comprobanteElectronico.estadoComprobanteElectronico='1' and MONTH(ace.comprobanteElectronico.fechaEmisionComprobanteElectronico)=:messelecionado and YEAR(comprobanteElectronico.fechaEmisionComprobanteElectronico)=:anio  order by ace.comprobanteElectronico.fechaEmisionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setParameter("messelecionado", Integer.parseInt(messelecionado));
        q.setParameter("anio", Integer.parseInt(anio));
        return q.list();
    }
    
    public List<AsignacionComprobanteElectronico> obtenerComprobantesElectronicosAutorizadosRecibidosPoranoSeccion(String _rucEmpresa,String anio,String seccion) throws Exception {
        Query q = currentSession.createQuery("from AsignacionComprobanteElectronico ace where ace.receptor.rucReceptor=:_rucEmpresa and ace.comprobanteElectronico.estadoComprobanteElectronico='1' and ace.comprobanteElectronico.seccion=:seccion  and YEAR(comprobanteElectronico.fechaEmisionComprobanteElectronico)=:anio  order by ace.comprobanteElectronico.fechaEmisionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setParameter("anio", Integer.parseInt(anio));
        q.setParameter("seccion", seccion);
        return q.list();
    }
    

    public List<AsignacionComprobanteElectronico> obtenerComprobanteElectronico(String id) {
        Query q = currentSession.createQuery("from Receptor where comprobanteElectronico.idAsignacionComprobanteElectronico=:id");
        q.setParameter("id", id);
        return q.list();
    }
   

    public boolean insertarAsignacionComprobanteElectronico(AsignacionComprobanteElectronico asignacion) {
        currentSession.save(asignacion);
        return true;
    }

    public AsignacionComprobanteElectronico guardarAsignacionComprobanteElectronico(ComprobanteElectronico _comprobanteElectronico, Receptor _receptor) throws Exception {
        boolean guardado = false;

        AsignacionComprobanteElectronico ace = new AsignacionComprobanteElectronico(_receptor, _comprobanteElectronico);
        ace.setVistoReceptorAsignacionComprobanteElectronico(Boolean.FALSE);
        return (AsignacionComprobanteElectronico) currentSession.merge(ace);

    }

    public List<AsignacionComprobanteElectronico> obtenerComprobantesElectronicosAutorizadosPorRUC(String _rucEmpresa, Integer _firstResult, Integer _maxResult) throws Exception {
        Query q = currentSession.createQuery("from AsignacionComprobanteElectronico where comprobanteElectronico.rucEmisorComprobanteElectronico=:_rucEmpresa and estadoComprobanteElectronico='1'  order by comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setFirstResult(_firstResult);
        q.setMaxResults(_maxResult);
        return q.list();
    }

    public List<AsignacionComprobanteElectronico> obtenerComprobantesElectronicosAutorizadosPorRUCMEs(String _rucEmpresa, String messelecionado, String anio, String tipo) throws Exception {
        Query q = currentSession.createQuery("from AsignacionComprobanteElectronico where comprobanteElectronico.rucEmisorComprobanteElectronico=:_rucEmpresa and comprobanteElectronico.tipoComprobanteElectronico.codigoTipoComprobanteElectronico=:tipo and estadoComprobanteElectronico='1' and MONTH(comprobanteElectronico.fechaEmisionComprobanteElectronico)=:messelecionado and YEAR(comprobanteElectronico.fechaEmisionComprobanteElectronico)=:anio order by comprobanteElectronico.fechaEmisionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setParameter("messelecionado", Integer.parseInt(messelecionado));
        q.setParameter("tipo", tipo);
        q.setParameter("anio", Integer.parseInt(anio));
        return q.list();
    }

    public Long obtenerTotalComprobantesElectronicosAutorizadosPorRUC(String _rucEmpresa) throws Exception {
        Query q = currentSession.createQuery("select count(*) from AsignacionComprobanteElectronico where comprobanteElectronico.rucEmisorComprobanteElectronico=:_rucEmpresa and estadoComprobanteElectronico='1' order by comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        return (Long) q.uniqueResult();
    }

    public List<AsignacionComprobanteElectronico> obtenerComprobantesElectronicosNoAutorizadosPorRUC(String _rucEmpresa, Integer _firstResult, Integer _maxResult) throws Exception {
        Query q = currentSession.createQuery("from AsignacionComprobanteElectronico where comprobanteElectronico.rucEmisorComprobanteElectronico=:_rucEmpresa and estadoComprobanteElectronico='2' order by comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setFirstResult(_firstResult);
        q.setMaxResults(_maxResult);
        return q.list();
    }

    public Long obtenerTotalComprobantesElectronicosNoAutorizadosPorRUC(String _rucEmpresa) throws Exception {
        Query q = currentSession.createQuery("select count(*) from AsignacionComprobanteElectronico where comprobanteElectronico.rucEmisorComprobanteElectronico=:_rucEmpresa and estadoComprobanteElectronico='2' order by comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        return (Long) q.uniqueResult();
    }

    public List<AsignacionComprobanteElectronico> obtenerComprobantesElectronicosAutorizadosRecibidosPorRUC(String _rucEmpresa, Integer _firstResult, Integer _maxResult) throws Exception {
        Query q = currentSession.createQuery("from AsignacionComprobanteElectronico ace where ace.receptor.rucReceptor=:_rucEmpresa and ace.comprobanteElectronico.estadoComprobanteElectronico='1'  order by ace.comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setFirstResult(_firstResult);
        q.setMaxResults(_maxResult);
        return q.list();
    }

    public Long obtenerTotalComprobantesElectronicosAutorizadosRecibidosPorRUC(String _rucEmpresa) throws Exception {
        Query q = currentSession.createQuery("select count(*) from AsignacionComprobanteElectronico ace where ace.receptor.rucReceptor=:_rucEmpresa and ace.comprobanteElectronico.estadoComprobanteElectronico='1'  order by ace.comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        return (Long) q.uniqueResult();
    }
    public Long obtenerTotalComprobantesElectronicosRuecibidosPorRUC(String _rucEmpresa) throws Exception {
        Query q = currentSession.createQuery("select count(*) from AsignacionComprobanteElectronico ace where ace.receptor.rucReceptor=:_rucEmpresa   order by ace.comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        return (Long) q.uniqueResult();
    }

    public List buscarComprobantesVariosParametros(List _ambiente, String _secuencial, String _rucEmisor, String _rucReceptor, Date _fechaSeleccionadaInicio, Date _fechaActual, String _estado, Integer _firstResult, Integer _maxResults, String tipo, String nombreEmpresa, String valorInicial, String valorFinal) {
        Criteria criteria_busqueda = this.buscarCriteriaComprobantesVariosParametros(_ambiente, _secuencial, _rucEmisor, _rucReceptor, _fechaSeleccionadaInicio, _fechaActual, _estado, tipo, nombreEmpresa,valorInicial,valorFinal);
        criteria_busqueda.setFirstResult(_firstResult);
        criteria_busqueda.setMaxResults(_maxResults);
        return criteria_busqueda.list();
    }

    public Criteria buscarCriteriaComprobantesVariosParametros(List _ambiente, String _secuencial, String _rucEmisor, String _rucReceptor, Date _fechaSeleccionadaInicio, Date _fechaActual, String _estado, String tipo, String nombreEmpresa, String valorInicial, String valorFinal) {
        Criteria criteriaQuery = currentSession.createCriteria(AsignacionComprobanteElectronico.class, "asignacion");
        criteriaQuery.createAlias("asignacion.comprobanteElectronico", "comprobanteElectronico");
        criteriaQuery.createAlias("comprobanteElectronico.tipoComprobanteElectronico", "tipoComprobanteElectronico");
        criteriaQuery.createAlias("asignacion.receptor", "receptor");
        criteriaQuery.addOrder(Order.desc("comprobanteElectronico.fechaAutorizacionComprobanteElectronico"));
//        if (_ambiente != null && !_ambiente.isEmpty()) {
//            criteriaQuery.add(Restrictions.in("comprobanteElectronico.ambienteComprobanteElectronico", _ambiente));
//        }
        if (_secuencial != null && !_secuencial.equals("")) {
            criteriaQuery.add(Restrictions.like("comprobanteElectronico.secuencialComprobanteElectronico", "%" + _secuencial + "%"));
        }
        if (_rucEmisor != null && !_rucEmisor.equals("")) {
            criteriaQuery.add(Restrictions.eq("comprobanteElectronico.rucEmisorComprobanteElectronico", _rucEmisor));
        }
        if (_rucReceptor != null && !_rucReceptor.equals("")) {
            criteriaQuery.add(Restrictions.eq("receptor.rucReceptor", _rucReceptor));
        }
        if (_fechaSeleccionadaInicio != null && _fechaActual != null) {
            Criterion criterioPorFechaEmision = Restrictions.between("comprobanteElectronico.fechaEmisionComprobanteElectronico", _fechaSeleccionadaInicio, _fechaActual);
            Criterion criterioPorFechaAutorizacion = Restrictions.between("comprobanteElectronico.fechaAutorizacionComprobanteElectronico", _fechaSeleccionadaInicio, _fechaActual);
            criteriaQuery.add(Restrictions.or(criterioPorFechaEmision, criterioPorFechaAutorizacion));
            criteriaQuery.addOrder(Order.desc("comprobanteElectronico.fechaAutorizacionComprobanteElectronico"));
        }
        if (_estado != null) {
            criteriaQuery.add(Restrictions.eq("comprobanteElectronico.estadoComprobanteElectronico", _estado));

        }
        if (tipo != null && !tipo.equals("-1")) {
            criteriaQuery.add(Restrictions.eq("tipoComprobanteElectronico.codigoTipoComprobanteElectronico", tipo));

        }
        if (nombreEmpresa != null && !nombreEmpresa.equals("")) {
            criteriaQuery.add(Restrictions.like("comprobanteElectronico.razonSocialEmisorComprobanteElectronico", "%" + nombreEmpresa + "%"));
        }
        if ((valorInicial != null && !valorInicial.equals("")) && (valorFinal == null || valorFinal.equals(""))) {
            criteriaQuery.add(Restrictions.gt("comprobanteElectronico.valorTotalFacturaComprobanteElectronico",Double.parseDouble(valorInicial)));
        }
        if (valorInicial != null && !valorInicial.equals("") && valorFinal != null && !valorFinal.equals("")) {
            criteriaQuery.add(Restrictions.gt("comprobanteElectronico.valorTotalFacturaComprobanteElectronico",Double.parseDouble(valorInicial)));
            criteriaQuery.add(Restrictions.le("comprobanteElectronico.valorTotalFacturaComprobanteElectronico",Double.parseDouble(valorFinal)));
       
        }
        
        return criteriaQuery;
    }

    public Long obtenerTotalComprobantesVariosParametros(List _ambiente, String _secuencial, String _rucEmisor, String _rucReceptor, Date _fechaSeleccionadaInicio, Date _fechaActual, String _estado, String tipo, String nombreEmpresa,String valorInicial,String valorFinal) {
        Criteria criteriaQuery = buscarCriteriaComprobantesVariosParametros(_ambiente, _secuencial, _rucEmisor, _rucReceptor, _fechaSeleccionadaInicio, _fechaActual, _estado, tipo, nombreEmpresa,valorInicial,valorFinal);
        Long total = Long.parseLong(criteriaQuery.setProjection(Projections.rowCount()).uniqueResult().toString());
        return total;
    }

    public List<AsignacionComprobanteElectronico> obtenerComprobantesRecibidosLazy(int idUsuario, int estadoAsignacion, int idTipoAsignacion, String _rucReceptor, String ambiente, String emisor, Integer first, Integer max) throws Exception {
        Query q;
        q = currentSession.createQuery("from AsignacionComprobanteElectronico ace join ace.comprobanteElectronico where ace.receptor.rucReceptor=:_rucReceptor");

        q.setParameter("_emisor", emisor);

        q.setParameter("_estado", estadoAsignacion);
        q.setParameter("_tipo", idTipoAsignacion);
        q.setParameter("_idUsuario", idUsuario);
        q.setParameter("_rucReceptor", _rucReceptor);
        q.setParameter("_ambiente", ambiente);
        q.setFirstResult(first);
        q.setMaxResults(max);
        return q.list();
    }

    public Long obtenerTotalComprobantesPorParametrosRecibidosLazy(int idUsuario, int estadoAsignacion, int idTipoAsignacion, String receptor, String ambiente, String emisor) throws Exception {
//        Query q = currentSession.createQuery("select count(*) from AsignacionComprobante ac join ac.comprobante.receptors as r where ac.comprobante.estado=1 and ac.usuario.idUsuario=:_idUsuario and ac.estado=:_estado and ac.tipoAsignacion.idTipoAsignacion=:_tipo and ac.comprobante.ambiente=:_ambiente and r.rucReceptor=:_receptor order by ac.comprobante.fechaAutorizacion desc");
        Query q;
        q = currentSession.createQuery("select count(*) from AsignacionComprobanteElectronico ace join ace.comprobanteElectronico where ace.receptor.rucReceptor=:receptor");

        //q = currentSession.createQuery("select count(*) from AsignacionComprobanteElectronico ac join ac.comprobante.receptors as r where ac.comprobante.estado=1 and ac.usuario.idUsuario=:_idUsuario and ac.estado=:_estado and ac.tipoAsignacion.idTipoAsignacion=:_tipo and ac.comprobante.ambiente=:_ambiente and ac.comprobante.rucEmisor=:_emisor and r.rucReceptor=:_receptor order by ac.comprobante.fechaAutorizacion desc");
        q.setParameter("_emisor", emisor);

        q.setParameter("_estado", estadoAsignacion);
        q.setParameter("_tipo", idTipoAsignacion);
        q.setParameter("_idUsuario", idUsuario);
        q.setParameter("receptor", receptor);
//        q.setParameter("_ambiente", ambiente);
        return (Long) q.uniqueResult();
    }

    public List<AsignacionComprobanteElectronico> obtenerComprobantesElectronicosAnuladosPorRUC(String _rucEmpresa, Integer _firstResult, Integer _maxResult) throws Exception {
        Query q = currentSession.createQuery("from AsignacionComprobanteElectronico ace where ace.comprobanteElectronico.rucEmisorComprobanteElectronico=:_rucEmpresa and ace.comprobanteElectronico.estadoComprobanteElectronico='12' order by ace.comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setFirstResult(_firstResult);
        q.setMaxResults(_maxResult);
        return q.list();
    }

    public Long obtenerTotalComprobantesElectronicosAnuladosPorRUC(String _rucEmpresa) throws Exception {
        Query q = currentSession.createQuery("select count(*) from AsignacionComprobanteElectronico ace where ace.comprobanteElectronico.rucEmisorComprobanteElectronico=:_rucEmpresa and ace.comprobanteElectronico.estadoComprobanteElectronico='12' order by ace.comprobanteElectronico.fechaAutorizacionComprobanteElectronico desc");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        return (Long) q.uniqueResult();
    }
}
