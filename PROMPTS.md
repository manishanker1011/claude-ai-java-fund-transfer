# Claude AI — Prompt Engineering Demonstration

## Objective

Demonstrate the effective use of Claude AI for reviewing Java Spring Boot code, identifying defects, recommending improvements, and developing automated test cases.

**Technology Stack:** Java 17, Spring Boot 3.3.5, Spring Data JPA, H2, JUnit 5, Mockito, Maven.

## Prompt 1: Initial Code Review

Act as a Senior Java Developer and Code Reviewer.

I have developed a banking fund transfer REST API using Java 17, Spring Boot, Spring Data JPA, and H2 Database.

The application supports fund transfers, balance validation, idempotency, transaction management, and REST API error handling.

After I share my source code:

1. Identify potential bugs and logical errors.
2. Review concurrency and thread-safety issues.
3. Verify whether idempotency works across multiple application instances.
4. Identify database transaction and rollback issues.
5. Check security vulnerabilities and input validation.
6. Recommend improvements following Java and Spring Boot best practices.
7. Recommend JUnit 5 and Mockito test cases.

For every issue, explain its severity, underlying cause, and recommended fix.

Do not assume the implementation is correct. Base your review on the actual source code provided.

Wait until I share the code before reviewing.

## Prompt 2: Full Application Review

Review the remaining application files together with TransferService.java.

Verify which previously identified issues are actual bugs and which are already handled elsewhere.

Prioritize confirmed issues by severity.

Suggest minimal changes without unnecessarily rewriting the application.

Generate additional JUnit 5, Mockito, and integration tests without duplicating existing coverage.

Separate confirmed findings from assumptions.

Wait until all source files have been provided before delivering the final review.

## Prompt 3: Final Assessment

The complete Java Spring Boot demonstration project has been shared.

This is a local application using an H2 in-memory database. It has no authentication or authorization configuration and is not intended for production deployment.

Please provide:

1. Confirmed bugs and security limitations.
2. Issues already handled correctly.
3. The five highest-priority improvements.
4. Minimal code fixes.
5. Additional unit tests.
6. Integration and concurrency tests.
7. Verification instructions.

Distinguish actual defects from production enhancements.

Do not rewrite the entire application.

## AI-Assisted Development Results

Claude identified potential issues involving transaction isolation, exception mapping, account validation, and concurrency verification.

After examining additional source files, Claude corrected several initial findings.

The review resulted in an improvement to HTTP exception handling and the addition of concurrency tests.

## Validation Results

Five concurrency tests were executed successfully using JUnit 5.

The complete Maven test suite also completed with BUILD SUCCESS.

The insufficient-funds HTTP response was verified using Postman.

## Key Learning

Effective prompt engineering involves providing sufficient technical context, clearly defining evaluation criteria, reviewing AI-generated recommendations, and verifying results through testing.

AI-generated code and recommendations should not be accepted without independent validation.
