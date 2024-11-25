package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.trash;

@Service
public class adminService extends comService {

    // private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public int saveUpdateNewAdmin(admin user) {
        try {
            admin temp = adminRe.findByrollno(user.getRollno());
            if(temp != null){
                temp.setName(user.getName());
                temp.setDepartment(user.getDepartment());
                temp.setPassword(user.getPassword());
                adminRe.save(temp);
                return 2;
            }
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

    public int voting(int roll, String id)
    {
        try {
            System.out.println(roll+" "+id);
            ObjectId objectId = new ObjectId(id);
            Optional<query> q = queryR.findById(objectId);
            if (q.isPresent() && (adminRe.findByrollno(roll) != null)) {
                query temp = q.get();
                if(temp.getVotes() == null) {
                    temp.setVotes(new HashMap<Integer, Boolean>());
                }
                if(!temp.getVotes().containsKey(roll) && (roll != Integer.parseInt(temp.getRaisedBy()))) 
                temp.getVotes().put(roll,true);
                else return 2;
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
