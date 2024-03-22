package com.OOP.TicketBookingSystem.service;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReportService {

    public JsonNode viewSalesStatistics(JsonNode body);

    public JsonNode viewAllSalesStatistics(JsonNode body);
}
