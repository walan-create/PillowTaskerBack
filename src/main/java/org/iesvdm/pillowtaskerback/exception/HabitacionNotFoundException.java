package org.iesvdm.pillowtaskerback.exception;

public class HabitacionNotFoundException extends RuntimeException {
    public HabitacionNotFoundException(Long id) {
        super("Not found Room with id: " + id);
    }
}
