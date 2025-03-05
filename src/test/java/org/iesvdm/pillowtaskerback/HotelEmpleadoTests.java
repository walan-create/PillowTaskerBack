package org.iesvdm.pillowtaskerback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.iesvdm.pillowtaskerback.domain.Empleado;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Usuario;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.repository.EmpleadoRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HotelEmpleadoTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Usuario usuario;
    private Hotel hotel;

    @BeforeEach
    public void setUp() {
        // Limpiar datos antes de cada prueba
        empleadoRepository.deleteAll();
        hotelRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Crear un usuario de ejemplo
        usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@ejemplo.com");
        usuario.setPassword("password123");

        // Crear un hotel de ejemplo
        hotel = new Hotel();
        hotel.setNombre("Hotel de Juan");
        hotel.setCodigoPostal("12345");
        hotel.setDireccion("Calle de Ejemplo 123");

        // Guardar usuario y hotel
        usuario = usuarioRepository.save(usuario);
        hotel.setOwner(usuario);  // Asignar el usuario como dueño del hotel
        hotel = hotelRepository.save(hotel);
    }

    @Test
    @Transactional
    public void testCreateEmpleado() {
        // Crear un empleado y asignarle un usuario y un hotel
        Empleado empleado = new Empleado();
        empleado.setNombre("Pedro");
        empleado.setApellido1("Gomez");
        empleado.setApellido2("Sanchez");
        empleado.setContrasenia("password123");
        empleado.setTipo(TipoEmpleadoEnum.RECEPCIONISTA);  // Asignar un tipo de empleado
        empleado.setUsuario(usuario);  // Asignar el usuario a este empleado
        empleado.setHotel(hotel);  // Asignar el hotel a este empleado

        // Guardar el empleado
        empleado = empleadoRepository.save(empleado);

        // Verificar que el empleado se ha guardado correctamente
        assertNotNull(empleado.getId());
        assertEquals("Pedro", empleado.getNombre());
        assertEquals("Gomez", empleado.getApellido1());
        assertEquals("Sanchez", empleado.getApellido2());
        assertEquals(TipoEmpleadoEnum.RECEPCIONISTA, empleado.getTipo());
        assertEquals(usuario.getId(), empleado.getUsuario().getId());
        assertEquals(hotel.getId(), empleado.getHotel().getId());
    }

    @Test
    @Transactional
    public void testDeleteEmpleado() {
        // Crear un empleado
        Empleado empleado = new Empleado();
        empleado.setNombre("Luis");
        empleado.setApellido1("Martinez");
        empleado.setApellido2("Lopez");
        empleado.setContrasenia("password123");
        empleado.setTipo(TipoEmpleadoEnum.RECEPCIONISTA);
        empleado.setUsuario(usuario);
        empleado.setHotel(hotel);

        // Guardar el empleado
        empleado = empleadoRepository.save(empleado);

        // Verificar que el empleado se ha guardado correctamente
        assertNotNull(empleado.getId());

        // Eliminar el empleado
        empleadoRepository.deleteById(empleado.getId());

        // Verificar que el empleado ha sido eliminado
        assertFalse(empleadoRepository.findById(empleado.getId()).isPresent());
    }

    @Test
    @Transactional
    public void testUpdateEmpleado() {
        // Crear un empleado
        Empleado empleado = new Empleado();
        empleado.setNombre("Carlos");
        empleado.setApellido1("Rodriguez");
        empleado.setApellido2("Diaz");
        empleado.setContrasenia("password123");
        empleado.setTipo(TipoEmpleadoEnum.ADMINISTRADOR);
        empleado.setUsuario(usuario);
        empleado.setHotel(hotel);

        // Guardar el empleado
        empleado = empleadoRepository.save(empleado);

        // Verificar que el empleado se ha guardado correctamente
        assertNotNull(empleado.getId());
        assertEquals("Carlos", empleado.getNombre());

        // Actualizar los datos del empleado
        empleado.setNombre("Carlos Alberto");
        empleado.setApellido1("Rodriguez Garcia");

        // Guardar los cambios
        empleado = empleadoRepository.save(empleado);

        // Verificar que los cambios se han guardado correctamente
        assertEquals("Carlos Alberto", empleado.getNombre());
        assertEquals("Rodriguez Garcia", empleado.getApellido1());
    }
}
