package com.mccasani.security.controller.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private String jwt;
}
