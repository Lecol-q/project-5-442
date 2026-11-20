# JoshHack Agent Instructions

## Build and Test Environment
- This is a Java Maven project. Use the repository Maven Wrapper (`./mvnw`) rather than relying on a globally installed Maven version.
- Tests use JUnit 5.
- Run commands from the repository root.

## Quality Checks
Before considering code changes complete:
- Run `./mvnw test` and verify that the test suite passes.
- Run the relevant coverage checks using the project's available coverage tooling.
- Run PIT mutation testing with `./mvnw org.pitest:pitest-maven:mutationCoverage` when evaluating test effectiveness. Report timeouts or incomplete results honestly.
- Run SpotBugs with `./mvnw spotbugs:spotbugs` and review the findings.

## Change Guidelines
- Preserve existing behavior unless a behavior change is explicitly requested.
- Do not weaken, remove, skip, or disable tests merely to make the build pass. Fix the underlying issue instead.
- Clearly identify all production-code changes and explain why they are needed so a human can review them.
- Do not push, merge, or publish changes automatically. Wait for explicit human review and approval.