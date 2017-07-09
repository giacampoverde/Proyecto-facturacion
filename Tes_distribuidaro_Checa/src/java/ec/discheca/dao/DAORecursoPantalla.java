/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.dao;
import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Recurso;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo
 */
public class DAORecursoPantalla extends DAO {

    public DAORecursoPantalla() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAORecursoPantalla(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAORecursoPantalla(DAO dao) throws Exception {
        super(dao);
    }

    public Recurso obtenerRecursoPorId(Integer _idRecurso) throws Exception {
        Query q = currentSession.createQuery("from Recurso where idRecurso=:_idRecurso");
        q.setParameter("_idRecurso", _idRecurso);
        return (Recurso) q.uniqueResult();

    }

    public List<Recurso> obtenerPantallasPorPerfil(Integer _idPerfil) throws Exception {
        Query q = currentSession.createQuery("select p.recurso from Permiso p where p.perfil.idPerfil=:_idPerfil order by p.recurso.nombreRecurso");
        q.setParameter("_idPerfil", _idPerfil);
        return q.list();

    }

//    public List<Recurso> obtenerPantallas() throws Exception {
//        Query q = currentSession.createQuery("from Recurso where pestaniaRecurso=null");
//        return q.list();
//    }
     public List<Recurso> obtenerPantallas() throws Exception {
        Query q = currentSession.createQuery("from Recurso ");
        return q.list();
    }

    public List<String> obtenerPantallasHijas(Integer _idPadre) throws Exception {
        Query q = currentSession.createQuery("select r.nombreRecurso from Recurso r where r.recurso.idRecurso=:_idPadre");
        q.setParameter("_idPadre", _idPadre);
        return q.list();
    }

    public List<Recurso> obtenerPantallasPorPestaniaPorPerfil(Integer _idPadre, Integer _idPerfil) throws Exception {
        Query q = currentSession.createQuery("select pe.recurso from Permiso pe where pe.recurso.recurso.idRecurso=:_idPadre and pe.perfil.idPerfil=:_idPerfil order by pe.recurso.nombreRecurso");
        q.setParameter("_idPadre", _idPadre);
        q.setParameter("_idPerfil", _idPerfil);
        return q.list();
    }

}
