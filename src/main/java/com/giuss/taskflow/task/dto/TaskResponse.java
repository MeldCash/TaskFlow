package com.giuss.taskflow.task.dto;

import com.giuss.taskflow.task.entity.TaskPriority;
import com.giuss.taskflow.task.entity.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;

public record TaskResponse(
        Long id, Long projectId, String title, String description, TaskStatus status, TaskPriority priority,
        Long assignedUserId, LocalDate dueDate, Instant createdAt, Instant updatedAt
) { }
