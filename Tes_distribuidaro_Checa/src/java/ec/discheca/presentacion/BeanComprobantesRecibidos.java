/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.discheca.presentacion;

import discheca.utilidades.Utilidades;
import ec.discheca.configuracion.ControlSesion;
import ec.discheca.configuracion.Valores;
import ec.discheca.dao.DAOAsignacionComprobanteElectronico;
import ec.discheca.modelo.AsignacionComprobanteElectronico;
import ec.discheca.utilidades.Descarga;
import ec.discheca.utilidades.RIDE;
import ec.discheca.dao.DAOAuditoria;
import ec.discheca.dao.DAOComprobanteElectronico;
import ec.discheca.dao.DAOUsuarioAcceso;
import ec.discheca.modelo.Auditoria;
import ec.discheca.modelo.ComprobanteElectronico;
import ec.discheca.serviciofacturacion.AlmacenamientoComprobanteElectronico;
import ec.discheca.utilidades.MensajesPrimefaces;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@ManagedBean
@ViewScoped
public class BeanComprobantesRecibidos implements Serializable {

    private String seleccionPeriodoTiempo;
    private Date fechaInicial;
    private Date fechaFinal;
    private String secuencialBusqueda;
    private String tipoDocumento;
    LazyDataModel<AsignacionComprobanteElectronico> comprobantesRecibidosLazy;
    private List<AsignacionComprobanteElectronico> comprobantesElectronicosSeleccionados;
    private AsignacionComprobanteElectronico comprobanteElectronicoSeleccionado;
    private List<String> ambiente;
    private String estado;
    private Date fechaSeleccionadaInicio;
    private Date fechaActual;
    File txtContactos;
    List<String> correos;
    List<String[]> listacontenedora = new ArrayList<String[]>();

    public Date getFechaSeleccionadaInicio() {
        return fechaSeleccionadaInicio;
    }

    public void setFechaSeleccionadaInicio(Date fechaSeleccionadaInicio) {
        this.fechaSeleccionadaInicio = fechaSeleccionadaInicio;
    }

    public Date getFechaActual() {
        return fechaActual;
    }

    public void setFechaActual(Date fechaActual) {
        this.fechaActual = fechaActual;
    }

    public List<String> getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(List<String> ambiente) {
        this.ambiente = ambiente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public AsignacionComprobanteElectronico getComprobanteElectronicoSeleccionado() {
        return comprobanteElectronicoSeleccionado;
    }

    public void setComprobanteElectronicoSeleccionado(AsignacionComprobanteElectronico comprobanteElectronicoSeleccionado) {
        this.comprobanteElectronicoSeleccionado = comprobanteElectronicoSeleccionado;
    }

    public List<AsignacionComprobanteElectronico> getComprobantesElectronicosSeleccionados() {
        return comprobantesElectronicosSeleccionados;
    }

    public void setComprobantesElectronicosSeleccionados(List<AsignacionComprobanteElectronico> comprobantesElectronicosSeleccionados) {
        this.comprobantesElectronicosSeleccionados = comprobantesElectronicosSeleccionados;
    }

    public LazyDataModel<AsignacionComprobanteElectronico> getComprobantesRecibidosLazy() {
        return comprobantesRecibidosLazy;
    }

    public void setComprobantesRecibidosLazy(LazyDataModel<AsignacionComprobanteElectronico> comprobantesRecibidosLazy) {
        this.comprobantesRecibidosLazy = comprobantesRecibidosLazy;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getSecuencialBusqueda() {
        return secuencialBusqueda;
    }

    public void setSecuencialBusqueda(String secuencialBusqueda) {
        this.secuencialBusqueda = secuencialBusqueda;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getSeleccionPeriodoTiempo() {
        return seleccionPeriodoTiempo;
    }

    public void setSeleccionPeriodoTiempo(String seleccionPeriodoTiempo) {
        this.seleccionPeriodoTiempo = seleccionPeriodoTiempo;
    }

    /**
     * Creates a new instance of BeanComprobantesRecibidos
     */
    public BeanComprobantesRecibidos() {
        guardarLogRegistros("Acceso al modulo  Comprobantes Recibidos");
        cargarTablaComprobantesRecibidosAutorizados();
    }

    public void guardarLogRegistros(String mensaje) {
        try {
            ControlSesion sesion = new ControlSesion();
            DAOUsuarioAcceso daoacceso = new DAOUsuarioAcceso();
            DAOAuditoria auditoria = new DAOAuditoria();
            Auditoria insertAudi = new Auditoria();
            insertAudi.setFecha(new Date());
            insertAudi.setFechaHora(new Date());
            insertAudi.setMensajeTransaccion(mensaje);
            insertAudi.setUsuarioAcceso(daoacceso.obtenerUsuarioAccesoPorId(Integer.parseInt(sesion.obtenerIdUsuarioSesionActiva())));
            auditoria.insertarRegistro(insertAudi);
        } catch (Exception ex) {
            Logger.getLogger(BeanAdministracionProductosServicios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargatxtclavesAcceso(FileUploadEvent event) throws Exception {

        try {
            txtContactos = new File(event.getFile().getFileName());
            FileOutputStream fos = new FileOutputStream(txtContactos);
            fos.write(event.getFile().getContents());
            fos.flush();
            fos.close();
            lecturaArchivoTxt();
        } catch (IOException ex) {
        }

    }

    public void lecturaArchivoTxt() {
        try {
            boolean almacenado = false;
            DAOComprobanteElectronico daocomrpobante = new DAOComprobanteElectronico();
            String detallesError = " <br> ";
            int contadorclavesAcceso = 0;
            boolean respuestaAlprocesar = false;
            List<String> lineastxt = obtenerlineasAccesoTxt();
            for (int i = 0; i < lineastxt.size(); i++) {
                String claveAcceso = validarClaveAccesoNombre(lineastxt.get(i) + ".");
                if (!claveAcceso.equals("")) {
                    Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.INFO, "lina del archivo txt a procesar. " + i);
                    Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.INFO, "claveAcceso a procesar." + claveAcceso);
                    ComprobanteElectronico respuestacomprobante = daocomrpobante.obtenerComprobatePorCA(claveAcceso);
                    if (respuestacomprobante == null) {
                        respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveAcceso);
                        
                    } else {
                        almacenado = true;
                        detallesError = detallesError + " YA SE ENCUENTRA ALMACENADO EL COMRPOBANTE CON CLAVE DE ACCESO: " + claveAcceso + " " + " LINEA: " + i + "<br>";
                    }
                    if (!almacenado) {
                        if (!respuestaAlprocesar) {
                            
                            detallesError = detallesError + " ERROR AL INSERTAR COMPROBANTE CON CLAVE DE ACCESO: " + claveAcceso + "  " + " LINEA: " + i + "<br>";
                        } else {
                            contadorclavesAcceso++;
                            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.INFO, "Archivo almacenado.");
                        }
                    }
                } else {
                    detallesError = detallesError + " EL CONTENIDO NO CORRESPONDE A UNA CLAVE DE ACCESO  " + " LINEA: " + i + "<br>";
                    Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.INFO, "La linea no corresponde a una clave de acceso .");
                }
            }
            if (lineastxt.size() > 0) {
                if (contadorclavesAcceso == lineastxt.size()) {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Archivo Procesado exitosamente.");
                } else {
                    MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se proceso todo el contenido del txt.");
                }
            } else {
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El Archivo esta vacio.");
            }
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<String> obtenerlineasAccesoTxt() {
        List<String> clavesAcceso = new ArrayList<String>();
        BufferedWriter bw = null;
        String sFichero = null;
        File fichero = null;


        String fileName;
        fileName = "" + new Date().toString();

        listacontenedora = new ArrayList<String[]>();
        Boolean respuesta = Boolean.FALSE;
        if (txtContactos != null) {
            
            sFichero = "" + "ERR - " + fileName + txtContactos.getName();
            fichero = new File(sFichero);

        }
        if (txtContactos != null && !txtContactos.getName().equals("")) {

            InputStreamReader fr = null;
            try {
                fr = new InputStreamReader(new FileInputStream(txtContactos), "UTF8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.INFO, "Error en la lectura txt.");
            } catch (FileNotFoundException ex) {

            }
            BufferedReader br = new BufferedReader(fr);
            String texto = "";

            try {
                while ((texto = br.readLine()) != null) {
                    if (!texto.equals("")) {
                        clavesAcceso.add(texto);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.INFO, "Error en la lectura txt.");
                Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return clavesAcceso;
    }

    public void cargaxml(FileUploadEvent event) throws Exception {
        ControlSesion ms = new ControlSesion();

        try {
            txtContactos = new File(event.getFile().getFileName());
            FileOutputStream fos = new FileOutputStream(txtContactos);
            fos.write(event.getFile().getContents());
            fos.flush();
            fos.close();
            lecturaxml(event.getFile().getContents());
        } catch (IOException ex) {
        }

    }

    public void lecturaxml(byte[] evento) {
        String claveDeAcceso = "";
        boolean respuestaAlprocesar = false;
        claveDeAcceso = validarClaveAccesoNombre(txtContactos.getName());
        if (claveDeAcceso.equals("")) {
            claveDeAcceso = obtenerclaveDeAccesoXml(evento);
            if (!claveDeAcceso.equals("")) {
                respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveDeAcceso);
            }
        } else {
            respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveDeAcceso);
        }
        if (respuestaAlprocesar) {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Almacenado Correctamente.");
        } else {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "El Comprobante ya se ecunetra amlacenado.");
        }
    }

    public static boolean ProcesoAlmacenamientoClaveAcceso(String ClaveAcceso) {
        boolean guardada = false;
        try {

            AlmacenamientoComprobanteElectronico ac = new AlmacenamientoComprobanteElectronico();
            guardada = ac.guardarPorClaveDeAcceso(ClaveAcceso);

        } catch (Exception ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return guardada;
    }

    public static String validarClaveAccesoNombre(String nombreArchivo) {
        String claveAcceso = "";
        String[] validarClave = nombreArchivo.split("\\.");
        if (validarClave.length > 0) {

            if (validarClave[0].length() == 49 && solonumeros(validarClave[0])) {
                claveAcceso = validarClave[0];
            }
        }
        return claveAcceso;
    }

    public static boolean solonumeros(String cedula) {

        if (!(cedula.matches("^[0-9]{49}$"))) {
            return false;
        }
        return true;
    }

    public static String obtenerclaveDeAccesoXml(byte[] part) {
        File archivo = new File("C:\\RecepcionTcs\\" + "xmldescargar.xml");
        Document documentotrasformado = null;
        String claveAcceso = "";

        NodeList respuesta1 = null;
        String respuesta = null;
        Document xml = null;
        OutputStream out;
        try {
            out = new FileOutputStream(archivo);
            out.write(part);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
        }

        try {

            DocumentBuilder dBuilder = null;
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xml = dBuilder.parse(archivo);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
        } catch (SAXException ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
        } catch (IOException ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
        }
        if (xml != null) {
            respuesta1 = xml.getElementsByTagName("claveAcceso");
        }
        String claveclaveDeAccesosinsri = null;

        if (respuesta1 != null && respuesta1.getLength() > 0) {
            claveclaveDeAccesosinsri = respuesta1.item(0).getTextContent();
        }
        if (xml != null && claveclaveDeAccesosinsri == null) {
            NodeList nodoComprobante = xml.getElementsByTagName("comprobante");
            for (int l = 0; l < nodoComprobante.getLength(); l++) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) nodoComprobante.item(l);
                Node child = element.getFirstChild();
                if (child instanceof CharacterData) {
                    CharacterData cd = (CharacterData) child;
                    respuesta = cd.getData();
                    break;
                }
            }
        } else {
            claveAcceso = claveclaveDeAccesosinsri;
        }
        if (respuesta != null) {
            documentotrasformado = convertStringToDocument(respuesta);
            if (documentotrasformado != null) {
                claveAcceso = documentotrasformado.getElementsByTagName("claveAcceso").item(0).getTextContent();

            } else {

                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_INFO, "Erro en el Documento.");
            }
        }
        return claveAcceso;

    }

    private static Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlStr)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final void cargarTablaComprobantesRecibidosAutorizados() {
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            comprobantesRecibidosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<AsignacionComprobanteElectronico> comprobantesElectronicos = new ArrayList<AsignacionComprobanteElectronico>();
                    DAOAsignacionComprobanteElectronico dao_ace = null;
                    try {
                        dao_ace = new DAOAsignacionComprobanteElectronico();
                        comprobantesElectronicos = dao_ace.obtenerComprobantesElectronicosAutorizadosRecibidosPorRUC(ms.obtenerRUCEmpresaSesionActiva(), first, pageSize);
                        this.setRowCount(dao_ace.obtenerTotalComprobantesElectronicosAutorizadosRecibidosPorRUC(ms.obtenerRUCEmpresaSesionActiva()).intValue());
                    } catch (Exception ex) {
                        Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return comprobantesElectronicos;
                }

                @Override
                public void setRowIndex(int rowIndex
                ) {
                    if (rowIndex == -1 || getPageSize() == 0) {
                        super.setRowIndex(-1);
                    } else {
                        super.setRowIndex(rowIndex % getPageSize());
                    }
                }

                public Object getRowKey(AsignacionComprobanteElectronico ace) {
                    return ace != null ? ace.getIdAsignacionComprobanteElectronico() : null;
                }

                @Override
                public AsignacionComprobanteElectronico getRowData(String rowKey) {
                    List<AsignacionComprobanteElectronico> aces = (List<AsignacionComprobanteElectronico>) getWrappedData();
                    Integer value = Integer.valueOf(rowKey);

                    for (AsignacionComprobanteElectronico ace : aces) {
                        if (ace.getIdAsignacionComprobanteElectronico().equals(value)) {
                            return ace;
                        }
                    }

                    return null;
                }
            };
        }
    }

    public void abrirDialogRangoFechas() {
        if (seleccionPeriodoTiempo != null) {
            if (seleccionPeriodoTiempo.equals("6")) {
                fechaInicial = new Date();
                fechaFinal = new Date();
                RequestContext.getCurrentInstance().execute("PF('wv-periodo-fechas').show();");
            } else {
                seleccionPeriodoTiempo = "1";
            }
        }
    }

    public String fechaFormateada(Date _fecha) {
        if (_fecha == null) {
            _fecha = new Date();
        }
        return Utilidades.obtenerFechaFormatoddMMyyyy(_fecha);
    }

    public void cerrarDialogRangoFechas() {

        RequestContext.getCurrentInstance().execute("PF('wv-periodo-fechas').hide();");

    }

    public void bajarZIP() {
        if (comprobantesElectronicosSeleccionados != null && !comprobantesElectronicosSeleccionados.isEmpty()) {
            Descarga d = new Descarga();
            String nombre_archivo = "Facturacion-Comprobantes-Recibidos-zip" + Utilidades.obtenerHHmmss(new Date());
            byte[] zipBytes = d.generarZIP(nombre_archivo, comprobantesElectronicosSeleccionados);
            if (zipBytes != null) {
                FacesContext faces = FacesContext.getCurrentInstance();

                HttpServletResponse response = d.descargarArchivo(faces, zipBytes, nombre_archivo + ".zip");
                faces.responseComplete();
            } else {
                Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, "");
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al bajar el archivo comprimido.");
            }

        } else {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Debe seleccionar al menos un comprobante.");
        }

    }

    public void descargarRIDE() {
        if (comprobanteElectronicoSeleccionado != null) {

            FacesContext faces = FacesContext.getCurrentInstance();

            try {
                RIDE descarga = new RIDE();
                byte[] ride = descarga.construirPDFRIDE(comprobanteElectronicoSeleccionado.getComprobanteElectronico());
                HttpServletResponse response = descarga.visualizarRIDE(faces, ride, comprobanteElectronicoSeleccionado.getComprobanteElectronico());
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al descargar el PDF-RIDE.");
            }

        } else {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
        }

    }

    public void descargarRespuestaXML() {
        if (comprobanteElectronicoSeleccionado != null) {

            FacesContext faces = FacesContext.getCurrentInstance();

            try {
                RIDE descarga = new RIDE();
                HttpServletResponse response = descarga.bajarArchivoRespuesta(faces, comprobanteElectronicoSeleccionado.getComprobanteElectronico().getArchivoRespuestaSricomprobanteElectronico(), comprobanteElectronicoSeleccionado.getComprobanteElectronico().getClaveAccesoComprobanteElectronico().concat(".xml"));
                faces.responseComplete();
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
                MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "Error al descargar el XML.");
            }

        } else {
            MensajesPrimefaces.mostrarMensaje(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
        }

    }

    public void buscarComprobantesRecibidosAutorizados() {
        this.obtenerFechas();
        ambiente = new ArrayList<String>();
        ambiente.add(Valores.AMBIENTE);
        estado = "1";
        ControlSesion ms = new ControlSesion();
        if (ms.obtenerEstadoSesionUsuario() == true) {
            comprobantesRecibidosLazy = new LazyDataModel() {
                @Override
                public List load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
                    ControlSesion ms = new ControlSesion();
                    List<AsignacionComprobanteElectronico> comprobantesElectronicos = new ArrayList<AsignacionComprobanteElectronico>();
                    DAOAsignacionComprobanteElectronico dao_ace = null;
                    try {
                        dao_ace = new DAOAsignacionComprobanteElectronico();
                        comprobantesElectronicos = dao_ace.buscarComprobantesVariosParametros(ambiente, secuencialBusqueda, null, ms.obtenerRUCEmpresaSesionActiva(), fechaSeleccionadaInicio, fechaActual, estado, first, pageSize, tipoDocumento);
                        this.setRowCount(dao_ace.obtenerTotalComprobantesVariosParametros(ambiente, secuencialBusqueda, null, ms.obtenerRUCEmpresaSesionActiva(), fechaSeleccionadaInicio, fechaActual, estado, tipoDocumento).intValue());
                    } catch (Exception ex) {
                        Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return comprobantesElectronicos;
                }

                @Override
                public void setRowIndex(int rowIndex
                ) {
                    if (rowIndex == -1 || getPageSize() == 0) {
                        super.setRowIndex(-1);
                    } else {
                        super.setRowIndex(rowIndex % getPageSize());
                    }
                }

                public Object getRowKey(AsignacionComprobanteElectronico ace) {
                    return ace != null ? ace.getIdAsignacionComprobanteElectronico() : null;
                }

                @Override
                public AsignacionComprobanteElectronico getRowData(String rowKey) {
                    List<AsignacionComprobanteElectronico> aces = (List<AsignacionComprobanteElectronico>) getWrappedData();
                    Integer value = Integer.valueOf(rowKey);

                    for (AsignacionComprobanteElectronico ace : aces) {
                        if (ace.getIdAsignacionComprobanteElectronico().equals(value)) {
                            return ace;
                        }
                    }

                    return null;
                }
            };
        }
    }

    private void obtenerFechas() {
        Calendar calendario = Calendar.getInstance();
        Calendar calendario_final_actual = Calendar.getInstance();
        fechaActual = calendario_final_actual.getTime();
        if (seleccionPeriodoTiempo != null && !seleccionPeriodoTiempo.equals("")) {

            if (seleccionPeriodoTiempo.equals("2")) {
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
            } else if (seleccionPeriodoTiempo.equals("3")) {
                int semana = calendario.get(Calendar.WEEK_OF_YEAR);
                semana--;
                calendario.set(Calendar.WEEK_OF_YEAR, semana);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
            } else if (seleccionPeriodoTiempo.equals("4")) {
                int mes = calendario.get(Calendar.MONTH);
                mes--;
                calendario.set(Calendar.MONTH, mes);
                calendario.set(Calendar.DAY_OF_MONTH, 1);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
                calendario_final_actual.set(Calendar.DAY_OF_MONTH, 1);
                calendario_final_actual.add(Calendar.DAY_OF_MONTH, -1);
                fechaActual = calendario_final_actual.getTime();

            } else if (seleccionPeriodoTiempo.equals("5")) {
                int mes = calendario.get(Calendar.MONTH);
                mes = mes - 6;
                calendario.set(Calendar.MONTH, mes);
                calendario.set(Calendar.DAY_OF_MONTH, 1);
                calendario.set(Calendar.HOUR_OF_DAY, 0);
                calendario.set(Calendar.MINUTE, 0);
                calendario.set(Calendar.SECOND, 0);
                fechaSeleccionadaInicio = calendario.getTime();
            } else if (seleccionPeriodoTiempo.equals("6")) {

                fechaSeleccionadaInicio = fechaInicial;
                fechaActual = fechaFinal;
            } else {
                fechaSeleccionadaInicio = null;
                fechaActual = null;
            }
        }

    }

    public void reiniciarParametros() {

        secuencialBusqueda = null;
        seleccionPeriodoTiempo = null;
        fechaSeleccionadaInicio = null;
        fechaActual = null;
        tipoDocumento = "-1";
        cargarTablaComprobantesRecibidosAutorizados();
    }

}
