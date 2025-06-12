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
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Devuelve la lista completa de hoteles.
     *
     * @return lista de todos los hoteles
     */
    public List<Hotel> all() {
        return this.hotelRepository.findAll();
    }

    /**
     * Guarda un nuevo hotel en la base de datos y actualiza su estado.
     *
     * @param hotel hotel a guardar
     * @return hotel guardado
     */
    @Transactional
    public Hotel save(Hotel hotel) {
        hotelRepository.save(hotel);
        entityManager.refresh(hotel);
        return hotel;
    }

    /**
     * Busca y devuelve un hotel por su id.
     *
     * @param id identificador del hotel
     * @return hotel encontrado
     * @throws ApiException si no se encuentra el hotel
     */
    @Transactional
    public Hotel one(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ApiException("Hotel con id " + id + " no encontrado", HttpStatus.NOT_FOUND));
    }

    /**
     * Reemplaza los datos de un hotel existente por los nuevos datos proporcionados.
     *
     * @param id identificador del hotel a modificar
     * @param hotelDetails datos nuevos del hotel
     * @return hotel actualizado
     * @throws ApiException si no se encuentra el hotel
     */
    @Transactional
    public Hotel replace(Long id, Hotel hotelDetails) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ApiException("Hotel con id " + id + " no encontrado", HttpStatus.NOT_FOUND));

        hotel.setName(hotelDetails.getName());
        hotel.setPostalCode(hotelDetails.getPostalCode());
        hotel.setAddress(hotelDetails.getAddress());

        return hotelRepository.save(hotel);
    }

    /**
     * Elimina un hotel por su id.
     *
     * @param id identificador del hotel a eliminar
     * @throws ApiException si no se encuentra el hotel
     */
    @Transactional
    public void delete(Long id) {
        this.hotelRepository.findById(id).map(h -> {
                    this.hotelRepository.delete(h);
                    return h;
                })
                .orElseThrow(() -> new ApiException("Hotel con id " + id + " no encontrado", HttpStatus.NOT_FOUND));
    }

    /**
     * Crea un nuevo hotel para un usuario y le asigna una credencial ADMIN.
     *
     * @param userId identificador del usuario propietario
     * @param dto datos del hotel y credencial
     * @return hotel creado
     * @throws ApiException si el usuario no existe
     */
    @Transactional
    public Hotel createHotelForUser(Long userId, HotelDTOAutoCreateCredential dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Usuario con id " + userId + " no encontrado", HttpStatus.NOT_FOUND));

        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setPostalCode(dto.getPostalCode());
        hotel.setAddress(dto.getAddress());
        hotel.setOwner(user);

        save(hotel);

        Credential credential = new Credential();
        credential.setPassword(passwordEncoder.encode(dto.getPassword()));
        credential.setRol(CredentialTypeEnum.ADMIN);
        credential.setUser(user);
        credential.setHotel(hotel);

        credentialRepository.save(credential);
        return hotel;
    }

    /**
     * Devuelve todos los hoteles asociados a un usuario como propietario o empleado.
     *
     * @param userId identificador del usuario
     * @return lista de hoteles en formato DTO
     * @throws ApiException si no se encuentra el usuario
     */
    @Transactional
    public List<HotelDTO> getAllHotelsDTOByOwnerIdOrCredentialId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Usuario con id " + userId + " no encontrado", HttpStatus.NOT_FOUND));

        Set<Hotel> hotelsByOwner = hotelRepository.findAllByOwner_Id(userId);
        Set<Hotel> hotelsByEmployee = hotelRepository.findAllByCredentials_User_Id(userId);

        Set<Hotel> allHotels = new HashSet<>(hotelsByOwner);
        allHotels.addAll(hotelsByEmployee);

        return allHotels.stream()
                .map(hotel -> new HotelDTO(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getPostalCode(),
                        hotel.getAddress(),
                        hotel.getCredentials().size(),
                        hotel.getRooms().size(),
                        hotel.getOwner().getId()
                ))
                .collect(Collectors.toList());
    }
}