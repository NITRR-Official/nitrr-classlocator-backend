package com.classlocator.nitrr.controller.Private;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.services.adminService;

//This will expose the admin endpoints to be used by the frontend
@RestController
@RequestMapping("/request")
public class adminAuth {

    @Autowired
    private adminService admins;

    @PostMapping("/raiseQuery")
    public ResponseEntity<String> raiseQuery(@RequestBody Map<String, String> q) {

        // We don't require password or id, as it come here only if it is authenticated.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            int rollno = Integer.parseInt(authentication.getName());
            int status = admins.saveQuery(q, rollno, 0);
            if (status == 1) return new ResponseEntity<String>("Query raised.", HttpStatus.CREATED);
            else if(status == -1) return new ResponseEntity<String>("Invalid room id", HttpStatus.CONFLICT);
            else if(status == -2) return new ResponseEntity<String>("Name or description not provided", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid roll no", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/vote")
    public ResponseEntity<String> voting(@RequestParam("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.isAuthenticated()) {
            int status = admins.voting(Integer.parseInt(authentication.getName()), id);
            if (status == 1)
                return new ResponseEntity<>("Vote successfully", HttpStatus.OK);
            else if (status == 0)
                return new ResponseEntity<>("Vote not applied", HttpStatus.BAD_REQUEST);
            else if (status == 2)
                return new ResponseEntity<>("Vote already applied.", HttpStatus.OK);
            return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
        } else
            return new ResponseEntity<String>("Not the authorized admin.", HttpStatus.UNAUTHORIZED);
    }

}
