# TaskFlow Requirements

## Product definition

TaskFlow is a REST API that gives a small team or an individual a single place to organize projects and the work inside them. It solves the basic visibility problem of knowing what work belongs to a project, who is responsible for it, and whether it is still pending, in progress, or done.

The system serves API consumers such as a future web client, mobile client, or API testing tool. In this MVP, a user is a persisted business record, not an authenticated account. Authentication and permissions are intentionally deferred.

## MVP scope

The MVP includes users, projects, tasks, task states, priorities, task assignment, and due dates. A project has one owner; a task has one project and may have one assignee. The API provides CRUD endpoints and the documented task filters.

The MVP excludes authentication, authorization, roles, permissions, invitations, multiple assignees, comments, attachments, labels, subtasks, notifications, audit history, reporting, pagination, sorting, soft deletion, recurring tasks, and project membership beyond the owner relationship. These may be proposed later only through an approved requirements change.

## Functional requirements

### Users

| ID | Name | Description | Business rules | Acceptance criteria |
| --- | --- | --- | --- | --- |
| RF-001 | Create user | Create a user record. | Name and email are required; email is unique and valid. | A valid request returns `201 Created` and the generated user; a duplicate email returns `409 Conflict`. |
| RF-002 | Read users | Retrieve one user or the complete user collection. | A requested user must exist. | `GET /api/users/{id}` returns `200 OK` for an existing ID and `404 Not Found` otherwise; collection retrieval returns `200 OK`. |
| RF-003 | Update user | Change a user's name and email. | The resulting email remains unique and valid. | A valid update returns `200 OK`; an email owned by another user returns `409 Conflict`. |
| RF-004 | Delete user | Delete a user that no longer owns or is assigned active data. | Deletion is prohibited while the user owns projects or is assigned tasks. | An eligible deletion returns `204 No Content`; a protected deletion returns `409 Conflict` with a clear reason. |

### Projects

| ID | Name | Description | Business rules | Acceptance criteria |
| --- | --- | --- | --- | --- |
| RF-005 | Create project | Create a project for an existing owner. | Name and owner are required; owner must exist. | A valid request returns `201 Created`; an unknown owner returns `404 Not Found`. |
| RF-006 | Read projects | Retrieve one project or the complete project collection. | A requested project must exist. | Existing projects return `200 OK`; an unknown ID returns `404 Not Found`. |
| RF-007 | Update project | Change a project's name, description, status, or dates. | Start date cannot be after end date; owner is immutable in the MVP. | A valid update returns `200 OK`; invalid dates return `400 Bad Request`. |
| RF-008 | Delete project | Delete a project and its tasks. | Project deletion is an explicit cascading operation. | Deleting an existing project returns `204 No Content`; its tasks can no longer be retrieved. |

### Tasks

| ID | Name | Description | Business rules | Acceptance criteria |
| --- | --- | --- | --- | --- |
| RF-009 | Create task | Create a task in an existing project. | Project and title are required; assignee, when present, must exist; status and priority use the supported values. | A valid request returns `201 Created`; invalid references or enum values return the documented error. |
| RF-010 | Read tasks | Retrieve one task or a filtered task collection. | A requested task must exist; supplied filter values must be valid. | Existing tasks return `200 OK`; `GET /api/tasks` applies every supplied filter with AND semantics. |
| RF-011 | Update task | Change task data, including title, description, status, priority, assignee, and due date. | A task always remains in its original project in the MVP. | A valid update returns `200 OK`; an unknown assignee returns `404 Not Found`. |
| RF-012 | Delete task | Delete a task. | A requested task must exist. | Existing task deletion returns `204 No Content`; an unknown ID returns `404 Not Found`. |
| RF-013 | Manage task state | Track a task as `TODO`, `IN_PROGRESS`, or `DONE`. | New tasks default to `TODO`; any supported state transition is allowed in the MVP. | A task response exposes one supported status, and omitted creation status produces `TODO`. |
| RF-014 | Manage priority and due date | Track urgency and an optional due date. | Priority is `LOW`, `MEDIUM`, or `HIGH`; new tasks default to `MEDIUM`; due date is a calendar date. | Omitted priority produces `MEDIUM`; a supplied due date is returned unchanged as a date. |
