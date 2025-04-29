package org.iesvdm.pillowtaskerback.exception;

public class UsuarioByMailNotFoundException extends RuntimeException {
    public UsuarioByMailNotFoundException(String mail) {
        super("Not found User with id: " + mail);
    }
}
