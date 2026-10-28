# AI-Assisted Testing — Fund Transfer REST API

## 1. Objective

Validate the functionality, reliability, transaction consistency, and concurrency behavior of the Java 17 Spring Boot Fund Transfer application.

Claude AI was used to recommend additional test scenarios. The resulting tests were implemented and executed using JUnit 5, Spring Boot Test, and Maven.

## 2. Technology Stack

* Java 17
* Spring Boot 3.3.5
* JUnit 5
* Mockito
* Spring Boot Test
* H2 In-Memory Database
* Maven
* Postman

## 3. API Functional Testing

| Test Scenario                               | Expected Result                         | Status |
| ------------------------------------------- | --------------------------------------- | ------ |
| Successful fund transfer                    | Transfer completed and balances updated | Passed |
| Duplicate request with same idempotency key | No duplicate deduction                  | Passed |
| Insufficient balance                        | Transfer rejected                       | Passed |
| Balance verification after failed transfer  | Original balance preserved              | Passed |
| HTTP exception mapping                      | Insufficient funds returns HTTP 422     | Passed |

## 4. Automated Concurrency Testing

Five concurrent transaction scenarios were tested using `TransferConcurrencyTest.java`.

| Test Case                         | Expected Result                     | Status |
| --------------------------------- | ----------------------------------- | ------ |
| Same key, same request            | Transfer applied exactly once       | Passed |
| Same key, different payload       | Conflicting transfer rejected       | Passed |
| Same key, different account pairs | Only one transfer committed         | Passed |
| Opposing transfers                | No deadlock; balances consistent    | Passed |
| Concurrent debits                 | No overdraft; balance remains valid | Passed |

### JUnit Execution Results

* Tests executed: 5
* Tests passed: 5
* Failures: 0
* Errors: 0
* Execution time: 10.268 seconds

## 5. Maven Build Verification

The complete Maven test suite was executed using:

```bash
mvn clean test
```

**Result: BUILD SUCCESS**

This confirms that the Maven test phase completed successfully in the tes
