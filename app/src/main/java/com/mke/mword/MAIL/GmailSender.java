package com.mke.mword.MAIL;

import java.io.*;
import java.security.Security;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.Message;
import javax.mail.internet.*;

/**
 * Created by Kanan on 3/8/2017.
 */

public class GmailSender extends javax.mail.Authenticator {

    private String user;
    private String password;
    private Session session;

    MimeMessage message = null;
    BodyPart messageBodyPart = null;
    Multipart multipart = null;


    static {
        Security.addProvider(new JSSEProvider());
    }

    public GmailSender(String user, String password) {
        this.user = user;
        this.password = password;

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");
        session = Session.getDefaultInstance(props, this);

        // Create a default MimeMessage object.
        message = new MimeMessage(session);
        // Create the message part
        messageBodyPart = new MimeBodyPart();
        // Create a multipar message
        multipart = new MimeMultipart();
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);
    }

    public void addFile(String szFileName){
        try
        {
            File file = new File(szFileName);
            if (file.exists()){
                // Fill the message
                messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText(szFileName);

                    multipart.addBodyPart(messageBodyPart);

                // Part second is attachment
                messageBodyPart = new MimeBodyPart();
                DataSource source1 = new FileDataSource(szFileName);
                messageBodyPart.setDataHandler(new DataHandler(source1));
                messageBodyPart.setFileName(szFileName);
                multipart.addBodyPart(messageBodyPart);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private void addContent(String szContent){
        try {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(szContent);
            multipart.addBodyPart(messageBodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public synchronized void sendMail(String subject, String[] FileName,
                                      String from, String to,String szContent) throws Exception {
        try {
            if (from.isEmpty() || to.isEmpty()){
//                Log.i("ERROR", "From or To is Empty");
                return;
            }

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            if (!szContent.isEmpty()){
                addContent(szContent);
            }
            // Fill the message
            if (FileName != null && FileName.length >0) {
                for(int n=0; n< FileName.length ;n++) {
                    addFile(FileName[n]);
                }
            }

            MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");

            // Send the complete message parts
            message.setContent(multipart);
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;

        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }


}
