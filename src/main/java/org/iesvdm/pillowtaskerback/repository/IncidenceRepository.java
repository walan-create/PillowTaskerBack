package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Incidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidenceRepository extends JpaRepository<Incidence,Long> {
}
