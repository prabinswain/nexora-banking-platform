package com.nexora.accounts.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

/**
 * Branch — master data table. Pre-populated by admin, never by user.
 *
 * User picks a branch from a dropdown.
 * System reads IFSC from this table — user never types it.
 */
@Entity
@Table(name = "branches")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 11, unique = true)
    private String ifscCode;       // "BANK0001234" — resolved internally

    @Column(nullable = false, length = 100)
    private String branchName;     // "Bhubaneswar Main Branch"

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 100)
    private String state;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false)
    private boolean active = true;
}