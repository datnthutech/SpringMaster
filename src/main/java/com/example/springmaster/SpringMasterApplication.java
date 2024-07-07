package com.example.springmaster;

import com.example.springmaster.service.ActiveMethodTimeoutService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringMasterApplication.class, args);
    }
}
