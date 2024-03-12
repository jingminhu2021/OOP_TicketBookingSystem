package com.OOP.TicketBookingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM transaction WHERE email=?", nativeQuery=true)
    public Transaction findByEmail(String email);

    @Query(value = "SELECT * FROM transaction WHERE event_id=? AND user_email=?", nativeQuery=true)
    public List<Transaction> getPurchasedTickets(int eventId, String userEmail);

    @Query(value = "SELECT COALESCE(MAX(transaction_id), 0) FROM transaction", nativeQuery=true)
    public int getMaxTransactionId();
}
