package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {
    @Autowired
    HotelRepository hotelRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Hotel> all(){return this.hotelRepository.findAll();}

    @Transactional
    public Hotel save (Hotel hotel){
        hotelRepository.save(hotel);
        entityManager.refresh(hotel);
        return hotel;
    }

    public Hotel one (Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(()->new HotelNotFoundException(id));
    }

    public Hotel replace(Long id, Hotel hotel) {
        return this.hotelRepository.findById(id)  // Busca el hotel con el ID proporcionado.
                .map(h -> (id.equals(hotel.getId())  // Compara el ID proporcionado con el del objeto hotel.
                        ? this.hotelRepository.save(hotel)  // Si son iguales, guarda el nuevo hotel en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new HotelNotFoundException(id));  // Si no se encuentra el hotel con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.hotelRepository.findById(id).map(h->{
                    this.hotelRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new HotelNotFoundException(id));
    }

}
