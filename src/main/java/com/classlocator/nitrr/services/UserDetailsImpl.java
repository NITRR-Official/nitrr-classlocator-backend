package com.classlocator.nitrr.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.superAdmin;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserDetailsImpl extends comService implements UserDetailsService {
    /**
     * Loads a user by their roll number and returns UserDetails.
     * 
     * @param rollno String - The roll number of the user to be loaded.
     * @return UserDetails - Returns the user details if found in either
     *         superAdmin or admin repository.
     * @throws UsernameNotFoundException - Thrown if the roll number does not match
     *                                   any user in the system.
     */

    @Override
    public UserDetails loadUserByUsername(String rollno) throws UsernameNotFoundException {

        /**
         * UserDetails - Returns the constructed UserDetails object containing username,
         * password, and roles
         * for super admin
         */
        Optional<superAdmin> suser = sadminRe.findById(Integer.parseInt(rollno));

        /**
         * UserDetails - Returns the constructed UserDetails object containing username,
         * password, and roles
         * for admin
         */
        admin user = adminRe.findByrollno(Integer.parseInt(rollno));

        if (suser.isPresent() && suser.get().isActive()) {
            log.info("Super Admin requested: " + suser.get().getId());
            return User.builder().username(suser.get().getId().toString()).password(suser.get().getPassword())
                    .roles(suser.get().getRoles().toArray(new String[0]))
                    .build();
        } else if (user != null) {
            log.info("Admin requested: " + user.getRollno());
            return User.builder().username(user.getRollno().toString()).password(user.getPassword())
                    .roles(user.getRoles().toArray(new String[0]))
                    .build();
        }

        throw new UsernameNotFoundException("User roll number not found: " + rollno);
    }

}
