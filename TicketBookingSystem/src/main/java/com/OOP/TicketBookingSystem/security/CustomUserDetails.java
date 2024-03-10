package com.OOP.TicketBookingSystem.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.OOP.TicketBookingSystem.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails extends User implements UserDetails{
    private String email;
    private String password;
    Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User byUsername) {
        this.email = byUsername.getEmail();
        this.password= byUsername.getPassword();
        List<GrantedAuthority> auths = new ArrayList<>();
        
        auths.add(new SimpleGrantedAuthority(byUsername.getClass().getSimpleName()));
        
        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
