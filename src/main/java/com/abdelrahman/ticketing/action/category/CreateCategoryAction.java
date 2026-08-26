package com.abdelrahman.ticketing.action.category;

import com.abdelrahman.ticketing.dto.CategoryRequest;
import com.abdelrahman.ticketing.dto.CategoryResponse;
import com.abdelrahman.ticketing.entity.Category;
import com.abdelrahman.ticketing.exception.DuplicateResourceException;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateCategoryAction {

    private final CategoryRepository categoryRepository;

    public CategoryResponse execute(CategoryRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new DuplicateResourceException("Category", "name", request.getName());
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Category saved = categoryRepository.save(category);
        return mapToResponse(saved);
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
