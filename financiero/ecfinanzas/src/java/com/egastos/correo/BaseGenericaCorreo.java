/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.egastos.correo;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
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


public abstract class BaseGenericaCorreo {

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

    public BaseGenericaCorreo() {
        this.inicializarValores();
    }

    /**
     * Envia el correo con los datos ingresado y añade las imagenes por el
     * momento las imagens solo son cabecera y pie debe ser cambiado para
     * recoger una lista con las direcciones y el identificador que se encuentra
     * en la plantilla
     *
     * @param direccion direccion donde se encuentran las imagens a ingresarse
     * @param imagenes
     */
    protected void enviarCorreo(String direccion, List<String> imagenes) {
        try {

            message.setFrom(new InternetAddress("facturaelectronica@ebox.ec"));
            message.setSubject(asunto);
            Multipart multipart = new MimeMultipart("related");
            BodyPart texto = new MimeBodyPart();
            texto.setContent(getMensaje(), "text/html;charset=UTF-8");

            multipart.addBodyPart(texto);
            for (int i = 0; i < imagenes.size(); i++) {
                multipart.addBodyPart(agregarImagen(direccion + imagenes.get(i) + ".jpg", imagenes.get(i)));
            }
            message.setContent(multipart);
            Transport t = session.getTransport("smtp");
            t.connect(user, password);

            //System.out.println(message.getAllRecipients());
            t.sendMessage(message, message.getAllRecipients());
            t.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * retorna un MimeBodyPart que es el que se añade al correo antes de
     * enviarlo
     *
     * @param direccion String que indica en que lugar se encuentra la imagen
     * que sera ingresada, la direccion tiene que especificar tambien el nombre
     * del archivo.jpg
     * @param tag indica el tag que sera remplasado por el contenido de la
     * imagen este tag NO debe estar entre < >
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public MimeBodyPart agregarImagen(String direccion, String tag) throws IOException, MessagingException {
        MimeBodyPart imagen = new MimeBodyPart();
        imagen.attachFile(direccion);
        imagen.setHeader("Content-ID", "<" + tag + ">");
        return imagen;
    }

    /**
     * Inicializa los valores de todo el correo
     *
     * @param _host Direccion del servidor de correo
     * @param _port puerto por el que sale el mensaje
     * @param _user usuario de correo
     * @param _password clave del usuario
     * @param _dirCorreo correo del remitente
     */
    public void inicializarValores() {

        host = "mocha3012.mochahost.com";

        port = "587";

        user = "facturaelectronica@ebox.ec";

        password = "bigdata2014";

        dirFrom = "facturaelectronica@ebox.ec";

        setPropiedades(host, port, user);
        session = Session.getDefaultInstance(props);
        session.setDebug(false);
        message = new MimeMessage(session);
        mensaje = new StringBuilder();
    }

    /**
     * Inicializa los valores de las propiedades del correo
     *
     * @param _host Direccion del servidor de correo
     * @param _port puerto por el que sale el mensaje
     * @param _user usuario de correo
     */
    private void setPropiedades(String _host, String _port, String _user) {
        props = new Properties();
        // Se debe crear un archivo de propiedades
        props.put("mail.host", _host);
        // TLS si est? disponible
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

    /**
     * Incluye un destinatario a la lista de destinatarios
     *
     * @param _destinario direccion de correodel destinatario
     */
    protected void setDestinarioPrincipal(String _destinario) {
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(_destinario));
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Incluye un destinatario a la lista de destinatarios con copia oculta
     *
     * @param _destinario direccion de correodel destinatario
     */
    protected void setDestinarioCopiaOculta(String _destinario) {
        try {
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress(_destinario));
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retorna el mensaje acutal en forma de string
     *
     * @return Mensaje en forma de string
     */
    public String getMensaje() {
        String menj = mensaje.toString();
        return menj;
    }

    /**
     * Metodo para enviar correo con platilla y con XML adjunto
     *
     * @param direccionImagenes
     * @param _direccion_pdf
     * @param _direccion_archivos
     * @param imagenes
     */
    public boolean enviarCorreoAdjuntoPlantilla(String _direccion_archivos, String _direccion_pdf, String direccionImagenes, List<String> imagenes) {
        boolean enviado = false;
        try {
            message.setFrom(new InternetAddress("facturaelectronica@ebox.ec"));
            Multipart multipart = new MimeMultipart("related");
            BodyPart texto = new MimeBodyPart();
            texto.setContent(getMensaje(), "text/html;charset=UTF-8");
            multipart.addBodyPart(texto);
            for (int i = 0; i < imagenes.size(); i++) {
                multipart.addBodyPart(agregarImagen(direccionImagenes + imagenes.get(i) + ".jpg", imagenes.get(i)));
            }
            BodyPart adjunto = null;
            File fileComprobante = null;
            if (_direccion_archivos != null) {
                adjunto = new MimeBodyPart();
                fileComprobante = new File(_direccion_archivos);
                adjunto.setDataHandler(new DataHandler(new FileDataSource(fileComprobante)));
                adjunto.setFileName(fileComprobante.getName());
                multipart.addBodyPart(adjunto);
            }
            if (_direccion_pdf != null) {
                adjunto = new MimeBodyPart();
                fileComprobante = new File(_direccion_pdf);
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

    public boolean enviarCorreoGenerico() {
        boolean enviado = false;
        try {
            message.setFrom(new InternetAddress("facturaelectronica@ebox.ec"));
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

    public boolean enviarCorreoGenericoReenvio(MimeMessage asd) {
        boolean enviado = false;
        try {
//            message.setFrom(new InternetAddress("facturaelectronica@ebox.ec"));
//            Multipart multipart = new MimeMultipart("related");
//            BodyPart texto = new MimeBodyPart();
//            texto.setContent(getMensaje(), "text/html;charset=UTF-8");
//            multipart.addBodyPart(texto);
//            message.setContent(multipart);

            message.setContent(asd.getContent(), asd.getContentType());
            ;
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
