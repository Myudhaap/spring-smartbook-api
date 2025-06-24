package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.constant.CustomHeader;
import dev.mayutama.smartbook.constant.ETypeDevice;
import dev.mayutama.smartbook.model.dto.request.auth.AuthReq;
import dev.mayutama.smartbook.model.dto.request.auth.LoginReq;
import dev.mayutama.smartbook.model.dto.response.auth.AuthRes;
import dev.mayutama.smartbook.model.dto.response.auth.LoginRes;
import dev.mayutama.smartbook.service.AuthService;
import dev.mayutama.smartbook.util.CookieUtil;
import dev.mayutama.smartbook.util.ResponseUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppPath.AUTH)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody AuthReq req
    ) {
        AuthRes res = authService.register(req);
        return ResponseUtil.responseSuccess(res, "Success registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginReq req,
            @RequestHeader(value = CustomHeader.X_CLIENT_TYPE, required = false, defaultValue = ETypeDevice.DEFAULT) String clientType,
            HttpServletResponse response
    ) {
        LoginRes res = authService.login(req);
        if (clientType.equals(ETypeDevice.WEB.getValue())) {
            CookieUtil.setAuthCookie(response, res.getAccessToken(), res.getRefreshToken());
            res.setAccessToken(null);
            res.setRefreshToken(null);
            return ResponseUtil.responseSuccess(res, "Login successfully");
        } else {
            return ResponseUtil.responseSuccess(res, "Login successfully");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            HttpServletResponse response
    ) {
        CookieUtil.clearAuthCookie(response, true, true);
        return ResponseUtil.responseSuccess(null, "Successfully logout");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            HttpServletResponse response,
            @RequestHeader(value = CustomHeader.X_CLIENT_TYPE, required = false, defaultValue = ETypeDevice.DEFAULT) String clientType,
            @RequestHeader(value = CustomHeader.X_REFRESH_TOKEN, required = false) String refreshTokenFromHeader,
            @CookieValue(value = "refresh_token", required = false) String refreshTokenFromCookie
    ) {
        String refreshToken = refreshTokenFromHeader != null ? refreshTokenFromHeader : refreshTokenFromCookie;
        LoginRes res = authService.refreshToken(refreshToken, response);

        if (clientType.equals(ETypeDevice.WEB.getValue())) {
            CookieUtil.setAuthCookie(response, res.getAccessToken(), res.getRefreshToken());
            return ResponseUtil.responseSuccess(null, "Refresh successfully");
        } else {
            return ResponseUtil.responseSuccess(res, "Refresh successfully");
        }
    }
}
