# 📝 Taskify

A robust Spring Boot REST API for managing tasks and task lists with full CRUD operations.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

## 📑 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Acknowledgements](#acknowledgements)

## ✨ Features

- Create, read, update, and delete task lists
- Manage tasks within task lists
- Mark tasks as complete
- Track completion percentage of task lists
- Priority-based task management
- Due date tracking for tasks
- Validation for all inputs
- PostgreSQL persistence
- Docker support for development and testing

## 🛠 Tech Stack

- Java 21
- Spring Boot 3.4.4
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven
- Lombok
- Testcontainers
- Jakarta Validation

## 🚀 Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- Docker and Docker Compose
- PostgreSQL (automatically handled via Docker)

### Installation

1. Clone the repository:

```bash
git clone https://github.com/nathsagar96/taskify.git
cd taskify
```

2. Build and run the application:

```bash
mvn clean install
mvn spring-boot:run
```

## 🔌 API Endpoints

### Task Lists

```
POST    /api/v1/tasklists           - Create a new task list
GET     /api/v1/tasklists           - Get all task lists
GET     /api/v1/tasklists/{id}      - Get a task list by ID
PUT     /api/v1/tasklists/{id}      - Update a task list
DELETE  /api/v1/tasklists/{id}      - Delete a task list
```

### Tasks

```
POST    /api/v1/tasklists/{taskListId}/tasks            - Create a new task
GET     /api/v1/tasklists/{taskListId}/tasks            - Get all tasks in a list
GET     /api/v1/tasklists/{taskListId}/tasks/{taskId}   - Get a task by ID
PUT     /api/v1/tasklists/{taskListId}/tasks/{taskId}   - Update a task
PATCH   /api/v1/tasklists/{taskListId}/tasks/{taskId}/complete - Mark task as complete
DELETE  /api/v1/tasklists/{taskListId}/tasks/{taskId}   - Delete a task
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📫 Contact

Sagar Nath - [@nathsagar96](https://github.com/nathsagar96)

## 🙏 Acknowledgements

- [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
- [Docker](https://www.docker.com/) - For containerization
- [Project Lombok](https://projectlombok.org/) - For reducing boilerplate code
