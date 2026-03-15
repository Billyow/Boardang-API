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

### User Repository Pattern

`IUserRepository` is a custom interface implemented by `JPAUserRepositoryImpl`, which also extends `IJPAUserRepository` (Spring Data JPA). This indirection keeps the service layer decoupled from JPA specifics.

### Task–Board Cross-DB Validation

`TaskServiceImpl` cross-references PostgreSQL entities (Board, BoardColumn) before writing to MongoDB. The `Task` document stores only Long IDs (`boardId`, `columnId`, `ownerId`) — there are no MongoDB references to PostgreSQL rows.

### API Routes

| Method | Path | Description |
|---|---|---|
| POST | `/api/v1/auth/login` | Login, returns JWT |
| POST | `/api/v1/auth/register` | Register new user |
| GET | `/api/v1/boards/getAllMyBoards` | Current user's boards |
| POST | `/api/v1/boards/create` | Create board |
| GET | `/api/v1/boards/{boardId}` | Get board with columns |
| DELETE | `/api/v1/boards/delete/{boardId}` | Delete board |
| POST | `/api/v1/boards/{boardId}/columns/create` | Create column in board |
| POST | `/api/v1/boards/{boardId}/tasks` | Create task |
| PUT | `/api/v1/boards/{boardId}/tasks/{taskId}/move` | Move task to another column |
| DELETE | `/api/v1/boards/{boardId}/tasks/{taskId}/delete` | Delete task |
| GET | `/user/{email}` | Find user by email (no cache) |
| GET | `/user/cache/{email}` | Find user by email (Redis cache) |

### MapStruct Mappers

Mappers are generated at compile time. When adding fields to entities/DTOs, update the corresponding `@Mapper` interface. The `TaskMapper` takes extra parameters (`ownerId`, `collaboratorsIds`) beyond the DTO — check `TaskMapper.toEntity()` signature before modifying.
