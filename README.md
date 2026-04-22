# Taskify 🚀

## What is Taskify?

Taskify is a simple, production-ready task management REST API built with Spring Boot. It provides basic CRUD for tasks
and task lists, supports prioritization and status tracking, and is designed for easy extension and integration.

Key design goals:

- Clean, modular architecture
- Production-friendly defaults (database migrations, caching, tests)
- Well-covered by unit & integration tests

## Highlights

- Task and Task List management (CRUD)
- Priorities: LOW, MEDIUM, HIGH
- Status: OPEN, CLOSED
- RESTful API with consistent error handling
- Flyway for database migrations
- Testcontainers for integration tests
- Caching where appropriate

## Technologies

- Java 25
- Spring Boot 4.x
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway
- Maven
- JUnit 5, Mockito, Testcontainers
- Docker / Docker Compose

## Prerequisites

- Java 25 (or compatible JDK)
- Maven 3.8+
- Docker Desktop (for running PostgreSQL via compose)
- Git

## Quick start (development)

1. Clone the repository

```bash
git clone https://github.com/nathsagar96/taskify.git
cd taskify
```

2. Start the PostgreSQL service using Docker Compose

> The repository includes `compose.yaml` to start a PostgreSQL container. Adjust env vars if needed.

```bash
# run from project root (macOS / zsh)
docker compose -f compose.yaml up -d
# or if your Docker uses docker-compose
# docker-compose -f compose.yaml up -d
```

3. Build the application

```bash
./mvnw clean install
```

4. Run the application

```bash
./mvnw spring-boot:run
```

By default, the application starts on: http://localhost:8080

## Configuration

- Primary configuration files are in `src/main/resources`:
    - `application.yml` (base)
    - `application-dev.yml`, `application-prod.yml` (profiles)
- Database migrations are in `src/main/resources/db/migration` (Flyway)

Configure database connection and other environment-specific settings via the appropriate profile or environment
variables.

## API Endpoints (selected)

Base path: `/api/v1`

Task Lists

- GET /api/v1/tasklists — list all task lists
- GET /api/v1/tasklists/{task_list_id} — get a task list
- POST /api/v1/tasklists — create a task list
- PUT /api/v1/tasklists/{task_list_id} — update a task list
- DELETE /api/v1/tasklists/{task_list_id} — delete a task list

Tasks

- GET /api/v1/tasklists/{task_list_id}/tasks — list tasks in a task list
- GET /api/v1/tasklists/{task_list_id}/tasks/{task_id} — get a task
- POST /api/v1/tasklists/{task_list_id}/tasks — create a task
- PUT /api/v1/tasklists/{task_list_id}/tasks/{task_id} — update a task
- DELETE /api/v1/tasklists/{task_list_id}/tasks/{task_id} — delete a task

(The full API and DTOs are defined in the source `controllers` and `dtos` packages.)

## API Documentation

When running in development the project exposes Swagger/OpenAPI UI. Once the app runs, open:

```
http://localhost:8080/swagger-ui.html
```

## Running tests

Unit and integration tests are included. To run them with Maven:

```bash
./mvnw test
```

Integration tests use Testcontainers and may require Docker running.

## Code formatting

This project uses the DiffPlug Spotless Maven plugin to enforce consistent Java formatting (configured in `pom.xml`).

Commands:

```bash
# check formatting (fails build if code is not formatted)
./mvnw spotless:check

# automatically apply formatting changes
./mvnw spotless:apply
```

The plugin is configured to use Palantir Java Format. Run `spotless:apply` before committing to keep the repository
consistent.

## Docker / Production

This project uses Docker Compose for local development Postgres. For production, build a container image and deploy to
your environment. Example build:

```bash
./mvnw -DskipTests package
# build a Docker image
docker build -t taskify:latest .
```

Alternatively, create an OCI image using Spring Boot's built-in Buildpacks support (via the Maven plugin):

```bash
# build image using Maven wrapper (Cloud Native Buildpacks)
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=taskify:latest
```

This will produce a container image named `taskify:latest` (adjust name as needed).

## Contributing

Contributions are welcome. Typical workflow:

1. Fork the repository
2. Create a branch: `git checkout -b feature/your-feature`
3. Commit and push
4. Open a Pull Request

Please include tests and keep changes focused.

## Troubleshooting

- If the application fails to connect to Postgres, verify the container is running and env vars (host, port, username,
  password) match the application configuration.
- For failing integration tests, ensure Docker is available and Testcontainers can start containers.

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
