package org.iesvdm.pillowtaskerback.service;

import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Invitation;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.enums.InvitationStateEnum;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.UsuarioByMailNotFoundException;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.InvitationRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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


    // Método para obtener todas las invitaciones de un correo electrónico
    public List<Invitation> getInvitationsByMail(String email) {
        return invitationRepository.findByMail(email);
    }

    /**
     * Enviar una invitación para un hotel con un tipo de rol especificado.
     *
     * @param hotelId ID del hotel al que se envía la invitación.
     * @param invitation Objeto de la invitación a enviar.
     * @return La invitación guardada en la base de datos.
     */
    @Transactional
    public Invitation sendInvitation(Long hotelId, Invitation invitation) {
        // Buscar el hotel por ID
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        // Verificar si ya existe una invitación con ese correo electrónico y hotel
        boolean invitationExists = invitationRepository.existsByMailAndHotelId(invitation.getMail(), hotelId);
        if (invitationExists) {
            throw new RuntimeException("Ya existe una invitación para este correo electrónico y este hotel");
        }

        // Asignar los datos a la invitación
        invitation.setHotel(hotel);
        invitation.setShippingDate(LocalDateTime.now());
        invitation.setState(InvitationStateEnum.PENDING);

        // Guardar la invitación
        return invitationRepository.save(invitation);
    }


    /**
     * Procesar la respuesta de una invitación (aceptada o rechazada).
     *
     * @param invitationId ID de la invitación a procesar.
     * @param accepted Indica si la invitación fue aceptada o rechazada.
     * @param password Contraseña para la credencial.
     * @return `true` si la operación fue exitosa, `false` si no.
     */
    public boolean processInvitationResponse(Long invitationId, boolean accepted, String password) {
        // Buscar la invitación por ID
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitación no encontradacon ID:" + invitationId));

        if (accepted) {
            System.out.println("Aceptada");
            // Buscar el usuario por correo (asociado con la invitación)
            User user = userRepository.findByMail(invitation.getMail())
                    .orElseThrow(() -> new UsuarioByMailNotFoundException(invitation.getMail()));
            System.out.println("Usuario encontrado: "+user.getName());
            // Crear la credencial
            Credential credential = new Credential();
            credential.setHotel(invitation.getHotel());
            credential.setUser(user);
            credential.setRol(invitation.getCredentialType()); // Asignar el rol de la invitación a la credencial
            credential.setPassword(password);

            // Guardar la credencial
            credentialRepository.save(credential);
        }

        // Eliminar la invitación en cualquier caso (aceptada o rechazada)
        invitationRepository.delete(invitation);
        return true;
    }

    // Método para eliminar una invitación específica basada en el correo electrónico y el ID de la invitación
    @Transactional
    public void deleteInvitationById(Long invitationId) {
        if (!invitationRepository.existsById(invitationId)) {
            throw new RuntimeException("Invitación no encontrada con ID: " + invitationId);
        }
        invitationRepository.deleteById(invitationId);
    }

}
