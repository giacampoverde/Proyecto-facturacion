/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class InicializadorProyecto implements ServletContextListener {
    
      //este metodo se ejecuta cuando se inicia la aplicacion o bien el contexto.
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Valores.init();
        } catch (Exception ex) {
            Logger.getLogger(InicializadorProyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
        Logger.getLogger(InicializadorProyecto.class.getName()).log(Level.SEVERE, null, "Contexto iniciado RenSoft E-gastos");

    }

    //Este metodo se ejecuta cuando se destruye el contexo, o cuando undeployas la aplicacion.
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Logger.getLogger(InicializadorProyecto.class.getName()).log(Level.SEVERE, null, "Contexto destruido Floral");
    }

  
}
