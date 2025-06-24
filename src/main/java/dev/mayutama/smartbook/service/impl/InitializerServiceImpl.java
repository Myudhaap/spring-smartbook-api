package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.constant.EGender;
import dev.mayutama.smartbook.constant.EMaritalStatus;
import dev.mayutama.smartbook.model.entity.Permission;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.model.entity.User;
import dev.mayutama.smartbook.model.entity.UserCredential;
import dev.mayutama.smartbook.repository.PermissionRepository;
import dev.mayutama.smartbook.repository.RoleRepository;
import dev.mayutama.smartbook.repository.UserCredentialRepository;
import dev.mayutama.smartbook.service.InitializerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InitializerServiceImpl implements InitializerService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void initData() {
        // Simpan semua permission dan simpan hasil save-nya
        List<String> permissionNames = List.of("PERMIT_ALL", "READ_ALL", "UPDATE_ALL", "DELETE_ALL", "CREATE_ALL", "ADD_REVIEW");

        for (String name : permissionNames) {
            Permission permission = permissionRepository.findByName(name)
                    .orElseGet(() -> permissionRepository.saveAndFlush(Permission.builder().name(name).build()));
        }

        // Simpan ROLE_ADMIN jika belum ada
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Set<Permission> permissionAdmin = permissionRepository.findAllByNameIn(Set.of("PERMIT_ALL"));
            roleRepository.saveAndFlush(Role.builder()
                    .name("ROLE_ADMIN")
                    .permissions(permissionAdmin)
                    .build());
        }

        // Simpan ROLE_USER jika belum ada
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Set<Permission> permissionUser = permissionRepository.findAllByNameIn(Set.of("READ_ALL", "UPDATE_ALL", "DELETE_ALL", "CREATE_ALL", "ADD_REVIEW"));
            roleRepository.saveAndFlush(Role.builder()
                    .name("ROLE_USER")
                    .permissions(permissionUser)
                    .build());
        }

        if (userCredentialRepository.findByEmailAndIsActive("superadmin@mail.com", true).isEmpty()) {
            Optional<Role> roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            if (roleAdmin.isPresent()) {
                Set<Role> roles = Set.of(roleAdmin.get());
                User user = User.builder()
                        .fullName("Superadmin")
                        .gender(EGender.MALE)
                        .maritalStatus(EMaritalStatus.SINGLE)
                        .build();
                UserCredential userCredential = UserCredential.builder()
                        .email("superadmin@mail.com")
                        .password(passwordEncoder.encode("superadmin"))
                        .roles(roles)
                        .build();
                userCredential.setUser(user);
                user.setUserCredential(userCredential);
                userCredentialRepository.save(userCredential);
            }
        }
    }
}
