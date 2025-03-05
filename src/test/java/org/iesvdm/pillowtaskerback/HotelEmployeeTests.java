package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.repository.EmployeeRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HotelEmployeeTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private User user;
    private Hotel hotel;

    @BeforeEach
    public void setUp() {
        // Limpiar datos antes de cada prueba
        employeeRepository.deleteAll();
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
    public void testCreateEmpleado() {
        // Crear un employee y asignarle un user y un hotel
        Employee employee = new Employee();
        employee.setName("Pedro");
        employee.setSurname1("Gomez");
        employee.setSurname2("Sanchez");
        employee.setPassword("password123");
        employee.setType(TipoEmpleadoEnum.RECEPTIONIST);  // Asignar un tipo de employee
        employee.setUser(user);  // Asignar el user a este employee
        employee.setHotel(hotel);  // Asignar el hotel a este employee

        // Guardar el employee
        employee = employeeRepository.save(employee);

        // Verificar que el employee se ha guardado correctamente
        assertNotNull(employee.getId());
        assertEquals("Pedro", employee.getName());
        assertEquals("Gomez", employee.getSurname1());
        assertEquals("Sanchez", employee.getSurname2());
        assertEquals(TipoEmpleadoEnum.RECEPTIONIST, employee.getType());
        assertEquals(user.getId(), employee.getUser().getId());
        assertEquals(hotel.getId(), employee.getHotel().getId());
    }

    @Test
    @Transactional
    public void testDeleteEmpleado() {
        // Crear un employee
        Employee employee = new Employee();
        employee.setName("Luis");
        employee.setSurname1("Martinez");
        employee.setSurname2("Lopez");
        employee.setPassword("password123");
        employee.setType(TipoEmpleadoEnum.RECEPTIONIST);
        employee.setUser(user);
        employee.setHotel(hotel);

        // Guardar el employee
        employee = employeeRepository.save(employee);

        // Verificar que el employee se ha guardado correctamente
        assertNotNull(employee.getId());

        // Eliminar el employee
        employeeRepository.deleteById(employee.getId());

        // Verificar que el employee ha sido eliminado
        assertFalse(employeeRepository.findById(employee.getId()).isPresent());
    }

    @Test
    @Transactional
    public void testUpdateEmpleado() {
        // Crear un employee
        Employee employee = new Employee();
        employee.setName("Carlos");
        employee.setSurname1("Rodriguez");
        employee.setSurname2("Diaz");
        employee.setPassword("password123");
        employee.setType(TipoEmpleadoEnum.ADMIN);
        employee.setUser(user);
        employee.setHotel(hotel);

        // Guardar el employee
        employee = employeeRepository.save(employee);

        // Verificar que el employee se ha guardado correctamente
        assertNotNull(employee.getId());
        assertEquals("Carlos", employee.getName());

        // Actualizar los datos del employee
        employee.setName("Carlos Alberto");
        employee.setSurname1("Rodriguez Garcia");

        // Guardar los cambios
        employee = employeeRepository.save(employee);

        // Verificar que los cambios se han guardado correctamente
        assertEquals("Carlos Alberto", employee.getName());
        assertEquals("Rodriguez Garcia", employee.getSurname1());
    }
}
