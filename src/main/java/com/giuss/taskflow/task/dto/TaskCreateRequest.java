package com.giuss.taskflow.task.dto;

import com.giuss.taskflow.task.entity.TaskPriority;
import com.giuss.taskflow.task.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskCreateRequest(
        @NotNull(message = "projectId is required") @Positive(message = "projectId must be positive") Long projectId,
        @NotBlank(message = "title is required") @Size(max = 200, message = "title must not exceed 200 characters") String title,
        @Size(max = 4000, message = "description must not exceed 4000 characters")
        @Pattern(regexp = ".*\\S.*", message = "description must not be blank") String description,
        TaskStatus status,
        TaskPriority priority,
        @Positive(message = "assignedUserId must be positive") Long assignedUserId,
        LocalDate dueDate
) { }
