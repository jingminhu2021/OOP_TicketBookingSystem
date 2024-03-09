package com.OOP.TicketBookingSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.OOP.TicketBookingSystem.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {
    @Query(value = "SELECT * FROM users WHERE email=?",nativeQuery=true)
    public Optional<User> findByOptEmail(String email);
    
    @Query(value = "SELECT * FROM users WHERE email=?",nativeQuery=true)
    public User findByEmail(String email);

    @Modifying
    @Query (value = "UPDATE User u set u.role='Ticket Manager' where u.id = :id", nativeQuery = true)
    public void setTicketManager(@Param("id")int id);
}
