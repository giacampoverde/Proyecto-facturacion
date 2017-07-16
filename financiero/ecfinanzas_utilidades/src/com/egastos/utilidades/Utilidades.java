/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.utilidades;

import com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 * @author Ricardo Delgado
 */
public class Utilidades {

    public static String obtFechaSeg() {
        try {
            SimpleDateFormat formSD = new SimpleDateFormat("yyyyMMddhhmmss");
            formSD.setLenient(false);
            Date fecha = new Date();
            String fechaStr = formSD.format(fecha);
            return fechaStr;
        } catch (Exception ex) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", ex);
            return null;
        }
    }

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

    public static String obtFechaDDMMYYYY(Date d) {
        try {
            SimpleDateFormat formSD = new SimpleDateFormat("ddMMyyyy");
            formSD.setLenient(false);
            String fechaStr = formSD.format(d);
            return fechaStr;
        } catch (Exception ex) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", ex);
            return null;
        }
    }

    public static String obtenerFechaFormatoddMMyyyy(Date d) {
        try {
            SimpleDateFormat formSD = new SimpleDateFormat("yyyy-MM-dd");
            formSD.setLenient(false);
            String fechaStr = formSD.format(d);
            return fechaStr;
        } catch (Exception ex) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", ex);
            return null;
        }
    }

    public static Date cadAFecha(String cadenaFecha) {
        Date fecha = null;
        SimpleDateFormat fechaSD = new SimpleDateFormat("dd/MM/yyyy");
        try {
            fecha = fechaSD.parse(cadenaFecha);
        } catch (ParseException pe) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", pe);
        }
        return fecha;
    }

    public static Date parsearCadenaFechaFormatoDiaMesAnio(String _cadenaFecha) {
        Date fecha = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
       
        try {
            fecha = sdf.parse(_cadenaFecha);
        } catch (ParseException pe) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", pe);
        }
        return fecha;
    }

    public static String obtFechaDMAHMS(Date cadenaFecha) {
        try {
            String fechaStr = null;
            SimpleDateFormat formSD = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            formSD.setLenient(false);
            fechaStr = formSD.format(cadenaFecha);
            return fechaStr;
        } catch (Exception ex) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", ex);
            return null;
        }
    }

    public static String obtenerFechaFormatoMySQL(Date cadenaFecha) {
        try {
            String fechaStr = null;
            SimpleDateFormat formSD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            formSD.setLenient(false);
            fechaStr = formSD.format(cadenaFecha);
            return fechaStr;
        } catch (Exception ex) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", ex);
            return null;
        }
    }

    public static LinkedHashMap<String, LinkedHashMap<String, String>> obtTipoCompr() {
        LinkedHashMap<String, LinkedHashMap<String, String>> datosTipoCompr = new LinkedHashMap<>();

        LinkedHashMap<String, String> datos1 = new LinkedHashMap<>();
        datos1.put("01", "Factura");
        datos1.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos1.put("04", "Nota de crédito");
        datos1.put("05", "Nota de débito");
        datos1.put("11", "Pasajes expedidos por empresas de aviación");
        datos1.put("12", "Documentos emitidos por instituciones financieras");
        datos1.put("21", "Carta de Porte Aéreo");
        datos1.put("41", "Comprobante de venta emitido por reembolso");
        datos1.put("43", "Liquidación para Explotación y Exploración de Hidrocarburos");
        datos1.put("47", "Nota de Crédito por Reembolso Emitida por Intermediario");
        datos1.put("48", "Nota de Débito por Reembolso Emitida por Intermediario");

        LinkedHashMap<String, String> datos2 = new LinkedHashMap<>();
        datos2.put("01", "Factura");
        datos2.put("02", "Nota o boleta de venta");
        datos2.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos2.put("04", "Nota de crédito");
        datos2.put("05", "Nota de débito");
        datos2.put("11", "Pasajes expedidos por empresas de aviación");
        datos2.put("12", "Documentos emitidos por instituciones financieras");
        datos2.put("15", "Comprobante de venta emitido en el Exterior");
        datos2.put("19", "Comprobantes de Pago de Cuotas o Aportes");
        datos2.put("20", "Documentos por Servicios Administrativos emitidos por Inst. del Estado");
        datos2.put("21", "Carta de Porte Aéreo");
        datos2.put("41", "Comprobante de venta emitido por reembolso");
        datos2.put("43", "Liquidación para Explotación y Exploración de Hidrocarburos");
        datos2.put("47", "Nota de Crédito por Reembolso Emitida por Intermediario");
        datos2.put("48", "Nota de Débito por Reembolso Emitida por Intermediario");

        LinkedHashMap<String, String> datos3 = new LinkedHashMap<>();
        datos3.put("01", "Factura");
        datos3.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos3.put("04", "Nota de crédito");
        datos3.put("05", "Nota de débito");
        datos3.put("41", "Comprobante de venta emitido por reembolso");
        datos3.put("47", "Nota de Crédito por Reembolso Emitida por Intermediario");
        datos3.put("48", "Nota de Débito por Reembolso Emitida por Intermediario");

        LinkedHashMap<String, String> datos4 = new LinkedHashMap<>();
        datos4.put("01", "Factura");
        datos4.put("02", "Nota o boleta de venta");
        datos4.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos4.put("04", "Nota de crédito");
        datos4.put("05", "Nota de débito");
        datos4.put("15", "Comprobante de venta emitido en el Exterior");
        datos4.put("41", "Comprobante de venta emitido por reembolso");
        datos4.put("47", "Nota de Crédito por Reembolso Emitida por Intermediario");
        datos4.put("48", "Nota de Débito por Reembolso Emitida por Intermediario");

        LinkedHashMap<String, String> datos5 = new LinkedHashMap<>();
        datos5.put("01", "Factura");
        datos5.put("02", "Nota o boleta de venta");
        datos5.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos5.put("04", "Nota de crédito");
        datos5.put("05", "Nota de débito");
        datos5.put("11", "Pasajes expedidos por empresas de aviación");
        datos5.put("15", "Comprobante de venta emitido en el Exterior");
        datos5.put("41", "Comprobante de venta emitido por reembolso");

        LinkedHashMap<String, String> datos6 = new LinkedHashMap<>();
        datos6.put("01", "Factura");
        datos6.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos6.put("04", "Nota de crédito");
        datos6.put("05", "Nota de débito");
        datos6.put("41", "Comprobante de venta emitido por reembolso");
        datos6.put("43", "Liquidación para Explotación y Exploración de Hidrocarburos");
        datos6.put("47", "Nota de Crédito por Reembolso Emitida por Intermediario");
        datos6.put("48", "Nota de Débito por Reembolso Emitida por Intermediario");

        LinkedHashMap<String, String> datos7 = new LinkedHashMap<>();
        datos7.put("01", "Factura");
        datos7.put("02", "Nota o boleta de venta");
        datos7.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos7.put("04", "Nota de crédito");
        datos7.put("05", "Nota de débito");
        datos7.put("15", "Comprobante de venta emitido en el Exterior");
        datos7.put("41", "Comprobante de venta emitido por reembolso");
        datos7.put("43", "Liquidación para Explotación y Exploración de Hidrocarburos");
        datos7.put("47", "Nota de Crédito por Reembolso Emitida por Intermediario");
        datos7.put("48", "Nota de Débito por Reembolso Emitida por Intermediario");

        LinkedHashMap<String, String> datos8 = new LinkedHashMap<>();
        datos8.put("01", "Factura");
        datos8.put("02", "Nota o boleta de venta");
        datos8.put("03", "Liquidación de compra de Bienes o Prestación de servicios");
        datos8.put("04", "Nota de crédito");
        datos8.put("05", "Nota de débito");
        datos8.put("21", "Carta de Porte Aéreo");

        LinkedHashMap<String, String> datos9 = new LinkedHashMap<>();
        datos9.put("04", "Nota de crédito");
        datos9.put("05", "Nota de débito");
        datos9.put("45", "Liquidación por reclamos de aseguradoras");

        LinkedHashMap<String, String> datos10 = new LinkedHashMap<>();
        datos10.put("19", "Comprobantes de Pago de Cuotas o Aportes");

        LinkedHashMap<String, String> datos0 = new LinkedHashMap<>();
        datos0.put("01", "Factura");
        datos0.put("02", "Nota o boleta de venta");
        datos0.put("04", "Nota de crédito");
        datos0.put("05", "Nota de débito");
        datos0.put("42", "Documento agente de retención Presuntiva");

        datosTipoCompr.put("01", datos1);
        datosTipoCompr.put("02", datos2);
        datosTipoCompr.put("03", datos3);
        datosTipoCompr.put("04", datos4);
        datosTipoCompr.put("05", datos5);
        datosTipoCompr.put("06", datos6);
        datosTipoCompr.put("07", datos7);
        datosTipoCompr.put("08", datos8);
        datosTipoCompr.put("09", datos9);
        datosTipoCompr.put("10", datos10);
        datosTipoCompr.put("00", datos0);
        return datosTipoCompr;
    }

    public static LinkedHashMap<String, String> obtSustCompr() {
        LinkedHashMap<String, String> sustentoCompr = new LinkedHashMap<>();
        sustentoCompr.put("01", "Crédito Tributario para declaración de IVA (servicios y bienes distintos de inventarios y activos fijos)");
        sustentoCompr.put("02", "Costo o Gasto para declaración de IR (servicios y bienes distintos de inventarios y activos fijos)");
        sustentoCompr.put("03", "Activo Fijo - Crédito Tributario para declaración de IVA");
        sustentoCompr.put("04", "Activo Fijo - Costo o Gasto para declaración de IR");
        sustentoCompr.put("05", "Liquidación Gastos de Viaje, hospedaje y alimentación Gastos IR (a nombre de empleados y no de la empresa)");
        sustentoCompr.put("06", "Inventario - Crédito Tributario para declaración de IVA");
        sustentoCompr.put("07", "Inventario - Costo o Gasto para declaración de IR");
        sustentoCompr.put("08", "Valor pagado para solicitar Reembolso de Gasto (intermediario)");
        sustentoCompr.put("09", "Reembolso por Siniestros");
        sustentoCompr.put("10", "Distribución de Dividendos, Beneficios o Utilidades");
        sustentoCompr.put("00", "Casos especiales cuyo sustento no aplica en las opciones anteriores");
        return sustentoCompr;
    }

    public static String BorrarCaract(String s_cadena, String s_caracteres) {
        String nuevaCad = "";
        Character caracter = null;
        boolean valido = true;

        /* Va recorriendo la cadena s_cadena y copia a la cadena que va a regresar,
         sólo los caracteres que no estén en la cadena s_caracteres */
        for (int i = 0; i < s_cadena.length(); i++) {
            valido = true;
            for (int j = 0; j < s_caracteres.length(); j++) {
                caracter = s_caracteres.charAt(j);

                if (s_cadena.charAt(i) == caracter) {
                    valido = false;
                    break;
                }
            }
            if (valido) {
                nuevaCad += s_cadena.charAt(i);
            }
        }

        return nuevaCad;
    }

    public static String borrarEsp(String texto) {
        java.util.StringTokenizer tokStr = new java.util.StringTokenizer(texto);
        StringBuilder buffStrBldr = new StringBuilder();
        while (tokStr.hasMoreTokens()) {
            buffStrBldr.append(" ").append(tokStr.nextToken());
        }
        return buffStrBldr.toString().trim();
    }

    public static String borrarSaltosLinea(String cadena) {
        // Para el reemplazo usamos un string vacío 
        return cadena.replaceAll("[\n]", "");
    }

    public static String obtHoraFLargo() {
        DateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        Calendar calendario = Calendar.getInstance();
        return formatoFecha.format(calendario.getTime());
    }

    public static String obtHoraFLargo(Date dia) {
        DateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
        return formatoFecha.format(dia);
    }

    public static String obtenerFechaEnFormatoAnioMesDia(Date d) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
            formato.setLenient(false);
            String f = formato.format(d);
            return f;
        } catch (Exception ex) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", ex);
            return null;
        }
    }

    public static String obtenerHHmmss(Date d) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
            formato.setLenient(false);
            String f = formato.format(d);
            return f.replace(':', '_');
        } catch (Exception ex) {
            Logger.getLogger(Validaciones.class.getName()).log(Level.WARNING, "No se pudo formatear la fecha", ex);
            return null;
        }
    }

    public static Date obtenerFechaEmisionComprobante(ComprobanteElectronico _comprobante) {
        Date fecha = null;
        String fecha_emision = null;
        String tipo_comprobante = _comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc();
        if (tipo_comprobante.equals("01")) {
            fecha_emision = _comprobante.ConstruirFactura().getInformacionFactura().getFechaEmision();
        } else if (tipo_comprobante.equals("04")) {
            fecha_emision = _comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getFechaEmision();
        } else if (tipo_comprobante.equals("07")) {
            fecha_emision = _comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getFechaEmision();
        } else {
            return null;
        }
        fecha = Utilidades.cadAFecha(fecha_emision);
        return fecha;
    }

    public static String obtenerRazonSocialReceptor(com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico comprobante) {

        String razon_social_cliente = "";

        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
            razon_social_cliente = comprobante.ConstruirFactura().getInformacionFactura().getRazonSocialComprador();
        }
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("04")) {
            razon_social_cliente = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getRazonSocialComprador();
        }
        
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("07")) {
            razon_social_cliente = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getRazonSocialSujetoRetenido();
        }

        return razon_social_cliente;
    }

    /**
     * Método que obtiene el RUC-cédula del receptor usando el objeto
     * ComprobanteElectronico
     *
     * @param comprobante
     * @return
     */
    public static String obtenerRucReceptor(com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico comprobante) {

        String receptor = "";

        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {

            receptor = comprobante.ConstruirFactura().getInformacionFactura().getIdentificacionComprador();
        }
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("04")) {

            receptor = comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getIdentificacionComprador();
        }
        
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("07")) {
            receptor = comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getIdentificacionSujetoRetenido();
        }

        if (receptor != null) {
            return receptor;
        } else {
            return null;
        }
    }

    public static String obtenerTipoDocumento(com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico comprobante) {

        String tipo_documento = "";

        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
            tipo_documento = "FACTURA";
        }
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("04")) {
            tipo_documento = "NOTA DE CRÉDITO";
        }
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("05")) {
            tipo_documento = "NOTA DE DÉBITO";
        }
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("06")) {
            tipo_documento = "GUÍA DE REMISIÓN";
        }
        if (comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("07")) {
            tipo_documento = "COMPROBANTE DE RETENCIÓN";
        }

        return tipo_documento;
    }

    public static String obtenerCodigoTarifaImpuesto(Integer _id) {
        switch (_id) {
            case 1:
                return "2";
            case 3:
                return "3";
            case 4:
                return "5";
            default:
                return null;
        }
    }

    public static int obtenerLongitudDespuesPunto(String numero) {
        int punto = 0;
        for (int i = 0; i < numero.length(); i++) {
            if (numero.charAt(i) == '.') {
                punto = i;
                break;
            }
        }
        String n = "";
        if (punto > 0) {
            n = numero.substring(punto + 1);
        }
        return n.length();

    }

    public static String getMacAddress() {
        StringBuilder sb = new StringBuilder();
        try {

            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface netInter = NetworkInterface.getByInetAddress(localHost);
            byte[] macAddressBytes = netInter.getHardwareAddress();

            for (int i = 0; i <= 5; i++) {

                sb.append((Integer.toHexString(macAddressBytes[i] & 0xff)).toUpperCase());
                if (i < 5) {
                    sb.append("-");
                }

            }
        } catch (SocketException | UnknownHostException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sb.toString();
    }

    public static boolean isReachable(String targetUrl) throws IOException {
        HttpsURLConnection httpUrlConnection = (HttpsURLConnection) new URL(
                targetUrl).openConnection();
        httpUrlConnection.setRequestMethod("HEAD");

        try {
            int responseCode = httpUrlConnection.getResponseCode();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (UnknownHostException noInternetConnection) {
            return false;
        }
    }

    /**
     * Pings a HTTP URL. This effectively sends a HEAD request and returns
     * <code>true</code> if the response code is in the 200-399 range.
     *
     * @param url The HTTP URL to be pinged.
     * @param timeout The timeout in millis for both the connection timeout and
     * the response read timeout. Note that the total timeout is effectively two
     * times the given timeout.
     * @return <code>true</code> if the given HTTP URL has returned response
     * code 200-399 on a HEAD request within the given timeout, otherwise
     * <code>false</code>.
     */
    public static boolean ping(String url, int timeout) {
        // url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }

    public static String obtenerMesEnLetras(int mes) {

        String mes_letras = "";
        switch (mes) {
            case 1: {
                mes_letras = "Enero";
                break;
            }
            case 2: {
                mes_letras = "Febrero";
                break;
            }
            case 3: {
                mes_letras = "Marzo";
                break;
            }
            case 4: {
                mes_letras = "Abril";
                break;
            }
            case 5: {
                mes_letras = "Mayo";
                break;
            }
            case 6: {
                mes_letras = "Junio";
                break;
            }
            case 7: {
                mes_letras = "Julio";
                break;
            }
            case 8: {
                mes_letras = "Agosto";
                break;
            }
            case 9: {
                mes_letras = "Septiembre";
                break;
            }
            case 10: {
                mes_letras = "Octubre";
                break;
            }
            case 11: {
                mes_letras = "Noviembre";
                break;
            }
            case 12: {
                mes_letras = "Diciembre";
                break;
            }
            default: {
                mes_letras = "Error";
                break;
            }
        }
        return mes_letras.toUpperCase();

    }

    public static String obtenerMesAnioLetras(Date dia) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dia);

        int mes = calendar.get(Calendar.MONTH) + 1;
        String anio = String.valueOf(calendar.get(Calendar.YEAR));
        String mes_letras = "";
        switch (mes) {
            case 1: {
                mes_letras = "Enero";
                break;
            }
            case 2: {
                mes_letras = "Febrero";
                break;
            }
            case 3: {
                mes_letras = "Marzo";
                break;
            }
            case 4: {
                mes_letras = "Abril";
                break;
            }
            case 5: {
                mes_letras = "Mayo";
                break;
            }
            case 6: {
                mes_letras = "Junio";
                break;
            }
            case 7: {
                mes_letras = "Julio";
                break;
            }
            case 8: {
                mes_letras = "Agosto";
                break;
            }
            case 9: {
                mes_letras = "Septiembre";
                break;
            }
            case 10: {
                mes_letras = "Octubre";
                break;
            }
            case 11: {
                mes_letras = "Noviembre";
                break;
            }
            case 12: {
                mes_letras = "Diciembre";
                break;
            }
            default: {
                mes_letras = "Error";
                break;
            }
        }
        return mes_letras.concat("_").concat(anio).toUpperCase();

    }

    public static boolean comprobarDisponiblidadWebServicesSRI(String URL) {
        boolean comprobado = false;
        try {

            URL myurl = new URL(URL);
            HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
            InputStream ins = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(ins);
            BufferedReader in = new BufferedReader(isr);

            String inputLine = null;

            while ((inputLine = in.readLine()) != null) {
                comprobado = true;
            }

            in.close();
        } catch (IOException ex) {
            Logger.getLogger(Utilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
        return comprobado;
    }
}
