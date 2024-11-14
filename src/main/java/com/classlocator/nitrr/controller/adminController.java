package com.classlocator.nitrr.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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


    @GetMapping("/check")
    public String check(){
        return "Yeah..., the setup is running...";
    }

    @PostMapping("/signup")
    public void createUser(@RequestBody admin user) {
        admins.saveNewAdmin(user);
    }

    @PostMapping("/raiseQuery/{id}")
    public String raiseQuery(@RequestBody query q, @PathVariable("id") Integer id) {
        System.out.print(q.toString());
        admins.saveQuery(q,id);
        return "Admin ID: " + id;
    }
}
