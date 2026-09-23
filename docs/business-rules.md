# Business Rules

## Global conventions

- Resource identifiers are generated positive `BIGINT` values and are never client supplied on creation.
- Blank strings are invalid after trimming. Text values are stored and returned trimmed.
- Timestamps are server managed in UTC. Calendar dates use ISO-8601 `YYYY-MM-DD` and do not imply a time zone.
- IDs in a request must refer to an existing resource. Missing references are reported as `404 Not Found`.

## Users

| Rule | Decision |
| --- | --- |
| Required data | `name` and `email` are required. |
| Name | Trimmed, 1–100 characters. |
| Email | Trimmed, syntactically valid, maximum 254 characters, and unique case-insensitively. The canonical stored value is lower case. |
| States | No user status is part of this MVP. Do not add active/inactive states without a requirements change. |
| Deletion | A user cannot be deleted while they own any project or are assigned to any task. The API returns `409 Conflict`, preventing accidental data loss. |

## Projects

| Rule | Decision |
| --- | --- |
| Required data | `name` and `ownerId` are required on creation. |
| Name | Trimmed, 1–150 characters. Project names need not be globally unique. |
| Owner | Exactly one existing user owns each project. Ownership is set at creation and is immutable in this MVP. |
| Status | `ACTIVE` or `ARCHIVED`; default `ACTIVE`. This project-level status is distinct from task status and is needed to express whether the project is current. |
| Dates | `startDate` and `endDate` are optional. If both are supplied, `startDate` must be on or before `endDate`. |
| Deletion | Deleting a project deletes all tasks contained by it. This cascade is intentional and must be clearly surfaced to clients. |

## Tasks

| Rule | Decision |
| --- | --- |
| Required data | `projectId` and `title` are required on creation. |
| Project | Every task belongs to exactly one existing project. A task cannot move between projects in the MVP. |
| Title | Trimmed, 1–200 characters. |
| Description | Optional, maximum 4,000 characters. |
| Status | Allowed values are `TODO`, `IN_PROGRESS`, and `DONE`; default is `TODO`. All transitions between these values are permitted in the MVP. |
| Priority | Allowed values are `LOW`, `MEDIUM`, and `HIGH`; default is `MEDIUM`. |
| Assignee | Optional. When supplied, it identifies one existing user. Assigning a user does not imply project membership, because membership is outside the MVP. |
| Due date | Optional ISO-8601 calendar date. Past due dates are accepted because they can represent imported or overdue work. |
| Deletion | A task may be deleted directly. It is also deleted when its project is deleted. |

## Deferred decisions

Project membership and assignee eligibility need an explicit design before being added. The current model permits any existing user to be assigned to a task, which keeps the MVP aligned with the stated three-entity model. Authentication, authorization, and user activation are deferred to the security phase.
