package com.abdelrahman.ticketing.action.ticket;

import com.abdelrahman.ticketing.dto.CommentRequest;
import com.abdelrahman.ticketing.entity.Comment;
import com.abdelrahman.ticketing.entity.Ticket;
import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.exception.ForbiddenException;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.CommentRepository;
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
class AddCommentActionTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AddCommentAction action;

    private Ticket ticket;
    private User admin;
    private User agent;
    private User owner;
    private User stranger;
    private CommentRequest request;

    @BeforeEach
    void setUp() {
        admin = User.builder().id(1L).name("Admin").role(Role.ADMIN).build();
        agent = User.builder().id(2L).name("Agent").role(Role.AGENT).build();
        owner = User.builder().id(3L).name("Owner").role(Role.USER).build();
        stranger = User.builder().id(4L).name("Stranger").role(Role.USER).build();

        ticket = Ticket.builder()
                .id(10L).title("Test").createdBy(owner).build();

        request = new CommentRequest();
        request.setContent("Nice ticket!");
    }

    @Test
    void adminCanCommentOnAnyTicket() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> action.execute(10L, request, 1L));
    }

    @Test
    void agentCanCommentOnAnyTicket() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(2L)).thenReturn(Optional.of(agent));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> action.execute(10L, request, 2L));
    }

    @Test
    void ownerCanCommentOnOwnTicket() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(3L)).thenReturn(Optional.of(owner));
        when(commentRepository.save(any(Comment.class))).thenAnswer(inv -> inv.getArgument(0));

        assertDoesNotThrow(() -> action.execute(10L, request, 3L));
    }

    @Test
    void userCannotCommentOnOthersTicket() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket));
        when(userRepository.findById(4L)).thenReturn(Optional.of(stranger));

        ForbiddenException ex = assertThrows(ForbiddenException.class,
                () -> action.execute(10L, request, 4L));
        assertTrue(ex.getMessage().contains("Users can only comment on their own tickets"));
    }
}
