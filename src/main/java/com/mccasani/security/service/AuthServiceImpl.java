package com.mccasani.security.service;

import com.mccasani.security.controller.request.LoginRequest;
import com.mccasani.security.controller.request.RegistroUsuarioRequest;
import com.mccasani.security.controller.response.LoginResponse;
import com.mccasani.security.controller.response.UsuarioResponse;
import com.mccasani.security.model.Usuario;
import com.mccasani.security.repository.UsuarioRepository;
import com.mccasani.security.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UsuarioRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<LoginResponse> login(LoginRequest loginRequest) {
        return userRepository.findByUsername(loginRequest.getUsername())
                .doOnNext(u -> System.out.println("DATA: " + u.getUsername()))
                .filter(user -> passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) // Usa PasswordEncoder
                .flatMap(user -> {
                    // Genera el token con el username y el rol
                    String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
                    // Retorna la respuesta con el token
                    return Mono.just(LoginResponse.builder().jwt(token).build());
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")));
    }

    @Override
    public Mono<Void> nuevoUsuario(RegistroUsuarioRequest request) {
        return Mono.just(request)
                .map(req ->Usuario.builder()
                        .username(req.getUsername())
                        .password(this.passwordEncoder.encode(req.getPassword()))
                        .role(req.getRole())
                        .build())
                .flatMap(userRepository::save)
                .then();
    }

    @Override
    public Flux<UsuarioResponse> listarUsuarios() {
        return this.userRepository.findAll()
                .map(usuario -> UsuarioResponse.builder()
                        .id(usuario.getId())
                        .username(usuario.getUsername())
                        .password(usuario.getPassword())
                        .role(usuario.getRole())
                        .build());
    }


}