package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Empleado;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.service.EmpleadoService;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/hoteles")
public class HotelController {

    @Autowired
    EmpleadoService empleadoService;
    private final HotelService hotelService;

    public HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHoteles() {
        return ResponseEntity.ok(hotelService.all());
    }
    /*
    @GetMapping({"","/"})
    public ResponseEntity<List<HotelDTO>> all() {
        List<HotelDTO> hotels = hotelService.getAllHotelsDTOWithEmployeeCount();
        return ResponseEntity.ok(hotels);
    }

    @PostMapping({"","/"})
    public Hotel newHotel(@RequestBody Hotel hotel){
        log.info("Creando un hotel = " + hotel);
        return this.hotelService.save(hotel);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(hotelService.one(hotelId));
    }

    @PutMapping("/{id}")
    public Hotel replaceHotel(@PathVariable("id") Long id, @RequestBody Hotel hotel) {
        log.info("Actualizar hotel con id = " + id + "\n hotel" + hotel);
        return this.hotelService.replace(id, hotel);
    }
    */

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable ("id") Long id) {
        this.hotelService.delete(id);
    }

    @GetMapping("/{hotelId}/empleados")
    public ResponseEntity<List<Empleado>> getEmpleadosPorHotel(@PathVariable Long hotelId) {
        return ResponseEntity.ok(empleadoService.allByHotelId(hotelId));
    }

}

