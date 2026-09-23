package com.giuss.taskflow.project.mapper;

import com.giuss.taskflow.project.dto.ProjectResponse;
import com.giuss.taskflow.project.entity.Project;

public final class ProjectMapper {
    private ProjectMapper() { }

    public static ProjectResponse toResponse(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getDescription(), project.getStatus(),
                project.getOwner().getId(), project.getStartDate(), project.getEndDate(),
                project.getCreatedAt(), project.getUpdatedAt());
    }
}
