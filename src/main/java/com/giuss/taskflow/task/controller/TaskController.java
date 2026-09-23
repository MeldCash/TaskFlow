package com.giuss.taskflow.task.controller;

import com.giuss.taskflow.task.dto.TaskCreateRequest;
import com.giuss.taskflow.task.dto.TaskResponse;
import com.giuss.taskflow.task.dto.TaskUpdateRequest;
import com.giuss.taskflow.task.entity.TaskPriority;
import com.giuss.taskflow.task.entity.TaskStatus;
import com.giuss.taskflow.task.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Validated
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskCreateRequest request) {
        TaskResponse response = taskService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<TaskResponse> findAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) @Positive Long projectId,
            @RequestParam(required = false) @Positive Long assignedUserId,
            @RequestParam(required = false) LocalDate dueDate
    ) {
        return taskService.findAll(status, priority, projectId, assignedUserId, dueDate);
    }

    @GetMapping("/{id}")
    public TaskResponse findById(@PathVariable @Positive Long id) { return taskService.findById(id); }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable @Positive Long id, @Valid @RequestBody TaskUpdateRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
