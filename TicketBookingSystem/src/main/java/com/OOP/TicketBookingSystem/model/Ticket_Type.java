package com.OOP.TicketBookingSystem.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Ticket_Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ticketTypeId;
    @Column
    private Integer eventId;
    @Column
    private String eventCat;
    @Column(columnDefinition = "decimal(38, 2) default 0.00")
    private BigDecimal eventPrice;
    @Column(columnDefinition = "decimal(10, 2) default 0.00")
    private BigDecimal cancellationFeePercentage;
    @Column
    private Integer numberOfTix;

    public int getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(int ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventCat() {
        return eventCat;
    }

    public void setEventCat(String eventCat) {
        this.eventCat = eventCat;
    }

    public BigDecimal getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(BigDecimal eventPrice) {
        this.eventPrice = eventPrice;
    }

    public int getNumberOfTix() {
        return numberOfTix;
    }

    public void setNumberOfTix(int numberOfTix) {
        this.numberOfTix = numberOfTix;
    }

    public BigDecimal getCancellationFeePercentage() {
        return cancellationFeePercentage;
    }

    public void setCancellationFeePercentage(BigDecimal cancellationFeePercentage) {
        this.cancellationFeePercentage = cancellationFeePercentage;
    }

}