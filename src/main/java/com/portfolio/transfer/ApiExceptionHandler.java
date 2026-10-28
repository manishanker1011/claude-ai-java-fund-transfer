package com.portfolio.transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
 @ExceptionHandler(AccountNotFoundException.class)
 ResponseEntity<Map<String,String>> notFound(AccountNotFoundException e){return respond(HttpStatus.NOT_FOUND,e.getMessage());}
 
 @ExceptionHandler(InvalidTransferException.class)
 ResponseEntity<Map<String,String>> invalidTransfer(
         InvalidTransferException e) {
     return respond(HttpStatus.BAD_REQUEST, e.getMessage());
 }
 
 @ExceptionHandler(InsufficientFundsException.class)
 ResponseEntity<Map<String,String>> insufficient(
         InsufficientFundsException e) {
     return respond(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
 }
 
 @ExceptionHandler(IdempotencyConflictException.class)
 ResponseEntity<Map<String,String>> keyConflict(
         IdempotencyConflictException e) {
     return respond(HttpStatus.CONFLICT, e.getMessage());
 }
 
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<Map<String,String>> validation(MethodArgumentNotValidException e){return respond(HttpStatus.BAD_REQUEST,"Invalid request fields");}
 
 @ExceptionHandler(DataIntegrityViolationException.class)
 ResponseEntity<Map<String,String>> contention(DataIntegrityViolationException e){return respond(HttpStatus.CONFLICT,"Concurrent request conflict; retry with the same idempotency key");}
 private ResponseEntity<Map<String,String>> respond(HttpStatus status,String message){return ResponseEntity.status(status).body(Map.of("error",message));}
}