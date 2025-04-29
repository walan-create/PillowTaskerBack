package org.iesvdm.pillowtaskerback.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.iesvdm.pillowtaskerback.domain.User;

@Data
@AllArgsConstructor
public class AuthResponse {
    private User user;
    private String token;
}
