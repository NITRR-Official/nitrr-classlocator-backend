package com.classlocator.nitrr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.services.adminService;

@RestController
public class controller {

    @Autowired
    private adminService admins;

    @GetMapping("/check")
    public ResponseEntity<String> check(){
        return new ResponseEntity<String>("Yeah..., the setup is running...", HttpStatus.OK);
    }

    @GetMapping("/getAllQueries")
    public ResponseEntity<?> getAllQueries() {
        List<query> all = admins.getAllQueries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateMap()
    {
        boolean status = admins.searchTools();
        if(status) {
            return new ResponseEntity<>("JSON to Map generation was sucessful", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Something went wrong...", HttpStatus.BAD_GATEWAY);
    }

}
