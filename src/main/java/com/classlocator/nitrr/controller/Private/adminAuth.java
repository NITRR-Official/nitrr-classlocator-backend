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

    /**
     * Handles raising a query by an authenticated admin user.
     * Requires authentication and access to admin only.
     * 
     * @param q The request body containing query details as key-value pairs.
     * @return A ResponseEntity with an appropriate HTTP status and message:
     *         - 201 CREATED: If the query is successfully raised.
     *         - 409 CONFLICT: If the room ID is invalid or the roll number is not
     *         valid.
     *         - 400 BAD REQUEST: If the name or description is missing.
     *         - 500 INTERNAL SERVER ERROR: If an unknown error occurs.
     */

    @PostMapping("/raiseQuery")
    public ResponseEntity<String> raiseQuery(@RequestBody Map<String, String> q) {

        // We don't require password or id, as it come here only if it is authenticated.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            int rollno = Integer.parseInt(authentication.getName());
            int status = admins.saveQuery(q, rollno);
            if (status == 1)
                return new ResponseEntity<>("Query raised.", HttpStatus.CREATED);
            else if (status == -1)
                return new ResponseEntity<>("Invalid room id", HttpStatus.CONFLICT);
            else if (status == -2)
                return new ResponseEntity<>("Name or description not provided", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid roll no", HttpStatus.CONFLICT);
        }
    }

    /**
     * Handles voting on a query by an authenticated user.
     * Requires authentication and access to admin only.
     * 
     * @param id The ID of the query to vote on (passed as a request parameter).
     * @return A ResponseEntity with an appropriate HTTP status and message:
     *         - 200 OK: If the vote is successfully applied or if the vote was
     *         already applied.
     *         - 406 NOT ACCEPTABLE: If the user who raised the query tries to vote.
     *         - 400 BAD REQUEST: If the query ID is invalid or not found.
     *         - 500 INTERNAL SERVER ERROR: If an unknown error occurs.
     *         - 409 CONFLICT: If the roll number is invalid.
     */
    @PostMapping("/vote")
    public ResponseEntity<String> voting(@RequestParam("id") String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            int rollno = Integer.parseInt(authentication.getName());
            int status = admins.voting(rollno, id);
            if (status == 1)
                return new ResponseEntity<>("Vote successful", HttpStatus.OK);
            else if (status == 0)
                return new ResponseEntity<>("Vote already applied.", HttpStatus.OK);
            else if (status == -1)
                return new ResponseEntity<>("Raised user can't vote", HttpStatus.NOT_ACCEPTABLE);
            else if (status == -2)
                return new ResponseEntity<>("Query not found", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid roll no", HttpStatus.CONFLICT);
        }
    }
}
