package com.abdelrahman.ticketing.action.ticket;

import com.abdelrahman.ticketing.entity.Category;
import com.abdelrahman.ticketing.entity.Ticket;
import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.entity.enums.TicketStatus;
import com.abdelrahman.ticketing.entity.enums.TicketPriority;
import com.abdelrahman.ticketing.exception.ForbiddenException;
import com.abdelrahman.ticketing.exception.InvalidTransitionException;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.TicketRepository;
import com.abdelrahman.ticketing.repository.TicketStatusHistoryRepository;
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
class UpdateTicketStatusActionTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TicketStatusHistoryRepository statusHistoryRepository;

    @InjectMocks
    private UpdateTicketStatusAction action;

    private Ticket ticket;
    private User admin;
    private User agent;
    private User user;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(1L).name("Support").build();
        admin = User.builder().id(1L).name("Admin").email("admin@test.com").role(Role.ADMIN).build();
        agent = User.builder().id(2L).name("Agent").email("agent@test.com").role(Role.AGENT).build();
        user = User.builder().id(3L).name("User").email("user@test.com").role(Role.USER).build();

        ticket = Ticket.builder()
                .id(10L)
                .title("Test ticket")
                .status(TicketStatus.OPEN)
                .priority(TicketPriority.MEDIUM)
                .category(category)
                .createdBy(user)
                .assignedTo(agent)
                .build();
    }

    @Test
    void adminCanUpdateAnyTicketStatus() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        assertDoesNotThrow(() -> action.execute(10L, TicketStatus.IN_PROGRESS, 1L));
        verify(statusHistoryRepository).save(any());
    }

    @Test
    void agentCanUpdateAssignedTicketStatus() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        assertDoesNotThrow(() -> action.execute(10L, TicketStatus.IN_PROGRESS, 2L));
    }

    @Test
    void agentCannotUpdateUnassignedTicketStatus() {
        Ticket unassigned = Ticket.builder()
                .id(11L).title("Other").status(TicketStatus.OPEN)
                .createdBy(user).assignedTo(null).build();

        when(ticketRepository.findById(11L)).thenReturn(Optional.of(unassigned));
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(11L, TicketStatus.IN_PROGRESS, 2L));
        assertTrue(ex.getMessage().contains("Agents can only update status"));
    }

    @Test
    void agentCannotUpdateTicketAssignedToSomeoneElse() {
        User otherAgent = User.builder().id(4L).name("Other").role(Role.AGENT).build();
        Ticket assignedToOther = Ticket.builder()
                .id(12L).title("Other").status(TicketStatus.OPEN)
                .createdBy(user).assignedTo(otherAgent).build();

        when(ticketRepository.findById(12L)).thenReturn(Optional.of(assignedToOther));
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));

        assertThrows(ForbiddenException.class,
                () -> action.execute(12L, TicketStatus.IN_PROGRESS, 2L));
    }

    @Test
    void userCannotUpdateStatus() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(10L, TicketStatus.IN_PROGRESS, 3L));
        assertTrue(ex.getMessage().contains("Users cannot update ticket status"));
    }

    @Test
    void invalidTransitionThrows422() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        InvalidTransitionException ex = assertThrows(InvalidTransitionException.class,
                () -> action.execute(10L, TicketStatus.RESOLVED, 1L));
        assertTrue(ex.getMessage().contains("OPEN"));
        assertTrue(ex.getMessage().contains("RESOLVED"));
    }

    @Test
    void validTransitionFromOpenToClosed() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);

        assertDoesNotThrow(() -> action.execute(10L, TicketStatus.CLOSED, 1L));
    }

    @Test
    void nonExistentUserThrows404() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> action.execute(10L, TicketStatus.IN_PROGRESS, 99L));
    }

    @Test
    void nonExistentTicketThrows404() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> action.execute(99L, TicketStatus.IN_PROGRESS, 1L));
    }
}
