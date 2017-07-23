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
    public static String  NOMBRE_AMBIENTE="AMBIENTE";
    public static String  VALOR_AMBIENTE="";
    public static String NOMBRE_MESES_GRACIAS="MESESGRACIA";
    public static String VALOR_MESES_GRACIAS="";
    public static String NOMBRE_NUMERO_MAXIMO_COMPROBANTES="NUMEROMAXIMOCOMPROBANTES";
    public static String VALOR_NUMERO_MAXIMO_COMPROBANTES="";
    public static String NOMBRE_NUMERO_CUENTA1="CUENTAEMPRESA1";
    public static String VALOR_NUMERO_CUENTA1="";
    public static String NOMBRE_NUMERO_CUENTA2="CUENTAEMPRESA2";
    public static String VALOR_NUMERO_CUENTA2="";
    public static String NOMBRE_NUMERO_CUENTA3="CUENTAEMPRESA3";
    public static String VALOR_NUMERO_CUENTA3="";

    public static String NOMBRE_CONEXIONES="STRINGCONEXION";
    public static String VALOR_CONEXION="";
    public static String NOMBRE_REPORTETOTAL="RUTAREPORTETOTAL";
    public static String VALOR_REPORTETOTAL="";
    public static String NOMBRE_REPORTESECCION="RUTAREPORTESECCION";
    public static String VALOR_REPORTESECCION="";
    public static String NOMBRE_REPORTEEMPRESA="RUTAREPOREMPRESA";
    public static String VALOR_REPORTEEMPRESA="";
    public static String NOMBRE_RERPOTERENTA="RUTAIMPESTORENTA";
    public static String VALOR_RERPOTERENTA="";
    public static String NOMBRE_USUARIOREPORTE="USERREPORTE";
    public static String VALOR_USUARIOREPORTE="";
    public static String NOMBRE_CONTRAREPORTE="PASREPORTE";
    public static String VALOR_CONTRAREPORTE="";
    public static String NOMBRE_RUTAEXCEL="RUTAEXCEL";
    public static String VALOR_RUTAEXCEL="";


    //    public static String rutaRecepcion="C:\\recepcionComprobantesDisCheca\\";
//    public static String serv_directorio_xmls="C:\\recepcionComprobantesDisCheca\\discheca\\";
    public static String AMBITO = "pruebas";
    public static String properties_file = "com.egastos." + AMBITO + ".properties";
//    public static final String NOMBRE_RUC_EMISOR = "RUC_EMISOR";
//    public static String VALOR_RUC_EMISOR = "";

    //Identificadores de tipos de comprobantes
    public static final String FACTURA = "01";
    public static final String NOTA_CREDITO = "04";
    public static final String NOTA_DEBITO = "05";
    public static final String GUIA_REMISION = "06";
    public static final String COMPROBANTE_RETENCION = "07";

//    public static final String MONEDA = "DOLAR";

//    public static String AMBIENTE = "1";

    /**
     * Directorios
     */
    public static final String NOMBRE_DIRECTORIO_CREACION_XMLS = "DIRECTORIO_CREACION_XMLS";
    public static final String NOMBRE_DIRECTORIO_TEMPORAL_ARCHIVOS = "DIRECTORIO_TEMPORAL_ARCHIVOS";
    public static String VALOR_DIRECTORIO_CREACION_XMLS = "";
    public static String VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS = "DIRECTORIO_TEMPORAL_ARCHIVOS";
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
    public static String VALOR_PLANTILLA_IMAGENES_CORREO = "";
    public static final String NOMBRE_PLANTILLA_HTML_CORREO = "PLANTILLA_CORREO_HTML";
    public static String VALOR_PLANTILLA_HTML_CORREO = "";
    /**
     * Parámetros de conexion para el correo de envío de notificaciones
     */
    public static final String NOMBRE_HOST_CORREO_NOTIFICACION = "HOST_CORREO_NOTIFICACION";
    //    public static String VALOR_HOST_CORREO_NOTIFICACION = "mocha3012.mochahost.com";
    public static String VALOR_HOST_CORREO_NOTIFICACION = "";

    public static final String NOMBRE_PUERTO_CORREO_NOTIFICACION = "PUERTO_CORREO_NOTIFICACION";
    public static String VALOR_PUERTO_CORREO_NOTIFICACION = "";

    public static final String NOMBRE_USUARIO_CORREO_NOTIFICACION = "USUARIO_CORREO_NOTIFICACION";
    //    public static String VALOR_USUARIO_CORREO_NOTIFICACION = "facturaelectronica@ebox.ec";
    public static String VALOR_USUARIO_CORREO_NOTIFICACION = "";

    public static final String NOMBRE_PASSWORD_CORREO_NOTIFICACION = "PASSWORD_CORREO_NOTIFICACION";
    //    public static String VALOR_PASSWORD_CORREO_NOTIFICACION = "bigdata2014";
    public static String VALOR_PASSWORD_CORREO_NOTIFICACION = "";

    public static final String NOMBRE_CUENTA_CORREO_NOTIFICACION = "CUENTA_CORREO_NOTIFICACION";
    public static String VALOR_CUENTA_CORREO_NOTIFICACION = "";

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
