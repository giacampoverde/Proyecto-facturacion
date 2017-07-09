/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.firma.firmaxades;

import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.ValidarFirmaXML;
import es.mityc.firmaJava.libreria.xades.errores.FirmaXMLError;
import es.mityc.javasign.ts.TimeStampValidator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertPath;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERUTF8String;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author Jesus_Mendoza
 */
public class ValidacionFirma {

    public boolean validarFirma(String fichero) {
        List<ResultadoValidacion> resVal = new ArrayList<>();
        DocumentBuilderFactory docBuilderFac = DocumentBuilderFactory.newInstance();
        docBuilderFac.setNamespaceAware(true); // Para que no se altere el orden de los atributos de nodo invalidando la firma 
        DocumentBuilder docBuilder = null;
        Document documento = null;
        boolean docEncontrado = false;
        boolean validado = true;
        try {
            docBuilder = docBuilderFac.newDocumentBuilder();
            documento = docBuilder.parse(fichero);
            docEncontrado = true;
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(ValidacionFirma.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (docEncontrado) {
            try {
                ValidarFirmaXML valXml = new ValidarFirmaXML();
                resVal = valXml.validar(documento, "./", null, new TimeStampValidator());
                for (ResultadoValidacion resValidacion : resVal) {
                    System.out.println("La firma es valida.\n" + resValidacion.getNivelValido());
                    if (!resValidacion.isValidate()) {
                        validado = false;

                    }
                }
            } catch (FirmaXMLError e) {
                e.printStackTrace();
            }
        }
        return validado;

    }

    public InformacionCertificado obtenerInformacionFirmante(String fichero, String titulo, String identificadorXML ) throws IOException {
        InformacionCertificado ic = new InformacionCertificado();
        List<ResultadoValidacion> resVal = new ArrayList<>();
        DocumentBuilderFactory docBuilderFac = DocumentBuilderFactory.newInstance();
        docBuilderFac.setNamespaceAware(true); // Para que no se altere el orden de los atributos de nodo invalidando la firma 
        DocumentBuilder docBuilder = null;
        Document documento = null;
        boolean docEncontrado = false;
        boolean validado = true;
        byte[] bites = null;
        try {
            docBuilder = docBuilderFac.newDocumentBuilder();
            documento = docBuilder.parse(fichero);
            docEncontrado = true;
        } catch (SAXException | IOException | ParserConfigurationException ex) {
            Logger.getLogger(ValidacionFirma.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (docEncontrado) {
            try {
                ValidarFirmaXML valXml = new ValidarFirmaXML();
                resVal = valXml.validar(documento, "./", null, new TimeStampValidator());

                for (ResultadoValidacion resValidacion : resVal) {
                    //ic.setValidezFirma( resValidacion.getNivelValido());
                    CertPath certs = resValidacion.getDatosFirma().getCadenaFirma();
                    List<?> clist = certs.getCertificates();
                    Object[] ob = clist.toArray();
                    for (Object ob1 : ob) {
                        X509Certificate xc = (X509Certificate) ob1;
                        ic.setTitulo(titulo);
                        ic.setIdentificadorXML(identificadorXML);//Para saber que tipo de archivos XML se debe descargar (Factura Original, Archivo Confirmacion o Endoso 1, 2 , 3,....,n)
                        ic.setRazonSocial(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.10"));
                        ic.setRuc(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.11"));
                        ic.setCedulaRepresentante(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.1"));
                        ic.setNombresRepresentante(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.2"));
                        ic.setPrimerApellidoRepresentante(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.3"));
                        ic.setSegundoApellidoRepresentante(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.4"));
                        ic.setCargoRepresentante(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.5"));
                        ic.setDireccionEmpresa(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.7"));
                        ic.setTelefonoEmpresa(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.8"));
                        ic.setCiudadEmpresa(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.9"));
                        ic.setPaisEmpresa(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.12"));
                        ic.setTipoCertificado(getExtensionValue(xc, "1.3.6.1.4.1.37947.3.51"));
                        ic.setValidezFirma(resValidacion.getNivelValido());
                        ic.setInformacionAutoridadCertificacion(xc.getIssuerDN().toString().split(",")[0].substring(3));                     
                        ic.setAlgoritmoFirma(xc.getSigAlgName());
                        ic.setValidoDesde(xc.getNotBefore());
                        ic.setValidoHasta(xc.getNotAfter());
                    }
                }
            } catch (FirmaXMLError e) {
                e.printStackTrace();
            }
        }
        return ic;

    }

    /**
     * Funcion para decodificar caracteres especiales presentes en las cadenas
     * obtenidas de informacion del certificado, cifradas en la firma
     * electronica delXML
     *
     * @param X509Certificate
     * @param oid
     * @return
     * @throws IOException
     */
    private String getExtensionValue(X509Certificate X509Certificate, String oid) throws IOException {
        String decoded = null;
        byte[] extensionValue = X509Certificate.getExtensionValue(oid);

        if (extensionValue != null) {
            DERObject derObject = toDERObject(extensionValue);
            if (derObject instanceof DEROctetString) {
                DEROctetString derOctetString = (DEROctetString) derObject;

                derObject = toDERObject(derOctetString.getOctets());
                decoded = derObject.toString();
                if (derObject instanceof DERUTF8String) {
                    DERUTF8String s = DERUTF8String.getInstance(derObject);
                    decoded = s.getString();
                }

            }
        }
        return decoded;
    }

    private DERObject toDERObject(byte[] data) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(data);
        ASN1InputStream asnInputStream = new ASN1InputStream(inStream);
        return asnInputStream.readObject();
    }
}
