package com.giuss.taskflow.project.controller;

import com.giuss.taskflow.project.dto.ProjectCreateRequest;
import com.giuss.taskflow.project.dto.ProjectResponse;
import com.giuss.taskflow.project.dto.ProjectUpdateRequest;
import com.giuss.taskflow.project.service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Validated
public class ProjectController {
    private final ProjectService projectService;
    public ProjectController(ProjectService projectService) { this.projectService = projectService; }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectResponse response = projectService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<ProjectResponse> findAll() { return projectService.findAll(); }

    @GetMapping("/{id}")
    public ProjectResponse findById(@PathVariable @Positive Long id) { return projectService.findById(id); }

    @PutMapping("/{id}")
    public ProjectResponse update(@PathVariable @Positive Long id, @Valid @RequestBody ProjectUpdateRequest request) {
        return projectService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Positive Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
