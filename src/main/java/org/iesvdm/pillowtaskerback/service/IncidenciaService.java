package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Incidence;
import org.iesvdm.pillowtaskerback.exception.IncidenciaNotFoundException;
import org.iesvdm.pillowtaskerback.repository.IncidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidenciaService {
    @Autowired
    IncidenceRepository incidenceRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Incidence> all(){return this.incidenceRepository.findAll();}

    @Transactional
    public Incidence save (Incidence incidence){
        incidenceRepository.save(incidence);
        entityManager.refresh(incidence);
        return incidence;
    }

    public Incidence one (Long id) {
        return incidenceRepository.findById(id)
                .orElseThrow(()->new IncidenciaNotFoundException(id));
    }

    public Incidence replace(Long id, Incidence incidence) {
        return this.incidenceRepository.findById(id)  // Busca el incidence con el ID proporcionado.
                .map(h -> (id.equals(incidence.getId())  // Compara el ID proporcionado con el del objeto incidence.
                        ? this.incidenceRepository.save(incidence)  // Si son iguales, guarda el nuevo incidence en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new IncidenciaNotFoundException(id));  // Si no se encuentra el incidence con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.incidenceRepository.findById(id).map(h->{
                    this.incidenceRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new IncidenciaNotFoundException(id));
    }

}
