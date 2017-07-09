/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.dao;


import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Perfil;
import ec.discheca.modelo.Permiso;
import ec.discheca.modelo.Producto;
import ec.discheca.modelo.Recurso;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo Delgado
 */
public class DAOProductos extends DAO{
    public DAOProductos() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOProductos(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOProductos(DAO dao) throws Exception {
        super(dao);
    }
    
     public List<Producto> obtenerPermiso(){
        Query q=currentSession.createQuery("from Producto");
        return q.list();
    }
     
    
    public boolean insertarPermiso(Permiso permiso){
        currentSession.save(permiso);
        return true;
    }
    
    public boolean actualizarRecursoPermiso(String id, Recurso recurso){
        boolean actualizar=false;
        
        try{
        Permiso permiso=(Permiso) currentSession.load(id, recurso);
        permiso.setRecurso(recurso);
        currentSession.merge(permiso);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarPerfilPermiso(String id, Perfil perfil){
        boolean actualizar=false;
        
        try{
        Permiso permiso=(Permiso) currentSession.load(id, perfil);
        permiso.setPerfil(perfil);
        currentSession.merge(permiso);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }


    
    public List<Permiso> obtenerPermisosPorPerfil(Integer _idPerfil) throws Exception {
        Query q = currentSession.createQuery("from Permiso where perfil.idPerfil=:_idPerfil and recurso.idRecurso!=1");
        q.setParameter("_idPerfil", _idPerfil);
        return q.list();
    }

    public Permiso obtenerPermisosPorPerfilRecurso(Integer _idPerfil, Integer _idRecurso) throws Exception {
        Query q = currentSession.createQuery("from Permiso where perfil.idPerfil=:_idPerfil and recurso.idRecurso=:_idRecurso");
        q.setParameter("_idPerfil", _idPerfil);
        q.setParameter("_idRecurso", _idRecurso);
        return (Permiso) q.uniqueResult();
    }
    public List<Permiso> obtenerRecursosPorPermisosHijos(Integer _idPadre,Integer _idPerfil) throws Exception {
        Query q = currentSession.createQuery("from Permiso p where p.recurso.recurso.idRecurso=:_idPadre and p.perfil.idPerfil=:_idPerfil");
        q.setParameter("_idPadre", _idPadre);
        q.setParameter("_idPerfil", _idPerfil);
        return q.list();
    }

    public boolean asignarPermiso(Perfil _perfil, Recurso _recurso) {
        boolean guardado = false;
        try {
            Permiso p = new Permiso();
            p.setPerfil(_perfil);
            p.setRecurso(_recurso);
            currentSession.save(p);
            guardado = true;
        } catch (HibernateException e) {
        }
        return guardado;
    }

    public boolean eliminarPermiso(Permiso _permiso) {
        boolean eliminado = false;
        try {

            currentSession.delete(_permiso);
            eliminado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return eliminado;
    }
}
