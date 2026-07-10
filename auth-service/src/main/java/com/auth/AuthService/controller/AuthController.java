package com.auth.AuthService.controller;

import com.auth.AuthService.AuthServiceApplication;
import com.auth.AuthService.dto.request.LoginRequest;
import com.auth.AuthService.dto.request.RegisterRequest;
import com.auth.AuthService.dto.response.AuthResponse;
import com.auth.AuthService.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.nexora.common.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * AuthController — all auth endpoints return ApiResponse<T>.
 * <p>
 * EVERY method follows this exact pattern:
 * 1. Call service method
 * 2. Wrap result in ApiResponse.success() or ApiResponse.created()
 * 3. Return ResponseEntity with correct HTTP status
 * <p>
 * Frontend gets this shape ALWAYS — success or error, same wrapper.
 */


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register, Login, 2FA, token management")
public class AuthController {


    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {

        AuthResponse data = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(data, "Registration successful. Please verify your email."));
    }

//    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request){
//
//
//    }



}
