# Complete Testing Guide for Task REST API

This guide provides step-by-step instructions for testing all API endpoints.

## Setup Before Testing

1. **Start MongoDB:**
```bash
# Linux/macOS
sudo systemctl start mongodb

# Or using brew (macOS)
brew services start mongodb-community

# Windows - MongoDB should be running as a service
```

2. **Start the Application:**
```bash
cd task1-rest-api
mvn spring-boot:run
```

3. **Verify Application is Running:**
```bash
curl http://localhost:8080/actuator/health
```

## Complete Test Suite with cURL

### Test 1: Create Tasks (PUT)

**Test 1.1 - Create First Task:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "task-001",
    "name": "Echo Hello World",
    "owner": "John Smith",
    "command": "echo Hello World from Task 1"
  }'
```

**Expected Response:**
```json
{
  "id": "task-001",
  "name": "Echo Hello World",
  "owner": "John Smith",
  "command": "echo Hello World from Task 1",
  "taskExecutions": []
}
```

**Test 1.2 - Create Second Task:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "task-002",
    "name": "List Directory",
    "owner": "Jane Doe",
    "command": "ls"
  }'
```

**Test 1.3 - Create Third Task:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "task-003",
    "name": "Print Current Date",
    "owner": "Bob Wilson",
    "command": "date"
  }'
```

**Test 1.4 - Create Fourth Task:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "task-004",
    "name": "Echo Greeting",
    "owner": "Alice Brown",
    "command": "echo Good Morning!"
  }'
```

### Test 2: Get All Tasks (GET)

**Test 2.1 - Retrieve All Tasks:**
```bash
curl -X GET http://localhost:8080/api/tasks
```

**Expected Response:** Array of all created tasks

**For Better Formatting (if you have jq installed):**
```bash
curl -X GET http://localhost:8080/api/tasks | jq '.'
```

### Test 3: Get Task by ID (GET)

**Test 3.1 - Get Existing Task:**
```bash
curl -X GET "http://localhost:8080/api/tasks?id=task-001"
```

**Expected Response:** Single task object with id "task-001"

**Test 3.2 - Get Non-Existent Task (Should Return 404):**
```bash
curl -X GET "http://localhost:8080/api/tasks?id=non-existent-id"
```

**Expected Response:** 404 Not Found with error message

### Test 4: Search Tasks by Name (GET)

**Test 4.1 - Search for "Echo":**
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name=Echo"
```

**Expected Response:** Array containing "Echo Hello World" and "Echo Greeting" tasks

**Test 4.2 - Search for "List":**
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name=List"
```

**Expected Response:** Array containing "List Directory" task

**Test 4.3 - Search for Non-Existent Name (Should Return 404):**
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name=NonExistent"
```

**Expected Response:** 404 Not Found

**Test 4.4 - Case Insensitive Search:**
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name=echo"
```

**Expected Response:** Should return same results as searching for "Echo"

### Test 5: Execute Tasks (PUT)

**Test 5.1 - Execute First Task:**
```bash
curl -X PUT http://localhost:8080/api/tasks/task-001/execute
```

**Expected Response:**
```json
{
  "startTime": "2025-10-18T...",
  "endTime": "2025-10-18T...",
  "output": "Hello World from Task 1"
}
```

**Test 5.2 - Execute Second Task:**
```bash
curl -X PUT http://localhost:8080/api/tasks/task-002/execute
```

**Expected Response:** TaskExecution with directory listing in output

**Test 5.3 - Execute Third Task:**
```bash
curl -X PUT http://localhost:8080/api/tasks/task-003/execute
```

**Expected Response:** TaskExecution with current date/time in output

**Test 5.4 - Execute Same Task Again (Multiple Executions):**
```bash
curl -X PUT http://localhost:8080/api/tasks/task-001/execute
```

**Expected Response:** New TaskExecution object

**Test 5.5 - Verify Multiple Executions Stored:**
```bash
curl -X GET "http://localhost:8080/api/tasks?id=task-001"
```

**Expected Response:** Task object with multiple entries in taskExecutions array

**Test 5.6 - Execute Non-Existent Task (Should Return 404):**
```bash
curl -X PUT http://localhost:8080/api/tasks/non-existent/execute
```

**Expected Response:** 404 Not Found

### Test 6: Security Validation Tests

**Test 6.1 - Attempt to Create Task with rm -rf:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "malicious-001",
    "name": "Delete Everything",
    "owner": "Hacker",
    "command": "rm -rf /"
  }'
```

**Expected Response:** 400 Bad Request with "unsafe/malicious code" error

**Test 6.2 - Attempt Command with Pipe:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "malicious-002",
    "name": "Chain Commands",
    "owner": "Hacker",
    "command": "ls || rm -rf /"
  }'
```

**Expected Response:** 400 Bad Request

**Test 6.3 - Attempt Command with Semicolon:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "malicious-003",
    "name": "Multiple Commands",
    "owner": "Hacker",
    "command": "echo test; rm -rf /"
  }'
```

**Expected Response:** 400 Bad Request

**Test 6.4 - Attempt Command Substitution:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "malicious-004",
    "name": "Command Substitution",
    "owner": "Hacker",
    "command": "echo $(whoami)"
  }'
```

**Expected Response:** 400 Bad Request

### Test 7: Delete Tasks (DELETE)

**Test 7.1 - Delete Existing Task:**
```bash
curl -X DELETE http://localhost:8080/api/tasks/task-004
```

**Expected Response:** Success message

**Test 7.2 - Verify Deletion:**
```bash
curl -X GET "http://localhost:8080/api/tasks?id=task-004"
```

**Expected Response:** 404 Not Found

**Test 7.3 - Attempt to Delete Non-Existent Task:**
```bash
curl -X DELETE http://localhost:8080/api/tasks/non-existent
```

**Expected Response:** 404 Not Found

**Test 7.4 - Verify Remaining Tasks:**
```bash
curl -X GET http://localhost:8080/api/tasks
```

**Expected Response:** Array with 3 tasks (task-004 should be gone)

### Test 8: Edge Cases

**Test 8.1 - Create Task with Empty Command:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "edge-001",
    "name": "Empty Command",
    "owner": "Test User",
    "command": ""
  }'
```

**Expected Response:** 400 Bad Request

**Test 8.2 - Create Task with Very Long Command:**
```bash
curl -X PUT http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "id": "edge-002",
    "name": "Long Command",
    "owner": "Test User",
    "command": "echo This is a very long command that should still work properly and not cause any issues with the system"
  }'
```

**Expected Response:** 200 OK with task created

**Test 8.3 - Search with Empty String:**
```bash
curl -X GET "http://localhost:8080/api/tasks/search?name="
```

**Expected Response:** All tasks or 404 (depending on implementation)

## Testing with Postman

### Setting Up Postman Collection

1. **Open Postman** and create a new collection named "Task API Tests"

2. **Set Base URL Variable:**
   - Click on the collection
   - Go to Variables tab
   - Add variable: `baseUrl` = `http://localhost:8080`

3. **Create Requests:**

#### Request 1: Create Task
- Name: Create Task
- Method: PUT
- URL: `{{baseUrl}}/api/tasks`
- Headers: `Content-Type: application/json`
- Body (raw JSON):
```json
{
  "id": "{{$randomUUID}}",
  "name": "Test Task",
  "owner": "{{$randomFullName}}",
  "command": "echo Hello from Postman"
}
```

#### Request 2: Get All Tasks
- Name: Get All Tasks
- Method: GET
- URL: `{{baseUrl}}/api/tasks`

#### Request 3: Get Task by ID
- Name: Get Task by ID
- Method: GET
- URL: `{{baseUrl}}/api/tasks?id=task-001`

#### Request 4: Search Tasks
- Name: Search Tasks by Name
- Method: GET
- URL: `{{baseUrl}}/api/tasks/search?name=Echo`

#### Request 5: Execute Task
- Name: Execute Task
- Method: PUT
- URL: `{{baseUrl}}/api/tasks/task-001/execute`

#### Request 6: Delete Task
- Name: Delete Task
- Method: DELETE
- URL: `{{baseUrl}}/api/tasks/task-001`

### Running Collection Tests

1. Click "Run" on the collection
2. Select all requests
3. Click "Run Task API Tests"
4. Review results

## Screenshot Requirements

For submission, capture screenshots showing:

1. **Terminal with your name/timestamp** - Running the application
2. **GET All Tasks** - Response with multiple tasks
3. **PUT Create Task** - Request and successful response
4. **GET Task by ID** - Single task retrieval
5. **GET Search by Name** - Search results
6. **PUT Execute Task** - Execution response with output
7. **DELETE Task** - Successful deletion
8. **MongoDB Data** - Optional: Show data in MongoDB Compass or CLI

### Adding Name/Timestamp to Screenshots

**Option 1 - Terminal Prompt:**
```bash
export PS1="\u@\h [\$(date '+%Y-%m-%d %H:%M:%S')] \w\$ "
```

**Option 2 - Echo Before Commands:**
```bash
echo "Testing by [Your Name] - $(date)"
curl -X GET http://localhost:8080/api/tasks
```

**Option 3 - Text Editor Window:**
- Open a text editor in a separate window
- Type your name and keep terminal clock visible
- Capture both in screenshot

## Verification Checklist

- [ ] MongoDB is running
- [ ] Application starts without errors
- [ ] Can create tasks
- [ ] Can retrieve all tasks
- [ ] Can retrieve single task by ID
- [ ] Can search tasks by name
- [ ] Can execute tasks and see output
- [ ] Can delete tasks
- [ ] Security validation rejects unsafe commands
- [ ] 404 returned for non-existent resources
- [ ] Task executions are stored in database
- [ ] Multiple executions of same task are recorded
- [ ] All screenshots include name and timestamp

## Common Issues and Solutions

### Issue: Connection Refused
**Solution:** Ensure MongoDB is running and application is started

### Issue: 404 on All Requests
**Solution:** Check if application is running on correct port (8080)

### Issue: Command Not Executing
**Solution:** Check command syntax for your OS (Windows vs Linux/Mac)

### Issue: Tasks Not Persisting
**Solution:** Verify MongoDB connection in application.properties

## Next Steps

After completing all tests:
1. Organize screenshots in `screenshots/` folder
2. Ensure README.md references all screenshots
3. Commit and push to GitHub
4. Verify repository structure matches requirements
5. Test cloning and running from fresh directory