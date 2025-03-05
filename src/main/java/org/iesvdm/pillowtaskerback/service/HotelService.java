package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.repository.EmployeeRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {
    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private EmployeeService employeeService;

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
    public Hotel replace(Long id, Hotel hotel) {
        return this.hotelRepository.findById(id)  // Busca el hotel con el ID proporcionado.
                .map(h -> (id.equals(hotel.getId())  // Compara el ID proporcionado con el del objeto hotel.
                        ? this.hotelRepository.save(hotel)  // Si son iguales, guarda el nuevo hotel en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new HotelNotFoundException(id));  // Si no se encuentra el hotel con ese ID, lanza una excepción.
    }

    @Transactional
    public void delete (Long id){
        this.hotelRepository.findById(id).map(h->{
                    this.hotelRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new HotelNotFoundException(id));
    }

    //Extras
    @Transactional
    public List<HotelDTO> getAllHotelsDTOByUserId(Long userId) {
        // Obtener el ID del user autenticado
        //Long authenticatedUserId = getAuthenticatedUserId();

        // Obtener todos los hoteles
        List<Hotel> hotels = hotelRepository.findAll();

        // Mapear los hoteles a HotelDTO y agregar el campo 'propio'
        return hotels.stream()
                .map(hotel -> {
                    // Verificar si el user autenticado es el dueño del hotel
                    //Boolean isOwner = hotel.getUser().getId().equals(authenticatedUserId);

                    // Crear el DTO con todos los datos del hotel y el campo 'propio'
                    return new HotelDTO(
                            hotel.getId(),
                            hotel.getName(),
                            hotel.getPostalCode(),
                            hotel.getAddress(),
                            hotel.getEmployees().size(),
                            false
                            //isOwner // Si el user autenticado es dueño, el valor será 'true'
                    );
                })
                .collect(Collectors.toList());
    }

    public void deleteEmpleadoFromHotel(Long id, Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId).orElseThrow();
        Employee employee = employeeRepository.findById(id).orElseThrow();
        hotel.getEmployees().remove(employee);
        hotelRepository.save(hotel);
        employeeRepository.delete(employee);
    }

    /*// Método auxiliar para obtener el ID del user autenticado
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verifica si el principal es una instancia de CustomUserDetails
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getId(); // Devuelve el ID del user autenticado
        } else {
            // Si el principal no es CustomUserDetails, lanza una excepción o maneja el error
            throw new IllegalStateException("User autenticado no es de tipo CustomUserDetails");
        }
    }*/
}
