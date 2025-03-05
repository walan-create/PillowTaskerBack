package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.service.EmployeeService;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/hoteles")
public class HotelController {

    @Autowired
    EmployeeService employeeService;
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

//    @GetMapping("/{hotelId}/empleados")
//    public ResponseEntity<List<Employee>> getEmpleadosPorHotel(@PathVariable Long hotelId) {
//        return ResponseEntity.ok(employeeService.allByHotelId(hotelId));
//    }

}

