package com.turkcell.spring_cqrs.core.security.context;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private String userId;
    private String email;
    private List<String> roles = Collections.EMPTY_LIST;
    private boolean isAuthenticated = false;
    
    public void setUser(String userId, String email, List<String> roles) {
        this.userId = userId;
        this.email = email;
        this.roles = roles;
        this.isAuthenticated = true;
    }

    public String getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }
    public List<String> getRoles() {
        return roles;
    }
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
}