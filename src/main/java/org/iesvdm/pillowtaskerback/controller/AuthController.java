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
        System.out.println("Intentando iniciar sesión con mail: " + request.getMail());
        return userService.findByEmail(request.getMail())
                .map(user -> {
                    if (userService.checkPassword(user, request.getPassword())) {
                        String token = jwtUtil.generateToken(user);
                        System.out.println("Login exitoso. Generando token...");
                        return ResponseEntity.ok(new AuthResponse(user, token));
                    } else {
                        System.out.println("Contraseña incorrecta");
                        return ResponseEntity.status(401).build(); // Unauthorized
                    }
                })
                .orElseGet(() -> {
                    System.out.println("Usuario no encontrado");
                    return ResponseEntity.status(401).build(); // Unauthorized
                });
    }


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        // Verificar si el usuario ya existe por correo
        if (userService.findByEmail(request.getMail()).isPresent()) {
            return ResponseEntity.status(409).build(); // Conflict, ya existe un usuario con ese correo
        }

        // Crear el nuevo usuario a partir del RegisterRequest
        User newUser = new User();
        newUser.setMail(request.getMail());
        newUser.setPassword(request.getPassword());  // Asegúrate de que la contraseña se encripte
        newUser.setName(request.getName());
        newUser.setSurname1(request.getSurname1());
        newUser.setSurname2(request.getSurname2());
        newUser.setDni(request.getDni());

        // Guardar el usuario en la base de datos
        User savedUser = userService.save(newUser);

        // Generar el token JWT
        String token = jwtUtil.generateToken(savedUser);

        // Crear el AuthResponse con el usuario y el token
        AuthResponse authResponse = new AuthResponse(savedUser, token);

        return ResponseEntity.ok(authResponse);
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
        String email = jwtUtil.extractUsername(token);

        // Busca el usuario por mail
        return userService.findByEmail(email)
                .map(user -> ResponseEntity.ok(new AuthResponse(user, token)))  // Si encuentra el usuario, lo devuelve
                .orElseGet(() -> ResponseEntity.status(404).build());  // Si no encuentra el usuario, responde con 404
    }

}

