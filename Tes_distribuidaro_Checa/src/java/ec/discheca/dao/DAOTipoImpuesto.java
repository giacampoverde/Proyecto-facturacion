/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.dao;


import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.TipoImpuesto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class DAOTipoImpuesto extends DAO{

    public DAOTipoImpuesto() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOTipoImpuesto(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOTipoImpuesto(DAO dao) throws Exception {
        super(dao);
    }
    
    public List<TipoImpuesto> ObtenerTipoImpuesto(){
        Query q=currentSession.createQuery("from TipoImpuesto");
        
        return q.list();
        
    }
    public TipoImpuesto ObtenerTipoImpuestoporPk(Integer codigoTipo){
        Query q=currentSession.createQuery("from TipoImpuesto where idTipoImpuesto=:codigoTipo");
        q.setParameter("codigoTipo", codigoTipo);
       return (TipoImpuesto) q.uniqueResult();
        
    }
    
    public boolean actualizarImpuesto(String _idUsuario, String impuesto) {
        boolean actualizado = false;
        try {
            TipoImpuesto tipoImpuesto = (TipoImpuesto) currentSession.load(_idUsuario, impuesto);
            tipoImpuesto.setImpuesto(impuesto);
            currentSession.merge(tipoImpuesto);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }
}
