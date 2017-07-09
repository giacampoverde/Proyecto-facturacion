/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.dao;



import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Certificado;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;


public class DAOCertificado extends DAO{
    public DAOCertificado() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOCertificado(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOCertificado(DAO dao) throws Exception {
        super(dao);
    }
    
     public List<Certificado> obtenerCertificado(){
        Query q=currentSession.createQuery("from Certificado");
        return q.list();
    }
    
    public boolean insertarCertificado(Certificado certificado){
        currentSession.save(certificado);
        return true;
    }
    
    public boolean actualizarRutaCertificado(String id, String rutaCertificado){
        boolean actualizar=false;
        
        try{
        Certificado certificado=(Certificado) currentSession.load(id, rutaCertificado);
        certificado.setRutaCertificado(rutaCertificado);
        currentSession.merge(certificado);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarTipoCertificado(String id, String tipoCertificado){
        boolean actualizar=false;
        
        try{
        Certificado certificado=(Certificado) currentSession.load(id, tipoCertificado);
        certificado.setTipoCertificado(tipoCertificado);
        currentSession.merge(certificado);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarEstadoCertificado(String id, String estadoCertificado){
        boolean actualizar=false;
        
        try{
        Certificado certificado=(Certificado) currentSession.load(id, estadoCertificado);
        certificado.setEstadoCertificado(estadoCertificado);
        currentSession.merge(certificado);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarClaveCertificado(String id, String claveCertificado){
        boolean actualizar=false;
        
        try{
        Certificado certificado=(Certificado) currentSession.load(id, claveCertificado);
        certificado.setClaveCertificado(claveCertificado);
        currentSession.merge(certificado);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarNumeroFirmasCertificado(String id, String numeroFirmas){
        boolean actualizar=false;
        
        try{
        Certificado certificado=(Certificado) currentSession.load(id, numeroFirmas);
        certificado.setNumeroFirmasCertificado(numeroFirmas);
        currentSession.merge(certificado);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarAmbienteCertificado(String id, String ambienteCertificado){
        boolean actualizar=false;
        
        try{
        Certificado certificado=(Certificado) currentSession.load(id, ambienteCertificado);
        certificado.setAmbienteCertificado(ambienteCertificado);
        currentSession.merge(certificado);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarFechaCreacionCertificado(String id, Date fechaCreacionCertificado){
        boolean actualizar=false;
        
        try{
        Certificado certificado=(Certificado) currentSession.load(id, fechaCreacionCertificado);
        certificado.setFechaCreacionCertificado(fechaCreacionCertificado);
        currentSession.merge(certificado);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public Certificado obtenerCertificadoPorRUC(String _ruc) throws Exception {
        Certificado certificado = null;
        try {
            Query q = currentSession.createQuery("from Certificado where empresa.idEmpresa=:_ruc");
            q.setParameter("_ruc", _ruc);
            if (q != null) {
                certificado = (Certificado) q.uniqueResult();
            }
        } catch (HibernateException e) {
              Logger.getLogger(DAOCertificado.class.getName()).log(Level.SEVERE, "Error en obtención de certificado.",e);
        }
        return certificado;
    }
}
