package com.portfolio.transfer;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class TransferController {
 private final TransferService service;
 private final AccountRepository accounts;
 public TransferController(TransferService service,AccountRepository accounts){this.service=service;this.accounts=accounts;}
 @PostMapping("/transfers") public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request){return ResponseEntity.ok(service.transfer(request));}
 @GetMapping("/accounts/{id}") public AccountView account(@PathVariable Long id){
  var a=accounts.findById(id).orElseThrow(()->new AccountNotFoundException(id));
  return new AccountView(a.getId(),a.getBalance());
 }
 record AccountView(Long id,java.math.BigDecimal balance) {}
}
