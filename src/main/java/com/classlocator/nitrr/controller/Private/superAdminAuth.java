package com.classlocator.nitrr.controller.Private;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.services.superAdminService;

//This will expose the super admin endpoints to be used by the frontend to be developed by other team members
@RestController
@RequestMapping("/requests")
public class superAdminAuth {

    @Autowired
    private superAdminService sadmins;


    //Special query raise system for super admin to be created here

    @DeleteMapping("/remove")
    public ResponseEntity<String> deactivateAdmin(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated())
        {
            sadmins.deleteSuperAdmin();
            return new ResponseEntity<String>("Super Admin deactivated", HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>("Failed to deactivate super admin...", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/raiseQuery")
    public ResponseEntity<String> raiseQuery(@RequestBody query q) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth.isAuthenticated()){
            int status = sadmins.saveQuery(q,0);
            if(status == 1) return new ResponseEntity<String>("Query raised.", HttpStatus.CREATED);
            else return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<String>("Not the authorized admin.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<String> voting(@RequestParam("id") String id) {
        int status = sadmins.approval(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(auth.isAuthenticated()){
            if(status == 1) return new ResponseEntity<>("Approved", HttpStatus.OK);
            else if(status == 0) return new ResponseEntity<>("Query not found", HttpStatus.BAD_REQUEST);
            else if(status == -1) return new ResponseEntity<>("Already approved", HttpStatus.OK);
            return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<String>("Not the authorized super admin.", HttpStatus.UNAUTHORIZED);
        }
    }

}
