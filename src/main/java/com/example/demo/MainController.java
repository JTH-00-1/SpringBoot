package com.example.demo;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@EnableJpaAuditing //JPA Auditing활성화
public class MainController {

    @GetMapping("/")
    public String root() {
        return "index";
    }
}