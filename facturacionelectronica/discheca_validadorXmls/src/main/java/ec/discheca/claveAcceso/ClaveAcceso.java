/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.claveAcceso;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Delgado
 */
public class ClaveAcceso {

    /**
     * Metodo que calcula el ultimo diito de la clave de acceso para la factura
     *
     * @param cadena 48 digitos de la clave de acceso
     * @return Digito verificador de la clave de acceso
     */
    private String obtenerDigitoVerificadorModuloOnce(String array) {
        try {
            int a = 2;
            int rutSumado = 0;
            int mulDig = 1;
            char[] arrayC = invertir(array.toCharArray());
            for (int i = 0; i < array.length(); i++) {
                mulDig = Integer.parseInt(String.valueOf(arrayC[i])) * a;
                rutSumado += mulDig;
                if (a == 7) {
                    a = 1;
                }
                a++;
            }

            int resto = rutSumado % 11;
            String Digito = String.valueOf(11 - resto);

            if (Digito.equals("11")) {
                Digito = "0";
            }

            if (Digito.equals("10")) {
                Digito = "1";
            }

            return Digito;
        } catch (Exception ex) {
            Logger.getLogger(ClaveAcceso.class.getName()).log(Level.WARNING, null, ex);

            return null;
        }
    }

    public String quitaEspacios(String texto) {
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(texto);
        StringBuilder buff = new StringBuilder();
        while (tokens.hasMoreTokens()) {
            buff.append(" ").append(tokens.nextToken());
        }
        return buff.toString().trim();
    }

    private static char[] invertir(char[] array) {
        char[] invertir_int = new char[array.length];
        int maximo = array.length;
        for (int i = 0; i < array.length; i++) {
            char j = array[maximo - 1];
            invertir_int[maximo - 1] = array[i];
            maximo--;
        }
        return invertir_int;
    }

    public String obtenerClaveDeAcceso(ComprobanteElectronico _comprobante, String _codigoNumerico) {

        StringBuilder claveAcceso = new StringBuilder();

        String[] fecha = null;
        if (_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("01")) {
            fecha = _comprobante.ConstruirFactura().getInformacionFactura().getFechaEmision().split("/");
            if (!fecha[0].matches("")
                    && !fecha[1].matches("")
                    && !fecha[2].matches("")
                    && !_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().matches("")
                    && !_comprobante.getInformacionTributariaComprobanteElectronico().getRuc().matches("")
                    && !_comprobante.getInformacionTributariaComprobanteElectronico().getAmbiente().matches("")
                    && !_comprobante.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento().matches("")
                    && !_comprobante.getInformacionTributariaComprobanteElectronico().getPuntoEmision().matches("")
                    && !_comprobante.getInformacionTributariaComprobanteElectronico().getSecuencial().matches("")
                    && !_comprobante.getInformacionTributariaComprobanteElectronico().getTipoEmision().matches("")) {
                claveAcceso.append(quitaEspacios(fecha[0]));
                claveAcceso.append(quitaEspacios(fecha[1]));
                claveAcceso.append(quitaEspacios(fecha[2]));
                claveAcceso.append(quitaEspacios(_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc()));
                claveAcceso.append(quitaEspacios(_comprobante.getInformacionTributariaComprobanteElectronico().getRuc()));
                claveAcceso.append(quitaEspacios(_comprobante.getInformacionTributariaComprobanteElectronico().getAmbiente()));
                claveAcceso.append(quitaEspacios(_comprobante.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento()));
                claveAcceso.append(quitaEspacios(_comprobante.getInformacionTributariaComprobanteElectronico().getPuntoEmision()));
                claveAcceso.append(quitaEspacios(_comprobante.getInformacionTributariaComprobanteElectronico().getSecuencial()));
                claveAcceso.append(quitaEspacios(_codigoNumerico));
                claveAcceso.append(quitaEspacios(_comprobante.getInformacionTributariaComprobanteElectronico().getTipoEmision()));
                //Se obtiene el digito verificador para adjuntarlo a la clave de acceso
                String digitoVerificador = this.obtenerDigitoVerificadorModuloOnce(claveAcceso.toString());
                claveAcceso.append(digitoVerificador);
            } else {

            }

            return claveAcceso.toString();
        } //nota de credito
        else if (_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("04")) {
            fecha = _comprobante.ConstruirNotaCredito().getInformacionNotaCredito().getFechaEmision().split("/");
            claveAcceso.append(fecha[0]);
            claveAcceso.append(fecha[1]);
            claveAcceso.append(fecha[2]);
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getRuc());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getAmbiente());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getPuntoEmision());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getSecuencial());
            claveAcceso.append(_codigoNumerico);
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getTipoEmision());
            //Se obtiene el digito verificador para adjuntarlo a la clave de acceso
            String digitoVerificador = this.obtenerDigitoVerificadorModuloOnce(claveAcceso.toString());
            claveAcceso.append(digitoVerificador);

            return claveAcceso.toString();
        } //nota de debito
        else
        // comprobante de retencion
        if (_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc().equals("07")) {
            fecha = _comprobante.ConstruirComprobanteRetencion().getInformacionComprobanteRetencion().getFechaEmision().split("/");
            claveAcceso.append(fecha[0]);
            claveAcceso.append(fecha[1]);
            claveAcceso.append(fecha[2]);
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getCodDoc());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getRuc());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getAmbiente());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getCodigoEstablecimiento());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getPuntoEmision());
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getSecuencial());
            claveAcceso.append(_codigoNumerico);
            claveAcceso.append(_comprobante.getInformacionTributariaComprobanteElectronico().getTipoEmision());
            //Se obtiene el digito verificador para adjuntarlo a la clave de acceso
            String digitoVerificador = this.obtenerDigitoVerificadorModuloOnce(claveAcceso.toString());
            claveAcceso.append(digitoVerificador);

            return claveAcceso.toString();
        } else {
            return null;
        }

    } 
    
}
