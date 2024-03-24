package com.OOP.TicketBookingSystem.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReportService {

    public JsonNode viewSalesStatistics(JsonNode body);

    public JsonNode viewAllSalesStatistics(JsonNode body);

    public String csvWriter(JsonNode body) throws IOException;
}