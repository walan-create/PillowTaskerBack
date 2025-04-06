package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;

@Data
@AllArgsConstructor
@Builder
public class HotelDTOAutoCreateCredential {
    //Datos del hotel
    private String name;
    private String postalCode;
    private String address;

    //Datos de la credencial AUTOGENERADA
    private String password;
}
