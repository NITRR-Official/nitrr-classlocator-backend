package com.classlocator.nitrr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.classlocator.nitrr.services.AdminUserDetailsImpl;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private AdminUserDetailsImpl adminUserDetailsImpl;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminUserDetailsImpl).passwordEncoder(passwordEncoder());
    }

    //This is used to store password in hashed format instead of plain text
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /*This is used to filter out which requests to be authenticated and which not, using SecurityFilterChain*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/check", "/getAllQueries", "/generate", "/map", "/download/**").permitAll()  // Publicly accessible routes
            .requestMatchers("/sadmin/**").hasRole("SUPER_ADMIN")    // Restricted to SUPER_ADMIN
            .requestMatchers("/admin/**").hasRole("ADMIN")           // Restricted to ADMIN
            .anyRequest().authenticated()                            // All other requests require authentication
        )
        .csrf(csrf -> csrf.disable())  // Disable CSRF (use with caution)
        .build();

    }
}

