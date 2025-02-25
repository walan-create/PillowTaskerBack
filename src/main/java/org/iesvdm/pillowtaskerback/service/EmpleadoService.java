package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Empleado;
import org.iesvdm.pillowtaskerback.exception.EmpleadoNotFoundException;
import org.iesvdm.pillowtaskerback.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {
    @Autowired
    EmpleadoRepository empleadoRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Empleado> all(){return this.empleadoRepository.findAll();}

    @Transactional
    public Empleado save (Empleado empleado){
        empleadoRepository.save(empleado);
        entityManager.refresh(empleado);
        return empleado;
    }

    public Empleado one (Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(()->new EmpleadoNotFoundException(id));
    }

    public Empleado replace(Long id, Empleado empleado) {
        return this.empleadoRepository.findById(id)  // Busca el empleado con el ID proporcionado.
                .map(h -> (id.equals(empleado.getId())  // Compara el ID proporcionado con el del objeto empleado.
                        ? this.empleadoRepository.save(empleado)  // Si son iguales, guarda el nuevo empleado en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new EmpleadoNotFoundException(id));  // Si no se encuentra el empleado con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.empleadoRepository.findById(id).map(h->{
                    this.empleadoRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new EmpleadoNotFoundException(id));
    }

}
