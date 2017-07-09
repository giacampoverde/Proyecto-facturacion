/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.dao;


import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.TarifasImpuesto;
import ec.discheca.modelo.TipoImpuesto;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

public class DAOTarifasImpuesto extends DAO {

    public DAOTarifasImpuesto() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOTarifasImpuesto(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOTarifasImpuesto(DAO dao) throws Exception {
        super(dao);
    }

    public List<TarifasImpuesto> obtenerTarifasImpuesto() {
        Query q = currentSession.createQuery("from TarifasImpuesto");
        return q.list();
    }

    public TarifasImpuesto obtenerTarifaporIdTarifa(Integer idTarifa) {
        Query q = currentSession.createQuery("from TarifasImpuesto where idtarifaImpuesto=:idTarifa");
        q.setParameter("idTarifa", idTarifa);
         return (TarifasImpuesto) q.uniqueResult();
    }

    public TarifasImpuesto obtenerTarifasporCodigoTarifa(String codTarifa) {
        Query q = currentSession.createQuery("from TarifasImpuesto where codigoTarifaImpuesto=:codTarifa");
        q.setParameter("codTarifa", codTarifa);

        return (TarifasImpuesto) q.uniqueResult();
    }

    public List<TarifasImpuesto> obtenerTarifasporCodigoImpuesto(Integer tipoimpuesto) {
        Query q = currentSession.createQuery("from TarifasImpuesto where tipoImpuesto.idTipoImpuesto=:tipoimpuesto");
        q.setParameter("tipoimpuesto", tipoimpuesto);

        return q.list();
    }

    public boolean insertarTarifasImpuesto(TarifasImpuesto tarifasImpuesto) {
        currentSession.save(tarifasImpuesto);
        return true;
    }

    public boolean actualizarCodigoTarifa(String id, String codTarifa) {
        boolean actualizado = false;

        try {
            TarifasImpuesto tarifasImpuesto = (TarifasImpuesto) currentSession.load(id, codTarifa);
            tarifasImpuesto.setCodigoTarifaImpuesto(codTarifa);
            currentSession.merge(tarifasImpuesto);
            actualizado = true;
        } catch (HibernateException e) {

        }

        return actualizado;
    }

    public boolean actualizarTipoImpuesto(String id, TipoImpuesto tipoImpuesto) {
        boolean actualizado = false;

        try {
            TarifasImpuesto tarifasImpuesto = (TarifasImpuesto) currentSession.load(id, tipoImpuesto);
            tarifasImpuesto.setTipoImpuesto(tipoImpuesto);
            currentSession.merge(tarifasImpuesto);
            actualizado = true;
        } catch (HibernateException e) {

        }

        return actualizado;
    }

    public boolean actualizarDescripcionTarifa(String id, String descripcion) {
        boolean actualizado = false;

        try {
            TarifasImpuesto tarifasImpuesto = (TarifasImpuesto) currentSession.load(id, descripcion);
            tarifasImpuesto.setDescripcionTarifaImpuesto(descripcion);
            currentSession.merge(tarifasImpuesto);
            actualizado = true;
        } catch (HibernateException e) {

        }

        return actualizado;
    }

    public boolean actualizarPorcentajeTarifa(String id, String porcentajeTarifa) {
        boolean actualizado = false;

        try {
            TarifasImpuesto tarifasImpuesto = (TarifasImpuesto) currentSession.load(id, porcentajeTarifa);
            tarifasImpuesto.setPorcentajeTarifaImpuesto(porcentajeTarifa);
            currentSession.merge(tarifasImpuesto);
            actualizado = true;
        } catch (HibernateException e) {

        }

        return actualizado;
    }

    public boolean actualizarVariableTarifa(String id, boolean variableTarifa) {
        boolean actualizado = false;

        try {
            TarifasImpuesto tarifasImpuesto = (TarifasImpuesto) currentSession.load(id, variableTarifa);
            tarifasImpuesto.setVariableTarifaImpuesto(variableTarifa);
            currentSession.merge(tarifasImpuesto);
            actualizado = true;
        } catch (HibernateException e) {

        }

        return actualizado;
    }
//cambio de dos a 3 para iba 14
    public List<TarifasImpuesto> obtenerImpuestoIVA() throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where tipoImpuesto.idTipoImpuesto=2 and codigoTarifaImpuesto in (0,3,6,7)");
        return q.list();
    }


    public List<TarifasImpuesto> obtenerImpuestosIVARenta() throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where tipoImpuesto.idTipoImpuesto=1 and codigoTarifaImpuesto in (1,2,3,7,8,9,10) order by codigoTarifaImpuesto");
        return q.list();
    }
    
     public List<TarifasImpuesto> obtenerImpuestosRenta() throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where tipoImpuesto.idTipoImpuesto=1 and codigoTarifaImpuesto not in (1,2,3,7,8,9,10) order by codigoTarifaImpuesto");
        return q.list();
    }

    public List<TarifasImpuesto> obtenerImpuestosISD() throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where tipoImpuesto.idTipoImpuesto=6");
        return q.list();
    }

    public TarifasImpuesto obtenerImpuestoPorTarifa(String _codigo) throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where codigoTarifaImpuesto=:_codigo");
        q.setParameter("_codigo", _codigo);
        return (TarifasImpuesto) q.uniqueResult();
    }
    
    public TarifasImpuesto obtenerImpuestoPorTarifaYTipoImpuesto(String _codigo, Integer _codigoTarifa) throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where codigoTarifaImpuesto=:_codigo and codigoTipoTarifaImpuesto=:_codigoTarifa");
        q.setParameter("_codigo", _codigo);
        q.setParameter("_codigoTarifa", _codigoTarifa);
        return (TarifasImpuesto) q.uniqueResult();
    }

    public TarifasImpuesto obtenerImpuestoIVAPorTarifa(String _codigo) throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where codigoTarifaImpuesto=:_codigo and descripcionTarifaImpuesto not like '%Rete%'");
        q.setParameter("_codigo", _codigo);
        return (TarifasImpuesto) q.uniqueResult();
    }
//     public TarifasImpuesto obtenerImpuestoIVAPorTarifa(String _codigo) throws Exception {
//        Query q = currentSession.createQuery("from TarifasImpuesto where codigoTarifaImpuesto=:_codigo and descripcionTarifaImpuesto not like '%Rete%'");
//        q.setParameter("_codigo", _codigo);
//        return (TarifasImpuesto) q.uniqueResult();
//    }

    public TarifasImpuesto obtenerImpuestoPorId(Integer _id) throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where idtarifaImpuesto=:_id");
        q.setParameter("_id", _id);
        return (TarifasImpuesto) q.uniqueResult();
    }

    public List<TarifasImpuesto> obtenerImpuestoICE() throws Exception {
        Query q = currentSession.createQuery("from TarifasImpuesto where tipoImpuesto.idTipoImpuesto=3");
        return q.list();
    }
}
