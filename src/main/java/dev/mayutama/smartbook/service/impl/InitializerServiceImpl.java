package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.model.entity.Permission;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.repository.PermissionRepository;
import dev.mayutama.smartbook.repository.RoleRepository;
import dev.mayutama.smartbook.service.InitializerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class InitializerServiceImpl implements InitializerService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

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
    }
}
