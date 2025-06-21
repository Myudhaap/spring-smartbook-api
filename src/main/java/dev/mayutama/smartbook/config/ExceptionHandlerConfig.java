package dev.mayutama.smartbook.config;

import dev.mayutama.smartbook.common.response.CommonErrorResponse;
import dev.mayutama.smartbook.exception.ApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionHandlerConfig {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(
            ApplicationException applicationException,
            HttpServletRequest request
    ) {
        CommonErrorResponse errorResponse = CommonErrorResponse.builder()
                .errorCode(applicationException.getErrorCode())
                .message(applicationException.getMessage())
                .statusName(applicationException.getHttpStatus().name())
                .statusCode(applicationException.getHttpStatus().value())
                .path(request.getRequestURI())
                .method(request.getMethod())
                .build();

        return new ResponseEntity<>(errorResponse, applicationException.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest req
    ) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        CommonErrorResponse errorResponse = CommonErrorResponse.builder()
                .errorCode(HttpStatus.BAD_REQUEST.name())
                .message(errors)
                .statusName(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .path(req.getRequestURI())
                .method(req.getMethod())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            HttpServletRequest req
    ) {
        Throwable rootCause = exception.getRootCause();
        String message = rootCause != null ? rootCause.getMessage() : exception.getMessage();

        CommonErrorResponse errorResponse = CommonErrorResponse.builder()
                .errorCode("-")
                .message("Duplicate or Invalid Data: " + message)
                .statusName(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .path(req.getRequestURI())
                .method(req.getMethod())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUniversalException(
            Exception exception,
            HttpServletRequest req
    ) {
        CommonErrorResponse errorResponse = CommonErrorResponse.builder()
                .errorCode("-")
                .message(exception.getMessage())
                .statusName(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(req.getRequestURI())
                .method(req.getMethod())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
