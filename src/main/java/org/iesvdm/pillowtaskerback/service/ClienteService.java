package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Cliente;
import org.iesvdm.pillowtaskerback.exception.ClienteNotFoundException;
import org.iesvdm.pillowtaskerback.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Cliente> all(){return this.clienteRepository.findAll();}

    @Transactional
    public Cliente save (Cliente cliente){
        clienteRepository.save(cliente);
        entityManager.refresh(cliente);
        return cliente;
    }

    public Cliente one (Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(()->new ClienteNotFoundException(id));
    }

    public Cliente replace(Long id, Cliente cliente) {
        return this.clienteRepository.findById(id)  // Busca el cliente con el ID proporcionado.
                .map(h -> (id.equals(cliente.getId())  // Compara el ID proporcionado con el del objeto cliente.
                        ? this.clienteRepository.save(cliente)  // Si son iguales, guarda el nuevo cliente en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new ClienteNotFoundException(id));  // Si no se encuentra el cliente con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.clienteRepository.findById(id).map(h->{
                    this.clienteRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new ClienteNotFoundException(id));
    }

}
