package org.iesvdm.pillowtaskerback.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IncidentNotFoundException extends RuntimeException {
  public IncidentNotFoundException(Long id) {
    super("Not found Incident with id: " + id);
  }
}
