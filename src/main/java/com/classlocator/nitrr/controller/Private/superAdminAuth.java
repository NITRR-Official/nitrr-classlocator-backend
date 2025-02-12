package com.classlocator.nitrr.controller.Private;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.services.superAdminService;

//This will expose the super admin endpoints to be used by the frontend to be developed by other team members
@RestController
@RequestMapping("/requests")
public class superAdminAuth {

    @Autowired
    private superAdminService sadmins;

    // Special query raise system for super admin to be created here

    /**
     * Deactivates the Super Admin account.
     * Requires super admin authorization to access.
     * 
     * @return A ResponseEntity with an appropriate HTTP status and message:
     *         - 204 NO CONTENT: If the Super Admin is successfully deactivated.
     *         - 500 INTERNAL SERVER ERROR: If an unknown error occurs.
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> deactivateAdmin() {
        if (sadmins.deleteSuperAdmin() == 1)
            return new ResponseEntity<String>("Super Admin deactivated", HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles raising a query by processing the provided request data.
     * Requires super admin authorization to access.
     * 
     * @param q A map containing query details, including name, description, and
     *          room ID.
     * @return A ResponseEntity with an appropriate HTTP status and message:
     *         - 201 CREATED: If the query is successfully raised.
     *         - 409 CONFLICT: If the provided room ID is invalid.
     *         - 400 BAD REQUEST: If the name or description is missing.
     *         - 500 INTERNAL SERVER ERROR: If an unknown error occurs.
     */
    @PostMapping("/raiseQuery")
    public ResponseEntity<String> raiseQuery(@RequestBody Map<String, String> q) {
        int status = sadmins.saveQuery(q);
        if (status == 1)
            return new ResponseEntity<String>("Query raised.", HttpStatus.CREATED);
        else if (status == -1)
            return new ResponseEntity<String>("Invalid room id", HttpStatus.CONFLICT);
        else if (status == -2)
            return new ResponseEntity<String>("Name or description not provided", HttpStatus.BAD_REQUEST);
        else
            return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Approves a query based on the provided query ID.
     * Requires super admin authorization to access.
     * 
     * @param id The unique identifier of the query to be approved.
     * @return A ResponseEntity with an appropriate HTTP status and message:
     *         - 200 OK: If the query is successfully approved.
     *         - 200 OK: If the query was already approved.
     *         - 400 BAD REQUEST: If the query was not found.
     *         - 501 NOT IMPLEMENTED: If the map was not generated.
     *         - 500 INTERNAL SERVER ERROR: If an unknown error occurs.
     */
    @PostMapping("/approve")
    public ResponseEntity<String> voting(@RequestParam String id) {

        int status = sadmins.approval(id);
        if (status == 1)
            return new ResponseEntity<>("Approved", HttpStatus.OK);
        else if (status == 0)
            return new ResponseEntity<>("Already approved", HttpStatus.OK);
        else if (status == -1)
            return new ResponseEntity<>("Query not found", HttpStatus.BAD_REQUEST);
        else if (status == -2)
            return new ResponseEntity<>("Map not generated", HttpStatus.NOT_IMPLEMENTED);
        return new ResponseEntity<>("Something went wrong.", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
