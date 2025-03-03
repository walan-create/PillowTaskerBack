package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Incidencia;
import org.iesvdm.pillowtaskerback.repository.IncidenciaRepository;
import org.iesvdm.pillowtaskerback.service.IncidenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/incidencias")
@Controller
public class IncidenciaController {
    private final IncidenciaService incidenciaService;

    public IncidenciaController(IncidenciaService incidenciaService){
        this.incidenciaService = incidenciaService;
    }

    @GetMapping({"","/"})
    public List<Incidencia> all() {
        log.info("Accediendo a todos los incidenciaes");
        return this.incidenciaService.all();
    }

    @PostMapping({"","/"})
    public Incidencia newIncidencia(@RequestBody Incidencia incidencia){
        log.info("Creando un incidencia = " + incidencia);
        return this.incidenciaService.save(incidencia);
    }

    @GetMapping("/{id}")
    public Incidencia one(@PathVariable("id") Long id) {
        log.info("Buscar incidencia con id: " + id);
        return this.incidenciaService.one(id);
    }

    @PutMapping("/{id}")
    public Incidencia replaceIncidencia(@PathVariable("id") Long id, @RequestBody Incidencia incidencia) {
        log.info("Actualizar incidencia con id = " + id + "\n incidencia" + incidencia);
        return this.incidenciaService.replace(id, incidencia);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteIncidencia(@PathVariable ("id") Long id) {
        this.incidenciaService.delete(id);
    }

}

