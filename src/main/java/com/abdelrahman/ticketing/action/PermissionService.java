package com.abdelrahman.ticketing.action;

import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.exception.ForbiddenException;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionService {

    private final UserRepository userRepository;

    public User requireUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    public User requireAdmin(Long userId) {
        User user = requireUser(userId);
        if (user.getRole() != Role.ADMIN) {
            throw new ForbiddenException("Only admins can perform this action");
        }
        return user;
    }
}
