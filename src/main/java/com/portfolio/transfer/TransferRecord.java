package com.portfolio.transfer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Table(name="transfer_record",uniqueConstraints=@UniqueConstraint(name="uk_transfer_idempotency",columnNames="idempotency_key"))
public class TransferRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="idempotency_key",nullable=false,updatable=false,length=100) private String idempotencyKey;
 @Column(nullable=false) private Long fromAccount;
 @Column(nullable=false) private Long toAccount;
 @Column(nullable=false,precision=19,scale=2) private BigDecimal amount;
 @Column(nullable=false) private Instant createdAt;
 protected TransferRecord() {}
 public TransferRecord(String key,Long from,Long to,BigDecimal amount){
  this.idempotencyKey=key; this.fromAccount=from;this.toAccount=to;this.amount=amount;this.createdAt=Instant.now();
 }
 public Long getId(){return id;}
 public String getIdempotencyKey(){return idempotencyKey;}
 public Long getFromAccount(){return fromAccount;}
 public Long getToAccount(){return toAccount;}
 public BigDecimal getAmount(){return amount;}
 public Instant getCreatedAt(){return createdAt;}
}
