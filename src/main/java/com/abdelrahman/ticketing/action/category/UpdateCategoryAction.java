package com.abdelrahman.ticketing.action.category;

import com.abdelrahman.ticketing.dto.CategoryRequest;
import com.abdelrahman.ticketing.dto.CategoryResponse;
import com.abdelrahman.ticketing.entity.Category;
import com.abdelrahman.ticketing.exception.DuplicateResourceException;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateCategoryAction {

    private final CategoryRepository categoryRepository;

    public CategoryResponse execute(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));

        categoryRepository.findByName(request.getName())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Category", "name", request.getName());
                });

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updated = categoryRepository.save(category);
        return mapToResponse(updated);
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
