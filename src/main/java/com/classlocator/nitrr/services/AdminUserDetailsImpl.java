package com.classlocator.nitrr.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.classlocator.nitrr.entity.admin;

@Component
public class AdminUserDetailsImpl extends comService implements UserDetailsService  {

    @Override
    public UserDetails loadUserByUsername(String rollno) throws UsernameNotFoundException {

        admin user = adminRe.findByrollno(Integer.parseInt(rollno));

        if(user != null)
        {
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder().
                username(user.getRollno().toString()).
                password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
            
            return userDetails;
        }

        throw new UsernameNotFoundException("User roll number not found: " + rollno);
    }
    
}
