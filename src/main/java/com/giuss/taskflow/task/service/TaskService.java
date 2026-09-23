package com.giuss.taskflow.task.service;

import com.giuss.taskflow.common.exception.ResourceNotFoundException;
import com.giuss.taskflow.project.entity.Project;
import com.giuss.taskflow.project.service.ProjectService;
import com.giuss.taskflow.task.dto.TaskCreateRequest;
import com.giuss.taskflow.task.dto.TaskResponse;
import com.giuss.taskflow.task.dto.TaskUpdateRequest;
import com.giuss.taskflow.task.entity.Task;
import com.giuss.taskflow.task.entity.TaskPriority;
import com.giuss.taskflow.task.entity.TaskStatus;
import com.giuss.taskflow.task.mapper.TaskMapper;
import com.giuss.taskflow.task.repository.TaskRepository;
import com.giuss.taskflow.task.repository.TaskSpecifications;
import com.giuss.taskflow.user.entity.TaskflowUser;
import com.giuss.taskflow.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;

    public TaskService(TaskRepository taskRepository, ProjectService projectService, UserService userService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Transactional
    public TaskResponse create(TaskCreateRequest request) {
        Project project = projectService.findEntityById(request.projectId());
        TaskflowUser assignedUser = findAssignedUser(request.assignedUserId());
        Task task = new Task(project, request.title().trim(), normalizeDescription(request.description()),
                request.status() == null ? TaskStatus.TODO : request.status(),
                request.priority() == null ? TaskPriority.MEDIUM : request.priority(), assignedUser, request.dueDate());
        return TaskMapper.toResponse(taskRepository.saveAndFlush(task));
    }

    public List<TaskResponse> findAll(TaskStatus status, TaskPriority priority, Long projectId,
                                      Long assignedUserId, LocalDate dueDate) {
        return taskRepository.findAll(TaskSpecifications.withFilters(status, priority, projectId, assignedUserId, dueDate))
                .stream().map(TaskMapper::toResponse).toList();
    }

    public TaskResponse findById(Long id) { return TaskMapper.toResponse(findEntityById(id)); }

    @Transactional
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        Task task = findEntityById(id);
        task.update(request.title().trim(), normalizeDescription(request.description()), request.status(), request.priority(),
                findAssignedUser(request.assignedUserId()), request.dueDate());
        return TaskMapper.toResponse(taskRepository.saveAndFlush(task));
    }

    @Transactional
    public void delete(Long id) { taskRepository.deleteAndFlush(findEntityById(id)); }

    private Task findEntityById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Task", id));
    }

    private TaskflowUser findAssignedUser(Long assignedUserId) {
        return assignedUserId == null ? null : userService.findEntityById(assignedUserId);
    }

    private String normalizeDescription(String description) { return description == null ? null : description.trim(); }
}
