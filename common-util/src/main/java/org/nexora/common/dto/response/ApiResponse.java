package org.nexora.common.dto.response;



/*

 * ApiResponse<T> — Universal response envelope.
 * EVERY endpoint in EVERY service returns this shape. No exceptions.
 * SUCCESS shape:
 * {
 *   "success": true,
 *   "status": 200,
 *   "message": "Account opened successfully",
 *   "data": { ...payload... },
 *   "traceId": "abc-123-def",
 *   "timestamp": "2025-01-01T10:00:00"
 * }
 *
 * ERROR shape:
 * {
 *   "success": false,
 *   "status": 400,
 *   "message": "Validation failed",
 *   "errorCode": "VALIDATION_ERROR",
 *   "errors": { "email": "Invalid format" },
 *   "traceId": "abc-123-def",
 *   "timestamp": "2025-01-01T10:00:00"
 * }
 *
 * WHY @JsonInclude(NON_NULL)?
 *   - On success: errorCode and errors are null → hidden from response
 *   - On error: data is null → hidden from response
 *   - Clean output, no "errorCode: null" noise
 *
 * WHY traceId?
 *   - Every request gets a unique ID (from MDC / Micrometer)
 *   - User reports issue → you search logs by traceId → find exact request instantly
 *   - Without this, debugging production issues is guesswork
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.nexora.common.util.TraceIdHolder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean       success;
    private int           status;
    private String        message;
    private T             data;          // null on error → hidden
    private String        errorCode;     // null on success → hidden
    private Object        errors;        // field-level errors (validation) → hidden on success
    private String        traceId;       // from MDC — for log correlation
    private LocalDateTime timestamp;

    // ── Static factory methods — clean API for callers ────────────────────────

    /**
     * 200 OK with data
     * Usage: return ApiResponse.success(userResponse, "Profile fetched");
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .traceId(TraceIdHolder.get())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 201 CREATED with data
     * Usage: return ApiResponse.created(accountResponse, "Account opened successfully");
     */
    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .traceId(TraceIdHolder.get())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * 200 OK with no data (void operations like freeze, logout)
     * Usage: return ApiResponse.success("Account frozen successfully");
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(HttpStatus.OK.value())
                .message(message)
                .traceId(TraceIdHolder.get())
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Error response with errorCode
     * Usage: return ApiResponse.error(400, "VALIDATION_ERROR", "Invalid input", errors);
     */
    public static <T> ApiResponse<T> error(int status, String errorCode,
                                           String message, Object errors) {
            return  ApiResponse.<T>builder()
                    .success(false)
                    .timestamp(LocalDateTime.now())
                    .message(message)
                    .status(status)
                    .errorCode(errorCode)
                    .errors(errors)
                    .traceId(TraceIdHolder.get())
                    .build();
    }

    /**
     * Simple error with no field-level errors
     */
    public static <T> ApiResponse<T> error(int status, String errorCode, String message) {
        return error(status, errorCode, message, null);
    }
}
