package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.CredentialDTO;
import org.iesvdm.pillowtaskerback.exception.CredentialNotFoundException;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    @Autowired
    CredentialRepository credentialRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelRepository hotelRepository;

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

        credential.setRol(credentialDetails.getRol());
        credential.setPassword(credentialDetails.getPassword());
        credential.setIncidents(credentialDetails.getIncidents());

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


    public List<CredentialDTO> getAllCredentialsDTObyHotelId(Long hotelId) {

        // Obtener las credenciales del hotel
        Set<Credential> credentialsSet = credentialRepository.findAllByHotel_Id(hotelId);

        // Mapear cada credencial a CredentialDTO
        return credentialsSet.stream()
                .map(credential -> new CredentialDTO(
                        credential.getId(),
                        credential.getRol(),
                        credential.getPassword(),
                        credential.getUser().getName(),
                        credential.getUser().getSurname1(),
                        credential.getUser().getSurname2(),
                        credential.getUser().getDni()
                ))
                .collect(Collectors.toList());
    }
}
