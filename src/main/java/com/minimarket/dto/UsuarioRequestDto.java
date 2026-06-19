package com.minimarket.dto;

import com.minimarket.security.validation.StrongPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class UsuarioRequestDto {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    private String username;

    @StrongPassword
    @Size(max = 100, message = "La contraseña no puede superar 100 caracteres")
    private String password;
    private Set<String> roles;

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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
