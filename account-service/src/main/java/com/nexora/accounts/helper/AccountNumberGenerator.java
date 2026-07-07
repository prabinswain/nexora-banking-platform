package com.nexora.accounts.helper;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Account number generator.
 *
 * FORMAT: BANK + YYYYMM + 6-digit-random = 16 chars total
 * Example: BANK2501847392
 *
 * WHY NOT sequential IDs?
 * - Sequential IDs expose account count (security risk)
 * - Random suffix prevents enumeration attacks
 * - Prefix helps tellers identify the bank
 *
 * In production: use a DB sequence + checksum digit (like credit cards use Luhn algorithm)
 */
@Component
public class AccountNumberGenerator {

    private static final String BANK_CODE = "BANK";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyMM");

    public String generate() {
        String datePart = LocalDateTime.now().format(DATE_FMT);
        String randomPart = String.format("%06d",
                ThreadLocalRandom.current().nextInt(100000, 999999));
        return BANK_CODE + datePart + randomPart;
    }
}