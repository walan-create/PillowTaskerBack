package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.User;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.UserRepository;
import org.iesvdm.pillowtaskerback.security.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @PersistenceContext
    EntityManager entityManager;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> all(){return this.userRepository.findAll();}

    @Transactional
    public User save (User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
        user.setMail(userDetails.getMail());
        user.setPassword(userDetails.getPassword());
        user.setName(userDetails.getName());
        user.setSurname1(userDetails.getSurname1());
        user.setSurname2(userDetails.getSurname2());
        user.setDni(userDetails.getDni());

        return userRepository.save(user);
    }

    public void delete (Long id){
        this.userRepository.findById(id).map(h->{
                    this.userRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new UsuarioNotFoundException(id));
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByMail(email);
    }

    public User register(RegisterRequest req) {
        User user = User.builder()
                .mail(req.getMail())
                .password(passwordEncoder.encode(req.getPassword()))
                .name(req.getName())
                .surname1(req.getSurname1())
                .surname2(req.getSurname2())
                .dni(req.getDni())
                .build();
        return userRepository.save(user);
    }

    public boolean checkPassword(User user, String rawPassword) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
