package com.OOP.TicketBookingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Integer> {
    @Query(value = "SELECT * FROM transaction WHERE user_email=?", nativeQuery=true)
    public List<Transaction> findByEmail(String email);

    @Query(value = "SELECT * FROM transaction WHERE event_id=? AND user_id=?", nativeQuery=true)
    public List<Transaction> getPurchasedTickets(int eventId, int userId);

    @Query(value = "SELECT COALESCE(MAX(transaction_id), 0) FROM transaction", nativeQuery=true)
    public int getMaxTransactionId();

    @Query(value = "SELECT * FROM transaction WHERE user_id=?", nativeQuery=true)
    public List<Transaction> findbyUserId(int userId);

    @Query(value = "SELECT * FROM transaction WHERE ticket_id=?", nativeQuery=true)
    public Transaction findByTicketId(int ticketId);

    @Query(value = "SELECT * FROM transaction WHERE event_id=?", nativeQuery=true)
    public List<Transaction> findByEventId(int eventId);

    @Modifying
    @Query (value = "UPDATE transaction t SET t.status='redeemed' where t.user_id=:userId and t.ticket_id=:ticketId and t.ticket_type_id=:ticketTypeId", nativeQuery = true)
    public void updateTicketStatus(@Param("userId")int userId, @Param("ticketId")int ticketId, @Param("ticketTypeId") int ticketTypeId);

    @Modifying
    @Query (value = "UPDATE transaction t SET t.status='cancelled' where t.ticket_id=:ticketId", nativeQuery = true)
    public void updateTicketStatus(@Param("ticketId")int ticketId);

    @Modifying
    @Query(value = "UPDATE transaction t SET t.status='refunded' where t.transaction_id=:transactionId and t.ticketTypeId=:ticketTypeId", nativeQuery = true)
    public void refundTicket(@Param("transactionId")int transactionId, @Param("ticketTypeId") int ticketTypeId);

    @Query(value = "SELECT * FROM transaction WHERE user_email=? AND transaction_id = ?", nativeQuery=true)
    public List<Transaction> findByEmailAndTransactionId(String email, int transaction_id);

    @Query(value = "SELECT * FROM transaction WHERE transaction_id = :transactionId and ticket_type_id = :ticketTypeId", nativeQuery = true)
    public Transaction findByTransactionIdAndticketTypeId(@Param("transactionId")int transactionId, @Param("ticketTypeId")int ticketTypeId);
}
