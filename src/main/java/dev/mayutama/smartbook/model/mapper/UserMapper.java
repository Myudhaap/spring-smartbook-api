package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.request.user.UserReq;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import dev.mayutama.smartbook.model.dto.response.user.UserRes;
import dev.mayutama.smartbook.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleMapper roleMapper;
    public UserRes toRes(User user) {
        Set<RoleRes> roleRes = user.getUserCredential().getRoles().stream()
                .map(roleMapper::toRes)
                .collect(Collectors.toSet());

        return UserRes.builder()
                .id(user.getId())
                .email(user.getUserCredential().getEmail())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .maritalStatus(user.getMaritalStatus())
                .roles(roleRes)
                .createdBy(user.getCreatedBy())
                .createdAt(user.getCreatedAt())
                .updatedBy(user.getUpdatedBy())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public User toEntity(UserReq req){
        return User.builder()
                .fullName(req.getFullName())
                .gender(req.getGender())
                .birthDate(req.getBirthDate())
                .maritalStatus(req.getMaritalStatus())
                .build();
    }
}
