package dev.mayutama.smartbook.model.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginReq {
    @NotBlank(message = "Field email is required")
    @Email(message = "Field email pattern is invalid")
    private String email;
    @NotBlank(message = "Field password is required")
    private String password;
}
