package org.iesvdm.pillowtaskerback.security;
import org.iesvdm.pillowtaskerback.domain.Usuario;


import java.util.Collection;
import java.util.Collections;
/*
public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;

    public CustomUserDetails(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() { return email; } // Usamos email como identificador

    @Override
    public String getPassword() { return password; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.emptyList(); }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
*/
