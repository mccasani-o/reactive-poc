package com.mccasani.security.controller;


import com.mccasani.security.controller.request.LoginRequest;
import com.mccasani.security.controller.request.RegistroUsuarioRequest;
import com.mccasani.security.controller.response.LoginResponse;
import com.mccasani.security.controller.response.UsuarioResponse;
import com.mccasani.security.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    private AuthService authService;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.authService = Mockito.mock(AuthService.class);
        AuthController authController = new AuthController(authService);
        this.webTestClient = WebTestClient.bindToController(authController).build();
    }

    @Test
    void testLogin_Success() {

        LoginRequest request = new LoginRequest("user", "password");
        LoginResponse expectedResponse = LoginResponse.builder().jwt("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2FyaW8iLCJyb2xlcyI6WyJST0xFX1VTRVIiXSwiZXhwIjoxNzQyNDAwMjg3fQ.bEZuA5wDvLEST-VXWzMZejX1pkjc46oTMPaoYHqoB761TANEYyR6i0LNe65zVQdkQraBHbzj7orQfvT7Y8CE4A").build();

        when(authService.login(any(LoginRequest.class))).thenReturn(Mono.just(expectedResponse));


        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    void testRegistro_Success() {

        RegistroUsuarioRequest request = new RegistroUsuarioRequest("newUser", "password", "USER");

        when(authService.nuevoUsuario(any(RegistroUsuarioRequest.class))).thenReturn(Mono.empty());


        webTestClient.post()
                .uri("/api/auth/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Void.class);
    }

    @Test
    void testListarUsuarios_Success() {

        UsuarioResponse usuario1= UsuarioResponse.builder().id(1L).username("user1").password("USER").role("123").build();
        UsuarioResponse usuario2= UsuarioResponse.builder().id(1L).username("user2").password("ADMIN").role("123").build();

        when(authService.listarUsuarios()).thenReturn(Flux.just(usuario1, usuario2));


        webTestClient.get()
                .uri("/api/auth")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UsuarioResponse.class)
                .hasSize(2)
                .contains(usuario1, usuario2);
    }

    @Test
    void testLogin_Failure() {

        LoginRequest request = new LoginRequest("invalidUser", "wrongPassword");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("Credenciales inválidas")));


        webTestClient.post()
                .uri("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class);
    }
}
