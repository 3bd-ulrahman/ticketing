package com.abdelrahman.ticketing.action.ticket;

import com.abdelrahman.ticketing.dto.*;
import com.abdelrahman.ticketing.entity.*;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddCommentAction {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public CommentResponse execute(Long ticketId, CommentRequest request) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.getUserId()));

        Comment comment = Comment.builder()
                .ticket(ticket)
                .user(user)
                .content(request.getContent())
                .build();

        Comment saved = commentRepository.save(comment);
        return mapToResponse(saved);
    }

    private CommentResponse mapToResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .ticketId(comment.getTicket().getId())
                .user(UserResponse.builder()
                        .id(comment.getUser().getId())
                        .name(comment.getUser().getName())
                        .email(comment.getUser().getEmail())
                        .role(comment.getUser().getRole())
                        .build())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
