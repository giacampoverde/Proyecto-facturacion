/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.dao;


import ec.discheca.configuracion.DAO;
import ec.discheca.modelo.ComprobanteElectronico;
import ec.discheca.modelo.ComprobanteElectronicoPendiente;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author Ricardo Delgado
 */
public class DAOComprobanteElectronicoPendiente extends DAO{
    public DAOComprobanteElectronicoPendiente() throws Exception {
        super(DAO.$MODULO_DISCHECA);
    }

    public DAOComprobanteElectronicoPendiente(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOComprobanteElectronicoPendiente(DAO dao) throws Exception {
        super(dao);
    }
    
     public List<ComprobanteElectronicoPendiente> obtenerComprobantePendiente(){
        Query q=currentSession.createQuery("from ComprobanteElectronicoPendiente  where  estadoComprobanteElectronicoPendiente!=:estado");
         q.setParameter("estado", "11");
        return q.list();
    }
    
    public boolean insertarComprobantePendiente(ComprobanteElectronicoPendiente pendiente){
        currentSession.save(pendiente);
        return true;
    }
       public boolean guardarComprobantePendiente(ComprobanteElectronico _comprobanteElectronico, Date _fechaEnvio, String _rutaComprobantePendiente, String _mensaje, String _estado) throws Exception {
        boolean guardado = false;
        try {
            ComprobanteElectronicoPendiente cep = new ComprobanteElectronicoPendiente(_comprobanteElectronico, _fechaEnvio, _rutaComprobantePendiente, _mensaje, _estado);
            currentSession.save(cep);
            guardado = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return guardado;
    }
    public boolean actualizarFechaEnvio(String id, Date fechaEnvioPendiente){
        boolean actualizar=false;
        
        try{
        ComprobanteElectronicoPendiente comprobantePendiente=(ComprobanteElectronicoPendiente) currentSession.load(id, fechaEnvioPendiente);
        comprobantePendiente.setFechaEnvioComprobanteElectronicoPendiente(fechaEnvioPendiente);
        currentSession.merge(comprobantePendiente);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarRutaComprobantePendiente(String id, String rutaComprobante){
        boolean actualizar=false;
        
        try{
        ComprobanteElectronicoPendiente comprobantePendiente=(ComprobanteElectronicoPendiente) currentSession.load(id, rutaComprobante);
        comprobantePendiente.setRutaComprobanteElectronicoPendiente(rutaComprobante);
        currentSession.merge(comprobantePendiente);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
    
    public boolean actualizarMensajeComprobantePendiente(String id, String mensaje){
        boolean actualizar=false;
        
        try{
        ComprobanteElectronicoPendiente comprobantePendiente=(ComprobanteElectronicoPendiente) currentSession.load(id, mensaje);
        comprobantePendiente.setMensajeComprobanteElectronicoPendiente(mensaje);
        currentSession.merge(comprobantePendiente);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
public boolean actualizarEstado(Integer id, String estado, String mensaje) {
        boolean actualizado = false;
        try {
            ComprobanteElectronicoPendiente cep = (ComprobanteElectronicoPendiente) currentSession.load(ComprobanteElectronicoPendiente.class, id);
            if (cep != null) {
                cep.setEstadoComprobanteElectronicoPendiente(estado);
                cep.setMensajeComprobanteElectronicoPendiente(mensaje);
                cep.setFechaEnvioComprobanteElectronicoPendiente(new Date());
                currentSession.merge(cep);
                actualizado = true;
            }
        } catch (Exception e) {

        }
        return actualizado;

    }
    public boolean actualizarEstadoComprobantePendiente(String id, String estadoComprobante){
        boolean actualizar=false;
        
        try{
        ComprobanteElectronicoPendiente comprobantePendiente=(ComprobanteElectronicoPendiente) currentSession.load(id, estadoComprobante);
        comprobantePendiente.setEstadoComprobanteElectronicoPendiente(estadoComprobante);
        currentSession.merge(comprobantePendiente);
        actualizar=true;
        }catch(HibernateException e){
            
        }
        
        return actualizar;
    }
public boolean eliminarComprobantePendiente(Integer id) {
        boolean eliminado = false;
        try {
            ComprobanteElectronicoPendiente cep = (ComprobanteElectronicoPendiente) currentSession.load(ComprobanteElectronicoPendiente.class, id);
            currentSession.delete(cep);
            eliminado = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return eliminado;
    }
}
