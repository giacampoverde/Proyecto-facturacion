/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.comprobanteelectronico.esquema.implementacion;

import com.egastos.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import com.egastos.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import com.egastos.comprobanteelectronico.esquema.factura.Detalle;
import com.egastos.comprobanteelectronico.esquema.factura.FormatoFactura;
import com.egastos.comprobanteelectronico.esquema.factura.Pagos;


import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 *
 * @Ricardo Delgado
 */
public class ImplementacionFactura extends FormatoFactura {

    private Document documentoJDom;

    public ImplementacionFactura() {
        super();
    }

    /**
     * Método que construye un objeto org.jdom.Document de un archivo XML
     *
     * @param _direccionNombreArchivoXML String con la dirección y nombre del
     * archivo XML
     * @throws Exception Si no se puede crear el archivo
     */
    private void setDocumentoJDom(String _direccionNombreArchivoXML) throws Exception {
        SAXBuilder saxbuilder = new SAXBuilder();
        FileInputStream fIS = null;

        try {
            File file = new File(_direccionNombreArchivoXML);
            InputStream is = new FileInputStream(file);
            Reader reader = new InputStreamReader(is, "UTF-8");
            documentoJDom = saxbuilder.build(reader);
            try {
                is.close();
            } catch (IOException e) {
                System.out.println("Error IS");
            }
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println("Error Reader");
            }
        } catch (IOException | JDOMException e) {
            Logger.getLogger(ImplementacionFactura.class.getName()).log(Level.SEVERE, "Error al crear objeto Documento de archivo XML", e);
        } finally {
            if (fIS != null) {
                fIS.close();
            }
        }
    }

    /**
     * Método que obtiene los lista_hijos_info_tributaria del nodo
     * informacionFacturatura a partir del archivo XML
     *
     * @param _documentJDOM Objeto org.jdom.Document inicializado con el archivo
     * origen del XML
     */
    private void obtenerInformacionFactura(Document _documentJDOM) {
        String nombre = "";

        // Se obtiene el elemento raíz
        Element raiz = _documentJDOM.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos = raiz.getChildren();

        for (Element hijo : hijos) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("infoFactura")) {

                List<Element> lista_hijos_info_tributaria = hijo.getChildren();

                for (Element etiqueta : lista_hijos_info_tributaria) {
                    //Se obtienen los valores de las etiquetas de la Información Tributaria
                    switch (etiqueta.getName()) {
                        case "contribuyenteEspecial":
                            informacionFactura.setContribuyenteEspecial(etiqueta.getValue());
                            break;
                        case "dirEstablecimiento":
                            informacionFactura.setDirEstablecimiento(etiqueta.getValue());
                            break;
                        case "fechaEmision":
                            informacionFactura.setFechaEmision(etiqueta.getValue());
                            break;
                        case "guiaRemision":
                            informacionFactura.setGuiaRemision(etiqueta.getValue());
                            break;
                        case "identificacionComprador":
                            informacionFactura.setIdentificacionComprador(etiqueta.getValue());
                            break;
                        case "direccionComprador":
                            informacionFactura.setDireccionComprador(etiqueta.getValue());
                            break;
                        case "importeTotal":
                            informacionFactura.setImporteTotal(etiqueta.getValue());
                            break;
                        case "moneda":
                            informacionFactura.setMoneda(etiqueta.getValue());
                            break;
                        case "obligadoContabilidad":
                            informacionFactura.setObligadoContabilidad(etiqueta.getValue());
                            break;
                        case "comercioExterior":
                            informacionFactura.setComercioExterior(etiqueta.getValue());
                            break;
                        case "incoTermFactura":
                            informacionFactura.setIncoTermFactura(etiqueta.getValue());
                            break;
                        case "lugarIncoTerm":
                            informacionFactura.setLugarIncoTerm(etiqueta.getValue());
                            break;
                        case "paisOrigen":
                            informacionFactura.setPaisOrigen(etiqueta.getValue());
                            break;
                        case "puertoEmbarque":
                            informacionFactura.setPuertoEmbarque(etiqueta.getValue());
                            break;
                        case "puertoDestino":
                            informacionFactura.setPuertoDestino(etiqueta.getValue());
                            break;
                        case "paisDestino":
                            informacionFactura.setPaisDestino(etiqueta.getValue());
                            break;
                        case "paisAdquisicion":
                            informacionFactura.setPaisAdquisicion(etiqueta.getValue());
                            break;
                        case "propina":
                            informacionFactura.setPropina(etiqueta.getValue());
                            break;
                        case "razonSocialComprador":
                            informacionFactura.setRazonSocialComprador(etiqueta.getValue());
                            break;
                        case "incoTermTotalSinImpuestos":
                            informacionFactura.setIncoTermTotalSinImpuestos(etiqueta.getValue());
                            break;
                        case "fleteInternacional":
                            informacionFactura.setFleteInternacional(etiqueta.getValue());
                            break;
                        case "seguroInternacional":
                            informacionFactura.setSeguroInternacional(etiqueta.getValue());
                            break;
                        case "gastosAduaneros":
                            informacionFactura.setGastosAduaneros(etiqueta.getValue());
                            break;
                        case "gastosTransporteOtros":
                            informacionFactura.setGastosTransporteOtros(etiqueta.getValue());
                            break;
                        case "tipoIdentificacionComprador":
                            informacionFactura.setTipoIdentificacionComprador(etiqueta.getValue());
                            break;
                        case "totalDescuento":
                            informacionFactura.setTotalDescuento(etiqueta.getValue());
                            break;
                        case "totalSinImpuestos":
                            informacionFactura.setTotalSinImpuestos(etiqueta.getValue());
                            break;
                        case "codDocReembolso":
                            informacionFactura.setCodDocReembolso(etiqueta.getValue());
                            break;
                        case "totalComprobantesReembolso":
                            informacionFactura.setTotalComprobantesReembolso(etiqueta.getValue());
                            break;
                        case "totalImpuestoReembolso":
                            informacionFactura.setTotalImpuestoReembolso(etiqueta.getValue());
                            break;
                        case "totalBaseImponibleReembolso":
                            informacionFactura.setTotalBaseImponibleReembolso(etiqueta.getValue());
                            break;
                        case "totalConImpuestos":
                            List<Element> lista_impuestos_hijos = etiqueta.getChildren();
                            List<ImpuestoComprobanteElectronico> impuestos_comprobante = new ArrayList<>();
                            for (Element impuesto : lista_impuestos_hijos) {
                                List<Element> impuestos_nodos = impuesto.getChildren();
                                ImpuestoComprobanteElectronico imp = new ImpuestoComprobanteElectronico();
                                for (Element impuesto_detalle : impuestos_nodos) {
                                    switch (impuesto_detalle.getName()) {
                                        case "codigo":
                                            imp.setCodigo(impuesto_detalle.getValue());
                                            break;
                                        case "codigoPorcentaje":
                                            imp.setCodigoPorcentaje(impuesto_detalle.getValue());
                                            break;
                                        case "tarifa":
                                            imp.setTarifa(impuesto_detalle.getValue());
                                            break;
                                        case "baseImponible":
                                            imp.setBaseImponible(impuesto_detalle.getValue());
                                            break;
                                        case "valor":
                                            imp.setValor(impuesto_detalle.getValue());
                                            break;

                                    }
                                }
                                impuestos_comprobante.add(imp);
                            }
                            this.informacionFactura.setTotalConImpuesto(impuestos_comprobante);
                            break;
                        case "pagos":
                            List<Pagos> pagos = new ArrayList<>();
                            List<Element> lista_pagos_hijos = etiqueta.getChildren();
                            for (Element pago : lista_pagos_hijos) {
                                List<Element> pagos_nodos = pago.getChildren();
                                Pagos pago_detalle = new Pagos();
                                for (Element pagoE : pagos_nodos) {
                                    switch (pagoE.getName()) {
                                        case "formaPago":
                                            pago_detalle.setFormaPago(pagoE.getValue());
                                            break;
                                        case "total":
                                            pago_detalle.setTotal(pagoE.getValue());
                                            break;
                                        case "plazo":
                                            pago_detalle.setPlazo(pagoE.getValue());
                                            break;
                                        case "unidadTiempo":
                                            pago_detalle.setUnidadTiempo(pagoE.getValue());
                                            break;

                                    }
                                }
                                pagos.add(pago_detalle);
                            }
                            this.informacionFactura.setPagos(pagos);
                            break;
                    }
                }
            }
        }
    }

    /**
     * Método que obtiene la información de los detalles contenidos en el
     * archivo XML
     *
     * @param _documentoJDom Objeto org.jdom.Document inicializado con el
     * archivo origen del XML
     */
    private void obtenerDetallesFactura(Document _documentoJDom) {
        Detalle detalle_factura = null;
        String nombre = "";

        // Se obtiene el elemento raíz
        Element raiz = _documentoJDom.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos = raiz.getChildren();

        for (Element hijo : hijos) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("detalles")) {

                List<Element> lista_detalles_hijos = hijo.getChildren();

                for (Element detalle : lista_detalles_hijos) {
                    List<Element> det = detalle.getChildren();
                    detalle_factura = new Detalle();
                    for (Element dato : det) {
                        //Se obtienen los valores de las etiquetas del detalle
                        switch (dato.getName()) {
                            case "cantidad":
                                detalle_factura.setCantidad(dato.getValue());
                                break;
                            case "codigoAuxiliar":
                                detalle_factura.setCodigoAuxiliar(dato.getValue());
                                break;
                            case "codigoPrincipal":
                                detalle_factura.setCodigoPrincipal(dato.getValue());
                                break;
                            case "descripcion":
                                detalle_factura.setDescripcion(dato.getValue());
                                break;
                            case "descuento":
                                detalle_factura.setDescuento(dato.getValue());
                                break;
                            case "precioTotalSinImpuesto":
                                detalle_factura.setPrecioTotalSinImpuesto(dato.getValue());
                                break;
                            case "precioUnitario":
                                detalle_factura.setPrecioUnitario(dato.getValue());
                                break;
                            case "impuestos":
                                List<Element> lista_impuestos_hijos = dato.getChildren();
                                List<ImpuestoComprobanteElectronico> imp = new ArrayList<>();
                                for (Element impuesto : lista_impuestos_hijos) {
                                    List<Element> lista_impuestos_nodos = impuesto.getChildren();
                                    ImpuestoComprobanteElectronico impuesto_detalle = new ImpuestoComprobanteElectronico();

                                    for (Element elemento_impuesto : lista_impuestos_nodos) {
                                        switch (elemento_impuesto.getName()) {
                                            case "codigo":
                                                impuesto_detalle.setCodigo(elemento_impuesto.getValue());
                                                break;
                                            case "codigoPorcentaje":
                                                impuesto_detalle.setCodigoPorcentaje(elemento_impuesto.getValue());
                                                break;
                                            case "baseImponible":
                                                impuesto_detalle.setBaseImponible(elemento_impuesto.getValue());
                                                break;
                                            case "valor":
                                                impuesto_detalle.setValor(elemento_impuesto.getValue());
                                                break;
                                            case "tarifa":
                                                impuesto_detalle.setTarifa(elemento_impuesto.getValue());
                                                break;
                                        }
                                    }
                                    imp.add(impuesto_detalle);
                                }
                                detalle_factura.setImpuestos(imp);
                                break;
                            case "detallesAdicionales":
                                List<Element> lista_detalles_adicionales = dato.getChildren();
                                List<InformacionAdicional> informacion_adicional_detalles = new ArrayList<>();
                                for (Element elemento_detalle_adicional : lista_detalles_adicionales) {
                                    InformacionAdicional detAdicional = new InformacionAdicional();
                                    if (elemento_detalle_adicional.getName().equals("detAdicional")) {
                                        if (elemento_detalle_adicional.getAttribute("nombre") != null) {
                                            if (elemento_detalle_adicional.getAttribute("nombre").getValue() != null && !elemento_detalle_adicional.getAttribute("nombre").getValue().equals("")) {
                                                detAdicional.setNombre(elemento_detalle_adicional.getAttribute("nombre").getValue());
                                            }
                                        }
                                        if (elemento_detalle_adicional.getAttribute("valor") != null) {
                                            if (elemento_detalle_adicional.getAttribute("valor").getValue() != null && !elemento_detalle_adicional.getAttribute("valor").getValue().equals("")) {
                                                detAdicional.setValor(elemento_detalle_adicional.getAttribute("valor").getValue());
                                            }
                                        }
                                    }
                                    informacion_adicional_detalles.add(detAdicional);
                                }
                                detalle_factura.setDetallesAdicionales(informacion_adicional_detalles);
                                break;
                        }
                    }
                    detalles.add(detalle_factura);
                }
            }
        }
    }


    /**
     * Método que agrega el nodo de infoFactura a partir de los atributos
     * heredados del objeto ComprobanteElectronico
     *
     * @param _elementoInformacionFactura Elemento que contiene los nodos de la
     * información de la factura
     */
    private void agregarInformacionFactura(Element _elementoInformacionFactura) {
        Element fechaEmision = new Element("fechaEmision");
        fechaEmision.setText(this.informacionFactura.getFechaEmision());

        Element dirEstablecimiento = new Element("dirEstablecimiento");
        dirEstablecimiento.setText(this.informacionFactura.getDirEstablecimiento());

        Element contribuyenteEspecial = new Element("contribuyenteEspecial");
        contribuyenteEspecial.setText(this.informacionFactura.getContribuyenteEspecial());

        Element obligadoContabilidad = new Element("obligadoContabilidad");
        obligadoContabilidad.setText(this.informacionFactura.getObligadoContabilidad());

        Element tipoIdentificacionComprador = new Element("tipoIdentificacionComprador");
        tipoIdentificacionComprador.setText(this.informacionFactura.getTipoIdentificacionComprador());

        Element guiaRemision = new Element("guiaRemision");
        guiaRemision.setText(this.informacionFactura.getGuiaRemision());

        Element razonSocialComprador = new Element("razonSocialComprador");
        razonSocialComprador.setText(this.informacionFactura.getRazonSocialComprador());

        Element identificacionComprador = new Element("identificacionComprador");
        identificacionComprador.setText(this.informacionFactura.getIdentificacionComprador());

        Element totalSinImpuestos = new Element("totalSinImpuestos");
        totalSinImpuestos.setText(this.informacionFactura.getTotalSinImpuestos());

        Element totalDescuento = new Element("totalDescuento");
        totalDescuento.setText(this.informacionFactura.getTotalDescuento());

        Element propina = new Element("propina");
        propina.setText(this.informacionFactura.getPropina());

        Element importeTotal = new Element("importeTotal");
        importeTotal.setText(this.informacionFactura.getImporteTotal());

        Element moneda = new Element("moneda");
        moneda.setText(this.informacionFactura.getMoneda());

        _elementoInformacionFactura.addContent(fechaEmision);
        if (this.informacionFactura.getDirEstablecimiento() != null && !this.informacionFactura.getDirEstablecimiento().equals("")) {
            _elementoInformacionFactura.addContent(dirEstablecimiento);
        }
        if (this.informacionFactura.getContribuyenteEspecial() != null && !this.informacionFactura.getContribuyenteEspecial().equals("")) {
            _elementoInformacionFactura.addContent(contribuyenteEspecial);
        }
        if (this.informacionFactura.getObligadoContabilidad() != null && !this.informacionFactura.getObligadoContabilidad().equals("")) {
            _elementoInformacionFactura.addContent(obligadoContabilidad);
        }
        if (this.informacionFactura.getComercioExterior() != null && !this.informacionFactura.getLugarIncoTerm().equals("")) {
            Element comercioExterior = new Element("comercioExterior");
            comercioExterior.setText(this.informacionFactura.getComercioExterior());
            _elementoInformacionFactura.addContent(comercioExterior);
        }
        if (this.informacionFactura.getIncoTermFactura() != null && !this.informacionFactura.getLugarIncoTerm().equals("")) {
            Element incoTermFactura = new Element("incoTermFactura");
            incoTermFactura.setText(this.informacionFactura.getIncoTermFactura());
            _elementoInformacionFactura.addContent(incoTermFactura);
        }
        if (this.informacionFactura.getLugarIncoTerm() != null && !this.informacionFactura.getLugarIncoTerm().equals("")) {
            Element lugarIncoTerm = new Element("lugarIncoTerm");
            lugarIncoTerm.setText(this.informacionFactura.getLugarIncoTerm());
            _elementoInformacionFactura.addContent(lugarIncoTerm);
        }
        if (this.informacionFactura.getPaisOrigen() != null && !this.informacionFactura.getPaisOrigen().equals("")) {
            Element paisOrigen = new Element("paisOrigen");
            paisOrigen.setText(this.informacionFactura.getPaisOrigen());
            _elementoInformacionFactura.addContent(paisOrigen);
        }
        if (this.informacionFactura.getPuertoEmbarque() != null && !this.informacionFactura.getPuertoEmbarque().equals("")) {
            Element puertoEmbarque = new Element("puertoEmbarque");
            puertoEmbarque.setText(this.informacionFactura.getPuertoEmbarque());
            _elementoInformacionFactura.addContent(puertoEmbarque);
        }
        if (this.informacionFactura.getPuertoDestino() != null && !this.informacionFactura.getPuertoDestino().equals("")) {
            Element puertoDestino = new Element("puertoDestino");
            puertoDestino.setText(this.informacionFactura.getPuertoDestino());
            _elementoInformacionFactura.addContent(puertoDestino);
        }
        if (this.informacionFactura.getPaisDestino() != null && !this.informacionFactura.getPaisDestino().equals("")) {
            Element paisDestino = new Element("paisDestino");
            paisDestino.setText(this.informacionFactura.getPaisDestino());
            _elementoInformacionFactura.addContent(paisDestino);
        }
        if (this.informacionFactura.getPaisAdquisicion() != null && !this.informacionFactura.getPaisAdquisicion().equals("")) {
            Element paisAdquisicion = new Element("paisAdquisicion");
            paisAdquisicion.setText(this.informacionFactura.getPaisAdquisicion());
            _elementoInformacionFactura.addContent(paisAdquisicion);
        }
        _elementoInformacionFactura.addContent(tipoIdentificacionComprador);
        if (this.informacionFactura.getGuiaRemision() != null && !this.informacionFactura.getGuiaRemision().equals("")) {
            _elementoInformacionFactura.addContent(guiaRemision);
        }
        _elementoInformacionFactura.addContent(razonSocialComprador);
        _elementoInformacionFactura.addContent(identificacionComprador);
        if (this.informacionFactura.getDireccionComprador() != null && !this.informacionFactura.getDireccionComprador().equals("")) {
            Element direccionComprador = new Element("direccionComprador");
            direccionComprador.setText(this.informacionFactura.getDireccionComprador());
            _elementoInformacionFactura.addContent(direccionComprador);
        }
        _elementoInformacionFactura.addContent(totalSinImpuestos);
        if (this.informacionFactura.getIncoTermTotalSinImpuestos() != null && !this.informacionFactura.getIncoTermTotalSinImpuestos().equals("")) {
            Element incoTermTotalSinImpuestos = new Element("incoTermTotalSinImpuestos");
            incoTermTotalSinImpuestos.setText(this.informacionFactura.getIncoTermTotalSinImpuestos());
            _elementoInformacionFactura.addContent(incoTermTotalSinImpuestos);
        }
        _elementoInformacionFactura.addContent(totalDescuento);

        if (this.informacionFactura.getCodDocReembolso() != null && !this.informacionFactura.getCodDocReembolso().equals("")) {
            Element codDocReembolso = new Element("codDocReembolso");
            codDocReembolso.setText(this.informacionFactura.getCodDocReembolso());
            _elementoInformacionFactura.addContent(codDocReembolso);
        }
        if (this.informacionFactura.getTotalComprobantesReembolso() != null && !this.informacionFactura.getTotalComprobantesReembolso().equals("")) {
            Element totalComprobantesReembolso = new Element("totalComprobantesReembolso");
            totalComprobantesReembolso.setText(this.informacionFactura.getTotalComprobantesReembolso());
            _elementoInformacionFactura.addContent(totalComprobantesReembolso);
        }
        if (this.informacionFactura.getTotalBaseImponibleReembolso() != null && !this.informacionFactura.getTotalBaseImponibleReembolso().equals("")) {
            Element totalBaseImponibleReembolso = new Element("totalBaseImponibleReembolso");
            totalBaseImponibleReembolso.setText(this.informacionFactura.getTotalBaseImponibleReembolso());
            _elementoInformacionFactura.addContent(totalBaseImponibleReembolso);
        }
        if (this.informacionFactura.getTotalImpuestoReembolso() != null && !this.informacionFactura.getTotalImpuestoReembolso().equals("")) {
            Element totalImpuestoReembolso = new Element("totalImpuestoReembolso");
            totalImpuestoReembolso.setText(this.informacionFactura.getTotalImpuestoReembolso());
            _elementoInformacionFactura.addContent(totalImpuestoReembolso);
        }
        if (this.informacionFactura.getTotalConImpuesto() != null && this.informacionFactura.getTotalConImpuesto().size() > 0) {
            Element totalConImpuestos = new Element("totalConImpuestos");
            agregarImpuestoComprobante(totalConImpuestos, this.informacionFactura.getTotalConImpuesto(), "totalImpuesto");
            _elementoInformacionFactura.addContent(totalConImpuestos);
        }

        _elementoInformacionFactura.addContent(propina);
        if (this.informacionFactura.getFleteInternacional() != null && !this.informacionFactura.getFleteInternacional().equals("")) {
            Element fleteInternacional = new Element("fleteInternacional");
            fleteInternacional.setText(this.informacionFactura.getFleteInternacional());
            _elementoInformacionFactura.addContent(fleteInternacional);
        }

        if (this.informacionFactura.getSeguroInternacional() != null && !this.informacionFactura.getSeguroInternacional().equals("")) {
            Element seguroInternacional = new Element("seguroInternacional");
            seguroInternacional.setText(this.informacionFactura.getSeguroInternacional());
            _elementoInformacionFactura.addContent(seguroInternacional);
        }
        if (this.informacionFactura.getGastosAduaneros() != null && !this.informacionFactura.getGastosAduaneros().equals("")) {
            Element gastosAduaneros = new Element("gastosAduaneros");
            gastosAduaneros.setText(this.informacionFactura.getGastosAduaneros());
            _elementoInformacionFactura.addContent(gastosAduaneros);
        }
        if (this.informacionFactura.getGastosTransporteOtros() != null && !this.informacionFactura.getGastosTransporteOtros().equals("")) {
            Element gastosTransporteOtros = new Element("gastosTransporteOtros");
            gastosTransporteOtros.setText(this.informacionFactura.getGastosTransporteOtros());
            _elementoInformacionFactura.addContent(gastosTransporteOtros);
        }

        _elementoInformacionFactura.addContent(importeTotal);
        if (this.informacionFactura.getMoneda() != null && !this.informacionFactura.getMoneda().equals("")) {
            _elementoInformacionFactura.addContent(moneda);
        }
        if (this.informacionFactura.getPagos() != null && !this.informacionFactura.getPagos().isEmpty()) {

            Element pagos = new Element("pagos");
            agregarPagos(pagos, this.informacionFactura.getPagos(), "pago");
            _elementoInformacionFactura.addContent(pagos);

        }
    }

    /**
     * Método que agrega el nodo de detalles a la factura, a partir de los
     * atributos heredados del objeto ComprobanteElectronico
     *
     * @param _elementoDetallesFactura Nodo de tipo Element que contiene todos
     * los nodos XML de los detalles de la factura
     */
    public void agregarDetalles(Element _elementoDetallesFactura) {
        if (this.detalles != null && !this.detalles.isEmpty()) {
            for (int i = 0; i < this.detalles.size(); i++) {
                Element det = new Element("detalle");

                Element codPrincipal = new Element("codigoPrincipal");
                codPrincipal.setText(this.detalles.get(i).getCodigoPrincipal());
                Element codigoAuxiliar = new Element("codigoAuxiliar");
                codigoAuxiliar.setText(this.detalles.get(i).getCodigoAuxiliar());
                Element descripcion = new Element("descripcion");
                descripcion.setText(this.detalles.get(i).getDescripcion());
                Element cantidad = new Element("cantidad");
                cantidad.setText(this.detalles.get(i).getCantidad());
                Element precioUnitario = new Element("precioUnitario");
                precioUnitario.setText(this.detalles.get(i).getPrecioUnitario());
                Element descuento = new Element("descuento");
                descuento.setText(this.detalles.get(i).getDescuento());
                Element precioTotalSinImpuesto = new Element("precioTotalSinImpuesto");
                precioTotalSinImpuesto.setText(this.detalles.get(i).getPrecioTotalSinImpuesto());
                /**
                 * Se verifica si el detalle de la factura tiene codigo auxiliar
                 * o no, para agregarlo al detalle.
                 */
                det.addContent(codPrincipal);
                if (this.detalles.get(i).getCodigoAuxiliar() != null && !this.detalles.get(i).getCodigoAuxiliar().equals("")) {
                    det.addContent(codigoAuxiliar);
                }

                det.addContent(descripcion);
                det.addContent(cantidad);
                det.addContent(precioUnitario);
                det.addContent(descuento);
                det.addContent(precioTotalSinImpuesto);
                /**
                 * Se verifica si el detalle de la factura tiene detalles
                 * adicionales o no, para agregarlo al detalle.
                 */
                if (this.detalles.get(i).getDetallesAdicionales() != null && !this.detalles.get(i).getDetallesAdicionales().isEmpty()) {
                    Element detallesAdicionales = new Element("detallesAdicionales");
                    agregarDetallesAdicionales(detallesAdicionales, this.detalles.get(i).getDetallesAdicionales());
                    det.addContent(detallesAdicionales);
                }
                //Nodo que contiene todos los impuestos
                Element impuestos = new Element("impuestos");
                //Se llama al método que lee los impuestos y los agrega al nodo impuuestos.
                agregarImpuestoDetalle(impuestos, this.detalles.get(i).getImpuestos(), "impuesto");
                //Se agregan los impuestos al detalle
                det.addContent(impuestos);
                //Se agregan el detalle al nodo superior detalles
                _elementoDetallesFactura.addContent(det);
            }

        }
    }

 
    /**
     * Método que agrega el nodo impuestos a la factura, a partir de los
     * atributos heredados del objeto ComprobanteElectronico
     *
     * @param _nodoPadre Elemento que contiene todos los impuestos
     * @param _impuestos Lista de impuestos que contiene el _nodoPadre
     * @param _nombreSubNodo Nombre de la etiqueta de los nodos hijos
     */
    private void agregarImpuestoComprobante(Element _nodoPadre, List<ImpuestoComprobanteElectronico> _impuestos, String _nombreSubNodo) {

        for (int i = 0; i < _impuestos.size(); i++) {
            Element impSubNodo = new Element(_nombreSubNodo);

            Element codigo = new Element("codigo");
            codigo.setText(_impuestos.get(i).getCodigo());
            Element codigoPorcentaje = new Element("codigoPorcentaje");
            codigoPorcentaje.setText(_impuestos.get(i).getCodigoPorcentaje());
            Element baseImponible = new Element("baseImponible");
            baseImponible.setText(_impuestos.get(i).getBaseImponible());
            Element tarifa = new Element("tarifa");
            tarifa.setText(_impuestos.get(i).getTarifa());
            Element valor = new Element("valor");
            valor.setText(_impuestos.get(i).getValor());

            impSubNodo.addContent(codigo);
            impSubNodo.addContent(codigoPorcentaje);
            impSubNodo.addContent(baseImponible);
            if (_impuestos.get(i).getTarifa() != null && !_impuestos.get(i).getTarifa().equals("")) {
                impSubNodo.addContent(tarifa);
            }

            impSubNodo.addContent(valor);

            _nodoPadre.addContent(impSubNodo);
        }
    }

    /**
     *
     * @param _nodoPadre
     * @param _pagos
     * @param _nombreSubNodo
     */
    private void agregarPagos(Element _nodoPadre, List<Pagos> _pagos, String _nombreSubNodo) {

        for (int i = 0; i < _pagos.size(); i++) {
            Element pago = new Element(_nombreSubNodo);

            Element formaPago = new Element("formaPago");
            formaPago.setText(_pagos.get(i).getFormaPago());
            Element total = new Element("total");
            total.setText(_pagos.get(i).getTotal());
            Element plazo = new Element("plazo");
            plazo.setText(_pagos.get(i).getPlazo());
            Element unidadTiempo = new Element("unidadTiempo");
            unidadTiempo.setText(_pagos.get(i).getUnidadTiempo());

            pago.addContent(formaPago);
            pago.addContent(total);
            pago.addContent(plazo);
            pago.addContent(unidadTiempo);

            _nodoPadre.addContent(pago);
        }
    }

    /**
     * Añade el nodo de los impuestos a un detalle de la factura
     *
     * @param _nodoPadre es el elemento al que se añadira la informacion de los
     * impuestos
     * @param _impuestos es el conjunto de los impuestos que sera agregado al
     * documento
     * @param _nombreSubNodo es el nombre del sub nodo que se agregara en el
     * padre
     */
    private void agregarImpuestoDetalle(Element _nodoPadre, List<ImpuestoComprobanteElectronico> _impuestos, String _nombreSubNodo) {

        for (int i = 0; i < _impuestos.size(); i++) {
            Element impSubNodo = new Element(_nombreSubNodo);

            Element codigo = new Element("codigo");
            codigo.setText(_impuestos.get(i).getCodigo());
            Element codigoPorcentaje = new Element("codigoPorcentaje");
            codigoPorcentaje.setText(_impuestos.get(i).getCodigoPorcentaje());
            Element tarifa = new Element("tarifa");
            tarifa.setText(_impuestos.get(i).getTarifa());
            Element baseImponible = new Element("baseImponible");
            baseImponible.setText(_impuestos.get(i).getBaseImponible());
            Element valor = new Element("valor");
            valor.setText(_impuestos.get(i).getValor());
            impSubNodo.addContent(codigo);
            impSubNodo.addContent(codigoPorcentaje);
            if (_impuestos.get(i).getTarifa() != null && !_impuestos.get(i).getTarifa().equals("")) {
                impSubNodo.addContent(tarifa);
            }
            impSubNodo.addContent(baseImponible);
            impSubNodo.addContent(valor);

            _nodoPadre.addContent(impSubNodo);
        }
    }

    @Override
    public ImplementacionFactura ConstruirFactura() {
        return this;
    }

    @Override
    public boolean ObjetoComprobanteAXML(String _direccionArchivoXMLOrigen) {
        boolean estado = false;
        try {
            setDocumentoJDom(_direccionArchivoXMLOrigen);
              obtenerInformacionComprobanteHijo(documentoJDom);
            obtenerInformacionTributaria(this.documentoJDom);
            obtenerInformacionFactura(this.documentoJDom);
            obtenerDetallesFactura(this.documentoJDom);
            obtenerInformacionAdicional(this.documentoJDom);
            estado = true;
        } catch (Exception ex) {
            Logger.getLogger(ImplementacionFactura.class.getName()).log(Level.SEVERE, "Leer Archivo", ex);
        }
        return estado;
    }

    public byte[] ObjetoComprobanteAXML2(String _direccionArchivoXMLOrigen) {
        byte[] respuestad = null;
        try {
            setDocumentoJDom(_direccionArchivoXMLOrigen);
         respuestad = obtenerInformacionComprobanteHijo(documentoJDom);
            
           
            
        } catch (Exception ex) {
            Logger.getLogger(ImplementacionFactura.class.getName()).log(Level.SEVERE, "Leer Archivo", ex);
        }
        return respuestad;
    }

    @Override
    public File ObjetoComprobanteAXML(String _direccionArchivoDestino, String version) {
        try {
            Document docNuevo = new Document();
            Element factura = new Element("factura");
            factura.setAttribute("id", "comprobante");
            factura.setAttribute("version", version);
            Element infoTributaria = new Element("infoTributaria");
            Element infoFactura = new Element("infoFactura");
            Element detalles = new Element("detalles");
            docNuevo.addContent(factura);
            this.agregarInformacionTributaria(infoTributaria);
            this.agregarInformacionFactura(infoFactura);
            this.agregarDetalles(detalles);
            factura.addContent(infoTributaria);
            factura.addContent(infoFactura);
            factura.addContent(detalles);
            if (this.informacionAdicional != null && !this.informacionAdicional.isEmpty()) {
                Element infoAdicional = new Element("infoAdicional");
                this.agregarInformacionAdicional(infoAdicional);
                factura.addContent(infoAdicional);
            }

            Format format = Format.getPrettyFormat();

            XMLOutputter xmlop = new XMLOutputter(format);

            String docStr = xmlop.outputString(docNuevo);
            File arch = new File(_direccionArchivoDestino);
            OutputStream os = new FileOutputStream(arch);
            Closeable resource = os;
            try {
                try (Writer writer = new OutputStreamWriter(os, Charset.forName("UTF-8"))) {
                    resource = writer;
                    writer.write(docStr);
                    writer.flush();
                }
            } finally {
                resource.close();
            }
            return arch;
        } catch (IOException ex) {
            Logger.getLogger(ImplementacionFactura.class.getName()).log(Level.SEVERE, "Error al generar y guardar el comprobante factura.", ex);
            return null;
        }
    }

    @Override
    public ImplementacionNotaCredito ConstruirNotaCredito() {
        throw new UnsupportedOperationException("No se puede crear Nota de Crédito a partir de un Factura.");
    }

    @Override
    public ImplementacionRetencion ConstruirComprobanteRetencion() {
        throw new UnsupportedOperationException("No se puede crear Retención a partir de una Factura.");
    }

}
