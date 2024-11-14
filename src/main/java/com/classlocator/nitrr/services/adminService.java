package com.classlocator.nitrr.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.repository.adminRepo;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.classlocator.nitrr.repository.queryRepo;

@Service
public class adminService {
    @Autowired
    private adminRepo adminRe;

    @Autowired
    private queryRepo queryR;

    // private static final PasswordEncoder passwordEncoder = new
    // BCryptPasswordEncoder();

    public boolean saveNewAdmin(admin user) {
        try {
            user.setName(user.getName());
            user.setDepartment(user.getDepartment());
            user.setRollno(user.getRollno());
            user.setPassword(user.getPassword());
            adminRe.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public admin findByRollno(Integer rollno) {
        return adminRe.findByrollno(rollno);
    }

    public boolean saveQuery(query q, Integer s) {
        try {
            admin user = findByRollno(s);

            if(user == null) throw new Exception("User not found");
            
            q.setName(q.getName());
            q.setDescription(q.getDescription());
            // System.out.println(q.getRoomid());
            q.setRoomid(q.getRoomid());
            q.setSuperAdmin(false);
            q.setVotes(0);
            q.setRaisedBy(s.toString());

            // Date system
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = currentDate.format(formatter);
            q.setDate(formattedDate);
            query temp = queryR.save(q);

            //Getting the admin data and setting the arraylist and updating it...
            
            user.getPendingQueries().add(temp);
            adminRe.save(user);            
            return true;
        } catch (Exception e) {
            System.out.print(e.toString());
            return false;
        }
    }
}
