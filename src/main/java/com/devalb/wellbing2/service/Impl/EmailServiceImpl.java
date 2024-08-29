package com.devalb.wellbing2.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
        mailSender.send(mensaje);
    }

    @Override
    public void sendEmail(String to, String subject, String content) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(content);
        mailSender.send(email);
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        mailSender.send(message);
    }
}