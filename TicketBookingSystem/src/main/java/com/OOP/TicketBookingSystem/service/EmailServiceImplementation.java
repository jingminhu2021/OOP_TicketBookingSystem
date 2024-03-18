package com.OOP.TicketBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailServiceImplementation implements EmailService{
    
    @Autowired
    private JavaMailSender emailSender;

    public JsonNode sendEmail(String email, String subject, String message) {


        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        try{
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true); // Set content as HTML

            emailSender.send(mimeMessage);

            node.put("message", "Email sent");
            node.put("status", true);
        }catch(Exception e){
            node.put("message", e.getMessage());
            node.put("status", false);
        }

        return node;   
    }
}
