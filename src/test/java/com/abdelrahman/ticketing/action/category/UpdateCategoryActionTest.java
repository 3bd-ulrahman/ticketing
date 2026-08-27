package com.abdelrahman.ticketing.action.category;

import com.abdelrahman.ticketing.action.PermissionService;
import com.abdelrahman.ticketing.dto.CategoryRequest;
import com.abdelrahman.ticketing.entity.Category;
import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.exception.ForbiddenException;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import com.abdelrahman.ticketing.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryActionTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    private PermissionService permissionService;
    @InjectMocks
    private UpdateCategoryAction action;

    private User admin;
    private User agent;

    @BeforeEach
    void setUp() {
        permissionService = Mockito.spy(new PermissionService(userRepository));
        action = new UpdateCategoryAction(categoryRepository, permissionService);
        admin = User.builder().id(1L).name("Admin").role(Role.ADMIN).build();
        agent = User.builder().id(2L).name("Agent").role(Role.AGENT).build();
    }

    private CategoryRequest request() {
        CategoryRequest r = new CategoryRequest();
        r.setName("Updated");
        r.setDescription("desc");
        return r;
    }

    @Test
    void adminCanUpdateCategory() {
        Category cat = Category.builder().id(10L).name("Old").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(categoryRepository.findById(10L)).thenReturn(Optional.of(cat));
        when(categoryRepository.findByName("Updated")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> action.execute(10L, request(), 1L));
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void agentCannotUpdateCategory() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(10L, request(), 2L));
        assertTrue(ex.getMessage().contains("Only admins"));
        verify(categoryRepository, never()).save(any());
    }
}
