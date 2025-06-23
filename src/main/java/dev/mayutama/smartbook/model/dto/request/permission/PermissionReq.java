package dev.mayutama.smartbook.model.dto.request.permission;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PermissionReq {
    @NotNull(message = "Field name is required!")
    private String name;
}
