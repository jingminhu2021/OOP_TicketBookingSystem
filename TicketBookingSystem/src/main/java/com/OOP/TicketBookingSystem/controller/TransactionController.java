package com.OOP.TicketBookingSystem.controller;

import java.util.List;
// import org.hibernate.mapping.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.model.Event;
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
}
