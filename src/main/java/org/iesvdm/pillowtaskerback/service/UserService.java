package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.exception.HotelNotFoundException;
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

    public User replace(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        user.setName(userDetails.getName());
        user.setMail(userDetails.getMail());
        user.setPassword(userDetails.getPassword());

        return userRepository.save(user);
    }

    public void delete (Long id){
        this.userRepository.findById(id).map(h->{
                    this.userRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new UsuarioNotFoundException(id));
    }
}
