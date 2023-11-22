package com.example.demo.user.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/login")
    public String loginPage(){return "Login.html";}

    @GetMapping("/signup")
    public String signUpPage(){return "signUp.html";}
}