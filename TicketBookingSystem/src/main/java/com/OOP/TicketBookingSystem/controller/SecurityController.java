package com.OOP.TicketBookingSystem.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.OOP.TicketBookingSystem.dto.JwtResponse;
import com.OOP.TicketBookingSystem.dto.LoginRequest;
import com.OOP.TicketBookingSystem.dto.RegisterRequest;
import com.OOP.TicketBookingSystem.model.Customer;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.model.Ticketing_Officer;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.CustomerRepo;
import com.OOP.TicketBookingSystem.repository.EventManagerRepo;
import com.OOP.TicketBookingSystem.repository.TicketingOfficerRepo;
import com.OOP.TicketBookingSystem.repository.UserRepo;
import com.OOP.TicketBookingSystem.security.JwtService;

@RestController
public class SecurityController {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired    
    private CustomerRepo customerRepo;
    
    @Autowired
    private EventManagerRepo eventManagerRepo;

    @Autowired
    private TicketingOfficerRepo ticketingOfficerRepo;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/login")
    public JwtResponse login(@ModelAttribute LoginRequest request){

    
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

            //generate our token & return
            
        if(authentication.isAuthenticated()){
            return JwtResponse.builder()
                    .accessToken(jwtService.GenerateToken(request.getEmail()))
                    .build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }


    }

    @PostMapping("/createNewUser")
    public ResponseEntity createNewUser(@ModelAttribute RegisterRequest request){
        String email = request.getEmail();
        Optional<User> userOptional = userRepo.findByOptEmail(email);
        if(userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
        String emailregex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        
        if(!email.matches(emailregex)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email");
        }
        String role = request.getRole();
        
        switch (role) {
            case "Customer":
                Customer customer = new Customer();
                customer.setEmail(email);
                customer.setName(request.getName());
                customer.setPassword(encoder.encode(request.getPassword()));
                customerRepo.save(customer);
                return ResponseEntity.ok("success");
        
            case "Event_Manager":
                Event_Manager eventManager = new Event_Manager();
                eventManager.setEmail(email);
                eventManager.setName(request.getName());
                eventManager.setPassword(encoder.encode(request.getPassword()));
                eventManagerRepo.save(eventManager);
                return ResponseEntity.ok("success");
        
            case "Ticketing_Officer":
                Ticketing_Officer ticketingOfficer = new Ticketing_Officer();
                ticketingOfficer.setEmail(email);
                ticketingOfficer.setName(request.getName());
                ticketingOfficer.setPassword(encoder.encode(request.getPassword()));
                ticketingOfficerRepo.save(ticketingOfficer);
                return ResponseEntity.ok("success");
        
            default:
                // Handle the case where className does not match any of the above
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid role");
        } 
    }
}
