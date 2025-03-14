package com.mccasani.security.controller.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UsuarioResponse {
    private Long id;
    private String username;
    private String password;
    private String role;
}
