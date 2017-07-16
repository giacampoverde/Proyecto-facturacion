/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.dao.ec;

import com.egastos.modelo.ec.ComprobanteElectronico;
import com.egastos.modelo.ec.Pagos;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.AES256;
import com.egastos.utilidades.DAO;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ricar
 */
public class DAOPagos extends DAO {

    public DAOPagos() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOPagos(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOPagos(DAO dao) throws Exception {
        super(dao);
    }

//    public Pagos obtenerPagosPorUsuario(Integer idUsuario, Date fecha) {
//         Query q = currentSession.createSQLQuery("SELECT * from Pagos where  estadopago= 'pagado' and idUsuario=:idUsuario and  idpagos= (select max(idpagos) from Pagos where  estadopago= 'pagado' order by(idpagos) desc ) order by(idpagos) desc");
//        q.setParameter("idUsuario", idUsuario);
////         q.setParameter("fecha", fecha);
//        return (Pagos) q.uniqueResult();
//    }
    public Integer obteneridMaximo(Integer idUsuario,String estado){
       Query q = currentSession.createSQLQuery("select max(idpagos) from pagos where  estadopago=:estado  and idUsuario=:idUsuario order by(idpagos) desc ");
        q.setParameter("idUsuario", idUsuario); 
         q.setParameter("estado",estado);
        return (Integer) q.uniqueResult();
    }
    public Pagos ObtenerPagos(Integer idUsuario,String estado) {
        
        Query q = currentSession.createQuery("from Pagos where  idUsuario=:idUsuario and  idpagos=:ID ");
        q.setParameter("idUsuario", idUsuario);
         q.setParameter("ID", obteneridMaximo(idUsuario,estado));
         
//         q.setParameter("fecha", fecha);
        return (Pagos) q.uniqueResult();
    }

    public boolean InsertarPago(Pagos pag) {
        currentSession.save(pag);
        return true;
    }
    
//     public List<Pagos> obtenerPagos(Integer _firstResult, Integer _maxResult) throws Exception {
//        Query q = currentSession.createQuery("from Pagos order by(idpagos) desc");
//        q.setFirstResult(_firstResult);
//        q.setMaxResults(_maxResult);
//        return q.list();
//    }
    
     public Long obtenerTotalComprobantesVariosParametros(Date _fechaSeleccionadaInicio, Date _fechaActual, String _estado,String valorInicial, String valorFinal,String cedula) {
        Criteria criteriaQuery = buscarCriteriaPlanesVariosParametros(_fechaSeleccionadaInicio, _fechaActual, _estado, valorInicial, valorFinal,cedula);
        Long total = Long.parseLong(criteriaQuery.setProjection(Projections.rowCount()).uniqueResult().toString());
        return total;
    }
        public List buscarPaogosVariosParametros( Integer _firstResult, Integer _maxResults,Date _fechaSeleccionadaInicio, Date _fechaActual, String _estado,String valorInicial, String valorFinal,String cedula) {
        Criteria criteria_busqueda = this.buscarCriteriaPlanesVariosParametros(_fechaSeleccionadaInicio, _fechaActual, _estado, valorInicial, valorFinal,cedula);
        criteria_busqueda.setFirstResult(_firstResult);
        criteria_busqueda.setMaxResults(_maxResults);
        return criteria_busqueda.list();
    }
        
          private Criteria buscarCriteriaPlanesVariosParametros(Date _fechaSeleccionadaInicio, Date _fechaActual, String _estado,String valorInicial, String valorFinal,String cedula) {
        Criteria criteriaQuery = currentSession.createCriteria(Pagos.class, "pago");
        criteriaQuery.createAlias("pago.usuarioAcceso", "usuario");
        criteriaQuery.createAlias("pago.planespago", "planes");
        criteriaQuery.addOrder(Order.desc("pago.idpagos"));
 
        if (_fechaSeleccionadaInicio != null && _fechaActual != null) {
            Criterion criterioPorFechaEmision = Restrictions.between("pago.fechaPago", _fechaSeleccionadaInicio, _fechaActual);
            Criterion criterioPorFechaAutorizacion = Restrictions.between("pago.fechaPago", _fechaSeleccionadaInicio, _fechaActual);
            criteriaQuery.add(Restrictions.or(criterioPorFechaEmision, criterioPorFechaAutorizacion));
            criteriaQuery.addOrder(Order.desc("pago.fechaPago"));
        }
        if (_estado != null && !_estado.equals("1")) {
            criteriaQuery.add(Restrictions.eq("pago.estadopago", _estado));

        }
        if (cedula != null && !cedula.equals("")) {
            criteriaQuery.add(Restrictions.eq("usuario.identificacionUsuario",cedula));

        }
        if ((valorInicial != null && !valorInicial.equals("")) && (valorFinal == null || valorFinal.equals(""))) {
            criteriaQuery.add(Restrictions.gt("pago.valorPago",Double.parseDouble(valorInicial)));
        }
        if (valorInicial != null && !valorInicial.equals("") && valorFinal != null && !valorFinal.equals("")) {
            criteriaQuery.add(Restrictions.gt("pago.valorPago",Double.parseDouble(valorInicial)));
            criteriaQuery.add(Restrictions.le("pago.valorPago",Double.parseDouble(valorFinal)));
       
        }
        
        return criteriaQuery;
    }


 public boolean actualizarEstadopago(Integer _idUsuario, String estado) {
        boolean actualizado = false;
        try {
            Pagos pag = (Pagos) currentSession.load(Pagos.class, _idUsuario);
            pag.setEstadopago(estado);
            currentSession.merge(pag);
            actualizado = true;
        } catch (Exception ex) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }

        return actualizado;
    }
  public boolean actualizarFechaPago(Integer _idUsuario, Date fecaVali) {
        boolean actualizado = false;
        try {
            Pagos pag = (Pagos) currentSession.load(Pagos.class, _idUsuario);
            pag.setFechaValidacion(fecaVali);
            currentSession.merge(pag);
            actualizado = true;
        } catch (Exception ex) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }

        return actualizado;
    }
}
