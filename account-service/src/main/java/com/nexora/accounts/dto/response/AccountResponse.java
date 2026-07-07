package com.nexora.accounts.dto.response;

import com.nexora.accounts.enums.AccountStatus;
import com.nexora.accounts.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AccountResponse {
    private UUID id;
    private String accountNumber;
    private UUID userId;
    private AccountType accountType;
    private String currency;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private BigDecimal minimumBalance;
    private BigDecimal interestRate;
    private BigDecimal overdraftLimit;
    private BigDecimal dailyTxnLimit;
    private BigDecimal monthlyTxnLimit;
    private String ifscCode;
    private AccountStatus status;
    private String nomineeName;
    private String nomineeRelation;
    private LocalDateTime createdAt;
}