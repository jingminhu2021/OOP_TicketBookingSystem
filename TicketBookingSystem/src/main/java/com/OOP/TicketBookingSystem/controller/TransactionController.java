package com.OOP.TicketBookingSystem.controller;


import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import java.util.Base64;
import java.net.URLEncoder;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Value("${app.secret-key}")
    private String SECRET_KEY;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/bookTicket")
    public JsonNode bookTicket(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return transactionService.bookTicket(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Customer')")
    @PostMapping("/bookingHistory")
    public List<Transaction> bookingHistory(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(body);
            int id = jsonNode.get("user_id").asInt();
            List<Transaction> trans_ls = transactionService.bookingHistory(id);
            return trans_ls;
        }catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }


@PreAuthorize("hasRole('Ticketing_Officer')")
@PostMapping("/onSiteBookTicket")
public JsonNode onSiteBookTicket(@RequestBody String body){
    ObjectMapper mapper = new ObjectMapper();
    try {
        JsonNode jsonNode = mapper.readTree(body);
        return transactionService.onSiteBookTicket(jsonNode);

    } catch (Exception e) {
        System.err.println(e);
    }
    return null;
    }

    @PostMapping("/sendTicketDetailsEmail")
    public JsonNode sendTicketDetailsEmail(@RequestBody String body){
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(body);
            String email = jsonNode.get("email").asText();
            int transactionId = jsonNode.get("TransactionId").asInt();
            return transactionService.sendTicketDetailsEmail(email, transactionId);
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    private String encrypt(String plainText) throws Exception {
        final String encryptionKey = SECRET_KEY;

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    @PostMapping("/generateQRCode")
    public JsonNode generateQRCode(@RequestBody String body){
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(body);
            int ticketId = jsonNode.get("ticketId").asInt();
            String text = jsonNode.get("text").asText();
            String domain = text.split("\\?")[0];
            String query = text.split("\\?")[1];
            System.err.println(domain);
            System.err.println(query);
            // Encrypt the query
            String encryptedQuery = encrypt(query);
            // URL encode the encrypted query
            String encodedEncryptedQuery = URLEncoder.encode(encryptedQuery, "UTF-8");

            return transactionService.generateQRCode(ticketId, domain + "?" + encodedEncryptedQuery);
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    @PostMapping("/cancellation")
    public JsonNode cancellation(@RequestBody JsonNode body){
        try {
            int transactionId = body.get("transactionId").asInt();
            int ticketTypeId = body.get("ticketTypeId").asInt();
            return transactionService.cancellation(transactionId, ticketTypeId);
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}
