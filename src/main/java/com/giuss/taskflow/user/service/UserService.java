package com.giuss.taskflow.user.service;

import com.giuss.taskflow.common.exception.ResourceNotFoundException;
import com.giuss.taskflow.user.dto.UserCreateRequest;
import com.giuss.taskflow.user.dto.UserResponse;
import com.giuss.taskflow.user.dto.UserUpdateRequest;
import com.giuss.taskflow.user.entity.TaskflowUser;
import com.giuss.taskflow.user.exception.DuplicateEmailException;
import com.giuss.taskflow.user.mapper.UserMapper;
import com.giuss.taskflow.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        String email = normalizeEmail(request.email());
        ensureEmailIsAvailable(email, null);

        TaskflowUser user = new TaskflowUser(normalizeName(request.name()), email);
        return UserMapper.toResponse(userRepository.saveAndFlush(user));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    public UserResponse findById(Long id) {
        return UserMapper.toResponse(findEntityById(id));
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        TaskflowUser user = findEntityById(id);
        String email = normalizeEmail(request.email());
        ensureEmailIsAvailable(email, id);
        user.update(normalizeName(request.name()), email);
        return UserMapper.toResponse(userRepository.saveAndFlush(user));
    }

    @Transactional
    public void delete(Long id) {
        TaskflowUser user = findEntityById(id);
        userRepository.deleteAndFlush(user);
    }

    public TaskflowUser findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private void ensureEmailIsAvailable(String email, Long currentUserId) {
        boolean emailAlreadyExists = currentUserId == null
                ? userRepository.existsByEmailIgnoreCase(email)
                : userRepository.existsByEmailIgnoreCaseAndIdNot(email, currentUserId);

        if (emailAlreadyExists) {
            throw new DuplicateEmailException(email);
        }
    }

    private String normalizeName(String name) {
        return name.trim();
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
