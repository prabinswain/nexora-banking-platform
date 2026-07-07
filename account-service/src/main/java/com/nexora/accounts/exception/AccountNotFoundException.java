package com.nexora.accounts.exception;

// Thrown when account number does not exist OR user doesn't own it
public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String message) { super(message); }
}
