package com.trantien.huetutor.services;


import jakarta.mail.MessagingException;
import org.springframework.mail.MailException;

public interface IEmailService {
    public void sendEmail(String to, String subject, String body) throws MessagingException;
}
