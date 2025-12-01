package com.mateo.api_hotwheels.service;

import com.mateo.api_hotwheels.dto.CrearUsuarioRequest;
import com.mateo.api_hotwheels.dto.LoginRequest;
import com.mateo.api_hotwheels.dto.LoginResponse;
import com.mateo.api_hotwheels.dto.UsuarioDto;
import com.mateo.api_hotwheels.model.Usuario;
import com.mateo.api_hotwheels.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTRO
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

    // LOGIN
    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String rawToken = usuario.getId() + ":" + usuario.getEmail() + ":" + System.currentTimeMillis();
        String token = Base64.getEncoder().encodeToString(rawToken.getBytes());

        UsuarioDto dto = mapToDto(usuario);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsuario(dto);

        return response;
    }

    private UsuarioDto mapToDto(Usuario usuario) {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());
        return dto;
    }
}
