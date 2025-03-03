package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Habitacion;
import org.iesvdm.pillowtaskerback.repository.HabitacionRepository;
import org.iesvdm.pillowtaskerback.service.HabitacionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/habitaciones")
@Controller
public class HabitacionController {
    private final HabitacionService habitacionService;

    public HabitacionController(HabitacionService habitacionService){
        this.habitacionService = habitacionService;
    }

    @GetMapping({"","/"})
    public List<Habitacion> all() {
        log.info("Accediendo a todos los habitaciones");
        return this.habitacionService.all();
    }

    @PostMapping({"","/"})
    public Habitacion newHabitacion(@RequestBody Habitacion habitacion){
        log.info("Creando un habitacion = " + habitacion);
        return this.habitacionService.save(habitacion);
    }

    @GetMapping("/{id}")
    public Habitacion one(@PathVariable("id") Long id) {
        log.info("Buscar habitacion con id: " + id);
        return this.habitacionService.one(id);
    }

    @PutMapping("/{id}")
    public Habitacion replaceHabitacion(@PathVariable("id") Long id, @RequestBody Habitacion habitacion) {
        log.info("Actualizar habitacion con id = " + id + "\n habitacion" + habitacion);
        return this.habitacionService.replace(id, habitacion);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteHabitacion(@PathVariable ("id") Long id) {
        this.habitacionService.delete(id);
    }

}

