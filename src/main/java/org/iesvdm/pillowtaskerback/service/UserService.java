package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<User> all(){return this.userRepository.findAll();}

    @Transactional
    public User save (User user){
        userRepository.save(user);
        entityManager.refresh(user);
        return user;
    }

    public User one (Long id) {
        return userRepository.findById(id)
                .orElseThrow(()->new UsuarioNotFoundException(id));
    }

    public User replace(Long id, User user) {
        return this.userRepository.findById(id)  // Busca el user con el ID proporcionado.
                .map(h -> (id.equals(user.getId())  // Compara el ID proporcionado con el del objeto user.
                        ? this.userRepository.save(user)  // Si son iguales, guarda el nuevo user en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new UsuarioNotFoundException(id));  // Si no se encuentra el user con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.userRepository.findById(id).map(h->{
                    this.userRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new UsuarioNotFoundException(id));
    }

}
