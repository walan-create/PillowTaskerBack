package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iesvdm.pillowtaskerback.enums.CredentialTypeEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelCredentialResponseDTO {

    private Long credentialId;
    private CredentialTypeEnum rol;

    private Long hotelId;
    private String hotelName;
    private String hotelAddress;
    private String hotelPostalCode;

    private String userName;
    private String userSurname1;
    private String userSurname2;
    private String userDni;
}

