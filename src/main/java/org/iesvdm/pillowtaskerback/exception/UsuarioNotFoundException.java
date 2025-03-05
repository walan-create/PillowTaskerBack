package org.iesvdm.pillowtaskerback.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long id) {
        super("Not found User with id: " + id);
    }
}
