/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;

/**
 *
 * @author Ricardo Delgado
 */
public class HibernateSessionRequestFilter implements javax.servlet.Filter {

    private SessionFactory discheca;

    public void init(FilterConfig filterConfig) throws ServletException {
        discheca = HibernateUtil.getSessionEgastos();

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            discheca.getCurrentSession().beginTransaction();

            chain.doFilter(request, response);

            discheca.getCurrentSession().getTransaction().commit();

            try {
                discheca.getCurrentSession().disconnect();

            } catch (Exception e) {

            }
        } catch (StaleObjectStateException staleEx) {

            throw staleEx;
        } catch (Throwable ex) {
            Logger.getLogger(HibernateSessionRequestFilter.class.getName()).log(Level.SEVERE, "Se realizara un rollback", ex);
            try {
                if (discheca.getCurrentSession().getTransaction().isActive()) {
                    Logger.getLogger(HibernateSessionRequestFilter.class.getName()).log(Level.SEVERE, "Trying to rollback database transaction after exception. Módulo Usuarios.");
                    discheca.getCurrentSession().getTransaction().rollback();
                }

                Logger.getLogger(HibernateSessionRequestFilter.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            } catch (Throwable rbEx) {
                Logger.getLogger(HibernateSessionRequestFilter.class.getName()).log(Level.SEVERE, "Could not rollback transaction after exception!", rbEx);
            }
        }
    }

    public void destroy() {
    }

}
