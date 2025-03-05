package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation,Long> {
}
