package dev.mayutama.smartbook.model.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {
    private String email;
    private String accessToken;
    private String refreshToken;
}
