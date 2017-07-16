/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.dao.ec;


import com.egastos.modelo.ec.Perfil;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.utilidades.DAO;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class DAOPerfil extends DAO{
    public DAOPerfil() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOPerfil(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOPerfil(DAO dao) throws Exception {
        super(dao);
    }
    
     public List<Perfil> obtenerPerfil(){
        Query q=currentSession.createQuery("from Perfil");
        return q.list();
    }
         public List<Perfil> obtenerPerfilactivos(){
        Query q=currentSession.createQuery("from Perfil where estadoPerfil=:_estado");
        q.setParameter("_estado", true);
        return q.list();
    }
     public Perfil obtenerPerfilPorId(Integer _idPerfil) throws Exception {
        Query q = currentSession.createQuery("from Perfil where idPerfil=:_idPerfil");
        q.setParameter("_idPerfil", _idPerfil);
        return (Perfil) q.uniqueResult();
        
    }
    
    public Perfil obtenerPerfilPorNombre(String _nombrePerfil) throws Exception {
        Query q = currentSession.createQuery("from Perfil where nombrePerfil=:_nombrePerfil");
        q.setParameter("_nombrePerfil", _nombrePerfil);
        return (Perfil) q.uniqueResult();
        
    }
    
    public boolean guardarPerfil(String _nombrePerfil, String _descripcionPerfil) {
        boolean guardado = false;
        try {
            Perfil p = new Perfil();            
            p.setNombrePerfil(_nombrePerfil);
            p.setDescripcionPerfil(_descripcionPerfil);
            p.setEstadoPerfil(Boolean.TRUE);
            currentSession.save(p);
            guardado = true;
        } catch (HibernateException e) {
        }
        return guardado;
        
    }
    
    public boolean insertarPerfil(Perfil perfil){
        currentSession.save(perfil);
        return true;
    }
    
    public boolean actualizarNombrePerfil(Integer id, String nombrePerfil){
        boolean actualizar=false;
        
        try{
        Perfil perfil=(Perfil) currentSession.load(Perfil.class, id);
        perfil.setNombrePerfil(nombrePerfil);
        currentSession.merge(perfil);
        actualizar=true;
        }catch(HibernateException e){
            e.printStackTrace();
        }
        
        return actualizar;
    }
    
    public boolean actualizarDescripcionPerfil(Integer id, String descripcionPerfil){
        boolean actualizar=false;
        
        try{
        Perfil perfil=(Perfil) currentSession.load(Perfil.class, id);
        perfil.setDescripcionPerfil(descripcionPerfil);
        currentSession.merge(perfil);
        actualizar=true;
        }catch(HibernateException e){
            e.printStackTrace();
        }
        
        return actualizar;
    }
    
    public boolean cambiarEstadoPerfil(Integer _idPerfil,boolean _nuevoEstado) {
        boolean actualizado = false;
        try {
            Perfil p = (Perfil) currentSession.load(Perfil.class, _idPerfil);
            p.setEstadoPerfil(_nuevoEstado);
            currentSession.merge(p);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;
        
    }
    
    public List<Perfil> obtenerPerfilesParaAsignacion() throws Exception {
        Query q = currentSession.createQuery("from Perfil");
        return q.list();
    }

    public Perfil obtenerPerfilPorCodigo(String _codigoPerfil) {
        List<Perfil> perfiles = new ArrayList<Perfil>();
        Perfil p = null;
        Query q = null;
        try {
            q = currentSession.createQuery("from Perfil where nombrePerfil=:_codigoPerfil");
            q.setParameter("_codigoPerfil", _codigoPerfil);
            p = (Perfil) q.uniqueResult();
        } catch (HibernateException e) {
            perfiles = q.list();
            p = perfiles.get(0);
        }
        return p;
    }
    
    public UsuarioAcceso obtenerPerfilPorCodigoUsuario(String identificacion){
        Query q=currentSession.createQuery("from UsuarioAcceso ua where ua.identificacionUsuario=:identificacion");
        q.setParameter("identificacion", identificacion);
        ArrayList<Perfil>p=new ArrayList<Perfil>();
        return (UsuarioAcceso) q.uniqueResult();
    }
}
