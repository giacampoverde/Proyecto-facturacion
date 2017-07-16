/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.utilidades;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Ricardo Delgado
 */
public class HibernateUtil {    

    private static final SessionFactory sesionEgastos;
   

    static {
        try {

            Configuration configuration = new AnnotationConfiguration();
            sesionEgastos = configuration.configure("/hibernate."+Valores.AMBITO+".cfg.xml").buildSessionFactory();
      

        } catch (Throwable ex) {
          
            Logger.getLogger(HibernateUtil.class.getName()).log(Level.SEVERE, "Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionEgastos() {
        return sesionEgastos;
    }


    public static void init() {
        Logger.getLogger(HibernateUtil.class.getName()).log(Level.INFO, "Se ha inicializado la sesión de Hibernate.");
    }

 
}
