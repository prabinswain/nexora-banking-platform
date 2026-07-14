package com.nexora.accounts.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler for Account Service.
 *
 * Every exception → ApiResponse shape.
 * Frontend always checks response.success — never parses different shapes.
 *
 * ERROR CODE REFERENCE (for frontend developers):
 * ┌────────────────────────────┬──────┬────────────────────────────────────────┐
 * │ errorCode                  │ HTTP │ When                                   │
 * ├────────────────────────────┼──────┼────────────────────────────────────────┤
 * │ ACCOUNT_NOT_FOUND          │ 404  │ Account number doesn't exist           │
 * │ ACCOUNT_FROZEN             │ 422  │ Account is frozen, debit blocked        │
 * │ ACCOUNT_CLOSED             │ 422  │ Account is closed                       │
 * │ INSUFFICIENT_FUNDS         │ 422  │ Not enough available balance            │
 * │ DAILY_LIMIT_EXCEEDED       │ 422  │ Daily txn limit would be breached       │
 * │ MONTHLY_LIMIT_EXCEEDED     │ 422  │ Monthly txn limit would be breached     │
 * │ KYC_NOT_VERIFIED           │ 403  │ KYC must be VERIFIED to open account    │
 * │ BRANCH_NOT_FOUND           │ 404  │ branchId doesn't exist in DB            │
 * │ BRANCH_NOT_ACTIVE          │ 422  │ Branch exists but is marked inactive    │
 * │ VALIDATION_ERROR           │ 400  │ Request field validation failed         │
 * │ FORBIDDEN                  │ 403  │ User doesn't own this account           │
 * │ LOCK_UNAVAILABLE           │ 409  │ Account busy — concurrent transaction   │
 * │ INTERNAL_ERROR             │ 500  │ Unexpected server error                 │
 * └────────────────────────────┴──────┴────────────────────────────────────────┘
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
}
