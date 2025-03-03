package org.iesvdm.pillowtaskerback.exception;

public class IncidenciaNotFoundException extends RuntimeException {
  public IncidenciaNotFoundException(Long id) {
    super("Not found Incidencia with id: " + id);
  }
}
