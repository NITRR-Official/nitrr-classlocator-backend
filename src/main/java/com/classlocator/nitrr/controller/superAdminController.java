package com.classlocator.nitrr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.services.superAdminService;

//This will expose the super admin endpoints to be used by the frontend to be developed by other team members
@RestController
@RequestMapping("/sadmin")
public class superAdminController {

    @Autowired
    private superAdminService sadmins;

    //Special query raise system for super admin to be created here
    

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody superAdmin suser) {
        int status = sadmins.saveUpdateSuperAdmin(suser);
        if(status == 1) return new ResponseEntity<String>("Super Admin created", HttpStatus.CREATED);
        else if(status == 0) return new ResponseEntity<String>("Super Admin enabled.", HttpStatus.OK);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/approve")
    public ResponseEntity<String> voting(@RequestParam("id") String id)
    {
        int status = sadmins.approval(id);
        if(status == 1) return new ResponseEntity<>("Approved", HttpStatus.OK);
        else if(status == 0) return new ResponseEntity<>("Query not found", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
