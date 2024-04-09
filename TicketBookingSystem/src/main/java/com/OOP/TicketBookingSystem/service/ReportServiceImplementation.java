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
    public JsonNode viewSalesStatistics(JsonNode body, int managerId) {
        // Obtain event name
        String eventName = body.get("eventName").textValue();

        ObjectMapper mapper = new ObjectMapper();
        
        ArrayNode arrayNode = mapper.createArrayNode();

        Event event = eventRepo.findByExactEvent(eventName);
        int eventId = event.getId();
        List<Transaction> transaction = transactionRepo.findByEventId(eventId);
        List<Ticket_Type> ticketTypes = ticketTypeRepo.findByEventId(eventId);

        for (Ticket_Type ticketType : ticketTypes) {
            ObjectNode node = mapper.createObjectNode();
            
            int attended = 0;
            int ticketSold = 0;
            BigDecimal totalRevenue = BigDecimal.ZERO;
            String eventCat = ticketType.getEventCat();
            int totalTickets = ticketType.getNumberOfTix();
            node.put("Event", event.getEventName());
            node.put("Category", eventCat);
            node.put("Total ticket", totalTickets);
            for (Transaction t : transaction){
                if (ticketType.getTicketTypeId()==t.getTicketTypeId()){
                    ticketSold++;
                    totalRevenue = totalRevenue.add(ticketType.getEventPrice());
                    if (t.getStatus().equals("redeemed")){
                        attended++;
                    }
                }
            }
            node.put("Ticket sold", ticketSold);
            node.put("Revenue", totalRevenue);
            node.put("Customer attendance", attended);
            arrayNode.add(node);
        }

        return arrayNode;
    }

    // View all events sales statistics
    @Override
    public JsonNode viewAllSalesStatistics(JsonNode body, int managerId) {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        ArrayNode arrayNode = mapper.createArrayNode();

        List<Event> events = eventRepo.findByEventManager(managerId);
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

            node.put("Event Name", event.getEventName());
            node.put("Tickets Sold", noOfTicketsSold);
            node.put("Total Revenue", totalRevenue);
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
                while (fieldNames.hasNext()) {;
                    String fieldName = fieldNames.next();
                    header.add(fieldName);
                }
                
                rows.add(header.toArray(new String[header.size()]));
                firstCheck=false;
            }

            List<String> items = new ArrayList<>();
            for(JsonNode value : item){
                
                items.add(value.asText());
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
