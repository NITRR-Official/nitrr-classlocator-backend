package com.classlocator.nitrr.controller.Public;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.services.adminService;

//This will expose the admin endpoints to be used by the frontend
@RestController
@RequestMapping("/admin")
public class adminController {

    @Autowired
    private adminService admins;

    @PostMapping("/signup")
    public ResponseEntity<String> createUser(@RequestBody admin user) {
        int status = admins.saveUpdateNewAdmin(user);
        if(status == 1) return new ResponseEntity<String>("Admin verification was successful", HttpStatus.CREATED);
        else if(status == 2) return new ResponseEntity<String>("Admin details updated.", HttpStatus.OK);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
