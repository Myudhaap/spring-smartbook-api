package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.request.permission.PermissionReq;
import dev.mayutama.smartbook.model.dto.response.permission.PermissionRes;
import dev.mayutama.smartbook.model.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {
    public Permission toEntity(PermissionReq req) {
        return Permission.builder()
                .name(req.getName())
                .build();
    }

    public PermissionRes toRes(Permission permission) {
        return PermissionRes.builder()
                .id(permission.getId())
                .name(permission.getName())
                .createdBy(permission.getCreatedBy())
                .createdAt(permission.getCreatedAt())
                .updatedBy(permission.getUpdatedBy())
                .updatedAt(permission.getUpdatedAt())
                .build();
    }
}
