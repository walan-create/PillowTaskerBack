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
        hotel.setName("Hotel de Prueba");
        hotel.setPostalCode("12345");
        hotel.setAddress("Calle Ficticia 123");

        // Guardar el hotel
        Hotel savedHotel = hotelRepository.save(hotel);

        // Verificar que el hotel fue guardado
        assertNotNull(savedHotel.getId());
        assertEquals("Hotel de Prueba", savedHotel.getName());
    }

    @Test
    @Transactional
    public void testGetAllHotels() {
        // Crear y guardar dos hoteles
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel 1");
        hotel1.setPostalCode("12345");
        hotel1.setAddress("Calle A 1");

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel 2");
        hotel2.setPostalCode("67890");
        hotel2.setAddress("Calle B 2");

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
        hotel.setName("Hotel Original");
        hotel.setPostalCode("12345");
        hotel.setAddress("Calle Ficticia");

        hotelRepository.save(hotel);

        // Actualizar el name del hotel
        hotel.setName("Hotel Actualizado");
        Hotel updatedHotel = hotelRepository.save(hotel);

        // Verificar que el name ha sido actualizado
        assertEquals("Hotel Actualizado", updatedHotel.getName());
    }

    @Test
    @Transactional  // Aplicamos transacción solo a este test
    public void testDeleteHotel() {
        // Crear y guardar un hotel
        Hotel hotel = new Hotel();
        hotel.setName("Hotel para Eliminar");
        hotel.setPostalCode("12345");
        hotel.setAddress("Calle X");

        hotelRepository.save(hotel);

        // Borrar el hotel
        hotelRepository.deleteById(hotel.getId());

        // Verificar que el hotel ha sido eliminado
        assertFalse(hotelRepository.existsById(hotel.getId()));
    }
}
