package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Reservation;
import org.iesvdm.pillowtaskerback.exception.ReservaNotFoundException;
import org.iesvdm.pillowtaskerback.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Reservation> all(){return this.reservationRepository.findAll();}

    @Transactional
    public Reservation save (Reservation reservation){
        reservationRepository.save(reservation);
        entityManager.refresh(reservation);
        return reservation;
    }

    public Reservation one (Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(()->new ReservaNotFoundException(id));
    }

    public Reservation replace(Long id, Reservation reservation) {
        return this.reservationRepository.findById(id)  // Busca el reservation con el ID proporcionado.
                .map(h -> (id.equals(reservation.getId())  // Compara el ID proporcionado con el del objeto reservation.
                        ? this.reservationRepository.save(reservation)  // Si son iguales, guarda el nuevo reservation en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new ReservaNotFoundException(id));  // Si no se encuentra el reservation con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.reservationRepository.findById(id).map(h->{
                    this.reservationRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new ReservaNotFoundException(id));
    }

}
