package org.iesvdm.pillowtaskerback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateClientNifException extends RuntimeException {
    public DuplicateClientNifException(String nif, Long hotelId) {
        super("Ya existe un cliente con NIF '" + nif + "' en el hotel con ID " + hotelId);
    }
}