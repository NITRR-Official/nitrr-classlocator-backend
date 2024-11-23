package com.classlocator.nitrr.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.searchTool;
import com.classlocator.nitrr.repository.adminRepo;
import com.classlocator.nitrr.repository.queryRepo;
import com.classlocator.nitrr.repository.searchToolRepo;

@Service
public class comService {
    // here isapproved, getAllQueries, saveQuery, authorization

    @Autowired
    private adminRepo adminRe;

    @Autowired
    private queryRepo queryR;

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
            Optional<searchTool> s = search.findById(q.getRoomid());
            if (s.isPresent()) {
                searchTool temp = s.get();
                if (temp.getData().containsKey(q) || temp.getData().containsKey(q))
                    return -1;
                else {
                    temp.getData().put(q.getId().toString(), Pair.of(q.getName(), q.getDescription()));
                }
                search.save(temp);
                return 1;
            } else {
                searchTool temp = new searchTool();
                temp.setData(new HashMap<String, Pair<String, String>>());
                if (!temp.getData().containsKey(q.getId().toString()))
                    temp.getData().put(q.getId().toString(), Pair.of(q.getName(), q.getDescription()));

                temp.setId(q.getRoomid());
                search.save(temp);
                return 1;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
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

    public int saveQuery(query q, Integer s) {
        try {
            admin user = adminRe.findByrollno(s);

            if (user == null)
                return 0;

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

            // Getting the admin data and setting the arraylist and updating it...

            user.getPendingQueries().add(temp);
            adminRe.save(user);
            return 1;
        } catch (Exception e) {
            System.out.print(e.toString());
            return -1;
        }
    }

}
