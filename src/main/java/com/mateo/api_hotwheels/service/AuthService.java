package com.mateo.api_hotwheels.service;

import com.mateo.api_hotwheels.dto.CrearUsuarioRequest;
import com.mateo.api_hotwheels.dto.LoginRequest;
import com.mateo.api_hotwheels.dto.LoginResponse;
import com.mateo.api_hotwheels.dto.UsuarioDto;
import com.mateo.api_hotwheels.model.Usuario;
import com.mateo.api_hotwheels.repository.UsuarioRepository;
import com.mateo.api_hotwheels.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ------------------ REGISTRO ------------------
    public UsuarioDto registrar(CrearUsuarioRequest request) {

        usuarioRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    throw new RuntimeException("El correo ya está registrado");
                });

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol("CLIENTE");

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToDto(guardado);
    }

    // ------------------ LOGIN ------------------
    public LoginResponse login(LoginRequest request) {

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // GENERAR JWT REAL
        String token = jwtService.generarToken(usuario.getEmail(), usuario.getRol());

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsuario(mapToDto(usuario));

        return response;
    }

    // ------------------ MAPEO ------------------
    private UsuarioDto mapToDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        return dto;
    }
}
