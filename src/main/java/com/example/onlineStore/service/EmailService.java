package com.example.onlineStore.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException {
        // Create a MimeMessage object.
        // Unlike SimpleMailMessage, MimeMessage supports HTML, attachments, and encoding.
        MimeMessage message = emailSender.createMimeMessage();
        // Use MimeMessageHelper to easily populate the message.
        // The 'true' parameter indicates that this is a "multipart" message (supports HTML/attachments).
        // "UTF-8" ensures special characters (like symbols or Cyrillic) display correctly.
        MimeMessageHelper helper=new MimeMessageHelper(message, true);

        helper.setTo(to); // Destination address
        helper.setSubject(subject); // Subject line
        // Set the email body
        // The second parameter 'true' tells Spring that the string contains HTML tags (like <b> or <a>).
        helper.setText(text, true);
        // Finalize and send the email through the SMTP server
        emailSender.send(message);
    }


}
