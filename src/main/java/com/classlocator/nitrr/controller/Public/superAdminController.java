package com.classlocator.nitrr.controller.Public;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.superAdmin;
import com.classlocator.nitrr.interfaces.constants;
import com.classlocator.nitrr.services.superAdminService;

//This will expose the super admin endpoints to be used by the frontend to be developed by other team members
@RestController
@RequestMapping("/sadmin")
public class superAdminController extends controller {

    @Autowired
    private superAdminService sadmins;

    Map<String, String> token = new HashMap<>();

    /**
     * Activates or updates a Super Admin account and returns a JWT token on
     * success,
     * or an appropriate error response on failure.
     * Requires super admin authorization to access.
     * 
     * @param suser Map<String, String> - A map containing Super Admin details
     *              (name, password).
     * @return ResponseEntity<?> - Returns a JWT token on success or an error
     *         message with an appropriate HTTP status.
     */

    @PostMapping("/signup")
    public ResponseEntity<?> activateAdmin(@RequestBody Map<String, String> suser) {
        int status = sadmins.saveUpdateSuperAdmin(suser);
        if (status == 1 || status == 0) {
            String action = status == 1 ? "Super Admin created" : "Super Admin updated";
            HttpStatus httpStatus = status == 1 ? HttpStatus.CREATED : HttpStatus.OK;
            token.put(action, jwt.generateToken("1", suser.get(constants.NAME), "NIT Raipur", constants.getRoles()[0]));
            return new ResponseEntity<>(token, httpStatus);
        } else if (status == -1)
            return new ResponseEntity<>("Wrong password or details were not provided", HttpStatus.UNAUTHORIZED);
        else if (status == -2)
            return new ResponseEntity<>("Unauthorized or details were not provided", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Authenticates a Super Admin and returns a JWT token on successful login,
     * or an appropriate error response if authentication fails.
     * Requires super admin authorization to access.
     * 
     * @param s Map<String, String> - A map containing login credentials (roll
     *          number, password).
     * @return ResponseEntity<?> - Returns a JWT token on success or an error
     *         message with an appropriate HTTP status.
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> s) {
        try {
            superAdmin status = (superAdmin) sadmins.authorization(1, s.get(constants.PASSWORD))
                    .get(constants.SMALL_ROLES.get(0));
            token.put("Token",
                    jwt.generateToken(status.getId().toString(), status.getName(), "NIT Raipur",
                            constants.getRoles()[0]));
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Wrong password or super admin not activated", HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
