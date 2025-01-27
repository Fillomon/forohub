package com.challange.forohub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.challange.forohub.domains.users.User;
import com.challange.forohub.domains.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Autowired
    private UserRepository userRepository;  // Suponiendo que tienes un UserRepository

    public String generateToken(User usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(usuario.getPassword());
            return JWT.create()
                    .withIssuer("forohub")
                    .withSubject(usuario.getMail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generateExpires())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error creating JWT token", exception);
        }
    }

    public String getSubject(String token) {
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }

        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            String user = decodedJWT.getSubject();

            User u = (User) userRepository.findByMail(user);

            User usuario = userRepository.findById(u.getId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Algorithm algorithm = Algorithm.HMAC256(usuario.getPassword());
            JWT.require(algorithm)
                    .withIssuer("forohub")
                    .build()
                    .verify(token);

            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Error verifying JWT token", exception);
        }
    }

    private Instant generateExpires() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}
