package com.OOP.TicketBookingSystem.controller;

import java.util.Base64;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.model.Event;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.service.EventService;
import com.OOP.TicketBookingSystem.service.ReportService;
import com.OOP.TicketBookingSystem.service.UserService;


@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/createEvent")
    public JsonNode createEvent(@RequestBody String body) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();

        ObjectMapper mapper = new ObjectMapper();
        try {
            Event event = new Event();
            JsonNode jsonNode = mapper.readTree(body);

            String eventName = jsonNode.get("eventName").asText();
            String eventType = jsonNode.get("eventType").asText();
            String description = jsonNode.get("description").asText();
            String venue = jsonNode.get("venue").asText();
            LocalDateTime dateTime = LocalDateTime.parse(jsonNode.get("dateTime").asText());
            String image = "";
            if(jsonNode.get("image").textValue() != null) {
                image = jsonNode.get("image").asText();
            }
        
            event.setEventName(eventName);
            event.setEventType(eventType);
            event.setDescription(description);
            event.setVenue(venue);
            event.setDateTime(dateTime);

            return eventService.createEvent(event, managerId, image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/updateEvent")
    public JsonNode updateEvent(@RequestBody Event event) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();
        
        try {    
            return eventService.updateEvent(event, managerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewOwnEventByEventManager")
    public JsonNode viewOwnEventByEventManager() { // view event that was created by the Event Manager
        User user = userService.getLoggedInUser();
        int managerId = user.getId();

        try {
            return eventService.viewEventByEventManager(managerId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/cancelEventByManager")
    public JsonNode cancelEvent(@RequestBody String body) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.cancelEventByManager(jsonNode, managerId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewSalesStatistics")
    public JsonNode viewSalesStatistics(@RequestBody String body) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return reportService.viewSalesStatistics(jsonNode, managerId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewAllSalesStatistics")
    public JsonNode viewAllSalesStatistics(@RequestBody String body) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return reportService.viewAllSalesStatistics(jsonNode, managerId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/getCSV")
    public ResponseEntity<InputStreamResource> getCSV(@RequestBody String body) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();

        ObjectMapper mapper = new ObjectMapper();
        
        try {
            JsonNode jsonNode = mapper.readTree(body);
            jsonNode = reportService.viewSalesStatistics(jsonNode, managerId);

            String filePath = reportService.csvWriter(jsonNode);
            return uploadCsv(filePath);
        
        } catch (Exception e) {
            System.err.println(e);
        } 

        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/getAllCSV")
    public ResponseEntity<InputStreamResource> getAllCSV(@RequestBody String body) {
        User user = userService.getLoggedInUser();
        int managerId = user.getId();

        ObjectMapper mapper = new ObjectMapper();
        System.out.println(managerId);
        try {
            JsonNode jsonNode = mapper.readTree(body);
            jsonNode = reportService.viewAllSalesStatistics(jsonNode, managerId);
            String filePath = reportService.csvWriter(jsonNode);
            return uploadCsv(filePath);

        } catch (Exception e) {
            System.err.println(e);
        } 

        return null;
    }

    private ResponseEntity<InputStreamResource> uploadCsv(String filePath) throws FileNotFoundException{
        Path path = Paths.get(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=data.csv");

        InputStreamResource resource = new InputStreamResource(new FileInputStream(path.toFile()));
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(path.toFile().length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    
    @GetMapping("/viewAllEvents")
    public List<Event> viewAllEvents() {

        List<Event> events = eventService.getAllEvents();
        for(Event event: events){
            String image = event.getImage();
            try{
                byte[] imageBytes = Files.readAllBytes(Paths.get(image));
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                event.setImage("data:image/jpg;base64," + base64Image);
            }catch(IOException e){
                System.err.println(e);
            }
        }

        return events;
    }

    @GetMapping("/viewEvent")
    public JsonNode viewEvent(@RequestParam int id) {
        
        try {
            return eventService.viewEvent(id);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewTicketingOfficerByEventId")
    public JsonNode viewTicketingOfficerByEventId(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            int eventId = jsonNode.get("eventId").asInt();
            return eventService.viewTicketingOfficerByEventId(eventId);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}
