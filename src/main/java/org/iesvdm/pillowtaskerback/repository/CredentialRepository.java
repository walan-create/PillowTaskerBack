package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Credential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CredentialRepository extends JpaRepository<Credential,Long> {

    Set<Credential> findAllByHotel_Id(Long hotelId);
    Optional<Credential> findByUserIdAndHotelId(Long userId, Long hotelId);

}
