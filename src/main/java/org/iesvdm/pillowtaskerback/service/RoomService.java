package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Room;
import org.iesvdm.pillowtaskerback.exception.HabitacionNotFoundException;
import org.iesvdm.pillowtaskerback.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

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

    public Room replace(Long id, Room room) {
        return this.roomRepository.findById(id)  // Busca el room con el ID proporcionado.
                .map(h -> (id.equals(room.getId())  // Compara el ID proporcionado con el del objeto room.
                        ? this.roomRepository.save(room)  // Si son iguales, guarda el nuevo room en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new HabitacionNotFoundException(id));  // Si no se encuentra el room con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.roomRepository.findById(id).map(h->{
                    this.roomRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new HabitacionNotFoundException(id));
    }

}
