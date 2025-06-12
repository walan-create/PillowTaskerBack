package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.iesvdm.pillowtaskerback.security.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de usuarios.
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Devuelve la lista completa de usuarios.
     * @return lista de todos los usuarios
     */
    public List<User> all() {
        return this.userRepository.findAll();
    }

    /**
     * Guarda un nuevo usuario en la base de datos y codifica su contraseña.
     * @param user usuario a guardar
     * @return usuario guardado
     */
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        entityManager.refresh(user);
        return user;
    }

    /**
     * Busca y devuelve un usuario por su id.
     * @param id identificador del usuario
     * @return usuario encontrado
     * @throws ApiException si no se encuentra el usuario
     */
    public User one(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException("Usuario con id " + id + " no encontrado", HttpStatus.NOT_FOUND));
    }
    /**
     * Reemplaza los datos de un usuario existente por los nuevos datos proporcionados,
     * asegurando que el correo y el DNI no estén en uso por otro usuario.
     * @param id identificador del usuario a modificar
     * @param userDetails datos nuevos del usuario
     * @return usuario actualizado
     * @throws ApiException si no se encuentra el usuario o si el correo/DNI ya están en uso por otro usuario
     */
    public User replace(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("Usuario con id " + id + " no encontrado", HttpStatus.NOT_FOUND));

        // Verifica que el correo no esté en uso por otro usuario
        userRepository.findByMail(userDetails.getMail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new ApiException("El correo ya está en uso por otro usuario", HttpStatus.CONFLICT);
            }
        });

        // Verifica que el DNI no esté en uso por otro usuario
        userRepository.findByDni(userDetails.getDni()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new ApiException("El DNI ya está en uso por otro usuario", HttpStatus.CONFLICT);
            }
        });

        user.setMail(userDetails.getMail());
        user.setPassword(userDetails.getPassword());
        user.setName(userDetails.getName());
        user.setSurname1(userDetails.getSurname1());
        user.setSurname2(userDetails.getSurname2());
        user.setDni(userDetails.getDni());

        return userRepository.save(user);
    }

    /**
     * Elimina un usuario por su id, solo si no es propietario de ningún hotel.
     * @param id identificador del usuario a eliminar
     * @throws ApiException si no se encuentra el usuario o si es propietario de algún hotel
     */
    public void delete(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new ApiException("Usuario con id " + id + " no encontrado", HttpStatus.NOT_FOUND));

        // Comprobar si el usuario es owner de algún hotel
        if (user.getOwnHotels() != null && !user.getOwnHotels().isEmpty()) {
            throw new ApiException("No se puede eliminar el usuario porque es propietario de uno o más hoteles.", HttpStatus.CONFLICT);
        }

        this.userRepository.delete(user);
    }

    /**
     * Busca un usuario por su email.
     * @param email correo electrónico del usuario
     * @return usuario encontrado (opcional)
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByMail(email);
    }

    /**
     * Realiza el login de un usuario comprobando su contraseña.
     * @param email correo electrónico
     * @param rawPassword contraseña sin codificar
     * @return usuario autenticado
     * @throws ApiException si el usuario no existe o la contraseña es incorrecta
     */
    public User login(String email, String rawPassword) {
        User user = userRepository.findByMail(email)
                .orElseThrow(() -> new ApiException("Usuario no encontrado", HttpStatus.NOT_FOUND));
        if (!checkPassword(user, rawPassword)) {
            throw new ApiException("Contraseña incorrecta", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }

    /**
     * Registra un nuevo usuario comprobando que el email y el DNI no estén en uso.
     * @param req datos de registro
     * @return usuario registrado
     * @throws ApiException si el correo o el DNI ya están en uso
     */
    public User register(RegisterRequest req) {
        if (userRepository.findByMail(req.getMail()).isPresent()) {
            throw new ApiException("El correo ya está en uso", HttpStatus.CONFLICT);
        }
        if (userRepository.findByDni(req.getDni()).isPresent()) {
            throw new ApiException("El DNI ya está en uso", HttpStatus.CONFLICT);
        }
        User user = User.builder()
                .mail(req.getMail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .surname1(req.getSurname1())
                .surname2(req.getSurname2())
                .dni(req.getDni())
                .build();

        return userRepository.save(user);
    }

    /**
     * Comprueba si la contraseña proporcionada coincide con la almacenada.
     * @param user usuario
     * @param rawPassword contraseña sin codificar
     * @return true si la contraseña es correcta, false en caso contrario
     */
    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}