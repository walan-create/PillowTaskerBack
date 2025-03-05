package org.iesvdm.pillowtaskerback.repository;

import org.hibernate.mapping.List;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {

    Set<Hotel> findAllByOwner_Id(Long ownerId);

    Set<Hotel> findAllByEmployees_User_Id(Long userId);
}
