/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import org.hibernate.Session;


public abstract class DAO {

    public static final Integer $MODULO_EGASTOS = 1;

    protected Session currentSession;

    public DAO(Integer modulo) throws Exception {
        switch (modulo) {
            case 1:

                currentSession = HibernateUtil.getSessionEgastos().getCurrentSession();
                break; 
            default:
                currentSession = null;
                throw new IllegalStateException("No se ha escogido un módulo válido.");
        }
    }

    public DAO(Session session) throws Exception {
        currentSession = session;
    }

    public DAO(DAO dao) throws Exception {
        currentSession = dao.getCurrentSession();
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }
}
