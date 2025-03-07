package org.iesvdm.pillowtaskerback;

import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Employee;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.HotelDTOAutoCreateEmployee;
import org.iesvdm.pillowtaskerback.enums.TipoEmpleadoEnum;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.EmployeeRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.iesvdm.pillowtaskerback.service.HotelService;
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

    @Autowired
    private HotelService hotelService;

    @Test
    @Transactional
    @Commit
    void testCreateUsers() {

        /*--------------------------------------------------*/
        /*--------------------CEATE USERS-------------------*/
        /*--------------------------------------------------*/

        User user1 = User.builder()
                .name("Marta La Jefa")
                .mail("martitaLaJefa@example.com")
                .build();
        user1 = userRepository.save(user1);

        User user2 = User.builder()
                .name("xX_CarlitosDestroyer_Xx")
                .mail("carlos2004@example.com")
                .build();
        user2 = userRepository.save(user2);

        User user3 = User.builder()
                .name("Ana Lover UwU")
                .mail("ana7@example.com")
                .build();
        user3 = userRepository.save(user3);

        User user4 = User.builder()
                .name("Luis a secas")
                .mail("pedrogomez123@example.com")
                .build();
        user4 = userRepository.save(user4);

        User user5 = User.builder()
                .name("Pablo Random")
                .mail("martinex@example.com")
                .build();
        user5 = userRepository.save(user5);

        /*--------------------------------------------------*/
        /*-------------------CEATE HOTELS-------------------*/
        /*--------------------------------------------------*/

        // Crear un hotel con el primer usuario (Marta)
        HotelDTOAutoCreateEmployee hotelDTO = HotelDTOAutoCreateEmployee.builder()
                .name("Hotel Paradise")
                .address("Calle loh Cordoneh, Cadih")
                .postalCode("29645")
                .employeeName("Marta")
                .surname1("Ramírez")
                .surname2("Castro")
                .dni("12345678A")
                .build();
        // Crear un hotel con el primer usuario (Marta) y autogeneramos un empleado con rol ADMIN
        Hotel hotel = hotelService.createHotelForUser(user1.getId(),hotelDTO);

        /*--------------------------------------------------*/
        /*------------------CEATE EMPLOYEES-----------------*/
        /*--------------------------------------------------*/

//        Employee employee2 = Employee.builder()
//                .name("Ana")
//                .surname1("López")
//                .surname2("Martínez")
//                .password("securePass456")
//                .type(TipoEmpleadoEnum.CLEANER)
//                .user(user3) // Asignamos el user3 como empleado
//                .hotel(hotel)
//                .build();
//        employee2 = employeeRepository.save(employee2);
//
//        Employee employee3 = Employee.builder()
//                .name("Pedro")
//                .surname1("Gómez")
//                .surname2("Pérez")
//                .password("pedroPass789")
//                .type(TipoEmpleadoEnum.ADMIN)
//                .user(user4) // Asignamos el user4 como empleado
//                .hotel(hotel)
//                .build();
//        employee3 = employeeRepository.save(employee3);
//
//        Employee employee4 = Employee.builder()
//                .name("Luis")
//                .surname1("Martínez")
//                .surname2("Rodríguez")
//                .password("luisPass321")
//                .type(TipoEmpleadoEnum.RECEPTIONIST)
//                .user(user5) // Asignamos el user5 como empleado
//                .hotel(hotel)
//                .build();
//        employee4 = employeeRepository.save(employee4);
//
//        // Verificar que se han guardado correctamente
//        assertThat(userRepository.count()).isEqualTo(6); // 6 usuarios
//        assertThat(employeeRepository.count()).isEqualTo(6); // 6 empleados (1 por usuario)
//        assertThat(hotelRepository.count()).isEqualTo(1); // 1 hotel
    }

}