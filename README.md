# Taskify 🚀

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-blue.svg?logo=openjdk&logoColor=white)](https://openjdk.java.net/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green.svg?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg?logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue.svg?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-blue.svg?logo=docker&logoColor=white)](https://www.docker.com/)

## Project Overview ✨

Taskify is a robust and intuitive task management application built with Spring Boot. It provides a comprehensive set of
features to help users organize their tasks efficiently, manage task lists, and track progress. Designed with a focus on
clean architecture, security, and performance, Taskify aims to deliver a seamless experience for personal and team
productivity.

## Key Features 🚀

* **Task Management**: Create, read, update, and delete individual tasks.
* **Task List Organization**: Group tasks into logical lists for better organization.
* **Task Prioritization**: Assign priorities (LOW, MEDIUM, HIGH) to tasks.
* **Status Tracking**: Track task status (OPEN, CLOSED).
* **RESTful API**: A well-documented and intuitive RESTful API for seamless integration.
* **Global Exception Handling**: Centralized error handling for a consistent API response.
* **Database Migrations**: Schema management using Flyway for reliable database evolution.
* **Comprehensive Testing**: Unit and integration tests ensuring application reliability.
* **Caching**: Caching enabled for improved performance.

## Technologies Used 🛠️

* **Spring Boot**: Framework for building robust, production-ready Spring applications.
* **Java 21**: The latest LTS version of Java.
* **Spring Data JPA**: For simplified data access and persistence.
* **Hibernate**: ORM framework for database interaction.
* **PostgreSQL**: Relational database for data storage.
* **Flyway**: Database migration tool.
* **Maven**: Build automation tool.
* **JUnit 5 & Mockito**: For unit testing.
* **Testcontainers**: For integration testing with real database instances.
* **Swagger/OpenAPI**: For API documentation.

## Getting Started 🏁

Follow these instructions to get a copy of the project up and running on your local machine for development and testing
purposes.

### Prerequisites 📋

Before you begin, ensure you have the following installed:

* **Java Development Kit (JDK) 21** or higher
* **Maven 3.8.x** or higher
* **Docker Desktop** (for running PostgreSQL via `compose.yaml`)
* **Git**

### Installation ⚙️

1. **Clone the repository:**
   ```bash
   git clone https://github.com/nathsagar96/taskify.git
   cd taskify
   ```
2. **Start the PostgreSQL database using Docker Compose:**
   ```bash
   docker-compose up -d
   ```
   This will start a PostgreSQL container in the background.
3. **Build the project using Maven:**
   ```bash
   ./mvnw clean install
   ```

### Running the Application ▶️

After successful installation and database setup, you can run the Spring Boot application:

```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## API Documentation 📖

The API documentation is available via Swagger UI once the application is running.

* **Swagger UI**: Access the interactive API documentation at `http://localhost:8080/swagger-ui.html`

## Usage 💡

You can interact with the API using tools like Postman, curl, or any HTTP client. A Postman collection is provided for
easy testing.

### Postman Collection 📥

A Postman collection named [Taskify.postman_collection.json](Taskify.postman_collection.json) is included in the root
directory of this project. You can import this collection into Postman to quickly test all available endpoints.

**Key Endpoints:**

* **Task Lists**:
    * `GET /api/v1/tasklists`: Retrieve all task lists.
    * `GET /api/v1/tasklists/{task_list_id}`: Retrieve a specific task list by ID.
    * `POST /api/v1/tasklists`: Create a new task list.
    * `PUT /api/v1/tasklists/{task_list_id}`: Update an existing task list.
    * `DELETE /api/v1/tasklists/{task_list_id}`: Delete a task list.
* **Tasks**:
    * `GET /api/v1/tasklists/{task_list_id}/tasks`: Retrieve all tasks for a specific task list.
    * `GET /api/v1/tasklists/{task_list_id}/tasks/{task_id}`: Retrieve a specific task by ID within a task list.
    * `POST /api/v1/tasklists/{task_list_id}/tasks`: Create a new task within a task list.
    * `PUT /api/v1/tasklists/{task_list_id}/tasks/{task_id}`: Update an existing task within a task list.
    * `DELETE /api/v1/tasklists/{task_list_id}/tasks/{task_id}`: Delete a task within a task list.

## Contributing 🤝

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any
contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also
simply open an issue with the tag "enhancement".
Don't forget to give the project a star! ⭐ Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License 📄

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.
