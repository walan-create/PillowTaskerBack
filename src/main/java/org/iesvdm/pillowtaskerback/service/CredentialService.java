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
import org.iesvdm.pillowtaskerback.exception.CredentialNotFoundException;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.iesvdm.pillowtaskerback.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public List<Credential> all() {
        return this.credentialRepository.findAll();
    }

    @Transactional
    public Credential save(Credential credential) {
        credentialRepository.save(credential);
        entityManager.refresh(credential);
        return credential;
    }

    public Credential one(Long id) {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new CredentialNotFoundException(id));
    }

    public Credential replace(Long id, Credential credentialDetails) {
        Credential credential = credentialRepository.findById(id)
                .orElseThrow(() -> new CredentialNotFoundException(id));

        credential.setRol(credentialDetails.getRol()); // Solo actualizamos rol

        return credentialRepository.save(credential);
    }


    public void delete(Long id) {
        this.credentialRepository.findById(id).map(h -> {
                    this.credentialRepository.delete(h);
                    return h;
                })
                .orElseThrow(() -> new CredentialNotFoundException(id));
    }

    public List<Credential> getCredentialsByHotel(Long hotelId) {
        Set<Credential> credentialsSet = credentialRepository.findAllByHotel_Id(hotelId);
        return new ArrayList<>(credentialsSet);
    }

    public Credential createCredential(Long hotelId, Long userId, Credential credential) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsuarioNotFoundException(userId));

        credential.setHotel(hotel);
        credential.setUser(user);
        return credentialRepository.save(credential);
    }

    public Optional<Credential> findByUserAndHotel(Long userId, Long hotelId) {
        return credentialRepository.findByUserIdAndHotelId(userId, hotelId);
    }
    
    public List<CredentialDTO> getAllCredentialsDTObyHotelId(Long hotelId) {

        // Buscar el hotel por su ID
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        // Obtener el ID del propietario del hotel para excluirlo de la lista
        Long ownerId = hotel.getOwner().getId();

        // Obtener las credenciales del hotel y filtrar las que no pertenezcan al propietario
        Set<Credential> credentialsSet = credentialRepository.findAllByHotel_Id(hotelId)
                .stream()
                .filter(credential -> !credential.getUser().getId().equals(ownerId))
                .collect(Collectors.toSet());

        // Mapear cada credencial a CredentialDTO
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

    public CredentialDTO getCredentialDTOById(Long credentialId) {
        // Buscar la credencial por su ID
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CredentialNotFoundException(credentialId));

        // Mapear la credencial a CredentialDTO
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


    public HotelCredentialResponseDTO validateAccess(String token, Long hotelId, String passwordIngresada) {

        // 1. Validar token
        if (!jwtUtil.isTokenValid(token)) {
            log.warn("Token inválido: {}", token);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }

        // 2. Extraer userId del token
        Long userId = jwtUtil.extractUserId(token);
        log.info("Token válido. userId extraído: {}", userId);

        // 3. Buscar credencial
        Credential credencial = credentialRepository.findByUserIdAndHotelId(userId, hotelId)
                .orElseThrow(() -> {
                    log.warn("No se encontró credencial para userId={} y hotelId={}", userId, hotelId);
                    return new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes acceso a este hotel");
                });

        log.info("Credencial encontrada. Validando contraseña...");

        // 4. Validar contraseña
        if (!passwordEncoder.matches(passwordIngresada, credencial.getPassword())) {
            log.warn("Contraseña incorrecta para userId={} en hotelId={}", userId, hotelId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Contraseña incorrecta");
        }

        log.info("Contraseña válida. Acceso concedido a userId={} en hotelId={}", userId, hotelId);

        // 5. Preparar respuesta
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



}
