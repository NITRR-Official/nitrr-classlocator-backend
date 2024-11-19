package com.classlocator.nitrr.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.repository.adminRepo;
import com.classlocator.nitrr.repository.queryRepo;

public class comService {
    //here isapproved, getAllQueries, saveQuery, authorization

    @Autowired
    private adminRepo adminRe;

    @Autowired
    private queryRepo queryR;

    public boolean deleteTrash(boolean isAdmin, int rollno)
    {
        //Logic is applied here to delete the already present trash by admin or superadmin 
        return false;
    }

    public boolean recoverQueries(boolean isAdmin, int rollno)
    {
        //Logic is applied here to recover the deleted queries
        return false;
    }

    public boolean rejectQueries(boolean isAdmin, int rollno) {
        //Logic to move the queries to trash is applied here
        return false;
    }

    public List<query> getAllQueries(){
        return queryR.findAll();
    }

    protected void isApproved(query q)
    {
        
    }

    public boolean authorization(Integer rollno)
    {
        //This function will authorize whether the user is legit or not by comparing passwords, authentication etc.
        return false;
    }

    public List<query> Queries(Integer rollno, Integer type)
    {
        try {
            admin a = adminRe.findByrollno(rollno);
            if(type == 1)
                return a.getPendingQueries();
            else
                return a.getAcceptedQueries();    
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<query>();
        }
    }

    public List<query> Queries(Integer type)
    {
        try {
            //Logic required to get all sadmin queries
            return new ArrayList<>();
            // if(type == 1)
            //     return a.getPendingQueries();
            // else
            //     return a.getAcceptedQueries();    
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<query>();
        }
    }

    public int saveQuery(query q, Integer s) {
        try {
            admin user = adminRe.findByrollno(s);

            if(user == null) return 0;
            
            q.setName(q.getName());
            q.setDescription(q.getDescription());
            // System.out.println(q.getRoomid());
            q.setRoomid(q.getRoomid());
            q.setSuperAdmin(false);
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
            return 1;
        } catch (Exception e) {
            System.out.print(e.toString());
            return -1;
        }
    }


}
