package com.devalb.wellbing2.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.devalb.wellbing2.entity.Email;
import com.devalb.wellbing2.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(Email email) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(email.getDestinatario());
        mensaje.setSubject(email.getAsunto());
        mensaje.setText(email.getMensaje());
        try {
            mailSender.send(mensaje);
        } catch (MailException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        try {
            mailSender.send(email);
        } catch (MailException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        try {
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println("Error al enviar el correo HTML: " + e.getMessage());
        }
    }

    @Override
    public void sendHtmlEmailMime(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true indica que el contenido es HTML
        mailSender.send(message);
    }
}