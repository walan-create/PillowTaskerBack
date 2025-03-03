package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Incidencia;
import org.iesvdm.pillowtaskerback.exception.IncidenciaNotFoundException;
import org.iesvdm.pillowtaskerback.repository.IncidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncidenciaService {
    @Autowired
    IncidenciaRepository incidenciaRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Incidencia> all(){return this.incidenciaRepository.findAll();}

    @Transactional
    public Incidencia save (Incidencia incidencia){
        incidenciaRepository.save(incidencia);
        entityManager.refresh(incidencia);
        return incidencia;
    }

    public Incidencia one (Long id) {
        return incidenciaRepository.findById(id)
                .orElseThrow(()->new IncidenciaNotFoundException(id));
    }

    public Incidencia replace(Long id, Incidencia incidencia) {
        return this.incidenciaRepository.findById(id)  // Busca el incidencia con el ID proporcionado.
                .map(h -> (id.equals(incidencia.getId())  // Compara el ID proporcionado con el del objeto incidencia.
                        ? this.incidenciaRepository.save(incidencia)  // Si son iguales, guarda el nuevo incidencia en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new IncidenciaNotFoundException(id));  // Si no se encuentra el incidencia con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.incidenciaRepository.findById(id).map(h->{
                    this.incidenciaRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new IncidenciaNotFoundException(id));
    }

}
