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

import com.classlocator.nitrr.entity.admin;
import com.classlocator.nitrr.interfaces.constants;
import com.classlocator.nitrr.services.adminService;

//This will expose the admin endpoints to be used by the frontend
@RestController
@RequestMapping("/admin")
public class adminController extends controller {

    @Autowired
    private adminService admins;

    Map<String, String> token = new HashMap<>();

    /**
     * Handles admin signup; creates or updates an admin account
     * and returns a JWT token on success, or an error response on failure.
     * Requires admin authorization to access.
     * @return: ResponseEntity<?>
     */

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> user) {
        int status = admins.saveUpdateNewAdmin(user);
        if (status == 1 || status == 2) {

            String action = status == 1 ? "created" : "updated";
            HttpStatus httpStatus = status == 1 ? HttpStatus.CREATED : HttpStatus.OK;
            token.put(action, jwt.generateToken(user.get(constants.ROLL_NO),
                    user.get(constants.NAME),
                    user.get(constants.DEPT), constants.getRoles()[1]));
            return new ResponseEntity<>(token, httpStatus);
        } else if (status == -1)
            return new ResponseEntity<>("Wrong username or password", HttpStatus.FORBIDDEN);
        else if (status == -2)
            return new ResponseEntity<>("Invalid Roll no", HttpStatus.CONFLICT);
        return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles admin login; returns a JWT token on success, or an error response on
     * failure.
     * Requires admin authorization to access.
     * @return: ResponseEntity<?>
     */

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> s) {
        try {
            admin status = (admin) admins.authorization(Integer.parseInt(s.get(constants.ROLL_NO)), s.get(constants.PASSWORD))
                    .get(constants.SMALL_ROLES.get(1));
            token.put("Token", jwt.generateToken(status.getRollno().toString(), status.getName(),
                    status.getDepartment(), constants.getRoles()[1]));
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid Roll no", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
