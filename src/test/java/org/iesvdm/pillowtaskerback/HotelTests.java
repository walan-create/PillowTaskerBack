package org.iesvdm.pillowtaskerback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HotelTests {

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    public void setUp() {
        // Limpiar los datos antes de cada prueba
        hotelRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateHotel() {
        // Crear un hotel de ejemplo
        Hotel hotel = new Hotel();
        hotel.setNombre("Hotel de Prueba");
        hotel.setCodigoPostal("12345");
        hotel.setDireccion("Calle Ficticia 123");

        // Guardar el hotel
        Hotel savedHotel = hotelRepository.save(hotel);

        // Verificar que el hotel fue guardado
        assertNotNull(savedHotel.getId());
        assertEquals("Hotel de Prueba", savedHotel.getNombre());
    }

    @Test
    @Transactional
    public void testGetAllHotels() {
        // Crear y guardar dos hoteles
        Hotel hotel1 = new Hotel();
        hotel1.setNombre("Hotel 1");
        hotel1.setCodigoPostal("12345");
        hotel1.setDireccion("Calle A 1");

        Hotel hotel2 = new Hotel();
        hotel2.setNombre("Hotel 2");
        hotel2.setCodigoPostal("67890");
        hotel2.setDireccion("Calle B 2");

        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);

        // Obtener todos los hoteles
        List<Hotel> hotels = hotelRepository.findAll();

        // Verificar que ambos hoteles estén presentes
        assertEquals(2, hotels.size());
    }

    @Test
    @Transactional
    public void testUpdateHotel() {
        // Crear y guardar un hotel
        Hotel hotel = new Hotel();
        hotel.setNombre("Hotel Original");
        hotel.setCodigoPostal("12345");
        hotel.setDireccion("Calle Ficticia");

        hotelRepository.save(hotel);

        // Actualizar el nombre del hotel
        hotel.setNombre("Hotel Actualizado");
        Hotel updatedHotel = hotelRepository.save(hotel);

        // Verificar que el nombre ha sido actualizado
        assertEquals("Hotel Actualizado", updatedHotel.getNombre());
    }

    @Test
    @Transactional  // Aplicamos transacción solo a este test
    public void testDeleteHotel() {
        // Crear y guardar un hotel
        Hotel hotel = new Hotel();
        hotel.setNombre("Hotel para Eliminar");
        hotel.setCodigoPostal("12345");
        hotel.setDireccion("Calle X");

        hotelRepository.save(hotel);

        // Borrar el hotel
        hotelRepository.deleteById(hotel.getId());

        // Verificar que el hotel ha sido eliminado
        assertFalse(hotelRepository.existsById(hotel.getId()));
    }
}
