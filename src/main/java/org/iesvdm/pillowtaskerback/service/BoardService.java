package org.iesvdm.pillowtaskerback.service;

import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.dto.HotelBoardDTO;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    ReservationService reservationService;
    @Autowired
    RoomService roomService;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    ClientService clientService;

    @Transactional
    public HotelBoardDTO getHotelBoardByHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        // Aquí se calculan los datos necesarios para el HotelBoardDTO
        Long totalClients = clientService.getClientsByHotel(hotelId).stream().count();
        Long checkInsToday = reservationService.getCheckInsTodayByHotel(hotelId);
        Long checkInsPendingToday = reservationService.getPendingCheckInsTodayByHotel(hotelId);
        Long checkInsDone = reservationService.getCompletedCheckInsByHotel(hotelId);
        Long totalRooms = hotel.getRooms().stream().count();
        Long availableRooms = roomService.getAvailableRoomsByHotel(hotelId).stream().count();
        Long occupiedRooms = roomService.getOccupiedRoomsByHotel(hotelId).stream().count();
        Long dirtyRooms = roomService.getDirtyRoomsByHotel(hotelId).stream().count();
        Long cleanRooms = availableRooms - dirtyRooms;
        Long roomsUnderMaintenance = roomService.getRoomsUnderMaintenanceByHotel(hotelId).stream().count();

        return HotelBoardDTO.builder()
                .totalClients(totalClients)
                .checkInsToday(checkInsToday)
                .checkInsPendingToday(checkInsPendingToday)
                .checkInsDone(checkInsDone)
                .totalRooms(totalRooms)
                .availableRooms(availableRooms)
                .occupiedRooms(occupiedRooms)
                .cleanRooms(cleanRooms)
                .dirtyRooms(dirtyRooms)
                .roomsUnderMaintenance(roomsUnderMaintenance)
                .build();
    }
}
