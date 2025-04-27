package org.iesvdm.pillowtaskerback.dto;

import lombok.Data;
import org.iesvdm.pillowtaskerback.enums.ReservationStateEnum;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReservationDTO {

    private String reservationsName;
    private LocalDateTime entryDate; // Formato ISO 8601
    private LocalDateTime departureDay; // Formato ISO 8601
    private ReservationStateEnum state;
    private boolean earlyDeparture;
    private List<Long> roomIds;
    private List<Long> clientIds;

}

