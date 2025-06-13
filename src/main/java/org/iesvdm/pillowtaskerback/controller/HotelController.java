package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.*;
import org.iesvdm.pillowtaskerback.dto.*;
import org.iesvdm.pillowtaskerback.security.JwtUtil;
import org.iesvdm.pillowtaskerback.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("v1/api/hotels")
@Validated
public class HotelController {

    @Autowired
    CredentialService credentialService;

    @Autowired
    BoardService boardService;

    @Autowired
    IncidentService incidentService;

    @Autowired
    ClientService clientService;

    @Autowired
    RoomService roomService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    InvitationService invitationService;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtil jwtUtil;

    private final HotelService hotelService;

    public HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        log.info("Accediendo a todos los hoteles");
        return ResponseEntity.ok(hotelService.all());
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.one(id);
        return hotel != null ? ResponseEntity.ok(hotel) : ResponseEntity.notFound().build();
    }

    // UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody @Valid Hotel hotel) {
        Hotel updatedHotel = hotelService.replace(id,hotel);
        return updatedHotel != null ? ResponseEntity.ok(updatedHotel) : ResponseEntity.notFound().build();
    }

    // DELETE CASCADE
    @ResponseBody
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable Long id) {
        hotelService.delete(id);
    }

    // GET BOARD DATA
    @GetMapping("/{hotelId}/board")
    public ResponseEntity<HotelBoardDTO> getHotelBoard(@PathVariable Long hotelId) {
        HotelBoardDTO hotelBoardDTO = boardService.getHotelBoardByHotelId(hotelId);
        return ResponseEntity.ok(hotelBoardDTO);
    }

    /*--------------------------------------------------*/
    /*----------------CRUD INVITATIONS------------------*/
    /*--------------------------------------------------*/

    // ENVIAR INVITACION
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN'})")
    @PostMapping("/{hotelId}/invitations")
    public ResponseEntity<Invitation> sendInvitation(@PathVariable Long hotelId, @RequestBody @Valid Invitation invitation) {
        Invitation createdInvitation = invitationService.sendInvitation(hotelId, invitation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
    }

    /*--------------------------------------------------*/
    /*-----------------CRUD CREDENTIAL------------------*/
    /*--------------------------------------------------*/

    // GET ALL by Hotel
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN'})")
    @GetMapping("/{hotelId}/credentials")
    public ResponseEntity<List<CredentialDTO>> getCredentialsDTOByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(credentialService.getAllCredentialsDTObyHotelId(hotelId));
    }

    // CREATE credential con Hotel y User asociado
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN'})")
    @PostMapping("/{hotelId}/credentials/user/{userId}")
    public ResponseEntity<Credential> createCredential(
            @PathVariable Long hotelId,
            @PathVariable Long userId,
            @RequestBody @Valid Credential credential) {
        Credential createdCredential = credentialService.createCredential(hotelId, userId, credential);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCredential);
    }

    // UPDATE by hotel
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN'})")
    @PutMapping("/{hotelId}/credentials/{credentialId}")
    public ResponseEntity<Credential> updateCredential(
            @PathVariable Long hotelId,
            @PathVariable Long credentialId,
            @RequestBody @Valid Credential credential) {
        log.info("Updating employee with id: {}", credentialId);
        Credential updatedCredential = credentialService.replace(credentialId, credential);
        return ResponseEntity.ok(updatedCredential);
    }

    // DELETE by hotel
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN'})")
    @DeleteMapping("/{hotelId}/credentials/{credentialId}")
    public ResponseEntity<Void> deleteCredential(
            @PathVariable Long hotelId,
            @PathVariable Long credentialId) {
        credentialService.delete(credentialId);
        return ResponseEntity.noContent().build();
    }

    // VALIDATE CREDENTIAL
    @PostMapping("/credentials/validate")
    public ResponseEntity<HotelCredentialResponseDTO> validarAccesoHotel(
            @RequestBody HotelAccessDTO hotelAccessDTO,
            @RequestHeader("Authorization") String header) {

        String token = header.replace("Bearer ", "").split(",")[0].trim();

        HotelCredentialResponseDTO responseDTO = credentialService.validateAccess(
                token,
                hotelAccessDTO.getHotelId(),
                hotelAccessDTO.getPassword()
        );

        return ResponseEntity.ok(responseDTO);
    }

    /*--------------------------------------------------*/
    /*--------------------CRUD ROOMS--------------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST','MAINTENANCE','CLEANER'})")
    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    // GET ONE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @GetMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.one(roomId));
    }

    // CREATE con Hotel asociado
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PostMapping("/{hotelId}/rooms")
    public ResponseEntity<Room> createRoom(
            @PathVariable Long hotelId,
            @RequestBody @Valid Room room) {
        Room createdRoom = roomService.createRoomForHotel(hotelId, room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    // UPDATE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST','MAINTENANCE','CLEANER'})")
    @PutMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long hotelId, @PathVariable Long roomId, @RequestBody @Valid Room room) {
        log.info("Updating room with id: {}", roomId);
        Room updatedRoom = roomService.replace(roomId, room);
        return ResponseEntity.ok(updatedRoom);
    }

    // DELETE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @DeleteMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long hotelId, @PathVariable Long roomId) {
        roomService.delete(roomId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*------------------CRUD CLIENTS--------------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @GetMapping("/{hotelId}/clients")
    public ResponseEntity<List<Client>> getClientsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(clientService.getClientsByHotel(hotelId));
    }
    // GET ONE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @GetMapping("/{hotelId}/clients/{clientId}")
    public ResponseEntity<Client> getClient(@PathVariable Long hotelId, @PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.one(clientId));
    }
    // CREATE con Hotel y User asociado
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PostMapping("/{hotelId}/clients")
    public ResponseEntity<Client> createClient(
            @PathVariable Long hotelId,
            @RequestBody @Valid Client client) {
        Client createdClient = clientService.createClientForHotel(hotelId, client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }

    // UPDATE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PutMapping("/{hotelId}/clients/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long hotelId, @PathVariable Long clientId, @RequestBody @Valid Client client) {
        log.info("Updating client with id: {}", clientId);
        Client updatedClient = clientService.replace(clientId, client);
        return ResponseEntity.ok(updatedClient);
    }
    // DELETE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @DeleteMapping("/{hotelId}/clients/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long hotelId, @PathVariable Long clientId) {
        clientService.delete(clientId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*----------------CRUD INCIDENTS--------------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @GetMapping("/{hotelId}/incidents")
    public ResponseEntity<List<Incident>> getIncidentsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(incidentService.getIncidentsByHotel(hotelId));
    }

    // GET ONE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @GetMapping("/{hotelId}/incidents/{incidentId}")
    public ResponseEntity<Incident> getIncident(@PathVariable Long hotelId, @PathVariable Long incidentId) {
        return ResponseEntity.ok(incidentService.one(incidentId));
    }

    // CREATE con Hotel y User asociado
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PostMapping("/{hotelId}/incidents")
    public ResponseEntity<Incident> createIncident(
            @PathVariable Long hotelId,
            @RequestBody @Valid Incident incident) {
        Incident createdIncident = incidentService.createIncidentForHotel(hotelId, incident);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIncident);
    }

    // UPDATE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PutMapping("/{hotelId}/incidents/{incidentId}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long hotelId, @PathVariable Long incidentId, @RequestBody @Valid Incident incident) {
        log.info("Updating incident with id: {}", incidentId);
        Incident updatedIncident = incidentService.replace(incidentId, incident);
        return ResponseEntity.ok(updatedIncident);
    }
    // DELETE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @DeleteMapping("/{hotelId}/incidents/{incidentId}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long hotelId, @PathVariable Long incidentId) {
        incidentService.delete(incidentId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*----------------CRUD RESERVATIONS-----------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @GetMapping("/{hotelId}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reservationService.getReservationsByHotel(hotelId));
    }

    // GET ONE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @GetMapping("/{hotelId}/reservations/{reservationId}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long hotelId, @PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.one(reservationId));
    }

    // CREATE con varias habitaciones asociadas
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PostMapping("/{hotelId}/reservations")
    public ResponseEntity<Reservation> createReservation(
            @PathVariable Long hotelId,
            @RequestBody @Valid ReservationDTO reservationDTO) {

        Reservation createdReservation = reservationService.createReservation(hotelId, reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    // UPDATE con validación de solapamiento entre fechas de reservas
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PutMapping("/{hotelId}/reservations/{reservationId}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long hotelId, @PathVariable Long reservationId, @RequestBody @Valid ReservationDTO reservationDTO) {
        log.info("Updating reservation with id: {}", reservationId);
        Reservation updatedReservation = reservationService.replace(reservationId, reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    // CHECK-IN
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PatchMapping("/{hotelId}/reservations/{reservationId}/checkin")
    public ResponseEntity<Reservation> checkInReservation(
            @PathVariable Long hotelId,
            @PathVariable Long reservationId,
            @RequestBody @Valid ReservationDTO reservationDTO) {

        Reservation updatedReservation = reservationService.checkInReservation(hotelId, reservationId, reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    // CHECK-OUT
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @PatchMapping("/{hotelId}/reservations/{reservationId}/checkout")
    public ResponseEntity<Reservation> checkOutReservation(
            @PathVariable Long hotelId,
            @PathVariable Long reservationId) {

        Reservation updatedReservation = reservationService.checkOutReservation(hotelId, reservationId);
        System.out.println(updatedReservation);
        return ResponseEntity.ok(updatedReservation);
    }

    // DELETE
    @PreAuthorize("@credentialService.hasRoleInHotel(authentication.name, #hotelId, {'ADMIN', 'RECEPTIONIST'})")
    @DeleteMapping("/{hotelId}/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long hotelId, @PathVariable Long reservationId) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}