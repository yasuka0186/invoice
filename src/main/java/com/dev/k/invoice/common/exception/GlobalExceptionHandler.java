package com.dev.k.invoice.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dev.k.invoice.common.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {

        ApiResponse<Void> response = new ApiResponse<>(
                e.getErrorCode().name(),
                e.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(ResourceNotFoundException e) {

        ApiResponse<Void> response = new ApiResponse<>(
                "NOT_FOUND",
                e.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException e
    ) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Validation error.");

        ApiResponse<Void> response = new ApiResponse<>(
                "VALIDATION_ERROR",
                message,
                null
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {

        ApiResponse<Void> response = new ApiResponse<>(
                "INTERNAL_ERROR",
                "Unexpected error occurred.",
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}