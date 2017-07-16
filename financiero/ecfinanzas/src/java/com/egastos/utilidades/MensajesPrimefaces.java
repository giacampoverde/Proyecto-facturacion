/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.utilidades;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;


public class MensajesPrimefaces {
    
     public static void mostrarMensaje(FacesMessage.Severity severityMessage, String mensaje) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(severityMessage, null, mensaje));
    }
    
     public static void mostrarMensajeDialog(FacesMessage.Severity severityMessage, String mensaje) {
        FacesMessage message = new FacesMessage(severityMessage, "Mensaje", mensaje);

        RequestContext.getCurrentInstance().showMessageInDialog(message);
    }
      
}
