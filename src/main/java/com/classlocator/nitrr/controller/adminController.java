package com.classlocator.nitrr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.entity.query;
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

    @PostMapping("/raiseQuery/{id}")
    public ResponseEntity<String> raiseQuery(@RequestBody query q, @PathVariable("id") Integer id) {
        int status = admins.saveQuery(q,id,0);
        System.out.print(q.toString());
        if(status == 1) return new ResponseEntity<String>("Query raised.", HttpStatus.CREATED);
        else if(status == 0) return new ResponseEntity<String>("Not the authorized admin.", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    

    @PostMapping("/vote")
    public ResponseEntity<String> voting(@RequestParam("rollno") int rollno, @RequestParam("id") String id)
    {
        int status = admins.voting(rollno, id);
        if(status == 1) return new ResponseEntity<>("Vote successfully", HttpStatus.OK);
        else if(status == 0) return new ResponseEntity<>("Vote not applied", HttpStatus.BAD_REQUEST);
        else if(status == 2) return new ResponseEntity<>("Vote already applied.", HttpStatus.OK);
        return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
