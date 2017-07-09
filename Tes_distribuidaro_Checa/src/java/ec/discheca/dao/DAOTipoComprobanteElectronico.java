/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.dao;


import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.TipoComprobanteElectronico;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class DAOTipoComprobanteElectronico extends DAO{
    public DAOTipoComprobanteElectronico() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOTipoComprobanteElectronico(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOTipoComprobanteElectronico(DAO dao) throws Exception {
        super(dao);
    }
    
    public List<TipoComprobanteElectronico> obtenerTipoComprobanteElectronico(){
        Query q=currentSession.createQuery("from TipoComprobanteElectronico");
        return q.list();
        
    }
    
    public TipoComprobanteElectronico obtenerTipoComprobanteElectronicoPorCodigo(String _codigoComprobante) throws Exception {
        Query q = currentSession.createQuery("from TipoComprobanteElectronico where codigoTipoComprobanteElectronico=:_codigoComprobante");
        q.setParameter("_codigoComprobante", _codigoComprobante);
        return (TipoComprobanteElectronico) q.uniqueResult();
    }
    
    public boolean InsertarTipoComprobanteElectronico(TipoComprobanteElectronico tipoComprobante){
        currentSession.save(tipoComprobante);
        return true;
    }
    
    public boolean actualizarCodTipoComprobanteElectronico(String identificacion,String codTipoComprobante){
        boolean actualizado=false;
        
        try{
        TipoComprobanteElectronico tCompElectronico=(TipoComprobanteElectronico)currentSession.load(codTipoComprobante, identificacion);
        tCompElectronico.setCodigoTipoComprobanteElectronico(codTipoComprobante);
        currentSession.merge(tCompElectronico);
        actualizado=true;
        }catch(HibernateException e){
            
        }
        
        return actualizado;
    }
    
    public boolean actualizarNombreComprobanteElectronico(String identificacion, String nombreComprobante){
        boolean actualizado=false;
        
        try{
            TipoComprobanteElectronico tCompElectronico=(TipoComprobanteElectronico)currentSession.load(nombreComprobante, identificacion);
            tCompElectronico.setCodigoTipoComprobanteElectronico(nombreComprobante);
            currentSession.merge(tCompElectronico);
            actualizado=true;
        }catch(HibernateException e){
            
        }
        
        return actualizado;
    }
    
}
