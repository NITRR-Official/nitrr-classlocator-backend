package com.classlocator.nitrr.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.trash;

@Service
public class adminService extends comService {

    private static final Logger logger = LoggerFactory.getLogger(adminService.class);

    private static final String ROLL_NO = "rollno";
    private static final String DEPT = "department";
    /**
     * Saves or updates an admin user based on roll number;
     * returns 1 for new admin, 2 for update, and negative values for errors.
     * 
     * @return: int
     */
    public int saveUpdateNewAdmin(Map<String, String> user) {
        try {
            int rollno = Integer.parseInt(user.get(ROLL_NO));
            if (rollno == 1)
                throw new NullPointerException();
            admin temp = adminRe.findByrollno(rollno);
            if (temp != null) {
                temp = (admin) authorization(rollno, user.get(PASSWORD)).get(SMALL_ROLES[1]);
                temp.setName(user.get(NAME));
                temp.setDepartment(user.get(DEPT));
                if (user.get(NEW_PASS) != null && !user.get(NEW_PASS).isEmpty()) {
                    temp.setPassword(passwordEncoder.encode(user.get(NEW_PASS)));
                }
                adminRe.save(temp);
                return 2;
            }

            temp = new admin();
            temp.setRollno(rollno);
            temp.setName(user.get(NAME));
            temp.setDepartment(user.get(DEPT));
            temp.setPassword(passwordEncoder.encode(user.get(PASSWORD)));
            temp.setRoles(Arrays.asList(ROLES[1]));
            adminRe.save(temp);
            return 1;
        } catch (BadCredentialsException e) {
            logger.error("Invalid roll number or password {} :", user.get(ROLL_NO), e);
            return -1;
        } catch (NullPointerException e) {
            logger.error("User {} not authorized",user.get(ROLL_NO), e);
            return -1;
        } catch (NumberFormatException e) {
            logger.error("Invalid roll number {}", user.get(ROLL_NO), e);
            return -2;
        } catch (Exception e) {
            logger.error("Internal server error by {}",user.get(ROLL_NO), e);
            return -3;
        }
    }

    /** For future updates */
    public Set<trash> adminTrash(Integer rollno) {
        try {
            admin a = adminRe.findByrollno(rollno);
            return a.getTrashedQueries();
        } catch (Exception e) {
            logger.error("Internal Server error by {}",rollno, e);
            return new HashSet<>();
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
                        logger.warn("Query {} already voted by user {}", id, roll);
                        return 0;
                    }
                } else {
                    logger.warn("Self-voting by {} for query {}", roll, id);
                    return -1;
                }

                logger.info("Query {} voted by user {}", id, roll);
                queryR.save(temp);
                return 1;
            } else {
                logger.warn("Query {} not found for voting by {}.", id, roll);
                return -2;
            }
        } catch (Exception e) {
            logger.error("Internal server error by {} for query {} :", roll, id, e);
            return -3;
        }
    }

}
