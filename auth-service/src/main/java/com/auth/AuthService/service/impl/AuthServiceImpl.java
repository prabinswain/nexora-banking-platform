package com.auth.AuthService.service.impl;

import com.auth.AuthService.builder.UserBuilder;
import com.auth.AuthService.dto.request.RegisterRequest;
import com.auth.AuthService.dto.response.AuthResponse;
import com.auth.AuthService.entity.User;
import com.auth.AuthService.exception.DuplicateUserException;
import com.auth.AuthService.repository.UserRepository;
import com.auth.AuthService.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * AuthService — handles all authentication flows.
 * <p>
 * FLOWS IMPLEMENTED:
 * 1. register()       → validate → check duplicate → hash pw → save → publish event → send OTP
 * 2. login()          → find user → check lock → verify pw → check 2FA → issue tokens
 * 3. refreshToken()   → validate refresh token from Redis → rotate tokens
 * 4. logout()         → blacklist access token → delete refresh token from Redis
 * 5. setup2FA()       → generate TOTP secret → return QR URI
 * 6. verify2FA()      → verify TOTP code → enable 2FA → encrypt secret in DB
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserBuilder userBuilder;
//    private final JwtUtil jwtUtil;
//    private final RedisService redisService;
//    private final OtpService otpService;
//    private final UserEventPublisher eventPublisher;
//    private final EncryptionUtil encryptionUtil;
//    private final SecretGenerator totpSecretGenerator;
//    private final CodeVerifier totpCodeVerifier;

    /**
     * Register a new customer.
     *
     * @Transactional: if anything fails after save (e.g. event publish fails?),
     * the DB save is NOT rolled back because event publish is non-transactional.
     * This is intentional — save first, handle event failures separately.
     * 1. register()       → validate → check duplicate → hash pw → save → publish event → send OTP
     */

    @Override
    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {

        // validate
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email already registered: " + request.getEmail());
        }
        // duplicate check
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateUserException("Email already registered: " + request.getEmail());
        }
        // 2. Build user entity — password hashed with BCrypt
        User user = userBuilder.buildUserEntity(request);
        user = userRepository.save(user);
        log.info("New user registered: userId={}", user.getId());

        // 3. Send email verification OTP (async via Kafka → Notification Service)

        // 4. Publish event — Audit and Notification services consume this

        // 5. Return tokens — user is logged in immediately after registration
        return buildAuthResponse(user);
    }




    // private methods

    private AuthResponse buildAuthResponse(User user) {
//        String accessToken = jwtUtil.generateAccessToken(
//                user.getId(), user.getEmail(), user.getRole().name()
//        );
//        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getEmail());
//
//        // Store refresh token in Redis
//        redisService.storeRefreshToken(
//                user.getId(), refreshToken, jwtUtil.getRefreshTokenExpiryMs()
//        );

        return AuthResponse.builder()
//                .accessToken(accessToken)
//                .refreshToken(refreshToken)
                .tokenType("Bearer")
//                .expiresIn(jwtUtil.getAccessTokenExpiryMs() / 1000)
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .twoFactorRequired(false)
                .build();
    }


}
