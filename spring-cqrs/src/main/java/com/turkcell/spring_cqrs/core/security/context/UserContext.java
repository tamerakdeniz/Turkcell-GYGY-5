package com.turkcell.spring_cqrs.core.security.context;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private final ThreadLocal<String> userId = new ThreadLocal<>();
    private final ThreadLocal<String> email = new ThreadLocal<>();
    private final ThreadLocal<List<String>> roles = ThreadLocal.withInitial(Collections::emptyList);
    private final ThreadLocal<Boolean> isAuthenticated = ThreadLocal.withInitial(() -> false);
    
    public void setUser(String userId, String email, List<String> roles) {
        this.userId.set(userId);
        this.email.set(email);
        this.roles.set(roles == null ? Collections.emptyList() : roles);
        this.isAuthenticated.set(true);
    }

    public void clear() {
        this.userId.remove();
        this.email.remove();
        this.roles.remove();
        this.isAuthenticated.remove();
    }

    public String getUserId() {
        return userId.get();
    }
    public String getEmail() {
        return email.get();
    }
    public List<String> getRoles() {
        return roles.get();
    }
    public boolean isAuthenticated() {
        return isAuthenticated.get();
    }
}
