package com.abdelrahman.ticketing.action.ticket;

import com.abdelrahman.ticketing.dto.*;
import com.abdelrahman.ticketing.entity.*;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AssignTicketAction {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketResponse execute(Long ticketId, Long assignedToId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

        User assignee = userRepository.findById(assignedToId)
                .orElseThrow(() -> new ResourceNotFoundException("User", assignedToId));

        ticket.setAssignedTo(assignee);
        Ticket saved = ticketRepository.save(ticket);

        return mapToResponse(saved);
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
                .assignedTo(UserResponse.builder()
                        .id(ticket.getAssignedTo().getId())
                        .name(ticket.getAssignedTo().getName())
                        .email(ticket.getAssignedTo().getEmail())
                        .role(ticket.getAssignedTo().getRole())
                        .build())
                .createdAt(ticket.getCreatedAt())
                .updatedAt(ticket.getUpdatedAt())
                .build();
    }
}
