package org.iesvdm.pillowtaskerback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Invitation;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.dto.HotelDTOAutoCreateCredential;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.iesvdm.pillowtaskerback.service.InvitationService;
import org.iesvdm.pillowtaskerback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final HotelService hotelService;

    @Autowired
    InvitationService invitationService;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.all());
    }

    // GET ONE
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.one(userId));
    }
    // CREATE
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    // UPDATE
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User user) {
        log.info("Updating user with id: {}", userId);
        User updatedUser = userService.replace(userId, user);
        return ResponseEntity.ok(updatedUser);
    }
    // DELETE
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*---------------CRUD PARCIAL HOTEL-----------------*/
    /*--------------------------------------------------*/

    // CREATE con User asociado
    @PostMapping("/{userId}/hotels")
    public ResponseEntity<Hotel> createHotelForUser(
            @PathVariable Long userId,
            @RequestBody HotelDTOAutoCreateCredential dto) {

        Hotel createdHotel = hotelService.createHotelForUser(userId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    // GET todos los hoteles a los que pertenece un usuario (ya sea como Owner o Employee)
    @GetMapping("/{userId}/hotels")
    public ResponseEntity<List<HotelDTO>> getAllHotelsByUserId(@PathVariable Long userId) {
        List<HotelDTO> hotelDTOs = hotelService.getAllHotelsDTOByOwnerIdOrCredentialId(userId);
        return ResponseEntity.ok(hotelDTOs);
    }

    /*--------------------------------------------------*/
    /*-----------CRUD PARCIAL INVITATIONS---------------*/
    /*--------------------------------------------------*/

    // Método GET para obtener todas las invitaciones de un correo electrónico
    @GetMapping("/invitations/email/{email}")
    public ResponseEntity<List<Invitation>> getInvitationsByEmail(@PathVariable String email) {
        List<Invitation> invitations = invitationService.getInvitationsByEmail(email);
        if (invitations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(invitations);
    }

    @DeleteMapping("/invitations/{invitationId}")
    public ResponseEntity<Void> deleteInvitationById(@PathVariable Long invitationId) {
        try {
            invitationService.deleteInvitationById(invitationId);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

}
