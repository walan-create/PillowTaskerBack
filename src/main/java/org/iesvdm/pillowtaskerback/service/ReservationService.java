package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.*;
import org.iesvdm.pillowtaskerback.dto.ReservationDTO;
import org.iesvdm.pillowtaskerback.enums.ReservationStateEnum;
import org.iesvdm.pillowtaskerback.enums.RoomStateEnum;
import org.iesvdm.pillowtaskerback.exception.*;
import org.iesvdm.pillowtaskerback.repository.ClientRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.ReservationRepository;
import org.iesvdm.pillowtaskerback.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<Reservation> all() {
        return this.reservationRepository.findAll();
    }

    @Transactional
    public Reservation save(Reservation reservation) {
        reservationRepository.save(reservation);
        entityManager.refresh(reservation);
        return reservation;
    }

    public Reservation one(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    @Transactional
    public Reservation replace(Long id, ReservationDTO dto) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException(id));

        LocalDateTime entryDateTime = dto.getEntryDate();
        LocalDateTime departureDateTime = dto.getDepartureDay();

        if (!entryDateTime.isBefore(departureDateTime)) {
            throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida.");
        }

        // Obtener habitaciones por ID
        Set<Room> rooms = dto.getRoomIds().stream()
                .map(roomId -> roomRepository.findById(roomId)
                        .orElseThrow(() -> new HabitacionNotFoundException(roomId)))
                .collect(Collectors.toSet());

        // Validar que todas las habitaciones sean del mismo hotel
        Long hotelId = rooms.iterator().next().getHotel().getId();
        for (Room room : rooms) {
            if (!room.getHotel().getId().equals(hotelId)) {
                throw new IllegalArgumentException("Todas las habitaciones deben pertenecer al mismo hotel.");
            }
        }

        // Verificar solapamiento de fechas
        for (Room room : rooms) {
            for (Reservation existing : room.getReservations()) {
                if (existing.getId().equals(reservation.getId())) continue; // saltar la actual

                boolean overlap = !(departureDateTime.isBefore(existing.getEntryDate()) ||
                        entryDateTime.isAfter(existing.getDepartureDay()));
                if (overlap) {
                    throw new IllegalArgumentException("La habitación con ID " + room.getId() +
                            " ya está reservada entre " + existing.getEntryDate() + " y " + existing.getDepartureDay());
                }
            }
        }

        // Actualizar campos de la reserva
        reservation.setReservationsName(dto.getReservationsName());
        reservation.setEntryDate(entryDateTime);
        reservation.setDepartureDay(departureDateTime);
        reservation.setState(dto.getState());
        reservation.setEarlyDeparture(dto.isEarlyDeparture());
        reservation.setRooms(rooms);

        // No se asignan clientes por ahora
        reservation.setOccupants(Collections.emptySet());

        return reservationRepository.save(reservation);
    }

    public void delete(Long id) {
        this.reservationRepository.findById(id).map(h -> {
                    this.reservationRepository.delete(h);
                    return h;
                })
                .orElseThrow(() -> new ReservationNotFoundException(id));
    }

    public List<Reservation> getReservationsByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        return this.reservationRepository.findDistinctByRooms_Hotel_Id(hotelId);
    }

    public Reservation createReservation(Long hotelId, ReservationDTO dto) {

        // Validación básica de fechas (asegurarse de que la fecha de entrada es antes de la de salida)
        if (dto.getEntryDate().isAfter(dto.getDepartureDay()) || dto.getEntryDate().isEqual(dto.getDepartureDay())) {
            throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida.");
        }

        // Crear nueva reserva
        Reservation reservation = new Reservation();
        reservation.setReservationsName(dto.getReservationsName());

        // Aquí usamos directamente las fechas recibidas en el DTO (ya son LocalDateTime)
        reservation.setEntryDate(dto.getEntryDate());
        reservation.setDepartureDay(dto.getDepartureDay());

        reservation.setEarlyDeparture(false);
        reservation.setState(ReservationStateEnum.ACTIVE);

        // Obtener y asociar habitaciones (con validación por hotel)
        Set<Room> rooms = dto.getRoomIds().stream()
                .map(id -> roomRepository.findById(id)
                        .orElseThrow(() -> new HabitacionNotFoundException(id)))
                .filter(room -> room.getHotel().getId().equals(hotelId))
                .collect(Collectors.toSet());

        // Validar solapamiento de fechas en cada habitación
        for (Room room : rooms) {
            for (Reservation existing : room.getReservations()) {
                boolean datesOverlap = !(dto.getDepartureDay().isBefore(existing.getEntryDate()) ||
                        dto.getEntryDate().isAfter(existing.getDepartureDay()));
                if (datesOverlap) {
                    throw new IllegalArgumentException("La habitación con ID " + room.getId() +
                            " ya está reservada entre " + existing.getEntryDate() + " y " + existing.getDepartureDay());
                }
            }
        }

        // Asociar habitaciones a la reserva
        reservation.setRooms(rooms);
        for (Room room : rooms) {
            room.getReservations().add(reservation); // sincronización en memoria
        }

        // No incluimos clientes hasta el check-in
        reservation.setOccupants(Collections.emptySet()); // Por claridad

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation expandReservation(Long hotelId, Long reservationId, ReservationDTO reservationDTO) {

        // Buscar la reserva anterior
        Reservation previousReservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        // Validación básica de fechas
        if (reservationDTO.getEntryDate().isAfter(reservationDTO.getDepartureDay()) ||
                reservationDTO.getEntryDate().isEqual(reservationDTO.getDepartureDay())) {
            throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida.");
        }

        // Crear nueva reserva
        Reservation newReservation = new Reservation();
        newReservation.setReservationsName(reservationDTO.getReservationsName());
        newReservation.setEntryDate(reservationDTO.getEntryDate());
        newReservation.setDepartureDay(reservationDTO.getDepartureDay());
        newReservation.setState(ReservationStateEnum.ACTIVE);
        newReservation.setEarlyDeparture(false);

        // Obtener habitaciones y validar que pertenezcan al mismo hotel
        Set<Room> rooms = reservationDTO.getRoomIds().stream()
                .map(id -> roomRepository.findById(id)
                        .orElseThrow(() -> new HabitacionNotFoundException(id)))
                .filter(room -> room.getHotel().getId().equals(hotelId))
                .collect(Collectors.toSet());

        // Validar solapamiento de fechas en las habitaciones
        for (Room room : rooms) {
            for (Reservation existing : room.getReservations()) {
                boolean datesOverlap = !(reservationDTO.getDepartureDay().isBefore(existing.getEntryDate()) ||
                        reservationDTO.getEntryDate().isAfter(existing.getDepartureDay()));
                if (datesOverlap) {
                    throw new IllegalArgumentException("La habitación con ID " + room.getId() +
                            " ya está reservada entre " + existing.getEntryDate() + " y " + existing.getDepartureDay());
                }
            }
        }

        // Asociar habitaciones
        newReservation.setRooms(rooms);
        for (Room room : rooms) {
            room.getReservations().add(newReservation); // sincronización en memoria
        }

        // No asignamos clientes todavía
        newReservation.setOccupants(Collections.emptySet());

        // Aquí se asocia la nueva reserva con la anterior
        newReservation.setPreviousReservation(previousReservation);

        // Marcar la reserva anterior como CHECKED_OUT
        previousReservation.setState(ReservationStateEnum.CHECKED_OUT);

        // Guardar y devolver
        return reservationRepository.save(newReservation);
    }

    @Transactional
    public Reservation checkInReservation(Long hotelId, Long reservationId, ReservationDTO reservationDTO) {

        // Buscar la reserva
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        // Marcar el estado de la reserva como CHECKED_IN
        reservation.setState(ReservationStateEnum.CHECKED_IN);

        // Asignar clientes
        Set<Client> clients = reservationDTO.getClientIds().stream()
                .map(clientId -> clientRepository.findById(clientId)
                        .orElseThrow(() -> new ClienteNotFoundException(clientId)))
                .collect(Collectors.toSet());

        // Sincronizar la relación en ambos lados (de los clientes con la reserva)
        for (Client client : clients) {
            client.getReservations().add(reservation); // Asegura que el cliente conoce la reserva
        }

        reservation.setOccupants(clients);

        // Marcar las habitaciones como OCCUPIED
        for (Room room : reservation.getRooms()) {
            room.setState(RoomStateEnum.OCCUPIED);
        }

        // Guardar cambios en la reserva
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation checkOutReservation(Long hotelId, Long reservationId) {

        // Buscar la reserva
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        // Cambiar el estado de la reserva a CHECKED_OUT
        reservation.setState(ReservationStateEnum.CHECKED_OUT);

        // Para cada habitación asociada, quitar esta reserva y cambiar estado
        for (Room room : reservation.getRooms()) {
            room.getReservations().remove(reservation); // Eliminar relación de la habitación hacia la reserva
            room.setState(RoomStateEnum.DIRTY); // Marcar para limpieza
        }

        // Para cada cliente asociado, quitar esta reserva
        for (Client client : reservation.getOccupants()) {
            client.getReservations().remove(reservation); // Eliminar relación de cliente hacia reserva
        }

        // OJO: NO vaciamos reservation.getRooms() ni reservation.getOccupants()
        // así conservamos el historial de qué habitaciones y clientes tuvo esta reserva.

        return reservationRepository.save(reservation);
    }

    public Long getCheckInsTodayByHotel(Long hotelId) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findDistinctByRooms_Hotel_Id(hotelId).stream()
                .filter(reservation -> reservation.getEntryDate().toLocalDate().isEqual(today)
                        && reservation.getState() == ReservationStateEnum.CHECKED_IN)
                .count();
    }

    public Long getPendingCheckInsTodayByHotel(Long hotelId) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findDistinctByRooms_Hotel_Id(hotelId).stream()
                .filter(reservation -> reservation.getEntryDate().toLocalDate().isEqual(today)
                        && reservation.getState() == ReservationStateEnum.ACTIVE)
                .count();
    }

    public Long getCompletedCheckInsByHotel(Long hotelId) {
        LocalDate today = LocalDate.now();
        return reservationRepository.findDistinctByRooms_Hotel_Id(hotelId).stream()
                .filter(reservation -> reservation.getEntryDate().toLocalDate().isEqual(today)
                        && reservation.getState() == ReservationStateEnum.CHECKED_IN)
                .count();
    }

}

