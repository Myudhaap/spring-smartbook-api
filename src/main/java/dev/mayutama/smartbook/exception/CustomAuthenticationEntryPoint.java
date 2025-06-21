package dev.mayutama.smartbook.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mayutama.smartbook.common.response.CommonErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        CommonErrorResponse errorResponse = CommonErrorResponse.builder()
                .errorCode(HttpStatus.UNAUTHORIZED.name())
                .message(authException.getMessage())
                .statusName(HttpStatus.UNAUTHORIZED.name())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(json);
    }
}
