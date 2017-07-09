/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.pdfride;

/**
 * Clase que contiene información en cuanto a los tipos de comprobantes que
 * existen con su respectivo valor.
 *
 * @author Ricardo Delgado
 */
public class Valores {

    //Identificadores de tipos de comprobantes
    public static final String $FACTURA = "01";
    public static final String $NOTA_CREDITO = "04";
    public static final String $NOTA_DEBITO = "05";
    public static final String $GUIA_REMISION = "06";
    public static final String $COMPROBANTES_RETENCION = "07";
    public static final String $LIQUIDACION_COMPRA = "03";
    public static final String $BOLETOS = "08";
    public static final String $TIQUETES = "09";
    public static final String $COMPROBANTES_VENTA = "10";
    public static final String $PASAJES = "11";
    public static final String $DOC_INSTIT_FINAN = "12";
    public static final String $DOC_COMP_SEGUROS = "13";
    public static final String $COMP_EMIT_TELECOM = "14";
    public static final String $COMP_EMIT_EXTE = "15";
    public static final String $COMP_PAGO_CUOTAS = "19";
    public static final String $DOC_SERV_ADM = "20";
    public static final String $CARTA_APORTE_AEREO = "21";
    public static final String $COMP_VENTA_REEMBOLSO = "41";
    public static final String $DOC_RET_COMBUS = "42";
    public static final String $LIQ_HIDROCARBUROS = "43";
    public static final String $LIQ_RECLAMO_ASEGURADORAS = "45";
    public static final String $NOTA_CREDITO_REEMBOLSO = "47";
    public static final String $NOTA_DEBITO_REEMBOLSO = "48";
    //Impuesetos
    public static final String $RENTA = "1";
    public static final String $IVA = "2";
    public static final String $ISD = "6";

    public static final String getNombreComprobante(String codigo) {
        switch (codigo) {
            case $FACTURA:
                return "Factura";
            case $NOTA_CREDITO:
                return "Nota de Crédito";
            case $NOTA_DEBITO:
                return "Nota de Débito";
            case $GUIA_REMISION:
                return "Guía de Remisión";
            case $COMPROBANTES_RETENCION:
                return "Retención";
            case $LIQUIDACION_COMPRA:
                return "Liquidación de compra de bienes o prestación de servicios";
            case $BOLETOS:
                return "Boletos o entradas a espectáculos públicos";
            case $TIQUETES:
                return "Tiquetes o vales emitidos por máquinas registradoras";
            case $COMPROBANTES_VENTA:
                return "Comprobante de venta autorizados en el Art. 13";
            case $PASAJES:
                return "Pasajes expedidos por empresas de aviación";
            case $DOC_INSTIT_FINAN:
                return "Documentos emitidos por instituciones financieras";
            case $DOC_COMP_SEGUROS:
                return "Documentos emitidos por compañias de seguros";
            case $COMP_EMIT_TELECOM:
                return "Comprobantes emitidos por empresas de telecomunicaciones";
            case $COMP_EMIT_EXTE:
                return "Comprobante de venta emitido en el exterior";
            case $COMP_PAGO_CUOTAS:
                return "Comprobantes de pago de cuotas o aportes";
            case $DOC_SERV_ADM:
                return "Documentos por servicios administrativos emitidos por Inst. del estado";
            case $CARTA_APORTE_AEREO:
                return "Carta de porte aéreo";
            case $COMP_VENTA_REEMBOLSO:
                return "Comprobante de venta emitido por reembolso";
            case $DOC_RET_COMBUS:
                return "Documento agente de retención combustibles";
            case $LIQ_HIDROCARBUROS:
                return "Liquidación para explotación y exportación de hidrocarburos";
            case $LIQ_RECLAMO_ASEGURADORAS:
                return "Liquidación por reclamos de aseguradoras";
            case $NOTA_CREDITO_REEMBOLSO:
                return "Nota de crédito por reembolso emitida por intermediario";
            case $NOTA_DEBITO_REEMBOLSO:
                return "Nota de débito por reembolso emitida por intermediario";
            default:
                return "";
        }
    }

    public static final String getImpuestoRetener(String codigo) {
        switch (codigo) {
            case $RENTA:
                return "Renta";
            case $IVA:
                return "IVA";
            case $ISD:
                return "ISD";
            default:
                return "";
        }
    }
}
