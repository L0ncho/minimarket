package com.minimarket.controller;

import com.minimarket.security.model.AuthResponse;
import com.minimarket.security.model.LoginRequest;
import com.minimarket.security.model.RegistroRequest;
import com.minimarket.security.service.JwtTokenService;
import com.minimarket.security.service.LoginAttemptService;
import com.minimarket.entity.Usuario;
import com.minimarket.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final UsuarioService usuarioService;
    private final LoginAttemptService loginAttemptService;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenService jwtTokenService,
            UsuarioService usuarioService,
            LoginAttemptService loginAttemptService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.usuarioService = usuarioService;
        this.loginAttemptService = loginAttemptService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@Valid @RequestBody LoginRequest loginRequest) {
        loginAttemptService.assertNotLocked(loginRequest.getUsername());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException badCredentialsException) {
            loginAttemptService.recordFailure(loginRequest.getUsername());
            throw badCredentialsException;
        }

        loginAttemptService.recordSuccess(loginRequest.getUsername());

        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        String jwt = jwtTokenService.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/registro")
    public ResponseEntity<Map<String, String>> registrarUsuario(@Valid @RequestBody RegistroRequest registroRequest) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(registroRequest.getUsername());
        nuevoUsuario.setPassword(registroRequest.getPassword());

        usuarioService.save(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("mensaje", "Usuario registrado con éxito"));
    }
}
