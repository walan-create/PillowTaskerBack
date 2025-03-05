package org.iesvdm.pillowtaskerback.security;

/*
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository usuarioRepository;

    public CustomUserDetailsService(UserRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado: " + email));
        return new CustomUserDetails(user);
    }

}
*/