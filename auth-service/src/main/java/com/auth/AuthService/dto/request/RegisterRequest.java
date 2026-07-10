package com.auth.AuthService.dto.request;


import com.auth.AuthService.entity.User;
import com.auth.AuthService.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Registration request DTO.
 *
 * WHY DTOs SEPARATE FROM ENTITIES:
 * - Never expose your entity fields directly (security risk, coupling)
 * - Validation lives here, not on the entity
 * - API contract is independent of DB schema
 */


@Data
public class RegisterRequest {

    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be 8-100 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain uppercase, lowercase, digit and special character"
    )
    private String password;


    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{9,14}$", message = "Invalid phone number with country code")
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Aadhar nu. is required")
    @Size(min = 11,max = 11, message = "Please enter a valid aadhar number")
    private String aadharNumber;

    @NotBlank(message = "Pan nu. is required")
    @Size(min = 10,max = 11, message = "Please enter a valid Pan number")
    private String panNumber;

    private Gender gender;
    private String nationality;

}
