// src/main/java/org/iesvdm/pillowtaskerback/service/ClientService.java
package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Client;
import org.iesvdm.pillowtaskerback.domain.Hotel;
import org.iesvdm.pillowtaskerback.exception.ApiException;
import org.iesvdm.pillowtaskerback.repository.ClientRepository;
import org.iesvdm.pillowtaskerback.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    /**
     * Devuelve la lista completa de clientes.
     *
     * @return lista de todos los clientes
     */
    public List<Client> all() {
        return this.clientRepository.findAll();
    }

    /**
     * Guarda un nuevo cliente en la base de datos y actualiza su estado.
     *
     * @param client cliente a guardar
     * @return cliente guardado
     */
    @Transactional
    public Client save(Client client) {
        clientRepository.save(client);
        entityManager.refresh(client);
        return client;
    }

    /**
     * Busca y devuelve un cliente por su id.
     *
     * @param id identificador del cliente
     * @return cliente encontrado
     * @throws ApiException si no se encuentra el cliente
     */
    public Client one(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ApiException("Cliente con id " + id + " no encontrado", HttpStatus.NOT_FOUND));
    }

    /**
     * Reemplaza los datos de un cliente existente por los nuevos datos proporcionados.
     *
     * @param id identificador del cliente a modificar
     * @param clientDetails datos nuevos del cliente
     * @return cliente actualizado
     * @throws ApiException si no se encuentra el cliente
     */
    public Client replace(Long id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ApiException("Cliente con id " + id + " no encontrado", HttpStatus.NOT_FOUND));

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

    /**
     * Elimina un cliente por su id si no tiene reservas asociadas.
     *
     * @param id identificador del cliente a eliminar
     * @throws ApiException si no se encuentra el cliente o tiene reservas asociadas
     */
    public void delete(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ApiException("Cliente con id " + id + " no encontrado", HttpStatus.NOT_FOUND));

        if (client.getReservations() != null && !client.getReservations().isEmpty()) {
            throw new ApiException("No se puede eliminar el cliente porque está asociado a una o más reservas.", HttpStatus.CONFLICT);
        }

        clientRepository.delete(client);
    }

    /**
     * Obtiene la lista de clientes asociados a un hotel específico.
     *
     * @param hotelId identificador del hotel
     * @return lista de clientes del hotel
     * @throws ApiException si no se encuentra el hotel
     */
    public List<Client> getClientsByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));

        return this.clientRepository.findAllByHotel_id(hotelId);
    }

    /**
     * Crea un nuevo cliente y lo asocia a un hotel, validando que no exista ya por NIF.
     *
     * @param hotelId identificador del hotel
     * @param client cliente a crear
     * @return cliente creado y asociado al hotel
     * @throws ApiException si el hotel no existe o el NIF ya está registrado en ese hotel
     */
    public Client createClientForHotel(Long hotelId, Client client) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel con id " + hotelId + " no encontrado", HttpStatus.NOT_FOUND));

        Optional<Client> existingClient = clientRepository.findByNifAndHotelId(client.getNif(), hotelId);
        if (existingClient.isPresent()) {
            throw new ApiException("Ya existe un cliente con NIF " + client.getNif() + " en este hotel", HttpStatus.CONFLICT);
        }

        client.setHotel(hotel);
        hotel.getClients().add(client);
        return clientRepository.save(client);
    }
}