package com.portfolio.transfer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
 

@SpringBootTest
public class TransferConcurrencyTest {
 @Autowired TransferService service;
 @Autowired AccountRepository accounts;
 @Autowired TransferRepository transfers;

 static final long A=9001L, B=9002L, C=9003L, D=9004L;
 record Outcome(TransferResponse ok, Throwable error) {}

 @BeforeEach void reset(){ // merge() overwrites existing rows, so each test starts clean
  accounts.saveAll(List.of(new Account(A,bd("1000.00")),new Account(B,bd("1000.00")),
                           new Account(C,bd("0.00")),new Account(D,bd("1000.00"))));
 }
 static BigDecimal bd(String s){ return new BigDecimal(s); }

 /** Releases all tasks at once. A deadlock surfaces as TimeoutException instead of hanging the build. */
 private List<Outcome> runAll(int poolSize, List<Callable<TransferResponse>> tasks) throws Exception {
  ExecutorService pool=Executors.newFixedThreadPool(poolSize);
  try {
   CountDownLatch go=new CountDownLatch(1);
   List<Future<Outcome>> fs=new ArrayList<>();
   for(var t:tasks) fs.add(pool.submit(()->{ go.await();
     try { return new Outcome(t.call(),null); } catch(Throwable e){ return new Outcome(null,e); } }));
   go.countDown();
   List<Outcome> out=new ArrayList<>();
   for(var f:fs) out.add(f.get(60,TimeUnit.SECONDS));
   return out;
  } finally { pool.shutdownNow(); }
 }
 private void assertBalance(long id,String expected){
  assertEquals(0,accounts.findById(id).orElseThrow().getBalance().compareTo(bd(expected)),"balance of "+id);
 }
 private void assertEquals(int i, int compareTo, String string) {
	// TODO Auto-generated method stub
	
}
private static Callable<TransferResponse> call(TransferService s,TransferRequest r){ return ()->s.transfer(r); }

 @Test void sameKeySameRequest_isAppliedExactlyOnce() throws Exception {
  String key=UUID.randomUUID().toString();
  var r=new TransferRequest(A,B,bd("10.00"),key);
  var out=runAll(8,IntStream.range(0,20).mapToObj(i->call(service,r)).toList());
  var ok=out.stream().filter(o->o.ok()!=null).toList();
  var errors=out.stream().map(Outcome::error).filter(Objects::nonNull).toList();
  assertFalse(ok.isEmpty());
  AssertTrue(errors.stream().allMatch(e->e instanceof DataIntegrityViolationException),"unexpected: "+errors);
  assertEquals(1,ok.stream().map(o->o.ok().transferId()).distinct().count());
  assertEquals(1,transfers.findAll().stream().filter(t->key.equals(t.getIdempotencyKey())).count());
  assertBalance(A,"990.00"); assertBalance(B,"1010.00");
 }

 private void AssertTrue(boolean allMatch, String string) {
	// TODO Auto-generated method stub
	
}
private void assertEquals(List<Object> list, List<Throwable> list2) {
	// TODO Auto-generated method stub
	
}
private void assertFalse(boolean empty) {
	// TODO Auto-generated method stub
	
}
@Test void sameKeyDifferentPayload_onlyOneWins() throws Exception {
  String key=UUID.randomUUID().toString();
  List<Callable<TransferResponse>> tasks=new ArrayList<>();
  for(int i=0;i<10;i++){ tasks.add(call(service,new TransferRequest(A,B,bd("10.00"),key)));
                         tasks.add(call(service,new TransferRequest(A,B,bd("20.00"),key))); }
  var out=runAll(8,tasks);
  assertTrue(out.stream().map(Outcome::error).filter(Objects::nonNull)
     .allMatch(e->e instanceof IdempotencyConflictException||e instanceof DataIntegrityViolationException));
  var winner=transfers.findByIdempotencyKey(key).orElseThrow();
  assertBalance(A,bd("1000.00").subtract(winner.getAmount()).toPlainString());
 }

 private void assertTrue(boolean allMatch) {
	// TODO Auto-generated method stub
	
}
@Test void sameKeyDifferentAccountPairs_loserRollsBack() throws Exception {
  String key=UUID.randomUUID().toString();
  var out=runAll(2,List.of(call(service,new TransferRequest(A,B,bd("10.00"),key)),
                           call(service,new TransferRequest(C,D,bd("0.01"),key))));
  assertEquals(1,transfers.findAll().stream().filter(t->key.equals(t.getIdempotencyKey())).count());
  assertEquals(1,out.stream().filter(o->o.ok()!=null).count());
  BigDecimal total=List.of(A,B,C,D).stream().map(id->accounts.findById(id).orElseThrow().getBalance())
                       .reduce(BigDecimal.ZERO,BigDecimal::add);
  assertEquals(0,total.compareTo(bd("2000.00"))); // money conserved; loser's debit/credit rolled back
 }

 private void assertEquals(int i, long count) {
	// TODO Auto-generated method stub
	
}
@Test void opposingTransfers_noDeadlock_moneyConserved() throws Exception {
  List<Callable<TransferResponse>> tasks=new ArrayList<>();
  for(int i=0;i<100;i++){
   tasks.add(call(service,new TransferRequest(A,B,bd("1.00"),"ab-"+UUID.randomUUID())));
   tasks.add(call(service,new TransferRequest(B,A,bd("1.00"),"ba-"+UUID.randomUUID())));
  }
  var out=runAll(8,tasks);
  assertEquals(List.of(),out.stream().map(Outcome::error).filter(Objects::nonNull).toList());
  assertBalance(A,"1000.00"); assertBalance(B,"1000.00");
 }

 @Test void concurrentDebits_neverOverdraw() throws Exception {
  // A holds 1000.00; 150 distinct 10.00 transfers → exactly 100 can succeed
  var tasks=IntStream.range(0,150).mapToObj(i->call(service,
      new TransferRequest(A,C,bd("10.00"),"od-"+UUID.randomUUID()))).toList();
  var out=runAll(8,tasks);
  assertEquals(100,out.stream().filter(o->o.ok()!=null).count());
  assertEquals(50,out.stream().filter(o->o.error() instanceof InsufficientFundsException).count());
  assertBalance(A,"0.00"); assertBalance(C,"1000.00");
 }
}
