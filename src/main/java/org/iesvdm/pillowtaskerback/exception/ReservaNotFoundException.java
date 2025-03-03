package org.iesvdm.pillowtaskerback.exception;

public class ReservaNotFoundException extends RuntimeException {
    public ReservaNotFoundException(Long id) {
        super("Not found Reserva with id: " + id);
    }
}
