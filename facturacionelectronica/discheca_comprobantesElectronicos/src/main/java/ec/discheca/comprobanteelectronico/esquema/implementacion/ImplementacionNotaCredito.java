/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.comprobanteelectronico.esquema.implementacion;

import ec.discheca.comprobanteelectronico.esquema.comprobantebase.ImpuestoComprobanteElectronico;
import ec.discheca.comprobanteelectronico.esquema.comprobantebase.InformacionAdicional;
import ec.discheca.comprobanteelectronico.esquema.factura.Detalle;
import ec.discheca.comprobanteelectronico.esquema.notacredito.NotaCreditoEsquema;
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
public class ImplementacionNotaCredito extends NotaCreditoEsquema {

    private Document documentoJDom;

    public ImplementacionNotaCredito() {
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

        } catch (JDOMException | IOException e) {
            Logger.getLogger(ImplementacionNotaCredito.class.getName()).log(Level.SEVERE, "Error al crear objeto Documento de archivo XML", e);
        } finally {
            if (fIS != null) {
                fIS.close();
            }
        }
    }

    /**
     * Método que construye un objeto org.jdom.Document de un archivo XML
     *
     * @param _direccionNombreArchivoXML String con la dirección y nombre del
     * archivo XML
     * @throws Exception Si no se puede crear el archivo
     */
    private void setDocumentoJDomFloral(String _direccionNombreArchivoXML) throws Exception {
        try{
        SAXBuilder builder = new SAXBuilder();
        documentoJDom = builder.build(new FileInputStream(_direccionNombreArchivoXML));
        }catch(JDOMException | IOException ex){
            System.out.println("archivo incorrecto");
            Logger.getLogger(ImplementacionFactura.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que obtiene los datos del nodo infoNotaCredito a partir de un
     * objeto Document
     *
     * @param _documentoJDom
     */
    private void obtenerInformacionNotaCredito(Document _documentJDOM) {
        String nombre = "";

        // Se obtiene el elemento raíz
        Element raiz = _documentJDOM.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos = raiz.getChildren();

        for (Element hijo : hijos) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("infoNotaCredito")) {

                List<Element> datos = hijo.getChildren();

                for (Element dato : datos) {
                    //Se obtienen los valores de las etiquetas de infoNotaCredito
                    switch (dato.getName()) {
                        case "fechaEmision":
                            informacionNotaCredito.setFechaEmision(dato.getValue());
                            break;
                        case "dirEstablecimiento":
                            informacionNotaCredito.setDirEstablecimiento(dato.getValue());
                            break;
                        case "tipoIdentificacionComprador":
                            informacionNotaCredito.setTipoIdentificacionComprador(dato.getValue());
                            break;
                        case "razonSocialComprador":
                            informacionNotaCredito.setRazonSocialComprador(dato.getValue());
                            break;
                        case "identificacionComprador":
                            informacionNotaCredito.setIdentificacionComprador(dato.getValue());
                            break;
                        case "contribuyenteEspecial":
                            informacionNotaCredito.setContribuyenteEspecial(dato.getValue());
                            break;
                        case "obligadoContabilidad":
                            informacionNotaCredito.setObligadoContabilidad(dato.getValue());
                            break;
                        case "rise":
                            informacionNotaCredito.setRise(dato.getValue());
                            break;
                        case "codDocModificado":
                            informacionNotaCredito.setCodDocModificado(dato.getValue());
                            break;
                        case "numDocModificado":
                            informacionNotaCredito.setNumDocModificado(dato.getValue());
                            break;
                        case "fechaEmisionDocSustento":
                            informacionNotaCredito.setFechaEmisionDocSustento(dato.getValue());
                            break;
                        case "totalSinImpuestos":
                            informacionNotaCredito.setTotalSinImpuestos(dato.getValue());
                            break;
                        case "valorModificacion":
                            informacionNotaCredito.setValorModificacion(dato.getValue());
                            break;
                        case "motivo":
                            informacionNotaCredito.setMotivo(dato.getValue());
                            break;
                        case "moneda":
                            informacionNotaCredito.setMoneda(dato.getValue());
                            break;
                    }

                    if (dato.getName().equals("totalConImpuestos")) {
                        List<Element> impuestos_hijos = dato.getChildren();
                        List<ImpuestoComprobanteElectronico> impuestos = new ArrayList<>();
                        for (Element impuesto_detalle : impuestos_hijos) {
                            ImpuestoComprobanteElectronico imp = new ImpuestoComprobanteElectronico();
                            List<Element> lista_impuestos_nodos = impuesto_detalle.getChildren();
                            for (Element impuesto_detalle_nodo : lista_impuestos_nodos) {
                                switch (impuesto_detalle_nodo.getName()) {
                                    case "codigo":
                                        imp.setCodigo(impuesto_detalle_nodo.getValue());
                                        break;
                                    case "codigoPorcentaje":
                                        imp.setCodigoPorcentaje(impuesto_detalle_nodo.getValue());
                                        break;
                                    case "baseImponible":
                                        imp.setBaseImponible(impuesto_detalle_nodo.getValue());
                                        break;
                                    case "valor":
                                        imp.setValor(impuesto_detalle_nodo.getValue());
                                        break;
                                    case "tarifa":
                                        imp.setTarifa(impuesto_detalle_nodo.getValue());
                                        break;
                                }
                            }
                            impuestos.add(imp);
                        }
                        informacionNotaCredito.setTotalConImpuesto(impuestos);
                    }
                }
            }
        }
    }

    /**
     * Método que obtiene los datos de los detalles a partir de un objeto
     * Document
     *
     * @param _documentoJDom
     */
    private void obtenerDetalles(Document _documentJDOM) {
        Detalle detNCred = null;
        String nombre = "";

        // Se obtiene el elemento raíz
        Element raiz = _documentJDOM.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos = raiz.getChildren();

        for (Element hijo : hijos) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("detalles")) {

                List<Element> datos = hijo.getChildren();

                for (Element dat : datos) {
                    List<Element> lista_detalles = dat.getChildren();
                    detNCred = new Detalle();
                    for (Element dato : lista_detalles) {
                        //Se obtienen los valores de las etiquetas de los detalles
                        switch (dato.getName()) {
                            case "cantidad":
                                detNCred.setCantidad(dato.getValue());
                                break;
                            case "codigoAdicional":
                                detNCred.setCodigoAuxiliar(dato.getValue());
                                break;
                            case "codigoInterno":
                                detNCred.setCodigoPrincipal(dato.getValue());
                                break;
                            case "descripcion":
                                detNCred.setDescripcion(dato.getValue());
                                break;
                            case "descuento":
                                detNCred.setDescuento(dato.getValue());
                                break;
                            case "precioTotalSinImpuesto":
                                detNCred.setPrecioTotalSinImpuesto(dato.getValue());
                                break;
                            case "precioUnitario":
                                detNCred.setPrecioUnitario(dato.getValue());
                                break;
                            case "impuestos":

                                List<Element> impuestos_hijos = dato.getChildren();
                                List<ImpuestoComprobanteElectronico> impuestos = new ArrayList<>();
                                for (Element impuesto_detalle : impuestos_hijos) {
                                    ImpuestoComprobanteElectronico impuesto = new ImpuestoComprobanteElectronico();
                                    List<Element> lista_impuestos_detalles = impuesto_detalle.getChildren();
                                    for (Element impuesto_detalle_nodo : lista_impuestos_detalles) {
                                        switch (impuesto_detalle_nodo.getName()) {
                                            case "codigo":
                                                impuesto.setCodigo(impuesto_detalle_nodo.getValue());
                                                break;
                                            case "codigoPorcentaje":
                                                impuesto.setCodigoPorcentaje(impuesto_detalle_nodo.getValue());
                                                break;
                                            case "baseImponible":
                                                impuesto.setBaseImponible(impuesto_detalle_nodo.getValue());
                                                break;
                                            case "valor":
                                                impuesto.setValor(impuesto_detalle_nodo.getValue());
                                                break;
                                            case "tarifa":
                                                impuesto.setTarifa(impuesto_detalle_nodo.getValue());
                                                break;
                                        }
                                    }
                                    impuestos.add(impuesto);
                                }
                                detNCred.setImpuestos(impuestos);

                                break;
                            case "detallesAdicionales":

                                List<Element> lista_detalles_hijos = dato.getChildren();
                                List<InformacionAdicional> informacion_adicional = new ArrayList<>();
                                for (Element informacion_adicional_nodo : lista_detalles_hijos) {
                                    InformacionAdicional detAdicional = new InformacionAdicional();
                                    if (informacion_adicional_nodo.getName().equals("detAdicional")) {
                                        detAdicional.setNombre(informacion_adicional_nodo.getAttribute("nombre").getValue());
                                        detAdicional.setValor(informacion_adicional_nodo.getAttribute("valor").getValue());
                                    }
                                    informacion_adicional.add(detAdicional);
                                }
                                detNCred.setDetallesAdicionales(informacion_adicional);

                                break;
                        }

                    }
                    detalles.add(detNCred);
                }
            }
        }

    }

    /**
     * Método que construye el nodo de infoNotaCredito de la Nota de Crédito
     *
     * @param _documentoJDom
     */
    
    
   
    private void agregarInfoNotaCredito(Element _elementoInformacionNotaCredito) {

        Element fechaEmision = new Element("fechaEmision");
        fechaEmision.setText(this.informacionNotaCredito.getFechaEmision());

        Element dirEstablecimiento = new Element("dirEstablecimiento");
        dirEstablecimiento.setText(this.informacionNotaCredito.getDirEstablecimiento());

        Element tipoIdentificacionComprador = new Element("tipoIdentificacionComprador");
        tipoIdentificacionComprador.setText(this.informacionNotaCredito.getTipoIdentificacionComprador());

        Element razonSocialComprador = new Element("razonSocialComprador");
        razonSocialComprador.setText(this.informacionNotaCredito.getRazonSocialComprador());

        Element identificacionComprador = new Element("identificacionComprador");
        identificacionComprador.setText(this.informacionNotaCredito.getIdentificacionComprador());

        Element contribuyenteEspecial = new Element("contribuyenteEspecial");
        contribuyenteEspecial.setText(this.informacionNotaCredito.getContribuyenteEspecial());

        Element obligadoContabilidad = new Element("obligadoContabilidad");
        obligadoContabilidad.setText(this.informacionNotaCredito.getObligadoContabilidad());

        Element totalSinImpuestos = new Element("totalSinImpuestos");
        totalSinImpuestos.setText(this.informacionNotaCredito.getTotalSinImpuestos());

        Element valorModificacion = new Element("valorModificacion");
        valorModificacion.setText(this.informacionNotaCredito.getValorModificacion());

        Element moneda = new Element("moneda");
        moneda.setText(this.informacionNotaCredito.getMoneda());

        _elementoInformacionNotaCredito.addContent(fechaEmision);
        if (this.informacionNotaCredito.getDirEstablecimiento() != null && !this.informacionNotaCredito.getDirEstablecimiento().equals("")) {
            _elementoInformacionNotaCredito.addContent(dirEstablecimiento);
        }
        _elementoInformacionNotaCredito.addContent(tipoIdentificacionComprador);
        _elementoInformacionNotaCredito.addContent(razonSocialComprador);
        _elementoInformacionNotaCredito.addContent(identificacionComprador);
        if (this.informacionNotaCredito.getContribuyenteEspecial() != null && !this.informacionNotaCredito.getContribuyenteEspecial().equals("")) {
            _elementoInformacionNotaCredito.addContent(contribuyenteEspecial);
        }
        if (this.informacionNotaCredito.getObligadoContabilidad() != null && !this.informacionNotaCredito.getObligadoContabilidad().equals("")) {
            _elementoInformacionNotaCredito.addContent(obligadoContabilidad);
        }
        if (this.informacionNotaCredito.getRise() != null && !this.informacionNotaCredito.getRise().equals("")) {
            Element rise = new Element("rise");
            rise.setText(this.informacionNotaCredito.getRise());
            _elementoInformacionNotaCredito.addContent(rise);
        }
        Element ecodDocModificado = null;
        Element enumDocModificado = null;
        Element efechaEmisionDocSustento = null;
        Element motivo = new Element("motivo");

        if (this.informacionNotaCredito.getCodDocModificado() != null && !this.informacionNotaCredito.getCodDocModificado().equals("")) {
            ecodDocModificado = new Element("codDocModificado");
            ecodDocModificado.setText(this.informacionNotaCredito.getCodDocModificado());
            _elementoInformacionNotaCredito.addContent(ecodDocModificado);

        }

        if (this.informacionNotaCredito.getNumDocModificado() != null && !this.informacionNotaCredito.getNumDocModificado().equals("")) {
            enumDocModificado = new Element("numDocModificado");
            enumDocModificado.setText(this.informacionNotaCredito.getNumDocModificado());
            _elementoInformacionNotaCredito.addContent(enumDocModificado);
        }

        if (this.informacionNotaCredito.getFechaEmisionDocSustento() != null && !this.informacionNotaCredito.getFechaEmisionDocSustento().equals("")) {
            efechaEmisionDocSustento = new Element("fechaEmisionDocSustento");
            efechaEmisionDocSustento.setText(this.informacionNotaCredito.getFechaEmisionDocSustento());
            _elementoInformacionNotaCredito.addContent(efechaEmisionDocSustento);
        }
        _elementoInformacionNotaCredito.addContent(totalSinImpuestos);
        _elementoInformacionNotaCredito.addContent(valorModificacion);
        if (this.informacionNotaCredito.getMoneda() != null && !this.informacionNotaCredito.getMoneda().equals("")) {
            _elementoInformacionNotaCredito.addContent(moneda);
        }
        if (this.informacionNotaCredito.getTotalConImpuesto() != null && this.informacionNotaCredito.getTotalConImpuesto().size() > 0) {
            Element totalCImp = new Element("totalConImpuestos");
            agregarImpuestos(totalCImp, this.informacionNotaCredito.getTotalConImpuesto(), "totalImpuesto");
            _elementoInformacionNotaCredito.addContent(totalCImp);
        }

        motivo.setText(this.informacionNotaCredito.getMotivo());
        _elementoInformacionNotaCredito.addContent(motivo);
    }

    /**
     * Método que construye el nodo de detalles de la Nota de Crédito
     *
     * @param _detallesNotaCreditoElement Elemento padre que contiene los
     * detalles
     */
    public void agregarDetalles(Element _detallesNotaCreditoElement) {
        if (this.detalles != null && this.detalles.size() > 0) {
            for (int i = 0; i < this.detalles.size(); i++) {
                Element detalle = new Element("detalle");

                Element codigoInterno = new Element("codigoInterno");
                codigoInterno.setText(this.detalles.get(i).getCodigoPrincipal());

                Element codigoAdicional = new Element("codigoAdicional");
                codigoAdicional.setText(this.detalles.get(i).getCodigoAuxiliar());

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
                //añade al nodo todos los componentes del XML del nodo detalle
                detalle.addContent(codigoInterno);
                if (this.detalles.get(i).getCodigoAuxiliar() != null && !this.detalles.get(i).getCodigoAuxiliar().equals("")) {
                    detalle.addContent(codigoAdicional);
                }
                detalle.addContent(descripcion);
                detalle.addContent(cantidad);
                detalle.addContent(precioUnitario);
                if (this.detalles.get(i).getDescuento() != null && !this.detalles.get(i).getDescuento().equals("")) {
                    detalle.addContent(descuento);
                }
                detalle.addContent(precioTotalSinImpuesto);
                //Nodo detalles adicionales
                if (this.detalles.get(i).getDetallesAdicionales() != null && !this.detalles.get(i).getDetallesAdicionales().isEmpty()) {
                    Element detallesAdicionales = new Element("detallesAdicionales");
                    agregarDetallesAdicionales(detallesAdicionales, this.detalles.get(i).getDetallesAdicionales());
                    detalle.addContent(detallesAdicionales);
                }

                //Nodo superior que contiene todos los impuestos
                Element impuestos = new Element("impuestos");
                //Añadir cada uno de los impuesto de un detalle
                agregarImpuestosADetalle(impuestos, this.detalles.get(i).getImpuestos(), "impuesto");
                //añade todos los impuestos al detalle
                detalle.addContent(impuestos);
                //añade el detalle al nodo superiror
                _detallesNotaCreditoElement.addContent(detalle);
            }

        }
    }

    /**
     * Método que construye el nodo de impuestos de la Nota de Crédito
     *
     * @param _nodoPadre Elemento padre-superior que contiene la lista del total
     * con impuestos
     * @param _impuestos Lista de impuestos
     * @param _nombreSubNodo Nombre del nodo que contiene los impuestos
     */
    private void agregarImpuestos(Element _nodoPadre, List<ImpuestoComprobanteElectronico> _impuestos, String _nombreSubNodo) {

        for (int i = 0; i < _impuestos.size(); i++) {
            Element impSubNodo = new Element(_nombreSubNodo);

            Element codigo = new Element("codigo");
            codigo.setText(_impuestos.get(i).getCodigo());
            Element codigoPorcentaje = new Element("codigoPorcentaje");
            codigoPorcentaje.setText(_impuestos.get(i).getCodigoPorcentaje());
            Element baseImponible = new Element("baseImponible");
            baseImponible.setText(_impuestos.get(i).getBaseImponible());

            Element valor = new Element("valor");
            valor.setText(_impuestos.get(i).getValor());

            impSubNodo.addContent(codigo);
            impSubNodo.addContent(codigoPorcentaje);
            impSubNodo.addContent(baseImponible);
            impSubNodo.addContent(valor);

            _nodoPadre.addContent(impSubNodo);
        }
    }

    /**
     * Método que construye el nodo de impuestos de la Nota de Crédito
     *
     * @param _nodoPadre Elemento padre-superior que contiene los impuestos.
     * @param _impuestos List de impuestos para el detalle de la Nota de Crédito
     * @param _nombreSubNodo Nodo que contiene el detalle del impuesto
     */
    private void agregarImpuestosADetalle(Element _nodoPadre, List<ImpuestoComprobanteElectronico> _impuestos, String _nombreSubNodo) {

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
    public boolean ObjetoComprobanteAXML(String _direccionArchivoXMLOrigen) {

        boolean transformado = false;
        try {
            setDocumentoJDom(_direccionArchivoXMLOrigen);
            obtenerInformacionTributaria(this.documentoJDom);
            obtenerInformacionNotaCredito(this.documentoJDom);
            obtenerDetalles(this.documentoJDom);
            obtenerInformacionAdicional(this.documentoJDom);
            transformado = true;
        } catch (Exception ex) {
            Logger.getLogger(ImplementacionNotaCredito.class.getName()).log(Level.SEVERE, "Leer Archivo Nota de Credit", ex);
        }
        return transformado;
    }

    @Override
    public File ObjetoComprobanteAXML(String _direccionArchivoDestino, String version) {
        try {
            Document nuevo = new Document();

            Element notaCredito = new Element("notaCredito");
            notaCredito.setAttribute("id", "comprobante");
            notaCredito.setAttribute("version", version);

            Element infoTributaria = new Element("infoTributaria");
            Element infoNotaCredito = new Element("infoNotaCredito");
            Element det = new Element("detalles");
            nuevo.addContent(notaCredito);

            this.agregarInformacionTributaria(infoTributaria);
            this.agregarInfoNotaCredito(infoNotaCredito);
            this.agregarDetalles(det);

            notaCredito.addContent(infoTributaria);
            notaCredito.addContent(infoNotaCredito);
            notaCredito.addContent(det);

            if (this.informacionAdicional != null && !this.informacionAdicional.isEmpty()) {
                Element infoAdicional = new Element("infoAdicional");
                this.agregarInformacionAdicional(infoAdicional);
                notaCredito.addContent(infoAdicional);
            }

            Format format = Format.getPrettyFormat();

            XMLOutputter xmlop = new XMLOutputter(format);

            String docStr = xmlop.outputString(nuevo);
            File file = new File(_direccionArchivoDestino);
            OutputStream os = new FileOutputStream(file);
            Closeable resourceCloseable = os;
            try {
                try (Writer wtr = new OutputStreamWriter(os, Charset.forName("UTF-8"))) {
                    resourceCloseable = wtr;
                    wtr.write(docStr);
                    wtr.flush();
                }
            } finally {
                resourceCloseable.close();
            }
            return file;
        } catch (IOException ex) {
            Logger.getLogger(ImplementacionNotaCredito.class.getName()).log(Level.SEVERE, "Guardar Archivo Nota de Credito", ex);
            return null;
        }

    }

    @Override
    public ImplementacionFactura ConstruirFactura() {
        throw new UnsupportedOperationException("No se puede crear Factura a partir de una Nota de Crédito.");
    }

    @Override
    public ImplementacionNotaCredito ConstruirNotaCredito() {
        return this;
    }



    @Override
    public ImplementacionRetencion ConstruirComprobanteRetencion() {
        throw new UnsupportedOperationException("No se puede crear Comprobante de Retención a partir de una Nota de Crédito.");
    }

}
