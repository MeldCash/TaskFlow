package com.giuss.taskflow.project.service;

import com.giuss.taskflow.common.exception.ResourceNotFoundException;
import com.giuss.taskflow.project.dto.ProjectCreateRequest;
import com.giuss.taskflow.project.dto.ProjectResponse;
import com.giuss.taskflow.project.dto.ProjectUpdateRequest;
import com.giuss.taskflow.project.entity.Project;
import com.giuss.taskflow.project.entity.ProjectStatus;
import com.giuss.taskflow.project.mapper.ProjectMapper;
import com.giuss.taskflow.project.repository.ProjectRepository;
import com.giuss.taskflow.user.entity.TaskflowUser;
import com.giuss.taskflow.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Transactional
    public ProjectResponse create(ProjectCreateRequest request) {
        validateDateRange(request.startDate(), request.endDate());
        TaskflowUser owner = userService.findEntityById(request.ownerId());
        Project project = new Project(request.name().trim(), normalizeDescription(request.description()),
                request.status() == null ? ProjectStatus.ACTIVE : request.status(), owner,
                request.startDate(), request.endDate());
        return ProjectMapper.toResponse(projectRepository.saveAndFlush(project));
    }

    public List<ProjectResponse> findAll() {
        return projectRepository.findAll().stream().map(ProjectMapper::toResponse).toList();
    }

    public ProjectResponse findById(Long id) {
        return ProjectMapper.toResponse(findEntityById(id));
    }

    @Transactional
    public ProjectResponse update(Long id, ProjectUpdateRequest request) {
        validateDateRange(request.startDate(), request.endDate());
        Project project = findEntityById(id);
        project.update(request.name().trim(), normalizeDescription(request.description()), request.status(),
                request.startDate(), request.endDate());
        return ProjectMapper.toResponse(projectRepository.saveAndFlush(project));
    }

    @Transactional
    public void delete(Long id) {
        projectRepository.deleteAndFlush(findEntityById(id));
    }

    public Project findEntityById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", id));
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("startDate must be on or before endDate");
        }
    }

    private String normalizeDescription(String description) {
        return description == null ? null : description.trim();
    }
}
