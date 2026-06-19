package com.minimarket.security.model;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio y no puede estar vacío")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria y no puede estar vacía")
    private String password;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
