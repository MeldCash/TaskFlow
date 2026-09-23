package com.giuss.taskflow.user.repository;

import com.giuss.taskflow.user.entity.TaskflowUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TaskflowUser, Long> {

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);
}
