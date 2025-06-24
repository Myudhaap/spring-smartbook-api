package dev.mayutama.smartbook.model.dto.response.user;

import dev.mayutama.smartbook.common.response.BaseResponse;
import dev.mayutama.smartbook.constant.EGender;
import dev.mayutama.smartbook.constant.EMaritalStatus;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends BaseResponse {
    private String id;
    private String email;
    private String fullName;
    private EGender gender;
    private Long birthDate;
    private EMaritalStatus maritalStatus;
    private Boolean isActive;
    private Set<RoleRes> roles;
}
