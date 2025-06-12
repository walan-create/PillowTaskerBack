package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Invitation;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.enums.InvitationStateEnum;
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.InvitationRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvitationService {

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Guarda una nueva invitación en la base de datos y actualiza su estado.
     *
     * @param invitation invitación a guardar
     * @return invitación guardada
     */
    @Transactional
    public Invitation save(Invitation invitation) {
        invitationRepository.save(invitation);
        entityManager.refresh(invitation);
        return invitation;
    }

    /**
     * Obtiene todas las invitaciones asociadas a un correo electrónico.
     *
     * @param email correo electrónico del usuario
     * @return lista de invitaciones encontradas
     */
    public List<Invitation> getInvitationsByMail(String email) {
        return invitationRepository.findByMail(email);
    }

    /**
     * Envía una invitación para un hotel con un tipo de rol especificado.
     *
     * @param hotelId ID del hotel al que se envía la invitación
     * @param invitation objeto de la invitación a enviar
     * @return invitación guardada en la base de datos
     * @throws ApiException si el hotel no existe, ya existe una invitación para ese correo, el usuario no existe o ya pertenece al hotel
     */
    @Transactional
    public Invitation sendInvitation(Long hotelId, Invitation invitation) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("No se encontró el hotel con ID: " + hotelId, HttpStatus.NOT_FOUND));

        boolean invitationExists = invitationRepository.existsByMailAndHotelId(invitation.getMail(), hotelId);
        if (invitationExists) {
            throw new ApiException("Ya existe una invitación para este correo electrónico", HttpStatus.BAD_REQUEST);
        }

        User user = userRepository.findByMail(invitation.getMail())
                .orElseThrow(() -> new ApiException("No existe ningún usuario con el mail proporcionado", HttpStatus.NOT_FOUND));

        boolean hasCredential = credentialRepository.findAllByHotel_Id(hotelId)
                .stream()
                .anyMatch(credential -> credential.getUser().getId().equals(user.getId()));
        if (hasCredential) {
            throw new ApiException("El usuario ya pertenece al hotel", HttpStatus.BAD_REQUEST);
        }

        invitation.setState(InvitationStateEnum.PENDING);
        invitation.setHotel(hotel);
        invitation.setShippingDate(LocalDateTime.now());

        return save(invitation);
    }

    /**
     * Procesa la respuesta de una invitación (aceptada o rechazada).
     *
     * @param invitationId ID de la invitación a procesar
     * @param accepted indica si la invitación fue aceptada o rechazada
     * @param password contraseña para la credencial (si se acepta)
     * @return true si la operación fue exitosa
     * @throws ApiException si la invitación o el usuario no existen
     */
    public boolean processInvitationResponse(Long invitationId, boolean accepted, String password) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ApiException("Invitación no encontrada con ID: " + invitationId, HttpStatus.NOT_FOUND));

        if (accepted) {
            User user = userRepository.findByMail(invitation.getMail())
                    .orElseThrow(() -> new ApiException("No existe ningún usuario con el mail proporcionado", HttpStatus.NOT_FOUND));
            Credential credential = new Credential();
            credential.setHotel(invitation.getHotel());
            credential.setUser(user);
            credential.setRol(invitation.getCredentialType());
            credential.setPassword(passwordEncoder.encode(password));
            credentialRepository.save(credential);
        }

        invitationRepository.delete(invitation);
        return true;
    }

    /**
     * Elimina una invitación específica por su ID.
     *
     * @param invitationId ID de la invitación a eliminar
     * @throws ApiException si la invitación no existe
     */
    @Transactional
    public void deleteInvitationById(Long invitationId) {
        if (!invitationRepository.existsById(invitationId)) {
            throw new ApiException("Invitación no encontrada con ID: " + invitationId, HttpStatus.NOT_FOUND);
        }
        invitationRepository.deleteById(invitationId);
    }

}