package com.OOP.TicketBookingSystem.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

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


// @PreAuthorize("hasRole('Ticket_Officer')")
// @PostMapping("/onSiteBookTicket")
// public JsonNode onSiteBookTicket(@RequestBody String body){
//     ObjectMapper mapper = new ObjectMapper();
//     try {
//         JsonNode jsonNode = mapper.readTree(body);
//         // JSON input: userEmail, eventName, eventCats, eachCatTickets
//         String userEmail = jsonNode.get("userEmail").textValue();
//         String eventName = jsonNode.get("eventName").textValue();
//         List<String> eventCats = new ArrayList<String>();
//         if (body.get("eventCats").isArray()) {
//             for (JsonNode cat : body.get("eventCats")) {
//                 eventCats.add(cat.textValue());
//             }
//         }
//         List<Integer> eachCatTickets = new ArrayList<Integer>();
//         if (body.get("eachCatTickets").isArray()) {
//             for (JsonNode ticketAmt : body.get("eachCatTickets")) {
//                 eachCatTickets.add(ticketAmt.intValue());
//                 totalTickets += ticketAmt.intValue();
//             }
//         }
//         int ticketOfficerId = body.get("ticketOfficerId").asInt();
//         return transactionService.onSiteBookTicket(userEmail, eventName, eventCats, eachCatTickets, ticketOfficerId);
//     } catch (Exception e) {
//         System.err.println(e);
//     }
//     return null;
//     }

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
    
    @PostMapping("/generateQRCode")
    public JsonNode generateQRCode(@RequestBody String body){
        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode jsonNode = mapper.readTree(body);
            int ticketId = jsonNode.get("ticketId").asInt();
            String text = jsonNode.get("text").asText();
            transactionService.generateQRCode(ticketId, text);
        }
        catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

}
