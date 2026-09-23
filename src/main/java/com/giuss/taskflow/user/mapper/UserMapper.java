package com.giuss.taskflow.user.mapper;

import com.giuss.taskflow.user.dto.UserResponse;
import com.giuss.taskflow.user.entity.TaskflowUser;

public final class UserMapper {

    private UserMapper() {
    }

    public static UserResponse toResponse(TaskflowUser user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
