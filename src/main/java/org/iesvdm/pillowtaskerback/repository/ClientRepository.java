package org.iesvdm.pillowtaskerback.repository;

import org.iesvdm.pillowtaskerback.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {
}
