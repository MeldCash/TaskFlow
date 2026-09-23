# Architecture

## Current repository assessment

Phase 1 provides `pom.xml`, the application entry point under `com.giuss.taskflow`, baseline configuration, and a `.gitignore`. Phase 2 adds Flyway, a PostgreSQL `local` profile, and the first schema migration. The configured target baseline is Java 21, Spring Boot 4.1.1, Maven, Spring Web, Spring Data JPA, PostgreSQL, Bean Validation, Lombok, and base package `com.giuss.taskflow`. The local environment currently has neither `java`, `mvn`, nor `psql` on its executable path, so the configured versions, migration, and build have not yet been run here. No Maven Wrapper could be generated without Maven.

## Architectural style

TaskFlow should begin as a modular monolith. It is one deployable Spring Boot application with modules organized around business capabilities, not technical layers across the whole application. This keeps the MVP simple to run and test while preserving boundaries that can later evolve independently.

```text
com.giuss.taskflow
├── common
│   ├── exception
│   ├── error
│   └── config
├── user
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   ├── mapper
│   └── exception
├── project
│   ├── controller
│   ├── service
│   ├── repository
│   ├── entity
│   ├── dto
│   ├── mapper
│   └── exception
└── task
    ├── controller
    ├── service
    ├── repository
    ├── entity
    ├── dto
    ├── mapper
    └── exception
```

The `user`, `project`, and `task` modules now contain their documented controller, service, repository, entity, DTO, and mapper layers. The `common` module contains the shared error contract and exception handler.

## Layer responsibilities

| Layer | Responsibility |
| --- | --- |
| `controller` | HTTP boundary: route requests, validate DTOs, call services, and select documented HTTP responses. It contains no business rules. |
| `service` | Transaction boundary and business rules. It orchestrates repositories and checks referenced resources. |
| `repository` | Persistence queries and access through Spring Data JPA. It does not expose HTTP concerns. |
| `entity` | JPA persistence mapping. Entities do not form the external API contract. |
| `dto` | Request and response contracts. Separate create, update, and response DTOs avoid accidental writable fields. |
| `mapper` | Explicit conversion between entities and DTOs. Manual mappers are recommended initially; MapStruct is not needed for this MVP. |
| `exception` | Module-specific domain exceptions where a common error treatment is insufficient. |
| `common` | Cross-cutting error model, exception handler, configuration, and shared non-domain utilities. It must not become a dumping ground for business logic. |

Dependencies flow inward: controller to service to repository/entity. Modules may use another module's service only for a defined use case (for example, task creation verifying project and assignee); they should not reach into another module's repository.

## API and persistence conventions

- Controllers use `/api` resource paths and DTOs only.
- Services own transactions and turn missing resources into domain-not-found errors.
- Database constraints back up, but do not replace, API validation and business-rule checks.
- A global exception handler in `common` produces the error envelope defined in [api.md](api.md#error-response-format).
- Enumerations are stored as strings, never ordinal positions.
- Database migrations are required before any persistent feature. Flyway is the recommended migration tool, but adding it is a Phase 2 decision and requires approval because it is an additional dependency.

## Local development configuration

The following is planned configuration, not yet present:

| Item | Expected value / action | Current state |
| --- | --- | --- |
| JDK | Java 21 installed and selected by Maven | Pending: Java is not available on the current executable path |
| Build | `mvn clean test` and `mvn spring-boot:run` supported; Maven Wrapper may be added after Maven installation | Pending: Maven is not available on the current executable path |
| Database | PostgreSQL database, for example `taskflow` | Pending local provisioning |
| `DB_HOST` | PostgreSQL host, e.g. `localhost` | Configured; default `localhost` |
| `DB_PORT` | PostgreSQL port, normally `5432` | Configured; default `5432` |
| `DB_NAME` | Database name, e.g. `taskflow` | Configured; default `taskflow` |
| `DB_USERNAME` | Local database user | Configured; default `postgres` |
| `DB_PASSWORD` | Local database password; never commit it | Required by the `local` profile; no default is supplied |
| Spring profile | `local` profile reading the variables above | Implemented |

Current bootstrap execution sequence after installing Java 21 and Maven: run `mvn clean test`, then `mvn spring-boot:run`. The default `bootstrap` profile deliberately excludes datasource and JPA auto-configuration, so this starts without a database. For the PostgreSQL profile, create the database, set the `DB_*` variables, then run `mvn spring-boot:run -Dspring-boot.run.profiles=local`. Flyway applies `V1__create_taskflow_schema.sql`, and `ddl-auto: validate` prevents Hibernate from silently changing that schema.

## Future security phase

Spring Security is out of scope for the MVP implementation. A later phase should define authentication, authorization, JWT issuance/validation, roles, permissions, password lifecycle, and ownership enforcement before adding security dependencies or configuration.
