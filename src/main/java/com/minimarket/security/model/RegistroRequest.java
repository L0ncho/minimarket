package com.minimarket.security.model;

import com.minimarket.security.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroRequest {
    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @StrongPassword
    @Size(max = 100, message = "La contraseña no puede superar 100 caracteres")
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
