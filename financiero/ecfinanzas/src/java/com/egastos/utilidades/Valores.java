/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Delgado
 */
public class Valores {
    public static String numeroCuentaEmpresa;
    public static String numeroMaximoComprobantes;
    public static String mesesGracia;
    public static String rutaRecepcion="C:\\recepcionComprobantesDisCheca\\";
    public static String serv_directorio_xmls="C:\\recepcionComprobantesDisCheca\\discheca\\";
    public static String AMBITO = "rd";
    public static String properties_file = "com.egastos." + AMBITO + ".properties";
    public static final String NOMBRE_RUC_EMISOR = "RUC_EMISOR";
    public static String VALOR_RUC_EMISOR = "";

    //Identificadores de tipos de comprobantes
    public static final String FACTURA = "01";
    public static final String NOTA_CREDITO = "04";
    public static final String NOTA_DEBITO = "05";
    public static final String GUIA_REMISION = "06";
    public static final String COMPROBANTE_RETENCION = "07";

    public static final String MONEDA = "DOLAR";

    public static String AMBIENTE = "1";

    /**
     * Directorios
     */
    public static final String NOMBRE_DIRECTORIO_CREACION_XMLS = "DIRECTORIO_CREACION_XMLS";
    public static final String NOMBRE_DIRECTORIO_TEMPORAL_ARCHIVOS = "DIRECTORIO_TEMPORAL_ARCHIVOS";
//pruebas
    public static String VALOR_DIRECTORIO_CREACION_XMLS = "";
    //Produccion
     // public static String VALOR_DIRECTORIO_CREACION_XMLS = "/opt/xml/";
   //Pruebas
    public static String VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS = "DIRECTORIO_TEMPORAL_ARCHIVOS";
    //produccion
    //public static String VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS = "/opt/pdf/";
    public static final String NOMBRE_DIRECTORIO_CERTIFICADOS = "DIRECTORIO_CERTIFICADOS";
    public static String VALOR_DIRECTORIO_CERTIFICADOS = "";


    /**
     * Perfil para empresa
     */
    public static final String NOMBRE_PERFIL_EMPRESA = "PERFIL_EMPRESA";
    public static String VALOR_PERFIL_EMPRESA = "";

    /**
     * Correo para las notificaciones
     */
    public static final String NOTIFICACIONES = "NOTIFICACIONES";
    public static String VALOR_NOTIFICACIONES = "C:\\correo\\xhtml\\";
    
    /**
     * Tipo de emisión 1:Emisión normal 2:Emisión en contingencia
     */
    public static String VALOR_TIPO_EMISION = "1";
    public static final String NOMBRE_TIPO_EMISION = "TIPO_EMISION";
    /**
     * Fecha de salida a producción
     * 
     */
    public static String VALOR_FECHA_SALIDA_PRODUCCION="";
    public static final String NOMBRE_FECHA_SALIDA_PRODUCCION="FECHA_SALIDA_PRODUCCION";
    
    public static boolean comprobacionDiaria=false;
    /*Se pone este valor hasta que se tenga uno definido*/
    public static final String plantillas="C:\\correo\\xhtml\\";
    
    /*Directorios de las plantillas para los correos*/
    public static final String NOMBRE_PLANTILLA_IMAGENES_CORREO = "PLANTILLA_CORREO_IMAGENES";
    //pruebas
    public static String VALOR_PLANTILLA_IMAGENES_CORREO = "C:\\plantillas\\imagenes\\";
    //Produccion
//      public static String VALOR_PLANTILLA_IMAGENES_CORREO = "/opt/plantillas/imagenes/";
    public static final String NOMBRE_PLANTILLA_HTML_CORREO = "PLANTILLA_CORREO_HTML";
    //pruebas 
    public static String VALOR_PLANTILLA_HTML_CORREO = "C:\\plantillas\\html\\";
   //produccion
//    public static String VALOR_PLANTILLA_HTML_CORREO = "/opt/plantillas/html/";
    
    
    
    /**
     * Parámetros de conexion para el correo de envío de notificaciones
     */
    public static final String HOST_CORREO_NOTIFICACION = "HOST_CORREO_NOTIFICACION";
    public static String VALOR_HOST_CORREO_NOTIFICACION = "mocha3012.mochahost.com";

    public static final String PUERTO_CORREO_NOTIFICACION = "PUERTO_CORREO_NOTIFICACION";
    public static String VALOR_PUERTO_CORREO_NOTIFICACION = "587";

    public static final String USUARIO_CORREO_NOTIFICACION = "USUARIO_CORREO_NOTIFICACION";
    public static String VALOR_USUARIO_CORREO_NOTIFICACION = "facturaelectronica@ebox.ec";

    public static final String PASSWORD_CORREO_NOTIFICACION = "PASSWORD_CORREO_NOTIFICACION";
    public static String VALOR_PASSWORD_CORREO_NOTIFICACION = "bigdata2014";

    public static final String CUENTA_CORREO_NOTIFICACION = "CUENTA_CORREO_NOTIFICACION";
    public static String VALOR_CUENTA_CORREO_NOTIFICACION = "soporte@ecfinanzas.com";
    
    public static void init() {

         CargarPropiedades p = new CargarPropiedades();
        p.cargarPropiedades();
        HibernateUtil.init();
        CargarConfiguraciones cc = new CargarConfiguraciones();
        try {
            cc.cargarConfiguraciones();
        } catch (Exception ex) {
            Logger.getLogger(Valores.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
