package com.mccasani.security.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistroUsuarioRequest {

    private String username;
    private String password;
    private String role;
}
