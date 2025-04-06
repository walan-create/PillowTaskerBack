package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.dto.CredentialDTO;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Room;
import org.iesvdm.pillowtaskerback.service.CredentialService;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.iesvdm.pillowtaskerback.service.RoomService;
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
    RoomService roomService;

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
    /*-----------------CRUD CREDENTIAL------------------*/
    /*--------------------------------------------------*/

    // GET ALL
    @GetMapping("/credentials")
    public ResponseEntity<List<Credential>> getAllCredentials() {
        return ResponseEntity.ok(credentialService.all());
    }

    // GET ALL por Hotel
    @GetMapping("/{hotelId}/credentials")
    public ResponseEntity<List<CredentialDTO>> getCredentialsDTOByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(credentialService.getAllCredentialsDTObyHotelId(hotelId));
    }

    // GET ONE
    @GetMapping("/credentials/{credentialId}")
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

    // UPDATE
    @PutMapping("/credentials/{credentialId}")
    public ResponseEntity<Credential> updateCredential(@PathVariable Long credentialId, @RequestBody Credential credential) {
        log.info("Updating employee with id: {}", credentialId);
        Credential updatedCredential = credentialService.replace(credentialId, credential);
        return ResponseEntity.ok(updatedCredential);
    }

    // DELETE
    @DeleteMapping("/credentials/{credentialId}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long credentialId) {
        credentialService.delete(credentialId);
        return ResponseEntity.noContent().build();
    }

    /*--------------------------------------------------*/
    /*--------------------CRUD ROOMS--------------------*/
    /*--------------------------------------------------*/

    // GET ALL
    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.all());
    }
    // GET ALL por Hotel
    @GetMapping("/{hotelId}/rooms")
    public ResponseEntity<List<Room>> getRoomsByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getRoomsByHotel(hotelId));
    }
    // GET ONE
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.one(roomId));
    }
    // CREATE con Hotel y User asociado
    @PostMapping("/{hotelId}/rooms")
    public ResponseEntity<Room> createRoom(
            @PathVariable Long hotelId,
            @RequestBody Room room) {
        Room createdRoom = roomService.createRoom(hotelId, room);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRoom);
    }
    // UPDATE
    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long roomId, @RequestBody Room room) {
        log.info("Updating room with id: {}", roomId);
        Room updatedRoom = roomService.replace(roomId, room);
        return ResponseEntity.ok(updatedRoom);
    }
    // DELETE
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.delete(roomId);
        return ResponseEntity.noContent().build();
    }

}

