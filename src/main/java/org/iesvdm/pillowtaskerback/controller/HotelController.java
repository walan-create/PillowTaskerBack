package org.iesvdm.pillowtaskerback.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Room;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.service.EmployeeService;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.iesvdm.pillowtaskerback.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
    EmployeeService employeeService;

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
    /*------------------CRUD EMPLOYEES------------------*/
    /*--------------------------------------------------*/

    // GET ALL
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.all());
    }

    // GET ALL por Hotel
    @GetMapping("/{hotelId}/employees")
    public ResponseEntity<List<Employee>> getEmployeesByHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(employeeService.getEmployeesByHotel(hotelId));
    }

    // GET ONE
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeService.one(employeeId));
    }

    // CREATE con Hotel y User asociado
    @PostMapping("/{hotelId}/employees/user/{userId}")
    public ResponseEntity<Employee> createEmployee(
            @PathVariable Long hotelId,
            @PathVariable Long userId,
            @RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(hotelId, userId, employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    // UPDATE
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employee) {
        log.info("Updating employee with id: {}", employeeId);
        Employee updatedEmployee = employeeService.replace(employeeId, employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        employeeService.delete(employeeId);
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

