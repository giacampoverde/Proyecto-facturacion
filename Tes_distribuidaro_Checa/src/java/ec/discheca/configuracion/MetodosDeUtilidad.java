/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ec.discheca.configuracion;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Usuario
 */
public class MetodosDeUtilidad {
        public static String generarcodigoenvioclave() {
        double aux;
        String clave = "";
        String claveAux = "";
        Integer veces;
        String numeros = "0123456789";
        String todo = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int i = 0; i < 1; i++) {
            aux = (Math.random()) * 10;
            clave = clave + numeros.charAt((int) aux);
        }
        for (int i = clave.length(); i <= 5; i++) {
            aux = (Math.random()) * 62;
            clave = clave + todo.charAt((int) aux);
        }
        veces = (int) Math.random() * 5;
        for (int j = 0; j <= veces; j++) {
            for (int i = 0; i < clave.length(); i += 2) {
                claveAux += clave.charAt(i);
            }
            for (int i = 1; i < clave.length(); i += 2) {
                claveAux += clave.charAt(i);
            }
            clave = claveAux;
            claveAux = "";
        }
        return clave;

    }
        
    public static String obtenerurl() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String[] partes = uri.split("\\/");
        if (partes.length < 2) {
            return "index.xhtml";
        } else {
            return partes[2];
        }
    }

    public static String obtenerDominio() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String uri = request.getRequestURI();
        String[] partes = url.split("\\/");

        return partes[1];

    }

    public static String obtenerDominiourl() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getRequestURL().toString();
        String[] partes = url.split("\\/");
        String finals = "";
        String pagina = partes[partes.length - 1];
        finals = url.replaceAll(pagina, "");
        return finals;
    }
}
