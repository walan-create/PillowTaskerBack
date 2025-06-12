package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Room;
import org.iesvdm.pillowtaskerback.enums.RoomStateEnum;
import org.springframework.http.HttpStatus;
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de habitaciones.
 * Proporciona operaciones CRUD y utilidades relacionadas con habitaciones de hoteles.
 */
@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    HotelService hotelService;

    @Autowired
    private HotelRepository hotelRepository;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Devuelve la lista completa de habitaciones.
     *
     * @return lista de todas las habitaciones
     */
    public List<Room> all() {
        return this.roomRepository.findAll();
    }

    /**
     * Guarda una nueva habitación en la base de datos y actualiza su estado.
     *
     * @param room habitación a guardar
     * @return habitación guardada
     */
    @Transactional
    public Room save(Room room) {
        roomRepository.save(room);
        entityManager.refresh(room);
        return room;
    }

    /**
     * Busca y devuelve una habitación por su id.
     *
     * @param id identificador de la habitación
     * @return habitación encontrada
     * @throws ApiException si no se encuentra la habitación
     */
    public Room one(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Habitación con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
    }

    /**
     * Reemplaza los datos de una habitación existente por los nuevos datos proporcionados.
     *
     * @param id identificador de la habitación a modificar
     * @param roomDetails datos nuevos de la habitación
     * @return habitación actualizada
     * @throws ApiException si no se encuentra la habitación
     */
    @Transactional
    public Room replace(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Habitación con id " + id + " no encontrada", HttpStatus.NOT_FOUND));
        room.setCode(roomDetails.getCode());
        room.setCapacity(roomDetails.getCapacity());
        room.setNumberOfRooms(roomDetails.getNumberOfRooms());
        room.setKitchen(roomDetails.isKitchen());
        room.setType(roomDetails.getType());
        room.setState(roomDetails.getState());

        return roomRepository.save(room);
    }

    /**
     * Elimina una habitación por su id.
     *
     * @param id identificador de la habitación a eliminar
     * @throws ApiException si no se encuentra la habitación o está asociada a reservas
     */
    public void delete(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ApiException("Habitación con id " + id + " no encontrada", HttpStatus.NOT_FOUND));

        if (room.getReservations() != null && !room.getReservations().isEmpty()) {
            throw new ApiException("No se puede eliminar la habitación porque está asociada a una o más reservas.", HttpStatus.CONFLICT);
        }

        roomRepository.delete(room);
    }

    /**
     * Obtiene la lista de habitaciones asociadas a un hotel específico.
     *
     * @param hotelId identificador del hotel
     * @return lista de habitaciones del hotel
     * @throws ApiException si no se encuentra el hotel
     */
    public List<Room> getRoomsByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));

        return this.roomRepository.findAllByHotel_id(hotelId);
    }

    /**
     * Crea una nueva habitación y la asocia a un hotel.
     *
     * @param hotelId identificador del hotel
     * @param room habitación a crear
     * @return habitación creada y asociada al hotel
     * @throws ApiException si no se encuentra el hotel o ya existe una habitación con ese código
     */
    public Room createRoomForHotel(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));

        boolean roomExists = hotel.getRooms().stream()
                .anyMatch(r -> r.getCode().equalsIgnoreCase(room.getCode()));

        if (roomExists) {
            throw new ApiException("Ya existe una habitación con ese número en este hotel.", HttpStatus.CONFLICT);
        }
        room.setHotel(hotel);
        hotel.getRooms().add(room);

        return roomRepository.save(room);
    }

    /**
     * Obtiene la lista de habitaciones disponibles de un hotel.
     *
     * @param hotelId identificador del hotel
     * @return lista de habitaciones disponibles
     */
    public List<Room> getAvailableRoomsByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.AVAILABLE)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de habitaciones ocupadas de un hotel.
     *
     * @param hotelId identificador del hotel
     * @return lista de habitaciones ocupadas
     */
    public List<Room> getOccupiedRoomsByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.OCCUPIED)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de habitaciones sucias de un hotel.
     *
     * @param hotelId identificador del hotel
     * @return lista de habitaciones sucias
     */
    public List<Room> getDirtyRoomsByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.DIRTY)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la lista de habitaciones en mantenimiento de un hotel.
     *
     * @param hotelId identificador del hotel
     * @return lista de habitaciones en mantenimiento
     */
    public List<Room> getRoomsUnderMaintenanceByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.MAINTENANCE)
                .collect(Collectors.toList());
    }

}