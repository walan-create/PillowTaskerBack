// src/main/java/org/iesvdm/pillowtaskerback/service/CredentialService.java
package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.CredentialDTO;
import org.iesvdm.pillowtaskerback.dto.HotelCredentialResponseDTO;
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.iesvdm.pillowtaskerback.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CredentialService {

    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Devuelve la lista completa de credenciales.
     *
     * @return lista de todas las credenciales
     */
    public List<Credential> all() {
        return this.credentialRepository.findAll();
    }

    /**
     * Guarda una nueva credencial en la base de datos y actualiza su estado.
     *
     * @param credential credencial a guardar
     * @return credencial guardada
     */
    @Transactional
    public Credential save(Credential credential) {
        credentialRepository.save(credential);
        entityManager.refresh(credential);
        return credential;
    }

    /**
     * Busca y devuelve una credencial por su id.
     *
     * @param id identificador de la credencial
     * @return credencial encontrada
     * @throws ApiException si no se encuentra la credencial
     */
    public Credential one(Long id) {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Credencial con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
    }

    /**
     * Reemplaza el rol de una credencial existente.
     *
     * @param id identificador de la credencial a modificar
     * @param credentialDetails datos nuevos de la credencial
     * @return credencial actualizada
     * @throws ApiException si no se encuentra la credencial
     */
    public Credential replace(Long id, Credential credentialDetails) {
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Credencial con id " + id + " no encontrada", HttpStatus.NOT_FOUND));

        credential.setRol(credentialDetails.getRol());
        return credentialRepository.save(credential);
    }

    /**
     * Elimina una credencial por su id.
     *
     * @param id identificador de la credencial a eliminar
     * @throws ApiException si no se encuentra la credencial
     */
    public void delete(Long id) {
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new ApiException("Credencial con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
        credentialRepository.delete(credential);
    }

    /**
     * Obtiene la lista de credenciales asociadas a un hotel.
     *
     * @param hotelId identificador del hotel
     * @return lista de credenciales del hotel
     */
    public List<Credential> getCredentialsByHotel(Long hotelId) {
        Set<Credential> credentialsSet = credentialRepository.findAllByHotel_Id(hotelId);
        return new ArrayList<>(credentialsSet);
    }

    /**
     * Crea una nueva credencial asociada a un hotel y usuario.
     *
     * @param hotelId identificador del hotel
     * @param userId identificador del usuario
     * @param credential credencial a crear
     * @return credencial creada y asociada
     * @throws ApiException si el hotel o el usuario no existen
     */
    public Credential createCredential(Long hotelId, Long userId, Credential credential) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("Usuario con id " + userId + " no encontrado", HttpStatus.NOT_FOUND));

        credential.setHotel(hotel);
        credential.setUser(user);
        return credentialRepository.save(credential);
    }

    /**
     * Busca una credencial por usuario y hotel.
     *
     * @param userId identificador del usuario
     * @param hotelId identificador del hotel
     * @return credencial encontrada (opcional)
     */
    public Optional<Credential> findByUserAndHotel(Long userId, Long hotelId) {
        return credentialRepository.findByUserIdAndHotelId(userId, hotelId);
    }

    /**
     * Devuelve todas las credenciales (DTO) de un hotel, excluyendo al propietario.
     *
     * @param hotelId identificador del hotel
     * @return lista de credenciales DTO del hotel
     * @throws ApiException si no se encuentra el hotel
     */
    public List<CredentialDTO> getAllCredentialsDTObyHotelId(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));
        Long ownerId = hotel.getOwner().getId();

        Set<Credential> credentialsSet = credentialRepository.findAllByHotel_Id(hotelId)
                .stream()
                .filter(credential -> !credential.getUser().getId().equals(ownerId))
                .collect(Collectors.toSet());

        return credentialsSet.stream()
                .map(credential -> new CredentialDTO(
                        credential.getId(),
                        credential.getRol(),
                        credential.getPassword(),
                        credential.getUser().getName(),
                        credential.getUser().getMail(),
                        credential.getUser().getSurname1(),
                        credential.getUser().getSurname2(),
                        credential.getUser().getDni()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Devuelve una credencial (DTO) por su id.
     *
     * @param credentialId identificador de la credencial
     * @return credencial DTO encontrada
     * @throws ApiException si no se encuentra la credencial
     */
    public CredentialDTO getCredentialDTOById(Long credentialId) {
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new ApiException("Credencial con id " + credentialId + " no encontrada", HttpStatus.NOT_FOUND));

        return new CredentialDTO(
                credential.getId(),
                credential.getRol(),
                credential.getPassword(),
                credential.getUser().getName(),
                credential.getUser().getMail(),
                credential.getUser().getSurname1(),
                credential.getUser().getSurname2(),
                credential.getUser().getDni()
        );
    }

    /**
     * Valida el acceso de un usuario a un hotel mediante token y contraseña.
     *
     * @param token token JWT del usuario
     * @param hotelId identificador del hotel
     * @param passwordIngresada contraseña introducida por el usuario
     * @return respuesta con los datos de la credencial y hotel
     * @throws ApiException si el token es inválido, la credencial no existe o la contraseña es incorrecta
     */
    public HotelCredentialResponseDTO validateAccess(String token, Long hotelId, String passwordIngresada) {
        if (!jwtUtil.isTokenValid(token)) {
            log.warn("Token inválido: {}", token);
            throw new ApiException("Token inválido", HttpStatus.UNAUTHORIZED);
        }

        Long userId = jwtUtil.extractUserId(token);
        log.info("Token válido. userId extraído: {}", userId);

        Credential credencial = credentialRepository.findByUserIdAndHotelId(userId, hotelId)
                .orElseThrow(() -> {
                    log.warn("No se encontró credencial para userId={} y hotelId={}", userId, hotelId);
                    return new ApiException("No tienes acceso a este hotel", HttpStatus.FORBIDDEN);
                });

        log.info("Credencial encontrada. Validando contraseña...");

        if (!passwordEncoder.matches(passwordIngresada, credencial.getPassword())) {
            log.warn("Contraseña incorrecta para userId={} en hotelId={}", userId, hotelId);
            throw new ApiException("Contraseña incorrecta", HttpStatus.FORBIDDEN);
        }

        log.info("Contraseña válida. Acceso concedido a userId={} en hotelId={}", userId, hotelId);

        Hotel hotel = credencial.getHotel();
        User user = credencial.getUser();

        HotelCredentialResponseDTO response = new HotelCredentialResponseDTO(
                credencial.getId(),
                credencial.getRol(),
                hotel.getId(),
                hotel.getName(),
                hotel.getAddress(),
                hotel.getPostalCode(),
                user.getName(),
                user.getSurname1(),
                user.getSurname2(),
                user.getDni()
        );

        log.info("Respuesta generada correctamente: {}", response);

        return response;
    }

    public boolean hasRoleInHotel(String username, Long hotelId, Set<String> roles) {
        Optional<User> userOpt = userRepository.findByMail(username);
        if (userOpt.isEmpty()) {
            return false;
        }
        User user = userOpt.get();

        Optional<Credential> credOpt = credentialRepository.findByUserIdAndHotelId(user.getId(), hotelId);
        if (credOpt.isEmpty()) {
            return false;
        }
        String rol = credOpt.get().getRol().name();

        return roles.contains(rol);
    }
}