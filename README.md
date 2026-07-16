# Prompt Management System

A two-service Spring Boot microservice system for creating, managing, and reviewing AI prompts.

---

## Services Overview

| Service | Port | Storage | Spring Boot |
|---------|------|---------|-------------|
| `prompt-service` | `8000` | SQLite (`prompt_manager.db`) | 3.1.0 / Java 21 |
| `prompt-review-service` | `8001` | Flat JSON files (`/reviews/`) | 3.3.5 / Java 17 |

```
┌─────────────────────────┐        RestTemplate        ┌────────────────────────────┐
│     prompt-service      │ ◄────────────────────────── │   prompt-review-service    │
│   localhost:8000        │   GET /api/prompts/{id}     │   localhost:8001           │
│   SQLite database       │                             │   JSON file storage        │
└─────────────────────────┘                             └────────────────────────────┘
```

---

## Initial Setup Guide

Follow this guide to set up the repository for the first time.

### 1. Prerequisites

Ensure you have the following installed on your machine:
- **Java Development Kit (JDK)**: JDK 17 or JDK 21 (Note: `prompt-service` uses JDK 21; `prompt-review-service` uses JDK 17). Ensure your `JAVA_HOME` environment variable points to your JDK installation.
- **Maven**: (Optional) Maven 3.8+ is recommended, though both services contain the Maven Wrapper (`mvnw` / `mvnw.cmd`) to run Maven tasks without a global installation.
- **HTTP Client**: cURL, Postman, or PowerShell for testing API endpoints.

---

### 2. IDE Setup & Lombok Annotation Processing

This repository uses **Lombok** to reduce boilerplate code (e.g., automatically generating getters, setters, builders, and constructors). To avoid IDE compilation errors like "Cannot find symbol builder()", configure annotation processing in your IDE:

#### For IntelliJ IDEA
1. Open IntelliJ IDEA and import/open the root folder or individual projects (`prompt-service` and `prompt-review-service`) as Maven projects.
2. Open Settings/Preferences (`Ctrl + Alt + S` on Windows/Linux, `Cmd + ,` on macOS).
3. Navigate to **Build, Execution, Deployment > Compiler > Annotation Processors**.
4. Check the box **"Enable annotation processing"**.
5. Ensure the Lombok plugin is installed and enabled (bundled by default in recent IntelliJ versions).
6. Click **Apply** and then **OK**.

#### For VS Code
1. Install the **Extension Pack for Java** (by Microsoft).
2. Install the **Lombok Annotations Support for VS Code** (by Gabrielbb).
3. VS Code will automatically detect Lombok and configure compilation.

---

### 3. Initial Build & Dependency Resolution

Before launching the applications, compile and package the services to download all necessary dependencies and verify that Lombok classes generate successfully.

#### On Windows (PowerShell or Command Prompt)
```powershell
# Build prompt-service
cd prompt-service
.\mvnw.cmd clean install -DskipTests
cd ..

# Build prompt-review-service
cd prompt-review-service
.\mvnw.cmd clean install -DskipTests
cd ..
```

#### On Linux or macOS
```bash
# Build prompt-service
cd prompt-service
chmod +x mvnw
./mvnw clean install -DskipTests
cd ..

# Build prompt-review-service
cd prompt-review-service
chmod +x mvnw
./mvnw clean install -DskipTests
cd ..
```

---

### 4. Database Setup

No external database installation (like PostgreSQL or MySQL) is required:
- `prompt-service` uses an embedded **SQLite** database (`prompt_manager.db`).
- Hibernate is configured to automatically create and update the database schema (`spring.jpa.hibernate.ddl-auto=update`) upon starting the service.
- `prompt-review-service` uses local flat JSON file storage (automatically created under the `reviews/` directory).

---

### 5. Running the Services

Both services are independent Spring Boot applications. Always start `prompt-service` first because `prompt-review-service` validates prompt existence with it.

#### Step 1: Start Prompt Service
Runs on **http://localhost:8000**
```bash
cd prompt-service
# On Windows: .\mvnw.cmd spring-boot:run
# On Linux/macOS: ./mvnw spring-boot:run
```

#### Step 2: Start Prompt Review Service
Runs on **http://localhost:8001**
```bash
cd prompt-review-service
# On Windows: .\mvnw.cmd spring-boot:run
# On Linux/macOS: ./mvnw spring-boot:run
```

---

### 6. Verifying the Setup (Smoke Test)

Once both services are running, verify their communication by running the following commands.

#### Step A: Create a Prompt in `prompt-service`
This creates a new prompt and returns its UUID.

**Using PowerShell (Windows):**
```powershell
$body = @{
  name = "Write a concise project summary"
  description = "Used for internal documentation drafts"
  content = "Summarize the project goals in 3 bullets."
  tags = "writing,summary,internal"
  modelTarget = "gpt-4o-mini"
} | ConvertTo-Json -Depth 5
$prompt = Invoke-RestMethod -Uri "http://localhost:8000/api/prompts" -Method Post -Body $body -ContentType "application/json"
$prompt
```

**Using cURL (Linux/macOS):**
```bash
curl -X POST http://localhost:8000/api/prompts \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Write a concise project summary",
    "description": "Used for internal documentation drafts",
    "content": "Summarize the project goals in 3 bullets.",
    "tags": "writing,summary,internal",
    "modelTarget": "gpt-4o-mini"
  }'
```

#### Step B: Review the Prompt in `prompt-review-service`
This submits a review referencing the prompt ID created in Step A.

**Using PowerShell (Windows):**
```powershell
$reviewBody = @{
  promptId = $prompt.id
  reviewerName = "Abuzar"
  score = 5
  feedback = "Excellent prompt, very clear instructions."
} | ConvertTo-Json -Depth 5
Invoke-RestMethod -Uri "http://localhost:8001/api/reviews" -Method Post -Body $reviewBody -ContentType "application/json"
```

**Using cURL (Linux/macOS):**
*(Replace `<PROMPT_UUID>` with the exact ID returned in Step A)*
```bash
curl -X POST http://localhost:8001/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "promptId": "<PROMPT_UUID>",
    "reviewerName": "Abuzar",
    "score": 5,
    "feedback": "Excellent prompt, very clear instructions."
  }'
```

---

## Swagger UI (Interactive Docs)

| Service | Swagger UI URL | OpenAPI JSON |
|---------|---------------|--------------|
| Prompt Service | http://localhost:8000/swagger-ui.html | http://localhost:8000/v3/api-docs |
| Review Service | http://localhost:8001/swagger-ui.html | http://localhost:8001/api-docs |

---

## API Reference

### Prompt Service — `/api/prompts`

#### Create a Prompt
```
POST /api/prompts
```
**Request body:**
```json
{
  "name": "Write a concise project summary",
  "description": "Used for internal documentation drafts",
  "content": "Summarize the project goals in 3 bullets.",
  "tags": "writing,summary,internal",
  "modelTarget": "gpt-4o-mini"
}
```
**Response:** `201 Created` — returns the saved prompt with auto-generated `id`, `createdAt`, `updatedAt`.

---

#### Get a Prompt by ID
```
GET /api/prompts/{id}
```
**Response:** `200 OK` or `404 Not Found`

---

#### List All Prompts
```
GET /api/prompts
GET /api/prompts?tag=writing
GET /api/prompts?tag=writing&limit=5
```

---

#### Update a Prompt (full replace)
```
PUT /api/prompts/{id}
```

#### Partially Update a Prompt
```
PATCH /api/prompts/{id}
```
**Request body:** any subset of fields, e.g.:
```json
{ "modelTarget": "gpt-4o" }
```

#### Delete a Prompt
```
DELETE /api/prompts/{id}
```
**Response:** `204 No Content`

#### Check Prompt Existence
```
GET /api/prompts/{id}/exists
```
**Response:**
```json
{ "exists": true }
```

---

### Prompt Review Service — `/api/reviews`

> The review service validates that the referenced prompt exists in `prompt-service` before saving any review.

#### Create a Review
```
POST /api/reviews
```
**Request body:**
```json
{
  "promptId": "030bd797-48b8-4ea6-a70f-c438f85740da",
  "reviewerName": "Abuzar",
  "score": 5,
  "feedback": "Excellent prompt, very clear instructions."
}
```

| Field | Type | Constraints |
|-------|------|------------|
| `promptId` | UUID | Required — must exist in prompt-service |
| `reviewerName` | String | Required, non-blank |
| `score` | Integer | Required, 1–5 |
| `feedback` | String | Required, non-blank |

**Response:** `201 Created` — returns the full `Review` object including `id`, `promptSnapshot`, and `reviewedAt`.

---

#### List Reviews
```
GET /api/reviews                          # all reviews
GET /api/reviews?promptId={uuid}          # filtered by prompt
```

#### Get a Review by ID
```
GET /api/reviews/{id}
```

#### Get Review Summary for a Prompt
```
GET /api/reviews/{promptId}/summary
```
**Response:**
```json
{
  "promptId": "030bd797-48b8-4ea6-a70f-c438f85740da",
  "totalReviews": 3,
  "averageScore": 4.33,
  "feedback": ["Great", "Very useful", "Excellent prompt"]
}
```

---

## Data Models

### Prompt (prompt-service)

| Field | Type | Notes |
|-------|------|-------|
| `id` | UUID | Auto-generated |
| `name` | String | Required |
| `description` | String | Optional |
| `content` | String | Required |
| `tags` | String | Comma-separated, e.g. `"writing,summary"` |
| `modelTarget` | String | e.g. `"gpt-4o-mini"` |
| `createdAt` | ISO-8601 datetime | Auto-set |
| `updatedAt` | ISO-8601 datetime | Auto-set |

### Review (prompt-review-service)

| Field | Type | Notes |
|-------|------|-------|
| `id` | UUID | Auto-generated |
| `promptId` | UUID | References a prompt |
| `promptSnapshot` | String | Copy of prompt `content` at review time |
| `reviewerName` | String | |
| `score` | Integer | 1–5 |
| `feedback` | String | |
| `reviewedAt` | ISO-8601 datetime | Auto-set |

---

## Error Responses

All errors follow this structure:

```json
{
  "timestamp": "2026-07-15T09:43:25.387",
  "status": 404,
  "error": "Not Found",
  "message": "Prompt not found with id: ...",
  "path": "/api/prompts/...",
  "validationErrors": null
}
```

| Status | Cause |
|--------|-------|
| `400` | Validation failed — `validationErrors` field will be populated |
| `404` | Resource not found (prompt or review) |
| `500` | Unhandled server error |
| `503` | prompt-service is unreachable (review service only) |

---

## Project Structure

```
prompt-service-p2/
├── prompt-service/                        # Prompt CRUD service
│   ├── src/main/java/com/ats/prompt_service/
│   │   ├── PromptServiceApplication.java
│   │   ├── controller/PromptController.java
│   │   ├── service/PromptService.java (+ impl)
│   │   ├── entity/Prompt.java
│   │   ├── dto/response/PromptResponse.java
│   │   └── exception/
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
├── prompt-review-service/                 # Review service
│   ├── src/main/java/com/ats/prompt_review_service/prompt_review/
│   │   ├── PromptReviewApplication.java
│   │   ├── controller/ReviewController.java
│   │   ├── service/ReviewService.java (+ impl)
│   │   ├── repository/ReviewFileRepository.java
│   │   ├── client/PromptServiceClient.java
│   │   ├── config/RestClientConfig.java
│   │   ├── entity/Review.java
│   │   ├── dto/request/CreateReviewRequest.java
│   │   ├── dto/response/PromptResponse.java
│   │   ├── dto/response/ReviewSummaryResponse.java
│   │   └── exception/
│   ├── reviews/                           # JSON file storage (auto-created)
│   ├── src/main/resources/application.properties
│   └── pom.xml
│
└── README.md
```

---

## Tech Stack

| Technology | Used in | Purpose |
|-----------|---------|---------|
| Spring Boot 3.x | Both | Application framework |
| Spring Web | Both | REST API layer |
| Spring Data JPA | prompt-service | ORM / database access |
| Spring Validation | Both | Bean validation (`@NotNull`, `@Min`, etc.) |
| SQLite + Hibernate | prompt-service | Lightweight embedded database |
| Jackson + JavaTimeModule | prompt-review-service | JSON file serialization with `LocalDateTime` |
| RestTemplate | prompt-review-service | HTTP calls to prompt-service |
| SpringDoc OpenAPI 2.x | Both | Swagger UI |
| Lombok | Both | Boilerplate reduction |
| Spring DevTools | prompt-review-service | Hot reload during development |

---

## Configuration Reference

### prompt-service `application.properties`
```properties
server.port=8000
spring.datasource.url=jdbc:sqlite:prompt_manager.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jackson.serialization.write-dates-as-timestamps=false
```

### prompt-review-service `application.properties`
```properties
server.port=8001
review.storage.path=reviews
prompt.service.url=http://localhost:8000/api/prompts
spring.jackson.deserialization.fail-on-unknown-properties=false
```

---

## Common Mistakes

| Mistake | Symptom | Fix |
|---------|---------|-----|
| Using the wrong port in Swagger | `Failed to fetch` in Swagger UI | Ensure `@Server(url = "http://localhost:800X")` matches `server.port` |
| Sending a non-existent `promptId` to review service | `500 Internal Server Error` | Only use prompt IDs returned by a successful `POST /api/prompts` |
| Sending UUID without dashes | Parsing error | Always use the full UUID format: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` |
| Calling review service endpoint expecting prompt data | `404 Not Found` | The review service only stores reviews — fetch prompts from `localhost:8000` |
