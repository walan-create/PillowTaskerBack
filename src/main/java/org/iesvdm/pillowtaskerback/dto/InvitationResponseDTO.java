package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class InvitationResponseDTO {

    private boolean accepted;
    private String password;

}

