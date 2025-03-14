package com.mccasani.security.repository;

import com.mccasani.security.model.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario,Long> {
    Mono<Usuario> findByUsername(String username);
}
