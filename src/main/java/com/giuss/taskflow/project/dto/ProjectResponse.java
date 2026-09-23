package com.giuss.taskflow.project.dto;

import com.giuss.taskflow.project.entity.ProjectStatus;

import java.time.Instant;
import java.time.LocalDate;

public record ProjectResponse(
        Long id, String name, String description, ProjectStatus status, Long ownerId,
        LocalDate startDate, LocalDate endDate, Instant createdAt, Instant updatedAt
) {
}
