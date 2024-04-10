package com.OOP.TicketBookingSystem.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String role;
    private BigDecimal wallet;
}
