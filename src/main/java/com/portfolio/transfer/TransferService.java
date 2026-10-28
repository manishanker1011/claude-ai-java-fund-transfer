package com.portfolio.transfer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
@Service
public class TransferService {
 private final AccountRepository accounts;
 private final TransferRepository transfers;
 public TransferService(AccountRepository accounts,TransferRepository transfers){this.accounts=accounts;this.transfers=transfers;}
 @Transactional
 public TransferResponse transfer(TransferRequest request){
  if(request.fromAccount().equals(request.toAccount())) throw new InvalidTransferException("Source and destination accounts must differ");
  var prior=transfers.findByIdempotencyKey(request.idempotencyKey());
  if(prior.isPresent())return replay(prior.get(),request);
  // Order locks to avoid deadlocks between simultaneous A->B and B->A requests.
  long first=Math.min(request.fromAccount(),request.toAccount());
  long second=Math.max(request.fromAccount(),request.toAccount());
  Account firstAccount=accounts.lockById(first).orElseThrow(()->new AccountNotFoundException(first));
  Account secondAccount=accounts.lockById(second).orElseThrow(()->new AccountNotFoundException(second));
  // Another request with this key may have committed while this request was waiting for locks.
  prior=transfers.findByIdempotencyKey(request.idempotencyKey());
  if(prior.isPresent())return replay(prior.get(),request);
  Account sender=Objects.equals(firstAccount.getId(),request.fromAccount())?firstAccount:secondAccount;
  Account receiver=sender==firstAccount?secondAccount:firstAccount;
  if(sender.getBalance().compareTo(request.amount())<0) throw new InsufficientFundsException();
  sender.debit(request.amount()); receiver.credit(request.amount());
  // DB unique constraint is last line of defense if same key races with unrelated account locks.
  TransferRecord saved=transfers.saveAndFlush(new TransferRecord(request.idempotencyKey(),request.fromAccount(),request.toAccount(),request.amount()));
  return TransferResponse.from(saved);
 }
 private TransferResponse replay(TransferRecord record,TransferRequest request){
  if(!record.getFromAccount().equals(request.fromAccount()) || !record.getToAccount().equals(request.toAccount()) || record.getAmount().compareTo(request.amount())!=0)
   throw new IdempotencyConflictException("Idempotency key already belongs to a different request");
  return TransferResponse.from(record);
 }
}
