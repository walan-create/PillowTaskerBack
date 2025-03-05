package org.iesvdm.pillowtaskerback.security;

import org.iesvdm.pillowtaskerback.domain.Usuario;
import org.iesvdm.pillowtaskerback.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
/*
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        return new CustomUserDetails(usuario);
    }

}
*/