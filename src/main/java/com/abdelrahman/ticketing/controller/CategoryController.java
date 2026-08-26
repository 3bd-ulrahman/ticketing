package com.abdelrahman.ticketing.controller;

import com.abdelrahman.ticketing.action.category.*;
import com.abdelrahman.ticketing.dto.CategoryRequest;
import com.abdelrahman.ticketing.dto.CategoryResponse;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CreateCategoryAction createCategoryAction;
    private final UpdateCategoryAction updateCategoryAction;
    private final DeleteCategoryAction deleteCategoryAction;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll() {
        List<CategoryResponse> categories = categoryRepository.findAll().stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .createdAt(category.getCreatedAt())
                        .build())
                .toList();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new com.abdelrahman.ticketing.exception.ResourceNotFoundException("Category", id));
        return ResponseEntity.ok(CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .build());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = createCategoryAction.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = updateCategoryAction.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteCategoryAction.execute(id);
        return ResponseEntity.noContent().build();
    }
}
