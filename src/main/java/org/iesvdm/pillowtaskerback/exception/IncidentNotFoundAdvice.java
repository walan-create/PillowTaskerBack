package org.iesvdm.pillowtaskerback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class IncidentNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(IncidentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String incidenciaNotFoundHandler(IncidentNotFoundException categoriaNotFoundException) {
        return categoriaNotFoundException.getMessage();
    }
}
