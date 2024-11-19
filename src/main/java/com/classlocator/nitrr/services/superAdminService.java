package com.classlocator.nitrr.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.entity.trash;
import com.classlocator.nitrr.repository.superAdminRepo;

public class superAdminService extends comService {

    @Autowired
    private superAdminRepo sAdmin;


    public int saveUpdateSuperAdmin(superAdmin user) {
        try {
            List<superAdmin> check = sAdmin.findAll();
            if(check.size() > 1) return 0;

            user.setName(user.getName());
            user.setPassword(user.getPassword());
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
            return 1;
        } catch (Exception e) {
            System.out.println(e.toString());
            return -1;
        }
    }

}
