package com.classlocator.nitrr.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.classlocator.nitrr.interfaces.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.searchTool;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.repository.adminRepo;
import com.classlocator.nitrr.repository.queryRepo;
import com.classlocator.nitrr.repository.searchToolRepo;
import com.classlocator.nitrr.repository.superAdminRepo;

@Service
public class comService {
    // here isapproved, getAllQueries, saveQuery, authorization

    @Autowired
    private adminRepo adminRe;

    @Autowired
    private queryRepo queryR;

    @Autowired
    private superAdminRepo sadminRe;

    @Autowired
    private searchToolRepo search;

    // The rollBack/deleteTrash/recoverQueries/rejectQueries to be applied later

    public boolean rollBack(query q, boolean isAdmin, int rollno) {
        // Logic applied to rollback a query if that query is latest to that particular
        // room then reject that query
        // to trash
        return false;
    }

    public boolean deleteTrash(boolean isAdmin, int rollno) {
        // Logic is applied here to delete the already present trash by admin or
        // superadmin
        return false;
    }

    public boolean recoverQueries(boolean isAdmin, int rollno) {
        // Logic is applied here to recover the deleted queries
        return false;
    }

    public boolean rejectQueries(boolean isAdmin, int rollno) {
        // Logic to move the queries to trash is applied here
        return false;
    }

    // The below functionality is to be applied as soon as possible

    protected int updateSearchTool(query q) {
        // After approval this function will update the searchTool in mongodb and will
        // update the maps
        try {
            Optional<searchTool> room = search.findById(q.getRoomid());
            searchTool temp;
            if(room.isPresent())
                temp = room.get();
            else
                temp = new searchTool();
            
            if(temp.getData() == null)
                temp.setData(new ArrayList<Pair<String, Pair<String, String>>>());
            Pair<String, String> t = new Pair<String, String>(q.getName(), q.getDescription());
            temp.getData().add(new Pair<String, Pair<String, String>>(q.getId().toString(), t));
            temp.setId(q.getRoomid());
            search.save(temp);
            return 1;
        } catch (Exception e) {
            System.out.println("Exception raised from update search tool: "+e.toString());
            return -1;
        }

    }

    public List<query> getAllQueries() {
        return queryR.findAll();
    }

    public boolean authorization(Integer rollno) {
        // This function will authorize whether the user is legit or not by comparing
        // passwords, authentication etc.
        return false;
    }

    public List<query> Queries(Integer rollno, Integer type) {
        try {
            admin a = adminRe.findByrollno(rollno);
            if (type == 1)
                return a.getPendingQueries();
            else
                return a.getAcceptedQueries();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<query>();
        }
    }

    public List<query> Queries(Integer type) {
        try {
            // Logic required to get all sadmin queries
            return new ArrayList<>();
            // if(type == 1)
            // return a.getPendingQueries();
            // else
            // return a.getAcceptedQueries();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<query>();
        }
    }

    private query processQuery(query q, Integer s)
    {
        try {
            q.setSuperAdmin(false);
            q.setRaisedBy(s.toString());

            // Date system
            LocalDateTime currentDate = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = currentDate.format(formatter);
            q.setDate(formattedDate);
            return queryR.save(q);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.toString());
            return null;
        }
    }
    
    public int saveQuery(query q)
    {
        try {
            // admin user = adminRe.findByrollno(s);
            System.out.println(q.toString());
            List<superAdmin> suser = sadminRe.findAll();
            if (suser.isEmpty())
                return 0;

            // Getting the admin data and setting the arraylist and updating it...

            query temp = processQuery(q, 1);
            if(temp != null)
            {
                for (superAdmin ele : suser) {
                    if(temp != null) {
                        ele.getPendingQueries().add(temp);
                    }
                    sadminRe.save(ele);
                }
            }  
            else return -1;
            return 1;
        } catch (Exception e) {
            System.out.print(e.toString());
            return -1;
        }
    }
    
    public int saveQuery(query q, Integer s) {
        try {
            admin user = adminRe.findByrollno(s);
            
            if (user == null)
                return 0;

            // Getting the admin data and setting the arraylist and updating it...
            query temp = processQuery(q, s);
            if(temp != null) user.getPendingQueries().add(temp); 
            else return -1;
            adminRe.save(user);
            return 1;
        } catch (Exception e) {
            System.out.print(e.toString());
            return -1;
        }
    }

}
