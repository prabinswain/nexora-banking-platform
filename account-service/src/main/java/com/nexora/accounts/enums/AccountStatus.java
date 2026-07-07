package com.nexora.accounts.enums;

public enum AccountStatus {
    ACTIVE,     // Normal operation
    DORMANT,    // No transaction for 12+ months
    FROZEN,     // Temporarily locked by admin/legal
    CLOSED      // Permanently closed
}
