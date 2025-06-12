package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.*;
import org.iesvdm.pillowtaskerback.dto.ReservationDTO;
import org.iesvdm.pillowtaskerback.enums.ReservationStateEnum;
import org.iesvdm.pillowtaskerback.enums.RoomStateEnum;
import org.iesvdm.pillowtaskerback.exception.ApiException;
import java.time.format.DateTimeFormatter;
import org.iesvdm.pillowtaskerback.repository.ClientRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.ReservationRepository;
import org.iesvdm.pillowtaskerback.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de reservas.
 * Proporciona operaciones CRUD y utilidades relacionadas con reservas de habitaciones y clientes.
 */
@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ClientRepository clientRepository;

    @PersistenceContext
    EntityManager entityManager;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /**
     * Devuelve la lista completa de reservas.
     *
     * @return lista de todas las reservas
     */
    public List<Reservation> all() {
        return this.reservationRepository.findAll();
    }

    /**
     * Guarda una nueva reserva en la base de datos y actualiza su estado.
     *
     * @param reservation reserva a guardar
     * @return reserva guardada
     */
    @Transactional
    public Reservation save(Reservation reservation) {
        reservationRepository.save(reservation);
        entityManager.refresh(reservation);
        return reservation;
    }

    /**
     * Busca y devuelve una reserva por su id.
     *
     * @param id identificador de la reserva
     * @return reserva encontrada
     * @throws ApiException si no se encuentra la reserva
     */
    public Reservation one(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Reserva con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
    }

    /**
     * Reemplaza los datos de una reserva existente por los nuevos datos proporcionados.
     *
     * @param id identificador de la reserva a modificar
     * @param dto datos nuevos de la reserva
     * @return reserva actualizada
     * @throws ApiException si no se encuentra la reserva, hay solapamiento de fechas o habitaciones no válidas
     */
    @Transactional
    public Reservation replace(Long id, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ApiException("Reserva con id " + id + " no encontrada", HttpStatus.NOT_FOUND));

        LocalDateTime entryDateTime = dto.getEntryDate();
        LocalDateTime departureDateTime = dto.getDepartureDay();

        if (!entryDateTime.isBefore(departureDateTime)) {
            throw new ApiException("La fecha de entrada debe ser anterior a la fecha de salida.", HttpStatus.BAD_REQUEST);
        }

        Set<Room> newRooms = dto.getRoomIds().stream()
                .map(roomId -> roomRepository.findById(roomId)
                        .orElseThrow(() -> new ApiException("Habitación con id " + roomId + " no encontrada", HttpStatus.NOT_FOUND)))
                .collect(Collectors.toSet());

        Set<Room> oldRooms = reservation.getRooms();
        Set<Room> addedRooms = newRooms.stream()
                .filter(room -> !oldRooms.contains(room))
                .collect(Collectors.toSet());

        Long hotelId = newRooms.iterator().next().getHotel().getId();
        for (Room room : newRooms) {
            if (!room.getHotel().getId().equals(hotelId)) {
                throw new ApiException("Todas las habitaciones deben pertenecer al mismo hotel.", HttpStatus.BAD_REQUEST);
            }
        }

        for (Room room : addedRooms) {
            if (room.getState() != RoomStateEnum.AVAILABLE) {
                throw new ApiException(
                        "La habitación " + room.getCode() + " no está disponible.",
                        HttpStatus.CONFLICT
                );
            }
            for (Reservation existing : room.getReservations()) {
                boolean overlap = !(departureDateTime.isBefore(existing.getEntryDate()) ||
                        entryDateTime.isAfter(existing.getDepartureDay()));
                if (overlap) {
                    throw new ApiException(
                            "La habitación " + room.getCode() +
                                    " ya está reservada entre " + existing.getEntryDate().format(formatter) + " y " + existing.getDepartureDay().format(formatter),
                            HttpStatus.CONFLICT
                    );
                }
            }
        }

        reservation.setReservationsName(dto.getReservationsName());
        reservation.setEntryDate(entryDateTime);
        reservation.setDepartureDay(departureDateTime);
        reservation.setState(dto.getState());
        reservation.setEarlyDeparture(dto.isEarlyDeparture());
        reservation.setRooms(newRooms);
        reservation.setOccupants(Collections.emptySet());

        return reservationRepository.save(reservation);
    }

    /**
     * Elimina una reserva por su id.
     *
     * @param reservationId identificador de la reserva a eliminar
     * @throws ApiException si no se encuentra la reserva o está en estado CHECKED_IN
     */
    public void delete(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException("Reserva no encontrada", HttpStatus.NOT_FOUND));

        if (reservation.getState() == ReservationStateEnum.CHECKED_IN) {
            throw new ApiException("No se puede eliminar una reserva en estado CHECKED_IN.", HttpStatus.CONFLICT);
        }

        reservation.getOccupants().forEach(client -> client.getReservations().remove(reservation));
        reservation.getRooms().forEach(room -> room.getReservations().remove(reservation));

        reservationRepository.delete(reservation);
    }

    /**
     * Obtiene la lista de reservas asociadas a un hotel específico.
     *
     * @param hotelId identificador del hotel
     * @return lista de reservas del hotel
     * @throws ApiException si no se encuentra el hotel
     */
    public List<Reservation> getReservationsByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));

        return this.reservationRepository.findDistinctByRooms_Hotel_Id(hotelId);
    }

    /**
     * Crea una nueva reserva y la asocia a un hotel y habitaciones.
     *
     * @param hotelId identificador del hotel
     * @param dto datos de la reserva
     * @return reserva creada
     * @throws ApiException si hay fechas inválidas, habitaciones no válidas o solapamiento
     */
    public Reservation createReservation(Long hotelId, ReservationDTO dto) {
        if (dto.getEntryDate().isAfter(dto.getDepartureDay()) || dto.getEntryDate().isEqual(dto.getDepartureDay())) {
            throw new ApiException("La fecha de entrada debe ser anterior a la fecha de salida.", HttpStatus.BAD_REQUEST);
        }

        Reservation reservation = new Reservation();
        reservation.setReservationsName(dto.getReservationsName());
        reservation.setEntryDate(dto.getEntryDate());
        reservation.setDepartureDay(dto.getDepartureDay());
        reservation.setEarlyDeparture(false);
        reservation.setState(ReservationStateEnum.PENDING);

        Set<Room> rooms = dto.getRoomIds().stream()
                .map(id -> roomRepository.findById(id)
                        .orElseThrow(() -> new ApiException("Habitación con id " + id + " no encontrada", HttpStatus.NOT_FOUND)))
                .filter(room -> room.getHotel().getId().equals(hotelId))
                .collect(Collectors.toSet());

        if (rooms.isEmpty()) {
            throw new ApiException("No se han encontrado habitaciones válidas para el hotel.", HttpStatus.BAD_REQUEST);
        }

        for (Room room : rooms) {
            for (Reservation existing : room.getReservations()) {
                if (existing.getState() != ReservationStateEnum.PENDING &&
                        existing.getState() != ReservationStateEnum.CHECKED_IN) {
                    continue;
                }
                boolean overlap = !(dto.getDepartureDay().isBefore(existing.getEntryDate()) ||
                        dto.getEntryDate().isAfter(existing.getDepartureDay()));
                if (overlap) {
                    throw new ApiException(
                            "La habitación " + room.getCode() +
                                    " ya está reservada entre " + existing.getEntryDate().format(formatter) + " y " + existing.getDepartureDay().format(formatter),
                            HttpStatus.CONFLICT
                    );
                }
            }
        }

        reservation.setRooms(rooms);
        for (Room room : rooms) {
            room.getReservations().add(reservation);
        }
        reservation.setOccupants(Collections.emptySet());

        return reservationRepository.save(reservation);
    }

    /**
     * Realiza el check-in de una reserva, actualizando habitaciones y ocupantes.
     *
     * @param hotelId identificador del hotel
     * @param reservationId identificador de la reserva
     * @param reservationDTO datos de la reserva para check-in
     * @return reserva actualizada tras el check-in
     * @throws ApiException si no se encuentra la reserva, habitaciones o clientes, o hay inconsistencias
     */
    @Transactional
    public Reservation checkInReservation(Long hotelId, Long reservationId, ReservationDTO reservationDTO) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException("Reserva con id " + reservationId + " no encontrada", HttpStatus.NOT_FOUND));

        if (reservation.getState() != ReservationStateEnum.PENDING) {
            throw new ApiException("Solo se puede hacer check-in de reservas en estado PENDING.", HttpStatus.CONFLICT);
        }

        for (Room oldRoom : reservation.getRooms()) {
            oldRoom.getReservations().remove(reservation);
        }

        reservation.setEntryDate(reservationDTO.getEntryDate());
        reservation.setDepartureDay(reservationDTO.getDepartureDay());

        Set<Room> rooms = reservationDTO.getRoomIds().stream()
                .map(roomId -> {
                    Room room = roomRepository.findById(roomId)
                            .orElseThrow(() -> new ApiException("Habitación con id " + roomId + " no encontrada", HttpStatus.NOT_FOUND));
                    return room;
                })
                .collect(Collectors.toSet());

        for (Room room : rooms) {
            if (!room.getHotel().getId().equals(hotelId)) {
                throw new ApiException("Todas las habitaciones deben pertenecer al hotel.", HttpStatus.BAD_REQUEST);
            }
            if (room.getState() != RoomStateEnum.AVAILABLE) {
                throw new ApiException("La habitación " + room.getCode() + " no está disponible para check-in.", HttpStatus.CONFLICT);
            }
        }
        reservation.setRooms(rooms);
        for (Room room : rooms) {
            room.getReservations().add(reservation);
            room.setState(RoomStateEnum.OCCUPIED);
        }

        for (Client oldClient : reservation.getOccupants()) {
            oldClient.getReservations().remove(reservation);
        }

        Set<Client> clients = reservationDTO.getClientIds().stream()
                .map(clientId -> {
                    Client client = clientRepository.findById(clientId)
                            .orElseThrow(() -> new ApiException("Cliente con id " + clientId + " no encontrado", HttpStatus.NOT_FOUND));
                    return client;
                })
                .collect(Collectors.toSet());
        for (Client client : clients) {
            client.getReservations().add(reservation);
        }
        reservation.setOccupants(clients);
        reservation.setState(ReservationStateEnum.CHECKED_IN);

        Reservation saved = reservationRepository.save(reservation);

        return saved;
    }

    /**
     * Realiza el check-out de una reserva, actualizando habitaciones y ocupantes.
     *
     * @param hotelId identificador del hotel
     * @param reservationId identificador de la reserva
     * @return reserva actualizada tras el check-out
     * @throws ApiException si no se encuentra la reserva o ya está en estado CHECKED_OUT
     */
    @Transactional
    public Reservation checkOutReservation(Long hotelId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException("Reserva con id " + reservationId + " no encontrada", HttpStatus.NOT_FOUND));

        if (reservation.getState() != ReservationStateEnum.CHECKED_IN) {
            throw new ApiException("Solo se puede hacer check-out de reservas en estado CHECKED_IN.", HttpStatus.CONFLICT);
        }

        reservation.setState(ReservationStateEnum.CHECKED_OUT);

        if (reservation.getDepartureDay().isAfter(LocalDateTime.now())) {
            reservation.setEarlyDeparture(true);
        }

        for (Room room : reservation.getRooms()) {
            room.getReservations().remove(reservation);
            room.setState(RoomStateEnum.DIRTY);
        }

        for (Client client : reservation.getOccupants()) {
            client.getReservations().remove(reservation);
        }

        Reservation saved = reservationRepository.save(reservation);

        return saved;
    }

    /**
     * Obtiene el número de check-ins realizados hoy en un hotel.
     *
     * @param hotelId identificador del hotel
     * @return número de check-ins realizados hoy
     */
    public Long getCheckInsTodayByHotel(Long hotelId) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findDistinctByRooms_Hotel_Id(hotelId).stream()
                .filter(reservation -> reservation.getEntryDate().toLocalDate().isEqual(today)
                        && reservation.getState() == ReservationStateEnum.CHECKED_IN)
                .count();
    }

    /**
     * Obtiene el número de check-ins pendientes para hoy en un hotel.
     *
     * @param hotelId identificador del hotel
     * @return número de check-ins pendientes hoy
     */
    public Long getPendingCheckInsTodayByHotel(Long hotelId) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findDistinctByRooms_Hotel_Id(hotelId).stream()
                .filter(reservation -> reservation.getEntryDate().toLocalDate().isEqual(today)
                        && reservation.getState() == ReservationStateEnum.CHECKED_IN)
                .count();
    }

    /**
     * Obtiene el número de check-ins completados hoy en un hotel.
     *
     * @param hotelId identificador del hotel
     * @return número de check-ins completados hoy
     */
    public Long getCompletedCheckInsByHotel(Long hotelId) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findDistinctByRooms_Hotel_Id(hotelId).stream()
                .filter(reservation -> reservation.getEntryDate().toLocalDate().isEqual(today)
                        && reservation.getState() == ReservationStateEnum.CHECKED_IN)
                .count();
    }

}