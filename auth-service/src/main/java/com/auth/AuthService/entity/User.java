package com.user.UserService.entity;

import com.user.UserService.enums.Gender;
import com.user.UserService.enums.KycStatus;
import com.user.UserService.enums.Role;
import com.user.UserService.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * User entity — implements UserDetails so Spring Security can use it directly.
 * <p>
 * KEY DESIGN DECISIONS:
 * 1. UUID primary keys — no sequential IDs exposed to clients (prevents enumeration attacks)
 * 2. Sensitive fields (PAN, Aadhaar) are encrypted at service layer before save
 * 3. Implements UserDetails — avoids a separate UserDetailsService wrapper class
 * 4. @Column(updatable=false) on createdAt — immutable audit field
 */


@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID uuid;

    @Column(name = "first_name", nullable = false, length = 30)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "phone_number",nullable = false, length = 15, unique = true)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING) // @Enumerated tells JPA/Hibernate:   How should this Java enum be stored in the database?
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "nationality", length = 50)
    private String nationality;

    // KYC — stored ENCRYPTED in DB. Encryption/decryption in EncryptionUtil
    @Column(name = "pan_number", nullable = false , length = 12)
    private String panNumber;

    @Column(name = "aadhar_number", nullable = false, length = 500, unique = true) // longer due to encryption
    private String aadharNumber;

    @Column(name = "kyc_status" , nullable = false , columnDefinition = "kyc_status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private KycStatus kycStatus= KycStatus.PENDING;

    @Column(name = "kyc_verified_at")
    private Timestamp kycVarifiedAt;

    @Builder.Default
    @Column(name = "two_factor_enabled")
    private boolean twoFactorEnabled = false;

    @Column(name = "two_factor_secret", length = 500)  // encrypted TOTP secret
    private boolean towFactorSecrete;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "user_role", nullable = false )
    @Builder.Default
    private Role role = Role.CUSTOMER ;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "user_status", nullable = false )
    @Builder.Default
    private UserStatus getKycStatus = UserStatus.ACTIVE;

    @Builder.Default
    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempt = 0;

    @Column(name = "locked_untill")
    private LocalDateTime lockedUntill;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "last_login_ip", length = 45)
    private String lastLoginIp;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    // Audit
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    private UUID createdBy;

    // ── Lifecycle hooks ───────────────────────────────────────────────────────
    @PrePersist
    protected void onCreate(){
        this.updatedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
