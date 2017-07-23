/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.utilidades;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;

/**
 *
 * @author Ricardo Delgado
 */
public class HibernateSessionHandler {

    private SessionFactory discheca;


    public HibernateSessionHandler() {
        discheca = HibernateUtil.getSessionEgastos();


        try {
            discheca.getCurrentSession().beginTransaction();
         

        } catch (Exception e) {
            Logger.getLogger(HibernateSessionHandler.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void close() {
        try {
            
            discheca.getCurrentSession().getTransaction().commit();
       
            try {
                discheca.getCurrentSession().disconnect();
              
            } catch (Exception e) {
                Logger.getLogger(HibernateSessionHandler.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            }

        } catch (Exception e) {
            if (discheca.getCurrentSession().getTransaction().isActive()) {
                Logger.getLogger(HibernateSessionHandler.class.getName()).log(Level.SEVERE, "Trying to rollback database transaction after exception. Módulo Usuarios.");
                discheca.getCurrentSession().getTransaction().rollback();
            }
      
            Logger.getLogger(HibernateSessionHandler.class.getName()).log(Level.SEVERE, e.getMessage(), e);
        }

    }
}
