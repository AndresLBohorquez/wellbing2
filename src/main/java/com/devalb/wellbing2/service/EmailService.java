package com.devalb.wellbing2.service;

import com.devalb.wellbing2.entity.Email;

import jakarta.mail.MessagingException;

public interface EmailService {

    public void enviarEmail(Email email);

    public void sendEmail(String to, String subject, String content);

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException;
}
