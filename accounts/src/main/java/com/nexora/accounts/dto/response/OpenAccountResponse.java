package com.nexora.accounts.dto.response;

import com.nexora.accounts.enums.AccountStatus;
import com.nexora.accounts.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Returned ONLY when a new account is opened.
 *
 * RULE: Only send what the frontend needs RIGHT NOW for that action.
 *
 * After opening an account, the user needs to know:
 *  ✅ accountNumber  → they'll use this everywhere
 *  ✅ accountType    → confirmation of what they opened
 *  ✅ ifscCode       → needed for receiving transfers
 *  ✅ currency       → confirmation
 *  ✅ status         → should be ACTIVE
 *  ✅ message        → human-readable confirmation
 *  ✅ createdAt      → for their records
 *
 * They do NOT need on creation:
 *  ❌ balance        → it's 0.00, pointless
 *  ❌ availableBalance, lockedBalance → same, all zeros
 *  ❌ interestRate   → internal config
 *  ❌ overdraftLimit → internal config
 *  ❌ minimumBalance → internal config
 *  ❌ dailyTxnLimit  → internal config
 *  ❌ monthlyTxnLimit → internal config
 *  ❌ userId         → they already know their own ID
 *  ❌ id (UUID)      → they'll use accountNumber, not internal UUID
 */

@Builder
@Data
public class OpenAccountResponse {

    private String               accountNumber;  // "BANK2501123456"
    private AccountType          accountType;    // SAVINGS / CURRENT / FD / RD / SALARY
    private String               ifscCode;       // "BANK0001234" — for receiving transfers
    private String               currency;       // "INR"
    private AccountStatus        status;        // ACTIVE
    private String               message;        // "Your SAVINGS account has been opened successfully"
    private LocalDateTime        createdAt;

}
