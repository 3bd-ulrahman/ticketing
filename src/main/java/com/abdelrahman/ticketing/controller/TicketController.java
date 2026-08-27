package com.abdelrahman.ticketing.controller;

import com.abdelrahman.ticketing.action.ticket.*;
import com.abdelrahman.ticketing.dto.*;
import com.abdelrahman.ticketing.entity.Ticket;
import com.abdelrahman.ticketing.entity.User;
import com.abdelrahman.ticketing.entity.enums.Role;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.CommentRepository;
import com.abdelrahman.ticketing.repository.TicketRepository;
import com.abdelrahman.ticketing.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CreateTicketAction createTicketAction;
    private final UpdateTicketStatusAction updateTicketStatusAction;
    private final AssignTicketAction assignTicketAction;
    private final UpdateTicketPriorityAction updateTicketPriorityAction;
    private final AddCommentAction addCommentAction;

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getAll(@RequestHeader("X-User-Id") Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        List<Ticket> tickets;
        if (user.getRole() == Role.USER) {
            tickets = ticketRepository.findByCreatedById(userId);
        } else {
            tickets = ticketRepository.findAll();
        }

        List<TicketResponse> responses = tickets.stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getById(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));

        if (user.getRole() == Role.USER) {
            if (ticket.getCreatedBy() == null || !ticket.getCreatedBy().getId().equals(userId)) {
                throw new ResourceNotFoundException("Ticket", id);
            }
        }

        return ResponseEntity.ok(mapToResponse(ticket));
    }

    @PostMapping
    public ResponseEntity<TicketResponse> create(
            @Valid @RequestBody TicketRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        TicketResponse response = createTicketAction.execute(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        TicketResponse response = updateTicketStatusAction.execute(id, request.getStatus(), userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/assign")
    public ResponseEntity<TicketResponse> assign(
            @PathVariable Long id,
            @Valid @RequestBody AssignTicketRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        TicketResponse response = assignTicketAction.execute(id, request.getAssigneeId(), userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<TicketResponse> updatePriority(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriorityRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        TicketResponse response = updateTicketPriorityAction.execute(id, request.getPriority(), userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        CommentResponse response = addCommentAction.execute(id, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", id));

        if (user.getRole() == Role.USER) {
            if (ticket.getCreatedBy() == null || !ticket.getCreatedBy().getId().equals(userId)) {
                throw new ResourceNotFoundException("Ticket", id);
            }
        }

        List<CommentResponse> comments = commentRepository.findByTicketId(id).stream()
                .map(comment -> CommentResponse.builder()
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
                        .build())
                .toList();
        return ResponseEntity.ok(comments);
    }

    private TicketResponse mapToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .title(ticket.getTitle())
                .description(ticket.getDescription())
                .status(ticket.getStatus())
                .priority(ticket.getPriority())
                .category(CategoryResponse.builder()
                        .id(ticket.getCategory().getId())
                        .name(ticket.getCategory().getName())
                        .description(ticket.getCategory().getDescription())
                        .createdAt(ticket.getCategory().getCreatedAt())
                        .build())
                .createdBy(UserResponse.builder()
                        .id(ticket.getCreatedBy().getId())
                        .name(ticket.getCreatedBy().getName())
                        .email(ticket.getCreatedBy().getEmail())
                        .role(ticket.getCreatedBy().getRole())
                        .build())
                .assignedTo(ticket.getAssignedTo() != null ? UserResponse.builder()
                        .id(ticket.getAssignedTo().getId())
                        .name(ticket.getAssignedTo().getName())
                        .email(ticket.getAssignedTo().getEmail())
                        .role(ticket.getAssignedTo().getRole())
                        .build() : null)
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
