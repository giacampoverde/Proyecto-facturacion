/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.dao;




import ec.discheca.configuracion.AES256;
import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Empresa;
import ec.discheca.modelo.Perfil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo Delgado
 */


public class DAOClienteEmpresa extends DAO {
    
    public DAOClienteEmpresa() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }
    
    public DAOClienteEmpresa(Session currentSession) throws Exception {
        super(currentSession);
    }
    
    public DAOClienteEmpresa(DAO dao) throws Exception {
        super(dao);
    }
    
    public Empresa obtenerClienteEmpresaPorId(String _identificacion) throws Exception {
        Query q = currentSession.createQuery("from Empresa where idEmpresa=:_identificacion");        
        q.setParameter("_identificacion", _identificacion);
        return (Empresa) q.uniqueResult();
        
    }
    
    public Empresa obtenerClienteEmpresaPorNombre(String _nombreEmpresa) throws Exception {
        Query q = currentSession.createQuery("from Empresa where idEmpresa=:_identificacion");
        q.setParameter("_identificacion", _nombreEmpresa);
        return (Empresa) q.uniqueResult();
        
    }
    
    public boolean insertarCliente(Empresa _cliente)
            throws Exception {
        currentSession.save(_cliente);
        return true;
    }
    
    public boolean actualizarClienteAcceso(String _idcliente, String _nombreUsuarioCliente, String _nombreComercialCliente, String _razonSocialCliente, String _direccionCleinteEmpresa, String _correpPrincipalCliente, String _telefonoPrincipalCliente, Boolean _obligadoContabilidadclienteEmpresa, String _numeroResolucionClienteEmpresa, Perfil perfil)
            throws Exception {
        Empresa u = (Empresa) currentSession.load(Empresa.class, _idcliente);
        if (u == null) {
            throw new Exception("No se ha encontrado el registro que se quiere actualizar.");
        }
        if (_nombreComercialCliente != null) {
            u.setNombreComercialClienteEmpresa(_nombreComercialCliente);
        }
        
        if (_razonSocialCliente != null) {
            u.setRazonSocialClienteEmpresa(_razonSocialCliente);
        }
        if (_direccionCleinteEmpresa != null) {
            u.setDireccionClienteEmpresa(_direccionCleinteEmpresa);
        }
        if (_correpPrincipalCliente != null) {
            u.setCorreoPrincipalClienteEmpresa(_correpPrincipalCliente);
            
        }
        if (_telefonoPrincipalCliente != null) {
            u.setTelefonoPrincipalClienteEmpresa(_telefonoPrincipalCliente);
            
        }
        if (_obligadoContabilidadclienteEmpresa != null) {
            u.setObligadoContabilidadClienteEmpresa(_obligadoContabilidadclienteEmpresa);
            
        }
        if (_numeroResolucionClienteEmpresa != null) {
            u.setNumeroResolucionClienteEmpresa(_numeroResolucionClienteEmpresa);
        }
     
        currentSession.merge(u);
        return true;
    }
    
    public byte[] obtenerLogoClienteEmpresaPorId(String _identificacion) throws Exception {
        Query q = currentSession.createQuery("select logoClienteEmpresa from Empresa where idEmpresa=:_identificacion");
        q.setParameter("_identificacion", _identificacion);
        return (byte[]) q.uniqueResult();
        
    }
    
    public Empresa validarEmpresaAcceso(String _nombreUsuarioAcceso, String _claveUsuarioAcceso) throws Exception {
        Empresa usuario = obtenerUsuarioAccesoPorNombre(_nombreUsuarioAcceso);
        _claveUsuarioAcceso = AES256.toAES256(_claveUsuarioAcceso);
        if (usuario != null && _claveUsuarioAcceso.equals(usuario.getClaveUsuarioClienteEmpresa())) {
            return usuario;
        }
        return null;
    }
    
    public boolean actualizarNombreComercial(String _identificacion, String _nombreComercial) {
        boolean actualizado = false;
        try {
            Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setNombreComercialClienteEmpresa(_nombreComercial);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }

    public boolean actualizarDireccion(String _identificacion, String _direccion) {
        boolean actualizado = false;
        try {
            Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setDireccionClienteEmpresa(_direccion);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }

    public boolean actualizarRazonSocial(String _identificacion, String _razonSocial) {
        boolean actualizado = false;
        try {
            Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setRazonSocialClienteEmpresa(_razonSocial);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }
    
    public boolean actualizarNumeroResolucion(String _identificacion, String _numeroResolucion) {
        boolean actualizado = false;
        try {
            Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setNumeroResolucionClienteEmpresa(_numeroResolucion);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }
    
    public boolean actualizarCorreo(String _identificacion, String _correo) {
        boolean actualizado = false;
        try {
            Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setCorreoPrincipalClienteEmpresa(_correo);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }
    
    public boolean actualizarTelefono(String _identificacion, String _telefono) {
        boolean actualizado = false;
        try {
           Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setTelefonoPrincipalClienteEmpresa(_telefono);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }
    
    public boolean actualizarObligadoContabilidad(String _identificacion, Boolean _obligadoContabilidad) {
        boolean actualizado = false;
        try {
            Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setObligadoContabilidadClienteEmpresa(_obligadoContabilidad);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }
    
 
    
    public boolean actualizarEstadoUsuario(String _identificacion, boolean _estado) {
        boolean actualizado = false;
        try {
          Empresa ce = (Empresa) currentSession.load(Empresa.class, _identificacion);
            ce.setEstadoClienteEmpresa(_estado);
            currentSession.merge(ce);
            actualizado = true;
        } catch (HibernateException e) {
            Logger.getLogger(DAOClienteEmpresa.class.getName()).log(Level.SEVERE, null, e);
        }
        return actualizado;
        
    }
    
    public Empresa obtenerUsuarioAccesoPorNombre(String _nombreEmpresaAcceso) throws Exception {
        Query q = currentSession.createQuery("from Empresa where nombreUsuarioClienteEmpresa=:_nombreEmpresaAcceso");
        q.setParameter("_nombreEmpresaAcceso", _nombreEmpresaAcceso);
        return (Empresa) q.uniqueResult();
    }
    
    public List<Empresa> obtenerTodosClientesEmpresa() throws Exception {
        Query q = currentSession.createQuery("from Empresa");
        return q.list();
    }
    
    public boolean actualizarClaveUsuarioClienteEmpresa(String _idUsuario, String _clave) {
        boolean actualizado = false;
        try {
            Empresa ce = (Empresa) currentSession.load(Empresa.class, _idUsuario);

            _clave = AES256.toAES256(_clave);
            ce.setClaveUsuarioClienteEmpresa(_clave);
            currentSession.merge(ce);
            actualizado = true;
        } catch (Exception ex) {
            Logger.getLogger(DAOUsuarioAcceso.class.getName()).log(Level.SEVERE, null, ex);
        }

        return actualizado;
    }
}
