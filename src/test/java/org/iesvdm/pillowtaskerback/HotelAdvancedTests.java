package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HotelAdvancedTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    public void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Test
    @Transactional
    public void testCreateHotelAndAssignOwner() {

        // Crear un user de ejemplo
        User user = new User();
        user.setName("Juan Pérez");
        user.setMail("juan.perez@ejemplo.com");
        user.setPassword("password123");

        // Guardar el user en la base de datos
        user = userRepository.save(user);

        // Crear un hotel y asignar al user como propietario
        Hotel hotel = new Hotel();
        hotel.setName("Hotel de Juan");
        hotel.setPostalCode("12345");
        hotel.setAddress("Calle de Ejemplo 123");
        hotel.setOwner(user);  // Asignamos el user como propietario del hotel

        // Guardar el hotel en la base de datos
        hotel = hotelRepository.save(hotel);

        // Verificar que el hotel se ha creado y que el propietario es el correcto
        assertNotNull(hotel.getId());
        assertEquals("Hotel de Juan", hotel.getName());
        assertEquals(user.getId(), hotel.getOwner().getId());
        assertEquals("Juan Pérez", hotel.getOwner().getName());
        assertEquals("juan.perez@ejemplo.com", hotel.getOwner().getMail());
    }

    @Test
    @Transactional
    public void testCreateMultipleHotelsAndAssignUsers() {
        // Crear y guardar dos usuarios
        User user2 = new User();
        user2.setName("Ana Gómez");
        user2.setMail("ana.gomez@ejemplo.com");
        user2.setPassword("password456");
        user2 = userRepository.save(user2);

        User user3 = new User();
        user3.setName("Carlos López");
        user3.setMail("carlos.lopez@ejemplo.com");
        user3.setPassword("password789");
        user3 = userRepository.save(user3);

        // Crear y asignar hoteles a los usuarios
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel de Ana");
        hotel1.setPostalCode("54321");
        hotel1.setAddress("Calle Ana 123");
        hotel1.setOwner(user2);  // Asignamos a Ana como propietaria

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel de Carlos");
        hotel2.setPostalCode("98765");
        hotel2.setAddress("Calle Carlos 456");
        hotel2.setOwner(user3);  // Asignamos a Carlos como propietario

        // Guardamos los hoteles
        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);

        // Verificar que los hoteles se han creado correctamente
        assertNotNull(hotel1.getId());
        assertNotNull(hotel2.getId());

        // Verificar que los propietarios de los hoteles son los correctos
        assertEquals(user2.getId(), hotel1.getOwner().getId());
        assertEquals("Ana Gómez", hotel1.getOwner().getName());

        assertEquals(user3.getId(), hotel2.getOwner().getId());
        assertEquals("Carlos López", hotel2.getOwner().getName());
    }

    @Test
    @Transactional
    @Commit
    public void testCreateHotelAndAssignUserAndCredentials() {

        // Creamos y guardamos user dueño de hotel
        User user = new User();
        user.setName("Ana");
        user.setMail("ana.gomez@ejemplo.com");
        user.setPassword("password321");
        user = userRepository.save(user);

        //Creamos y guardamos user para credential
        User user2 = new User();
        user2.setName("xX_Carlitos2000_Xx");
        user2.setMail("elCarlos@gmail.com");
        user2.setPassword("password123");
        user2 = userRepository.save(user2);

        //Creamos y guardamos Hotel asociado a su owner (User)
        Hotel hotel = new Hotel();
        hotel.setName("Hotel de Ana");
        hotel.setPostalCode("12345");
        hotel.setAddress("Calle Ficticia");
        hotel.setOwner(user);
        hotelRepository.save(hotel);

        //Creamos y guardamos credential asignando user
        Credential credential = new Credential();
        credential.setPassword("ContraHotelCarlos123");
        credential.setRol(TipoEmpleadoEnum.RECEPTIONIST);
        credential.setUser(user2);
        credential.setHotel(hotel);
        credentialRepository.save(credential);

        //Volvemos a guardar Hotel con la credential
        hotel.getCredentials().add(credential);
        hotelRepository.save(hotel);

        // Verificar que el hotel se ha creado correctamente
        assertNotNull(hotel.getId());

        // Verificar que el propietario del hotel es correcto
        assertEquals(user.getId(), hotel.getOwner().getId());
        assertEquals("Ana", hotel.getOwner().getName());

        // Verificar que el hotel tiene la credential de Carlos
        assertTrue(hotel.getCredentials().contains(credential));
        assertTrue(hotel.getCredentials().stream()
                .anyMatch(c -> "xX_Carlitos2000_Xx".equals(c.getUser().getName())));
    }
}