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

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.services.adminService;

//This will expose the admin endpoints to be used by the frontend
@RestController
@RequestMapping("/admin")
public class adminController extends controller {

    @Autowired
    private adminService admins;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody admin user) {
        int status = admins.saveUpdateNewAdmin(user);
        if (status == 1) {
            Map<String, String> mp = new HashMap<String, String>();
            mp.put("Admin verification was successful", jwt.generateToken(user.getRollno().toString(),
                    user.getName(),
                    user.getDepartment(), "ADMIN"));
            return new ResponseEntity<Map<String, String>>(mp, HttpStatus.CREATED);
        } else if (status == 2)
            return new ResponseEntity<String>("Admin details updated.", HttpStatus.OK);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
