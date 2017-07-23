/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import com.egastos.dao.ec.DAOConfiguracion;

public class CargarConfiguraciones {

    public void cargarConfiguraciones() throws Exception {
        HibernateSessionHandler hsh = new HibernateSessionHandler();
        DAOConfiguracion dao_configuracion = new DAOConfiguracion();
        Valores.VALOR_AMBIENTE = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_AMBIENTE, "0");
        Valores.VALOR_MESES_GRACIAS = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_MESES_GRACIAS, Valores.VALOR_AMBIENTE);
        Valores.VALOR_NUMERO_MAXIMO_COMPROBANTES = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_NUMERO_MAXIMO_COMPROBANTES, Valores.VALOR_AMBIENTE);
        Valores.VALOR_NUMERO_CUENTA1 = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_NUMERO_CUENTA1, Valores.VALOR_AMBIENTE);
        Valores.VALOR_NUMERO_CUENTA2 = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_NUMERO_CUENTA2, Valores.VALOR_AMBIENTE);
        Valores.VALOR_NUMERO_CUENTA2 = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_NUMERO_CUENTA3, Valores.VALOR_AMBIENTE);
        Valores.VALOR_DIRECTORIO_CREACION_XMLS = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_DIRECTORIO_CREACION_XMLS, Valores.VALOR_AMBIENTE);
        Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_DIRECTORIO_TEMPORAL_ARCHIVOS, Valores.VALOR_AMBIENTE);
        Valores.VALOR_PLANTILLA_IMAGENES_CORREO = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_PLANTILLA_IMAGENES_CORREO, Valores.VALOR_AMBIENTE);

        Valores.VALOR_PLANTILLA_HTML_CORREO = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_PLANTILLA_HTML_CORREO, Valores.VALOR_AMBIENTE);
        Valores.VALOR_HOST_CORREO_NOTIFICACION = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_HOST_CORREO_NOTIFICACION, Valores.VALOR_AMBIENTE);
        Valores.VALOR_PUERTO_CORREO_NOTIFICACION = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_PUERTO_CORREO_NOTIFICACION, Valores.VALOR_AMBIENTE);
        Valores.VALOR_USUARIO_CORREO_NOTIFICACION = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_USUARIO_CORREO_NOTIFICACION, Valores.VALOR_AMBIENTE);
        Valores.VALOR_PASSWORD_CORREO_NOTIFICACION = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_PASSWORD_CORREO_NOTIFICACION, Valores.VALOR_AMBIENTE);
        Valores.VALOR_CUENTA_CORREO_NOTIFICACION = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_CUENTA_CORREO_NOTIFICACION, Valores.VALOR_AMBIENTE);

        Valores.VALOR_CONEXION = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_CONEXIONES, Valores.VALOR_AMBIENTE);
        Valores.VALOR_REPORTETOTAL = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_REPORTETOTAL, Valores.VALOR_AMBIENTE);
        Valores.VALOR_REPORTESECCION = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_REPORTESECCION, Valores.VALOR_AMBIENTE);
        Valores.VALOR_REPORTEEMPRESA = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_REPORTEEMPRESA, Valores.VALOR_AMBIENTE);

        Valores.VALOR_RERPOTERENTA = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_RERPOTERENTA, Valores.VALOR_AMBIENTE);
        Valores.VALOR_USUARIOREPORTE = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_USUARIOREPORTE, Valores.VALOR_AMBIENTE);
        Valores.VALOR_CONTRAREPORTE = dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_CONTRAREPORTE, Valores.VALOR_AMBIENTE);
        Valores.VALOR_RUTAEXCEL=dao_configuracion.obtenerValorConfiguracion(Valores.NOMBRE_RUTAEXCEL, Valores.VALOR_AMBIENTE);

//        Valores.VALOR_DIRECTORIO_CREACION_XMLS = dao_configuracion.obtenerConfiguracionPorNombre(Valores.NOMBRE_DIRECTORIO_CREACION_XMLS, Valores.AMBIENTE).getValorConfiguracion();
//        Valores.VALOR_RUC_EMISOR = dao_configuracion.obtenerConfiguracionPorNombre(Valores.NOMBRE_RUC_EMISOR, Valores.AMBIENTE).getValorConfiguracion();
//        Valores.VALOR_DIRECTORIO_TEMPORAL_ARCHIVOS=dao_configuracion.obtenerConfiguracionPorNombre(Valores.NOMBRE_DIRECTORIO_TEMPORAL_ARCHIVOS, Valores.AMBIENTE).getValorConfiguracion();
//        Valores.VALOR_RUC_EMISOR="1704206174001";
        hsh.close();
    }
}
