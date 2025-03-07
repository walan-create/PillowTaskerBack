package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.dto.HotelDTOAutoCreateEmployee;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.exception.EmpleadoNotFoundException;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.EmployeeRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelService {
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager entityManager;


    public List<Hotel> all(){return this.hotelRepository.findAll();}

    @Transactional
    public Hotel save (Hotel hotel){
        hotelRepository.save(hotel);
        entityManager.refresh(hotel);
        return hotel;
    }

    @Transactional
    public Hotel one (Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(()->new HotelNotFoundException(id));
    }

    @Transactional
    public Hotel replace(Long id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException(id));

        hotel.setName(hotelDetails.getName());
        hotel.setPostalCode(hotelDetails.getPostalCode());
        hotel.setAddress(hotelDetails.getAddress());

        return hotelRepository.save(hotel);
    }

    @Transactional
    public void delete (Long id){
        this.hotelRepository.findById(id).map(h->{
                    this.hotelRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new HotelNotFoundException(id));
    }

    @Transactional
    public Hotel createHotelForUser(Long userId, HotelDTOAutoCreateEmployee dto) {
        // Comprobar si el User existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsuarioNotFoundException(userId));

        // Crear y asignar el hotel al usuario
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setPostalCode(dto.getPostalCode());
        hotel.setAddress(dto.getAddress());
        hotel.setOwner(user); // Asignamos el User como el propietario del hotel

        // Guardamos el hotel con el dueño asignado
        save(hotel);

        // Autogeneramos el Empleado para el usuario que ha creado el hotel y le Asignamos el rol ADMIN
        Employee employee = new Employee();
        employee.setName(dto.getEmployeeName());
        employee.setSurname1(dto.getSurname1());
        employee.setSurname2(dto.getSurname2());
        employee.setPassword(dto.getPassword());
        employee.setType(TipoEmpleadoEnum.ADMIN);
        employee.setUser(user);
        employee.setHotel(hotel);

        // Guardar el empleado
        employeeRepository.save(employee);

        return hotel;
    }

    //Extras
    @Transactional
    public List<HotelDTO> getAllHotelsDTOByOwnerIdOrEmployeeId(Long userId) {

        // Obtener los hoteles cuyo propietario es el `userId`
        Set<Hotel> hotelsByOwner = hotelRepository.findAllByOwner_Id(userId);

        // Obtener los hoteles que tienen un empleado cuyo `user` es el `userId`
        Set<Hotel> hotelsByEmployee = hotelRepository.findAllByEmployees_User_Id(userId);

        // Combinar ambos hoteles sin repeticiones
        Set<Hotel> allHotels = new HashSet<>(hotelsByOwner);
        allHotels.addAll(hotelsByEmployee);

        // Mapear los hoteles a HotelDTO
        return allHotels.stream()
                .map(hotel -> new HotelDTO(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getPostalCode(),
                        hotel.getAddress(),
                        hotel.getEmployees().size(), //Calculamos el total de los empleados por hotel
                        hotel.getOwner().getId() //Asociamos el id del dueño
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteEmployeeFromHotel(Long id, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmpleadoNotFoundException(id));
        hotel.getEmployees().remove(employee);
        hotelRepository.save(hotel);
        employeeRepository.delete(employee);
    }

}
