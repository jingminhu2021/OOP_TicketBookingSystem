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

@Service
public class EventServiceImplementation implements EventService {
    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private EventManagerRepo eventManagerRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private TicketTypeRepo ticketTypeRepo;

    @Override
    public JsonNode createEvent(Event event, String managerName) {
        String eventName = event.getEventName();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event already existed");
        node.put("status", false);

        if (LocalDateTime.now().plusDays(2).isAfter(event.getDateTime())){ 
            node.put("message", "Event date must be at least 2 days from today");
            return node;
        }

        if (eventRepo.findByExactEvent(eventName) != null) {
            node.put("message", "Event already exists");
            return node;
        }
        
        if (eventManagerRepo.findByName(managerName) == null) {
            node.put("message", "Invalid Event Manager");
            return node;
        }
        
        try {
            event.setEventManagerName(managerName);
            eventRepo.save(event);
            node.put("message", "Successfully created Event");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());
        } catch (OptimisticLockingFailureException e) {
            node.put("message", e.toString());
        }
        
        return node;
    }

    @Override
    public Event getEventById(int id) {
        return eventRepo.findById(id).get();
    }

    @Override
    public List<Event> getEventsByEventManager(Event_Manager eventManager) {
        String eventManagerName = eventManager.getName();
        return eventRepo.findByEventManager(eventManagerName);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    @Override
    public List<Transaction> getTransaction(int event_id){
        
        List<Transaction> transactions = transactionRepo.findByEventId(event_id);

        return transactions;
    }

    @Override
    public List<String> getCustomerEmails(List<Transaction> transactions){
        List<String> email = new ArrayList<>();
        for(Transaction transaction: transactions){
            email.add(transaction.getUserEmail());
        }
        return email;
    }

    @Override
    public JsonNode updateEvent(Event event) {
        // Todo add check if event manager match
        int id = event.getId();
        System.out.println(id);
        String eventName = event.getEventName();

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event not found");
        node.put("status", false);

        Event existingEvent = eventRepo.findById(id).orElse(null); //check if event exist
        if (existingEvent == null) {
            return node;
        }

        Field[] fields = Event.class.getDeclaredFields();
        for (Field field : fields) { // Loop through all fields
            field.setAccessible(true);
            try {
                Object newValue = field.get(event);
                Object oldValue = field.get(existingEvent);
                if (newValue == null) { // if new value is null, set it to the old value
                    field.set(event, oldValue); 
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (!existingEvent.getDateTime().isEqual(event.getDateTime()) //check if event date is at least 2 days from today
            && LocalDateTime.now().plusDays(2).isAfter(event.getDateTime())) {
            node.put("message", "Event date must be at least 2 days from today");
            return node;
        }

        Event eventWithSameName = eventRepo.findByExactEvent(eventName); //check if event name already exist and not the same event
        if (eventWithSameName != null && eventWithSameName.getId() != id) {
            node.put("message", "Event name already exist");
            return node;
        }

        try {
            eventRepo.save(event);
            node.put("message", "Successfully updated Event");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());
        } catch (OptimisticLockingFailureException e) {
            node.put("message", e.toString());
        }

        return node;
    }

    @Override
    public JsonNode viewEventByEventManager(String ManagerName) {
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ObjectNode node = mapper.createObjectNode();

        List<Event> events = eventRepo.findByEventManager(ManagerName);

        node.put("message", "No event found");
        node.put("status", false);

        if (!events.isEmpty()) {
            node.put("message", "Event found");
            node.put("status", true);
            node.set("events", mapper.valueToTree(events));
        }

        return node;
    }

    @Override
    public JsonNode cancelEventByManager(JsonNode body) {    
        //To do:
        // 1) check if event belong to the manager - done
        // 2) check if event is not already cancelled - done
        // 3) check if event is not already started - done
        // 4) update the event status to cancelled - done
        // 5) process refund to the customers (put in another function)
        // 6) send email to the customers - done

        // Data expectation
        // 1) requested event manager name
        // 2) event_id

        int event_id = body.get("event_id").intValue();
        String eventManagerName = body.get("eventManagerName").textValue();
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();

        node.put("message", "Event not found");
        node.put("status", false);
        
        Event event = eventRepo.findById(event_id).orElse(null);
        if (event == null) {
            return node;
        }else if (!event.getEventManagerName().equals(eventManagerName)) {
            node.put("message", "Invalid Event Manager");
            return node;
        }else if (LocalDateTime.now().isAfter(event.getDateTime())) {
            node.put("message", "Event already started");
            return node;
        }else if (event.getStatus().equals("Cancelled")) {
            node.put("message", "Event already cancelled");
            return node;
        }
        
        try {
            event.setStatus("Cancelled");
            eventRepo.save(event);
            node.put("message", "Event successfully cancelled");
            node.put("status", true);
        } catch (IllegalArgumentException e) {
            node.put("message", e.toString());
        }
        
        if(node.get("status").asBoolean()){
            // send email to the customers
            // get all the emails of the customers who bought the ticket
            List<Transaction> transactions = this.getTransaction(event_id);
            List<Transaction> nonCancelled = new ArrayList<>();

            for(Transaction transaction: transactions){
                if(transaction.getStatus().equals("active")){
                    transaction.setStatus("cancelled");
                    transactionRepo.save(transaction);
                    nonCancelled.add(transaction);
                }
            }
            
            List<String> emails = this.getCustomerEmails(nonCancelled);

            //refund
            systemRefund(nonCancelled, true);

            //Send Email
            String subject = String.format("[Notice] %s Cancellation", event.getEventName());
            String message = String.format("The event %s has been cancelled. We are sorry for the inconvenience. Your ticket will be refunded.%n Regards, Event Manager",event.getEventName());
            for(String email: emails){
                emailService.sendEmail(email,subject,message);
            }
            
        }

        return node;
    }

    @Override
    public boolean systemRefund(List<Transaction> transactions, boolean eventManager){
        try{
            for(Transaction transaction : transactions){

                int ticketTypeID = transaction.getTicketTypeId();
                Ticket_Type ticketType = ticketTypeRepo.findByTicketTypeId(ticketTypeID);
                BigDecimal price = ticketType.getEventPrice();
                String email = transaction.getUserEmail();
                User user = userRepo.findByEmail(email);
                BigDecimal wallet = user.getWallet();

                if(!eventManager){ //if not cancel by event manager, then we will need to deduct the cancellation fee
                    BigDecimal cancellationFeePercentage = ticketType.getCancellationFeePercentage();
                    if(!cancellationFeePercentage.equals(BigDecimal.ZERO)){
                        BigDecimal cancellationFee = price.divide(cancellationFeePercentage);
                        price = price.subtract(cancellationFee);
                    }    
                }

                user.setWallet(wallet.add(price));
                userRepo.save(user);
            }   
            return true; 
        }catch(Exception e){
            System.err.println(e);
            return false;
        }
    }

    @Override
    public JsonNode viewEvent(JsonNode body) {
        // Obtain event name
        String eventName = body.get("eventName").textValue();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        ObjectNode node = mapper.createObjectNode();

        Event event = eventRepo.findByExactEvent(eventName);

        node.put("message", "No event found");
        node.put("status", false);

        // Check if event exists
        if (event == null) {
            return node;
        }

        // Return event + ticket types
        node.put("message", "Event found");
        node.put("status", true);
        node.set("event", mapper.valueToTree(event));
        node.set("ticketTypes", mapper.valueToTree(ticketTypeRepo.findByEventId(event.getId())));

        return node;
    }

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

        // Get the total revenue
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

        node.put("message", "Event found");
        node.put("status", true);
        node.put("ticketsSold", noOfTicketsSold);
        node.put("totalRevenue", totalRevenue);

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
