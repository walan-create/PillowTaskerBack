package org.iesvdm.pillowtaskerback.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.service.HotelService;
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
    private final HotelService hotelService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.one(userId));
    }

    // Crear un hotel para un usuario específico
    @PostMapping("/{userId}/hotels")
    public ResponseEntity<Hotel> createHotelForUser(@PathVariable Long userId, @RequestBody Hotel hotel) {
        Hotel createdHotel = hotelService.createHotelForUser(userId, hotel);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHotel);
    }

    @GetMapping("/{userId}/hotels")
    public ResponseEntity<List<HotelDTO>> getAllHotelsByUserId(@PathVariable Long userId) {
        // Llamar al servicio para obtener los hoteles donde el usuario es dueño o tiene empleados
        List<HotelDTO> hotelDTOs = hotelService.getAllHotelsDTOByOwnerIdOrEmployeeId(userId);
        return ResponseEntity.ok(hotelDTOs);
    }

}
