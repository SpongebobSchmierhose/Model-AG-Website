package de.api.backend.application.utils;

import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import de.api.backend.domain.user.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
@Component
public class JwtUtils {

    @Value("${myapp.jwtSecret}")
    private String jwtSecret;

    @Value("${myapp.jwtAccessExpirationMs}")
    private int jwtAccessExpirationMs;


    @Value("${myapp.jwtRefreshExpirationMs}")
    private long jwtRefreshExpirationMs;

    public String createAccessToken(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtAccessExpirationMs))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String createAccessToken(UserEntity user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtAccessExpirationMs))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String createRefreshToken(User user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    public String createRefreshToken(UserEntity user, HttpServletRequest request) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
    }

    private DecodedJWT decodeJWTToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public String getUsernameFromToken(String token) {
        return decodeJWTToken(token).getSubject();
    }

    public String[] getRolesFromToken(String token) {
        return decodeJWTToken(token).getClaim("roles").asArray(String.class);
    }
}
