package com.mccasani.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class Usuario {
    @Id
    private Long id;
    private String username;
    private String password;
    private String role;
}
