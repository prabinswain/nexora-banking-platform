package com.nexora.accounts.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.nexora.accounts.enums.AccountStatus;
import com.nexora.accounts.enums.AccountType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Accounts Entity — every field from the blueprint.
 *
 * KEY CONCEPTS:
 * - balance vs availableBalance vs lockedBalance
 *   balance          = total money in account
 *   lockedBalance    = money on hold (pending txn, dispute, legal freeze)
 *   availableBalance = balance - lockedBalance  ← what user can actually spend
 *
 * - overdraftLimit: allowed to go negative up to this amount (CURRENT accounts mainly)
 * - minimumBalance: if balance falls below this → penalty charged
 * - dailyTxnLimit / monthlyTxnLimit: enforced by TransactionService before every debit
 */

@Entity
@Table(name = "accounts")
@Getter  @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    // Generated account number: IFSC + timestamp + random (e.g. BANK0001234567)
    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    // Owner — foreign key to user-service (cross-service, just UUID stored here)
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(length = 3, nullable = false)
    @Builder.Default
    private String currency = "INR";   // ISO 4217

    // ── Balance fields — DECIMAL(18,2) for financial precision ────────────────
    @Column(nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "available_balance", nullable = false, precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @Column(name = "locked_balance", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal lockedBalance = BigDecimal.ZERO;

    @Column(name = "minimum_balance", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal minimumBalance = BigDecimal.ZERO;

    // Annual interest rate e.g. 0.0350 = 3.5%
    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "overdraft_limit", precision = 18, scale = 2)
    @Builder.Default
    private BigDecimal overdraftLimit = BigDecimal.ZERO;

    @Column(name = "daily_txn_limit", precision = 18, scale = 2)
    private BigDecimal dailyTxnLimit;

    @Column(name = "monthly_txn_limit", precision = 18, scale = 2)
    private BigDecimal monthlyTxnLimit;

    // ── Branch info ───────────────────────────────────────────────────────────
    @Column(name = "ifsc_code", nullable = false, length = 11)
    private String ifscCode;

    @Column(name = "branch_id")
    private UUID branchId;

    // ── Status ────────────────────────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "dormancy_date")
    private LocalDate dormancyDate;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    // ── Nominee ───────────────────────────────────────────────────────────────
    @Column(name = "nominee_name", length = 100)
    private String nomineeName;

    @Column(name = "nominee_relation", length = 50)
    private String nomineeRelation;

    // ── Audit ─────────────────────────────────────────────────────────────────
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        // Ensure availableBalance = balance on creation
        this.availableBalance = this.balance;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // -- Business methods ------------------------------------------------------------------
    /**
     * Debit: reduce balance and availableBalance.
     * Called ONLY after distributed lock is acquired in AccountService.
     * Saves balanceBefore snapshot before calling this.
     */
    public void debit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        BigDecimal effectiveAvailable = availableBalance.add(overdraftLimit);
        if (amount.compareTo(effectiveAvailable) > 0) {
            throw new IllegalStateException(
                    "Insufficient funds. Available: " + availableBalance + ", Overdraft: " + overdraftLimit
            );
        }
        this.balance = this.balance.subtract(amount);
        this.availableBalance = this.availableBalance.subtract(amount);
    }

    /**
     * Credit: increase balance and availableBalance.
     */
    public void credit(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        this.balance = this.balance.add(amount);
        this.availableBalance = this.availableBalance.add(amount);
    }

    /**
     * Lock funds: move from availableBalance to lockedBalance.
     * Used for: pending transactions, disputes, legal holds.
     */
    public void lockFunds(BigDecimal amount) {
        if (amount.compareTo(availableBalance) > 0) {
            throw new IllegalStateException("Cannot lock more than available balance");
        }
        this.availableBalance = this.availableBalance.subtract(amount);
        this.lockedBalance = this.lockedBalance.add(amount);
    }

    /**
     * Release locked funds back to available.
     */
    public void releaseLock(BigDecimal amount) {
        this.lockedBalance = this.lockedBalance.subtract(amount);
        this.availableBalance = this.availableBalance.add(amount);
    }

    public boolean isActive() { return status == AccountStatus.ACTIVE; }
    public boolean isFrozen() { return status == AccountStatus.FROZEN; }
}
