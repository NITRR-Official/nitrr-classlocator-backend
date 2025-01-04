package com.classlocator.nitrr.controller.Public;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.query;
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
    public ResponseEntity<String> activateAdmin(@RequestBody superAdmin suser) {
        int status = sadmins.saveUpdateSuperAdmin(suser);
        if(status == 1) return new ResponseEntity<String>("Super Admin created", HttpStatus.CREATED);
        else if(status == 0) return new ResponseEntity<String>("Super Admin enabled.", HttpStatus.OK);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> deactivateAdmin(){
        int status = sadmins.deleteSuperAdmin();
        if(status == 1) return new ResponseEntity<String>("Super Admin deactivated", HttpStatus.NO_CONTENT);
        return new ResponseEntity<>("Failed to deactivate super admin...", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/raiseQuery")
    public ResponseEntity<String> raiseQuery(@RequestBody query q) {
        System.out.print("From super admin: " + q.toString());
        int status = sadmins.saveQuery(q,0);
        if(status == 1) return new ResponseEntity<String>("Query raised.", HttpStatus.CREATED);
        else if(status == 0) return new ResponseEntity<String>("Not the authorized admin.", HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/approve")
    public ResponseEntity<String> voting(@RequestParam("id") String id) {
        int status = sadmins.approval(id);
        if(status == 1) return new ResponseEntity<>("Approved", HttpStatus.OK);
        else if(status == 0) return new ResponseEntity<>("Query not found", HttpStatus.BAD_REQUEST);
        else if(status == -1) return new ResponseEntity<>("Vote already applied", HttpStatus.OK);
        return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
