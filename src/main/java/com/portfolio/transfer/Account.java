package com.portfolio.transfer;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
public class Account {
 @Id private Long id;
 @Column(nullable=false,precision=19,scale=2) private BigDecimal balance;
 protected Account() {}
 public Account(Long id, BigDecimal balance) {this.id=id;this.balance=balance;}
 public Long getId(){return id;}
 public BigDecimal getBalance(){return balance;}
 public void debit(BigDecimal amount){balance=balance.subtract(amount);}
 public void credit(BigDecimal amount){balance=balance.add(amount);}
}
