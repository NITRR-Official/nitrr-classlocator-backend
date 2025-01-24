package com.classlocator.nitrr.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.repository.adminRepo;
import com.classlocator.nitrr.repository.superAdminRepo;

@Component
public class UserDetailsImpl implements UserDetailsService  {

    @Autowired
    private adminRepo adminRe;

    @Autowired
    private superAdminRepo sadminRe;

    private UserDetails helper(admin user){
        UserDetails userDetails = User.builder().
                username(user.getRollno().toString()).
                password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
            
            return userDetails;
    }

    private UserDetails helper(superAdmin user){
        UserDetails userDetails = User.builder().
                username(user.getId().toString()).
                password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();
            
            return userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String rollno) throws UsernameNotFoundException {

        System.out.println("From admin checking : "+rollno);

        admin user = adminRe.findByrollno(Integer.parseInt(rollno));
        Optional<superAdmin> suser = sadminRe.findById(Integer.parseInt(rollno));

        if(user != null)
        {
            return helper(user);
        }
        else if (suser.isPresent()) return helper(suser.get());

        throw new UsernameNotFoundException("User roll number not found: " + rollno);
    }
    
}
