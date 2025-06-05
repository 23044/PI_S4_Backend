package com.example.backend.jwtModule.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.backend.jwtModule.services.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private MyUserDetailsService userDetailsService;



@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                System.out.println("[JwtFilter] Incoming request to: " + request.getRequestURI());
        String authHeader = request.getHeader("Authorization");

        
        if (authHeader == null) {
            System.out.println("[JwtFilter] No Authorization header found. Checking for cookie...");
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                System.out.println("[JwtFilter] Cookies found. Searching for Authorization cookie...");
                for (Cookie cookie : cookies) {
                    if ("Authorization".equals(cookie.getName())) {
                        System.out.println("[JwtFilter] Authorization cookie found.");
                        authHeader = cookie.getValue();
                        break;
                    }
                }
            }
        }

        
        if (authHeader != null) {
            String token = authHeader;  
            String username = jwtUtil.extractUsername(token);

            System.out.println("[JwtFilter] Token found. Username: " + username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    System.out.println("[JwtFilter] Invalid token.");
                }
            }
        } else {
            System.out.println("[JwtFilter] No valid Authorization header or cookie found.");
        }

        filterChain.doFilter(request, response);
    }


}

