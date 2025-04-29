package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.service.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/credentials")
@Validated
public class CredentialController {

    @Autowired
    CredentialService credentialService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Credential>> getAllCredentials() {
        return ResponseEntity.ok(credentialService.all());
    }

    // GET ONE
    @GetMapping("/{credentialId}")
    public ResponseEntity<Credential> getCredential(@PathVariable Long credentialId) {
        return ResponseEntity.ok(credentialService.one(credentialId));
    }

    // UPDATE
    @PutMapping("/{credentialId}")
    public ResponseEntity<Credential> updateCredential(@PathVariable Long credentialId, @RequestBody Credential credential) {
        log.info("Updating employee with id: {}", credentialId);
        Credential updatedCredential = credentialService.replace(credentialId, credential);
        return ResponseEntity.ok(updatedCredential);
    }

    @DeleteMapping("/credentials/{credentialId}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long credentialId) {
        credentialService.delete(credentialId);
        return ResponseEntity.noContent().build();
    }

}
