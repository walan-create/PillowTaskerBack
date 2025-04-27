package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Incident;
import org.iesvdm.pillowtaskerback.service.IncidentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/incidents")
@Controller
public class incidentController {
    private final IncidentService incidentService;

    public incidentController(IncidentService incidentService){
        this.incidentService = incidentService;
    }

    @GetMapping({"","/"})
    public List<Incident> all() {
        log.info("Accediendo a todos los incidenciaes");
        return this.incidentService.all();
    }

    @PostMapping({"","/"})
    public Incident newIncident(@RequestBody Incident incident){
        log.info("Creando un incident = " + incident);
        return this.incidentService.save(incident);
    }

    @GetMapping("/{id}")
    public Incident one(@PathVariable("id") Long id) {
        log.info("Buscar incidencia con id: " + id);
        return this.incidentService.one(id);
    }

    @PutMapping("/{id}")
    public Incident replaceIncident(@PathVariable("id") Long id, @RequestBody Incident incident) {
        log.info("Actualizar incident con id = " + id + "\n incident" + incident);
        return this.incidentService.replace(id, incident);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteIncident(@PathVariable ("id") Long id) {
        this.incidentService.delete(id);
    }

}

