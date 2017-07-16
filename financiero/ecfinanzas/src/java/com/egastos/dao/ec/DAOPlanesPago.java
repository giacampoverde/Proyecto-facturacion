/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.dao.ec;

import com.egastos.modelo.ec.Planespago;
import com.egastos.utilidades.DAO;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author ricar
 */
public class DAOPlanesPago extends DAO{
    public DAOPlanesPago() throws Exception {
        super(DAO.$MODULO_EGASTOS);
    }

    public DAOPlanesPago(Session currentSession) throws Exception {
        super(currentSession);
    }

    public DAOPlanesPago(DAO dao) throws Exception {
        super(dao);
    }
     public List<Planespago> obtenerPlanesPago(){
        Query q=currentSession.createQuery("from Planespago where  valor>:valor order by meses asc");
         q.setParameter("valor",new Double(0));
        return q.list();
    }
}
