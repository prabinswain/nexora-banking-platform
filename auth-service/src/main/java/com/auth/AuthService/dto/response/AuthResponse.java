package com.auth.AuthService.dto.response;

import com.auth.AuthService.enums.Role;
import lombok.Builder;
import lombok.Data;
import java.util.UUID;

/**
 * Auth response — returned on successful login.
 * Never include passwordHash or sensitive fields here.
 */
@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;       // Always "Bearer"
    private long expiresIn;         // seconds until access token expiry
    private UUID userId;
    private String email;
    private Role role;
    private boolean twoFactorRequired;  // true if 2FA not yet completed
}