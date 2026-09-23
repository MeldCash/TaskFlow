# REST API Contract

Base path: `/api`. All request and response bodies use JSON with `application/json`; dates use `YYYY-MM-DD`; timestamps use UTC ISO-8601 strings. Resource response examples omit server-managed timestamps only when brevity helps—the actual response DTO should include `createdAt` and `updatedAt`.

## Shared representations

### User

```json
{ "id": 1, "name": "Ada Lovelace", "email": "ada@example.com", "createdAt": "2026-09-23T12:00:00Z", "updatedAt": "2026-09-23T12:00:00Z" }
```

Create/update request: `{ "name": "Ada Lovelace", "email": "ada@example.com" }`.

### Project

```json
{ "id": 10, "name": "Website refresh", "description": "MVP delivery", "status": "ACTIVE", "ownerId": 1, "startDate": "2026-10-01", "endDate": null, "createdAt": "2026-09-23T12:00:00Z", "updatedAt": "2026-09-23T12:00:00Z" }
```

Create request requires `name` and `ownerId`; it may include `description`, `status`, `startDate`, and `endDate`. Update uses the same editable fields except `ownerId`, which is immutable.

### Task

```json
{ "id": 100, "projectId": 10, "title": "Publish landing page", "description": null, "status": "TODO", "priority": "HIGH", "assignedUserId": 1, "dueDate": "2026-10-10", "createdAt": "2026-09-23T12:00:00Z", "updatedAt": "2026-09-23T12:00:00Z" }
```

Create request requires `projectId` and `title`; it may include `description`, `status`, `priority`, `assignedUserId`, and `dueDate`. Omitted `status` defaults to `TODO`; omitted `priority` defaults to `MEDIUM`. Update uses the same editable fields except `projectId`, which is immutable.

## Users

| Method and URL | Description | Request body | Success response | Validation and errors |
| --- | --- | --- | --- | --- |
| `POST /api/users` | Create a user. | User create request. | `201 Created`, User, `Location` header. | Required trimmed name; valid unique email. `400`, `409`. |
| `GET /api/users/{id}` | Retrieve one user. | None. | `200 OK`, User. | Positive ID. `400`, `404`. |
| `GET /api/users` | List users. | None. | `200 OK`, array of Users. | `500` only for unexpected server failure. |
| `PUT /api/users/{id}` | Replace editable user data. | User update request. | `200 OK`, User. | Same as creation. `400`, `404`, `409`. |
| `DELETE /api/users/{id}` | Delete an eligible user. | None. | `204 No Content`. | `400`, `404`, `409` if the user owns projects or has assigned tasks. |

## Projects

| Method and URL | Description | Request body | Success response | Validation and errors |
| --- | --- | --- | --- | --- |
| `POST /api/projects` | Create a project. | Project create request. | `201 Created`, Project, `Location` header. | Required name and existing `ownerId`; valid dates/status. `400`, `404`. |
| `GET /api/projects` | List projects. | None. | `200 OK`, array of Projects. | `500` only for unexpected failure. |
| `GET /api/projects/{id}` | Retrieve one project. | None. | `200 OK`, Project. | Positive ID. `400`, `404`. |
| `PUT /api/projects/{id}` | Replace editable project data. | Project update request. | `200 OK`, Project. | Owner cannot change; name/status/dates valid. `400`, `404`. |
| `DELETE /api/projects/{id}` | Delete a project and its tasks. | None. | `204 No Content`. | Positive ID. `400`, `404`. |

## Tasks

| Method and URL | Description | Request body | Success response | Validation and errors |
| --- | --- | --- | --- | --- |
| `POST /api/tasks` | Create a task. | Task create request. | `201 Created`, Task, `Location` header. | Required title/project; existing project and optional assignee; valid enums/date. `400`, `404`. |
| `GET /api/tasks` | List tasks, optionally filtered. | None. | `200 OK`, array of Tasks. | See filters below. Invalid parameters return `400`. |
| `GET /api/tasks/{id}` | Retrieve one task. | None. | `200 OK`, Task. | Positive ID. `400`, `404`. |
| `PUT /api/tasks/{id}` | Replace editable task data. | Task update request. | `200 OK`, Task. | Project cannot change; same validations as creation. `400`, `404`. |
| `DELETE /api/tasks/{id}` | Delete a task. | None. | `204 No Content`. | Positive ID. `400`, `404`. |

### Task filters

`GET /api/tasks` accepts these optional query parameters. Multiple supplied filters are combined with AND semantics.

| Parameter | Type | Meaning | Example |
| --- | --- | --- | --- |
| `status` | `TODO`, `IN_PROGRESS`, `DONE` | Exact task status. | `?status=TODO` |
| `priority` | `LOW`, `MEDIUM`, `HIGH` | Exact task priority. | `?priority=HIGH` |
| `projectId` | positive integer | Tasks of one project. | `?projectId=10` |
| `assignedUserId` | positive integer | Tasks assigned to one user. | `?assignedUserId=1` |
| `dueDate` | ISO date | Tasks due on one calendar date. | `?dueDate=2026-10-10` |

Pagination and sorting are deliberately out of scope for this first contract and must be added before production-scale collection use.

## HTTP status policy

| Status | Use |
| --- | --- |
| `200 OK` | Successful read or update that returns a body. |
| `201 Created` | Successful creation; return the new resource and `Location` header. |
| `204 No Content` | Successful deletion. |
| `400 Bad Request` | Malformed JSON, invalid field values, unsupported enum values, invalid filters, or business input such as invalid date order. |
| `404 Not Found` | Requested resource or referenced resource does not exist. |
| `409 Conflict` | Uniqueness violation or a deletion forbidden by existing relationships. |
| `500 Internal Server Error` | Unexpected failure; never disclose stack traces or secrets. |

## Error response format

Every handled error returns this envelope. `fieldErrors` is included only for field validation failures.

```json
{
  "timestamp": "2026-09-23T12:00:00Z",
  "status": 404,
  "error": "RESOURCE_NOT_FOUND",
  "message": "Project with id 10 was not found",
  "path": "/api/projects/10"
}
```

Example validation extension:

```json
{
  "timestamp": "2026-09-23T12:00:00Z",
  "status": 400,
  "error": "VALIDATION_FAILED",
  "message": "Request validation failed",
  "path": "/api/users",
  "fieldErrors": [{ "field": "email", "message": "must be a valid email address" }]
}
```

Primary error codes: `VALIDATION_FAILED`, `MALFORMED_REQUEST`, `RESOURCE_NOT_FOUND`, `DUPLICATE_EMAIL`, `RESOURCE_CONFLICT`, and `INTERNAL_ERROR`.
