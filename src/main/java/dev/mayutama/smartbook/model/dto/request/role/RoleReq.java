package dev.mayutama.smartbook.model.dto.request.role;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class RoleReq {
    @NotNull(message = "Field name is required")
    private String name;

    @NotNull(message = "Field permissions must not be null")
    @Size(min = 1, message = "Field permissions must contain at least one item")
    private Set<String> permissions;
}
