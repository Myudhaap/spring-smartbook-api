package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.model.dto.request.auth.AuthReq;
import dev.mayutama.smartbook.model.dto.request.auth.LoginReq;
import dev.mayutama.smartbook.model.dto.response.auth.AuthRes;
import dev.mayutama.smartbook.model.dto.response.auth.LoginRes;
import dev.mayutama.smartbook.model.entity.User;
import dev.mayutama.smartbook.model.entity.UserCredential;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthRes register(AuthReq request);
    UserCredential registerFromAdmin(User user, UserCredential userCredential);
    LoginRes login(LoginReq request);
    LoginRes refreshToken(String refreshToken, HttpServletResponse response);
}
