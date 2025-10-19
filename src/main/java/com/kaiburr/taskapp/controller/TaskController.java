// ...existing code...
package com.kaiburr.taskapp.controller;

import com.kaiburr.taskapp.model.Task;
import com.kaiburr.taskapp.model.TaskExecution;
import com.kaiburr.taskapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class RootController {

    @GetMapping("/")
    public String index() {
        return "Backend is up and running! Visit /api/tasks to access the Task API.";
    }
}

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Root mapping for testing
    @GetMapping("/")
    public String home() {
        return "Task API is running!";
    }

    @PutMapping
    public ResponseEntity<?> createOrUpdateTask(@RequestBody Task task) {
        try {
            Task saved = taskService.createOrUpdateTask(task);
            return ResponseEntity.ok(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getTasks(@RequestParam(required = false) String id) {
        if (id != null) {
            return taskService.getTaskById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.ok(taskService.getAllTasks());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchTasks(@RequestParam String name) {
        List<Task> tasks = taskService.searchTasksByName(name);
        if (tasks.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}/execute")
    public ResponseEntity<?> executeTask(@PathVariable String id) {
        try {
            TaskExecution result = taskService.executeTask(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
// ...existing code...