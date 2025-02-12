package com.classlocator.nitrr.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.classlocator.nitrr.services.UserDetailsImpl;
import com.classlocator.nitrr.services.jwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JWT Filter for processing authentication requests.
 * 
 * This filter intercepts HTTP requests and checks for a JWT token in the
 * Authorization header. If a valid token is found, it extracts the roll number
 * and authenticates the user in the Spring Security context.
 * 
 * This class extends {@link OncePerRequestFilter} to ensure that the filter
 * executes once per request.
 */
@Component
public class jwtFilter extends OncePerRequestFilter {

    @Autowired
    private jwtService jwt;

    @Autowired
    private ApplicationContext context;

    /**
     * Filters incoming HTTP requests and processes JWT authentication.
     * 
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain to continue processing.
     * @throws ServletException If a servlet error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT token from Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String rollno = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            rollno = jwt.extractRoll(token);
        }

        // Authenticate user if roll number is extracted and no prior authentication
        // exists
        if (rollno != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user details from database
            UserDetails userDetails = context.getBean(UserDetailsImpl.class).loadUserByUsername(rollno);

            // Validate JWT token
            if (jwt.validateToken(token, userDetails)) {
                // Create authentication token and set in SecurityContext
                UsernamePasswordAuthenticationToken ptoken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                ptoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(ptoken);
            }
        }

        // Continue the request processing
        filterChain.doFilter(request, response);
    }
}
