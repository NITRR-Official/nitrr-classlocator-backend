package com.classlocator.nitrr.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.trash;

@Service
public class adminService extends comService {

    /**
     * Saves or updates an admin user based on roll number;
     * returns 1 for new admin, 2 for update, and negative values for errors.
     * 
     * @return: int
     */
    public int saveUpdateNewAdmin(Map<String, String> user) {
        try {
            int rollno = Integer.parseInt(user.get("rollno"));
            if (rollno == 1)
                throw new NullPointerException();
            admin temp = adminRe.findByrollno(rollno);
            if (temp != null) {
                temp = (admin) authorization(rollno, user.get("password")).get("admin");
                temp.setName(user.get("name"));
                temp.setDepartment(user.get("department"));
                if (user.get("new_pass") != null && !user.get("new_pass").isEmpty()) {
                    temp.setPassword(passwordEncoder.encode(user.get("new_pass")));
                }
                adminRe.save(temp);
                return 2;
            }

            temp = new admin();
            temp.setRollno(rollno);
            temp.setName(user.get("name"));
            temp.setDepartment(user.get("department"));
            temp.setPassword(passwordEncoder.encode(user.get("password")));
            temp.setRoles(Arrays.asList("ADMIN"));
            adminRe.save(temp);
            return 1;
        } catch (BadCredentialsException e) {
            System.out.println(e.toString());
            return -1;
        } catch (NullPointerException e) {
            System.out.println(e.toString()); // To be added in logs
            return -1;
        } catch (NumberFormatException e) {
            System.out.println(e.toString()); // To be added in logs
            return -2;
        } catch (Exception e) {
            System.out.println(e.toString()); // To be added in logs
            return -3;
        }
    }

    /** For future updates */
    public HashSet<trash> adminTrash(Integer rollno) {
        try {
            admin a = adminRe.findByrollno(rollno);
            return a.getTrashedQueries();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new HashSet<trash>();
        }
    }

    /**
     * Handles voting on a query; returns 1 for a successful vote, 0 if already
     * voted,
     * -1 if self-voting, and negative values for errors.
     * 
     * @return int
     */
    public int voting(int roll, String id) {
        try {
            Optional<query> q = queryR.findById(new ObjectId(id));
            if (q.isPresent()) {
                query temp = q.get();
                int same = Integer.parseInt(temp.getRaisedBy());
                if (roll != same) {
                    if (!temp.getVotes().containsKey(roll)) {
                        temp.getVotes().put(roll, true);
                    } else {
                        return 0;
                    }
                } else {
                    return -1;
                }
                queryR.save(temp);
                return 1;
            } else {
                return -2;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return -3;
        }
    }

}
