package com.auth.AuthService.service;

import com.auth.AuthService.dto.request.RegisterRequest;
import com.auth.AuthService.dto.response.AuthResponse;
import jakarta.validation.Valid;

public interface AuthService {

    public AuthResponse registerUser(@Valid RegisterRequest request );
}
