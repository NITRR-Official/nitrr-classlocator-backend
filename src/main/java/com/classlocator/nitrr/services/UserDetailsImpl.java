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

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserDetailsImpl implements UserDetailsService {

    @Autowired
    private adminRepo adminRe;

    @Autowired
    private superAdminRepo sadminRe;

    /**
     * Helper method to convert an admin user into UserDetails.
     * 
     * @param user admin - The admin user whose details are to be converted.
     * @return UserDetails - Returns the constructed UserDetails object
     *         containing username, password, and roles.
     */

    private UserDetails helper(admin user) {
        UserDetails userDetails = User.builder().username(user.getRollno().toString()).password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();

        return userDetails;
    }

    /**
     * Helper method to convert a superAdmin user into UserDetails.
     * 
     * @param user superAdmin - The super admin user whose details
     *             are to be converted.
     * @return UserDetails - Returns the constructed UserDetails object
     *         containing username, password, and roles.
     */

    private UserDetails helper(superAdmin user) {
        UserDetails userDetails = User.builder().username(user.getId().toString()).password(user.getPassword())
                .roles(user.getRoles().toArray(new String[0]))
                .build();

        return userDetails;
    }

    /**
     * Loads a user by their roll number and returns UserDetails.
     * 
     * @param rollno String - The roll number of the user to be loaded.
     * @return UserDetails - Returns the user details if found in either
     *         superAdmin or admin repository.
     * @throws UsernameNotFoundException - Thrown if the roll number does not match any user in the system.
     */

    @Override
    public UserDetails loadUserByUsername(String rollno) throws UsernameNotFoundException {

        Optional<superAdmin> suser = sadminRe.findById(Integer.parseInt(rollno));
        admin user = adminRe.findByrollno(Integer.parseInt(rollno));

        if (suser.isPresent()) {
            log.info("Super Admin requested: " + suser.get().getId());
            return helper(suser.get());
        } else if (user != null) {
            log.info("Admin requested: " + user.getRollno());
            return helper(user);
        }

        throw new UsernameNotFoundException("User roll number not found: " + rollno);
    }

}
