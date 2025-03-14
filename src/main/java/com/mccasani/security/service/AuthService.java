package com.mccasani.security.service;

import com.mccasani.security.controller.request.LoginRequest;
import com.mccasani.security.controller.request.RegistroUsuarioRequest;
import com.mccasani.security.controller.response.LoginResponse;
import com.mccasani.security.controller.response.UsuarioResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthService {

    Mono<LoginResponse>login(LoginRequest loginRequest);

    Mono<Void> nuevoUsuario(RegistroUsuarioRequest request);

    Flux<UsuarioResponse> listarUsuarios();

}
