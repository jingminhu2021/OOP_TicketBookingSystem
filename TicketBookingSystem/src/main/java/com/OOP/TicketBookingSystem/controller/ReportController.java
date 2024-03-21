package com.OOP.TicketBookingSystem.controller;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private TicketTypeService eventService;

    @PreAuthorize("hasRole('Event_Manager')")
    @PostMapping("/viewSalesStatistics")
    public JsonNode viewSalesStatistics(@RequestBody String body) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode jsonNode = mapper.readTree(body);
            return eventService.viewSalesStatistics(jsonNode);
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
            return eventService.viewAllSalesStatistics(jsonNode);
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
    

}