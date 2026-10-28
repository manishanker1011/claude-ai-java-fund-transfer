# AI-Assisted Code Review — Java Spring Boot Fund Transfer API

## 1. Objective

Evaluate the correctness, reliability, and maintainability of a Java 17 Spring Boot Fund Transfer REST API using Claude AI as a code-review assistant.

The review focused on transaction management, concurrency, idempotency, input validation, exception handling, and automated testing.

## 2. AI-Assisted Review Process

I provided Claude with the application's source code, repository interfaces, DTOs, exception handlers, configuration, and existing tests.

Claude initially identified potential defects and security concerns. After reviewing the complete application, it revised several findings that were already addressed by existing implementations.

The final recommendations were evaluated against the application's intended purpose as a local demonstration project.

## 3. Findings

| Finding                             | Review Outcome                                   |
| ----------------------------------- | ------------------------------------------------ |
| Missing amount validation           | Already handled at the API boundary              |
| Duplicate fund-transfer processing  | Protected by database uniqueness and row locking |
| Transaction rollback                | Runtime exceptions support automatic rollback    |
| HTTP exception status mapping       | Improvement identified and implemented           |
| Concurrent fund transfers           | Additional automated verification recommended    |
| Authentication and authorization    | Known limitation of the demonstration project    |
| Multi-instance database consistency | Not demonstrated with the in-memory H2 database  |

## 4. Implemented Improvement

### HTTP Exception Handling

Updated API error responses to distinguish between different business failures.

| Exception                    | HTTP Status              |
| ---------------------------- | ------------------------ |
| InvalidTransferException     | 400 Bad Request          |
| InsufficientFundsException   | 422 Unprocessable Entity |
| IdempotencyConflictException | 409 Conflict             |

The insufficient-funds response was verified using Postman.

## 5. AI-Assisted Concurrency Testing

Claude suggested additional integration tests to validate the application's concurrency behavior.

Five tests were added and executed using JUnit 5 and Spring Boot Test.

| Test Scenario                                        | Result |
| ---------------------------------------------------- | ------ |
| Simultaneous requests using the same idempotency key | Passed |
| Conflicting payloads using the same key              | Passed |
| Same key across different account pairs              | Passed |
| Opposing transfers between accounts                  | Passed |
| Concurrent debit requests preventing overdrafts      | Passed |

JUnit execution results:

* Tests executed: 5
* Failures: 0
* Errors: 0
* Execution time: 10.268 seconds

The complete Maven test suite was subsequently executed using `clean test` and completed successfully.

## 6. Verification

The application was tested using Postman, JUnit 5, and Maven.

Verified scenarios included successful fund transfers, idempotent retries, insufficient-balance rejection, HTTP exception mapping, and concurrent transaction processing.

## 7. Limitations

This project is a local demonstration and not a production banking system.

It uses an H2 in-memory database and does not include authentication or authorization.

Concurrency testing demonstrates behavior under the tested conditions, but does not establish production readiness or multi-instance guarantees.

## 8. Conclusion

This exercise demonstrates an AI-assisted development workflow involving structured prompting, code-review analysis, evaluation of AI recommendations, implementation of improvements, and automated verification.

AI-generated recommendations were reviewed and tested rather than accepted without validation.
