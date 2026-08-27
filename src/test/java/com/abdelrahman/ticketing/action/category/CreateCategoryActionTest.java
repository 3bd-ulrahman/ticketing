package com.abdelrahman.ticketing.action.category;

import com.abdelrahman.ticketing.action.PermissionService;
import com.abdelrahman.ticketing.dto.CategoryRequest;
import com.abdelrahman.ticketing.entity.Category;
import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.exception.DuplicateResourceException;
import com.abdelrahman.ticketing.exception.ForbiddenException;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import com.abdelrahman.ticketing.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryActionTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;

    private CreateCategoryAction action;

    private User admin;
    private User agent;
    private User user;

    @BeforeEach
    void setUp() {
        action = new CreateCategoryAction(categoryRepository, new PermissionService(userRepository));
        admin = User.builder().id(1L).name("Admin").role(Role.ADMIN).build();
        agent = User.builder().id(2L).name("Agent").role(Role.AGENT).build();
        user = User.builder().id(3L).name("User").role(Role.USER).build();
    }

    private CategoryRequest request() {
        CategoryRequest r = new CategoryRequest();
        r.setName("Billing");
        r.setDescription("Billing issues");
        return r;
    }

    @Test
    void adminCanCreateCategory() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(categoryRepository.findByName("Billing")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> action.execute(request(), 1L));
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void agentCannotCreateCategory() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(request(), 2L));
        assertTrue(ex.getMessage().contains("Only admins"));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void userCannotCreateCategory() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(request(), 3L));
        assertTrue(ex.getMessage().contains("Only admins"));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void duplicateNameThrowsConflict() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        Category existing = Category.builder().id(5L).name("Billing").build();
        when(categoryRepository.findByName("Billing")).thenReturn(Optional.of(existing));

        assertThrows(DuplicateResourceException.class, () -> action.execute(request(), 1L));
    }
}
