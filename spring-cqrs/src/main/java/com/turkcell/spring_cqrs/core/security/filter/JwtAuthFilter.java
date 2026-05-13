package com.turkcell.spring_cqrs.core.security.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.turkcell.spring_cqrs.core.security.context.UserContext;
import com.turkcell.spring_cqrs.core.security.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Her istekte devreye gir, varsa JWT'i doğrula ve sisteme bak, bu ikisi şu jwt ile girildi bilgisini tanıt...

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserContext userContext;

    public JwtAuthFilter(JwtService jwtService, UserContext userContext) {
        this.jwtService = jwtService;
        this.userContext = userContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
            // request -> istek
            // response -> repsonse o ana kadar ki oluşan halini döndürür
            // filterChain -> (zindirin kendisi)bir sonraki filtreye geçiş yapmamızı sağlar

            String jwtHeader = request.getHeader("Authorization");

            if (jwtHeader != null) {
                String token = jwtHeader.substring(7); // "Bearer " kısmını atlamak için
                // token doğrulama işlemleri yapılabilir
                try {
                    String userId = jwtService.extractUserId(token);
                    String email = jwtService.extractEmail(token);
                    // TODO : Implement roles extraction from JWT if needed
                    List<String> roles = Collections.EMPTY_LIST; // jwt'den roller de çekilebilir
                    userContext.setUser(userId, email, roles); // roller de jwt'den çekilebilir
                } catch (Exception e) {
                    // SecurityContextHolder.Clear(); // Geçersiz token durumunda güvenlik bağlamını temizleyebiliriz
                }
            }
            filterChain.doFilter(request, response); // chain'e devam et - ilerletir
    }
}
