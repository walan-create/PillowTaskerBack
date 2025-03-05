package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Incidence;
import org.iesvdm.pillowtaskerback.service.IncidenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/incidencias")
@Controller
public class IncidenceController {
    private final IncidenciaService incidenciaService;

    public IncidenceController(IncidenciaService incidenciaService){
        this.incidenciaService = incidenciaService;
    }

    @GetMapping({"","/"})
    public List<Incidence> all() {
        log.info("Accediendo a todos los incidenciaes");
        return this.incidenciaService.all();
    }

    @PostMapping({"","/"})
    public Incidence newIncidencia(@RequestBody Incidence incidence){
        log.info("Creando un incidence = " + incidence);
        return this.incidenciaService.save(incidence);
    }

    @GetMapping("/{id}")
    public Incidence one(@PathVariable("id") Long id) {
        log.info("Buscar incidencia con id: " + id);
        return this.incidenciaService.one(id);
    }

    @PutMapping("/{id}")
    public Incidence replaceIncidencia(@PathVariable("id") Long id, @RequestBody Incidence incidence) {
        log.info("Actualizar incidence con id = " + id + "\n incidence" + incidence);
        return this.incidenciaService.replace(id, incidence);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteIncidencia(@PathVariable ("id") Long id) {
        this.incidenciaService.delete(id);
    }

}

