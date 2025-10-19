# Task 1: Java Backend and REST API

This is a Spring Boot REST API application for managing and executing tasks. Tasks represent shell commands that can be stored, retrieved, executed, and managed through RESTful endpoints.

## Project Structure

```
task1-rest-api/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/kaiburr/taskapp/
│   │   │       ├── model/
│   │   │       │   ├── Task.java
│   │   │       │   └── TaskExecution.java
│   │   │       ├── controller/
│   │   │       │   └── TaskController.java
│   │   │       ├── repository/
│   │   │       │   └── TaskRepository.java
│   │   │       ├── service/
│   │   │       │   └── TaskService.java
│   │   │       └── Application.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── com/kaiburr/taskapp/
│               └── TaskAPITests.java
│
├── pom.xml
├── README.md
├── screenshots/
│   ├── get-tasks.png
│   ├── add-task.png
│   ├── delete-task.png
│   └── find-task.png
└── .gitignore
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MongoDB 4.4+ (running locally or remotely)

## Installation and Setup

### 1. Install MongoDB

**On Ubuntu/Debian:**
```bash
sudo apt-get update
sudo apt-get install mongodb
sudo systemctl start mongodb
sudo systemctl enable mongodb
```

**On macOS:**
```bash
brew tap mongodb/brew
brew install mongodb-community
brew services start mongodb-community
```

**On Windows:**
Download and install from [MongoDB Official Website](https://www.mongodb.com/try/download/community)

### 2. Clone the Repository

```bash
git clone <your-repo-url>
cd task1-rest-api
```

### 3. Configure MongoDB Connection

Edit `src/main/resources/application.properties` if needed:

```properties
spring.data.mongodb.host=localhost
spring.data.mongodb.port=27017
spring.data.mongodb.database=kaiburr_tasks
```

### 4. Build the Project

```bash
mvn clean install
```

### 5. Run the Application

```bash
mvn spring-boot:run
```

Or run the JAR file:

```bash
java -jar target/task-app-1.0.0.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Get All Tasks

**Endpoint:** `GET /api/tasks`

**Description:** Returns all tasks from the database

**Example:**
```bash
curl -X GET http://localhost:8080/api/tasks
```

**Response:**
```json
[
  {
    "id": "123",
    "name": "Print Hello",
    "owner": "John Smith",
    "command": "echo Hello World",
    "taskExecutions": []
  }
]
```

### 2. Get Task by ID

**Endpoint:** `GET /api/tasks?id={taskId}`

**Description:** Returns a single task by ID or 404 if not found

**Example:**
```bash
curl -X GET "http://localhost:8080/api/tasks?id=123"
```

### 3. Create/Update Task

**Endpoint:** `PUT /api/tasks`

**Description:** Creates or updates a task. Validates the command for safety.

**Example:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "123",
    "name": "Print Hello",
    "owner": "John Smith",
    "command": "echo Hello World"
  }'
```

**Response:**
```json
{
  "id": "123",
  "name": "Print Hello",
  "owner": "John Smith",
  "command": "echo Hello World",
  "taskExecutions": []
}
```

### 4. Delete Task

**Endpoint:** `DELETE /api/tasks/{id}`

**Description:** Deletes a task by ID

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/tasks/123
```

### 5. Find Tasks by Name

**Endpoint:** `GET /api/tasks/search?name={searchString}`

**Description:** Searches for tasks containing the given string in their name (case-insensitive)

**Example:**
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name=Hello"
```

### 6. Execute Task

**Endpoint:** `PUT /api/tasks/{id}/execute`

**Description:** Executes the shell command associated with a task and stores the execution result

**Example:**
```bash
curl -X PUT http://localhost:8080/api/tasks/123/execute
```

**Response:**
```json
{
  "startTime": "2025-10-18T10:30:00.000Z",
  "endTime": "2025-10-18T10:30:01.000Z",
  "output": "Hello World"
}
```

## Security Features

The application validates commands to prevent execution of potentially dangerous operations:

- `rm -rf` commands
- `format` commands
- `mkfs` (filesystem creation)
- `dd` commands with device files
- Output redirection to device files
- Command chaining with `||` or `;`
- Command substitution with `$()` or backticks

## Testing

Run the unit tests:

```bash
mvn test
```

## Testing with Postman

### 1. Import Collection

Create a new Postman collection and add the following requests:

#### Create a Task
- **Method:** PUT
- **URL:** `http://localhost:8080/api/tasks`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "id": "1",
  "name": "Print Hello",
  "owner": "John Smith",
  "command": "echo Hello World"
}
```

#### Get All Tasks
- **Method:** GET
- **URL:** `http://localhost:8080/api/tasks`

#### Get Task by ID
- **Method:** GET
- **URL:** `http://localhost:8080/api/tasks?id=1`

#### Search Tasks by Name
- **Method:** GET
- **URL:** `http://localhost:8080/api/tasks/search?name=Hello`

#### Execute Task
- **Method:** PUT
- **URL:** `http://localhost:8080/api/tasks/1/execute`

#### Delete Task
- **Method:** DELETE
- **URL:** `http://localhost:8080/api/tasks/1`

## Testing with cURL

### Create a Task
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "1",
    "name": "List Files",
    "owner": "Jane Doe",
    "command": "ls -la"
  }'
```

### Get All Tasks
```bash
curl -X GET http://localhost:8080/api/tasks
```

### Get Task by ID
```bash
curl -X GET "http://localhost:8080/api/tasks?id=1"
```

### Search Tasks
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name=List"
```

### Execute Task
```bash
curl -X PUT http://localhost:8080/api/tasks/1/execute
```

### Delete Task
```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

## Sample Test Scenarios

### Scenario 1: Basic CRUD Operations

1. **Create multiple tasks:**
```bash
# Task 1
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "1",
    "name": "Echo Hello",
    "owner": "Alice",
    "command": "echo Hello World"
  }'

# Task 2
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "2",
    "name": "Print Date",
    "owner": "Bob",
    "command": "date"
  }'

# Task 3
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "3",
    "name": "Current Directory",
    "owner": "Charlie",
    "command": "pwd"
  }'
```

2. **Get all tasks:**
```bash
curl -X GET http://localhost:8080/api/tasks
```

3. **Search for tasks:**
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name=Print"
```

4. **Execute a task:**
```bash
curl -X PUT http://localhost:8080/api/tasks/1/execute
```

5. **Verify execution was stored:**
```bash
curl -X GET "http://localhost:8080/api/tasks?id=1"
```

6. **Delete a task:**
```bash
curl -X DELETE http://localhost:8080/api/tasks/3
```

### Scenario 2: Testing Security Validation

Try to create a task with unsafe command (should be rejected):

```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "unsafe",
    "name": "Malicious Task",
    "owner": "Hacker",
    "command": "rm -rf /"
  }'
```

Expected response: `400 Bad Request` with error message about unsafe command.

## Screenshots

### app_running
![app_running](screenshots/app_running.png)

### delete_method
![delete_method](screenshots/delete_method.png)

### empty_list
![empty_list](screenshots/empty_list.png)

### get_task
![get_task](screenshots/get_task.png)

### list_methods
![list_methods](screenshots/list_methods.png)

### mongo_connected
![mongo_connected](screenshots/mongo_connected.png)

### put_task
![put_task](screenshots/put_task.png)

### task_created
![task_created](screenshots/task_created.png)

## Troubleshooting

### MongoDB Connection Issues

If you see connection errors:

1. Verify MongoDB is running:
```bash
# On Linux/macOS
sudo systemctl status mongodb

# Or check if process is running
ps aux | grep mongod
```

2. Check if MongoDB port is accessible:
```bash
telnet localhost 27017
```

3. Verify connection string in `application.properties`

### Port Already in Use

If port 8080 is already in use, change it in `application.properties`:
```properties
server.port=8081
```

## Technologies Used

- **Java 17** - Programming language
- **Spring Boot 3.2.0** - Framework
- **Spring Data MongoDB** - Database integration
- **MongoDB** - NoSQL database
- **Maven** - Build tool
- **JUnit 5** - Testing framework

## API Documentation

For detailed API documentation, you can access Swagger UI (if configured) or refer to the endpoint descriptions above.

## Author

[Your Name]

## License

This project is created for Kaiburr LLC assessment.

## Notes

- Make sure to include screenshots with your name and timestamp as per submission requirements
- The application validates commands to prevent malicious operations
- All task executions are stored in the database with timestamps
- The API supports CORS for frontend integration