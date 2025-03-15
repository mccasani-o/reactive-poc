package com.mccasani.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    public static final String URI = "/api/productos/test";
    private WebTestClient client;

    @BeforeEach
    void setUp() {
        client = WebTestClient.bindToController(new ProductoController()).build();
    }


    @Test
    void exitosoTest() {
        client.get().uri(URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class) // ✅ Cambiado a `expectBody(String.class)`
                .consumeWith(response -> {
                    String resp = response.getResponseBody();
                    Assertions.assertNotNull(resp);
                    Assertions.assertEquals("OK", resp); // ✅ Verifica que el contenido sea "OK"
                });
    }



}