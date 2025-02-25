package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Habitacion;
import org.iesvdm.pillowtaskerback.exception.HabitacionNotFoundException;
import org.iesvdm.pillowtaskerback.repository.HabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitacionService {
    @Autowired
    HabitacionRepository habitacionRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Habitacion> all(){return this.habitacionRepository.findAll();}

    @Transactional
    public Habitacion save (Habitacion habitacion){
        habitacionRepository.save(habitacion);
        entityManager.refresh(habitacion);
        return habitacion;
    }

    public Habitacion one (Long id) {
        return habitacionRepository.findById(id)
                .orElseThrow(()->new HabitacionNotFoundException(id));
    }

    public Habitacion replace(Long id, Habitacion habitacion) {
        return this.habitacionRepository.findById(id)  // Busca el habitacion con el ID proporcionado.
                .map(h -> (id.equals(habitacion.getId())  // Compara el ID proporcionado con el del objeto habitacion.
                        ? this.habitacionRepository.save(habitacion)  // Si son iguales, guarda el nuevo habitacion en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new HabitacionNotFoundException(id));  // Si no se encuentra el habitacion con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.habitacionRepository.findById(id).map(h->{
                    this.habitacionRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new HabitacionNotFoundException(id));
    }

}
