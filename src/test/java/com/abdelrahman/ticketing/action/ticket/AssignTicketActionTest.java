package com.abdelrahman.ticketing.action.ticket;

import com.abdelrahman.ticketing.entity.Category;
import com.abdelrahman.ticketing.entity.Ticket;
import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.exception.ForbiddenException;
import com.abdelrahman.ticketing.repository.TicketRepository;
import com.abdelrahman.ticketing.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignTicketActionTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssignTicketAction action;

    private Ticket ticket;
    private User admin;
    private User agent;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Support").build();
        admin = User.builder().id(1L).name("Admin").role(Role.ADMIN).build();
        agent = User.builder().id(2L).name("Agent").role(Role.AGENT).build();
        user = User.builder().id(3L).name("User").role(Role.USER).build();

        ticket = Ticket.builder()
                .id(10L).title("Test").category(category).createdBy(user).build();
    }

    @Test
    void adminCanAssignTicket() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(inv -> {
            Ticket t = inv.getArgument(0);
            t.setAssignedTo(agent);
            return t;
        });

        assertDoesNotThrow(() -> action.execute(10L, 2L, 1L));
    }

    @Test
    void agentCannotAssignTicket() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(10L, 1L, 2L));
        assertTrue(ex.getMessage().contains("Only admins can assign tickets"));
    }

    @Test
    void userCannotAssignTicket() {
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(10L, 2L, 3L));
        assertTrue(ex.getMessage().contains("Only admins can assign tickets"));
    }
}
