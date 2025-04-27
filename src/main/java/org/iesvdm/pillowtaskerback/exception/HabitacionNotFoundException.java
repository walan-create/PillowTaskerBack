package org.iesvdm.pillowtaskerback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HabitacionNotFoundException extends RuntimeException {
    public HabitacionNotFoundException(Long id) {
        super("Not found Room with id: " + id);
    }
}
