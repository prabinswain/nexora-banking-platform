package com.auth.AuthService.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * AuthController — all auth endpoints return ApiResponse<T>.
 *
 * EVERY method follows this exact pattern:
 *   1. Call service method
 *   2. Wrap result in ApiResponse.success() or ApiResponse.created()
 *   3. Return ResponseEntity with correct HTTP status
 *
 * Frontend gets this shape ALWAYS — success or error, same wrapper.
 */


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {



}
