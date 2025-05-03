package org.iesvdm.pillowtaskerback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class HotelBoardDTO {
    private Long totalClients;
    private Long checkInsToday;
    private Long checkInsPendingToday;
    private Long checkInsDone;
    private Long totalRooms;
    private Long availableRooms;
    private Long occupiedRooms;
    private Long cleanRooms;
    private Long dirtyRooms;
    private Long roomsUnderMaintenance;

}
