/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.dao.ec;



import com.egastos.modelo.ec.Configuracion;
import com.egastos.utilidades.DAO;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo Delgado
 */
public class DAOConfiguracion extends DAO{
    public DAOConfiguracion() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOConfiguracion(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOConfiguracion(DAO dao) throws Exception {
        super(dao);
    }
    
    public boolean insertarConfiguracion(Configuracion configuracion){
        currentSession.save(configuracion);
        return true;
    }
    
    public boolean actualizarNombreConfiguracion(String id, String nombre){
        boolean actualizar=false;
        
        try{
        Configuracion configuracion=(Configuracion) currentSession.load(id, nombre);
        configuracion.setNombreConfiguracion(nombre);
        currentSession.merge(configuracion);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarValorConfiguracion(String id, String valor){
        boolean actualizar=false;
        
        try{
        Configuracion configuracion=(Configuracion) currentSession.load(id, valor);
        configuracion.setValorConfiguracion(valor);
        currentSession.merge(configuracion);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarAmbienteConfiguracion(String id, String ambiente){
        boolean actualizar=false;
        
        try{
        Configuracion configuracion=(Configuracion) currentSession.load(id, ambiente);
        configuracion.setAmbienteConfiguracion(ambiente);
        currentSession.merge(configuracion);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public Configuracion obtenerConfiguracionPorNombre(String _nombre, String _ambiente) throws Exception {
        Query q = currentSession.createQuery("from Configuracion where nombreConfiguracion=:_nombre and ambienteConfiguracion=:_ambiente");
        q.setParameter("_ambiente", _ambiente);
        q.setParameter("_nombre", _nombre);
        return (Configuracion) q.uniqueResult();

    }
    
    public String obtenerValorConfiguracion(String _nombre, String _ambiente) throws Exception {
        Query q = currentSession.createQuery("select valorConfiguracion from Configuracion where nombreConfiguracion=:_nombre and ambienteConfiguracion=:_ambiente");
        q.setParameter("_ambiente", _ambiente);
        q.setParameter("_nombre", _nombre);
        return (String) q.uniqueResult();

    }
    
    public boolean actualizarTasaInteres(Integer _idConfiguracion, String _nuevaTasaInteres) {
        boolean actualizado = false;
        try {
            Configuracion configuracion = (Configuracion) currentSession.load(Configuracion.class, _idConfiguracion);
            configuracion.setValorConfiguracion(_nuevaTasaInteres);
            currentSession.merge(configuracion);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }
    public boolean actualizarHost(Integer _idConfiguracion, String _hostServidor) {
        boolean actualizado = false;
        try {
            Configuracion configuracion = (Configuracion) currentSession.load(Configuracion.class, _idConfiguracion);
            configuracion.setValorConfiguracion(_hostServidor);
            currentSession.merge(configuracion);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }
    public boolean actualizarPuerto(Integer _idConfiguracion, String _puerto) {
        boolean actualizado = false;
        try {
            Configuracion configuracion = (Configuracion) currentSession.load(Configuracion.class, _idConfiguracion);
            configuracion.setValorConfiguracion(_puerto);
            currentSession.merge(configuracion);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }
    
    public boolean actualizarUsuarioCuentaCorreo(Integer _idConfiguracion, String _usuarioCorreo) {
        boolean actualizado = false;
        try {
            Configuracion configuracion = (Configuracion) currentSession.load(Configuracion.class, _idConfiguracion);
            configuracion.setValorConfiguracion(_usuarioCorreo);
            currentSession.merge(configuracion);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }
    
    public boolean actualizarClaveCuentaCorreo(Integer _idConfiguracion, String _claveCorreo) {
        boolean actualizado = false;
        try {
            Configuracion configuracion = (Configuracion) currentSession.load(Configuracion.class, _idConfiguracion);
            configuracion.setValorConfiguracion(_claveCorreo);
            currentSession.merge(configuracion);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }
    
    public boolean actualizarCuentaCorreo(Integer _idConfiguracion, String _cuentaCorreo) {
        boolean actualizado = false;
        try {
            Configuracion configuracion = (Configuracion) currentSession.load(Configuracion.class, _idConfiguracion);
            configuracion.setValorConfiguracion(_cuentaCorreo);
            currentSession.merge(configuracion);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }
}
