package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.request.auth.AuthReq;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.model.entity.UserCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserCredentialMapper {
    private final PasswordEncoder passwordEncoder;
    public UserCredential toEntity(AuthReq req, Set<Role> roles) {
        return UserCredential.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(roles)
                .build();
    }
}
