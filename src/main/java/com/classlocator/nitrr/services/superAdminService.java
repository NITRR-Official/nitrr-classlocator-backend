package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.entity.trash;
import com.classlocator.nitrr.repository.queryRepo;
import com.classlocator.nitrr.repository.superAdminRepo;

@Service
public class superAdminService extends comService {

    @Autowired
    private superAdminRepo sAdmin;

    @Autowired
    private queryRepo queryR;

    public int saveUpdateSuperAdmin(superAdmin user) {
        try {
            List<superAdmin> check = sAdmin.findAll();
            if(check.size() > 1) return 0;
            sAdmin.save(user);
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
                int appStatus = updateSearchTool(temp);
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
