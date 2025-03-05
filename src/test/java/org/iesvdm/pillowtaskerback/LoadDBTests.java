package org.iesvdm.pillowtaskerback;

import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.repository.EmployeeRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Carga solo la capa de persistencia y usa una BD en memoria (H2)
class LoadDBTests
{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    @Commit
    void testSaveAndRetrieveUsersAndEmployees() {
        // Crear Dueño del Hotel
        User owner = User.builder()
                .name("Carlos Owner")
                .mail("carlos.owner@example.com")
                .build();
        owner = userRepository.save(owner);

        // Crear Hotel con el dueño
        Hotel hotel = Hotel.builder()
                .name("Hotel Paradise")
                .address("Main Street 123, Madrid")
                .postalCode("29645")
                .owner(owner) // Asignamos el user dueño del hotel
                .build();
        hotel = hotelRepository.save(hotel);

        // Crear Employee 1
        User user1 = User.builder()
                .name("xX_JuanDestroyer_Xx")
                .mail("juan.perez@example.com")
                .build();
        user1 = userRepository.save(user1);

        Employee employee1 = Employee.builder()
                .name("Juan")
                .surname1("Pérez")
                .surname2("Gómez")
                .password("password123")
                .type(TipoEmpleadoEnum.RECEPTIONIST)
                .user(user1) //Asignamos el user al que corresponde este empleado
                .hotel(hotel)
                .build();
        employeeRepository.save(employee1);

        // Crear Employee 2
        User user2 = User.builder()
                .name("Anita")
                .mail("ana.lopez@example.com")
                .build();
        user2 = userRepository.save(user2);

        Employee employee2 = Employee.builder()
                .name("Ana")
                .surname1("López")
                .surname2("Martínez")
                .password("securePass456")
                .type(TipoEmpleadoEnum.CLEANER)
                .user(user2) //Asignamos el user al que corresponde este empleado
                .hotel(hotel)
                .build();
        employeeRepository.save(employee2);

        // Verificar que los employees se guardaron correctamente
        assertThat(employeeRepository.count()).isEqualTo(2);
        assertThat(userRepository.count()).isEqualTo(3); // Dueño + 2 employees
        assertThat(hotelRepository.count()).isEqualTo(1);
    }
}