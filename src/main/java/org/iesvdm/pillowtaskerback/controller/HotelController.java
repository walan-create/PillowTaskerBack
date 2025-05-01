package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.*;
import org.iesvdm.pillowtaskerback.dto.CredentialDTO;
import org.iesvdm.pillowtaskerback.dto.ReservationDTO;
import org.iesvdm.pillowtaskerback.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/hotels")
@Validated
public class HotelController {

    @Autowired
    CredentialService credentialService;

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
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable Long id, @RequestBody Hotel hotel) {
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

    /*--------------------------------------------------*/
    /*----------------CRUD INVITATIONS------------------*/
    /*--------------------------------------------------*/

    // ENVIAR INVITACION
    @PostMapping("/{hotelId}/invitations")
    public ResponseEntity<Invitation> sendInvitation(@PathVariable Long hotelId, @RequestBody Invitation invitation) {
        Invitation createdInvitation = invitationService.sendInvitation(hotelId, invitation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInvitation);
    }



    /*--------------------------------------------------*/
    /*-----------------CRUD CREDENTIAL------------------*/
    /*--------------------------------------------------*/

    // GET ALL by Hotel
    @GetMapping("/{hotelId}/credentials")
    public ResponseEntity<List<CredentialDTO>> getCredentialsDTOByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(credentialService.getAllCredentialsDTObyHotelId(hotelId));
    }

    // GET ONE by hotel
    @GetMapping("/{hotelId}/{credentialId}")
    public ResponseEntity<Credential> getCredential(@PathVariable Long credentialId) {
        return ResponseEntity.ok(credentialService.one(credentialId));
    }

    // CREATE credential con Hotel y User asociado
    @PostMapping("/{hotelId}/credentials/user/{userId}")
    public ResponseEntity<Credential> createCredential(
            @PathVariable Long hotelId,
            @PathVariable Long userId,
            @RequestBody Credential credential) {
        Credential createdCredential = credentialService.createCredential(hotelId, userId, credential);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCredential);
    }
    // UPDATE by hotel
    @PutMapping("/{hotelId}/credentials/{credentialId}")
    public ResponseEntity<Credential> updateCredential(@PathVariable Long credentialId, @RequestBody Credential credential) {
        log.info("Updating employee with id: {}", credentialId);
        Credential updatedCredential = credentialService.replace(credentialId, credential);
        return ResponseEntity.ok(updatedCredential);
    }

    // DELETE by hotel
    @DeleteMapping("/{hotelId}/credentials/{credentialId}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long credentialId) {
        credentialService.delete(credentialId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*--------------------CRUD ROOMS--------------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }

    // GET ONE
    @GetMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.one(roomId));
    }

    // CREATE con Hotel asociado
    @PostMapping("/{hotelId}/rooms")
    public ResponseEntity<Room> createRoom(
            @PathVariable Long hotelId,
            @RequestBody Room room) {
        Room createdRoom = roomService.createRoomForHotel(hotelId, room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }

    // UPDATE
    @PutMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long roomId, @RequestBody Room room) {
        log.info("Updating room with id: {}", roomId);
        Room updatedRoom = roomService.replace(roomId, room);
        return ResponseEntity.ok(updatedRoom);
    }

    // DELETE
    @DeleteMapping("/{hotelId}/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.delete(roomId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*------------------CRUD CLIENTS--------------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @GetMapping("/{hotelId}/clients")
    public ResponseEntity<List<Client>> getClientsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(clientService.getClientsByHotel(hotelId));
    }
    // GET ONE
    @GetMapping("/{hotelId}/clients/{clientId}")
    public ResponseEntity<Client> getClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(clientService.one(clientId));
    }
    // CREATE con Hotel y User asociado
    @PostMapping("/{hotelId}/clients")
    public ResponseEntity<Client> createClient(
            @PathVariable Long hotelId,
            @RequestBody Client client) {
        Client createdClient = clientService.createClientForHotel(hotelId, client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }
    // UPDATE
    @PutMapping("/{hotelId}/clients/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody Client client) {
        log.info("Updating client with id: {}", clientId);
        Client updatedClient = clientService.replace(clientId, client);
        return ResponseEntity.ok(updatedClient);
    }
    // DELETE
    @DeleteMapping("/{hotelId}/clients/{clientId}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        clientService.delete(clientId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*----------------CRUD INCIDENTS--------------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @GetMapping("/{hotelId}/incidents")
    public ResponseEntity<List<Incident>> getIncidentsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(incidentService.getIncidentsByHotel(hotelId));
    }
    // GET ONE
    @GetMapping("/{hotelId}/incidents/{incidentId}")
    public ResponseEntity<Incident> getIncident(@PathVariable Long incidentId) {
        return ResponseEntity.ok(incidentService.one(incidentId));
    }
    // CREATE con Hotel y User asociado
    @PostMapping("/{hotelId}/incidents/credential/{credentialId}")
    public ResponseEntity<Incident> createIncident(
            @PathVariable Long hotelId,
            @PathVariable Long credentialId,
            @RequestBody Incident incident) {
        Incident createdIncident = incidentService.createIncidentForHotel(hotelId, credentialId, incident);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdIncident);
    }
    // UPDATE
    @PutMapping("/{hotelId}/incidents/{incidentId}")
    public ResponseEntity<Incident> updateIncident(@PathVariable Long incidentId, @RequestBody Incident incident) {
        log.info("Updating incident with id: {}", incidentId);
        Incident updatedIncident = incidentService.replace(incidentId, incident);
        return ResponseEntity.ok(updatedIncident);
    }
    // DELETE
    @DeleteMapping("/{hotelId}/incidents/{incidentId}")
    public ResponseEntity<Void> deleteIncident(@PathVariable Long incidentId) {
        incidentService.delete(incidentId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*----------------CRUD RESERVATIONS-----------------*/
    /*--------------------------------------------------*/

    // GET ALL por Hotel
    @GetMapping("/{hotelId}/reservations")
    public ResponseEntity<List<Reservation>> getReservationsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reservationService.getReservationsByHotel(hotelId));
    }

    // GET ONE
    @GetMapping("/{hotelId}/reservations/{reservationId}")
    public ResponseEntity<Reservation> getReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(reservationService.one(reservationId));
    }

    // CREATE con varias habitaciones asociadas
    @PostMapping("/{hotelId}/reservations")
    public ResponseEntity<Reservation> createReservation(
            @PathVariable Long hotelId,
            @RequestBody ReservationDTO reservationDTO) {

        Reservation createdReservation = reservationService.createReservation(hotelId, reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    // CREATE Se amplia reserva (se crea una nueva y se pasa como parametro el id de la anterior)
    @PostMapping("/{hotelId}/reservations/{reservationId}")
    public ResponseEntity<Reservation> extendReservation(
            @PathVariable Long hotelId,
            @PathVariable Long reservationId,
            @RequestBody ReservationDTO reservationDTO) {

        Reservation createdReservation = reservationService.expandReservation(hotelId,reservationId, reservationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    // UPDATE con validación de solapamiento entre fechas de reservas
    @PutMapping("/{hotelId}/reservations/{reservationId}")
    public ResponseEntity<Reservation> updateIncident(@PathVariable Long reservationId, @RequestBody ReservationDTO reservationDTO) {
        log.info("Updating reservation with id: {}", reservationId);
        Reservation updatedReservation = reservationService.replace(reservationId, reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    // CHECK-IN
    @PatchMapping("/{hotelId}/reservations/{reservationId}/checkin")
    public ResponseEntity<Reservation> checkInReservation(
            @PathVariable Long hotelId,
            @PathVariable Long reservationId,
            @RequestBody ReservationDTO reservationDTO) {

        Reservation updatedReservation = reservationService.checkInReservation(hotelId, reservationId, reservationDTO);
        return ResponseEntity.ok(updatedReservation);
    }

    // CHECK-IN
    @PatchMapping("/{hotelId}/reservations/{reservationId}/checkout")
    public ResponseEntity<Reservation> checkOutReservation(
            @PathVariable Long hotelId,
            @PathVariable Long reservationId) {

        Reservation updatedReservation = reservationService.checkOutReservation(hotelId, reservationId);
        return ResponseEntity.ok(updatedReservation);
    }

    // DELETE
    @DeleteMapping("/{hotelId}/reservations/{reservationId}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long reservationId) {
        reservationService.delete(reservationId);
        return ResponseEntity.noContent().build();
    }
}

