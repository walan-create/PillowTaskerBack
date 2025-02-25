package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Empleado;
import org.iesvdm.pillowtaskerback.repository.EmpleadoRepository;
import org.iesvdm.pillowtaskerback.service.EmpleadoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/empleados")
@Controller
public class EmpleadoController {
    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService){
        this.empleadoService = empleadoService;
    }

    @GetMapping({"","/"})
    public List<Empleado> all() {
        log.info("Accediendo a todos los empleados");
        return this.empleadoService.all();
    }

    @PostMapping({"","/"})
    public Empleado newEmpleado(@RequestBody Empleado empleado){
        log.info("Creando un empleado = " + empleado);
        return this.empleadoService.save(empleado);
    }

    @GetMapping("/{id}")
    public Empleado one(@PathVariable("id") Long id) {
        log.info("Buscar empleado con id: " + id);
        return this.empleadoService.one(id);
    }

    @PutMapping("/{id}")
    public Empleado replaceEmpleado(@PathVariable("id") Long id, @RequestBody Empleado empleado) {
        log.info("Actualizar empleado con id = " + id + "\n empleado" + empleado);
        return this.empleadoService.replace(id, empleado);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteEmpleado(@PathVariable ("id") Long id) {
        this.empleadoService.delete(id);
    }

}

