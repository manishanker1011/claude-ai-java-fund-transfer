package com.portfolio.transfer;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class TransferServiceTest {
 AccountRepository accounts=mock(AccountRepository.class);
 TransferRepository transfers=mock(TransferRepository.class);
 TransferService service=new TransferService(accounts,transfers);
 TransferRequest req=new TransferRequest(101L,202L,new BigDecimal("120.00"),"demo-key");
 @Test void rejectsSameAccount(){assertThrows(InvalidTransferException.class,()->service.transfer(new TransferRequest(101L,101L,new BigDecimal("1"),"k")));}
 @Test void insufficientBalanceDoesNotWriteTransfer(){
  when(transfers.findByIdempotencyKey("demo-key")).thenReturn(Optional.empty());
  when(accounts.lockById(101L)).thenReturn(Optional.of(new Account(101L,new BigDecimal("100.00"))));
  when(accounts.lockById(202L)).thenReturn(Optional.of(new Account(202L,new BigDecimal("500.00"))));
  assertThrows(InsufficientFundsException.class,()->service.transfer(req));
  verify(transfers,never()).saveAndFlush(any());
 }
 @Test void replayReturnsPreviousTransferWithoutLockingAccounts(){
  var existing=new TransferRecord("demo-key",101L,202L,new BigDecimal("120.00"));
  when(transfers.findByIdempotencyKey("demo-key")).thenReturn(Optional.of(existing));
  assertEquals("SUCCESS",service.transfer(req).status());
  verifyNoInteractions(accounts);
 }
 @Test void reusedKeyWithDifferentAmountRejected(){
  when(transfers.findByIdempotencyKey("demo-key")).thenReturn(Optional.of(new TransferRecord("demo-key",101L,202L,new BigDecimal("90.00"))));
  assertThrows(IdempotencyConflictException.class,()->service.transfer(req));
 }
 @Test void happyPathUpdatesBalancesAndWritesTransfer(){
  Account a=new Account(101L,new BigDecimal("1000.00")); Account b=new Account(202L,new BigDecimal("500.00"));
  when(accounts.lockById(101L)).thenReturn(Optional.of(a)); when(accounts.lockById(202L)).thenReturn(Optional.of(b));
  when(transfers.saveAndFlush(any())).thenAnswer(inv->inv.getArgument(0));
  TransferResponse response=service.transfer(req);
  assertEquals("SUCCESS",response.status());
  assertEquals(0,a.getBalance().compareTo(new BigDecimal("880.00")));
  assertEquals(0,b.getBalance().compareTo(new BigDecimal("620.00")));
  verify(transfers).saveAndFlush(any(TransferRecord.class));
 }
}
