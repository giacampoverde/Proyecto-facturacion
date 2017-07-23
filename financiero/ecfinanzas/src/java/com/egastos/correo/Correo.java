/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egastos.correo;
import com.egastos.utilidades.MetodosDeUtilidad;
import com.egastos.utilidades.Valores;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Correo {

    protected MimeMessage message;
    protected String host;
    protected String port;
    protected String user;
    protected String password;
    protected String dirFrom;
    protected StringBuilder mensaje;
    protected Properties props;
    protected String asunto;
    protected Session session;

    public Correo(String identificador) {
        if (identificador != null) {
            if (identificador.equals("1")) {
                this.inicializarValoresNotificacion();
            }
        }
    }

    private void contenidoCorreo(String _asunto, String _contenido) {
        asunto = _asunto;

        aniadirMensaje(_contenido);
    }

    private void contenidoCorreoGenerico(String _asunto) {
        asunto = _asunto;

    }

    private void setPropiedades(String _host, String _port, String _user) {
        props = new Properties();
        // Se debe crear un archivo de propiedades
        props.put("mail.host", _host);
        // TLS si está disponible
        props.setProperty("mail.smtp.starttls.enable", "false");
        // Puerto de gmail para envio de correos
        props.setProperty("mail.smtp.port", _port);
        // Nombre del usuario
        props.setProperty("mail.smtp.user", _user);
        // Si requiere o no usuario y password para conectarse.
        props.setProperty("mail.smtp.auth", "true");
        //si requiere o no autentificacion
        props.setProperty("mail.smtp.ssl.enable", "false");
        //Forma de conectarse al sevidor de correo
        props.setProperty("mail.session.mail.smtp.auth.mechanisms", "LOGIN");
        props.setProperty("mail.session.mail.smtp.auth.plain.disable", "false");
        //Se indica si la conexion nesesita tls
        props.setProperty("mail.session.mail.smtp.starttls.enable", "false");
    }

    private void setDestinarioCopiaOculta(String _destinario) {
        try {
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(_destinario));
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    private void aniadirListaDestinatarios(List<String> _listaDestinarios) {
        for (int i = 0; i < _listaDestinarios.size(); i++) {
            if (i == 0) {
                setDestinarioPrincipal(_listaDestinarios.get(i));
            } else {
                setDestinarioCopiaOculta(_listaDestinarios.get(i));
            }

        }
    }

    public void inicializarValoresNotificacion() {

////        host = Valores.VALOR_HOST_CORREO_NOTIFICACION;
        host = "mail.ecfinanzas.com";
        password = "Ecu@dor2012";
        port = "587";
        user = "soporte@ecfinanzas.com";
//        port = Valores.VALOR_PUERTO_CORREO_NOTIFICACION;

//        user = Valores.VALOR_USUARIO_CORREO_NOTIFICACION;
       

//        password = Valores.VALOR_PASSWORD_CORREO_NOTIFICACION;

        dirFrom = Valores.VALOR_CUENTA_CORREO_NOTIFICACION;

        setPropiedades(host, port, user);
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
        message = new MimeMessage(session);
        mensaje = new StringBuilder();
    }

    private void aniadirDestinatario(String _destinatarioCorrero) {
        setDestinarioPrincipal(_destinatarioCorrero);
    }

    private void aniadirMensaje(String _mensaje) {
        mensaje.append(_mensaje);
        mensaje.append("\n");

    }

    private void aniadirMensajeGenerico(String _mensaje) {

        mensaje.append(_mensaje);

    }

    private void setDestinarioPrincipal(String _destinario) {
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(_destinario));
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    public String getMensaje() {
        String menj = mensaje.toString();
        return menj;
    }

    public MimeBodyPart agregarImagen(String direccion, String tag) throws IOException, MessagingException {
        MimeBodyPart imagen = new MimeBodyPart();
        imagen.attachFile(direccion);
        imagen.setHeader("Content-ID", "<" + tag + ">");
        return imagen;
    }

    public void enviarCorreoRegistro(String mail,List<String> _contenido) {
        try {
            List<String> imagenes = new ArrayList<String>();
            imagenes.add("cab");
            imagenes.add("pie");
            this.enviarMailRegistro(mail,_contenido, Valores.VALOR_PLANTILLA_HTML_CORREO + File.separator + "registroUsuario.html", Valores.VALOR_PLANTILLA_IMAGENES_CORREO + File.separator, "Registro Exitoso", imagenes);
        } catch (IOException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
     public boolean enviarMailReguistroecFinanzas(String _direccionDestinatario, List<String> _contenido, String pathHtml, String dirImagenes, String asunto, List<String> imagenes) throws IOException {
        boolean bandera = false;
        this.aniadirDestinatario(_direccionDestinatario);
        String fichero = "";
        String linea;
        BufferedReader br = new BufferedReader(new FileReader(pathHtml));
        if (br != null && _contenido != null && !_contenido.isEmpty()) {
            bandera = true;
        }
        while ((linea = br.readLine()) != null) {

            if (linea.contains("nombre")) {
                fichero += _contenido.get(0);
            } else if (linea.contains("contrasena")) {
                fichero += _contenido.get(1);
            } else {
                fichero += linea;
            }
        }
        br.close();
        this.contenidoCorreo(asunto, fichero);
        this.enviarCorreo(dirImagenes, imagenes);
        return bandera;
    }

    public boolean enviarMailRegistro(String _direccionDestinatario,List<String> _contenido, String pathHtml, String dirImagenes, String asunto, List<String> imagenes) throws IOException {
        this.aniadirDestinatario(_direccionDestinatario);
        String fichero = "";
        String linea;
        String dominio = MetodosDeUtilidad.obtenerDominiourl();
        BufferedReader br = new BufferedReader(new FileReader(pathHtml));
        while ((linea = br.readLine()) != null) {
             if (linea.contains("nombre")) {
                fichero += _contenido.get(0);
            } else if (linea.contains("contrasena")) {
                fichero += _contenido.get(1);
            } else if(linea.contains("redireccion")){
              fichero += "Si desea ingresar <a href='" + dominio + "'>Clic Aqui</a><br/><br/>Si la url no funciona por favor copie la siguiente url en la barra de navegación :<br/>" + dominio;;
            }else{
              fichero += linea;  
            }
        }
        br.close();
        this.contenidoCorreo(asunto, fichero);
        this.enviarCorreo(dirImagenes, imagenes);
        return true;
    }

    private void enviarCorreo(String direccion, List<String> imagenes) {
        try {

            message.setFrom(new InternetAddress(this.dirFrom));
            message.setSubject(asunto);
            Multipart multipart = new MimeMultipart("related");
            BodyPart texto = new MimeBodyPart();
            texto.setContent(getMensaje(), "text/html;charset=UTF-8");

            multipart.addBodyPart(texto);
            for (int i = 0; i < imagenes.size(); i++) {
                multipart.addBodyPart(agregarImagen(direccion + imagenes.get(i) + ".png", imagenes.get(i)));
            }
            message.setContent(multipart);
            Transport t = session.getTransport("smtp");
            t.connect(user, password);
            t.sendMessage(message, message.getAllRecipients());
            t.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean enviarMailRecuperacionClave(String _direccionDestinatario, List<String> _contenido, String pathHtml, String dirImagenes, String asunto, List<String> imagenes) throws IOException {
        boolean bandera = false;
        this.aniadirDestinatario(_direccionDestinatario);
        String fichero = "";
        String linea;
        BufferedReader br = new BufferedReader(new FileReader(pathHtml));
        if (br != null && _contenido != null && !_contenido.isEmpty()) {
            bandera = true;
        }
        while ((linea = br.readLine()) != null) {

            if (linea.contains("nombre")) {
                fichero += _contenido.get(0);
            } else if (linea.contains("redireccion")) {
                fichero += _contenido.get(1);
            } else {
                fichero += linea;
            }
        }
        br.close();
        this.contenidoCorreo(asunto, fichero);
        this.enviarCorreo(dirImagenes, imagenes);
        return bandera;
    }

    public boolean enviarMailReguistroCheca(String _direccionDestinatario, List<String> _contenido, String pathHtml, String dirImagenes, String asunto, List<String> imagenes) throws IOException {
        boolean bandera = false;
        this.aniadirDestinatario(_direccionDestinatario);
        String fichero = "";
        String linea;
        BufferedReader br = new BufferedReader(new FileReader(pathHtml));
        if (br != null && _contenido != null && !_contenido.isEmpty()) {
            bandera = true;
        }
        while ((linea = br.readLine()) != null) {

            if (linea.contains("nombre")) {
                fichero += _contenido.get(0);
            } else if (linea.contains("contrasena")) {
                fichero += _contenido.get(1);
            } else {
                fichero += linea;
            }
        }
        br.close();
        this.contenidoCorreo(asunto, fichero);
        this.enviarCorreo(dirImagenes, imagenes);
        return bandera;
    }

    public boolean enviarMailPruebaCorreo(List<String> _direccionDestinatario, String _contenido, String _asunto) {
        boolean enviado = false;
        try {
           
            this.aniadirListaDestinatarios(_direccionDestinatario);
            this.contenidoCorreoGenerico(_asunto);
            this.aniadirMensajeGenerico(_contenido);
            enviado = this.enviarCorreoGenerico();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enviado;
    }

    public boolean enviarMailAsistencia(List<String> _direccionDestinatario, String _contenido, String _asunto) {
        boolean enviado = false;
        try {
            this.aniadirListaDestinatarios(_direccionDestinatario);
            this.contenidoCorreoGenerico(_asunto);
            this.aniadirMensajeGenerico(_contenido);
            enviado = this.enviarCorreoGenerico();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enviado;
    }

    /**
     *
     * @param _direccionXMLRespuesta
     * @param _direccionPDFRIDE
     * @param _direccionDestinatarios
     * @param _contenido
     * @param _rutaPlantillaHTML
     * @param _direccionImagenes
     * @param _asunto
     * @param _imagenes
     * @return
     */
    public boolean enviarNotificacionAutorizacionComprobante(String _direccionXMLRespuesta, String _direccionPDFRIDE, List<String> _direccionDestinatarios, List<String> _contenido, String _asunto, String _rutaPlantillaHTML, String _direccionImagenes, List<String> _imagenes) {
        boolean existe_plantilla = false;
        boolean leyo_plantilla = false;
        this.aniadirListaDestinatarios(_direccionDestinatarios);
        String fichero = "";
        String linea;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(_rutaPlantillaHTML));
            existe_plantilla = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            while ((linea = br.readLine()) != null) {
                if (linea.contains("estadocomprobante")) {
                    fichero += _contenido.get(0);
                } else if (linea.contains("nombre")) {
                    fichero += _contenido.get(1);
                } else if (linea.contains("numerofactura")) {
                    fichero += _contenido.get(2);
                } else if (linea.contains("tipodocumento")) {
                    fichero += _contenido.get(3);
                } else if (linea.contains("clacceso")) {
                    fichero += _contenido.get(4);
                } else {
                    fichero += linea;
                }
            }
            br.close();

        } catch (IOException ex) {
            Logger.getLogger(Correo.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.contenidoCorreo(_asunto, fichero);
        boolean enviado = this.enviarCorreoConPlantillaPDFYXML(_direccionXMLRespuesta, _direccionPDFRIDE, _direccionImagenes, _imagenes);
        return existe_plantilla && enviado;
    }

    /**
     * Metodo para enviar correo con platilla y con XML adjunto
     *
     * @param direccionImagenes
     * @param _direccionPDFRIDE
     * @param _direccionXMLRespuesta
     * @param imagenes
     */
    public boolean enviarCorreoConPlantillaPDFYXML(String _direccionXMLRespuesta, String _direccionPDFRIDE, String direccionImagenes, List<String> imagenes) {
        boolean enviado = false;
        try {
//            message.setFrom(new InternetAddress("facturaelectronica@ebox.ec"));
            message.setFrom(new InternetAddress(this.dirFrom));
            Multipart multipart = new MimeMultipart("related");
            BodyPart texto = new MimeBodyPart();
            texto.setContent(getMensaje(), "text/html;charset=UTF-8");
            multipart.addBodyPart(texto);
            for (int i = 0; i < imagenes.size(); i++) {
                multipart.addBodyPart(agregarImagen(direccionImagenes + imagenes.get(i) + ".jpg", imagenes.get(i)));
            }
            BodyPart adjunto = null;
            File fileComprobante = null;
            if (_direccionXMLRespuesta != null) {
                adjunto = new MimeBodyPart();
                fileComprobante = new File(_direccionXMLRespuesta);
                adjunto.setDataHandler(new DataHandler(new FileDataSource(fileComprobante)));
                adjunto.setFileName(fileComprobante.getName());
                multipart.addBodyPart(adjunto);
            }
            if (_direccionPDFRIDE != null) {
                adjunto = new MimeBodyPart();
                fileComprobante = new File(_direccionPDFRIDE);
                adjunto.setDataHandler(new DataHandler(new FileDataSource(fileComprobante)));
                adjunto.setFileName(fileComprobante.getName());
                multipart.addBodyPart(adjunto);
            }
            message.setContent(multipart);
            Transport t = session.getTransport("smtp");
            t.connect(user, password);
            message.setSubject(asunto);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            enviado = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return enviado;
    }

    private boolean enviarCorreoGenerico() {
        boolean enviado = false;
        try {
//            message.setFrom(new InternetAddress(this.dirFrom));
            Multipart multipart = new MimeMultipart("related");
            BodyPart texto = new MimeBodyPart();
            texto.setContent(getMensaje(), "text/html;charset=UTF-8");
            multipart.addBodyPart(texto);
            message.setContent(multipart);
            Transport t = session.getTransport("smtp");
            t.connect(user, password);
            message.setSubject(asunto);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
            enviado = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return enviado;
    }

}
