/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.dao.ec;


import com.egastos.modelo.ec.Auditoria;
import com.egastos.utilidades.DAO;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Usuario
 */
public class DAOAuditoria extends DAO{
    public DAOAuditoria() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOAuditoria(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOAuditoria(DAO dao) throws Exception {
        super(dao);
    }
    
    public boolean insertarRegistro(Auditoria auditoria){
        currentSession.save(auditoria);
        return true;
    }
     public List buscarRegistrosVariosParametros(String _rucEmisor,Date _fechaSeleccionadaInicio, Date _fechaActual, Integer _firstResult, Integer _maxResults) {
        Criteria criteria_busqueda = this.buscarCriteriaRegistrosVariosParametros( _rucEmisor, _fechaSeleccionadaInicio, _fechaActual);
        criteria_busqueda.setFirstResult(_firstResult);
        criteria_busqueda.setMaxResults(_maxResults);
        return criteria_busqueda.list();
    }
       public List buscarRegistrosVariosParaReporte(String _rucEmisor,Date _fechaSeleccionadaInicio, Date _fechaActual) {
        Criteria criteria_busqueda = this.buscarCriteriaRegistrosVariosParametros( _rucEmisor, _fechaSeleccionadaInicio, _fechaActual);
        return criteria_busqueda.list();
    }

    private Criteria buscarCriteriaRegistrosVariosParametros( String _rucEmisor, Date _fechaSeleccionadaInicio, Date _fechaActual) {
        Criteria criteriaQuery = currentSession.createCriteria(Auditoria.class, "registro");
        criteriaQuery.addOrder(Order.desc("registro.fechaHora"));
        criteriaQuery.createAlias("registro.usuarioAcceso", "usuario");
      
        if (_rucEmisor != null && !_rucEmisor.equals("")) {
            criteriaQuery.add(Restrictions.eq("usuario.identificacionUsuario", _rucEmisor));
        }
      
        if (_fechaSeleccionadaInicio != null && _fechaActual != null) {
            Criterion criterioPorFechaEmision = Restrictions.between("registro.fecha", _fechaSeleccionadaInicio, _fechaActual);
            
            criteriaQuery.add(criterioPorFechaEmision);
        }
     
        return criteriaQuery;
    }

    public Long obtenerTotalRegistrosVariosParametros( String _rucEmisor, Date _fechaSeleccionadaInicio, Date _fechaActual) {
        Criteria criteriaQuery = buscarCriteriaRegistrosVariosParametros(_rucEmisor, _fechaSeleccionadaInicio, _fechaActual);
        Long total = Long.parseLong(criteriaQuery.setProjection(Projections.rowCount()).uniqueResult().toString());
        return total;
    }

}
