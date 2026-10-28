package com.portfolio.transfer;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record TransferRequest(
 @NotNull @Positive Long fromAccount,
 @NotNull @Positive Long toAccount,
 @NotNull @DecimalMin("0.01") @Digits(integer=17,fraction=2) BigDecimal amount,
 @NotBlank @Size(max=100) String idempotencyKey) {}
