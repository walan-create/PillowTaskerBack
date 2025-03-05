package org.iesvdm.pillowtaskerback.repository;

import org.hibernate.mapping.List;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
