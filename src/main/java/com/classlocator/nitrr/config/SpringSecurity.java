package com.classlocator.nitrr.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.classlocator.nitrr.services.UserDetailsImpl;
// import com.classlocator.nitrr.services.SuperAdminUserDetailsImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private UserDetailsImpl adminUserDetailsImpl;

    @Autowired
    private jwtFilter jwtfilter;

    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**", "/sadmin/**", "/check", "/getAllQueries", "/map",
                                "/download/**")
                        .permitAll()
                        .requestMatchers("/requests/**", "/generate").hasRole("SUPER_ADMIN")
                        .requestMatchers("/request/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtfilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(e -> e.authenticationEntryPoint(customAuthenticationEntryPoint))
                .csrf(csrf -> csrf.disable())
                .httpBasic(https -> {
                });

        return http.build();
    }

    public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response,
                AuthenticationException authException) throws IOException {
                    
            String authHeader = request.getHeader("Authorization");
            int httpResponse = HttpServletResponse.SC_BAD_REQUEST;
            String error = "No Auth used";

            if (authHeader != null) {
                if (authHeader.startsWith("Bearer ")) {
                    httpResponse = HttpServletResponse.SC_NOT_ACCEPTABLE;
                    error = "Invalid Token";
                } else if (authHeader.startsWith("Basic ")) {
                    httpResponse = HttpServletResponse.SC_UNAUTHORIZED;
                    error = "Invalid Username or Password";
                }
            }

            response.setContentType("application/json");
            response.setStatus(httpResponse);
            response.getWriter().write(String.format("{\"error\":\"%s\"}", error));
            response.getWriter().flush();
        }
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.userDetailsService(adminUserDetailsImpl).passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}