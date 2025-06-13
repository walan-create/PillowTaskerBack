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
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * Servicio para inicializar y poblar la base de datos con datos de prueba.
 */
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
    /**
     * Borra todos los datos de la base de datos.
     *
     * @return true si la operación fue exitosa, false si hubo algún error
     * @throws ApiException si ocurre un error al eliminar los datos
     */
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
            // Registrar el error para depuración
            throw new ApiException("Error al eliminar los datos de la base de datos.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Este es un metodo que al ser llamado limpia la Base de Datos y la llena con datos de prueba.
     * @return Boolean de creación exitosa.
     * @throws ApiException si ocurre un error durante la siembra de datos
     */
    @Transactional
    public boolean seedDatabase() {

        // BORRAMOS LOS DATOS DE LA BASE DE DATOS
        this.deleteAllDataBase();

        //-----------------------------------------------------------------
        //------------- Creación de usuarios ------------------------------
        //-----------------------------------------------------------------

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
        //------------- Creación del hotel principal ----------------------
        //-----------------------------------------------------------------

        // Hotel principal: Hotel Vega de Mijas (antes Bella Vista)
        Hotel hotelPrincipal = hotelService.createHotelForUser(user1.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Hotel Vega de Mijas")
                .postalCode("28001")
                .address("Calle Sita, 1")
                .password("hotelpass")
                .build());

        // Otros hoteles (puedes mantenerlos si los necesitas)
        Hotel hotel2user1 = hotelService.createHotelForUser(user1.getId(), HotelDTOAutoCreateCredential.builder()
                .name("Hostal Paredes")
                .postalCode("29370")
                .address("Calle María, 7")
                .password("hotelpass")
                .build());

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
        //---------- Invitaciones para Hotel Vega de Mijas ----------------
        //-----------------------------------------------------------------

        // Recepcionista
        Invitation invitationRecep = invitationService
                .sendInvitation(
                        hotelPrincipal.getId(),
                        Invitation.builder()
                                .mail("user2@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotelPrincipal)
                                .credentialType(CredentialTypeEnum.RECEPTIONIST)
                                .build()
                );

        // Limpieza
        Invitation invitationCleaner = invitationService
                .sendInvitation(
                        hotelPrincipal.getId(),
                        Invitation.builder()
                                .mail("user6@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotelPrincipal)
                                .credentialType(CredentialTypeEnum.CLEANER)
                                .build()
                );

        // Mantenimiento
        Invitation invitationMaintenance = invitationService
                .sendInvitation(
                        hotelPrincipal.getId(),
                        Invitation.builder()
                                .mail("user5@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotelPrincipal)
                                .credentialType(CredentialTypeEnum.MAINTENANCE)
                                .build()
                );

        // Invitaciones User 1
        Invitation user1Invitation = invitationService
                .sendInvitation(
                        hotel3.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotelPrincipal)
                                .credentialType(CredentialTypeEnum.MAINTENANCE)
                                .build()
                );
        Invitation user1Invitation2 = invitationService
                .sendInvitation(
                        hotel4.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotelPrincipal)
                                .credentialType(CredentialTypeEnum.MAINTENANCE)
                                .build()
                );
        Invitation user1Invitation3 = invitationService
                .sendInvitation(
                        hotel5.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotelPrincipal)
                                .credentialType(CredentialTypeEnum.MAINTENANCE)
                                .build()
                );
        Invitation user1Invitation4 = invitationService
                .sendInvitation(
                        hotel6.getId(),
                        Invitation.builder()
                                .mail("user1@example.com")
                                .shippingDate(LocalDateTime.now())
                                .hotel(hotelPrincipal)
                                .credentialType(CredentialTypeEnum.MAINTENANCE)
                                .build()
                );

        // Aceptamos las invitaciones principales
        invitationService.processInvitationResponse(invitationRecep.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(invitationCleaner.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(invitationMaintenance.getId(), true, "hotelpass");
        invitationService.processInvitationResponse(user1Invitation2.getId(), true, "hotelpass");

        //-----------------------------------------------------------------
        //------------------ Creación de Habitaciones ---------------------
        //-----------------------------------------------------------------

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("101")
                .capacity(2)
                .numberOfRooms(1)
                .kitchen(true)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.OCCUPIED)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("102")
                .capacity(4)
                .numberOfRooms(2)
                .kitchen(false)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("103")
                .capacity(3)
                .numberOfRooms(1)
                .kitchen(true)
                .type(RoomTypeEnum.SUITE)
                .state(RoomStateEnum.OCCUPIED)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("104")
                .capacity(1)
                .numberOfRooms(1)
                .kitchen(false)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.DIRTY)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("105")
                .capacity(2)
                .numberOfRooms(1)
                .kitchen(true)
                .type(RoomTypeEnum.ADAPTABLE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("106")
                .capacity(5)
                .numberOfRooms(3)
                .kitchen(true)
                .type(RoomTypeEnum.SUITE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("107")
                .capacity(2)
                .numberOfRooms(1)
                .kitchen(false)
                .type(RoomTypeEnum.ADAPTABLE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("108")
                .capacity(3)
                .numberOfRooms(2)
                .kitchen(true)
                .type(RoomTypeEnum.SUITE)
                .state(RoomStateEnum.AVAILABLE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        roomService.createRoomForHotel( hotelPrincipal.getId(), Room.builder()
                .code("109")
                .capacity(3)
                .numberOfRooms(2)
                .kitchen(true)
                .type(RoomTypeEnum.STANDAR)
                .state(RoomStateEnum.MAINTENANCE)
                .hotel(null)
                .reservations(new HashSet<>())
                .build());

        return true;
    }
}