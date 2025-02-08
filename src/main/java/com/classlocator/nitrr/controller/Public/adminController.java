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
import com.classlocator.nitrr.services.adminService;

//This will expose the admin endpoints to be used by the frontend
@RestController
@RequestMapping("/admin")
public class adminController extends controller {

    @Autowired
    private adminService admins;

    Map<String, String> token = new HashMap<String, String>();

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody Map<String, String> user) {
        int status = admins.saveUpdateNewAdmin(user);
        if (status == 1 || status == 2) {

            String action = status == 1 ? "created" : "updated";
            HttpStatus httpStatus = status == 1 ? HttpStatus.CREATED : HttpStatus.OK;
            token.put(action, jwt.generateToken(user.get("rollno"),
                    user.get("name"),
                    user.get("department"), "ADMIN"));
            return new ResponseEntity<Map<String, String>>(token, httpStatus);
        } else if (status == -1)
            return new ResponseEntity<String>("Wrong username or password", HttpStatus.FORBIDDEN);
        else if (status == -2)
            return new ResponseEntity<String>("Invalid Roll no", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> s) {
        try {
            admin status  = (admin) admins.authorization(Integer.parseInt(s.get("rollno")),s.get("password")).get("admin");
            token.put("Token", jwt.generateToken(status.getRollno().toString(),status.getName(),status.getDepartment(), status.getRoles().get(0)));
            return new ResponseEntity<Map<String, String>>(token, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("Wrong username or password", HttpStatus.UNAUTHORIZED);
        } catch (NullPointerException e) {
            return new ResponseEntity<String>("Not allowed", HttpStatus.FORBIDDEN);
        }
        catch (Exception e) {
            return new ResponseEntity<String>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
