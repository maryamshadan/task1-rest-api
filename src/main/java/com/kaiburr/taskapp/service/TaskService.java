package com.kaiburr.taskapp.service;

import com.kaiburr.taskapp.model.Task;
import com.kaiburr.taskapp.model.TaskExecution;
import com.kaiburr.taskapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(String id) {
        return taskRepository.findById(id);
    }

    public List<Task> searchTasksByName(String name) {
        return taskRepository.findByNameContainingIgnoreCase(name);
    }

    public Task createOrUpdateTask(Task task) {
        if (task.getCommand() == null || task.getCommand().trim().isEmpty()) {
            throw new IllegalArgumentException("Command cannot be empty");
        }

        // Security checks
        String cmd = task.getCommand();
        if (cmd.contains("rm -rf") || cmd.contains(";") || cmd.contains("||") || cmd.contains("$(")) {
            throw new IllegalArgumentException("Unsafe command detected");
        }

        return taskRepository.save(task);
    }

    public TaskExecution executeTask(String id) throws Exception {
        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            throw new RuntimeException("Task not found");
        }

        Task task = taskOpt.get();
        LocalDateTime start = LocalDateTime.now();

        ProcessBuilder builder = new ProcessBuilder();
        builder.command("bash", "-c", task.getCommand());
        builder.redirectErrorStream(true);

        Process process = builder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitCode = process.waitFor();
        LocalDateTime end = LocalDateTime.now();

        TaskExecution exec = new TaskExecution(start, end, output.toString());
        task.addTaskExecution(exec);
        taskRepository.save(task);

        return exec;
    }

    public void deleteTask(String id) {
        if (!taskRepository.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
