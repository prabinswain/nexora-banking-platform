package com.auth.AuthService.builder;

import com.auth.AuthService.dto.request.RegisterRequest;
import com.auth.AuthService.entity.User;
import com.auth.AuthService.enums.KycStatus;
import com.auth.AuthService.enums.Role;
import com.auth.AuthService.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBuilder {

    @Autowired
    private final PasswordEncoder passwordEncoder ;

    public User buildUserEntity(RegisterRequest request) {

        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail().toLowerCase().trim())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .nationality(request.getNationality())
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .kycStatus(KycStatus.PENDING)
                .twoFactorEnabled(false)
                .failedLoginAttempt(0)
                .aadharNumber(request.getAadharNumber())
                .panNumber(request.getPanNumber())
                .build();
    }
}
