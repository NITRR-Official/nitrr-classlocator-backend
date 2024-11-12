package com.classlocator.nitrr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//This will expose the admin endpoints to be used by the frontend
@RestController
public class adminController {
    @GetMapping("check")
    public String check(){
        return "Yeah..., the setup is running...";
    }
}
