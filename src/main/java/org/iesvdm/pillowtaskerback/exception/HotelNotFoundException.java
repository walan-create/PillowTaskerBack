package org.iesvdm.pillowtaskerback.exception;

public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(Long id) {
        super("Not found Hotel with id: " + id);
    }
}
