package com.OOP.TicketBookingSystem.service;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.repository.TicketOfficerRestrictionRepo;
import com.OOP.TicketBookingSystem.model.Ticket_Type;
import com.OOP.TicketBookingSystem.model.Transaction;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.model.Ticket_Officer_Restriction;
import com.OOP.TicketBookingSystem.repository.EventRepo;
import com.OOP.TicketBookingSystem.repository.TicketTypeRepo;
import com.OOP.TicketBookingSystem.repository.TransactionRepo;
import com.OOP.TicketBookingSystem.repository.UserRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.io.OutputStream;

import java.nio.file.Path;
import java.nio.file.Paths;

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

    @Autowired
    private TicketOfficerRestrictionRepo ticketOfficerRestrictionRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ResourceLoader resourceLoader;

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
        // E.g. ["cat1", "cat2", "cat3"] and [2, 0, 3] -> buy 2 cat1, 3 cat3 (list all cats and no of tickets)
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

            // Create QR Code for each tickets purchased -->NOTE:(For now only store ticketId, im planning to store the url of a verifyTicket web page(Still creating))
            List<Transaction> ls = transactionRepo.findByEmail(userEmail);
            for (Transaction transaction: ls){
                int ticketId = transaction.getTicketId();
                String text = "ticketId: "+ticketId;
                generateQRCode(ticketId, text);
            }

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

    @Override
    public JsonNode onSiteBookTicket(JsonNode body){

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("status", false);

        LocalDateTime dateNow = LocalDateTime.now();
        String eventName = body.get("eventName").textValue();
        Event event = eventRepo.findByExactEvent(eventName);
        int eventId = event.getId();
        LocalDateTime dateEvent = event.getDateTime();
        int ticketOfficerId = body.get("ticketOfficerId").asInt();

        if (!dateEvent.toLocalDate().isEqual(dateNow.toLocalDate())){
            node.put("message", "Can only process on-site sales on event day");
            return node;
        }

        Ticket_Officer_Restriction ticketOfficerRestriction = ticketOfficerRestrictionRepo.findByEventIdAndUserId(eventId, ticketOfficerId);
        if (ticketOfficerRestriction==null){
            node.put("message", "Ticket Officer has no permissions to sell tickets");
            return node;
        }

        bookTicket(body);
        node.put("message", "Successfully booked on-site ticket");
        node.put("status", true);
        return node;
    }

    @Override
    public JsonNode sendTicketDetailsEmail(String email, int transaction_id) {
        // Fetch transaction details for the given email and transaction ID
        List<Transaction> transactions = transactionRepo.findByEmailAndTransactionId(email, transaction_id);
        User user = userRepo.findByEmail(email);

        // Constructing the message
        StringBuilder messageBuilder = new StringBuilder();

        // Email styles
        String emailStyle = "font-family: Arial, sans-serif; font-size: 14px;";

        // Header styles
        String headerStyle = "background-color: #f8f9fa; padding: 20px;";

        // Table styles
        String tableStyle = "border-collapse: collapse; width: 100%;";
        String thStyle = "border: 1px solid #dddddd; text-align: left; padding: 8px;";
        String tdStyle = "border: 1px solid #dddddd; text-align: left; padding: 8px;";

        messageBuilder.append("<div style=\"").append(emailStyle).append("\">");
        messageBuilder.append("<div style=\"").append(headerStyle).append("\">");
        messageBuilder.append("<h2 style='color:#007bff;text-align: center;'>Ticket Purchase Confirmation</h2>");
        messageBuilder.append("</div>");

        messageBuilder.append("<div style=\"padding: 20px;\">");
        messageBuilder.append("<p>Dear ").append(user.getName()).append(",</p>");
        messageBuilder.append("<p>Thank you for your purchase! Below are the details of your tickets:</p>");
        int transactionId = transactions.get(0).getTransactionId();
        messageBuilder.append("<p><strong>Transaction ID: </strong>").append(transactionId).append("</p>");
        // Extract booking date
        LocalDateTime bookingDateTime = transactions.get(0).getBookingDateTime();
        // Format the booking date and time 
        String formattedBookingDateTime = bookingDateTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy, hh:mma (EEEE)"));
        messageBuilder.append("<p><strong>Time of booking: </strong>").append(formattedBookingDateTime).append("</p>");
        messageBuilder.append("<p><strong>You have purchased:</strong> ").append(transactions.size()).append(" ticket(s).</p><br>");

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<Integer> ls = new ArrayList<>();

        for (Transaction transaction : transactions) {
            int ticketId = transaction.getTicketId();

            // Adding a row for ticket details with a nested table for event details
            messageBuilder.append("<table style=\"").append(tableStyle).append("\">");
            messageBuilder.append("<tr>");
            messageBuilder.append("<th style=\"").append(thStyle).append("; background-color: #f0f0f0;\">Ticket ID</th>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("; background-color: #ffffff;\">").append(ticketId).append("</td>");
            messageBuilder.append("</tr>");
            messageBuilder.append("<tr>");
            messageBuilder.append("<th style=\"").append(thStyle).append("; background-color: #f0f0f0;\">Event Details</th>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("; background-color: #ffffff;\">");
            // Start of nested table for event details
            messageBuilder.append("<table style=\"").append(tableStyle).append("; background-color: #f8f8f8;\">");
            // Add headers for event details
            messageBuilder.append("<tr>");
            messageBuilder.append("<th style=\"").append(thStyle).append("\">Event Name</th>");
            messageBuilder.append("<th style=\"").append(thStyle).append("\">Venue</th>");
            messageBuilder.append("<th style=\"").append(thStyle).append("\">Date and Time</th>");
            messageBuilder.append("<th style=\"").append(thStyle).append("\">Event Manager</th>");
            messageBuilder.append("<th style=\"").append(thStyle).append("\">Description</th>");
            messageBuilder.append("<th style=\"").append(thStyle).append("\">Event Type</th>");
            messageBuilder.append("<th style=\"").append(thStyle).append("\">Price</th>");
            messageBuilder.append("</tr>");
            
            // Fetch event details for the transaction
            // Event event = eventRepo.findByEventId(transaction.getEventId());
            Event event = eventRepo.findById(transaction.getEventId()).orElse(null);
            String eventName = event.getEventName();
            String venue = event.getVenue();
            LocalDateTime eventDateTime = event.getDateTime();
            // Format the event date and time 
            String formattedEventDateTime = eventDateTime.format(DateTimeFormatter.ofPattern("MM-dd-yyyy, hh:mma (EEEE)"));
            String eventManagerName = event.getEventManagerName();
            String description = event.getDescription();
            int ticket_type_id = transaction.getTicketTypeId();
            int eventId = transaction.getEventId();
            // Get ticket type name
            String eventCat = ticketTypeRepo.findByTicketTypeId(ticket_type_id).getEventCat();
            // Get ticket cat price of event
            BigDecimal eventPrice = ticketTypeRepo.findByEventCat(eventCat, eventId).getEventPrice();
            totalPrice = totalPrice.add(eventPrice);

            // add ticketId to ls
            ls.add(ticketId);
            
            // Add data row for event details
            messageBuilder.append("<tr>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("\">").append(eventName).append("</td>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("\">").append(venue).append("</td>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("\">").append(formattedEventDateTime).append("</td>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("\">").append(eventManagerName).append("</td>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("\">").append(description).append("</td>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("\">").append(eventCat).append("</td>");
            messageBuilder.append("<td style=\"").append(tdStyle).append("\">").append("$ "+eventPrice).append("</td>");
            messageBuilder.append("</tr>");
            // End of nested table for event details
            messageBuilder.append("</table>");
            messageBuilder.append("<img src='cid:"+ticketId+"' alt='"+ticketId+"'/>");
            messageBuilder.append("</table>").append("<br>");
        }

        messageBuilder.append("<div style=\"").append(headerStyle).append("\">");
        messageBuilder.append("<h2 style='text-align: center;color: black'>Total Price: $ "+totalPrice+"<br></h2>");
        messageBuilder.append("</div><br>");

        messageBuilder.append("<p>If there are any issues with your booking, please contact 91234567 immediately.</p>");
        messageBuilder.append("<p>Thank you for choosing our services!</p>");
        messageBuilder.append("<p>Best regards,<br>(Company Name)</p>");
        messageBuilder.append("</div>");
        messageBuilder.append("</div>");

        String message = messageBuilder.toString();

        // Subject for the email
        String subject = "Your Ticket Purchase Confirmation";

        // Sending the email
        // return emailService.sendEmailForTicketComfirm("hujingmin123@gmail.com", subject, message, ls); // for testing - change to ur own email to receive it to ur email.
        return emailService.sendEmailForTicketComfirm(email, subject, message, ls);
    }

    @Override
    public JsonNode generateQRCode(int ticketId, String text) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        try {
            // Create a directory if it doesn't exist
            Path qrCodeDir = Paths.get("src/main/resources/static/qrcodes");
            Files.createDirectories(qrCodeDir);

            // Construct the path for the QR code file
            String qrCodeName = ticketId + ".png";
            Path qrCodePath = qrCodeDir.resolve(qrCodeName);

            // Generate the QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    text,
                    BarcodeFormat.QR_CODE, 400, 400);

            // Write the QR code image to the file
            try (OutputStream outputStream = Files.newOutputStream(qrCodePath, StandardOpenOption.CREATE)) {
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            }
            String qrCodeFilePath = qrCodePath.toString();
            node.put("success", true);
            node.put("message", "QR Code generated successfully");
            node.put("qrCodeFilePath", qrCodeFilePath);
        } catch (IOException | WriterException e) {
            node.put("success", false);
            node.put("message", "Error generating QR Code: " + e.getMessage());
        }
        return node;
    }

}
