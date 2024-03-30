package com.example.postingapp.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.postingapp.entity.User;

public class UserDetailsImpl implements UserDetails {
    private final User user;
    private final Collection<GrantedAuthority> authorities;

    public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    public User getUser() {
        return user;
    }

    // Returns the hashed password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // Returns the email for login
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    // Returns the roles of the user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Returns true if the account is not expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Returns true if the account is not locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Returns true if the credentials are not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // Returns true if the account is enabled
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}

