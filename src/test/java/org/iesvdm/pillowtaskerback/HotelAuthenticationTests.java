/*package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.security.CustomUserDetails;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Usuario;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HotelAuthenticationTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelService hotelService;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        // Limpiar los datos antes de cada prueba
        hotelRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Crear un usuario de ejemplo
        usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan.perez@ejemplo.com");
        usuario.setPassword("password123");

        // Guardar el usuario en la base de datos
        usuario = usuarioRepository.save(usuario);

        // Configurar el contexto de seguridad manualmente con el usuario guardado
        CustomUserDetails customUserDetails = new CustomUserDetails(usuario);

        // Crear un token de autenticación con el CustomUserDetails
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // Establecer el token en el contexto de seguridad
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @Transactional
    public void testGetHotelsWithOwner() {
        // Crear un hotel y asignar al usuario como propietario
        Hotel hotel = new Hotel();
        hotel.setNombre("Hotel de Juan");
        hotel.setCodigoPostal("12345");
        hotel.setDireccion("Calle de Ejemplo 123");
        hotel.setUsuario(usuario);  // Asignamos el usuario como propietario del hotel

        // Guardar el hotel en la base de datos
        hotel = hotelRepository.save(hotel);

        // Obtener los hoteles del servicio
        List<HotelDTO> hotels = hotelService.getAllHotelsDTOWithEmployeeCount();

        assertNotNull(hotels);
        assertFalse(hotels.isEmpty());

        // Verifica si el campo 'propio' está correctamente asignado
        HotelDTO hotelDTO = hotels.get(0);
        assertTrue(hotelDTO.isPropio()); // Debería ser true, ya que el usuario autenticado es el dueño
    }
}
*/