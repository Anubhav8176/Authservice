package com.anucodes.authservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class HomeControllers {

    @GetMapping("/login")
    public String Login(){
        return "This is Login Endpoint!!!";
    }

    @GetMapping("/signup")
    public String RegisterUser(){
        return "This is Register User Endpoint!!!";
    }

    @GetMapping("/home")
    public String home(){
        return "This is Home Endpoint!!!";
    }

}
