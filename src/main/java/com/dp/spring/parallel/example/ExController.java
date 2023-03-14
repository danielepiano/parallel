package com.dp.spring.parallel.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExController {

    @GetMapping
    public String hello() {
        return "hello";
    }
}
