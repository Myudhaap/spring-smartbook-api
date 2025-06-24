package dev.mayutama.smartbook.model.dto.response.user;

import dev.mayutama.smartbook.common.response.BaseResponse;
import dev.mayutama.smartbook.constant.EGender;
import dev.mayutama.smartbook.constant.EMaritalStatus;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponse extends BaseResponse {
    private String id;
    private String fullName;
    private EGender gender;
    private Long birthDate;
    private EMaritalStatus maritalStatus;
    private Boolean isActive;
}
