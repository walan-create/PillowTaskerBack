package org.iesvdm.pillowtaskerback.controller;

import lombok.extern.slf4j.Slf4j;
import org.iesvdm.pillowtaskerback.domain.Room;
import org.iesvdm.pillowtaskerback.service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("v1/api/habitaciones")
@Controller
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }

    @GetMapping({"","/"})
    public List<Room> all() {
        log.info("Accediendo a todos los habitaciones");
        return this.roomService.all();
    }

    @PostMapping({"","/"})
    public Room newHabitacion(@RequestBody Room room){
        log.info("Creando un room = " + room);
        return this.roomService.save(room);
    }

    @GetMapping("/{id}")
    public Room one(@PathVariable("id") Long id) {
        log.info("Buscar habitacion con id: " + id);
        return this.roomService.one(id);
    }

    @PutMapping("/{id}")
    public Room replaceHabitacion(@PathVariable("id") Long id, @RequestBody Room room) {
        log.info("Actualizar room con id = " + id + "\n room" + room);
        return this.roomService.replace(id, room);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteHabitacion(@PathVariable ("id") Long id) {
        this.roomService.delete(id);
    }

}

