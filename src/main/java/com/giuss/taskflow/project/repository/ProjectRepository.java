package com.giuss.taskflow.project.repository;

import com.giuss.taskflow.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
