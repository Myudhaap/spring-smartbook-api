package dev.mayutama.smartbook.model.dto.response.permission;

import dev.mayutama.smartbook.common.response.BaseResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class PermissionRes extends BaseResponse {
    private String id;
    private String name;
}
