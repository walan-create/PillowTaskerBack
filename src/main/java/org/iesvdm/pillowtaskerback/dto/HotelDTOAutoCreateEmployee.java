package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;

@Data
@AllArgsConstructor
@Builder
public class HotelDTOAutoCreateEmployee {
    //Datos del hotel
    private String name;
    private String postalCode;
    private String address;

    //Datos del employee AUTOGENERADO
    private String employeeName;
    private String surname1;
    private String surname2;
    private String dni;
    private String password;
    private TipoEmpleadoEnum type;
}
