package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Client;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.exception.ClienteNotFoundException;
import org.iesvdm.pillowtaskerback.exception.DuplicateClientNifException;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.ClientRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Client> all(){return this.clientRepository.findAll();}

    @Transactional
    public Client save (Client client){
        clientRepository.save(client);
        entityManager.refresh(client);
        return client;
    }

    public Client one (Long id) {
        return clientRepository.findById(id)
                .orElseThrow(()->new ClienteNotFoundException(id));
    }

    public Client replace(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        client.setNif(clientDetails.getNif());
        client.setName(clientDetails.getName());
        client.setSurname1(clientDetails.getSurname1());
        client.setSurname2(clientDetails.getSurname2());
        client.setBirthDate(clientDetails.getBirthDate());
        client.setNationality(clientDetails.getNationality());
        client.setAddress(clientDetails.getAddress());
        client.setPostalCode(clientDetails.getPostalCode());
        client.setPhoneNumber(clientDetails.getPhoneNumber());

        return clientRepository.save(client);
    }


    public void delete (Long id){
        this.clientRepository.findById(id).map(h->{
                    this.clientRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new ClienteNotFoundException(id));
    }

    public List<Client> getClientsByHotel(Long hotelId){
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        return this.clientRepository.findAllByHotel_id(hotelId);
    }

    public Client createClientForHotel(Long hotelId, Client client) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new HotelNotFoundException(hotelId));

        // Verificación: ¿existe cliente con ese NIF en este hotel?
        Optional<Client> existingClient = clientRepository.findByNifAndHotelId(client.getNif(), hotelId);
        if (existingClient.isPresent()) {
            throw new DuplicateClientNifException(client.getNif(), hotelId);
        }

        client.setHotel(hotel);
        hotel.getClients().add(client);
        return clientRepository.save(client);
    }



}
