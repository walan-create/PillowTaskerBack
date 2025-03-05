package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Client;
import org.iesvdm.pillowtaskerback.exception.ClienteNotFoundException;
import org.iesvdm.pillowtaskerback.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

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

    public Client replace(Long id, Client client) {
        return this.clientRepository.findById(id)  // Busca el client con el ID proporcionado.
                .map(h -> (id.equals(client.getId())  // Compara el ID proporcionado con el del objeto client.
                        ? this.clientRepository.save(client)  // Si son iguales, guarda el nuevo client en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new ClienteNotFoundException(id));  // Si no se encuentra el client con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.clientRepository.findById(id).map(h->{
                    this.clientRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new ClienteNotFoundException(id));
    }

}
