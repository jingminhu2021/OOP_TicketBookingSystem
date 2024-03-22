package com.OOP.TicketBookingSystem.service;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

public interface ReportService {

    public JsonNode viewSalesStatistics(JsonNode body);

    public JsonNode viewAllSalesStatistics(JsonNode body);
}
