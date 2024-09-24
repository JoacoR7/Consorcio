package com.consorcio.controller;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@ManagedBean
@ViewScoped
public class ControllerMail {

    @Resource(name = "mail/gmail")
    private Session mailSession;

    public void send() {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress("consorciouncode@gmail.com")); // Cambia por tu dirección de correo
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("joaquin.ruiz2012@gmail.com"));
            message.setSubject("Hola");
            message.setText("Hola");

            Transport.send(message);
            System.out.println("Email enviado exitosamente");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
