/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.dao;

import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Secuencial;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo Delgado
 */
public class DAOSecuencial extends DAO {

    public DAOSecuencial() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOSecuencial(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOSecuencial(DAO dao) throws Exception {
        super(dao);
    }

    public List<Secuencial> obtenerSecuencialesPorRuc(String _rucEmpresa) throws Exception {
        Query q = currentSession.createQuery("from Secuencial where empresa.idEmpresa=:_rucEmpresa");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        return q.list();
    }

    public List<Secuencial> obtenerSecuencialesPorRucYAmbiente(String _rucEmpresa, String _ambiente) throws Exception {
        Query q = currentSession.createQuery("from Secuencial where empresa.idEmpresa=:_rucEmpresa and ambienteSecuencial=:_ambienteSecuencial");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setParameter("_ambienteSecuencial", _ambiente);
        return q.list();
    }

    public Secuencial obtenerSecuencialActivoPorRucYAmbiente(String _rucEmpresa, String _ambiente) throws Exception {
        Query q = currentSession.createQuery("from Secuencial where empresa.idEmpresa=:_rucEmpresa and ambienteSecuencial=:_ambienteSecuencial and estadoSecuencial=true");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setParameter("_ambienteSecuencial", _ambiente);
        return (Secuencial) q.uniqueResult();
    }

    public Secuencial obtenerSecuencialPorRucYAmbiente(String _rucEmpresa, String _ambiente) throws Exception {
        Query q = currentSession.createQuery("from Secuencial where empresa.idEmpresa=:_rucEmpresa and ambienteSecuencial=:_ambienteSecuencial");
        q.setParameter("_rucEmpresa", _rucEmpresa);
        q.setParameter("_ambienteSecuencial", _ambiente);
        return (Secuencial) q.uniqueResult();
    }

    public List<Secuencial> obtenerSecuencial() {
        Query q = currentSession.createQuery("from Secuencial");
        return q.list();
    }

    public boolean insertarSecuencial(Secuencial secuencial) {
        currentSession.save(secuencial);
        return true;
    }

    public boolean actualizarSecuencialFactura(Integer _idSecuencial, String _nuevoSecuencial) {
        boolean actualizado = false;
        try {
            Secuencial s = (Secuencial) currentSession.load(Secuencial.class, _idSecuencial);
            s.setSecuencialFacturaSecuencial(_nuevoSecuencial);
            currentSession.merge(s);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }

    public boolean actualizarSecuencialPorTipoComprobante(Integer _idSecuencial, String _nuevoSecuencial, String _tipoComprobante) {
        boolean actualizado = false;
        try {
            Secuencial s = (Secuencial) currentSession.load(Secuencial.class, _idSecuencial);
            switch (Integer.parseInt(_tipoComprobante)) {
                case 1:
                    s.setSecuencialFacturaSecuencial(_nuevoSecuencial);
                    break;
                case 4:
                    s.setSecuencialNotaCreditoSecuencial(_nuevoSecuencial);
                    break;
                case 5:
                    s.setSecuencialNotaDebitoSecuencial(_nuevoSecuencial);
                    break;
                case 6:
                    s.setSecuencialGuiaRemisionSecuencial(_nuevoSecuencial);
                    break;
                case 7:
                    s.setSecuencialRetencionSecuencial(_nuevoSecuencial);
                    break;
            }
            currentSession.merge(s);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }

    public boolean actualizarFacturaSecuencial(String id, String facturaSecuencial) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, facturaSecuencial);
            secuencial.setSecuencialFacturaSecuencial(facturaSecuencial);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarNotaCreditoSecuencial(String id, String notaCreditoSecuencial) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, notaCreditoSecuencial);
            secuencial.setSecuencialNotaCreditoSecuencial(notaCreditoSecuencial);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarNotaDebitoSecuencial(String id, String notaDebitoSecuencial) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, notaDebitoSecuencial);
            secuencial.setSecuencialNotaDebitoSecuencial(notaDebitoSecuencial);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarGuiaRemisionSecuencial(String id, String guiaRemision) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, guiaRemision);
            secuencial.setSecuencialGuiaRemisionSecuencial(guiaRemision);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarRetencionSecuencial(String id, String retencionSecuencial) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, retencionSecuencial);
            secuencial.setSecuencialRetencionSecuencial(retencionSecuencial);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarPuntoEmisionSecuencial(String id, String puntoEmisionSecuencial) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, puntoEmisionSecuencial);
            secuencial.setPuntoEmisionSecuencial(puntoEmisionSecuencial);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarCodigoEstablecimientoSecuencial(String id, String codigoEstablecimiento) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, codigoEstablecimiento);
            secuencial.setCodigoEstablecimientoSecuencial(codigoEstablecimiento);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarDireccionSecuencial(String id, String direccion) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, direccion);
            secuencial.setDireccionSecuencial(direccion);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public boolean actualizarEstadoSecuencial(Integer _idSecuencial, Boolean _estado) {
        boolean actualizado = false;
        try {
            Secuencial s = (Secuencial) currentSession.load(Secuencial.class, _idSecuencial);
            s.setEstadoSecuencial(_estado);
            currentSession.merge(s);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }

    public boolean actualizarAmbienteSecuencial(String id, String ambiente) {
        boolean actualizar = false;

        try {
            Secuencial secuencial = (Secuencial) currentSession.load(id, ambiente);
            secuencial.setAmbienteSecuencial(ambiente);
            currentSession.merge(secuencial);
            actualizar = true;
        } catch (HibernateException e) {

        }

        return actualizar;
    }

    public Secuencial obtenerSecuencialPorId(Integer _id) throws Exception {
        Query q = currentSession.createQuery("from Secuencial where idSecuencial=:_id");
        q.setParameter("_id", _id);
        return (Secuencial) q.uniqueResult();
    }

    public boolean actualizarCodigoEstablecimiento(Integer _idSecuencial, String _codigoEstablecimiento) {
        boolean actualizado = false;
        try {
            Secuencial s = (Secuencial) currentSession.load(Secuencial.class, _idSecuencial);
            s.setCodigoEstablecimientoSecuencial(_codigoEstablecimiento);
            currentSession.merge(s);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }

    public boolean actualizarPuntoEmision(Integer _idSecuencial, String _puntoEmision) {
        boolean actualizado = false;
        try {
            Secuencial s = (Secuencial) currentSession.load(Secuencial.class, _idSecuencial);
            s.setPuntoEmisionSecuencial(_puntoEmision);
            currentSession.merge(s);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }

    public boolean datossecuanlesPersonales(Integer _idSecuencial, String factura, String notacredtio, String retencion) {
        boolean actualizado = false;
        try {
            Secuencial s = (Secuencial) currentSession.load(Secuencial.class, _idSecuencial);
            if (!factura.equals("")) {
                s.setSecuencialFacturaSecuencial(factura);
            }
            if (!notacredtio.equals("")) {
                s.setSecuencialNotaCreditoSecuencial(notacredtio);
            }
            if (!retencion.equals("")) {
                s.setSecuencialRetencionSecuencial(retencion);
            }
            currentSession.merge(s);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }

    public boolean actualizarDireccion(Integer _idSecuencial, String _direccion) {
        boolean actualizado = false;
        try {
            Secuencial s = (Secuencial) currentSession.load(Secuencial.class, _idSecuencial);
            s.setDireccionSecuencial(_direccion);
            currentSession.merge(s);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }
}
