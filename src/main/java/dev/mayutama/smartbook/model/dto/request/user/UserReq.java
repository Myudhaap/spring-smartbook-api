package dev.mayutama.smartbook.model.dto.request.user;

import dev.mayutama.smartbook.constant.EGender;
import dev.mayutama.smartbook.constant.EMaritalStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
public class UserReq {
    @NotBlank(message = "Field email is required")
    @Email(message = "Field email pattern is invalid")
    private String email;
    @NotBlank(message = "Field fullName is required")
    private String fullName;
    @NotNull(message = "Field gender is required")
    private EGender gender;
    private Long birthDate;
    @NotNull(message = "Field maritalStatus is required")
    private EMaritalStatus maritalStatus;
    @NotNull(message = "Field roles is required")
    @Size(min = 1, message = "Field roles must contain at least one item")
    private Set<String> roles;
    private String password;
}
