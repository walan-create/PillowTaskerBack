package org.iesvdm.pillowtaskerback.controller;

import lombok.RequiredArgsConstructor;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Usuario;
import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.iesvdm.pillowtaskerback.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/v1/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    @Autowired
    private final UsuarioService usuarioService;
    private final HotelService hotelService;

    @GetMapping("/{usuarioId}")
    public ResponseEntity<Usuario> getUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(usuarioService.one(usuarioId));
    }


    @GetMapping("/{usuarioId}/hoteles")
    public ResponseEntity<List<Hotel>> getHotelesPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(hotelService.findByOwner(usuarioId));
    }

    @GetMapping("/{usuarioId}/hoteles")
    public ResponseEntity<List<HotelDTO>> allHotels() {
        List<HotelDTO> hotels = hotelService.getAllHotelsDTOWithEmployeeCount();
        return ResponseEntity.ok(hotels);
    }
}
