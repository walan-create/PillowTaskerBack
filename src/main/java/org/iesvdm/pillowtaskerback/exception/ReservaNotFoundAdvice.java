package org.iesvdm.pillowtaskerback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ReservaNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ReservaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String reservaNotFoundHandler(ReservaNotFoundException categoriaNotFoundException) {
        return categoriaNotFoundException.getMessage();
    }
}
