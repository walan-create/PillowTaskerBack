package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Reserva;
import org.iesvdm.pillowtaskerback.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/reservas")
@Controller
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService){
        this.reservaService = reservaService;
    }

    @GetMapping({"","/"})
    public List<Reserva> all() {
        log.info("Accediendo a todos los reservaes");
        return this.reservaService.all();
    }

    @PostMapping({"","/"})
    public Reserva newReserva(@RequestBody Reserva reserva){
        log.info("Creando un reserva = " + reserva);
        return this.reservaService.save(reserva);
    }

    @GetMapping("/{id}")
    public Reserva one(@PathVariable("id") Long id) {
        log.info("Buscar reserva con id: " + id);
        return this.reservaService.one(id);
    }

    @PutMapping("/{id}")
    public Reserva replaceReserva(@PathVariable("id") Long id, @RequestBody Reserva reserva) {
        log.info("Actualizar reserva con id = " + id + "\n reserva" + reserva);
        return this.reservaService.replace(id, reserva);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteReserva(@PathVariable ("id") Long id) {
        this.reservaService.delete(id);
    }

}

