# Task 1: Java Backend and REST API

Simple Spring Boot REST API to store, list, execute and delete shell-command tasks.

## Project layout
- src/main/java/com/kaiburr/taskapp
  - model/Task.java
  - model/TaskExecution.java
  - controller/TaskController.java
  - repository/TaskRepository.java
  - service/TaskService.java
  - Application.java
- src/main/resources/application.properties
- screenshots/
- pom.xml

## Prerequisites
- Java 17+
- Maven 3.6+
- MongoDB (running locally or remotely)
- Git (to push to GitHub)

## Quick start (Windows PowerShell)
1. Start MongoDB
```powershell
# if installed as a service
net start MongoDB

# or run manually (adjust dbpath)
& "C:\Program Files\MongoDB\Server\6.0\bin\mongod.exe" --dbpath "C:\data\db"
```

2. Build and run
```powershell
mvn clean package
mvn spring-boot:run
```
App will run on http://localhost:8080 by default.

## Required JSON fields
When creating a task, include the "command" field. Example:
```json
{
  "name": "Test Task",
  "description": "My first task",
  "command": "echo hello"
}
```

## API endpoints
- Create / Update
  - PUT /api/tasks
  - PowerShell:
    ```powershell
    Invoke-RestMethod -Uri "http://127.0.0.1:8080/api/tasks" -Method Put -ContentType "application/json" -Body '{"name":"Test Task","description":"My first task","command":"echo hello"}'
    ```
  - curl.exe:
    ```powershell
    curl.exe -X PUT "http://127.0.0.1:8080/api/tasks" -H "Content-Type: application/json" -d "{\"name\":\"Test Task\",\"description\":\"My first task\",\"command\":\"echo hello\"}"
    ```

- List all
  - GET /api/tasks
  - Example:
    ```powershell
    Invoke-RestMethod -Uri "http://127.0.0.1:8080/api/tasks" -Method Get | ConvertTo-Json -Depth 5
    ```

- Get by id
  - GET /api/tasks?id={id}

- Execute
  - PUT /api/tasks/{id}/execute

- Delete
  - DELETE /api/tasks/{id}

## Screenshots
Place screenshots in `screenshots/` so they display on GitHub. Example files:
- screenshots/add-task.png — show PUT response (200 + JSON)
- screenshots/get-tasks.png — show GET /api/tasks output (array with created task)
- screenshots/delete-task.png — show DELETE response
- screenshots/find-task.png — show GET by id or search result

Open screenshots locally:
```powershell
ii .\screenshots\get-tasks.png
ii .\screenshots
```

Save API output for screenshot:
```powershell
# raw response text (use Invoke-WebRequest to capture raw Content)
$response = Invoke-WebRequest -Uri "http://127.0.0.1:8080/api/tasks" -Method Get
$response.Content > tasks.json
notepad tasks.json
```

## Push to GitHub (one-time)
```powershell
git init
git add .
git commit -m "Initial commit - task1-rest-api"
git remote add origin <your-repo-url>
git branch -M main
git push -u origin main
```
If repo already initialized:
```powershell
git add .
git commit -m "Update README and add screenshots"
git push
```

## Troubleshooting
- "Command cannot be empty": include the `command` property in JSON.
- MongoDB connection: verify mongod is running and application.properties is correct.
- Port in use: set `server.port` in `src/main/resources/application.properties`.

## Testing
Run unit tests:
```bash
mvn test
```

## Notes
- The app validates command strings to block dangerous commands.
- Include your screenshots in the `screenshots/` folder before pushing so GitHub displays them.
```// filepath: c:\Users\HP\Documents\task1-rest-api\task1-rest-api\README.md
# Task 1: Java Backend and REST API

Simple Spring Boot REST API to store, list, execute and delete shell-command tasks.

## Project layout
- src/main/java/com/kaiburr/taskapp
  - model/Task.java
  - model/TaskExecution.java
  - controller/TaskController.java
  - repository/TaskRepository.java
  - service/TaskService.java
  - Application.java
- src/main/resources/application.properties
- screenshots/
- pom.xml

## Prerequisites
- Java 17+
- Maven 3.6+
- MongoDB (running locally or remotely)
- Git (to push to GitHub)

## Quick start (Windows PowerShell)
1. Start MongoDB
```powershell
# if installed as a service
net start MongoDB

# or run manually (adjust dbpath)
& "C:\Program Files\MongoDB\Server\6.0\bin\mongod.exe" --dbpath "C:\data\db"
```

2. Build and run
```powershell
mvn clean package
mvn spring-boot:run
```
App will run on http://localhost:8080 by default.

## Required JSON fields
When creating a task, include the "command" field. Example:
```json
{
  "name": "Test Task",
  "description": "My first task",
  "command": "echo hello"
}
```

## API endpoints
- Create / Update
  - PUT /api/tasks
  - PowerShell:
    ```powershell
    Invoke-RestMethod -Uri "http://127.0.0.1:8080/api/tasks" -Method Put -ContentType "application/json" -Body '{"name":"Test Task","description":"My first task","command":"echo hello"}'
    ```
  - curl.exe:
    ```powershell
    curl.exe -X PUT "http://127.0.0.1:8080/api/tasks" -H "Content-Type: application/json" -d "{\"name\":\"Test Task\",\"description\":\"My first task\",\"command\":\"echo hello\"}"
    ```

- List all
  - GET /api/tasks
  - Example:
    ```powershell
    Invoke-RestMethod -Uri "http://127.0.0.1:8080/api/tasks" -Method Get | ConvertTo-Json -Depth 5
    ```

- Get by id
  - GET /api/tasks?id={id}

- Execute
  - PUT /api/tasks/{id}/execute

- Delete
  - DELETE /api/tasks/{id}

## Screenshots
Place screenshots in `screenshots/` so they display on GitHub. Example files:
- screenshots/add-task.png — show PUT response (200 + JSON)
- screenshots/get-tasks.png — show GET /api/tasks output (array with created task)
- screenshots/delete-task.png — show DELETE response
- screenshots/find-task.png — show GET by id or search result

Open screenshots locally:
```powershell
ii .\screenshots\get-tasks.png
ii .\screenshots
```

Save API output for screenshot:
```powershell
# raw response text (use Invoke-WebRequest to capture raw Content)
$response = Invoke-WebRequest -Uri "http://127.0.0.1:8080/api/tasks" -Method Get
$response.Content > tasks.json
notepad tasks.json
```

## Push to GitHub (one-time)
```powershell
git init
git add .
git commit -m "Initial commit - task1-rest-api"
git remote add origin <your-repo-url>
git branch -M main
git push -u origin main
```
If repo already initialized:
```powershell
git add .
git commit -m "Update README and add screenshots"
git push
```

## Troubleshooting
- "Command cannot be empty": include the `command` property in JSON.
- MongoDB connection: verify mongod is running and application.properties is correct.
- Port in use: set `server.port` in `src/main/resources/application.properties`.

## Testing
Run unit tests:
```bash
mvn test
```

## Notes
- The app validates command strings to block dangerous commands.
- Include your screenshots in the `screenshots/` folder before pushing so GitHub displays them.