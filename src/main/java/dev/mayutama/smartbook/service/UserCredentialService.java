package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.model.dto.response.user.UserCredentialRes;
import dev.mayutama.smartbook.model.entity.AppUser;
import dev.mayutama.smartbook.model.entity.UserCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserCredentialService extends UserDetailsService {
    AppUser loadUserByUserId(String userId);
    UserCredential addUserCredential(UserCredential userCredential);
}
