package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.trash;

@Service
public class adminService extends comService {

    public int saveUpdateNewAdmin(Map<String, String> user) {
        try {
            admin temp = adminRe.findByrollno(Integer.parseInt(user.get("rollno")));
            if(temp != null) {
                temp  = (admin) authorization(Integer.parseInt(user.get("rollno")),user.get("password")).get("admin");
                temp.setName(user.get("name"));
                temp.setDepartment(user.get("department"));
                if(user.get("new_pass") != null && !user.get("new_pass").isEmpty()) {
                    temp.setPassword(passwordEncoder.encode(user.get("new_pass")));
                }
                adminRe.save(temp);
                return 2; 
            }

            temp = new admin();
            temp.setRollno(Integer.parseInt(user.get("rollno")));
            temp.setName(user.get("name"));
            temp.setDepartment(user.get("department"));
            temp.setPassword(passwordEncoder.encode(user.get("password")));
            temp.setRoles(Arrays.asList("ADMIN"));
            adminRe.save(temp);
            return 1;
        } catch (NullPointerException e) {
            System.out.println(e.toString()); //To be added in logs
            return -1;
        } catch (NumberFormatException e) {
            System.out.println(e.toString()); //To be added in logs
            return -2;
        } catch (Exception e) {
            System.out.println(e.toString().hashCode()); //To be added in logs
            return -3;
        }
    }
    
    public int updateNewAdmin(Map<String, String> user) {
        try {
            admin temp = adminRe.findByrollno(Integer.parseInt(user.get("rollno")));
            if(temp != null){
                temp.setName(user.get("name"));
                temp.setDepartment(user.get("department"));
                temp.setPassword(passwordEncoder.encode(user.get("password")));
                adminRe.save(temp);
                return 2; 
            }
            return 0;
        } catch (NumberFormatException e) {
            System.out.println(e.toString()); //To be added in logs
            return -1;
        } catch (Exception e) {
            System.out.println(e.toString().hashCode()); //To be added in logs
            return -2;
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
