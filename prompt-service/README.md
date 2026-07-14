# Prompt Service

Prompt Service is a Spring Boot REST API for creating, updating, listing, checking, and deleting prompt templates. It uses SQLite for persistence, Bean Validation for request checks, and OpenAPI/Swagger for interactive API documentation.

## Features

- Create, retrieve, replace, patch, and delete prompts
- Filter prompts by tag and optional limit
- Validate required fields like `name` and `content`
- Return structured error responses for missing resources and validation failures
- Explore the API in Swagger UI

## Tech Stack

- Java 21
- Spring Boot 3.1
- Spring Web
- Spring Data JPA
- Bean Validation
- SQLite
- springdoc-openapi / Swagger UI

## Requirements

- Java 21
- Maven 3.9+ or the Maven Wrapper

## Run Locally

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application starts on `http://localhost:8080`.

## Swagger UI

After starting the application, open:

- `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## API Overview

Base path: `/api/prompts`

### Endpoints

- `POST /api/prompts` - Create a prompt
- `GET /api/prompts` - List prompts, optionally filter by `tag` and `limit`
- `GET /api/prompts/{id}` - Get a prompt by id
- `PUT /api/prompts/{id}` - Replace a prompt
- `PATCH /api/prompts/{id}` - Partially update a prompt
- `DELETE /api/prompts/{id}` - Delete a prompt
- `GET /api/prompts/{id}/exists` - Check whether a prompt exists

## Request Example

```json
{
  "name": "Write a project summary",
  "description": "Useful for internal documentation",
  "content": "Summarize the project goals in 3 bullets.",
  "tags": "writing,summary",
  "modelTarget": "gpt-4o-mini"
}
```

## Patch Example

```json
{
  "name": "Write a concise project summary"
}
```

## Validation Behavior

- Missing or blank required fields return `400 Bad Request`
- Invalid PATCH updates return a structured validation error payload
- Unknown prompt ids return `404 Not Found`

## Testing

Run the test suite with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

## Database

The app stores data in a local SQLite file named `prompt_manager.db` in the project root.

## Useful Links

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `http://localhost:8080/v3/api-docs`
