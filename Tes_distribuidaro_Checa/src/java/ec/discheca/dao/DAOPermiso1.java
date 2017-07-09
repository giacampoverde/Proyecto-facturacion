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
import ec.discheca.modelo.UsuarioAcceso;
import java.util.List;
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
public class DAOPermiso1 extends DAO {

    public DAOPermiso1() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOPermiso1(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOPermiso1(DAO dao) throws Exception {
        super(dao);
    }

    public List<Producto> obtenerPermiso() {
        Query q = currentSession.createQuery("from Producto");
        return q.list();
    }

    public List<Producto> obtenerPermisosql() {
        
        Query q = currentSession.createSQLQuery("select *from producto");
        return q.list();
    }

    public List buscarProductosInternosVarios(String _estado, String codigo, String descripcion) {
        Criteria criteria_busqueda = this.buscarProductosInternosVariosParametros(_estado, codigo, descripcion);

        return criteria_busqueda.list();
    }

    public List<Producto> buscarProductosInternoslikesql(String _codigoProducto) {
        
        Query q = currentSession.createSQLQuery("select *from producto where estado=:_estado and codigoProducto like :_codigoProducto");
        q.setParameter("_estado", "1");
        q.setParameter("_codigoProducto","%"+_codigoProducto+"%");
        return q.list();
    }

    public List buscarProductosInternoslike(String _estado, String codigo, String descripcion) {
        Criteria criteria_busqueda = this.buscarProductosInternosVariosParalike(_estado, codigo, descripcion);

        return criteria_busqueda.list();
    }

    private Criteria buscarProductosInternosVariosParalike(String _estado, String codigo, String descripcion) {
        Criteria criteria_query = currentSession.createCriteria(Producto.class, "producto");
        if (!_estado.equals("-1")) {
            criteria_query.add(Restrictions.eq("producto.estado", _estado));
        }
        if (!codigo.equals("")) {

            criteria_query.add(Restrictions.like("producto.codigoProducto", codigo+"%"));
        }
        if (!descripcion.equals("")) {
            criteria_query.add(Restrictions.like("producto.descripcion", "%"+descripcion+"%"));
        }
        return criteria_query;
    }

    private Criteria buscarProductosInternosVariosParametros(String _estado, String codigo, String descripcion) {
        Criteria criteria_query = currentSession.createCriteria(Producto.class, "producto");
        if (!_estado.equals("-1")) {
            criteria_query.add(Restrictions.eq("producto.estado", _estado));
        }
        if (!codigo.equals("")) {
            criteria_query.add(Restrictions.eq("producto.codigoProducto", codigo));
        }
        if (!descripcion.equals("")) {
            criteria_query.add(Restrictions.eq("producto.descripcion", descripcion));
        }
        return criteria_query;
    }

    public Long obtenerTotalProductoInternosPorCriteriaComprobantesVariosParametros(String _estado, String cedula, String descripcion) {
        Criteria criteria_query = buscarProductosInternosVariosParametros(_estado, cedula, descripcion);
        Long total = Long.parseLong(criteria_query.setProjection(Projections.rowCount()).uniqueResult().toString());
        return total;
    }

    public boolean cambiarEstadoProducto(Integer _idProducto, String _nuevoEstado) {
        boolean actualizado = false;
        try {
            Producto p = (Producto) currentSession.load(Producto.class, _idProducto);
            p.setEstado(_nuevoEstado);
            currentSession.merge(p);
            actualizado = true;
        } catch (HibernateException e) {
        }
        return actualizado;

    }

    public Producto obtenerProductoporId(String codProdu) {
        Query q = currentSession.createQuery("from Producto where codigoProducto:=codProdu");
        q.setParameter("codigoProducto", codProdu);
        return (Producto) q.uniqueResult();
    }

    public Producto insertarProducto(Producto producto) {
        return (Producto) currentSession.merge(producto);
    }

    public Producto obtenerProductoPorCodigoPrincipal(String _codigoPrincipal) throws Exception {
        Query q = currentSession.createQuery("from Producto where codigoProducto=:_codigoPrincipal ");
        q.setParameter("_codigoPrincipal", _codigoPrincipal);
        return (Producto) q.uniqueResult();
    }
//antes

    public boolean actualizarProducto(Integer _idProducto, Producto _nuevoProducto) {
        boolean actualizado = false;
        try {
            Producto p = (Producto) currentSession.load(Producto.class, _idProducto);
            if (_nuevoProducto.getCodigoProducto() != null) {
                if (!_nuevoProducto.getCodigoProducto().equals("")) {
                    p.setCodigoProducto(_nuevoProducto.getCodigoProducto());
                }
            }
            if (_nuevoProducto.getDescripcion() != null) {
                if (!_nuevoProducto.getDescripcion().equals("")) {
                    p.setDescripcion(_nuevoProducto.getDescripcion());
                }
            }
            if (_nuevoProducto.getUnidadMedida() != null) {
                if (!_nuevoProducto.getUnidadMedida().equals("")) {
                    p.setUnidadMedida(_nuevoProducto.getUnidadMedida());
                }
            }
            if (_nuevoProducto.getCantidad() != null) {
                if (!_nuevoProducto.getCantidad().equals("")) {
                    p.setCantidad(_nuevoProducto.getCantidad());
                }
            }
            if (_nuevoProducto.getPrecioCosto() != null) {
                if (!_nuevoProducto.getPrecioCosto().equals("")) {
                    p.setPrecioCosto(_nuevoProducto.getPrecioCosto());
                }
            }
            if (_nuevoProducto.getPorcentajeVenta() != null) {
                if (!_nuevoProducto.getPorcentajeVenta().equals("")) {
                    p.setPorcentajeVenta(_nuevoProducto.getPorcentajeVenta());
                }
            }
            if (_nuevoProducto.getPorcentajeVentaDos() != null) {
                if (!_nuevoProducto.getPorcentajeVentaDos().equals("")) {
                    p.setPorcentajeVentaDos(_nuevoProducto.getPorcentajeVentaDos());
                }
            }
            if (_nuevoProducto.getPrecioVenta() != null) {
                if (!_nuevoProducto.getPrecioVenta().equals("")) {
                    p.setPrecioVenta(_nuevoProducto.getPrecioVenta());
                }
            }
            if (_nuevoProducto.getPrecioVentaDos() != null) {
                if (!_nuevoProducto.getPrecioVentaDos().equals("")) {
                    p.setPrecioVentaDos(_nuevoProducto.getPrecioVentaDos());
                }
            }

            currentSession.merge(p);
            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return actualizado;

    }

    public Producto actualizarProducto2(Integer _idProducto, Producto _nuevoProducto) {
        boolean actualizado = false;
        Producto p = (Producto) currentSession.load(Producto.class, _idProducto);
        try {

            if (_nuevoProducto.getCodigoProducto() != null) {
                if (!_nuevoProducto.getCodigoProducto().equals("")) {
                    p.setCodigoProducto(_nuevoProducto.getCodigoProducto());
                }
            }
            if (_nuevoProducto.getDescripcion() != null) {
                if (!_nuevoProducto.getDescripcion().equals("")) {
                    p.setDescripcion(_nuevoProducto.getDescripcion());
                }
            }
            if (_nuevoProducto.getUnidadMedida() != null) {
                if (!_nuevoProducto.getUnidadMedida().equals("")) {
                    p.setUnidadMedida(_nuevoProducto.getUnidadMedida());
                }
            }
            if (_nuevoProducto.getCantidad() != null) {
                if (!_nuevoProducto.getCantidad().equals("")) {
                    p.setCantidad(_nuevoProducto.getCantidad());
                }
            }
            if (_nuevoProducto.getPrecioCosto() != null) {
                if (!_nuevoProducto.getPrecioCosto().equals("")) {
                    p.setPrecioCosto(_nuevoProducto.getPrecioCosto());
                }
            }
            if (_nuevoProducto.getPorcentajeVenta() != null) {
                if (!_nuevoProducto.getPorcentajeVenta().equals("")) {
                    p.setPorcentajeVenta(_nuevoProducto.getPorcentajeVenta());
                }
            }
            if (_nuevoProducto.getPorcentajeVentaDos() != null) {
                if (!_nuevoProducto.getPorcentajeVentaDos().equals("")) {
                    p.setPorcentajeVentaDos(_nuevoProducto.getPorcentajeVentaDos());
                }
            }
            if (_nuevoProducto.getPrecioVenta() != null) {
                if (!_nuevoProducto.getPrecioVenta().equals("")) {
                    p.setPrecioVenta(_nuevoProducto.getPrecioVenta());
                }
            }
            if (_nuevoProducto.getPrecioVentaDos() != null) {
                if (!_nuevoProducto.getPrecioVentaDos().equals("")) {
                    p.setPrecioVentaDos(_nuevoProducto.getPrecioVentaDos());
                }
            }

            actualizado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (Producto) currentSession.merge(p);

    }
}
