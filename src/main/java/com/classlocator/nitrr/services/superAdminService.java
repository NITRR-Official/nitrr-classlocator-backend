package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.entity.trash;

public class superAdminService  {
    public boolean authorization(Integer rollno)
    {
        //This function will authorize whether the user is legit or not by comparing passwords, authentication etc.
        return false;
    }

    public int saveSuperAdmin(superAdmin user) {
        try {
            admin temp = findByRollno(user.getRollno());
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

    public admin findByRollno(Integer rollno) {
        return adminRe.findByrollno(rollno);
    }

    public List<query> adminQueries(Integer rollno, Integer type)
    {
        try {
            admin a = findByRollno(rollno);
            if(type == 1)
                return a.getPendingQueries();
            else
                return a.getAcceptedQueries();    
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<query>();
        }
    }

    public List<trash> adminTrash(Integer rollno)
    {
        try {
            admin a = findByRollno(rollno);
            return a.getTrashedQueries();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<trash>();
        }
    }

    public List<query> getAllQueries(){
        return queryR.findAll();
    }

    public void isApproved(query q)
    {
        
    }

    public int voting(String id)
    {
        try {
            ObjectId objectId = new ObjectId(id);
            Optional<query> q = queryR.findById(objectId);
            if (q.isPresent()) {
                query temp = q.get();
                temp.setVotes(temp.getVotes()+1);
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
