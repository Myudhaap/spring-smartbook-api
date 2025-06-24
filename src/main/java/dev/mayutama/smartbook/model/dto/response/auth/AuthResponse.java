package dev.mayutama.smartbook.model.dto.response.auth;

import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String email;
}
