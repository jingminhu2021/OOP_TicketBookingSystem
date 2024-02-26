package com.OOP.TicketBookingSystem.service;

import java.math.BigInteger; 
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException; 
import java.util.List;
import java.util.Random;

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
import com.fasterxml.jackson.databind.JsonNode;

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
        String emailregex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        
        if(userRepo.findByEmail(email) == null && email.matches(emailregex)){

            String salt = getSaltString();
            user.setSalt(salt);
            user.setPassword(getHash(user.getPassword(), salt));

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
    public User login(JsonNode body){
        
        String email = body.get("email").textValue();
        User user = userRepo.findByEmail(email);
        
        if(user != null){
            String password = getHash(body.get("password").textValue(), user.getSalt());
            String hashedPassword = user.getPassword();

            if( hashedPassword.equals(password) ){
                return user;
            }
        }

        return null;
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
    public String getHash(String password, String salt){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // digest() method called 
            // to calculate message digest of an input 
            // and return array of byte 
            byte[] messageDigest = md.digest((password+salt).getBytes()); 

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

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
}
