package org.iesvdm.pillowtaskerback.exception;

public class EmpleadoNotFoundException extends RuntimeException {
    public EmpleadoNotFoundException(Long id) {
        super("Not found Empleado with id: " + id);
    }
}
