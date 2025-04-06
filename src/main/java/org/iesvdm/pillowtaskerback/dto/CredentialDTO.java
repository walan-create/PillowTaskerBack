package org.iesvdm.pillowtaskerback.dto;

import jakarta.persistence.*;
import lombok.*;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;

@Data
@AllArgsConstructor
public class CredentialDTO {

    // Info de la credencial
    private Long CredentialId;
    private TipoEmpleadoEnum rol;
    private String credentialPassword;

    // Info del usuario
    private String name;
    private String surname1;
    private String surname2;
    private String dni;

}
