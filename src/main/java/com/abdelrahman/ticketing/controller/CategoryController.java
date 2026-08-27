package com.abdelrahman.ticketing.controller;

import com.abdelrahman.ticketing.action.category.*;
import com.abdelrahman.ticketing.dto.CategoryRequest;
import com.abdelrahman.ticketing.dto.CategoryResponse;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Manage ticket categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final CreateCategoryAction createCategoryAction;
    private final UpdateCategoryAction updateCategoryAction;
    private final DeleteCategoryAction deleteCategoryAction;

    @GetMapping
    @Operation(summary = "List all categories")
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
    @Operation(summary = "Get category by ID")
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
    @Operation(summary = "Create a category", description = "ADMIN only.")
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CategoryResponse response = createCategoryAction.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a category", description = "ADMIN only.")
    public ResponseEntity<CategoryResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CategoryResponse response = updateCategoryAction.execute(id, request, userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a category", description = "ADMIN only.")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        deleteCategoryAction.execute(id, userId);
        return ResponseEntity.noContent().build();
    }
}
