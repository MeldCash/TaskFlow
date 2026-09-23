# Roadmap

| Phase | Objective | Tasks | Dependencies | Expected result |
| --- | --- | --- | --- | --- |
| 0 — Documentation | Approve a coherent MVP design. | Review requirements, architecture, database model, API, rules, tests, and roadmap; resolve open decisions. | None. | Approved technical specification. |
| 1 — Project setup | Create a runnable backend skeleton. | Create Maven/Spring Boot project, Java 21 configuration, package structure, baseline dependencies, and configuration boundary. | Phase 0 approval. | Completed in repository; execution remains pending local Java 21 and Maven installation. |
| 2 — Database | Establish safe persistent storage. | PostgreSQL local profile, Flyway migration, initial schema, constraints, indexes, and `updated_at` triggers. | Phase 1. | Implemented in repository; execution verification awaits local Java, Maven, and PostgreSQL. |
| 3 — Users | Implement user capability. | Entity/DTO/repository/service/controller, email uniqueness, validation, and shared errors. | Phase 2. | Implemented in repository; runtime and test verification await local Java, Maven, and PostgreSQL. User-deletion relationship protection is completed in Phases 4–5. |
| 4 — Projects | Implement project capability. | Owner lookup, project CRUD, dates/status rules, and shared errors. | Phase 3. | Implemented in repository; runtime and test verification await local Java, Maven, and PostgreSQL. |
| 5 — Tasks | Implement task capability. | Task CRUD, project/assignee lookup, state/priority defaults, filters, and cascade behavior. | Phases 3–4. | Implemented in repository; runtime and test verification await local Java, Maven, and PostgreSQL. |
| 6 — Validation & Error Handling | Make failure behavior consistent. | Global error handler, error codes, validation messages, conflict translation, and coverage. | Phases 3–5. | Stable public error contract. |
| 7 — Testing | Build confidence in the MVP. | Complete unit, repository, controller, and integration test matrix; automate Maven test execution. | Phases 3–6. | Repeatable passing test suite. |
| 8 — API Documentation | Publish consumable API reference. | Add OpenAPI/Swagger only after explicit approval, document examples, and align generated output with this contract. | Phases 3–7. | Current API reference for consumers. |
| 9 — Security | Secure the API. | Define identities, roles, permissions, JWT, password policy, authorization rules, and security tests. | MVP stable; security design approval. | Authenticated and authorized API. |
| 10 — Docker | Standardize local/runtime packaging. | Dockerfile, Compose service for PostgreSQL, environment handling, and documentation. | Phases 1–2, preferably MVP stable. | One-command local environment. |
| 11 — CI/CD | Automate quality and delivery. | Continuous build/test workflow, dependency/security scanning policy, artifact/image publication, and deployment design. | Phases 7 and 10. | Automated, repeatable pipeline. |

## Approval gates

Before Phase 1, approve the MVP boundaries, ownership/deletion policies, project status decision, task-assignment rule, API update semantics, and the preferred migration tool. Before Phase 9, approve the identity and authorization model rather than retrofitting it implicitly.
