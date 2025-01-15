package com.proyecto.nodocentral;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NodoCentralApplication implements CommandLineRunner {

    @Autowired
    private NodoCentralService nodoCentralService;

    public static void main(String[] args) {
        SpringApplication.run(NodoCentralApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        nodoCentralService.start();
    }
}