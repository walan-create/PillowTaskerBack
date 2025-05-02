package org.iesvdm.pillowtaskerback.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.security.AuthResponse;
import org.iesvdm.pillowtaskerback.security.JwtUtil;
import org.iesvdm.pillowtaskerback.security.LoginRequest;
import org.iesvdm.pillowtaskerback.security.RegisterRequest;
import org.iesvdm.pillowtaskerback.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request.getMail(), request.getPassword());
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok(new AuthResponse(user, token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage()); // Unauthorized
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest request) {
        try {
            User savedUser = userService.register(request);
            String token = jwtUtil.generateToken(savedUser);
            return ResponseEntity.ok(new AuthResponse(savedUser, token));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage()); // Conflicto (duplicado)
        }
    }

    @GetMapping("/check-status")
    public ResponseEntity<?> checkStatus(@RequestHeader(value = "Authorization", required = false) String header) {

        if (header == null || !header.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body("Cabecera 'Authorization' no encontrada o mal formada");
        }

        String token = header.replace("Bearer ", "");

        // Verifica si el token es válido
        if (!jwtUtil.isTokenValid(token)) {
            return ResponseEntity.status(401).body("Token inválido");
        }

        // Extrae el mail del token
        String email = jwtUtil.extractMail(token);

        // Busca el usuario por mail
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(new AuthResponse(user, token)))  // Si encuentra el usuario, lo devuelve
                .orElseGet(() -> ResponseEntity.status(404).build());  // Si no encuentra el usuario, responde con 404
    }

}

