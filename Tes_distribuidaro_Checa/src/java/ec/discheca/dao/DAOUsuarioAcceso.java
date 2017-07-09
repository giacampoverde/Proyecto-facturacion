/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.dao;

import ec.discheca.configuracion.AES256;
import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Perfil;
import ec.discheca.modelo.UsuarioAcceso;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ricardo Delgado
 */
public class DAOUsuarioAcceso extends DAO {

    public DAOUsuarioAcceso() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOUsuarioAcceso(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOUsuarioAcceso(DAO dao) throws Exception {
        super(dao);
    }

    public List<UsuarioAcceso> obtenerUsuariosPorEmpresa(String _ruc) {

        Query q = currentSession.createQuery("select aue.usuarioAcceso from AsignacionUsuarioEmpresa aue where aue.clienteEmpresa.idClienteEmpresa=:_ruc");

        q.setParameter("_ruc", _ruc);
        return q.list();

    }

    public List<UsuarioAcceso> obtenerUsuario() {

        Query q = currentSession.createQuery("from UsuarioAcceso");

        return q.list();

    }

    public UsuarioAcceso obtenerUsuarioAccesoPorId(Integer _idUsuario) throws Exception {
        Query q = currentSession.createQuery("from UsuarioAcceso where idUsuario=:_idUsuario");
        q.setParameter("_idUsuario", _idUsuario);
        return (UsuarioAcceso) q.uniqueResult();
    }

    public UsuarioAcceso obtenerUsuarioAccesoPorNombre(String _nombreUsuarioAcceso) throws Exception {
        Query q = currentSession.createQuery("from UsuarioAcceso where nombreUsuarioAcceso=:_nombreUsuarioAcceso");
        q.setParameter("_nombreUsuarioAcceso", _nombreUsuarioAcceso);
        return (UsuarioAcceso) q.uniqueResult();
    }

    public UsuarioAcceso obtenerUsuarioPorRucCedula(String _identificacion) throws Exception {
        Query q = currentSession.createQuery("from UsuarioAcceso where identificacionUsuario=:_identificacion");
        q.setParameter("_identificacion", _identificacion);
        return (UsuarioAcceso) q.uniqueResult();
    }

    public UsuarioAcceso insertarUsuarioAcceso(UsuarioAcceso _usuario)
            throws Exception {
        return (UsuarioAcceso) currentSession.merge(_usuario);
    }

    public UsuarioAcceso validarUsuarioAcceso(String _nombreUsuarioAcceso, String _claveUsuarioAcceso) throws Exception {
        UsuarioAcceso usuario = obtenerUsuarioAccesoPorNombre(_nombreUsuarioAcceso);
        _claveUsuarioAcceso = AES256.toAES256(_claveUsuarioAcceso);
        if (usuario != null && _claveUsuarioAcceso.equals(usuario.getClaveUsuarioAcceso())) {
            return usuario;
        }
        return null;
    }

    public boolean actualizarClaveUsuarioAcceso(Integer _idUsuario, String _claveUsuarioAcceso) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);

            _claveUsuarioAcceso = AES256.toAES256(_claveUsuarioAcceso);

            ua.setClaveUsuarioAcceso(_claveUsuarioAcceso);
            currentSession.merge(ua);
            actualizado = true;
        } catch (Exception ex) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }

        return actualizado;
    }

    public boolean actualizarNombreUsuarioAcceso(Integer _idUsuario, String _nombre) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setNombreUsuarioAcceso(_nombre);
            currentSession.merge(ua);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public boolean actualizarNombreUsuario(Integer _idUsuario, String _nombre) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setNombreUsuario(_nombre);
            currentSession.merge(ua);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public boolean actualizarApellidoUsuario(Integer _idUsuario, String _apellido) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setApellidoUsuario(_apellido);
            currentSession.merge(ua);
            actualizado = true;
        } catch (Exception e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public boolean actualizarEstadoUsuario(Integer _idUsuario, String _estado) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setEstadoUsuario(_estado);
            currentSession.merge(ua);
            actualizado = true;
        } catch (Exception e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public boolean actualizarCorreoPrincipalUsuario(Integer _idUsuario, String _correoPrincipal) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setCorreoPrincipalUsuario(_correoPrincipal);
            currentSession.merge(ua);
            actualizado = true;
        } catch (Exception e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public boolean actualizarCorreoAdicionalUsuario(Integer _idUsuario, String _correoAdicional) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setCorreoAdicionalUsuario(_correoAdicional);
            currentSession.merge(ua);
            actualizado = true;
        } catch (Exception e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public boolean actualizarPerfilUsuario(Integer _idUsuario, Perfil _perfil) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setPerfil(_perfil);
            currentSession.merge(ua);
            actualizado = true;
        } catch (Exception e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public boolean actualizarTelefonoPrincipalUsuario(Integer _idUsuario, String _telefonoPrincipal) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setTelefonoPrincipalUsuario(_telefonoPrincipal);
            currentSession.merge(ua);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public List buscarUsuariosInternos(String _estado, Integer _firstResult, Integer _maxResults) {
        Criteria criteria_busqueda = this.buscarUsuariosInternos(_estado);
        criteria_busqueda.setFirstResult(_firstResult);
        criteria_busqueda.setMaxResults(_maxResults);
        return criteria_busqueda.list();
    }

    public List buscarUsuariosInternosVariosParametros(String _estado, String cedula, String nombre, Integer _firstResult, Integer _maxResults) {
        Criteria criteria_busqueda = this.buscarUsuariosInternosVariosParametros(_estado, cedula, nombre);
        criteria_busqueda.setFirstResult(_firstResult);
        criteria_busqueda.setMaxResults(_maxResults);
        return criteria_busqueda.list();
    }

    public Long obtenerTotalUsuariosInternosPorCriteriaComprobantesVariosParametros(String _estado, String cedula, String nombre) {
        Criteria criteria_query = buscarUsuariosInternosVariosParametros(_estado, cedula, nombre);
        Long total = Long.parseLong(criteria_query.setProjection(Projections.rowCount()).uniqueResult().toString());
        return total;
    }

    public Long obtenerTotalUsuariosInternosPorCriteria(String _estado) {
        Criteria criteria_query = buscarUsuariosInternos(_estado);
        Long total = Long.parseLong(criteria_query.setProjection(Projections.rowCount()).uniqueResult().toString());
        return total;
    }

    private Criteria buscarUsuariosInternosVariosParametros(String _estado, String cedula, String nombre) {
        Criteria criteria_query = currentSession.createCriteria(UsuarioAcceso.class, "UsuarioAcceso");
        if (!_estado.equals("-1")) {
            criteria_query.add(Restrictions.eq("UsuarioAcceso.estadoUsuario", _estado));
        }
        if (!cedula.equals("")) {
            criteria_query.add(Restrictions.eq("UsuarioAcceso.identificacionUsuario", cedula));
        }
        if (!nombre.equals("")) {
            criteria_query.add(Restrictions.eq("UsuarioAcceso.nombreUsuario", nombre));
        }
        return criteria_query;
    }

    private Criteria buscarUsuariosInternos(String _estado) {
        Criteria criteria_query = currentSession.createCriteria(UsuarioAcceso.class, "UsuarioAcceso");
        if (!_estado.equals("-1")) {
            criteria_query.add(Restrictions.eq("UsuarioAcceso.estadoUsuario", _estado));
        }
        return criteria_query;
    }

    public boolean actualizarTelefonoAdicionalUsuario(Integer _idUsuario, String _telefonoAdicional) {
        boolean actualizado = false;
        try {
            UsuarioAcceso ua = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
            ua.setTelefonoAdicionalUsuario(_telefonoAdicional);
            currentSession.merge(ua);
            actualizado = true;
        } catch (Exception e) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;

    }

    public UsuarioAcceso actualizarUsuario(Integer _idCliente, UsuarioAcceso _usuarioActualizado) {
        UsuarioAcceso p = new UsuarioAcceso();
        try {
            p = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idCliente);

            if (_usuarioActualizado.getNombreUsuarioAcceso() != null && !_usuarioActualizado.getNombreUsuarioAcceso().trim().equals("")) {
                p.setNombreUsuarioAcceso(_usuarioActualizado.getNombreUsuarioAcceso());
            }
            if (_usuarioActualizado.getNombreUsuario() != null && !_usuarioActualizado.getNombreUsuario().trim().equals("")) {
                p.setNombreUsuario(_usuarioActualizado.getNombreUsuario());
            }
            if (_usuarioActualizado.getApellidoUsuario() != null && !_usuarioActualizado.getApellidoUsuario().trim().equals("")) {
                p.setApellidoUsuario(_usuarioActualizado.getApellidoUsuario());
            }
            if (_usuarioActualizado.getTelefonoPrincipalUsuario() != null && !_usuarioActualizado.getTelefonoPrincipalUsuario().trim().equals("")) {
                p.setTelefonoPrincipalUsuario(_usuarioActualizado.getTelefonoPrincipalUsuario());
            }
            if (_usuarioActualizado.getTelefonoAdicionalUsuario() != null && !_usuarioActualizado.getTelefonoAdicionalUsuario().trim().equals("")) {
                p.setTelefonoAdicionalUsuario(_usuarioActualizado.getTelefonoAdicionalUsuario());
            }
            if (_usuarioActualizado.getCorreoPrincipalUsuario() != null && !_usuarioActualizado.getCorreoPrincipalUsuario().trim().equals("")) {
                p.setCorreoPrincipalUsuario(_usuarioActualizado.getCorreoPrincipalUsuario());
            }
            if (_usuarioActualizado.getCorreoAdicionalUsuario() != null && !_usuarioActualizado.getCorreoAdicionalUsuario().trim().equals("")) {
                p.setCorreoAdicionalUsuario(_usuarioActualizado.getCorreoAdicionalUsuario());
            }
            if (_usuarioActualizado.getEstadoUsuario() != null && !_usuarioActualizado.getEstadoUsuario().trim().equals("")) {
                p.setEstadoUsuario(_usuarioActualizado.getEstadoUsuario());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (UsuarioAcceso) currentSession.merge(p);
    }

//    public boolean cambiarEstadoUsuarioAcceso(Integer _idUsuario, String _estadoUsuario) throws Exception {
//        UsuarioAcceso u = (UsuarioAcceso) currentSession.load(UsuarioAcceso.class, _idUsuario);
//        if (u == null) {
//            throw new Exception("No se ha encontrado el registro que se quiere actualizar.");
//        }
//        u.setEstadoUsuario(_estadoUsuario);
//        currentSession.merge(u);
//        return true;
//    }
}
