# TaskFlow Backend

TaskFlow is a portfolio REST API for managing users, projects, and the tasks that belong to those projects. It is deliberately scoped as a small, maintainable MVP before collaboration, notifications, reporting, and security are introduced.

## Goal

Provide a clear backend foundation for tracking project work: who owns a project, which tasks it contains, who is assigned to each task, and each task's status, priority, and due date.

## Feature status

| Status | Scope |
| --- | --- |
| Implemented | Project bootstrap and PostgreSQL/Flyway schema baseline |
| Implemented | User CRUD, validation, unique email handling, and shared API error format |
| Implemented | Project ownership, lifecycle, dates, and CRUD |
| Implemented | Task assignment, status, priority, due dates, filters, and CRUD |
| Planned | Validation, consistent API errors, and automated tests |
| Future phase | Authentication, authorization, JWT, roles, permissions, Docker, and CI/CD |

Users, projects, and tasks are implemented. Automated test coverage, API documentation tooling, security, Docker, and CI/CD remain planned.

## Planned stack

- Java 21
- Spring Boot 4.1.1
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Lombok

The Maven project, application entry point, PostgreSQL local profile, and first Flyway migration are in place. No domain entities or API endpoints have been created.

## Architecture

The target architecture is a modular monolith organized by business capability: `user`, `project`, `task`, and `common`. Each module owns its HTTP, application, persistence, DTO, mapping, and error-boundary concerns. See [architecture.md](docs/architecture.md).

## Documentation

- [Requirements](docs/requirements.md)
- [Business rules](docs/business-rules.md)
- [Database model and ERD](docs/database.md)
- [REST API contract](docs/api.md)
- [Architecture](docs/architecture.md)
- [Testing strategy](docs/testing.md)
- [Roadmap](docs/roadmap.md)

## Running locally

The bootstrap requires Java 21, Maven, and PostgreSQL for the database profile. A Maven Wrapper has not been added because Maven is not installed in the current environment.

To start without a database: `mvn spring-boot:run`.

To run the database profile, create a PostgreSQL database, set the variables in [.env.example](.env.example) in your shell (never commit `.env`), then run `mvn spring-boot:run -Dspring-boot.run.profiles=local`. Flyway applies `V1__create_taskflow_schema.sql` automatically and JPA validates rather than generates the schema. The required configuration is documented in [architecture.md](docs/architecture.md#local-development-configuration).

## Roadmap

The work proceeds from documentation review through bootstrap, database, users, projects, tasks, validation/error handling, tests, API documentation, security, Docker, and CI/CD. Details and dependencies are in [roadmap.md](docs/roadmap.md).
