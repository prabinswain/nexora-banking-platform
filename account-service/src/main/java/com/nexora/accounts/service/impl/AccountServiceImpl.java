package com.nexora.accounts.service.impl;

import com.nexora.accounts.dto.request.OpenAccountRequest;
import com.nexora.accounts.dto.response.OpenAccountResponse;
import com.nexora.accounts.entity.Branch;
import com.nexora.accounts.exception.BranchNotActiveException;
import com.nexora.accounts.exception.BranchNotFoundException;
import com.nexora.accounts.repository.AccountRepository;
import com.nexora.accounts.repository.BranchRepository;
import com.nexora.accounts.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ═══════════════════════════════════════════════════════════════════════
 * AccountService — Core banking account management
 * ═══════════════════════════════════════════════════════════════════════
 *
 * WHAT THIS SERVICE DOES:
 * ┌─────────────────────────────────────────────────────────────────┐
 * │  1. openAccount()        → Create SAVINGS/CURRENT/FD/RD/SALARY │
 * │  2. getBalance()         → Redis cache first, DB fallback       │
 * │  3. debitAccount()       → Distributed lock + balance deduction │
 * │  4. creditAccount()      → Credit balance + release holds       │
 * │  5. freezeAccount()      → Admin: stop all transactions         │
 * │  6. applyInterest()      → Monthly interest credit (scheduler)  │
 * │  7. checkDormancy()      → Weekly: mark inactive accounts       │
 * │  8. updateLimits()       → Update daily/monthly txn limits      │
 * │  9. closeAccount()       → Initiate account closure             │
 * │ 10. getMiniStatement()   → Last 5 transactions summary          │
 * └─────────────────────────────────────────────────────────────────┘
 *
 * HOW BALANCE WORKS:
 * ┌──────────────────────────────────────────────────────────────┐
 * │  balance          = total money                              │
 * │  lockedBalance    = money on hold (pending txn / dispute)    │
 * │  availableBalance = balance - lockedBalance                  │
 * │                                                              │
 * │  When user debits → availableBalance checked, not balance    │
 * │  When funds locked → availableBalance ↓, lockedBalance ↑    │
 * │  When txn completes → balance ↓, lockedBalance ↓            │
 * └──────────────────────────────────────────────────────────────┘
 *
 * DISTRIBUTED LOCKING STRATEGY:
 * ┌──────────────────────────────────────────────────────────────┐
 * │  Redis lock key: lock_transfer:{accountNumber}               │
 * │  TTL: 10 seconds (from blueprint)                            │
 * │  Why Redis not DB lock?                                      │
 * │    → DB @Lock works but ties up DB connection               │
 * │    → Redis lock is faster and doesn't block DB pool         │
 * │    → Multiple service instances compete for same Redis lock  │
 * └──────────────────────────────────────────────────────────────┘
 */

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {



    private final AccountRepository accountRepository;
    private final BranchRepository branchRepository;
//    private final DailyTxnTotalRepository    dailyTxnTotalRepository;
//    private final MonthlyTxnTotalRepository  monthlyTxnTotalRepository;
//    private final AccountNumberGenerator     accountNumberGenerator;
//    private final AccountEventPublisher      eventPublisher;
//    private final RedisTemplate<String, String> redisTemplate;
//    private final UserServiceClient          userServiceClient;


    // ═══════════════════════════════════════════════════════════════════════
    // 1. OPEN ACCOUNT
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Opens a new bank account for the authenticated user.
     *
     * STEPS:
     *  Step 1 → Verify user exists and KYC is VERIFIED (call user-service)
     *  Step 2 → Set defaults based on account type (interest, min balance, limits)
     *  Step 3 → Generate unique account number
     *  Step 4 → Save to DB
     *  Step 5 → Publish ACCOUNT_OPENED event to Kafka
     *
     * WHY KYC CHECK?
     *   RBI mandates KYC before opening any bank account.
     *   We check with user-service via Feign client.
     */
    @Override
    @Transactional
    public OpenAccountResponse openAccount(OpenAccountRequest request) {

        // Step 1: KYC verification check
        // Need to check by calling UserService by UserServiceClient

        // Step 2: Resolve branch — user picked branchId from dropdown,
        //         we look up IFSC internally. User never types IFSC.
        //
        // TWO separate checks — different errors for different problems:
        //   Branch UUID not in DB at all → BranchNotFoundException  (404)
        //   Branch exists but inactive   → BranchNotActiveException (422)
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new BranchNotFoundException(
                        "Branch not found: " + request.getBranchId()
                                + ". Please select a branch from the available list."
                ));

        if (!branch.isActive()) {
            throw new BranchNotActiveException(request.getBranchId().toString());
        }

        // Step 3: Set type-specific defaults


        // Step 4: Build the account entity

        // Step 4: Publish event → Audit Service + Notification Service consume this


        // Return ONLY what the frontend needs on creation confirmation


        return null;
    }
}
