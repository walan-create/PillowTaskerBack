package org.iesvdm.pillowtaskerback.service;

import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Invitation;
import org.iesvdm.pillowtaskerback.domain.Room;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.dto.HotelDTOAutoCreateCredential;
import org.iesvdm.pillowtaskerback.enums.CredentialTypeEnum;
import org.iesvdm.pillowtaskerback.enums.RoomStateEnum;
import org.iesvdm.pillowtaskerback.enums.RoomTypeEnum;
import org.iesvdm.pillowtaskerback.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

@Service
public class SeedService {

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private CredentialRepository credentialRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    // Borra toda la DATA
    public boolean deleteAllDataBase() {
        try {
            hotelRepository.deleteAll();
            invitationRepository.deleteAll();
            reservationRepository.deleteAll();
            roomRepository.deleteAll();
            incidentRepository.deleteAll();
            credentialRepository.deleteAll();
            clientRepository.deleteAll();
            userRepository.deleteAll();
            return true; // Todo salió bien
        } catch (Exception e) {
            e.printStackTrace(); // Registrar el error para depuración
            return false; // Algo falló
        }
    }

    /**
     * Este es un metodo que al ser llamado limpia la Base de Datos y la llena con datos de prueba.
     * @return Boolean de creación exitosa.
     */
    @Transactional
    public boolean seedDatabase() {

        // BORRAMOS LOS DATOS DE LA BASE DE DATOS
        this.deleteAllDataBase();

        //-----------------------------------------------------------------
        //------------- Creación de hoteles para cada usuario -------------
        //-----------------------------------------------------------------

        // Creación de usuarios y asignación de IDs
        User user1 = userService.save(User.builder()
                .mail("user1@example.com")
                .password("password")
                .name("Armando")
                .surname1("Paredes")
                .surname2("Duras")
                .dni("23456789M")
                .build());

        User user2 = userService.save(User.builder()
                .mail("user2@example.com")
                .password("password")
                .name("Elena")
                .surname1("Nito")
                .surname2("Delgado")
                .dni("12345678Z")
                .build());

        User user3 = userService.save(User.builder()
                .mail("user3@example.com")
                .password("password")
                .name("Alex")
                .surname1("Plosivo")
                .surname2("")
                .dni("87654321X")
                .build());

        User user4 = userService.save(User.builder()
                .mail("user4@example.com")
                .password("password")
                .name("Ana")
                .surname1("Tomia")
                .surname2("")
                .dni("45678912L")
                .build());

        User user5 = userService.save(User.builder()
                .mail("user5@example.com")
                .password("password")
                .name("Carlos")
                .surname1("Tercero")
                .surname2("Gomez")
                .dni("56789012P")
                .build());

        User user6 = userService.save(User.builder()
                .mail("user6@example.com")
                .password("password")
                .name("Lucia")
                .surname1("Martinez")
                .surname2("Lopez")
                .dni("67890123Q")
                .build());

        //-----------------------------------------------------------------
        //------------- Creación de hoteles para cada usuario -------------
        //-----------------------------------------------------------------

        // Hoteles para user 1
        Hotel hotel1user1 = hotelService.createHotelForUser(user1.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Hotel Bella Vista")
                .postalCode("28001")
                .address("Calle Sita, 1")
                .password("hotelpass")
                .build());

        Hotel hotel2user1 = hotelService.createHotelForUser(user1.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Hostal Paredes")
                .postalCode("29370")
                .address("Calle María, 7")
                .password("hotelpass")
                .build());

        // Hoteles para user 2
        Hotel hotel3 = hotelService.createHotelForUser(user2.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Duraz Village")
                .postalCode("29370")
                .address("Calle Ficticias, 3")
                .password("hotelpass")
                .build());

        Hotel hotel4 = hotelService.createHotelForUser(user2.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Hotel Elena")
                .postalCode("28002")
                .address("Calle Amapola, 7")
                .password("hotelpass")
                .build());

        Hotel hotel5 = hotelService.createHotelForUser(user2.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Hotel Alex")
                .postalCode("28003")
                .address("Calle Avianca, 3")
                .password("hotelpass")
                .build());

        Hotel hotel6 = hotelService.createHotelForUser(user2.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Hotel Ana")
                .postalCode("28004")
                .address("Calle Romero, 4")
                .password("hotelpass4")
                .build());

        //-----------------------------------------------------------------
        //---------- Creación de invitaciones para cada usuario -----------
        //-----------------------------------------------------------------

        // Creación de invitaciones para hotel1
        Invitation invitation1 = invitationService
                .sendInvitation(
                        hotel1user1.getId(),
                        Invitation.builder()
                                    .mail("user2@example.com")
                                    .shippingDate(LocalDateTime.now())
                                    .hotel(hotel1user1)
                                    .credentialType(CredentialTypeEnum.RECEPTIONIST)
                                    .build()
                );

        Invitation invitation2 = invitationService
                .sendInvitation(
                        hotel1user1.getId(),
                        Invitation.builder()
                                .mail("user3@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel1user1)
                                .credentialType(CredentialTypeEnum.ADMIN)
                                .build()
                );


        Invitation invitation3 = invitationService
                .sendInvitation(
                        hotel1user1.getId(),
                        Invitation.builder()
                                .mail("user4@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel1user1)
                                .credentialType(CredentialTypeEnum.RECEPTIONIST)
                                .build()
                );

        Invitation invitation4 = invitationService
                .sendInvitation(
                        hotel1user1.getId(),
                        Invitation.builder()
                                .mail("user5@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel1user1)
                                .credentialType(CredentialTypeEnum.ADMIN)
                                .build()
                );

        Invitation invitation5 = invitationService
                .sendInvitation(
                        hotel1user1.getId(),
                        Invitation.builder()
                                .mail("user6@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel1user1)
                                .credentialType(CredentialTypeEnum.RECEPTIONIST)
                                .build()
                );

        // Invitaciones para user1

        Invitation invitation6 = invitationService
                .sendInvitation(
                        hotel3.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel3)
                                .credentialType(CredentialTypeEnum.RECEPTIONIST)
                                .build()
                );

        Invitation invitation7 = invitationService
                .sendInvitation(
                        hotel4.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel4)
                                .credentialType(CredentialTypeEnum.RECEPTIONIST)
                                .build()
                );

        Invitation invitation8 = invitationService
                .sendInvitation(
                        hotel5.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel5)
                                .credentialType(CredentialTypeEnum.CLEANER)
                                .build()
                );
        Invitation invitation9 = invitationService
                .sendInvitation(
                        hotel6.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotel6)
                                .credentialType(CredentialTypeEnum.RECEPTIONIST)
                                .build()
                );



        // Aceptamos las invitaciones
        invitationService.processInvitationResponse(invitation1.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(invitation2.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(invitation3.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(invitation4.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(invitation5.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(invitation6.getId(), true, "hotelpass");


        //-----------------------------------------------------------------
        //------------------ Creación de Habitaciones ---------------------
        //-----------------------------------------------------------------

        Room room1 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("101")
                .capacity(2)
                .roomsNumber(1)
                .kitchen(true)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.OCCUPIED)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room2 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("102")
                .capacity(4)
                .roomsNumber(2)
                .kitchen(false)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room3 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("103")
                .capacity(3)
                .roomsNumber(1)
                .kitchen(true)
                .type(RoomTypeEnum.SUITE)
                .state(RoomStateEnum.OCCUPIED)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room4 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("104")
                .capacity(1)
                .roomsNumber(1)
                .kitchen(false)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.DIRTY)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room5 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("105")
                .capacity(2)
                .roomsNumber(1)
                .kitchen(true)
                .type(RoomTypeEnum.ADAPTABLE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room6 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("106")
                .capacity(5)
                .roomsNumber(3)
                .kitchen(true)
                .type(RoomTypeEnum.SUITE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room7 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("107")
                .capacity(2)
                .roomsNumber(1)
                .kitchen(false)
                .type(RoomTypeEnum.ADAPTABLE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room8 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("108")
                .capacity(3)
                .roomsNumber(2)
                .kitchen(true)
                .type(RoomTypeEnum.SUITE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        Room room9 = roomService.createRoomForHotel( hotel1user1.getId(), Room.builder()
                .NumberRoom("109")
                .capacity(3)
                .roomsNumber(2)
                .kitchen(true)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.MAINTENANCE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());


        return true;
    }
}
