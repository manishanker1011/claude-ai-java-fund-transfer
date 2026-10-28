package com.portfolio.transfer;
class AccountNotFoundException extends RuntimeException {AccountNotFoundException(long id){super("Account not found: "+id);}}
class InsufficientFundsException extends RuntimeException {InsufficientFundsException(){super("Insufficient funds");}}
class InvalidTransferException extends RuntimeException {InvalidTransferException(String message){super(message);}}
class IdempotencyConflictException extends RuntimeException {IdempotencyConflictException(String message){super(message);}}
