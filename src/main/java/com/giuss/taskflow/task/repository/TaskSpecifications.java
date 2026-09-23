package com.giuss.taskflow.task.repository;

import com.giuss.taskflow.task.entity.Task;
import com.giuss.taskflow.task.entity.TaskPriority;
import com.giuss.taskflow.task.entity.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public final class TaskSpecifications {
    private TaskSpecifications() { }

    public static Specification<Task> withFilters(TaskStatus status, TaskPriority priority, Long projectId,
                                                   Long assignedUserId, LocalDate dueDate) {
        return (root, query, criteriaBuilder) -> {
            var predicate = criteriaBuilder.conjunction();
            if (status != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (priority != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("priority"), priority));
            }
            if (projectId != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("project").get("id"), projectId));
            }
            if (assignedUserId != null) {
                predicate.getExpressions().add(
                        criteriaBuilder.equal(root.get("assignedUser").get("id"), assignedUserId));
            }
            if (dueDate != null) {
                predicate.getExpressions().add(criteriaBuilder.equal(root.get("dueDate"), dueDate));
            }
            return predicate;
        };
    }
}
