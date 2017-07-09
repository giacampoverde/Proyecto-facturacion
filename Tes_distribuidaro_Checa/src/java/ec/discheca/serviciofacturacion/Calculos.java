/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.serviciofacturacion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.factura.Detalle;
import ec.discheca.configuracion.HibernateSessionHandler;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOTarifasImpuesto;
import ec.discheca.modelo.TarifasImpuesto;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Calculos {

    BigDecimal total_iva0 = BigDecimal.ZERO;
    BigDecimal total_iva12 = BigDecimal.ZERO;
    BigDecimal total_iva_no_objeto_impuesto = BigDecimal.ZERO;
    BigDecimal total_iva_exento = BigDecimal.ZERO;
    BigDecimal total_ice = BigDecimal.ZERO;
    BigDecimal total_irbpnr = BigDecimal.ZERO;
    BigDecimal importe_total = BigDecimal.ZERO;

    public String obtenerTotalSinImpuestos(Date _fechaPago, String _valorLocal, String _valorTasa) {
        BigDecimal valor_total_sin_impuestos = BigDecimal.ZERO;
        BigDecimal valor_sin_impuestos = new BigDecimal(_valorLocal);
        BigDecimal valor_tasa = new BigDecimal(_valorTasa);
        Calculos calculos = new Calculos();
        valor_total_sin_impuestos = valor_total_sin_impuestos.add(valor_sin_impuestos).add(valor_tasa);
        return valor_total_sin_impuestos.toString();

    }

    public String obtenerTotalConImpuestos(String _valor) throws Exception {
        BigDecimal valor_total_sin_impuestos = new BigDecimal(_valor);
        BigDecimal valor_doce_porciento = valor_total_sin_impuestos.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_EVEN);

        return valor_total_sin_impuestos.add(valor_doce_porciento).toString();

    }

    public List<ImpuestoComprobanteElectronico> obtenerImpuestosComprobante(String _idIVASeleccionado, String _idICESeleccionado, BigDecimal _cantidad, BigDecimal _precioUnitario, BigDecimal _descuento) throws Exception {
        List<ImpuestoComprobanteElectronico> impuestos_detalle_comprobante = new ArrayList();
        DAOTarifasImpuesto dao_impuestos = new DAOTarifasImpuesto();
        if (_idIVASeleccionado != null && !_idIVASeleccionado.equals("-1")) {
            TarifasImpuesto ti = dao_impuestos.obtenerImpuestoIVAPorTarifa(_idIVASeleccionado);
            if (ti != null) {
                ImpuestoComprobanteElectronico impuesto_detalle = new ImpuestoComprobanteElectronico();
                BigDecimal base_imponible = BigDecimal.ZERO;
                String valor = null;
                if (_descuento != null && !_descuento.equals(BigDecimal.ZERO)) {
                    base_imponible = _cantidad.multiply(_precioUnitario).subtract(_descuento);
                } else {
                    base_imponible = _cantidad.multiply(_precioUnitario);
                }
                base_imponible = base_imponible.setScale(2, RoundingMode.HALF_EVEN);
                if (!ti.getCodigoTarifaImpuesto().equals("6")) {
                    valor = obtenerValor(base_imponible.toString(), ti.getPorcentajeTarifaImpuesto());
                } else {
                    valor = base_imponible.toString();
                }
                impuesto_detalle.setTarifa(ti.getPorcentajeTarifaImpuesto());
                impuesto_detalle.setCodigo(ti.getTipoImpuesto().getIdTipoImpuesto().toString());
                impuesto_detalle.setBaseImponible(base_imponible.toString());
                impuesto_detalle.setValor(valor);
                impuesto_detalle.setCodigoPorcentaje(ti.getCodigoTarifaImpuesto());
                impuestos_detalle_comprobante.add(impuesto_detalle);
            }
        }
        if (_idICESeleccionado != null && !_idICESeleccionado.equals("-1")) {
            TarifasImpuesto ti = dao_impuestos.obtenerImpuestoIVAPorTarifa(_idICESeleccionado);
            if (ti != null) {
                ImpuestoComprobanteElectronico impuesto_detalle = new ImpuestoComprobanteElectronico();
                BigDecimal base_imponible = BigDecimal.ZERO;
                String valor = null;
                if (_descuento != null && !_descuento.equals(BigDecimal.ZERO)) {
                    base_imponible = _cantidad.multiply(_precioUnitario).subtract(_descuento);
                } else {
                    base_imponible = _cantidad.multiply(_precioUnitario);
                }
                base_imponible = base_imponible.setScale(2,RoundingMode.HALF_EVEN);

                valor = obtenerValor(base_imponible.toString(), ti.getPorcentajeTarifaImpuesto());
                impuesto_detalle.setTarifa(ti.getPorcentajeTarifaImpuesto());
                impuesto_detalle.setCodigo(ti.getTipoImpuesto().getIdTipoImpuesto().toString());
                impuesto_detalle.setBaseImponible(base_imponible.toString());
                impuesto_detalle.setValor(valor);
                impuesto_detalle.setCodigoPorcentaje(ti.getCodigoTarifaImpuesto());
                impuestos_detalle_comprobante.add(impuesto_detalle);
            }
        }
        return impuestos_detalle_comprobante;
    }

    public List<ImpuestoComprobanteElectronico> obtenerTotalConImpuestos(List<Detalle> detalles) throws Exception {
        List<ImpuestoComprobanteElectronico> total_impuestos = new ArrayList<ImpuestoComprobanteElectronico>();

        BigDecimal valor_iva0 = BigDecimal.ZERO;
        BigDecimal valor_iva12 = BigDecimal.ZERO;
        BigDecimal valor_iva_no_objeto_impuesto = BigDecimal.ZERO;
        BigDecimal valor_iva_exento = BigDecimal.ZERO;

        BigDecimal valor_ice = BigDecimal.ZERO;
        BigDecimal valor_irbpnr = BigDecimal.ZERO;
        String codigo_ice = null;
        String codigo_irbpnr = null;

        DAOTarifasImpuesto dao_impuestos = new DAOTarifasImpuesto();

        if (detalles != null && !detalles.isEmpty()) {
            for (Detalle d : detalles) {
                importe_total = importe_total.add(new BigDecimal(d.getPrecioTotalSinImpuesto()));
                List<ImpuestoComprobanteElectronico> impuestos_detalle = d.getImpuestos();
                for (ImpuestoComprobanteElectronico ia : impuestos_detalle) {
                    if (ia.getCodigo().equals("2")) {
                        if (ia.getCodigoPorcentaje().equals("0")) {
                            total_iva0 = total_iva0.add(new BigDecimal(ia.getBaseImponible()));
                            TarifasImpuesto ti = dao_impuestos.obtenerImpuestoPorTarifa(ia.getCodigoPorcentaje());
                                //se cambio de codigo 2 a tres por el iva
                        } else if (ia.getCodigoPorcentaje().equals("3")) {
                            total_iva12 = total_iva12.add(new BigDecimal(ia.getBaseImponible()));
                        } else if (ia.getCodigoPorcentaje().equals("6")) {
                            total_iva_no_objeto_impuesto = total_iva_no_objeto_impuesto.add(new BigDecimal(ia.getBaseImponible()));
                        } else if (ia.getCodigoPorcentaje().equals("7")) {
                            total_iva_exento = total_iva_exento.add(new BigDecimal(ia.getBaseImponible()));
                        }

                    } else if (ia.getCodigo().equals("3")) {
                        total_ice = total_ice.add(new BigDecimal(ia.getBaseImponible()));
                        codigo_ice = ia.getCodigoPorcentaje();
                    } else if (ia.getCodigo().equals("5")) {
                        codigo_irbpnr = ia.getCodigoPorcentaje();
                        total_irbpnr = total_irbpnr.add(new BigDecimal(ia.getBaseImponible()));
                    }
                }
            }
            ImpuestoComprobanteElectronico ice = new ImpuestoComprobanteElectronico();
            TarifasImpuesto ti = new TarifasImpuesto();
            if (!total_iva0.equals(BigDecimal.ZERO)) {

                total_iva0.setScale(2, RoundingMode.HALF_EVEN);
                ti = new TarifasImpuesto();
                ti = dao_impuestos.obtenerImpuestoIVAPorTarifa("0");
                ice.setCodigo("2");
                ice.setCodigoPorcentaje("0");
                total_iva0.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(total_iva0.toString());
                ice.setTarifa(ti.getPorcentajeTarifaImpuesto());
                BigDecimal porcentaje = new BigDecimal(ti.getPorcentajeTarifaImpuesto()).divide(new BigDecimal(100.00));
                valor_iva0 = total_iva0.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valor_iva0.toString());
                total_impuestos.add(ice);

            }
            if (!total_iva12.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                ti = new TarifasImpuesto();
                total_iva12.setScale(2, RoundingMode.HALF_EVEN);
                //cambio de dos a 3
                ti = dao_impuestos.obtenerImpuestoIVAPorTarifa("3");
                ice.setCodigo("2");
                //se cambiode dos a3 para iba 14
                ice.setCodigoPorcentaje("3");
                total_iva12.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(total_iva12.toString());
                ice.setTarifa(ti.getPorcentajeTarifaImpuesto());
                BigDecimal porcentaje = new BigDecimal(ti.getPorcentajeTarifaImpuesto()).divide(new BigDecimal(100.00));
                valor_iva12 = total_iva12.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valor_iva12.toString());
                total_impuestos.add(ice);

            }
            if (!total_iva_no_objeto_impuesto.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                total_iva_no_objeto_impuesto.setScale(2, RoundingMode.HALF_EVEN);
                ti = new TarifasImpuesto();
                ti = dao_impuestos.obtenerImpuestoIVAPorTarifa("6");
                ice.setCodigo("2");
                ice.setCodigoPorcentaje("6");
                total_iva_no_objeto_impuesto.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(total_iva_no_objeto_impuesto.toString());
                ice.setTarifa(ti.getPorcentajeTarifaImpuesto());
                BigDecimal porcentaje = new BigDecimal(ti.getPorcentajeTarifaImpuesto()).divide(new BigDecimal(100.00));
                valor_iva_no_objeto_impuesto = total_iva_no_objeto_impuesto.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valor_iva_no_objeto_impuesto.toString());
                total_impuestos.add(ice);

            }
            if (!total_iva_exento.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                total_iva_exento.setScale(2, RoundingMode.HALF_EVEN);
                ti = new TarifasImpuesto();
                ti = dao_impuestos.obtenerImpuestoIVAPorTarifa("7");
                ice.setCodigo("2");
                ice.setCodigoPorcentaje("7");
                total_iva_exento.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(total_iva_exento.toString());
                ice.setTarifa(ti.getPorcentajeTarifaImpuesto());
                BigDecimal porcentaje = new BigDecimal(ti.getPorcentajeTarifaImpuesto()).divide(new BigDecimal(100.00));
                valor_iva_exento = total_iva_exento.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valor_iva_exento.toString());
                total_impuestos.add(ice);

            }
            if (!total_irbpnr.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                total_irbpnr.setScale(2, RoundingMode.HALF_EVEN);
                ti = new TarifasImpuesto();
                ti = dao_impuestos.obtenerImpuestoPorTarifa(codigo_irbpnr);
                ice.setCodigo("5");
                ice.setCodigoPorcentaje(codigo_irbpnr);
                total_irbpnr.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(total_irbpnr.toString());
                ice.setTarifa(ti.getPorcentajeTarifaImpuesto());
                BigDecimal porcentaje = new BigDecimal(ti.getPorcentajeTarifaImpuesto()).divide(new BigDecimal(100.00));
                valor_irbpnr = total_irbpnr.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valor_irbpnr.toString());
                total_impuestos.add(ice);
            }
            if (!total_ice.equals(BigDecimal.ZERO)) {
                ice = new ImpuestoComprobanteElectronico();
                total_ice.setScale(2, RoundingMode.HALF_EVEN);
                ti = new TarifasImpuesto();
                ti = dao_impuestos.obtenerImpuestoPorTarifa(codigo_ice);
                ice.setCodigo("3");
                ice.setCodigoPorcentaje(codigo_ice);
                total_ice.setScale(2, RoundingMode.HALF_EVEN);
                ice.setBaseImponible(total_ice.toString());
                ice.setTarifa(ti.getPorcentajeTarifaImpuesto());
                BigDecimal porcentaje = new BigDecimal(ti.getPorcentajeTarifaImpuesto()).divide(new BigDecimal(100.00));
                valor_ice = total_ice.multiply(porcentaje).setScale(2, RoundingMode.HALF_EVEN);
                ice.setValor(valor_ice.toString());
                total_impuestos.add(ice);
            }
            importe_total = importe_total.add(valor_iva0).add(valor_iva12).add(valor_iva_exento).add(valor_iva_no_objeto_impuesto).add(valor_ice).add(valor_irbpnr);

        }
        return total_impuestos;

    }

    public BigDecimal obtenerTotalDescuento(List<Detalle> detalles) throws Exception {
        BigDecimal total_descuento = BigDecimal.ZERO;
        if (detalles != null && !detalles.isEmpty()) {

            for (Detalle d : detalles) {
                if (d.getDescuento() != null && !d.getDescuento().equals("")) {
                    BigDecimal bd_descuento = new BigDecimal(d.getDescuento());
                    total_descuento = total_descuento.add(bd_descuento);
                }
            }
        }
        return total_descuento;
    }

    public BigDecimal obtenerTotalSinImpuestos(List<Detalle> detalles) throws Exception {
        BigDecimal total_sin_impuestos = BigDecimal.ZERO;
        if (detalles != null && !detalles.isEmpty()) {

            for (Detalle d : detalles) {

                total_sin_impuestos = total_sin_impuestos.add(new BigDecimal(d.getPrecioTotalSinImpuesto()));

            }
            total_sin_impuestos.setScale(2, RoundingMode.HALF_EVEN);
        }
        return total_sin_impuestos;
    }

    public BigDecimal obtenerImporteTotal2(List<ImpuestoComprobanteElectronico> total_impuestos) {
        BigDecimal importe_total = BigDecimal.ZERO;
        if (total_impuestos != null && !total_impuestos.isEmpty()) {
            for (ImpuestoComprobanteElectronico ice : total_impuestos) {
                importe_total = importe_total.add(new BigDecimal(ice.getBaseImponible())).add(new BigDecimal(ice.getValor()));
            }
        }
        return importe_total;
    }

    public BigDecimal obtenerImporteTotal(List<Detalle> detalles) throws Exception {
        BigDecimal importe_total = BigDecimal.ZERO;
        List<ImpuestoComprobanteElectronico> total_impuestos = obtenerTotalConImpuestos(detalles);
        if (total_impuestos != null && !total_impuestos.isEmpty()) {

        }
        return importe_total;
    }

    public String obtenerValor12PorCiento(String _valor) {
        BigDecimal valor_total_sin_impuestos = new BigDecimal(_valor);
        BigDecimal valor_doce_porciento = valor_total_sin_impuestos.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_EVEN);
        return valor_doce_porciento.toString();
    }

    private String obtenerValor(String _valor, String _porcentaje) {
        BigDecimal valor_total_sin_impuestos = new BigDecimal(_valor);
        BigDecimal valor_doce_porciento = valor_total_sin_impuestos.multiply(new BigDecimal(_porcentaje).divide(new BigDecimal(100.00))).setScale(2, RoundingMode.HALF_EVEN);
        return valor_doce_porciento.toString();
    }

    public boolean verificarDiaPago(Date _fechaAutorizacion) {
        boolean tiene_recargo = false;
        Calendar calendario_actual = Calendar.getInstance();

        Calendar calendario_fecha_facturado = Calendar.getInstance();

        int dia_cancela_factura = calendario_actual.get(Calendar.DAY_OF_MONTH);

        calendario_fecha_facturado.setTime(_fechaAutorizacion);

        int dia_envia_factura = calendario_fecha_facturado.get(Calendar.DAY_OF_MONTH);

        if (calendario_fecha_facturado.get(Calendar.MONTH) == calendario_actual.get(Calendar.MONTH)) {

            int dia_max_pago = dia_envia_factura + 10;
            if (dia_cancela_factura > dia_max_pago) {
                tiene_recargo = true;
            }
        } else {
            tiene_recargo = true;
        }
        return tiene_recargo;
    }

    /**
     * Método que redondea a dos decimales
     *
     * @param numero
     * @return numero con 2 decimales
     */
    public double redondear(double numero) {
        return Math.round(numero * Math.pow(10, 2)) / Math.pow(10, 2);
    }

    public BigDecimal getTotal_iva0() {
        return total_iva0;
    }

    public void setTotal_iva0(BigDecimal total_iva0) {
        this.total_iva0 = total_iva0;
    }

    public BigDecimal getTotal_iva12() {
        return total_iva12;
    }

    public void setTotal_iva12(BigDecimal total_iva12) {
        this.total_iva12 = total_iva12;
    }

    public BigDecimal getTotal_iva_no_objeto_impuesto() {
        return total_iva_no_objeto_impuesto;
    }

    public void setTotal_iva_no_objeto_impuesto(BigDecimal total_iva_no_objeto_impuesto) {
        this.total_iva_no_objeto_impuesto = total_iva_no_objeto_impuesto;
    }

    public BigDecimal getTotal_iva_exento() {
        return total_iva_exento;
    }

    public void setTotal_iva_exento(BigDecimal total_iva_exento) {
        this.total_iva_exento = total_iva_exento;
    }

    public BigDecimal getTotal_ice() {
        return total_ice;
    }

    public void setTotal_ice(BigDecimal total_ice) {
        this.total_ice = total_ice;
    }

    public BigDecimal getTotal_irbpnr() {
        return total_irbpnr;
    }

    public void setTotal_irbpnr(BigDecimal total_irbpnr) {
        this.total_irbpnr = total_irbpnr;
    }

    public BigDecimal getImporte_total() {
        return importe_total;
    }

    public void setImporte_total(BigDecimal importe_total) {
        this.importe_total = importe_total;
    }

    public static void main(String args[]) throws Exception {
        try {
            Calendar c = Calendar.getInstance();

            System.out.println(c.getTime());
            Valores.init();
            HibernateSessionHandler hsh = new HibernateSessionHandler();
            Calculos cc = new Calculos();

            hsh.close();
        } catch (Exception ex) {
            Logger.getLogger(Calculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
