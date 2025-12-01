package com.mateo.api_hotwheels.controller;

import com.mateo.api_hotwheels.dto.CrearUsuarioRequest;
import com.mateo.api_hotwheels.dto.LoginRequest;
import com.mateo.api_hotwheels.dto.LoginResponse;
import com.mateo.api_hotwheels.dto.UsuarioDto;
import com.mateo.api_hotwheels.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public UsuarioDto registrar(@RequestBody CrearUsuarioRequest request) {
        return authService.registrar(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
