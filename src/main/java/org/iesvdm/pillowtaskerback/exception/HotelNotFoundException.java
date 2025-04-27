package org.iesvdm.pillowtaskerback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HotelNotFoundException extends RuntimeException {
    public HotelNotFoundException(Long id) {
        super("Not found Hotel with id: " + id);
    }
}
