/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.dao;


import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Detalleadicional;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo Delgado
 */
public class DAODetalleAdicional extends DAO {

    public DAODetalleAdicional() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAODetalleAdicional(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAODetalleAdicional(DAO dao) throws Exception {
        super(dao);
    }

    public List<Detalleadicional> obtenerDetalleAdicional() {
        Query q = currentSession.createQuery("from DetalleAdicional");
        return q.list();
    }

    public Detalleadicional obtenerDetalleAdicionalporId(Integer idDetalleAdicional) {
        Query q = currentSession.createQuery("from DetalleAdicional where idDetalleAdicional=:idDetalleAdicional");
        q.setParameter("idDetalleAdicional", idDetalleAdicional);
        return (Detalleadicional) q.uniqueResult();
    }

    public List<Detalleadicional> obtenerDetallesAdicionalesporIdProducto(Integer idProducto) {
        Query q = currentSession.createQuery("from DetalleAdicional where producto.idproducto=:idProducto");
        q.setParameter("idProducto", idProducto);
        return q.list();
    }

    public boolean guardarDetalleAdicional(Detalleadicional detalleAdicional) throws Exception {
        currentSession.save(detalleAdicional);
        return true;
    }

    public boolean actualizarDetalleAdicional(Integer idDetalleAdicional, String nombre, String valor) {
        boolean actualizar = false;

        try {
            Detalleadicional detalle = (Detalleadicional) currentSession.load(Detalleadicional.class, idDetalleAdicional);
            detalle.setNombre(nombre);
            detalle.setNombre(valor);
            currentSession.merge(detalle);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean eliminarDetalleAdicional(Detalleadicional detalleAdicional) throws Exception {
        currentSession.delete(detalleAdicional);
        return true;
    }
}
