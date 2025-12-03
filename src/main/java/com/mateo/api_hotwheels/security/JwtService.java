package com.mateo.api_hotwheels.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String SECRET = "supersecreto123"; // cámbialo por tu secreto real
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    // Duración del token: 24 hrs
    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    // Generar token
    public String generarToken(String email, String rol) {
        return JWT.create()
                .withSubject(email)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(algorithm);
    }

    // Validar y obtener JWT decodificado
    public DecodedJWT validarToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token);
    }

    // Extraer email
    public String obtenerEmail(String token) {
        return validarToken(token).getSubject();
    }

    // Extraer rol
    public String obtenerRol(String token) {
        return validarToken(token).getClaim("rol").asString();
    }
}
