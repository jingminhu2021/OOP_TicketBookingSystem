package com.OOP.TicketBookingSystem.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReportService {

    public JsonNode viewSalesStatistics(JsonNode body, int managerId);

    public JsonNode viewAllSalesStatistics(JsonNode body, int managerId);

    public String csvWriter(JsonNode body) throws IOException;
}
