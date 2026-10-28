# Claude AI-Assisted Java Development

### Spring Boot Banking Fund Transfer REST API

## Project Overview

This project demonstrates practical AI-assisted software development using Claude AI and Java Spring Boot.

The objective is to demonstrate three essential AI-assisted engineering skills:

1. **Prompt Engineering** — Providing structured technical instructions to Claude AI.
2. **Code Review** — Evaluating AI recommendations, identifying defects, and implementing improvements.
3. **Automated Testing** — Using AI to design test scenarios and validating the results through JUnit and Maven.

The application implements a banking fund transfer REST API with transaction management, database locking, and idempotency.

## Technology Stack

| Technology        | Purpose                                     |
| ----------------- | ------------------------------------------- |
| Java 17           | Backend development                         |
| Spring Boot 3.3.5 | Application framework                       |
| Spring Data JPA   | Database persistence                        |
| Hibernate         | ORM                                         |
| H2 Database       | Local in-memory database                    |
| JUnit 5           | Automated testing                           |
| Mockito           | Unit testing                                |
| Maven             | Build and dependency management             |
| Postman           | REST API testing                            |
| Claude AI         | AI-assisted code review and test generation |

## Application Features

* REST API for transferring funds between accounts.
* Balance validation before transfers.
* Transaction management using Spring.
* Idempotency to prevent duplicate transactions.
* Pessimistic database locking.
* Consistent lock ordering to reduce deadlock risks.
* HTTP exception handling.
* Automated unit, integration, and concurrency tests.

## AI-Assisted Development Workflow

### Phase 1: Prompt Engineering

Claude was provided with structured prompts specifying the technology stack, application requirements, review criteria, and testing expectations.

See [PROMPTS.md](PROMPTS.md) for the documented prompts.

### Phase 2: Code Review

Claude reviewed the Java source code and identified potential issues.

After reviewing additional application files, Claude revised several initial findings and distinguished existing safeguards from remaining improvements.

An HTTP exception-handling improvement was implemented and verified.

See [CODE_REVIEW.md](CODE_REVIEW.md) for the findings and outcomes.

### Phase 3: Automated Testing

Claude recommended additional concurrency tests.

Five tests were implemented and successfully executed.

| Concurrency Scenario                       | Result |
| ------------------------------------------ | ------ |
| Simultaneous duplicate requests            | PASSED |
| Same key with conflicting payloads         | PASSED |
| Same key across different account pairs    | PASSED |
| Opposing fund transfers                    | PASSED |
| Concurrent debits and overdraft prevention | PASSED |

**JUnit Results:** 5 tests, 0 failures, 0 errors.

**Maven Build:** BUILD SUCCESS.

See [TEST_PLAN.md](TEST_PLAN.md) for additional testing details.

## Getting Started

### Prerequisites

* Java JDK 17
* Maven
* Eclipse IDE or IntelliJ IDEA
* Postman (optional)

### Build and Test

Run the following commands from the project root:

```bash
mvn clean test
```

### Start the Application

```bash
mvn spring-boot:run
```

The application runs locally on port `8080`.

### Example Fund Transfer Request

**POST** `/api/transfers`

```json
{
  "fromAccount": 101,
  "toAccount": 202,
  "amount": 125.00,
  "idempotencyKey": "transfer-001"
}
```

### Check Account Balance

**GET** `/api/accounts/101`

### Example Error Response

An insufficient-funds transfer returns HTTP `422 Unprocessable Entity`.

```json
{
  "error": "Insufficient funds"
}
```

## Verification Results

The application was tested using Postman, JUnit 5, and Maven.

Verified functionality includes successful transfers, idempotent retries, insufficient-funds handling, balance preservation, and concurrent transactions.

The complete Maven test suite completed successfully.

## Project Documentation

* [PROMPTS.md](PROMPTS.md) — AI prompting approach and instructions.
* [CODE_REVIEW.md](CODE_REVIEW.md) — Review findings, fixes, and limitations.
* [TEST_PLAN.md](TEST_PLAN.md) — Functional and automated test coverage.

## Limitations

This project is intended for local demonstration and educational purposes.

It uses an H2 in-memory database, which does not provide shared persistence across independent application instances.

Authentication, authorization, and production banking security controls are not implemented.

The concurrency results describe the tested scenarios and do not establish production readiness.

## Conclusion

This project demonstrates an AI-assisted software development workflow combining structured prompting, critical evaluation of AI recommendations, implementation of code improvements, and automated testing.

Claude was used as a development assistant, while correctness was evaluated through code review and executable tests.
