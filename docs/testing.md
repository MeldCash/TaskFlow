# Testing Strategy

Testing is a planned implementation phase. The test suite should use Maven's standard lifecycle and isolate fast unit tests from database-backed integration tests.

| Test type | Scope | What to prove |
| --- | --- | --- |
| Unit tests | Services, mappers, and small rule helpers | Business rules: unique-email handling, reference checks, defaults, immutable owner/project rules, allowed enum values, date ordering, and deletion restrictions. Mock repositories at this level. |
| Repository tests | JPA repositories against PostgreSQL-compatible test infrastructure | Entity mappings, constraints, queries, indexes where observable, and each task filter alone and in combination. Prefer Testcontainers PostgreSQL once dependencies are approved. |
| Controller tests | HTTP controllers and global exception handler | Routing, request validation, JSON shape, `Location` header, status policy, filter parameter parsing, and the stable error envelope. |
| Integration tests | Full application slices crossing controller, service, repository, and database | Critical CRUD flows, project-delete task cascade, user-deletion protection, unique email conflict, and persistence of defaults. |

## Minimum test scenarios

- Create, retrieve, update, and delete an eligible user, project, and task.
- Reject a duplicate email regardless of casing.
- Reject a project with a missing owner and a task with a missing project or assignee.
- Default an omitted task status to `TODO` and priority to `MEDIUM`.
- Reject invalid task status/priority and invalid project date ordering.
- Combine `status`, `priority`, `projectId`, `assignedUserId`, and `dueDate` filters using AND semantics.
- Prevent deletion of a user with owned projects or task assignments.
- Confirm that project deletion removes its tasks.
- Verify `400`, `404`, and `409` responses use the documented envelope and do not leak implementation details.

## Quality gates

Before merging implementation work, run `mvn clean test` (or the Maven Wrapper equivalent) and require the relevant unit, repository, controller, and integration tests to pass. Code coverage targets should not be selected until the first test baseline exists; meaningful coverage of critical rules is more valuable than a superficial percentage.
