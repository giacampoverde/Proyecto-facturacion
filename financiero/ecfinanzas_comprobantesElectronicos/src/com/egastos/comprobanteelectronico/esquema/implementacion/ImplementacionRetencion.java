/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.comprobanteelectronico.esquema.implementacion;

import com.egastos.comprobanteelectronico.esquema.retencion.ComprobanteRetencionEsquema;
import com.egastos.comprobanteelectronico.esquema.retencion.ImpuestoRetencion;
import java.io.*;
import java.nio.charset.Charset;
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
public class ImplementacionRetencion extends ComprobanteRetencionEsquema {

    private Document docJdom;

    public ImplementacionRetencion() {
        super();
    }
 private void setDocumentoJDomFloral(String _direccionNombreArchivoXML) throws Exception {
     try{
         SAXBuilder builder = new SAXBuilder();
        docJdom = builder.build(new FileInputStream(_direccionNombreArchivoXML));
     }catch(JDOMException | IOException ex){
         System.out.println("archivo incorrecto");
            Logger.getLogger(ImplementacionFactura.class.getName()).log(Level.SEVERE, null, ex);
     }
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
        FileInputStream fInputStrm = null;

        try {

            File file = new File(_direccionNombreArchivoXML);
            InputStream inputStrm = new FileInputStream(file);
            Reader rdr = new InputStreamReader(inputStrm, "UTF-8");
            docJdom = saxbuilder.build(rdr);

            try {
                inputStrm.close();
            } catch (IOException e) {
                System.out.println("Error en Input Stream");
            }

            try {
                rdr.close();
            } catch (IOException e) {
                System.out.println("Error en Reader");
            }
        } catch (IOException | JDOMException e) {
            Logger.getLogger(ImplementacionRetencion.class.getName()).log(Level.SEVERE, "Error al crear objeto Documento de archivo XML", e);
        } finally {
            if (fInputStrm != null) {
                fInputStrm.close();
            }

        }
    }

    /**
     * Método que obtiene los datos del nodo infoCompRetencion a partir de un
     * objeto Document
     *
     * @param _documentoJDom
     */
    private void obtenerInformacionComprobanteRetencion(Document _documentJDOM) {
        String nombre = "";

        // Se obtiene el elemento raíz
        Element raiz = _documentJDOM.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos = raiz.getChildren();

        for (Element hijo : hijos) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("infoCompRetencion")) {

                List<Element> datos = hijo.getChildren();
                for (Element d : datos) {
                    //Se obtienen los valores de las etiquetas de infoCompRetencion
                    if (d.getName().equals("fechaEmision")) {
                        this.informacionComprobanteRetencion.setFechaEmision(d.getValue());
                    } else if (d.getName().equals("dirEstablecimiento")) {
                        this.informacionComprobanteRetencion.setDirEstablecimiento(d.getValue());
                    } else if (d.getName().equals("contribuyenteEspecial")) {
                        this.informacionComprobanteRetencion.setContribuyenteEspecial(d.getValue());
                    } else if (d.getName().equals("obligadoContabilidad")) {
                        this.informacionComprobanteRetencion.setObligadoContabilidad(d.getValue());
                    } else if (d.getName().equals("tipoIdentificacionSujetoRetenido")) {
                        this.informacionComprobanteRetencion.setTipoIdentificacionSujetoRetenido(d.getValue());
                    } else if (d.getName().equals("razonSocialSujetoRetenido")) {
                        this.informacionComprobanteRetencion.setRazonSocialSujetoRetenido(d.getValue());
                    } else if (d.getName().equals("identificacionSujetoRetenido")) {
                        this.informacionComprobanteRetencion.setIdentificacionSujetoRetenido(d.getValue());
                    } else if (d.getName().equals("periodoFiscal")) {
                        this.informacionComprobanteRetencion.setPeriodoFiscal(d.getValue());
                    }

                }

            }
        }
    }

    /**
     * Método que obtiene los datos del nodo impuestos a partir de un objeto
     * Document
     *
     * @param _documentoJDom
     */
    private void obtenerImpuestos(Document _documentJDOM) {
        ImpuestoRetencion impuesto_detalle = null;
        String nombre = "";

        // Se obtiene el elemento raíz
        Element raiz = _documentJDOM.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos = raiz.getChildren();

        for (Element hijo : hijos) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("impuestos")) {

                List<Element> datos = hijo.getChildren();

                for (Element dat : datos) {
                    List<Element> impuesto = dat.getChildren();
                    impuesto_detalle = new ImpuestoRetencion();
                    for (Element d : impuesto) {
                        //Se obtienen los valores de las etiquetas de infoNotaDebito
                        switch (d.getName()) {
                            case "codigo":
                                impuesto_detalle.setCodigo(d.getValue());
                                break;
                            case "codigoRetencion":
                                impuesto_detalle.setCodigoRetencion(d.getValue());
                                break;
                            case "baseImponible":
                                impuesto_detalle.setBaseImponible(d.getValue());
                                break;
                            case "porcentajeRetener":
                                impuesto_detalle.setPorcentajeRetener(d.getValue());
                                break;
                            case "valorRetenido":
                                impuesto_detalle.setValorRetenido(d.getValue());
                                break;
                            case "codDocSustento":
                                impuesto_detalle.setCodDocSustento(d.getValue());
                                break;
                            case "numDocSustento":
                                impuesto_detalle.setNumDocSustento(d.getValue());
                                break;
                            case "fechaEmisionDocSustento":
                                impuesto_detalle.setFechaEmisionDocSustento(d.getValue());
                                break;
                        }
                    }
                    this.impuestos.add(impuesto_detalle);
                }
            }
        }
    }

    /**
     * Método que construye el nodo de infoCompRetencion del Comprobante de
     * Retención
     *
     * @param _elementoInfoComprobanteRetencion
     */
    private void agregarInformacionComprobanteRetencion(Element _elementoInfoComprobanteRetencion) {

        Element fechaEmision = new Element("fechaEmision");
        fechaEmision.setText(this.informacionComprobanteRetencion.getFechaEmision());

        Element dirEstablecimiento = new Element("dirEstablecimiento");
        dirEstablecimiento.setText(this.informacionComprobanteRetencion.getDirEstablecimiento());

        Element contribuyenteEspecial = new Element("contribuyenteEspecial");
        contribuyenteEspecial.setText(this.informacionComprobanteRetencion.getContribuyenteEspecial());

        Element obligadoContabilidad = new Element("obligadoContabilidad");
        obligadoContabilidad.setText(this.informacionComprobanteRetencion.getObligadoContabilidad());

        Element tipoIdentificacionSujetoRetenido = new Element("tipoIdentificacionSujetoRetenido");
        tipoIdentificacionSujetoRetenido.setText(this.informacionComprobanteRetencion.getTipoIdentificacionSujetoRetenido());

        Element razonSocialSujetoRetenido = new Element("razonSocialSujetoRetenido");
        razonSocialSujetoRetenido.setText(this.informacionComprobanteRetencion.getRazonSocialSujetoRetenido());

        Element identificacionSujetoRetenido = new Element("identificacionSujetoRetenido");
        identificacionSujetoRetenido.setText(this.informacionComprobanteRetencion.getIdentificacionSujetoRetenido());

        Element periodoFiscal = new Element("periodoFiscal");
        periodoFiscal.setText(this.informacionComprobanteRetencion.getPeriodoFiscal());

        _elementoInfoComprobanteRetencion.addContent(fechaEmision);
        if (this.informacionComprobanteRetencion.getDirEstablecimiento() != null && !this.informacionComprobanteRetencion.getDirEstablecimiento().equals("")) {
            _elementoInfoComprobanteRetencion.addContent(dirEstablecimiento);
        }
        if (this.informacionComprobanteRetencion.getContribuyenteEspecial() != null && !this.informacionComprobanteRetencion.getContribuyenteEspecial().equals("")) {
            _elementoInfoComprobanteRetencion.addContent(contribuyenteEspecial);
        }
        if (this.informacionComprobanteRetencion.getObligadoContabilidad() != null && !this.informacionComprobanteRetencion.getObligadoContabilidad().equals("")) {
            _elementoInfoComprobanteRetencion.addContent(obligadoContabilidad);
        }
        _elementoInfoComprobanteRetencion.addContent(tipoIdentificacionSujetoRetenido);
        _elementoInfoComprobanteRetencion.addContent(razonSocialSujetoRetenido);
        _elementoInfoComprobanteRetencion.addContent(identificacionSujetoRetenido);
        _elementoInfoComprobanteRetencion.addContent(periodoFiscal);

    }

    /**
     * Método que construye el nodo de impuesto del Comprobante de Retención
     *
     * @param _elementoImpuestoComprobante
     */
    public void agregarImpuestos(Element _elementoImpuestoComprobante) {
        if (this.impuestos != null && !this.impuestos.isEmpty()) {
            for (int i = 0; i < this.impuestos.size(); i++) {
                Element impuesto = new Element("impuesto");

                Element codigo = new Element("codigo");
                codigo.setText(this.impuestos.get(i).getCodigo());
                Element codigoRetencion = new Element("codigoRetencion");
                codigoRetencion.setText(this.impuestos.get(i).getCodigoRetencion());
                Element baseImponible = new Element("baseImponible");
                baseImponible.setText(this.impuestos.get(i).getBaseImponible());
                Element porcentajeRetener = new Element("porcentajeRetener");
                porcentajeRetener.setText(this.impuestos.get(i).getPorcentajeRetener());
                Element valorRetenido = new Element("valorRetenido");
                valorRetenido.setText(this.impuestos.get(i).getValorRetenido());
                Element codDocSustento = new Element("codDocSustento");
                codDocSustento.setText(this.impuestos.get(i).getCodDocSustento());
                Element numDocSustento = new Element("numDocSustento");
                numDocSustento.setText(this.impuestos.get(i).getNumDocSustento());
                Element fechaEmisionDocSustento = new Element("fechaEmisionDocSustento");
                fechaEmisionDocSustento.setText(this.impuestos.get(i).getFechaEmisionDocSustento());

                impuesto.addContent(codigo);
                impuesto.addContent(codigoRetencion);
                impuesto.addContent(baseImponible);
                impuesto.addContent(porcentajeRetener);
                impuesto.addContent(valorRetenido);
                impuesto.addContent(codDocSustento);
                if ((this.impuestos.get(i).getNumDocSustento() != null) && (!this.impuestos.get(i).getNumDocSustento().equals(""))) {
                    impuesto.addContent(numDocSustento);
                }
                if ((this.impuestos.get(i).getFechaEmisionDocSustento() != null) && (!this.impuestos.get(i).getFechaEmisionDocSustento().equals(""))) {
                    impuesto.addContent(fechaEmisionDocSustento);
                }

                _elementoImpuestoComprobante.addContent(impuesto);
            }

        }
    }

    @Override
    public boolean ObjetoComprobanteAXML(String _direccionArchivoXMLOrigen) {
        boolean flag = false;
        try {
            setDocumentoJDom(_direccionArchivoXMLOrigen);
            obtenerInformacionTributaria(this.docJdom);
            obtenerInformacionComprobanteRetencion(this.docJdom);
            obtenerImpuestos(this.docJdom);
            obtenerInformacionAdicional(docJdom);
            flag = true;
        } catch (Exception ex) {
            Logger.getLogger(ImplementacionRetencion.class.getName()).log(Level.SEVERE, "Leer Archivo", ex);
        }
        return flag;
    }

    @Override
    public File ObjetoComprobanteAXML(String _direccionArchivoDestino, String version) {
        try {
            Document nuevo = new Document();

            Element comprobanteRetencion = new Element("comprobanteRetencion");
            comprobanteRetencion.setAttribute("id", "comprobante");
            comprobanteRetencion.setAttribute("version", version);

            Element infoTributaria = new Element("infoTributaria");
            Element infoCompRetencion = new Element("infoCompRetencion");
            Element impuestos = new Element("impuestos");

            nuevo.addContent(comprobanteRetencion);

            this.agregarInformacionTributaria(infoTributaria);
            this.agregarInformacionComprobanteRetencion(infoCompRetencion);
            this.agregarImpuestos(impuestos);

            comprobanteRetencion.addContent(infoTributaria);
            comprobanteRetencion.addContent(infoCompRetencion);
            comprobanteRetencion.addContent(impuestos);

            if (this.informacionAdicional != null && !this.informacionAdicional.isEmpty()) {
                Element infoAdicional = new Element("infoAdicional");
                this.agregarInformacionAdicional(infoAdicional);
                comprobanteRetencion.addContent(infoAdicional);
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
            Logger.getLogger(ImplementacionRetencion.class.getName()).log(Level.SEVERE, "Guardar Archivo", ex);
            return null;
        }
    }

    @Override
    public ImplementacionFactura ConstruirFactura() {
        throw new UnsupportedOperationException("no se puede convertir a objeto factura");
    }

    @Override
    public ImplementacionNotaCredito ConstruirNotaCredito() {
        throw new UnsupportedOperationException("no se puede convertir a objeto Nota de Credito");
    }
    @Override
    public ImplementacionRetencion ConstruirComprobanteRetencion() {
        return this;
    }

   

}
