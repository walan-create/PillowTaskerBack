package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("v1/api/empleados")
@Controller
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/{empleadoId}")
    public ResponseEntity<Employee> getEmpleado(@PathVariable Long empleadoId) {
        return ResponseEntity.ok(employeeService.one(empleadoId));
    }

    @DeleteMapping("/{empleadoId}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable Long empleadoId) {
        employeeService.delete(empleadoId);
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

