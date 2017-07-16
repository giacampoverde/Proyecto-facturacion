/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.dao.ec;


import com.egastos.utilidades.DAO;
import com.egastos.modelo.ec.ComprobanteElectronico;
import com.egastos.modelo.ec.Detalle;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class DAODetalle extends DAO{
    public DAODetalle() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAODetalle(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAODetalle(DAO dao) throws Exception {
        super(dao);
    }
    
     public List<Detalle> obtenerDetalle(){
        Query q=currentSession.createQuery("from Detalle");
        return q.list();
    }
     
     public List<Detalle>obtenerDetalleporCodigoPrincipal(String codigoPrincipal){
         Query q=currentSession.createQuery("from Detalle where codigoPrincipalDetalle=:codigoPrincipal");
         q.setParameter("codigoPrincipal", codigoPrincipal);
         return q.list();
     }
    

       public boolean guardarDetalle(String _codigoPrincipalDetalle, String _descripcionDetalle, String _cantidadDetalle, String _precioUnitarioDetalle, ComprobanteElectronico _comprobanteElectronico) throws Exception {
        com.egastos.modelo.ec.Detalle detalle = new com.egastos.modelo.ec.Detalle();
        detalle.setCodigoPrincipalDetalle(_codigoPrincipalDetalle);
        detalle.setCantidadDetalle(_cantidadDetalle);
        detalle.setPrecioUnitarioDetalle(_precioUnitarioDetalle);
        detalle.setComprobanteElectronico(_comprobanteElectronico);
        detalle.setDescripcionDetalle(_descripcionDetalle);        
        currentSession.save(detalle);
        return true;
    }
    public boolean actualizarComprobanteElectronicoDetalle(String id, ComprobanteElectronico comprobanteElectronico){
        boolean actualizar=false;
        
        try{
        Detalle detalle=(Detalle) currentSession.load(id, comprobanteElectronico);
        detalle.setComprobanteElectronico(comprobanteElectronico);
        currentSession.merge(detalle);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarCodigoPrincipalDetalle(String id, String codPrincipal){
        boolean actualizar=false;
        
        try{
        Detalle detalle=(Detalle) currentSession.load(id, codPrincipal);
        detalle.setCodigoPrincipalDetalle(codPrincipal);
        currentSession.merge(detalle);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarDescripcionDetalle(String id, String descripcionDetalle){
        boolean actualizar=false;
        
        try{
        Detalle detalle=(Detalle) currentSession.load(id, descripcionDetalle);
        detalle.setCodigoPrincipalDetalle(descripcionDetalle);
        currentSession.merge(detalle);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarCantidadDetalle(String id, String cantidadDetalle){
        boolean actualizar=false;
        
        try{
        Detalle detalle=(Detalle) currentSession.load(id, cantidadDetalle);
        detalle.setCodigoPrincipalDetalle(cantidadDetalle);
        currentSession.merge(detalle);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarPrecioUnitarioDetalle(String id, String precioUnitario){
        boolean actualizar=false;
        
        try{
        Detalle detalle=(Detalle) currentSession.load(id, precioUnitario);
        detalle.setPrecioUnitarioDetalle(precioUnitario);
        currentSession.merge(detalle);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
}
