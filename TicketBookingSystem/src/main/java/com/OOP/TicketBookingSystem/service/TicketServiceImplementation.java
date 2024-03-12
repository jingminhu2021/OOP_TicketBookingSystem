package com.OOP.TicketBookingSystem.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Ticket;
import com.OOP.TicketBookingSystem.model.Ticket_Type;
import com.OOP.TicketBookingSystem.repository.TicketRepo;
import com.OOP.TicketBookingSystem.repository.TicketTypeRepo;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.UserRepo;
import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.repository.EventRepo;

@Service
public class TicketServiceImplementation implements TicketService {
    @Autowired
    private TicketRepo ticketRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private TicketTypeRepo ticketTypeRepo;

    @Override
    public JsonNode bookTicket(Ticket ticket) {
        String userEmail = ticket.getUserEmail();
        String eventName = ticket.getEventName();

        int purchaseTickets = ticket.getNumberOfTickets();
        int ticket_type = 1; // ticket.getTicketType(); to be replace when it is implemented

        Ticket_Type ticketType = ticketTypeRepo.findById(ticket_type).get();

        BigDecimal totalCost = ticket.getTotalCost();
        LocalDateTime bookingTime = ticket.getBookingDateTime();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "User does not exist");
        node.put("status", false);

        User user = userRepo.findByEmail(userEmail);
        Event event = eventRepo.findByExactEvent(eventName);

        
        // Check if user exists
        if(user == null){
            return node;
        }

        // Check if event exists
        if (event == null){
            node.put("message", "Event does not exist");
            return node;
        }
        
        // Check if enough tickets
        int remainingTickets = ticketType.getNumberOfTix();

        if(remainingTickets < purchaseTickets){
            node.put("message", "Not enough tickets");
            return node;
        }

        // Check if allowed to book (6 months in advance - no later than 24hrs)
        LocalDateTime eventStart = event.getDateTime();
        if(bookingTime.getMonthValue()-eventStart.getMonthValue() > 6 || bookingTime.isAfter(eventStart.minus(24,ChronoUnit.HOURS))){
            node.put("message", "Booking is not allowed now");
            return node;
        }

        // Check if user has enough money
        if(user.getWallet().compareTo(totalCost) < 0){
            node.put("message", "Wallet has insufficient funds");
            return node;
        }

        try {
            // Book ticket
            ticketRepo.save(ticket);

            // Update remaining event tickets
            ticketType.setNumberOfTix(remainingTickets - purchaseTickets);
            ticketTypeRepo.save(ticketType);

            // Update user wallet
            user.setWallet(user.getWallet().subtract(totalCost));
            userRepo.save(user);

            node.put("message", "Successfully booked Ticket");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());

        } catch (OptimisticLockingFailureException e) {
            node.put("message", e.toString());
        }

    return node;
}}
