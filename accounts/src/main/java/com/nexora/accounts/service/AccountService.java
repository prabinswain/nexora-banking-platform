package com.nexora.accounts.service;

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


public interface AccountService {


}
