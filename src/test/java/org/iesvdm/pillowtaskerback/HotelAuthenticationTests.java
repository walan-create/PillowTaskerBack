/*package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.dto.HotelDTO;
import org.iesvdm.pillowtaskerback.security.CustomUserDetails;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
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
    private UserRepository usuarioRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelService hotelService;

    private User user;

    @BeforeEach
    public void setUp() {
        // Limpiar los datos antes de cada prueba
        hotelRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Crear un user de ejemplo
        user = new User();
        user.setName("Juan Pérez");
        user.setMail("juan.perez@ejemplo.com");
        user.setPassword("password123");

        // Guardar el user en la base de datos
        user = usuarioRepository.save(user);

        // Configurar el contexto de seguridad manualmente con el user guardado
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

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
        // Crear un hotel y asignar al user como propietario
        Hotel hotel = new Hotel();
        hotel.setName("Hotel de Juan");
        hotel.setPostalCode("12345");
        hotel.setAddress("Calle de Ejemplo 123");
        hotel.setUser(user);  // Asignamos el user como propietario del hotel

        // Guardar el hotel en la base de datos
        hotel = hotelRepository.save(hotel);

        // Obtener los hoteles del servicio
        List<HotelDTO> hotels = hotelService.getAllHotelsDTOWithEmployeeCount();

        assertNotNull(hotels);
        assertFalse(hotels.isEmpty());

        // Verifica si el campo 'owner' está correctamente asignado
        HotelDTO hotelDTO = hotels.get(0);
        assertTrue(hotelDTO.isOwner()); // Debería ser true, ya que el user autenticado es el dueño
    }
}
*/