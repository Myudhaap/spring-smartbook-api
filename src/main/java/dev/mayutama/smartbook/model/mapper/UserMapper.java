package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import dev.mayutama.smartbook.model.dto.response.user.UserResponse;
import dev.mayutama.smartbook.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final RoleMapper roleMapper;
    public UserResponse toRes(User user) {
        Set<RoleRes> roleRes = user.getUserCredential().getRoles().stream()
                .map(roleMapper::toRes)
                .collect(Collectors.toSet());

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getUserCredential().getEmail())
                .fullName(user.getFullName())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .maritalStatus(user.getMaritalStatus())
                .roles(roleRes)
                .build();
    }
}
