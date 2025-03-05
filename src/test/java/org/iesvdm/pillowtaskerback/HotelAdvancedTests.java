package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.domain.Empleado;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Usuario;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.repository.EmpleadoRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UsuarioRepository;
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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

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

        // Crear un usuario de ejemplo
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@ejemplo.com");
        usuario.setPassword("password123");

        // Guardar el usuario en la base de datos
        usuario = usuarioRepository.save(usuario);

        // Crear un hotel y asignar al usuario como propietario
        Hotel hotel = new Hotel();
        hotel.setNombre("Hotel de Juan");
        hotel.setCodigoPostal("12345");
        hotel.setDireccion("Calle de Ejemplo 123");
        hotel.setOwner(usuario);  // Asignamos el usuario como propietario del hotel

        // Guardar el hotel en la base de datos
        hotel = hotelRepository.save(hotel);

        // Verificar que el hotel se ha creado y que el propietario es el correcto
        assertNotNull(hotel.getId());
        assertEquals("Hotel de Juan", hotel.getNombre());
        assertEquals(usuario.getId(), hotel.getOwner().getId());
        assertEquals("Juan Pérez", hotel.getOwner().getNombre());
        assertEquals("juan.perez@ejemplo.com", hotel.getOwner().getEmail());
    }
    @Test
    @Transactional
    public void testCreateMultipleHotelsAndAssignUsers() {
        // Crear y guardar dos usuarios
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Ana Gómez");
        usuario2.setEmail("ana.gomez@ejemplo.com");
        usuario2.setPassword("password456");
        usuario2 = usuarioRepository.save(usuario2);

        Usuario usuario3 = new Usuario();
        usuario3.setNombre("Carlos López");
        usuario3.setEmail("carlos.lopez@ejemplo.com");
        usuario3.setPassword("password789");
        usuario3 = usuarioRepository.save(usuario3);

        // Crear y asignar hoteles a los usuarios
        Hotel hotel1 = new Hotel();
        hotel1.setNombre("Hotel de Ana");
        hotel1.setCodigoPostal("54321");
        hotel1.setDireccion("Calle Ana 123");
        hotel1.setOwner(usuario2);  // Asignamos a Ana como propietaria

        Hotel hotel2 = new Hotel();
        hotel2.setNombre("Hotel de Carlos");
        hotel2.setCodigoPostal("98765");
        hotel2.setDireccion("Calle Carlos 456");
        hotel2.setOwner(usuario3);  // Asignamos a Carlos como propietario

        // Guardamos los hoteles
        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);

        // Verificar que los hoteles se han creado correctamente
        assertNotNull(hotel1.getId());
        assertNotNull(hotel2.getId());

        // Verificar que los propietarios de los hoteles son los correctos
        assertEquals(usuario2.getId(), hotel1.getOwner().getId());
        assertEquals("Ana Gómez", hotel1.getOwner().getNombre());

        assertEquals(usuario3.getId(), hotel2.getOwner().getId());
        assertEquals("Carlos López", hotel2.getOwner().getNombre());
    }
    @Test
    @Transactional
    @Commit
    public void testCreateHotelAndAssignUserAndEmployees() {

        // Creamos y guardamos usuario dueño de hotel
        Usuario usuario = new Usuario();
        usuario.setNombre("Ana");
        usuario.setEmail("ana.gomez@ejemplo.com");
        usuario.setPassword("password321");
        usuario = usuarioRepository.save(usuario);

        //Creamos y guardamos usuario para empleado
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("xX_Carlitos2000_Xx");
        usuario2.setEmail("elCarlos@gmail.com");
        usuario2.setPassword("password123");
        usuario2 = usuarioRepository.save(usuario2);

        //Creamos y guardamos Hotel asociado a su owner (Usuario)
        Hotel hotel = new Hotel();
        hotel.setNombre("Hotel de Ana");
        hotel.setCodigoPostal("12345");
        hotel.setDireccion("Calle Ficticia");
        hotel.setOwner(usuario);
        hotelRepository.save(hotel);

        //Creamos y guardamos empleado asignando usuario
        Empleado empleado = new Empleado();
        empleado.setNombre("Carlos");
        empleado.setApellido1("Gomez");
        empleado.setApellido2("Grijalba");
        empleado.setContrasenia("ContraHotelCarlos123");
        empleado.setTipo(TipoEmpleadoEnum.RECEPCIONISTA);
        empleado.setUsuario(usuario2);
        empleado.setHotel(hotel);
        empleadoRepository.save(empleado);

        //Volvemos a guardar Hotel con el empleado
        hotel.getEmpleados().add(empleado);
        hotelRepository.save(hotel);

        // Verificar que el hotel se ha creado correctamente
        assertNotNull(hotel.getId());

        // Verificar que el propietario del hotel es correcto
        assertEquals(usuario.getId(), hotel.getOwner().getId());
        assertEquals("Ana", hotel.getOwner().getNombre());

        // Verificar que el hotel tiene al empleado Carlos
        assertTrue(hotel.getEmpleados().contains(empleado));
        assertTrue(hotel.getEmpleados().stream()
                .anyMatch(e -> "Carlos".equals(e.getNombre())));
    }

}




