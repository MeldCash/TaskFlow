package com.giuss.taskflow.task.mapper;

import com.giuss.taskflow.task.dto.TaskResponse;
import com.giuss.taskflow.task.entity.Task;

public final class TaskMapper {
    private TaskMapper() { }

    public static TaskResponse toResponse(Task task) {
        Long assignedUserId = task.getAssignedUser() == null ? null : task.getAssignedUser().getId();
        return new TaskResponse(task.getId(), task.getProject().getId(), task.getTitle(), task.getDescription(),
                task.getStatus(), task.getPriority(), assignedUserId, task.getDueDate(),
                task.getCreatedAt(), task.getUpdatedAt());
    }
}
