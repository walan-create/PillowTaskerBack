package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelDTO {

    private Long id;
    private String name;
    private String postalCode;
    private String address;
    private Integer totalEmployees;
    private Integer totalRooms;
    private Long userId;  // Agregamos el userId para saber el dueño

}
