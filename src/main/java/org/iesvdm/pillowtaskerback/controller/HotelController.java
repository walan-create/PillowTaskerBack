package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/hoteles")
@Controller
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService){
        this.hotelService = hotelService;
    }

    @GetMapping({"","/"})
    public List<Hotel> all() {
        log.info("Accediendo a todos los hoteles");
        return this.hotelService.all();
    }

    @PostMapping({"","/"})
    public Hotel newHotel(@RequestBody Hotel hotel){
        log.info("Creando un hotel = " + hotel);
        return this.hotelService.save(hotel);
    }

    @GetMapping("/{id}")
    public Hotel one(@PathVariable("id") Long id) {
        log.info("Buscar hotel con id: " + id);
        return this.hotelService.one(id);
    }

    @PutMapping("/{id}")
    public Hotel replaceHotel(@PathVariable("id") Long id, @RequestBody Hotel hotel) {
        log.info("Actualizar hotel con id = " + id + "\n hotel" + hotel);
        return this.hotelService.replace(id, hotel);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteHotel(@PathVariable ("id") Long id) {
        this.hotelService.delete(id);
    }

}

