package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.model.dto.request.auth.AuthRequest;
import dev.mayutama.smartbook.model.dto.request.auth.LoginRequest;
import dev.mayutama.smartbook.model.dto.response.auth.AuthResponse;
import dev.mayutama.smartbook.model.dto.response.auth.LoginResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse register(AuthRequest request);
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(String refreshToken, HttpServletResponse response);
}
