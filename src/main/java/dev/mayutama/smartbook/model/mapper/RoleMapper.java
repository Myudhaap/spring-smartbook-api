package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.request.role.RoleReq;
import dev.mayutama.smartbook.model.dto.response.permission.PermissionRes;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import dev.mayutama.smartbook.model.entity.Permission;
import dev.mayutama.smartbook.model.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final PermissionMapper permissionMapper;

    public Role toEntity(RoleReq req, Set<Permission> permissions) {
        return Role.builder()
                .name(req.getName())
                .permissions(permissions)
                .build();
    }

    public RoleRes toRes(Role role) {
        Set<PermissionRes> permissionRes = role.getPermissions().stream()
                .map(permissionMapper::toRes)
                .collect(Collectors.toSet());

        return  RoleRes.builder()
                .id(role.getId())
                .name(role.getName())
                .permissions(permissionRes)
                .createdBy(role.getCreatedBy())
                .createdAt(role.getCreatedAt())
                .updatedBy(role.getUpdatedBy())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}
