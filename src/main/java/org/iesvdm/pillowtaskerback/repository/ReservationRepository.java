package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Incident;
import org.iesvdm.pillowtaskerback.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    // findDistinctBy — para evitar duplicados.
    //Rooms_Hotel_Id — Spring lo interpreta como:
    //→ Reservation.rooms
    //→ .hotel
    //→ .id
    List<Reservation> findDistinctByRooms_Hotel_Id(Long hotelId);

}
