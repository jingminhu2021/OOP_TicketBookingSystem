package com.OOP.TicketBookingSystem.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketId;
    @Column
    private int transactionId;
    @Column
    private int eventId;
    @Column
    private int ticketTypeId;
    @Column
    private LocalDateTime bookingDateTime;
    @Column
    private String userEmail;
    @Column
    private int userId;
    @Column
    private String status;


    // Getters
    public int getTicketId() {
        return ticketId;
    }
    public int getTransactionId() {
        return transactionId;
    }
    public int getEventId() {
        return eventId;
    }
    public int getTicketTypeId() {
        return ticketTypeId;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public LocalDateTime getBookingDateTime() {
        return bookingDateTime;
    }
    public int getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }
    
    // Setters
    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }
    public void setTransactionId(int transaction_id) {
        this.transactionId = transaction_id;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }   
    public void setTicketTypeId(int ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setBookingDateTime(LocalDateTime bookingDateTime) {
        this.bookingDateTime = bookingDateTime;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
