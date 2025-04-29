package org.iesvdm.pillowtaskerback.security;

import lombok.Data;

@Data
public class LoginRequest {
    private String mail;
    private String password;
}

