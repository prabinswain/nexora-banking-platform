package com.nexora.accounts.dto.response;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class BalanceResponse {
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private BigDecimal lockedBalance;
    private LocalDateTime asOf;   // timestamp of when balance was read
    private String source;        // "CACHE" or "DATABASE"
}