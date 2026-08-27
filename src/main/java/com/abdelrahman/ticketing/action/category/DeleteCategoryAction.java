package com.abdelrahman.ticketing.action.category;

import com.abdelrahman.ticketing.action.PermissionService;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteCategoryAction {

    private final CategoryRepository categoryRepository;
    private final PermissionService permissionService;

    public void execute(Long id, Long userId) {
        permissionService.requireAdmin(userId);
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category", id);
        }
        categoryRepository.deleteById(id);
    }
}
