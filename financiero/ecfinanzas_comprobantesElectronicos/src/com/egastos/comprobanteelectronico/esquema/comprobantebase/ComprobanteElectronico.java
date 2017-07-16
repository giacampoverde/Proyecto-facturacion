package com.egastos.comprobanteelectronico.esquema.comprobantebase;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionFactura;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionNotaCredito;
import com.egastos.comprobanteelectronico.esquema.implementacion.ImplementacionRetencion;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;

/**
 *
 * @Ricardo Delgado
 */
public abstract class ComprobanteElectronico implements Serializable {

    protected InformacionTributariaComprobanteElectronico informacionTributariaComprobanteElectronico;
    protected List<InformacionAdicional> informacionAdicional;

    public ComprobanteElectronico() {
        this.informacionTributariaComprobanteElectronico = new InformacionTributariaComprobanteElectronico();
        informacionAdicional = new ArrayList<>();
    }

    public InformacionTributariaComprobanteElectronico getInformacionTributariaComprobanteElectronico() {
        return informacionTributariaComprobanteElectronico;
    }

    public void setInformacionTributariaComprobanteElectronico(InformacionTributariaComprobanteElectronico infTrib) {
        this.informacionTributariaComprobanteElectronico = infTrib;
    }

    public List<InformacionAdicional> getInformacionAdicional() {
        return informacionAdicional;
    }

    public void setInformacionAdicional(List<InformacionAdicional> informacionAdicional) {
        this.informacionAdicional = informacionAdicional;
    }

    /**
     * Método que obtiene la información de los nodos de la InfoTributaria desde
     * un archivo XML
     *
     * @param _documentoJDom Objeto org.jdom.Document incializado con el archivo
     * XML que contienen los datos
     */
    protected void obtenerInformacionTributaria(Document _documentoJDom) {

        String nombre = "";

        // Se obtiene el elemento raíz
        Element elemento_raiz = _documentoJDom.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos_raiz = elemento_raiz.getChildren();

        for (Element hijo : hijos_raiz) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("infoTributaria")) {

                List<Element> datos = hijo.getChildren();

                for (Element d : datos) {
                    //Se obtienen los valores de las etiquetas de la Información Tributaria
                    switch (d.getName()) {
                        case "ambiente":
                            informacionTributariaComprobanteElectronico.setAmbiente(d.getValue());
                            break;
                        case "claveAcceso":
                            informacionTributariaComprobanteElectronico.setClaveAcceso(d.getValue());
                            break;
                        case "codDoc":
                            informacionTributariaComprobanteElectronico.setCodDoc(d.getValue());
                            break;
                        case "dirMatriz":
                            informacionTributariaComprobanteElectronico.setDirMatriz(d.getValue());
                            break;
                        case "estab":
                            informacionTributariaComprobanteElectronico.setCodigoEstablecimiento(d.getValue());
                            break;
                        case "nombreComercial":
                            informacionTributariaComprobanteElectronico.setNombreComercial(d.getValue());
                            break;
                        case "ptoEmi":
                            informacionTributariaComprobanteElectronico.setPuntoEmision(d.getValue());
                            break;
                        case "razonSocial":
                            informacionTributariaComprobanteElectronico.setRazonSocial(d.getValue());
                            break;
                        case "ruc":
                            informacionTributariaComprobanteElectronico.setRuc(d.getValue());
                            break;
                        case "secuencial":
                            informacionTributariaComprobanteElectronico.setSecuencial(d.getValue());
                            break;
                        case "tipoEmision":
                            informacionTributariaComprobanteElectronico.setTipoEmision(d.getValue());
                            break;
                    }
                }
            }
        }

    }

    protected byte[] obtenerInformacionComprobanteHijo(Document _documentoJDom) {
byte[] bytes=null;
        String nombre = "";

        // Se obtiene el elemento raíz
        Element elemento_raiz = _documentoJDom.getRootElement();

        // Se recorren los hijos del elemento raíz
        List<Element> hijos_raiz = elemento_raiz.getChildren();

        for (Element hijo : hijos_raiz) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("comprobante")) {
                ObjectOutputStream oos = null;
                try {
                    //trasformar de content a bites
                    List datos = hijo.getContent();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    oos = new ObjectOutputStream(bos);
                    oos.writeObject(datos);
                    bytes = bos.toByteArray();
                } catch (IOException ex) {
                    Logger.getLogger(ComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    try {
                        oos.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ComprobanteElectronico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        }
        return bytes;
    }

    /**
     * Método que obtiene la Información Adicional del Comprobante Electrónico
     * desde un archivo XML
     *
     * @param _documentoJDom Objeto org.jdom.Document inicializado con el
     * archivo origen del XML
     */
    protected void obtenerInformacionAdicional(Document _documentoJDom) {
        InformacionAdicional informacion_adicional = null;
        String nombre = "";

        // Se obtiene el elemento raíz
        Element elementoRaiz = _documentoJDom.getRootElement();

        // Se recorre los hijos del elemento raíz
        List<Element> hijos_raiz = elementoRaiz.getChildren();

        for (Element hijo : hijos_raiz) {

            // Se obtiene el nombre de la etiqueta
            nombre = hijo.getName();

            if (nombre.equals("infoAdicional")) {
                List<Element> datos = hijo.getChildren();
                for (Element dat : datos) {
                    informacion_adicional = new InformacionAdicional();
                    informacion_adicional.setNombre(dat.getAttributeValue("nombre"));
                    informacion_adicional.setValor(dat.getValue());
                    this.informacionAdicional.add(informacion_adicional);
                }
            }
        }
    }

    /**
     * Método que contruye el nodo XML infoTributaria con la Información
     * Tributaria de un Comprobante Electrónico
     *
     * @param _elementoInformacionTributaria Es el nodo principal que va a
     * contener la Información Tributaria del Comprobante Electrónico
     */
    protected void agregarInformacionTributaria(Element _elementoInformacionTributaria) {
        Element eAmbiente = new Element("ambiente");
        eAmbiente.setText(this.informacionTributariaComprobanteElectronico.getAmbiente());

        Element etipoEmision = new Element("tipoEmision");
        etipoEmision.setText(this.informacionTributariaComprobanteElectronico.getTipoEmision());

        Element erazonSocial = new Element("razonSocial");
        erazonSocial.setText(this.informacionTributariaComprobanteElectronico.getRazonSocial());

        Element enombreComercial = new Element("nombreComercial");
        enombreComercial.setText(this.informacionTributariaComprobanteElectronico.getNombreComercial());

        Element eruc = new Element("ruc");
        eruc.setText(this.informacionTributariaComprobanteElectronico.getRuc());

        Element eclaveAcceso = new Element("claveAcceso");
        eclaveAcceso.setText(this.informacionTributariaComprobanteElectronico.getClaveAcceso());

        Element ecodDoc = new Element("codDoc");
        ecodDoc.setText(this.informacionTributariaComprobanteElectronico.getCodDoc());

        Element eestab = new Element("estab");
        eestab.setText(this.informacionTributariaComprobanteElectronico.getCodigoEstablecimiento());

        Element eptoEmi = new Element("ptoEmi");
        eptoEmi.setText(this.informacionTributariaComprobanteElectronico.getPuntoEmision());

        Element esecuencial = new Element("secuencial");
        esecuencial.setText(this.informacionTributariaComprobanteElectronico.getSecuencial());

        Element edirMatriz = new Element("dirMatriz");
        edirMatriz.setText(this.informacionTributariaComprobanteElectronico.getDirMatriz());

        _elementoInformacionTributaria.addContent(eAmbiente);
        _elementoInformacionTributaria.addContent(etipoEmision);
        _elementoInformacionTributaria.addContent(erazonSocial);
        if (this.informacionTributariaComprobanteElectronico.getNombreComercial() != null) {
            _elementoInformacionTributaria.addContent(enombreComercial);
        }
        _elementoInformacionTributaria.addContent(eruc);
        _elementoInformacionTributaria.addContent(eclaveAcceso);
        _elementoInformacionTributaria.addContent(ecodDoc);
        _elementoInformacionTributaria.addContent(eestab);
        _elementoInformacionTributaria.addContent(eptoEmi);
        _elementoInformacionTributaria.addContent(esecuencial);
        _elementoInformacionTributaria.addContent(edirMatriz);
    }

    /**
     * Método que construye el nodo XML infoAdicional para todos los esquemas de
     * Comprobantes Electrónicos
     *
     * @param _elementoInformacionAdicional Es el nodo principal que va a
     * contener la Información Adicional del Comprobante Electrónico
     */
    protected void agregarInformacionAdicional(Element _elementoInformacionAdicional) {
        if (this.informacionAdicional != null && !this.informacionAdicional.isEmpty()) {
            for (int i = 0; i < this.informacionAdicional.size(); i++) {
                Element ecampoAdicional = new Element("campoAdicional");
                ecampoAdicional.setAttribute("nombre", this.informacionAdicional.get(i).getNombre());
                ecampoAdicional.setText(this.informacionAdicional.get(i).getValor());
                _elementoInformacionAdicional.addContent(ecampoAdicional);
            }

        }
    }

    /**
     * Método que construye el nodo XML detallesAdicionales para todos los
     * esquemas de Comprobantes Electrónicos
     *
     * @param _elementoDetallesAdicionales Elemento que va a contener los
     * detalles adicionales
     * @param _detallesAdicionales Lista de detalles adicionales que van a ser
     * añadidos al documento XML.
     */
    protected void agregarDetallesAdicionales(Element _elementoDetallesAdicionales, List<InformacionAdicional> _detallesAdicionales) {
        for (int i = 0; i < _detallesAdicionales.size(); i++) {
            Element edetAdicional = new Element("detAdicional");
            edetAdicional.setAttribute("nombre", _detallesAdicionales.get(i).getNombre());
            edetAdicional.setAttribute("valor", _detallesAdicionales.get(i).getValor());
            _elementoDetallesAdicionales.addContent(edetAdicional);
        }
    }

    /**
     * Metodo abstracto para leer un archivoXML y recorre los atributos para el
     * objeto ComprobanteElectronico
     *
     * @param _direccionArchivoXMLOrigen Xml de origen con los datos para mapear
     * el objeto ComprobanteElectronico
     * @return true si se construye el objeto, false si el el objeto no se pudo
     * mapear.
     */
    abstract public boolean ObjetoComprobanteAXML(String _direccionArchivoXMLOrigen);

    /**
     * Metodo que crea un archivo XML a partir de los atributos de los objetos
     * que contiene el Comprobante Electrónico.
     *
     * @param _direccionArchivoDestino Es la direccion en la cual se creará el
     * archivo del Comprobante Electrónico.
     * @param version
     * @return Objeto File creado
     */
    abstract public File ObjetoComprobanteAXML(String _direccionArchivoDestino, String version);

    abstract public ImplementacionFactura ConstruirFactura();

    abstract public ImplementacionNotaCredito ConstruirNotaCredito();

    abstract public ImplementacionRetencion ConstruirComprobanteRetencion();

}
