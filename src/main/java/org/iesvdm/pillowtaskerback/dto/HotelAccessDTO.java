package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HotelAccessDTO {
    private Long hotelId;
    private String password;
}
