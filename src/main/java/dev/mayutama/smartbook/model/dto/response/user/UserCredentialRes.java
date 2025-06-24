package dev.mayutama.smartbook.model.dto.response.user;

import dev.mayutama.smartbook.common.response.BaseResponse;
import dev.mayutama.smartbook.model.entity.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialRes extends BaseResponse {
    private String id;
    private Set<Role> roles;
    private String email;
}
