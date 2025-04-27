package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.enums.CredentialTypeEnum;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HotelCredentialTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    private User user;
    private Hotel hotel;

    @BeforeEach
    public void setUp() {
        // Limpiar datos antes de cada prueba
        credentialRepository.deleteAll();
        hotelRepository.deleteAll();
        userRepository.deleteAll();

        // Crear un user de ejemplo
        user = new User();
        user.setName("Juan Pérez");
        user.setMail("juan.perez@ejemplo.com");
        user.setPassword("password123");

        // Crear un hotel de ejemplo
        hotel = new Hotel();
        hotel.setName("Hotel de Juan");
        hotel.setPostalCode("12345");
        hotel.setAddress("Calle de Ejemplo 123");

        // Guardar user y hotel
        user = userRepository.save(user);
        hotel.setOwner(user);  // Asignar el user como dueño del hotel
        hotel = hotelRepository.save(hotel);
    }

    @Test
    @Transactional
    public void testCreateCredential() {
        // Crear una credencial y asignarle un user y un hotel
        Credential credential = new Credential();
        credential.setPassword("password123");
        credential.setRol(CredentialTypeEnum.RECEPTIONIST);  // Asignar un tipo de credencial
        credential.setUser(user);  // Asignar el user a esta credencial
        credential.setHotel(hotel);  // Asignar el hotel a esta credencial

        // Guardar la credencial
        credential = credentialRepository.save(credential);

        // Verificar que la credencial se ha guardado correctamente
        assertNotNull(credential.getId());
        assertEquals(CredentialTypeEnum.RECEPTIONIST, credential.getRol());
        assertEquals(user.getId(), credential.getUser().getId());
        assertEquals(hotel.getId(), credential.getHotel().getId());
    }

    @Test
    @Transactional
    public void testDeleteCredential() {
        // Crear una credencial
        Credential credential = new Credential();
        credential.setPassword("password123");
        credential.setRol(CredentialTypeEnum.RECEPTIONIST);
        credential.setUser(user);
        credential.setHotel(hotel);

        // Guardar la credencial
        credential = credentialRepository.save(credential);

        // Verificar que la credencial se ha guardado correctamente
        assertNotNull(credential.getId());

        // Eliminar la credencial
        credentialRepository.deleteById(credential.getId());

        // Verificar que la credencial ha sido eliminada
        assertFalse(credentialRepository.findById(credential.getId()).isPresent());
    }

    @Test
    @Transactional
    public void testUpdateCredential() {
        // Crear una credencial
        Credential credential = new Credential();
        credential.setPassword("password123");
        credential.setRol(CredentialTypeEnum.ADMIN);
        credential.setUser(user);
        credential.setHotel(hotel);

        // Guardar la credencial
        credential = credentialRepository.save(credential);

        // Verificar que la credencial se ha guardado correctamente
        assertNotNull(credential.getId());

        // Actualizar los datos de la credencial
        credential.setPassword("newpassword123");
        credential.setRol(CredentialTypeEnum.ADMIN);

        // Guardar los cambios
        credential = credentialRepository.save(credential);

        // Verificar que los cambios se han guardado correctamente
        assertEquals("newpassword123", credential.getPassword());
        assertEquals(CredentialTypeEnum.ADMIN, credential.getRol());
    }
}