/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.dao;

import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.Receptor;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Ricardo Delgado
 */
public class DAOClientes extends DAO {

    public DAOClientes() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOClientes(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOClientes(DAO dao) throws Exception {
        super(dao);
    }

    public Receptor obtenerClientePorIdentificacionYRucEmpresa(String _identificacionCliente) throws Exception {
        Query q = currentSession.createQuery("from Receptor where rucReceptor=:_rucCliente");
        q.setParameter("_rucCliente", _identificacionCliente);
        return (Receptor) q.uniqueResult();
    }

    public List<Receptor> obtenerTodosCliente() {
        Query q = currentSession.createQuery("from Receptor");
        return q.list();
    }

    public boolean insertarCliente(Receptor receptor) {
        currentSession.save(receptor);
        return true;
    }

    public Receptor insertarClienteUsuario(Receptor receptor) {

        return (Receptor) currentSession.merge(receptor);
    }

    public Receptor actualizarCliente(Integer _idCliente, Receptor _clienteActualizado) {
        Receptor p = new Receptor();
        try {
            p = (Receptor) currentSession.load(Receptor.class, _idCliente);
            if (_clienteActualizado.getRucReceptor() != null && !_clienteActualizado.getRucReceptor().trim().equals("")) {
                p.setRucReceptor(_clienteActualizado.getRucReceptor());
            }
            if (_clienteActualizado.getRazonSocialReceptor() != null && !_clienteActualizado.getRazonSocialReceptor().trim().equals("")) {
                p.setRazonSocialReceptor(_clienteActualizado.getRazonSocialReceptor());
            }
            if (_clienteActualizado.getTelefono() != null && !_clienteActualizado.getTelefono().trim().equals("")) {
                p.setTelefono(_clienteActualizado.getTelefono());
            }
            if (_clienteActualizado.getDireccion() != null && !_clienteActualizado.getDireccion().trim().equals("")) {
                p.setDireccion(_clienteActualizado.getDireccion());
            }
            if (_clienteActualizado.getCorreo() != null && !_clienteActualizado.getCorreo().trim().equals("")) {
                p.setCorreo(_clienteActualizado.getCorreo());
            }
            if (_clienteActualizado.getCorreoAdicional() != null && !_clienteActualizado.getCorreoAdicional().trim().equals("")) {
                p.setCorreoAdicional(_clienteActualizado.getCorreoAdicional());
            }

            p.setEstado(_clienteActualizado.getEstado());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Receptor) currentSession.merge(p);
    }

    public List buscarclientesInternosVarios(String _estado, String celula,String nombre) {
        Criteria criteria_busqueda = this.buscarclientesInternosVariosParametros(_estado, celula,nombre);

        return criteria_busqueda.list();
    }

    private Criteria buscarclientesInternosVariosParametros(String _estado, String cedula,String nombre) {
        Criteria criteria_query = currentSession.createCriteria(Receptor.class, "cliente");
        if (!_estado.equals("-1")) {
            criteria_query.add(Restrictions.eq("cliente.estado", _estado));
        }
        if (!cedula.equals("")) {
            criteria_query.add(Restrictions.eq("cliente.rucReceptor", cedula));
        }
         if (!nombre.equals("")) {
            criteria_query.add(Restrictions.eq("cliente.razonSocialReceptor", nombre));
        }
        return criteria_query;
    }

    public Receptor obtenerClientePorId(Integer _id) throws Exception {
        Query q = currentSession.createQuery("from Cliente where idCliente=:_id");
        q.setParameter("_id", _id);
        return (Receptor) q.uniqueResult();
    }

    public List<Receptor> obtenerClientePorRucCliente(String _ruc) throws Exception {
        Query q = currentSession.createQuery("from Cliente where ruc=:_ruc");
        q.setParameter("_ruc", _ruc);
        return q.list();
    }

}
