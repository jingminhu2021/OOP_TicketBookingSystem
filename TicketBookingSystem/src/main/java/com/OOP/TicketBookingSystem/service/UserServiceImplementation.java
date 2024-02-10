package com.OOP.TicketBookingSystem.service;

import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.OOP.TicketBookingSystem.model.Customer;
import com.OOP.TicketBookingSystem.model.Event_Manager;
import com.OOP.TicketBookingSystem.model.Ticketing_Officer;
import com.OOP.TicketBookingSystem.model.User;
import com.OOP.TicketBookingSystem.repository.CustomerRepo;
import com.OOP.TicketBookingSystem.repository.EventManagerRepo;
import com.OOP.TicketBookingSystem.repository.TicketingOfficerRepo;
import com.OOP.TicketBookingSystem.repository.UserRepo;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired    
    private CustomerRepo customerRepo;
    
    @Autowired
    private EventManagerRepo eventManagerRepo;

    @Autowired
    private TicketingOfficerRepo ticketingOfficerRepo;
    
    @Override
    public boolean createUser(User user){
        String email = user.getEmail();

        if(userRepo.findByEmail(email) == null){

            user.setPassword(getHash(user.getPassword()));

            if(user instanceof Customer){
                customerRepo.save((Customer) user);
                return true;
            }else if (user instanceof Event_Manager){
                eventManagerRepo.save((Event_Manager) user);
                return true;
            }else if (user instanceof Ticketing_Officer){
                ticketingOfficerRepo.save((Ticketing_Officer) user);
                return true;
            }
        }

        return false;
    }   

    @Override
    public User getUserById(int id) {
        return userRepo.findById(id).get();
    }

    @Override
    public User getUserByEmail(String email){
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public String getHash(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // digest() method called 
            // to calculate message digest of an input 
            // and return array of byte 
            byte[] messageDigest = md.digest(password.getBytes()); 

            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 

            // Convert message digest into hex value 
            String hashtext = no.toString(16); 

            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 

            return hashtext; 

        }catch (NoSuchAlgorithmException e){
            System.out.println("Exception thrown for incorrect algorithm: " + e); 
            return null; 
        }
    }
}
