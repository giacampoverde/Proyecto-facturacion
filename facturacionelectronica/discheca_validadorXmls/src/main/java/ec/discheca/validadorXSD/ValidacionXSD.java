package ec.discheca.validadorXSD;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ricardo Delgado
 */
public class ValidacionXSD {

    private String mensajeResumenXSD = "";

    public String getMensajeResumenXSD() {
        return mensajeResumenXSD;
    }

    public void setMensajeResumenXSD(String mensajeResumenXSD) {
        this.mensajeResumenXSD = mensajeResumenXSD;
    }

    /**
     * valida el contenido del archivo XML dentro de los parametros establecidos
     * por el SRI
     *
     * @param _comprobante Objeto File con el archivo a validar.
     * @param _codigoComprobante String con el tipo de archivo a validar
     * @throws Exception si no se encuentra los archivos asociados a la
     * validacion o si no se valida correctamente
     */
    public boolean validarComprobantePorTipo(File _comprobante, String _codigoComprobante) throws IOException, SAXException {
        boolean exito = false;
        String pathtotal = "/ec/discheca/validadorXSD";

        if (_codigoComprobante.matches("01")) {
            exito = this.validarComprobante(_comprobante, pathtotal + "/factura10.xsd");
        } else if (_codigoComprobante.matches("07")) {
            exito = this.validarComprobante(_comprobante, pathtotal + "/comprobanteRetencion10.xsd");
        } else if (_codigoComprobante.matches("04")) {
//            exito = this.validarComprobante(_comprobante, pathtotal + "/notaCredito10.xsd");
            exito = this.validarComprobante(_comprobante, pathtotal + "/Nota_Credito_V_1_1_0.xsd");

        }

        return exito;

    }

    public boolean validarComprobantePorTipoVersion1110(File _comprobante, String _codigoComprobante) throws IOException, SAXException {
        boolean exito = false;
        String pathtotal = "/ec/discheca/validadorXSD";
        try {
            if (_codigoComprobante.matches("01")) {
                exito = this.validarComprobante(_comprobante, pathtotal + "/factura2.xsd");
            } else if (_codigoComprobante.matches("04")) {
//                exito = this.validarComprobante(_comprobante, pathtotal + "/notaCredito2Respaldo.xsd");
                     exito = this.validarComprobante(_comprobante, pathtotal + "/notaCredito2.xsd");
            } else if (_codigoComprobante.matches("06")) {
                exito = this.validarComprobante(_comprobante, pathtotal + "/guiaRemision2.xsd");
            }
        } catch (Exception e) {

        }
        return exito;

    }

    public boolean validarComprobantePorTipoFloral(File _comprobante, String _codigoComprobante) throws IOException, SAXException {
        boolean exito = false;
        String pathtotal = "/ec/discheca/validadorXSD";

        if (_codigoComprobante.matches("01")) {
            exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/factura10.xsd");
        } else if (_codigoComprobante.matches("07")) {
            exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/comprobanteRetencion10.xsd");
        } else if (_codigoComprobante.matches("06")) {
            exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/guiaRemision10.xsd");
        } else if (_codigoComprobante.matches("04")) {
            exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/notaCredito10.xsd");
        } else if (_codigoComprobante.matches("05")) {
            exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/notaDebito10.xsd");
        }

        return exito;

    }

    public boolean validarComprobantePorTipoVersion1110Floral(File _comprobante, String _codigoComprobante) throws IOException, SAXException {
        boolean exito = false;
        String pathtotal = "/ec/discheca/validadorXSD";
        try {
            if (_codigoComprobante.matches("01")) {
                exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/factura2.xsd");
            } else if (_codigoComprobante.matches("04")) {
                exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/notaCredito2.xsd");
            } else if (_codigoComprobante.matches("06")) {
                exito = this.validarComprobanteFloral(_comprobante, pathtotal + "/guiaRemision2.xsd");
            }
        } catch (Exception e) {

        }
        return exito;

    }

    /**
     * valida el contenido del archivo XML dentro de los parametros establecidos
     * por el SRI
     *
     * @param _comprobante
     * @param _codigoComprobante
     * @return true si el comprobante es correcto
     * @throws IOException
     * @throws SAXException
     */
    public boolean validarComprobantePorTipo(Document _comprobante, String _codigoComprobante) throws IOException, SAXException {
        boolean exito = false;
        String pathtotal = "/ec/discheca/validadorXSD";

        if (_codigoComprobante.matches("01")) {
            exito = this.validarComprobante(_comprobante, pathtotal + "/factura1.xsd");
        } else if (_codigoComprobante.matches("07")) {
            exito = this.validarComprobante(_comprobante, pathtotal + "/comprobanteRetencion1.xsd");
        } else if (_codigoComprobante.matches("06")) {
            exito = this.validarComprobante(_comprobante, pathtotal + "/guiaRemision1.xsd");
        } else if (_codigoComprobante.matches("04")) {
            exito = this.validarComprobante(_comprobante, pathtotal + "/notaCredito1.xsd");
        } else if (_codigoComprobante.matches("05")) {
            exito = this.validarComprobante(_comprobante, pathtotal + "/notaDebito1.xsd");
        }

        return exito;
    }

    /**
     * funcion que ejecuta la validacion del comprobante contra el XSD
     *
     * @param _comprobante
     * @param _direccionXSD
     * @return true si el archivo es correcto
     * @throws IOException
     * @throws SAXException
     */
    public boolean validarComprobante(File _comprobante, String _direccionXSD) throws IOException, SAXException {
        boolean exito = false;
        //
        URL location = getClass().getResource(_direccionXSD);
        //
        //Instanciamos la fabrica de W3C XML Schema language.
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        //factory.newSchema(new StreamSource(this.getClass().getResourceAsStream("xmldsig-core-schema.xsd")));
        //Instanciamos el esquema a utilizar para validar.

        // File schemaLocation = new File(_direccionXSD);
        Schema schema = factory.newSchema(location);
        Validator validator = schema.newValidator();
        InputStream inputStream = new FileInputStream(_comprobante);
        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        // creamos el objeto Source a validar a partir del file del comprobante
        Source source = new StreamSource(reader);

        // validamos el docuemnto
        try {
            validator.validate(source);
            this.setMensajeResumenXSD("la estructura XML y datos del comprobante son correctos");
            exito = true;
        } catch (SAXException ex) {

            this.setMensajeResumenXSD(ex.getMessage());
            exito = false;
        }

        return exito;
    }

    public boolean validarComprobanteFloral(File _comprobante, String _direccionXSD) throws IOException, SAXException {
        boolean exito = false;
        //
        URL location = getClass().getResource(_direccionXSD);
        //
        //Instanciamos la fabrica de W3C XML Schema language.
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        //factory.newSchema(new StreamSource(this.getClass().getResourceAsStream("xmldsig-core-schema.xsd")));
        //Instanciamos el esquema a utilizar para validar.

        // File schemaLocation = new File(_direccionXSD);
        Schema schema = factory.newSchema(location);
        Validator validator = schema.newValidator();
//        InputStream inputStream = new FileInputStream(_comprobante);
//        Reader reader = new InputStreamReader(inputStream);
//        // creamos el objeto Source a validar a partir del file del comprobante
        Source source = new StreamSource(_comprobante);

        // validamos el docuemnto
        try {
            validator.validate(source);
            this.setMensajeResumenXSD("la estructura XML y datos del comprobante son correctos");
            exito = true;
        } catch (SAXException ex) {

            this.setMensajeResumenXSD(ex.getMessage());
            exito = false;
        }

        return exito;
    }

    /**
     * funcion que ejecuta la validacion del comprobante contra el XSD
     *
     * @param _comprobante
     * @param _direccionXSD
     * @return true si el archivo es correcto
     * @throws IOException
     * @throws SAXException
     */
    public boolean validarComprobante(Document _comprobante, String _direccionXSD) throws IOException, SAXException {
        boolean exito = false;
        //
        URL location = getClass().getResource(_direccionXSD);
        //
        //Instanciamos la fabrica de W3C XML Schema language.
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        //Instanciamos el esquema a utilizar para validar.

        // File schemaLocation = new File(_direccionXSD);
        Schema schema = factory.newSchema(location);
        Validator validator = schema.newValidator();

        // creamos el objeto Source a validar a partir del Objeto Document
        Source source = new SAXSource(documentToInputSource(_comprobante));

        // validamos el docuemnto
        try {
            validator.validate(source);
            this.setMensajeResumenXSD("Validacion XSD : la estructura XML y datos del comprobante son correctos");
            exito = true;
        } catch (SAXException ex) {
            Logger.getLogger(ValidacionXSD.class.getName()).log(Level.SEVERE, "Validacion XSD " + ex.getMessage());
            this.setMensajeResumenXSD(ex.getMessage());
            exito = false;
        }

        return exito;
    }

    /**
     * funcion que tranforma un objeto Document DOM a Input Source
     *
     * @param _documento
     * @return true si la transformacion fue exitosa
     */
    public static InputSource documentToInputSource(Document _documento) {
        InputSource iSource = null;
        try {
            DOMSource source = new DOMSource(_documento);
            StringWriter xmlAsWriter = new StringWriter();
            StreamResult result = new StreamResult(xmlAsWriter);
            TransformerFactory.newInstance().newTransformer().transform(source, result);
            StringReader xmlReader = new StringReader(xmlAsWriter.toString());
            iSource = new InputSource(xmlReader);
        } catch (Exception ex) {
            Logger.getLogger(ValidacionXSD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iSource;
    }

    /**
     * funcion que elimina el nodo de firma de un document DOM
     *
     * @param _doc
     * @return decuelve el nuevo document DOM sin el nodo de Firma
     */
    public static Document eliminarNodoDeFirma(Document _doc) {
        Element elementoFirma = (Element) _doc.getElementsByTagName("ds:Signature").item(0);
        if (elementoFirma != null) {
            elementoFirma.getParentNode().removeChild(elementoFirma);
        }

        return _doc;
    }

}
