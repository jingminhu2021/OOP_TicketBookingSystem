package com.OOP.TicketBookingSystem.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Ticket_Type;
import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.EventRepo;
import com.OOP.TicketBookingSystem.repository.TicketTypeRepo;
import com.OOP.TicketBookingSystem.repository.TransactionRepo;
import com.OOP.TicketBookingSystem.repository.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class TransactionServiceImplementation implements TransactionService {
    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private TicketTypeRepo ticketTypeRepo;

    @Override
    public JsonNode bookTicket(JsonNode body) {
        // JSON input: userEmail, eventName, eventCats, eachCatTickets
        // Get user info
        String userEmail = body.get("userEmail").textValue();
        User user = userRepo.findByEmail(userEmail);
        int userId = (user != null) ? user.getId() : 0;

        // Get event
        String eventName = body.get("eventName").textValue();
        Event event = eventRepo.findByExactEvent(eventName);
        int eventId = (event != null) ? event.getId() : 0;

        int totalTickets = 0;
        // Get Transaction types and number of tickets to purchase in each category
        // E.g. ["cat1", "cat2"] and [2, 3] -> buy 2 cat1, 3 cat2
        List<String> eventCats = new ArrayList<String>();
        if (body.get("eventCats").isArray()) {
            for (JsonNode cat : body.get("eventCats")) {
                eventCats.add(cat.textValue());
            }
        }

        List<Integer> eachCatTickets = new ArrayList<Integer>();
        if (body.get("eachCatTickets").isArray()) {
            for (JsonNode ticketAmt : body.get("eachCatTickets")) {
                eachCatTickets.add(ticketAmt.intValue());
                totalTickets += ticketAmt.intValue();
            }
        }

        // Get booking date time
        LocalDateTime bookingDateTime = LocalDateTime.now();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "No tickets selected");
        node.put("status", false);

        // All conditions: purchasing > 0 tix, correct input values, user exists, event exists, event active, category exists, currently purchasing <= 5 tix, allowed booking range, if total purchased tickets for this event is max 5, user has enough money, enough tickets available     
        // Check if purchasing any tickets
        if (totalTickets == 0) {
            return node;
        }
        
        // Check if user exists
        if(user == null){
            node.put("message", "User does not exist");
            return node;
        }

        // Check if event exists
        if (event == null){
            node.put("message", "Event does not exist");
            return node;
        }

        // Check if event active
        if (event.getStatus().equals("Cancelled")){
            node.put("message", "Event is cancelled");
            return node;
        }

        // Check if valid event categories
        for (String cat : ticketTypeRepo.getEventCats(eventId)) {
            if (!eventCats.contains(cat)) {
                node.put("message", "Invalid event category");
                return node;
            }
        }
        
        // Check if correct input values
        if (eventCats.size() != eachCatTickets.size()) {
            node.put("message", "Invalid input values");
            return node;
        }        

        // Ensure user can only purchase 5 tickets max in this transaction
        if (totalTickets > 5) {
            node.put("message", "Maximum of 5 tickets can be bought");
            return node;
        }

        // Check if allowed to book (6 months in advance - no later than 24hrs)
        LocalDateTime eventStart = event.getDateTime();
        if(bookingDateTime.getMonthValue()-eventStart.getMonthValue() > 6 || bookingDateTime.isAfter(eventStart.minus(24,ChronoUnit.HOURS))){
            node.put("message", "Booking is not allowed now");
            return node;
        }

        // Check if previously purchased + current purchase exceeds 5 tickets
        List<Transaction> existingTransactions = transactionRepo.getPurchasedTickets(eventId, userEmail);
        int previouslyPurchased = existingTransactions.size();
        if (previouslyPurchased + totalTickets > 5) {
            node.put("message", "Maximum of 5 tickets can be bought for this event");
            return node;
        }

        // Get total cost of all tickets + check if enough tickets
        BigDecimal totalCost = BigDecimal.ZERO;
        for (int i = 0; i < eventCats.size(); i++) {
            Ticket_Type ticketType = ticketTypeRepo.findByEventCat(eventCats.get(i), eventId);
            int purchaseTickets = eachCatTickets.get(i);
            // Check if enough tickets
            if (ticketType.getNumberOfTix() < purchaseTickets) {
                node.put("message", "Not enough tickets available for " + eventCats.get(i));
                return node;
            }            
            totalCost = totalCost.add(ticketType.getEventPrice().multiply(BigDecimal.valueOf(purchaseTickets)));
        }

        // Check if user has enough money
        if(user.getWallet().compareTo(totalCost) < 0){
            node.put("message", "Wallet has insufficient funds");
            return node;
        }

        try {   
            // Add each ticket as a transaction
            for (int i = 0; i < eventCats.size(); i++) {
                Ticket_Type ticketType = ticketTypeRepo.findByEventCat(eventCats.get(i), eventId);
                int purchaseTickets = eachCatTickets.get(i);
                ticketType.setNumberOfTix(ticketType.getNumberOfTix() - purchaseTickets);
                // Add a record for each ticket bought
                for (int j = 0; j < purchaseTickets; j++) {
                    existingTransactions = transactionRepo.getPurchasedTickets(eventId, userEmail);
                    // If user has bought tickets before
                    if (existingTransactions.size() > 0) {
                        Transaction transaction = new Transaction();
                        transaction.setTransactionId(existingTransactions.get(0).getTransactionId());
                        transaction.setEventId(eventId);
                        transaction.setTicketTypeId(ticketType.getTicketTypeId());
                        transaction.setBookingDateTime(bookingDateTime);
                        transaction.setUserEmail(userEmail);
                        transaction.setUserId(userId);
                        transactionRepo.save(transaction);
                    }
                    else {
                        Transaction transaction = new Transaction();
                        transaction.setTransactionId(transactionRepo.getMaxTransactionId() + 1);
                        transaction.setEventId(eventId);
                        transaction.setTicketTypeId(ticketType.getTicketTypeId());
                        transaction.setBookingDateTime(bookingDateTime);
                        transaction.setUserEmail(userEmail);
                        transaction.setUserId(userId);
                        transactionRepo.save(transaction);
                    }
                }
                // Update remaining event tickets
                ticketTypeRepo.save(ticketType);
            }

            // Update user wallet
            user.setWallet(user.getWallet().subtract(totalCost));
            userRepo.save(user);

            node.put("message", "Successfully booked ticket(s)");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());

        } catch (OptimisticLockingFailureException e) {
            node.put("message", e.toString());
        }

        return node;
    }

    @Override
    public List<Transaction> bookingHistory(int user_id){
        return transactionRepo.findbyUserId(user_id);
    }
}
