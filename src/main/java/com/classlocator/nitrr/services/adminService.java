package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.trash;
import com.classlocator.nitrr.repository.adminRepo;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.classlocator.nitrr.repository.queryRepo;

@Service
public class adminService extends comService {
    @Autowired
    private adminRepo adminRe;

    @Autowired
    private queryRepo queryR;

    // private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public int saveUpdateNewAdmin(admin user) {
        try {
            admin temp = adminRe.findByrollno(user.getRollno());
            if(temp != null) return 0;

            user.setName(user.getName());
            user.setDepartment(user.getDepartment());
            user.setRollno(user.getRollno());
            user.setPassword(user.getPassword());
            adminRe.save(user);
            return 1;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    public List<trash> adminTrash(Integer rollno)
    {
        try {
            admin a = adminRe.findByrollno(rollno);
            return a.getTrashedQueries();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<trash>();
        }
    }    

    public int voting(String id, int roll)
    {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<query> q = queryR.findById(objectId);
            if (q.isPresent()) {
                query temp = q.get();
                // temp.setVotes(temp.getVotes()+1);
                if(!temp.getVotes().containsKey(roll)) temp.getVotes().put(roll,true);
                else return 2;
                isApproved(temp);
                queryR.save(temp);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }

}
