package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.response.user.UserCredentialResponse;
import dev.mayutama.smartbook.model.dto.response.user.UserResponse;
import dev.mayutama.smartbook.model.entity.AppUser;
import dev.mayutama.smartbook.model.entity.User;
import dev.mayutama.smartbook.model.entity.UserCredential;
import dev.mayutama.smartbook.repository.UserCredentialRepository;
import dev.mayutama.smartbook.service.UserCredentialService;
import dev.mayutama.smartbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {
    private final UserCredentialRepository userCredentialRepository;
    private final UserService userService;

    @Override
    public AppUser loadUserByUserId(String userId) {
        UserResponse user = userService.findByUserCredentialId(userId);
        UserCredential userCredential = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));
        return AppUser.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .fullName(user.getFullName())
                .roles(userCredential.getRoles())
                .build();
    }

    @Override
    public UserCredentialResponse getByUserId(String userId) {
        User user = userService.findEntityById(userId);
        if (user.getUserCredential() == null){
            return null;
        }
        return UserCredentialResponse.builder()
                .id(user.getUserCredential().getId())
                .email(user.getUserCredential().getEmail())
                .roles(user.getUserCredential().getRoles())
                .build();
    }

    @Override
    public UserCredential getByEmail(String email) {
        return userCredentialRepository.findByEmail(email)
                .orElseThrow(() -> new ApplicationException(
                        HttpStatus.NOT_FOUND.name(),
                        "User Credential with email: " + email + " not found",
                        HttpStatus.NOT_FOUND
                ));
    }

    @Override
    public UserCredential addUserCredential(UserCredential userCredential) {
        return userCredentialRepository.saveAndFlush(userCredential);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));
        UserResponse userResponse = userService.findByUserCredentialId(userCredential.getId());

        return AppUser.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .fullName(userResponse.getFullName())
                .roles(userCredential.getRoles())
                .build();
    }
}
