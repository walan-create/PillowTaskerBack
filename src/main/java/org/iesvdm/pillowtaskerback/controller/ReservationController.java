package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Reservation;
import org.iesvdm.pillowtaskerback.dto.ReservationDTO;
import org.iesvdm.pillowtaskerback.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/reservas")
@Controller
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @GetMapping({"","/"})
    public List<Reservation> all() {
        log.info("Accediendo a todos los reservaes");
        return this.reservationService.all();
    }

    @PostMapping({"","/"})
    public Reservation newReserva(@RequestBody Reservation reservation){
        log.info("Creando un reservation = " + reservation);
        return this.reservationService.save(reservation);
    }

    @GetMapping("/{id}")
    public Reservation one(@PathVariable("id") Long id) {
        log.info("Buscar reserva con id: " + id);
        return this.reservationService.one(id);
    }

    @PutMapping("/{id}")
    public Reservation replaceReserva(@PathVariable("id") Long id, @RequestBody ReservationDTO reservationDTO) {
        log.info("Actualizar reservation con id = " + id + "\n reservation" + reservationDTO);
        return this.reservationService.replace(id, reservationDTO);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReserva(@PathVariable ("id") Long id) {
        this.reservationService.delete(id);
    }

}

