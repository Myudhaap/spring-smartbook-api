package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.request.auth.AuthReq;
import dev.mayutama.smartbook.model.dto.request.auth.LoginReq;
import dev.mayutama.smartbook.model.dto.response.auth.AuthRes;
import dev.mayutama.smartbook.model.dto.response.auth.LoginRes;
import dev.mayutama.smartbook.model.entity.AppUser;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.model.entity.User;
import dev.mayutama.smartbook.model.entity.UserCredential;
import dev.mayutama.smartbook.model.mapper.UserCredentialMapper;
import dev.mayutama.smartbook.repository.UserCredentialRepository;
import dev.mayutama.smartbook.security.JwtUtil;
import dev.mayutama.smartbook.service.AuthService;
import dev.mayutama.smartbook.service.RoleService;
import dev.mayutama.smartbook.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final RoleService roleService;
    private final UserCredentialRepository userCredentialRepository;
    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    private final UserCredentialMapper userCredentialMapper;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public AuthRes register(AuthReq request) {
        Role role = roleService.finByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);


        User user = User.builder()
                .fullName(request.getFullName())
                .gender(request.getGender())
                .birthDate(request.getBirthDate())
                .maritalStatus(request.getMaritalStatus())
                .build();

        UserCredential userCredential = userCredentialMapper.toEntity(request, roles);

        registerFromAdmin(user, userCredential);

        return AuthRes.builder()
                .email(request.getEmail())
                .build();
    }

    public UserCredential registerFromAdmin(User user, UserCredential userCredential){
        userCredential.setUser(user);
        user.setUserCredential(userCredential);
        return userCredentialRepository.save(userCredential);
    }

    @Override
    public LoginRes login(LoginReq request) {
        try {
            System.out.println("TES");
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("TES login");

            AppUser user = (AppUser) authentication.getPrincipal();
            System.out.println(user);
            String token = jwtUtil.generateToken(user);
            System.out.println(token);
            String refreshToken = jwtUtil.generateRefreshToken(user);
            System.out.println(refreshToken);

            return LoginRes.builder()
                    .email(user.getEmail())
                    .accessToken(token)
                    .refreshToken(refreshToken)
                    .build();
        } catch (BadCredentialsException e) {
            throw new ApplicationException("Invalid credentials", "Email or password incorrect", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public LoginRes refreshToken(String refreshToken, HttpServletResponse response) {
        if (StringUtils.isBlank(refreshToken))
            throw new ApplicationException(
                    HttpStatus.NOT_FOUND.name(),
                    "Refresh token not found",
                    HttpStatus.NOT_FOUND
            );

        Boolean isRefreshTokenValid = jwtUtil.verifyJwtRefreshToken(refreshToken, response);
        if (!isRefreshTokenValid) {
            CookieUtil.clearAuthCookie(response, true, true);
            throw new ApplicationException(
                    HttpStatus.BAD_REQUEST.name(),
                    "Refresh Token already expired. Please login again",
                    HttpStatus.BAD_REQUEST
            );
        }

        Map<String, String> userInfo = jwtUtil.getUserInfoByToken(refreshToken);
        UserCredential userCredential = userCredentialRepository.findById(userInfo.get("userId"))
                .orElseThrow(() -> new ApplicationException(
                        HttpStatus.NOT_FOUND.name(),
                        "User not found",
                        HttpStatus.NOT_FOUND
                ));

        String token = jwtUtil.generateToken(AppUser.builder()
                .id(userCredential.getId())
                .email(userCredential.getEmail())
                .password(userCredential.getPassword())
                .fullName(userInfo.get("fullName"))
                .roles(userCredential.getRoles())
                .build()
        );

        return LoginRes.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .email(userCredential.getEmail())
                .build();
    }
}
