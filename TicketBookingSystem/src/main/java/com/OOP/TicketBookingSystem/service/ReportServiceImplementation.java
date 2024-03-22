package com.OOP.TicketBookingSystem.service;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.opencsv.CSVWriter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.Ticket_Type;
import com.OOP.TicketBookingSystem.repository.EventRepo;
import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.repository.TransactionRepo;
import com.OOP.TicketBookingSystem.repository.TicketTypeRepo;

@Service
public class ReportServiceImplementation implements ReportService{

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private TicketTypeRepo ticketTypeRepo;

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


    @Override
    public String csvWriter(JsonNode body) throws IOException{
    
        List<String[]> rows = new ArrayList<>();
        
        boolean firstCheck = true;
        for(JsonNode item : body){
            if(firstCheck){
                List<String> header = new ArrayList<>(); 
                Iterator<String> fieldNames = item.fieldNames();
                while (fieldNames.hasNext()) {
                    String fieldName = fieldNames.next();
                    header.add(fieldName);
                }
                
                rows.add(header.toArray(new String[header.size()]));
                firstCheck=false;
            }

            List<String> items = new ArrayList<>();
            for(JsonNode value : item){
                items.add(value.toString());
            }
            rows.add(items.toArray(new String[items.size()]));
        }
        UUID uuid = UUID.randomUUID();
        String filepath = "src/main/resources/static/csv/event"+uuid+".csv";
        CSVWriter writer = new CSVWriter(new FileWriter(filepath));
        
        writer.writeAll(rows);
        writer.close();
        return filepath;
    }
    
}
