package com.mccasani.security.controller;

import com.mccasani.security.controller.request.LoginRequest;
import com.mccasani.security.controller.request.RegistroUsuarioRequest;
import com.mccasani.security.controller.response.LoginResponse;
import com.mccasani.security.controller.response.UsuarioResponse;
import com.mccasani.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public Mono<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        return this.authService.login(loginRequest);
    }
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> registro(@RequestBody RegistroUsuarioRequest request) {
        return this.authService.nuevoUsuario(request);
    }

    @GetMapping()
    public Flux<UsuarioResponse> listarUsuarios() {
        return this.authService.listarUsuarios();
    }
}