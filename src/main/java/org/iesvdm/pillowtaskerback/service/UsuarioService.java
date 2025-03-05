package org.iesvdm.pillowtaskerback.service;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesvdm.pillowtaskerback.domain.Usuario;
import org.iesvdm.pillowtaskerback.exception.UsuarioNotFoundException;
import org.iesvdm.pillowtaskerback.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @PersistenceContext
    EntityManager entityManager;

    public List<Usuario> all(){return this.usuarioRepository.findAll();}

    @Transactional
    public Usuario save (Usuario usuario){
        usuarioRepository.save(usuario);
        entityManager.refresh(usuario);
        return usuario;
    }

    public Usuario one (Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(()->new UsuarioNotFoundException(id));
    }

    public Usuario replace(Long id, Usuario usuario) {
        return this.usuarioRepository.findById(id)  // Busca el usuario con el ID proporcionado.
                .map(h -> (id.equals(usuario.getId())  // Compara el ID proporcionado con el del objeto usuario.
                        ? this.usuarioRepository.save(usuario)  // Si son iguales, guarda el nuevo usuario en la base de datos.
                        : null))  // Si los IDs no coinciden, devuelve null.
                .orElseThrow(() -> new UsuarioNotFoundException(id));  // Si no se encuentra el usuario con ese ID, lanza una excepción.
    }

    public void delete (Long id){
        this.usuarioRepository.findById(id).map(h->{
                    this.usuarioRepository.delete(h);
                    return h; })
                .orElseThrow(()-> new UsuarioNotFoundException(id));
    }

}
