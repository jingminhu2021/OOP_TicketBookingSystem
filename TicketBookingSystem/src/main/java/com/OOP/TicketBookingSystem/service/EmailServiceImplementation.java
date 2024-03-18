package com.OOP.TicketBookingSystem.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailServiceImplementation implements EmailService {
    
    @Autowired
    private JavaMailSender emailSender;

    public JsonNode sendEmail(String email, String subject, String message) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true); // Set content as HTML

            emailSender.send(mimeMessage);

            node.put("message", "Email sent");
            node.put("status", true);
        } catch (Exception e) {
            node.put("message", e.getMessage());
            node.put("status", false);
        }

        return node;   
    }

    public JsonNode sendEmailForTicketComfirm(String email, String subject, String message, List<Integer> ticketIds) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
    
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true); // Set content as HTML
    
            // Loop through each ticket ID in the list and attach the corresponding image
            for (int i = 0; i < ticketIds.size(); i++) {
                int ticketId = ticketIds.get(i);
                String imageContentId = ""+(i + 1); // Content-ID for the image
    
                // Attach the image based on the ticket ID
                String imagePath = "ticket_barcodes/" + ticketId + ".png";
                ClassPathResource imageResource = new ClassPathResource(imagePath);
                helper.addInline(imageContentId, imageResource, "image/png");
            }
    
            emailSender.send(mimeMessage);
    
            node.put("message", "Email sent");
            node.put("status", true);
        } catch (Exception e) {
            node.put("message", e.getMessage());
            node.put("status", false);
        }
    
        return node;   
    }
    
}

