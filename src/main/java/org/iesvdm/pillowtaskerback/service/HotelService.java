package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.HotelBoardDTO;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.dto.HotelDTOAutoCreateCredential;
import org.iesvdm.pillowtaskerback.enums.CredentialTypeEnum;
import org.iesvdm.pillowtaskerback.exception.CredentialNotFoundException;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HotelService {


    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


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
                .orElseThrow(() -> new CredentialNotFoundException(id));

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
    public Hotel createHotelForUser(Long userId, HotelDTOAutoCreateCredential dto) {
        // Obtenemos el usuario que va a crear el hotel
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsuarioNotFoundException(userId));

        // Crear y asignar el hotel al usuario
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setPostalCode(dto.getPostalCode());
        hotel.setAddress(dto.getAddress());
        // Asignamos el User como el propietario del hotel
        // ↓    ↓   ↓   ↓   ↓
        hotel.setOwner(user);

        // Guardamos el hotel con el dueño asignado
        save(hotel);

        /* Autogeneramos el Empleado para el usuario
          que ha creado el hotel y le Asignamos el rol ADMIN*/
        Credential credential = new Credential();
        credential.setPassword(passwordEncoder.encode(dto.getPassword())); // Codificamos la contraseña antes de persistirla en BD
        credential.setRol(CredentialTypeEnum.ADMIN);
        credential.setUser(user);
        credential.setHotel(hotel);

        // Guardar el empleado
        credentialRepository.save(credential);
        return hotel;
    }

    //Extras
    @Transactional
    public List<HotelDTO> getAllHotelsDTOByOwnerIdOrCredentialId(Long userId) {

        // Obtener los hoteles cuyo propietario es el `userId`
        Set<Hotel> hotelsByOwner = hotelRepository.findAllByOwner_Id(userId);

        // Obtener los hoteles que tienen una credencial cuyo `user` es el `userId`
        Set<Hotel> hotelsByEmployee = hotelRepository.findAllByCredentials_User_Id(userId);

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
                        hotel.getCredentials().size(),
                        hotel.getRooms().size(), //Calculamos el total de las credenciales por hotel
                        hotel.getOwner().getId() //Asociamos el id del dueño
                ))
                .collect(Collectors.toList());
    }


}
