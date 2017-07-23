/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.correo;

import com.egastos.utilidades.Valores;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricar
 */
public class prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {
            List<String> imagenes = new ArrayList<String>();
            List<String> contenido=new ArrayList<String>();
            contenido.add("1");
            contenido.add("1");
            contenido.add("1");
            contenido.add("1");
            contenido.add("1");
            contenido.add("1");
            List<String> correos = new ArrayList<String>();
            correos.add("ricardo.telcomp@hotmail.com");
            imagenes.add("cab");
            imagenes.add("pie");
            Correo correo = new Correo("1");
            correo.enviarMailReguistroCheca("ricardo.telcomp@hotmail.com", contenido, Valores.VALOR_PLANTILLA_HTML_CORREO + File.separator + "registroUsuario.html", Valores.VALOR_PLANTILLA_IMAGENES_CORREO + File.separator, "Bienvenido Al Portal DisCheca", imagenes);
        } catch (IOException ex) {
            //Logger.getLogger(pruebanoti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    

