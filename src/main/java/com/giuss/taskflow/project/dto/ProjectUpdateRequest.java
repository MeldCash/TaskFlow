package com.giuss.taskflow.project.dto;

import com.giuss.taskflow.project.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjectUpdateRequest(
        @NotBlank(message = "name is required") @Size(max = 150, message = "name must not exceed 150 characters") String name,
        @Size(max = 4000, message = "description must not exceed 4000 characters")
        @Pattern(regexp = ".*\\S.*", message = "description must not be blank") String description,
        @NotNull(message = "status is required") ProjectStatus status,
        LocalDate startDate,
        LocalDate endDate
) {
}
