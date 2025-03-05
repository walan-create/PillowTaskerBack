package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Empleado;
import org.iesvdm.pillowtaskerback.repository.EmpleadoRepository;
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
@RequestMapping("v1/api/empleados")
@Controller
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/{empleadoId}")
    public ResponseEntity<Empleado> getEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(empleadoService.one(empleadoId));
    }

    @DeleteMapping("/{empleadoId}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long empleadoId) {
        empleadoService.delete(empleadoId);
        return ResponseEntity.noContent().build();
    }
    /*
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}/hoteles/{hotelId}")
    public void deleteEmpleadoFromHotel(@PathVariable ("id") Long id,@PathVariable ("hotelId") Long hotelId) {
        this.hotelService.deleteEmpleadoFromHotel(id,hotelId);
    }*/

}

