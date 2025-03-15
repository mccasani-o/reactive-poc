package com.mccasani;

import com.mccasani.security.model.Usuario;
import com.mccasani.security.repository.UsuarioRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@SpringBootApplication
public class Application implements CommandLineRunner {

	private final UsuarioRepository usuarioRepository;
	private final PasswordEncoder passwordEncoder;

    public Application(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		this.usuarioRepository.save(new Usuario(null, "Mauricio-ccasani", this.passwordEncoder.encode("Mauricio12345@"), "ROLE_ADMIN"))
				.thenMany(this.usuarioRepository.findAll()) // Ejecuta findAll después de guardar
				.doOnNext(usuario -> System.out.println("Usuario en BD: " + usuario))
				.subscribe();

		this.usuarioRepository.findByUsername("mau").doOnNext(u->log.info("DATA: {}", u)).subscribe();
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

		// Convertir la clave a Base64 para almacenarla o usarla en tu aplicación
		String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
		System.out.println("Clave secreta en Base64: " + base64Key);
	}

}
