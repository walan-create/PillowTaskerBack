package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.iesvdm.pillowtaskerback.domain.Hotel;

@Data
@AllArgsConstructor
public class HotelDTO {

    private Long id;
    private String nombre;
    private String codigoPostal;
    private String direccion;
    private Integer numeroEmpleados;
    private boolean propio;// Nuevo campo que indica si el usuario actual es el dueño del hotel

}
