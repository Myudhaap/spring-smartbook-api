package dev.mayutama.smartbook.model.dto.request.user;

import dev.mayutama.smartbook.constant.EGender;
import dev.mayutama.smartbook.constant.EMaritalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email pattern is invalid")
    private String email;
    @NotBlank(message = "FullName is required")
    private String fullName;
    @NotNull(message = "Gender is required")
    private EGender gender;
    private Long birthDate;
    @NotNull(message = "Marital Status is required")
    private EMaritalStatus maritalStatus;
    @NotNull(message = "Role is required")
    private String role;
}
