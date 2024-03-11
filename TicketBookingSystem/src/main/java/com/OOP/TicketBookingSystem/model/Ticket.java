package com.OOP.TicketBookingSystem.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String userEmail;
    @Column
    private String eventName;
    @Column
    private int numberOfTickets;
    @Column
    private BigDecimal totalCost;
    @Column
    private LocalDateTime bookingDateTime;

    // Getters
    public int getId() {
        return id;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getEventName() {
        return eventName;
    }
    public int getNumberOfTickets() {
        return numberOfTickets;
    }
    public BigDecimal getTotalCost() {
        return totalCost;
    }
    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }
    
    // Setters
    public void setId(int id) {
        this.id = id;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setNumberOfTickets(int numberOfTickets) {
        this.numberOfTickets = numberOfTickets;
    }
    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }
}
