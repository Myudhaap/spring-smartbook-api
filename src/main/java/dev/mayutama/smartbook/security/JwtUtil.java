package dev.mayutama.smartbook.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.entity.AppUser;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${dev.mayutama.smartbook.jwt.jwt-secret}")
    private String jwtSecret;
    @Value("${dev.mayutama.smartbook.jwt.app-name}")
    private String appName;
    @Value("${dev.mayutama.smartbook.jwt.jwt-expired}")
    private Long jwtExpiredAt;
    @Value("${dev.mayutama.smartbook.jwt.jwt-expired-refresh}")
    private Long jwtExpiredRefreshAt;

    public String generateToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));

            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(appUser.getId())
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpiredAt))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", appUser.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toList()))
                    .withClaim("email", appUser.getEmail())
                    .withClaim("fullName", appUser.getFullName())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public String generateRefreshToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));

            return JWT.create()
                    .withIssuer(appName)
                    .withSubject(appUser.getId())
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpiredRefreshAt))
                    .withIssuedAt(Instant.now())
                    .withClaim("role", appUser.getRoles().stream()
                            .map(Role::getName)
                            .collect(Collectors.toList()))
                    .withClaim("email", appUser.getEmail())
                    .withClaim("fullName", appUser.getFullName())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException();
        }
    }

    public Boolean verifyJwtToken(String token, HttpServletResponse response) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            return decodedJWT.getIssuer().equals(appName) && decodedJWT.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            CookieUtil.clearAuthCookie(response, true, false);
            throw new ApplicationException(
                    HttpStatus.FORBIDDEN.name(),
                    "Access Token already expired. Please generate new access token",
                    HttpStatus.FORBIDDEN
            );

        }
    }

    public Boolean verifyJwtRefreshToken(String token, HttpServletResponse response) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            System.out.println(decodedJWT);
            return decodedJWT.getIssuer().equals(appName) && decodedJWT.getExpiresAt().after(new Date());
        } catch (JWTVerificationException e) {
            CookieUtil.clearAuthCookie(response, true, true);
            throw new ApplicationException(
                    HttpStatus.FORBIDDEN.name(),
                    "Refresh Token already expired. Please login again",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    public Map<String, String> getUserInfoByToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("userId", decodedJWT.getSubject());
            userInfo.put("role", decodedJWT.getClaim("role").asString());
            userInfo.put("email", decodedJWT.getClaim("email").asString());
            userInfo.put("fullName", decodedJWT.getClaim("fullName").asString());

            return userInfo;
        } catch (JWTVerificationException e) {
            throw new RuntimeException();
        }
    }
}
