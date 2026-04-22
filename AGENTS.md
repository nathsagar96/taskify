# Taskify Agent Instructions

## Quick Start

1. Start PostgreSQL:
   ```bash
   docker compose -f compose.yaml up -d
   ```

2. Build and run:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## Key Commands

- **Build**: `./mvnw clean install`
- **Run**: `./mvnw spring-boot:run`
- **Test**: `./mvnw test` (requires Docker for integration tests)
- **Format**: `./mvnw spotless:apply` (Palantir Java Format)
- **Check format**: `./mvnw spotless:check`

## Architecture

- **Spring Boot 4.x** with Java 25
- **PostgreSQL** via Docker Compose (`compose.yaml`)
- **Flyway** for database migrations (`src/main/resources/db/migration`)
- **Testcontainers** for integration tests
- **Caffeine** caching for `taskLists` and `tasks`

## Configuration

- **Profiles**: `dev` (default), `prod`
- **Dev config**: `application-dev.yml` (connects to `localhost:5432/taskdb`)
- **Prod config**: `application-prod.yml` (uses env vars)
- **Database**: PostgreSQL with credentials `postgres/password`

## Testing

- **Unit tests**: Mockito-based
- **Integration tests**: Testcontainers with PostgreSQL
- **Test files**: `src/test/java/com/taskify/`

## Code Style

- **Formatter**: Palantir Java Format via Spotless Maven plugin
- **Run before commit**: `./mvnw spotless:apply`

## API

- **Base path**: `/api/v1`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html` (dev only)
- **Endpoints**: Task lists and tasks under `/api/v1/tasklists`

## Gotchas

- Integration tests require Docker running
- Flyway migrations in `src/main/resources/db/migration`
- Caching enabled for `taskLists` and `tasks` caches
- Virtual threads enabled in Spring Boot