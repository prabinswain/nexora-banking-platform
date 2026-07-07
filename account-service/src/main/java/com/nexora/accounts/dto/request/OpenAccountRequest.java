package com.nexora.accounts.dto.request;

import com.nexora.accounts.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;


/**
 * What the user actually fills in when opening an account.
 *
 * REMOVED: ifscCode  — user never types this. System resolves from branchId.
 * ADDED:   branchId  — user picks branch from dropdown in the app.
 *
 * Frontend flow:
 *   1. GET /branches?state=Odisha&city=Bhubaneswar → branch list shown as dropdown
 *   2. User picks one branch
 *   3. Frontend sends that branch UUID as branchId
 *   4. Backend resolves IFSC from Branch table — user never sees or types it
 */

@Data
public class OpenAccountRequest {


    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotNull(message = "Please select a branch")
    private UUID branchId;              // picked from dropdown, NOT typed manually

    private String currency = "INR";

    private String nomineeName;
    private String nomineeRelation;

    @DecimalMin(value = "0.0", message = "Initial deposit cannot be negative")
    private BigDecimal initialDeposit = BigDecimal.ZERO;

}
