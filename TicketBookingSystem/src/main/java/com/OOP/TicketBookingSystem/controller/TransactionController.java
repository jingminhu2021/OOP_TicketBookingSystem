package com.OOP.TicketBookingSystem.controller;


import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.service.TransactionService;
import com.OOP.TicketBookingSystem.service.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import java.util.Base64;
import java.net.URLDecoder;
import java.net.URLEncoder;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Value("${enc.secret-key}")
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

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/success")
    public JsonNode success(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            String session_id = jsonNode.get("session_id").asText();
            return transactionService.success(session_id);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/bookingHistory")
    public JsonNode bookingHistory() {
        User user = userService.getLoggedInUser();
        int id = user.getId();
        try{
            return transactionService.bookingHistory(id);
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
    
    public String decrypt(String encryptedText) throws Exception {
        final String encryptionKey = SECRET_KEY;

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }

    @PostMapping("/decryptParams")
    public String decryptParams(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Decrypt the encrypted parameters
            JsonNode jsonNode = mapper.readTree(body);
            String urlDecodedParams = URLDecoder.decode(jsonNode.get("encryptedText").asText(), "UTF-8").replace(" ", "+").replace("=", "");
            String decryptedParams = decrypt(urlDecodedParams);
            return decryptedParams.replace(body, decryptedParams);
        } catch (Exception e) {
            e.printStackTrace(); 
            return "Decryption failed";
        }
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
            return transactionService.generateQRCode(ticketId, text);
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Customer')")
    @PostMapping("/cancellation")
    public JsonNode cancellation(@RequestBody JsonNode body){
        try {
            int ticket_id = body.get("ticket_id").asInt();
            User user = userService.getLoggedInUser();
            
            return transactionService.cancellation(ticket_id, user);
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Customer')")
    @PostMapping("getTicketDetails")
    public Transaction getTicketDetails(@RequestBody JsonNode body){
        try {
            
            int ticket_id = body.get("ticket_id").asInt();
            return transactionService.getTicketDetails(ticket_id);
        }
        catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}
