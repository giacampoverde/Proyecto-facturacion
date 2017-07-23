/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.vistas.ec;

import com.egastos.dao.ec.DAOAsignacionComprobanteElectronico;
import com.egastos.dao.ec.DAOAuditoria;
import com.egastos.dao.ec.DAOComprobanteElectronico;
import com.egastos.dao.ec.DAOReceptor;
import com.egastos.dao.ec.DAOTipoComprobanteElectronico;
import com.egastos.dao.ec.DAOUsuarioAcceso;
import com.egastos.modelo.ec.AsignacionComprobanteElectronico;
import com.egastos.modelo.ec.Auditoria;
import com.egastos.modelo.ec.ComprobanteElectronico;
import com.egastos.modelo.ec.Receptor;
import com.egastos.modelo.ec.UsuarioAcceso;
import com.egastos.servicios.ec.AlmacenamientoComprobanteElectronico;
import com.egastos.utilidades.ControlSesion;
import com.egastos.utilidades.Descarga;
import com.egastos.utilidades.RIDE;
import com.egastos.utilidades.Utilidades;
import com.egastos.utilidades.Valores;
import com.egastos.utilidades.MensajesPrimefaces;
import com.egastos.utilidades.TransformadorArchivos;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.Session;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.UploadedFile;
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
    private String fechaInicialS;
    private String fechaFinalS;
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
    private String clave;
    private boolean apareceRago = false;
    private boolean apareceValores = false;
    private String nombreEmpresa;
    private String valorInicial;
    private String valorFinal;
    private String opcionValores;
    private Integer ano;
    private Integer mes;
    private Integer dia;
    private String rucempresa;
    private String detalleFactua;
    private Double valorfactura;
    private String nombreProvedor;
    private String seccion;
    private UploadedFile file;
    private UploadedFile txtfile;
    private boolean apareceDialog;
    private boolean mensaje90;

    public boolean isMensaje90() {
        return mensaje90;
    }

    public void setMensaje90(boolean mensaje90) {
        this.mensaje90 = mensaje90;
    }

    public boolean isApareceDialog() {
        return apareceDialog;
    }

    public void setApareceDialog(boolean apareceDialog) {
        this.apareceDialog = apareceDialog;
    }

    public UploadedFile getTxtfile() {
        return txtfile;
    }

    public void setTxtfile(UploadedFile txtfile) {
        this.txtfile = txtfile;
    }

    
    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public String getNombreProvedor() {
        return nombreProvedor;
    }

    public void setNombreProvedor(String nombreProvedor) {
        this.nombreProvedor = nombreProvedor;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

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

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public String getRucempresa() {
        return rucempresa;
    }

    public void setRucempresa(String rucempresa) {
        this.rucempresa = rucempresa;
    }

    public String getDetalleFactua() {
        return detalleFactua;
    }

    public void setDetalleFactua(String detalleFactua) {
        this.detalleFactua = detalleFactua;
    }

    public Double getValorfactura() {
        return valorfactura;
    }

    public void setValorfactura(Double valorfactura) {
        this.valorfactura = valorfactura;
    }

    /**
     * Creates a new instance of BeanComprobantesRecibidos
     */
    public BeanComprobantesRecibidos() {
        ControlSesion ms = new ControlSesion();
            if (!ms.obtenerEstadoSesionUsuario()) {

                BeanDireccionamiento nosesion=new BeanDireccionamiento();
                nosesion.direccionarLogin();
            }
        apareceDialog=false;
        try {
            seccion = "0";
            ControlSesion sesion = new ControlSesion();
            
            DAOUsuarioAcceso usuarioda=new DAOUsuarioAcceso();
            UsuarioAcceso us=usuarioda.obtenerUsuarioAccesoPorId(Integer.parseInt(sesion.obtenerIdUsuarioSesionActiva()));
            if(us!=null){
             if(us.getMensajeOpcional().equals("1")){
                apareceDialog=true;
            }
            }
//            guardarLogRegistros("Acceso al modulo  Comprobantes Recibidos");
            cargarTablaComprobantesRecibidosAutorizados();
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     public void actualzarCerrar(){
        try {
            if(mensaje90){
            ControlSesion control=new ControlSesion();
            control.obtenerIdUsuarioSesionActiva();
            DAOUsuarioAcceso usuarioAc=new DAOUsuarioAcceso();
            usuarioAc.actualizarEstaMensaje(Integer.parseInt(control.obtenerIdUsuarioSesionActiva()),"2");
            apareceDialog=false;
            RequestContext.getCurrentInstance().execute("PF('dlg2').hide();");
            }else{
            RequestContext.getCurrentInstance().execute("PF('dlg2').hide();");
            }
            
//          MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "MOSTRAR MENSAJE DE QUE SE DEBE ALMACENAR ANTES DE CUMPLIR 3 MESES .");
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarMensaje(){
        if(apareceDialog){
        RequestContext.getCurrentInstance().execute("PF('dlg2').show();");
        }
//          MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "MOSTRAR MENSAJE DE QUE SE DEBE ALMACENAR ANTES DE CUMPLIR 3 MESES .");
    }
    
//@PostConstruct
//    public void init() {
//    
//         
//      
//            RequestContext.getCurrentInstance().execute("alert('This onload script is added from backing bean.')");
//        
//    }
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
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void guardarFacturaManual() {
        if (!seccion.equals("0")) {
            if (mes >= 1 && mes <= 12) {
                if (dia >= 1 && dia <= 31) {
                    if (!rucempresa.equals("")) {
                        if (valorfactura > 0) {
                            if (!detalleFactua.equals("")) {
                                if (detalleFactua.length() >= 12) {
                                    if (!nombreProvedor.equals("")) {
                                        DAOComprobanteElectronico a = null;
                                        DAOAsignacionComprobanteElectronico b = null;
                                        try {
                                            a = new DAOComprobanteElectronico();
                                        } catch (Exception ex) {
                                            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        try {
                                            b = new DAOAsignacionComprobanteElectronico();
                                            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
                                            String strFecha = ano + "-" + mes + "-" + dia;
                                            Date fecha = null;
                                            try {

                                                fecha = formatoDelTexto.parse(strFecha);

                                            } catch (ParseException ex) {

                                                ex.printStackTrace();

                                            }
                                            ControlSesion ms = new ControlSesion();
                                            DAOTipoComprobanteElectronico daotipo = new DAOTipoComprobanteElectronico();
                                            ComprobanteElectronico respuestaguarado = a.guardarComprobanteElectronico("", "", "", "", "", nombreProvedor, nombreProvedor, rucempresa, "1", "1", detalleFactua, fecha, new Date(), "" + valorfactura, daotipo.obtenerTipoComprobanteElectronicoPorCodigo("01"), null, null, seccion);
                                            DAOReceptor recep = new DAOReceptor();
                                            AsignacionComprobanteElectronico respu = b.guardarAsignacionComprobanteElectronico(respuestaguarado, recep.obtenerReceptorPorIdentificacion(ms.obtenerRUCEmpresaSesionActiva()));
                                            if (respu != null) {
                                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Almacenado Correctamente.");
                                                nombreProvedor = "";
                                                rucempresa = "";
                                                valorfactura = 0.0;
                                                seccion = "0";
                                                detalleFactua = "";
                                                ano = 0;
                                                mes = 0;
                                                dia = 0;
                                            } else {
                                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo Almacenado .");
                                            }
                                        } catch (Exception ex) {
                                            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                    } else {
                                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ingrese el nombre de proveedor.");
                                    }
                                } else {
                                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El detalle debe contener minimo 12 caracteres.");
                                }

                            } else {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ingrese el detalle de la factura.");
                            }
                        } else {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El valor de la factura debe ser mayor a cero.");
                        }

                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El ruc de la empresa no puede estar vacio.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El dia no puede ser mayor a 31 ni menor a 0.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El mes no peude ser mayor a 12 ni menor a 0.");
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor seleccione una sección para el  comprobante.");
        }
    }

    public void cargarporClave() {
        if (!seccion.equals("0")) {
            boolean respuestaAlprocesar = false;
            try {
                DAOComprobanteElectronico daocomrpobante = new DAOComprobanteElectronico();
                if (!clave.equals("")) {
                    if (clave.length() == 49) {
                        ComprobanteElectronico respuestacomprobante = daocomrpobante.obtenerComprobatePorCA(clave);
                        if (respuestacomprobante != null) {
                            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "El comprobante ya se ecuentra almacenado.");
                        } else {
                            respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(clave, seccion);
                            if (respuestaAlprocesar) {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Almacenado correctamente.");
                                clave = "";
                            } else {
                                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Sri no responde ,le recordamos que sus facturas solo pueden ser almacenas hasta 90 dias despues de su emisión");
                            }
                        }
                    } else {
                        MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "La clave de acceso debe contener 49 digitos.");
                    }
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Ingrese una clave de acceso.");
                }
            } catch (Exception ex) {
                Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor seleccione una sección para el comprobante.");
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
                        respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveAcceso, seccion);

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
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Archivo Procesado exitosamente.");
                } else {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se proceso todo el contenido del txt.");
                }
            } else {
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "El Archivo esta vacio.");
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
    public void cargaxml()  {
        if (!seccion.equals("0")) {
                if(!file.getFileName().equals("")){
                    if(file.getFileName().substring((file.getFileName().length()-3),file.getFileName().length()).equals("xml")
                      ||file.getFileName().substring((file.getFileName().length()-3),file.getFileName().length()).equals("XML")
                      ||file.getFileName().substring((file.getFileName().length()-3),file.getFileName().length()).equals("Xml")){
            try {
                txtContactos = new File(file.getFileName());
                FileOutputStream fos = new FileOutputStream(txtContactos);
                fos.write(file.getContents());
                fos.flush();
                fos.close();
                lecturaxml(file.getContents());
                 file=new DefaultUploadedFile();
            } catch (IOException ex) {
            }
                    }else{
                         file=new DefaultUploadedFile();
           MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Formato de archivo incorrecto");    
                    }
            
        } else {
                     file=new DefaultUploadedFile();
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha cargador un archivo xml.");
        }
                   } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor seleccione una sección para el comprobante.");
        }
    }
    public void realizaAccion(){
        System.out.println(seccion);
    }
    public void cargaxml2(FileUploadEvent a)  {
        if (!seccion.equals("0")) {
                if(!a.getFile().getFileName().equals("")){
                    if(a.getFile().getFileName().substring((a.getFile().getFileName().length()-3),a.getFile().getFileName().length()).equals("xml")
                      ||a.getFile().getFileName().substring((a.getFile().getFileName().length()-3),a.getFile().getFileName().length()).equals("XML")
                      ||a.getFile().getFileName().substring((a.getFile().getFileName().length()-3),a.getFile().getFileName().length()).equals("Xml")){
            try {
                txtContactos = new File(a.getFile().getFileName());
                FileOutputStream fos = new FileOutputStream(txtContactos);
                fos.write(a.getFile().getContents());
                fos.flush();
                fos.close();
                lecturaxml(a.getFile().getContents());
//                 file=new DefaultUploadedFile();
            } catch (IOException ex) {
            }
                    }else{
                         file=new DefaultUploadedFile();
           MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Formato de archivo incorrecto");    
                    }
            
        } else {
                     file=new DefaultUploadedFile();
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha cargador un archivo xml.");
        }
   } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor seleccione una sección para el comprobante.");
        }
    }
    
    
            public void cargatxtclavesAcceso2(FileUploadEvent a)  {
       if (!seccion.equals("0")) {
 if(!a.getFile().getFileName().equals("")){
     if(a.getFile().getFileName().substring((a.getFile().getFileName().length()-3),a.getFile().getFileName().length()).equals("txt")
                      ||a.getFile().getFileName().substring((a.getFile().getFileName().length()-3),a.getFile().getFileName().length()).equals("TXT")
                      ||a.getFile().getFileName().substring((a.getFile().getFileName().length()-3),a.getFile().getFileName().length()).equals("Txt")){
            try {
                
                txtContactos = new File(a.getFile().getFileName());
                FileOutputStream fos = new FileOutputStream(txtContactos);
                fos.write(a.getFile().getContents());
                fos.flush();
                fos.close();
                lecturaArchivoTxt();
//                a.getFile()=new DefaultUploadedFile();
            } catch (IOException ex) {
            }
             }else{
           MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Formato de archivo incorrecto"); 
            txtfile=new DefaultUploadedFile();
                    }
            
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha cargador un archivo txt.");
             txtfile=new DefaultUploadedFile();
        }
   } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Por favor seleccione una sección para el comprobante.");
        }
    }
    
    
        public void cargatxtclavesAcceso()  {
//        if (!seccion.equals("0")) {
 if(!txtfile.getFileName().equals("")){
     if(txtfile.getFileName().substring((file.getFileName().length()-3),file.getFileName().length()).equals("txt")
                      ||txtfile.getFileName().substring((file.getFileName().length()-3),file.getFileName().length()).equals("TXT")
                      ||txtfile.getFileName().substring((file.getFileName().length()-3),file.getFileName().length()).equals("Txt")){
            try {
                
                txtContactos = new File(txtfile.getFileName());
                FileOutputStream fos = new FileOutputStream(txtContactos);
                fos.write(txtfile.getContents());
                fos.flush();
                fos.close();
                lecturaArchivoTxt();
                txtfile=new DefaultUploadedFile();
            } catch (IOException ex) {
            }
             }else{
           MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Formato de archivo incorrecto"); 
            txtfile=new DefaultUploadedFile();
                    }
            
        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No ha cargador un archivo txt.");
             txtfile=new DefaultUploadedFile();
        }

    }
    
    public void pruebaarchivar(FileUploadEvent event) throws FileNotFoundException, IOException{
              
         byte[] respu = TransformadorArchivos.byteCompr2(event.getFile().getContents(), Valores.VALOR_DIRECTORIO_CREACION_XMLS + RandomStringUtils.randomAlphanumeric(10).concat(".xml")); 
          txtContactos = new File("C:\\"+event.getFile().getFileName());
                FileOutputStream fos = new FileOutputStream(txtContactos);
                fos.write(respu);
                fos.flush();
                fos.close();
         
    }
    public void lecturaxml(byte[] evento) {
        String claveDeAcceso = "";
        boolean respuestaAlprocesar = false;
        claveDeAcceso = validarClaveAccesoNombre(txtContactos.getName());
        if (claveDeAcceso.equals("")) {
            claveDeAcceso = obtenerclaveDeAccesoXml(evento);
            if ( claveDeAcceso!=null&&!claveDeAcceso.equals("")) {
                respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveDeAcceso, seccion);
            }
        } else {
            respuestaAlprocesar = ProcesoAlmacenamientoClaveAcceso(claveDeAcceso, seccion);
        }
        if (respuestaAlprocesar) {
           
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Almacenado Correctamente.");
        } else {
            if (evento != null) {
                try {
//                    DAOAsignacionComprobanteElectronico b = new DAOAsignacionComprobanteElectronico();
//                     DAOComprobanteElectronico a = new DAOComprobanteElectronico();
//                    ControlSesion ms = new ControlSesion();
//                    DAOTipoComprobanteElectronico daotipo = new DAOTipoComprobanteElectronico();
////                    byte[] respu = TransformadorArchivos.byteCompr2(evento, Valores.VALOR_DIRECTORIO_CREACION_XMLS + RandomStringUtils.randomAlphanumeric(10).concat(".xml"));
//                     com.egastos.comprobanteelectronico.esquema.comprobantebase.ComprobanteElectronico c = TransformadorArchivos.byteCompr(evento, Valores.VALOR_DIRECTORIO_CREACION_XMLS + RandomStringUtils.randomAlphanumeric(10).concat(".xml"));
////                    ComprobanteElectronico respuestaguarado = a.guardarComprobanteElectronico("", "", "", "", "", nombreProvedor, nombreProvedor, rucempresa, "1", "1", detalleFactua, fecha, new Date(), "" + valorfactura, daotipo.obtenerTipoComprobanteElectronicoPorCodigo("01"), null, null, seccion);
////                    DAOReceptor recep = new DAOReceptor();
////                    AsignacionComprobanteElectronico respu = b.guardarAsignacionComprobanteElectronico(respuestaguarado, recep.obtenerReceptorPorIdentificacion(ms.obtenerRUCEmpresaSesionActiva()));
                } catch (Exception ex) {
                    Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
         MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Sri no responde ,le recordamos que sus facturas solo pueden ser almacenas hasta 90 dias despues de su emisión");
        }
    }

    public static boolean ProcesoAlmacenamientoClaveAcceso(String ClaveAcceso, String seccion) {
        boolean guardada = false;
        try {

            AlmacenamientoComprobanteElectronico ac = new AlmacenamientoComprobanteElectronico();
            guardada = ac.guardarPorClaveDeAcceso(ClaveAcceso, seccion);

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
        File archivo = new File("xmldescargar.xml");
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
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo leer el comprobante.");
        } catch (Exception ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se pudo leer el comprobante.");
        }

        try {

            DocumentBuilder dBuilder = null;
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xml = dBuilder.parse(archivo);

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
        } catch (SAXException ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
        } catch (IOException ex) {
            Logger.getLogger(BeanComprobantesRecibidos.class.getName()).log(Level.SEVERE, null, ex);
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "No se pudo leer el comprobante.");
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

                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_INFO, "Erro en el Documento.");
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

    public void abrirValores() {
        if (opcionValores.equals("1")) {
            apareceValores = false;
            valorInicial = "";
            valorFinal = "";
        }
        if (opcionValores.equals("2")) {
            apareceValores = false;
            valorInicial = "0";
            valorFinal = "100";

        }
        if (opcionValores.equals("4")) {
            apareceValores = false;
            valorInicial = "100";
            valorFinal = "500";
        }
        if (opcionValores.equals("5")) {
            apareceValores = false;
            valorInicial = "500";
            valorFinal = null;
        }
        if (opcionValores.equals("6")) {
            valorInicial = "0";
            valorFinal = "0";
            apareceValores = true;
        }
    }

    public void abrirDialogRangoFechas() {
        if (seleccionPeriodoTiempo != null) {
            if (seleccionPeriodoTiempo.equals("6")) {
                fechaInicial = new Date();
                fechaInicialS = fechaFormateada(fechaInicial);
                fechaFinal = new Date();
                fechaFinalS = fechaFormateada(fechaFinal);
                apareceRago = true;
            } else {
                seleccionPeriodoTiempo = "1";
                apareceRago = false;
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
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al bajar el archivo comprimido.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Debe seleccionar al menos un comprobante.");
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
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al descargar el PDF-RIDE.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
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
                MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error al descargar el XML.");
            }

        } else {
            MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "No se ha seleccionado un comprobante.");
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
                        comprobantesElectronicos = dao_ace.buscarComprobantesVariosParametros(ambiente, secuencialBusqueda, null, ms.obtenerRUCEmpresaSesionActiva(), fechaSeleccionadaInicio, fechaActual, estado, first, pageSize, tipoDocumento, nombreEmpresa, valorInicial, valorFinal);
                        this.setRowCount(dao_ace.obtenerTotalComprobantesVariosParametros(ambiente, secuencialBusqueda, null, ms.obtenerRUCEmpresaSesionActiva(), fechaSeleccionadaInicio, fechaActual, estado, tipoDocumento, nombreEmpresa, valorInicial, valorFinal).intValue());
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
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    fechaInicial = formatter.parse(fechaInicialS);
                } catch (ParseException ex) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error Fecha Inicial (dia/mes/anio).");
                }
                try {
                    fechaFinal = formatter.parse(fechaFinalS);
                } catch (ParseException ex) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error Fecha Final (dia/mes/anio).");
                }
                if (fechaInicial.after(fechaFinal)) {
                    MensajesPrimefaces.mostrarMensajeDialog(FacesMessage.SEVERITY_ERROR, "Error en rango de fechas (dia/mes/anio).");
                    return;
                }
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

    public String getFechaInicialS() {
        return fechaInicialS;
    }

    public void setFechaInicialS(String fechaInicialS) {
        this.fechaInicialS = fechaInicialS;
    }

    public String getFechaFinalS() {
        return fechaFinalS;
    }

    public void setFechaFinalS(String fechaFinalS) {
        this.fechaFinalS = fechaFinalS;
    }

    public boolean isApareceRago() {
        return apareceRago;
    }

    public void setApareceRago(boolean apareceRago) {
        this.apareceRago = apareceRago;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getValorInicial() {
        return valorInicial;
    }

    public void setValorInicial(String valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }

    public boolean isApareceValores() {
        return apareceValores;
    }

    public void setApareceValores(boolean apareceValores) {
        this.apareceValores = apareceValores;
    }

    public String getOpcionValores() {
        return opcionValores;
    }

    public void setOpcionValores(String opcionValores) {
        this.opcionValores = opcionValores;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

}
