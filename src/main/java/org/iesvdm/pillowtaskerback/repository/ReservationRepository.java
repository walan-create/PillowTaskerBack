package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {
}
