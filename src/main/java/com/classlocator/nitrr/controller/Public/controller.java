package com.classlocator.nitrr.controller.Public;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.classlocator.nitrr.entity.query;
import com.classlocator.nitrr.interfaces.Pair;
import com.classlocator.nitrr.services.adminService;
import com.classlocator.nitrr.services.jwtService;

@RestController
public class controller {

    @Autowired
    private adminService admins;

    @Autowired
    protected jwtService jwt;

    @GetMapping("/generate")
    public ResponseEntity<String> generate(){
        
        if(admins.searchTools())
        {
            return new ResponseEntity<String>("Genearted successfully...", HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("Something went wrong...", HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/download/{version}")
    public ResponseEntity<String> map(@PathVariable("version") Integer version) {
        Pair<Integer, String> download = admins.downloadMap(version);
        if(download.getKey() == 1) {
            return new ResponseEntity<>(download.getValue(), HttpStatus.OK);
        }
        else if(download.getKey() == 0) return new ResponseEntity<String>("Already the latest version.", HttpStatus.OK);
        return new ResponseEntity<String>("Something went wrong, and we couldn't update.", HttpStatus.OK);
    }

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
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
