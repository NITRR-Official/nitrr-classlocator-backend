package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.entity.trash;

@Service
public class superAdminService extends comService {

    public int saveUpdateSuperAdmin(Map<String, String> user) {
        try {
            Optional<superAdmin> getSAdmin = sadminRe.findById(1);

            if(getSAdmin.isPresent()) {
                superAdmin sAdmin  = (superAdmin) authorization(1, user.get("password")).get("sadmin");
                sAdmin.setName(user.get("name"));
                if(user.get("new_pass") != null && !user.get("new_pass").isEmpty()) {
                    sAdmin.setPassword(passwordEncoder.encode(user.get("new_pass")));
                }
                sadminRe.save(sAdmin);
                return 0; 
            }

            superAdmin suser = new superAdmin();
            suser.setId(1);
            suser.setName(user.get("name"));
            suser.setPassword(passwordEncoder.encode(user.get("password")));
            suser.setRoles(Arrays.asList("SUPER_ADMIN"));
            sadminRe.save(suser);
            return 1;
        } catch (BadCredentialsException e) { 
            System.out.println(e.toString());
            return -1;
        } catch (NullPointerException e) {
            System.out.println(e.toString());
            return -2;
        }
        catch (Exception e) {
            System.out.println(e.toString());
            return -3;
        }
    }

    public int deleteSuperAdmin()
    {
        try {
            sadminRe.deleteById(1);
            return 1;
        } catch (Exception e) {
            System.out.println(e.toString()); //To be added in log file
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

            Optional<query> q = queryR.findById(new ObjectId(id));
            if (q.isPresent()) {
                query temp = q.get();
                if(!temp.isSuperAdmin())
                {
                    temp.setSuperAdmin(true);
                    int status = Integer.parseInt(temp.getRaisedBy());
                    int appStatus = status == 1 ? updateSearchTool(temp, true) : updateSearchTool(temp, false);
                    if(appStatus != 1) return appStatus;
                }
                else return 0;
                queryR.save(temp);
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return -3;
        }
    }

}
