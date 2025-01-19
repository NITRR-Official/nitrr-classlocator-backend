package com.classlocator.nitrr.controller.Public;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.services.superAdminService;

//This will expose the super admin endpoints to be used by the frontend to be developed by other team members
@RestController
@RequestMapping("/sadmin")
public class superAdminController extends controller {

    @Autowired
    private superAdminService sadmins;

    //Special query raise system for super admin to be created here
    

    @PostMapping("/signup")
    public ResponseEntity<?> activateAdmin(@RequestBody superAdmin suser) {
        int status = sadmins.saveUpdateSuperAdmin(suser);
        if(status == 1) {
            Map<String, String> mp = new HashMap<String, String>();
            mp.put("Super admin activated: ", jwt.generateToken(suser.getId().toString(),suser.getName(),"NIT Raipur", "SUPER_ADMIN"));
            return new ResponseEntity<Map<String, String>>(mp, HttpStatus.CREATED);
        } 
        else if(status == 0) return new ResponseEntity<String>("Super Admin enabled.", HttpStatus.OK);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
