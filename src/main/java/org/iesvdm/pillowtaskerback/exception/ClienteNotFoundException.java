package org.iesvdm.pillowtaskerback.exception;

public class ClienteNotFoundException extends RuntimeException {
  public ClienteNotFoundException(Long id) {
    super("Not found Client with id: " + id);
  }
}
