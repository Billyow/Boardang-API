# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build and run
./mvnw spring-boot:run

# Build JAR (skip tests)
./mvnw package -DskipTests

# Run tests
./mvnw test

# Run a single test class
./mvnw test -Dtest=BoardangApplicationTests
```

## Required Environment Variables

The app will not start without these:

| Variable | Description |
|---|---|
| `PG_URL` | JDBC URL for PostgreSQL (e.g. `jdbc:postgresql://localhost:5432/boardang`) |
| `PG_USER` | PostgreSQL username |
| `PG_PASSWORD` | PostgreSQL password |
| `MONGO_URI` | MongoDB connection URI |
| `JWT_SECRET` | Secret key for signing JWTs |

Optional: `SERVER_PORT` (8080), `REDIS_HOST` (localhost), `REDIS_PORT` (6379), `REDIS_PASSWORD` (empty), `REDIS_SSL` (false), `JWT_EXPIRATION` (86400000ms).

## Architecture

**Boardang** is a Kanban-style board API using a hybrid persistence model:
- **PostgreSQL + JPA**: `User`, `Board`, `BoardColumn` (relational data with foreign keys)
- **MongoDB**: `Task` documents (stores `boardId`, `columnId`, `ownerId` as Long references to PostgreSQL entities)
- **Redis**: Manual user caching via `UserCacheService` (key: `user:email:<email>`, TTL: 10 min)

### Package Structure

Each feature follows the pattern: `controller / DTO / mapper / model / repository / service`

```
com.billyow.app.boardang
├── auth/           JWT login+register, JwtService, JwtAuthenticationFilter, PrincipalUser
├── board/          Board CRUD (PostgreSQL entity)
├── boardColumn/    Column creation under a board (PostgreSQL entity)
├── task/           Task CRUD (MongoDB document), includes assembler/
├── user/           User entity, repo, service, cache
├── UserSearch/     UserSearchController (search by email, with/without Redis cache)
├── config/         CorsConfig, MongoConfig (@EnableMongoAuditing), RedisConfig
└── security/       SecurityConfig (Spring Security filter chain)
```

### Authentication Flow

1. `POST /api/v1/auth/register` or `POST /api/v1/auth/login` → returns JWT
2. All subsequent requests include `Authorization: Bearer <token>`
3. `JwtAuthenticationFilter` validates the token and sets a `PrincipalUser` into `SecurityContextHolder`
4. Services call `authService.getCurrentUserId()` to get the authenticated user's PostgreSQL ID

### Access Token Claims

The JWT access token payload includes the following claims:

| Claim | Type | Description |
|---|---|---|
| `sub` | String | User's email (JWT standard subject) |
| `type` | String | Always `"access"` |
| `userId` | Long | User's PostgreSQL ID |
| `name` | String | User's display name |
| `email` | String | User's email (explicit claim, mirrors `sub`) |
| `role` | String | User's role (e.g. `"USER"`, `"ADMIN"`) |

### User Repository Pattern

`IUserRepository` is a custom interface implemented by `JPAUserRepositoryImpl`, which also extends `IJPAUserRepository` (Spring Data JPA). This indirection keeps the service layer decoupled from JPA specifics.

### Task–Board Cross-DB Validation

`TaskServiceImpl` cross-references PostgreSQL entities (Board, BoardColumn) before writing to MongoDB. The `Task` document stores only Long IDs (`boardId`, `columnId`, `ownerId`) — there are no MongoDB references to PostgreSQL rows.

### API Routes

Public endpoints (`/api/v1/auth/**`) require no token. All others require `Authorization: Bearer <accessToken>`.

| Method | Path | Auth | Description |
|---|---|---|---|
| POST | `/api/v1/auth/register` | Public | Register new user |
| POST | `/api/v1/auth/login` | Public | Login, returns `accessToken` + `refreshToken` |
| POST | `/api/v1/auth/refresh` | Public | Refresh tokens using `refreshToken` |
| GET | `/api/v1/boards` | Bearer | Current user's boards |
| POST | `/api/v1/boards` | Bearer | Create board |
| GET | `/api/v1/boards/{boardId}` | Bearer | Get board with columns and tasks |
| DELETE | `/api/v1/boards/{boardId}` | Bearer | Delete board (owner only) |
| GET | `/api/v1/boards/{boardId}/members` | Bearer | Get board members |
| POST | `/api/v1/boards/{boardId}/members` | Bearer | Add member by email (owner only) |
| DELETE | `/api/v1/boards/{boardId}/members/{userId}` | Bearer | Remove member (owner only) |
| POST | `/api/v1/boards/{boardId}/columns` | Bearer | Create column in board |
| DELETE | `/api/v1/boards/{boardId}/columns/{columnId}` | Bearer | Delete column |
| PUT | `/api/v1/boards/{boardId}/columns/{columnId}` | Bearer | Update column title/position |
| GET | `/api/v1/boards/{boardId}/columns/count` | Bearer | Get column count for board |
| POST | `/api/v1/boards/{boardId}/tasks` | Bearer | Create task |
| PATCH | `/api/v1/boards/{boardId}/tasks/{taskId}` | Bearer | Update task fields (title, description, priority) |
| PUT | `/api/v1/boards/{boardId}/tasks/{taskId}/column` | Bearer | Move task to another column — body: `{ "newColumnId": Long }` |
| DELETE | `/api/v1/boards/{boardId}/tasks/{taskId}` | Bearer | Delete task |
| POST | `/api/v1/boards/{boardId}/tasks/{taskId}/collaborators/{collaboratorId}` | Bearer | Assign collaborator to task |
| DELETE | `/api/v1/boards/{boardId}/tasks/{taskId}/collaborators/{collaboratorId}` | Bearer | Unassign collaborator from task |
| GET | `/api/v1/tasks/me` | Bearer | Get all tasks assigned to current user |
| GET | `/user/get/{id}` | Public | Find user by ID |
| GET | `/user/{email}` | Public | Find user by email (no cache) |
| GET | `/user/cache/{email}` | Public | Find user by email (Redis cache) |

### MapStruct Mappers

Mappers are generated at compile time. When adding fields to entities/DTOs, update the corresponding `@Mapper` interface. The `TaskMapper.toEntity()` takes extra parameters beyond the DTO — `boardId`, `ownerId`, and `collaboratorsIds` — since these come from path variables or service logic, not the request body.
