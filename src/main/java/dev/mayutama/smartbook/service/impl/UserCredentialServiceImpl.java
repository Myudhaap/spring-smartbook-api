package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.model.entity.AppUser;
import dev.mayutama.smartbook.model.entity.UserCredential;
import dev.mayutama.smartbook.repository.UserCredentialRepository;
import dev.mayutama.smartbook.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCredentialServiceImpl implements UserCredentialService {
    private final UserCredentialRepository userCredentialRepository;

    @Override
    public AppUser loadUserByUserId(String userId) {
        UserCredential userCredential = userCredentialRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));
        return AppUser.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .fullName(userCredential.getUser().getFullName())
                .roles(userCredential.getRoles())
                .build();
    }

    @Override
    public UserCredential addUserCredential(UserCredential userCredential) {
        return userCredentialRepository.saveAndFlush(userCredential);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByEmailAndIsActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credential"));

        return AppUser.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .fullName(userCredential.getUser().getFullName())
                .roles(userCredential.getRoles())
                .build();
    }
}
