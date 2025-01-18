package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.entity.trash;

@Service
public class superAdminService extends comService {

    public int saveUpdateSuperAdmin(superAdmin user) {
        try {
            List<superAdmin> check = sadminRe.findAll();
            if(check.size() > 1 && (check.get(0).getId() == 1 || check.get(1).getId() == 1)) return 0;
            user.setId(1);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("SUPER_ADMIN"));
            sadminRe.save(user);
            return 1;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    public int deleteSuperAdmin()
    {
        try {
            sadminRe.deleteById(1);
            return 1;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }

    public List<trash> sAdminTrash(Integer rollno)
    {
        try {
            //Logic to get sAdminTrash is applied here
            return new ArrayList<>();
        } catch (Exception e) {
            System.out.println(e.toString());
            return new ArrayList<trash>();
        }
    }

    public int approval(String id)
    {
        try {
            //Direct rejection/approval is handled here

            //IsAuthorized or not is handled here

            ObjectId objectId = new ObjectId(id);
            Optional<query> q = queryR.findById(objectId);
            if (q.isPresent()) {
                query temp = q.get();
                if(!temp.isSuperAdmin())
                    temp.setSuperAdmin(true);
                else return -1;

                int appStatus = Integer.parseInt(temp.getRaisedBy().toString()) == 1 ? updateSearchTool(temp, true) : updateSearchTool(temp, false);
                System.out.println("After approval"+temp);
                if(appStatus == -1) return -2;
                queryR.save(temp);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return -2;
        }
    }

}
