package com.OOP.TicketBookingSystem.service;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.model.Ticket_Type;
import com.OOP.TicketBookingSystem.repository.EventManagerRepo;
import com.OOP.TicketBookingSystem.repository.EventRepo;
import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.TransactionRepo;
import com.OOP.TicketBookingSystem.repository.UserRepo;
import com.OOP.TicketBookingSystem.repository.TicketTypeRepo;

public class ReportServiceImplementation implements ReportService{

    @Autowired
    private EventRepo eventRepo;

    // Individual event sales statistics
    @Override
    public JsonNode viewSalesStatistics(JsonNode body) {
        // Obtain event name
        String eventName = body.get("eventName").textValue();
        String eventManagerName = body.get("eventManagerName").textValue();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        Event event = eventRepo.findByExactEvent(eventName);

        node.put("message", "No event found");
        node.put("status", false);

        // Check if event exists
        if (event == null) {
            return node;
        }

        if (!event.getEventManagerName().equals(eventManagerName)) {
            node.put("message", "Invalid Event Manager");
            return node;
        }

        // Get the event id
        int eventId = event.getId();

        // Obtain number of tickets sold
        List<Transaction> transaction = transactionRepo.findByEventId(eventId);
        int noOfTicketsSold = transaction.size();

        // Check if any tickets sold
        if (noOfTicketsSold == 0) {
            node.put("message", "No tickets sold");
            return node;
        }

        // Get the total revenue and attendance
        BigDecimal totalRevenue = BigDecimal.ZERO;
        List<Ticket_Type> ticketTypes = ticketTypeRepo.findByEventId(eventId);

        // Add prices of each ticket
        for (Transaction t : transaction) {
            for (Ticket_Type ticketType : ticketTypes) {
                if (t.getTicketTypeId() == ticketType.getTicketTypeId()) {
                    totalRevenue = totalRevenue.add(ticketType.getEventPrice());
                    break;
                }
            }
        }

        // Attendance rate
        int totalTickets = 0;
        for (Ticket_Type ticketType : ticketTypes) {
            totalTickets = totalTickets + ticketType.getNumberOfTix();
        }
        double attendanceRate = (double)noOfTicketsSold / (double)totalTickets;

        node.put("message", "Event found");
        node.put("status", true);
        node.put("ticketsSold", noOfTicketsSold);
        node.put("totalRevenue", totalRevenue);
        node.put("attendanceRate", attendanceRate);

        return node;
    }

    // View all events sales statistics
    @Override
    public JsonNode viewAllSalesStatistics(JsonNode body) {
        String eventManagerName = body.get("eventManagerName").textValue();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        List<Event> events = eventRepo.findByEventManager(eventManagerName);

        node.put("message", "No events found");
        node.put("status", false);

        // Check if any events exist
        if (events.isEmpty()) {
            return node;
        }

        // Loop through each event
        for (Event event : events) {
            BigDecimal totalRevenue = BigDecimal.ZERO;

            int eventId = event.getId();

            // Obtain number of tickets sold
            List<Transaction> transaction = transactionRepo.findByEventId(eventId);
            int noOfTicketsSold = transaction.size();

            node = mapper.createObjectNode();

            // Check if any tickets sold
            if (noOfTicketsSold == 0) {
                node.put("message", event.getEventName());
                node.put("ticketsSold", noOfTicketsSold);
                node.put("totalRevenue", totalRevenue);
                node.put("status", true);
                arrayNode.add(node);
                continue;
            }

            // Get the total revenue
            List<Ticket_Type> ticketTypes = ticketTypeRepo.findByEventId(eventId);

            // Add prices of each ticket
            for (Transaction t : transaction) {
                for (Ticket_Type ticketType : ticketTypes) {
                    if (t.getTicketTypeId() == ticketType.getTicketTypeId()) {
                        totalRevenue = totalRevenue.add(ticketType.getEventPrice());
                        break;
                    }
                }
            }

            node.put("message", event.getEventName());
            node.put("ticketsSold", noOfTicketsSold);
            node.put("totalRevenue", totalRevenue);
            node.put("status", true);
            arrayNode.add(node);
        }

        return arrayNode;
    }

    
}
