package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.service.IncidentService;
import org.iesvdm.pillowtaskerback.service.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("v1/api")
@Validated
public class SeedRoute {

    @Autowired
    SeedService seedService;

    @GetMapping("/seed")
    public ResponseEntity<String> seedDatabase() {
        try {
            // Llama al servicio encargado de llenar la base de datos
            seedService.seedDatabase();
            return ResponseEntity.ok("SEED SUCCESSFUL");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("SEED FAILED: " + e.getMessage());
        }
    }
}
