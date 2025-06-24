package dev.mayutama.smartbook.model.dto.request.user;

import dev.mayutama.smartbook.constant.EGender;
import dev.mayutama.smartbook.constant.EMaritalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private EGender gender;
    private Long birthDate;
    private EMaritalStatus maritalStatus;
    private String role;
}
