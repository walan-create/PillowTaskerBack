package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class PillowTaskerBackApplication {
    @Autowired
    private SeedService seedService;

    public static void main(String[] args) {
        SpringApplication.run(PillowTaskerBackApplication.class, args);
    }

    @PostConstruct
    public void init() {
        seedService.seedDatabase(); // Al iniciar la aplicación carga una BD de prueba
    }
}