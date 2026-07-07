package com.nexora.accounts.controller;

import com.nexora.accounts.dto.request.OpenAccountRequest;
import com.nexora.accounts.dto.response.AccountResponse;
import com.nexora.accounts.dto.response.OpenAccountResponse;
import com.nexora.accounts.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final AccountService accountService;

    @PostMapping // ("/create")
   public ResponseEntity<OpenAccountResponse> openAccount(@RequestBody @Valid OpenAccountRequest request){
        OpenAccountResponse data = accountService.openAccount(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(AccountResponse.created(data, "Account opened successfully"));
    }
}
