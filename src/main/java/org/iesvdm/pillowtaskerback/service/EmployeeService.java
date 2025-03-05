package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.exception.EmpleadoNotFoundException;
import org.iesvdm.pillowtaskerback.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Employee> all(){return this.employeeRepository.findAll();}

    @Transactional
    public Employee save (Employee employee){
        employeeRepository.save(employee);
        entityManager.refresh(employee);
        return employee;
    }

    public Employee one (Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(()->new EmpleadoNotFoundException(id));
    }

    public Employee replace(Long id, Employee employee) {
        return this.employeeRepository.findById(id)  // Busca el employee con el ID proporcionado.
                .map(h -> (id.equals(employee.getId())  // Compara el ID proporcionado con el del objeto employee.
                        ? this.employeeRepository.save(employee)  // Si son iguales, guarda el nuevo employee en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new EmpleadoNotFoundException(id));  // Si no se encuentra el employee con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.employeeRepository.findById(id).map(h->{
                    this.employeeRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new EmpleadoNotFoundException(id));
    }



}
