package dev.mayutama.smartbook.model.dto.response.role;

import dev.mayutama.smartbook.common.response.BaseResponse;
import dev.mayutama.smartbook.model.entity.Permission;
import lombok.*;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RoleRes extends BaseResponse {
    private String id;
    private String name;
    private Set<Permission> permissions;
}
