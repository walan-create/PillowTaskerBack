package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Reserva;
import org.iesvdm.pillowtaskerback.exception.ReservaNotFoundException;
import org.iesvdm.pillowtaskerback.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {
    @Autowired
    ReservaRepository reservaRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Reserva> all(){return this.reservaRepository.findAll();}

    @Transactional
    public Reserva save (Reserva reserva){
        reservaRepository.save(reserva);
        entityManager.refresh(reserva);
        return reserva;
    }

    public Reserva one (Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(()->new ReservaNotFoundException(id));
    }

    public Reserva replace(Long id, Reserva reserva) {
        return this.reservaRepository.findById(id)  // Busca el reserva con el ID proporcionado.
                .map(h -> (id.equals(reserva.getId())  // Compara el ID proporcionado con el del objeto reserva.
                        ? this.reservaRepository.save(reserva)  // Si son iguales, guarda el nuevo reserva en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new ReservaNotFoundException(id));  // Si no se encuentra el reserva con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.reservaRepository.findById(id).map(h->{
                    this.reservaRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new ReservaNotFoundException(id));
    }

}
