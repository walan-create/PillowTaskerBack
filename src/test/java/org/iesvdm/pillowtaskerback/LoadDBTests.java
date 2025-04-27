package org.iesvdm.pillowtaskerback;

import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.HotelDTOAutoCreateCredential;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Credential;
import org.iesvdm.pillowtaskerback.enums.CredentialTypeEnum;
import org.iesvdm.pillowtaskerback.repository.CredentialRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.iesvdm.pillowtaskerback.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Carga solo la capa de persistencia y usa una BD en memoria (H2)
class LoadDBTests {

    @Autowired
    private CredentialRepository credentialRepository;

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
                .name("Alice")
                .mail("alice.johnson@example.com")
                .password("alicePass123")
                .surname1("Johnson")
                .surname2("Smith")
                .dni("12345678A")
                .build();
        user1 = userRepository.save(user1);

        User user2 = User.builder()
                .name("Bob")
                .mail("bob.smith@example.com")
                .password("bobPass123")
                .surname1("Smith")
                .surname2("Johnson")
                .dni("87654321B")
                .build();
        user2 = userRepository.save(user2);

        User user3 = User.builder()
                .name("Charlie")
                .mail("charlie.brown@example.com")
                .password("charliePass123")
                .surname1("Brown")
                .surname2("Williams")
                .dni("11223344C")
                .build();
        user3 = userRepository.save(user3);

        User user4 = User.builder()
                .name("Diana")
                .mail("diana.prince@example.com")
                .password("dianaPass123")
                .surname1("Prince")
                .surname2("Diana")
                .dni("22334455D")
                .build();
        user4 = userRepository.save(user4);

        User user5 = User.builder()
                .name("Eve")
                .mail("eve.adams@example.com")
                .password("evePass123")
                .surname1("Adams")
                .surname2("Eve")
                .dni("33445566E")
                .build();
        user5 = userRepository.save(user5);

        /*--------------------------------------------------*/
        /*-------------------CEATE HOTELS-------------------*/
        /*--------------------------------------------------*/

        // Crear un hotel con el primer usuario (Alice)
        HotelDTOAutoCreateCredential hotelDTO = HotelDTOAutoCreateCredential.builder()
                .name("Sunshine Hotel")
                .address("123 Sunshine St, Miami")
                .postalCode("33101")
                .password("hotelPass123")
                .build();
        // Crear un hotel con el primer usuario (Alice) y autogeneramos una credencial con rol ADMIN
        Hotel hotel = hotelService.createHotelForUser(user1.getId(), hotelDTO);

        /*--------------------------------------------------*/
        /*------------------CEATE CREDENTIALS---------------*/
        /*--------------------------------------------------*/

        Credential credential2 = Credential.builder()
                .password("cleanerPass456")
                .rol(CredentialTypeEnum.CLEANER)
                .user(user3) // Asignamos el user3 como credencial
                .hotel(hotel)
                .build();
        credential2 = credentialRepository.save(credential2);

        Credential credential3 = Credential.builder()
                .password("adminPass789")
                .rol(CredentialTypeEnum.ADMIN)
                .user(user4) // Asignamos el user4 como credencial
                .hotel(hotel)
                .build();
        credential3 = credentialRepository.save(credential3);

        Credential credential4 = Credential.builder()
                .password("receptionistPass321")
                .rol(CredentialTypeEnum.RECEPTIONIST)
                .user(user5) // Asignamos el user5 como credencial
                .hotel(hotel)
                .build();
        credential4 = credentialRepository.save(credential4);

        // Verificar que se han guardado correctamente
        assertThat(userRepository.count()).isEqualTo(5); // 5 usuarios
        assertThat(credentialRepository.count()).isEqualTo(4); // 4 credenciales (1 por usuario)
        assertThat(hotelRepository.count()).isEqualTo(1); // 1 hotel
    }
}