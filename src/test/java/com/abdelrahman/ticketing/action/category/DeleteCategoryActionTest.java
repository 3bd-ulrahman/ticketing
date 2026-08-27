package com.abdelrahman.ticketing.action.category;

import com.abdelrahman.ticketing.action.PermissionService;
import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.exception.ForbiddenException;
import com.abdelrahman.ticketing.repository.CategoryRepository;
import com.abdelrahman.ticketing.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryActionTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    private PermissionService permissionService;
    private DeleteCategoryAction action;

    private User admin;
    private User agent;
    private User user;

    @BeforeEach
    void setUp() {
        permissionService = Mockito.spy(new PermissionService(userRepository));
        action = new DeleteCategoryAction(categoryRepository, permissionService);
        admin = User.builder().id(1L).name("Admin").role(Role.ADMIN).build();
        agent = User.builder().id(2L).name("Agent").role(Role.AGENT).build();
        user = User.builder().id(3L).name("User").role(Role.USER).build();
    }

    @Test
    void adminCanDeleteCategory() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(categoryRepository.existsById(10L)).thenReturn(true);

        assertDoesNotThrow(() -> action.execute(10L, 1L));
        verify(categoryRepository).deleteById(10L);
    }

    @Test
    void agentCannotDeleteCategory() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(10L, 2L));
        assertTrue(ex.getMessage().contains("Only admins"));
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void userCannotDeleteCategory() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        assertThrows(ForbiddenException.class, () -> action.execute(10L, 3L));
        verify(categoryRepository, never()).deleteById(any());
    }
}
