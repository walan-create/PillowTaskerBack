package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.Room;
import org.iesvdm.pillowtaskerback.enums.RoomStateEnum;
import org.iesvdm.pillowtaskerback.exception.HabitacionNotFoundException;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.iesvdm.pillowtaskerback.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Room> all(){return this.roomRepository.findAll();}

    @Transactional
    public Room save (Room room){
        roomRepository.save(room);
        entityManager.refresh(room);
        return room;
    }

    public Room one (Long id) {
        return roomRepository.findById(id)
                .orElseThrow(()->new HabitacionNotFoundException(id));
    }

    @Transactional
    public Room replace(Long id, Room roomDetails) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new HabitacionNotFoundException(id));
        room.setNumberRoom(roomDetails.getNumberRoom());
        room.setCapacity(roomDetails.getCapacity());
        room.setRoomsNumber(roomDetails.getRoomsNumber());
        room.setKitchen(roomDetails.isKitchen());
        room.setType(roomDetails.getType());
        room.setState(roomDetails.getState());

        return roomRepository.save(room);
    }

    public void delete (Long id){
        this.roomRepository.findById(id).map(h->{
                    this.roomRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new HabitacionNotFoundException(id));
    }

    public List<Room> getRoomsByHotel(Long hotelId){
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        return this.roomRepository.findAllByHotel_id(hotelId);
    }

    public Room createRoomForHotel(Long hotelId, Room room) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        // Verificar si ya existe una habitación con el mismo número en ese hotel
        boolean roomExists = hotel.getRooms().stream()
                .anyMatch(r -> r.getNumberRoom().equalsIgnoreCase(room.getNumberRoom()));

        if (roomExists) {
            throw new IllegalArgumentException("Ya existe una habitación con ese número en este hotel.");
        }
        // Asociar habitación con el hotel
        room.setHotel(hotel);
        hotel.getRooms().add(room);

        return roomRepository.save(room);
    }

    public List<Room> getAvailableRoomsByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.AVAILABLE)
                .collect(Collectors.toList());
    }

    public List<Room> getOccupiedRoomsByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.OCCUPIED)
                .collect(Collectors.toList());
    }

    public List<Room> getDirtyRoomsByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.DIRTY)
                .collect(Collectors.toList());
    }

    public List<Room> getRoomsUnderMaintenanceByHotel(Long hotelId) {
        return roomRepository.findAllByHotel_id(hotelId).stream()
                .filter(room -> room.getState() == RoomStateEnum.MAINTENANCE)
                .collect(Collectors.toList());
    }


}
