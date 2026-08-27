# Ticketing System API

A RESTful ticketing system built with Spring Boot, using the Action pattern for business logic, PostgreSQL for persistence, and Flyway for database migrations.

## Tech Stack

- **Java 21** / **Spring Boot 4.1.1**
- **PostgreSQL 16** (via Docker)
- **Flyway** for database migrations
- **Docker Compose** for containerization
- **JUnit 5 + Mockito** for unit testing

## How to Run

### Prerequisites
- Docker & Docker Compose installed

### Start the application
```bash
docker compose up
```

The API will be available at `http://localhost:8080`.

### Run tests
```bash
docker compose exec app mvn test
```

### Stop the application
```bash
docker compose down
```

### Reset database
```bash
docker compose down -v
docker compose up
```

## Authentication

This API uses a **header-based identity system** to simulate authentication. Every request must include the `X-User-Id` header with a valid user ID.

```
X-User-Id: 3
```

The user's role is looked up from the database and used to enforce permissions.

## Database

Three roles are seeded:

| ID | Name | Email | Role |
|----|------|-------|------|
| 1 | Ahmed Hassan | ahmed@ticketing.com | ADMIN |
| 2 | Sara Mohamed | sara@ticketing.com | AGENT |
| 3 | Omar Ali | omar@ticketing.com | USER |

## API Endpoints

### Categories

| Method | Endpoint | Description | Permission |
|--------|----------|-------------|------------|
| GET | `/api/categories` | List all categories | All |
| GET | `/api/categories/{id}` | Get category by ID | All |
| POST | `/api/categories` | Create category | All |
| PUT | `/api/categories/{id}` | Update category | All |
| DELETE | `/api/categories/{id}` | Delete category | All |

### Tickets

| Method | Endpoint | Description | Permission |
|--------|----------|-------------|------------|
| GET | `/api/tickets` | List tickets | ADMIN/AGENT: all, USER: own only |
| GET | `/api/tickets/{id}` | Get ticket by ID | ADMIN/AGENT: all, USER: own only |
| POST | `/api/tickets` | Create ticket | All |
| PATCH | `/api/tickets/{id}/status` | Update status | ADMIN: any, AGENT: assigned only |
| PATCH | `/api/tickets/{id}/assign` | Assign ticket | ADMIN only |
| PATCH | `/api/tickets/{id}/priority` | Update priority | ADMIN: any, AGENT: assigned only |
| POST | `/api/tickets/{id}/comments` | Add comment | ADMIN/AGENT: any, USER: own ticket only |
| GET | `/api/tickets/{id}/comments` | Get comments | ADMIN/AGENT: any, USER: own ticket only |

## Request Examples

### Create Ticket
```bash
curl -X POST http://localhost:8080/api/tickets \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 3" \
  -d '{
    "title": "Login page not loading",
    "description": "The login page returns a 500 error",
    "category_id": 1
  }'
```

### Update Status
```bash
curl -X PATCH http://localhost:8080/api/tickets/1/status \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 2" \
  -d '{"status": "IN_PROGRESS"}'
```

### Assign Ticket
```bash
curl -X PATCH http://localhost:8080/api/tickets/1/assign \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{"assignee_id": 2}'
```

### Update Priority
```bash
curl -X PATCH http://localhost:8080/api/tickets/1/priority \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 1" \
  -d '{"priority": "HIGH"}'
```

### Add Comment
```bash
curl -X POST http://localhost:8080/api/tickets/1/comments \
  -H "Content-Type: application/json" \
  -H "X-User-Id: 3" \
  -d '{"content": "Still experiencing this issue"}'
```

## Status Transitions

Tickets follow a strict state machine:

```
OPEN ──────────> IN_PROGRESS ──────────> RESOLVED ──────────> CLOSED
  │                    │                    │                    │
  │                    │                    │                    │
  └──> CLOSED          └──> OPEN            └──> REOPENED        └──> REOPENED
                                                  │                    │
                                                  │                    │
                                                  └──> CLOSED          └──> IN_PROGRESS
```

| Current Status | Allowed Next Status |
|----------------|---------------------|
| OPEN | IN_PROGRESS, CLOSED |
| IN_PROGRESS | RESOLVED, OPEN |
| RESOLVED | CLOSED, REOPENED |
| CLOSED | REOPENED |
| REOPENED | IN_PROGRESS |

Invalid transitions return `422 Unprocessable Entity`.

## Permission Rules

| Action | ADMIN | AGENT | USER |
|--------|-------|-------|------|
| Create ticket | Yes | Yes | Yes |
| View tickets | All | All | Own only |
| Update status | Any ticket | Assigned only | No |
| Update priority | Any ticket | Assigned only | No |
| Assign ticket | Yes | No | No |
| Add comment | Any ticket | Any ticket | Own ticket only |

## HTTP Status Codes

| Code | When |
|------|------|
| 200 | Success |
| 201 | Created |
| 400 | Missing header, invalid JSON, missing parameters |
| 404 | Resource not found |
| 409 | Duplicate resource (e.g., category name already exists) |
| 415 | Wrong Content-Type (must be application/json) |
| 422 | Validation error or invalid status transition |
| 403 | Permission denied |
| 500 | Unexpected server error |

## Architecture

### Action Pattern
Business logic lives in single-purpose `@Component` classes (one per operation) instead of service classes. Each action handles one use case:

```
Controller → Action → Repository → Database
```

### Project Structure
```
src/main/java/com/abdelrahman/ticketing/
├── action/
│   ├── category/        # CreateCategoryAction, UpdateCategoryAction, DeleteCategoryAction
│   └── ticket/          # CreateTicketAction, UpdateTicketStatusAction, AssignTicketAction, ...
├── controller/          # CategoryController, TicketController
├── dto/                 # Request/Response DTOs
├── entity/              # JPA entities + enums/
├── exception/           # GlobalExceptionHandler + custom exceptions
└── repository/          # Spring Data JPA repositories
```

### Database Migrations
Flyway manages the schema with versioned SQL files:

| Version | Description |
|---------|-------------|
| V1 | Users table |
| V2 | Categories table |
| V3 | Tickets table |
| V4 | Comments table |
| V5 | Ticket status histories table |
| V6 | Seed users |
| V7 | Seed categories |
| V8 | Seed tickets |

## Assumptions

1. **No real authentication** — User identity is passed via `X-User-Id` header. In production, this would come from a JWT token or session.
2. **AGENT manages assigned tickets only** — Per US-002 ("tickets assigned to me"), agents can only update status/priority of tickets assigned to them.
3. **USER sees only own tickets** — Users can only view and comment on tickets they created.
4. **Snake_case JSON** — All JSON field names use snake_case convention (e.g., `category_id`, `created_at`).
5. **Pre-seeded data** — The database comes with 3 users, 5 categories, and 5 sample tickets.
