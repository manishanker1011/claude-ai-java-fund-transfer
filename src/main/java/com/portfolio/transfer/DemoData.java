package com.portfolio.transfer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import java.math.BigDecimal;
@Configuration
public class DemoData {
 @Bean CommandLineRunner seed(AccountRepository repo){return args->{
  if(repo.count()==0)repo.saveAll(java.util.List.of(new Account(101L,new BigDecimal("1000.00")),new Account(202L,new BigDecimal("500.00"))));
 };}
}
