package org.iesvdm.pillowtaskerback.exception;

public class CredentialNotFoundException extends RuntimeException {
    public CredentialNotFoundException(Long id) {
        super("Not found Credential with id: " + id);
    }
}
