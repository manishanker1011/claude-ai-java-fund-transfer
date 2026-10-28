package com.portfolio.transfer;
import java.math.BigDecimal;
public record TransferResponse(Long transferId,String idempotencyKey,Long fromAccount,Long toAccount,BigDecimal amount,String status){
 static TransferResponse from(TransferRecord record){return new TransferResponse(record.getId(),record.getIdempotencyKey(),record.getFromAccount(),record.getToAccount(),record.getAmount(),"SUCCESS");}
}
