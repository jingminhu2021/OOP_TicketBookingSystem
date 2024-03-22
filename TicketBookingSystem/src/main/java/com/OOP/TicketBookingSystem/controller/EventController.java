package com.OOP.TicketBookingSystem.controller;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public JsonNode createEvent(@RequestBody Event event) {
        User user = userService.getLoggedInUser();
        String managerName = user.getName();
        
        try {
            return eventService.createEvent(event, managerName);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    
    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/updateEvent")
    public JsonNode updateEvent(@RequestBody Event event) {
        User user = userService.getLoggedInUser();
        String managerName = user.getName();
        event.setEventManagerName(managerName);
        try {
            return eventService.updateEvent(event);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewOwnEventByEventManager")
    public JsonNode viewOwnEventByEventManager() { // view event that was created by the Event Manager
        User user = userService.getLoggedInUser();
        String managerName = user.getName();

        try {
            return eventService.viewEventByEventManager(managerName);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/cancelEventByManager")
    public JsonNode cancelEvent(@RequestBody String body) {
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.cancelEventByManager(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewSalesStatistics")
    public JsonNode viewSalesStatistics(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return reportService.viewSalesStatistics(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewAllSalesStatistics")
    public JsonNode viewAllSalesStatistics(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return reportService.viewAllSalesStatistics(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/getCSV")
    public ResponseEntity<InputStreamResource> getCSV(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            JsonNode jsonNode = mapper.readTree(body);
            jsonNode = reportService.viewSalesStatistics(jsonNode);
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
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            JsonNode jsonNode = mapper.readTree(body);
            jsonNode = reportService.viewAllSalesStatistics(jsonNode);
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
        return eventService.getAllEvents();
    }

    @GetMapping("/viewEvent")
    public JsonNode viewEvent(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.viewEvent(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}
