package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    // Método para verificar si existe una invitación para un correo y un hotel
    boolean existsByMailAndHotelId(String mail, Long hotelId);

    List<Invitation> findByMail(String email);

    Optional<Invitation> findByIdAndMail(Long invitationId, String email);
}

