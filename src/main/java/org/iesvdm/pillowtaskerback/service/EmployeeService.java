package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.exception.EmpleadoNotFoundException;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.EmployeeRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelRepository hotelRepository;

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

    public Employee replace(Long id, Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException(id));

        employee.setName(employeeDetails.getName());
        employee.setSurname1(employeeDetails.getSurname1());
        employee.setSurname2(employeeDetails.getSurname2());
        employee.setPassword(employeeDetails.getPassword());
        employee.setType(employeeDetails.getType());

        return employeeRepository.save(employee);
    }


    public void delete (Long id){
        this.employeeRepository.findById(id).map(h->{
                    this.employeeRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new EmpleadoNotFoundException(id));
    }

    public List<Employee> getEmployeesByHotel(Long hotelId) {return employeeRepository.findByHotel_Id(hotelId);}

    public Employee createEmployee(Long hotelId, Long userId, Employee employee) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsuarioNotFoundException(userId));

        employee.setHotel(hotel);
        employee.setUser(user);
        return employeeRepository.save(employee);
    }




}
