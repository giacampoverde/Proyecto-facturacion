/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.dao.ec;


import com.egastos.utilidades.DAO;
import com.egastos.modelo.ec.Receptor;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class DAOReceptor extends DAO{
    public DAOReceptor() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOReceptor(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOReceptor(DAO dao) throws Exception {
        super(dao);
    }
    
     public List<Receptor> obtenerReceptor(){
        Query q=currentSession.createQuery("from Receptor");
        return q.list();
    }
     
     public List<Receptor>obtenerReceptorporId(String Id){
         Query q=currentSession.createQuery("from Receptor Where idReceptor=:Id");
         q.setParameter("Id", Id);
         return q.list();
     }
         public Receptor obtenerReceptorPorIdentificacion(String _rucReceptor) throws Exception {
        Query q = currentSession.createQuery("from Receptor where rucReceptor=:_rucReceptor");
        q.setParameter("_rucReceptor", _rucReceptor);
        return (Receptor) q.uniqueResult();
    }

       public Receptor guardarReceptor(String _rucReceptor, String _razonSocialReceptor) throws Exception {
        boolean guardado = false;

        Receptor receptor = new Receptor(_rucReceptor, _razonSocialReceptor);

        return (Receptor) currentSession.merge(receptor);

    }

     
     public List<Receptor>obtenerReceptorporRazonSocial(String razonSocial){
         Query q=currentSession.createQuery("from Receptor Where razonSocialReceptor=:razonSocial");
         q.setParameter("razonSocial", razonSocial);
         return q.list();
     }
    

    public boolean actualizarRUCReceptor(String id, String rucReceptor){
        boolean actualizar=false;
        
        try{
        Receptor receptor=(Receptor) currentSession.load(id, rucReceptor);
        receptor.setRucReceptor(rucReceptor);
        currentSession.merge(receptor);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarRazonSocialReceptor(String id, String razonSocial){
        boolean actualizar=false;
        
        try{
        Receptor receptor=(Receptor) currentSession.load(id, razonSocial);
        receptor.setRazonSocialReceptor(razonSocial);
        currentSession.merge(receptor);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
}
