CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Identificador único (clave primaria)
    username VARCHAR(255) NOT NULL UNIQUE, -- Nombre de usuario (único)
    password VARCHAR(255) NOT NULL,        -- Contraseña del usuario
    role VARCHAR(50) NOT NULL              -- Rol del usuario (ej. ROLE_USER, ROLE_ADMIN)
);

CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL UNIQUE,
    estado CHAR(1) NOT NULL
);