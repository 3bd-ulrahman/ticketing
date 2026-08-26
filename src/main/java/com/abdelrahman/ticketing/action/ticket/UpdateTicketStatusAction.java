package com.abdelrahman.ticketing.action.ticket;

import com.abdelrahman.ticketing.dto.*;
import com.abdelrahman.ticketing.entity.*;
import com.abdelrahman.ticketing.exception.InvalidTransitionException;
import com.abdelrahman.ticketing.exception.ResourceNotFoundException;
import com.abdelrahman.ticketing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UpdateTicketStatusAction {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final TicketStatusHistoryRepository statusHistoryRepository;

    private static final Map<com.abdelrahman.ticketing.entity.enums.TicketStatus, Set<com.abdelrahman.ticketing.entity.enums.TicketStatus>> ALLOWED_TRANSITIONS = Map.of(
            com.abdelrahman.ticketing.entity.enums.TicketStatus.OPEN, Set.of(com.abdelrahman.ticketing.entity.enums.TicketStatus.IN_PROGRESS, com.abdelrahman.ticketing.entity.enums.TicketStatus.CLOSED),
            com.abdelrahman.ticketing.entity.enums.TicketStatus.IN_PROGRESS, Set.of(com.abdelrahman.ticketing.entity.enums.TicketStatus.RESOLVED, com.abdelrahman.ticketing.entity.enums.TicketStatus.OPEN),
            com.abdelrahman.ticketing.entity.enums.TicketStatus.RESOLVED, Set.of(com.abdelrahman.ticketing.entity.enums.TicketStatus.CLOSED, com.abdelrahman.ticketing.entity.enums.TicketStatus.REOPENED),
            com.abdelrahman.ticketing.entity.enums.TicketStatus.CLOSED, Set.of(com.abdelrahman.ticketing.entity.enums.TicketStatus.REOPENED),
            com.abdelrahman.ticketing.entity.enums.TicketStatus.REOPENED, Set.of(com.abdelrahman.ticketing.entity.enums.TicketStatus.IN_PROGRESS)
    );

    public TicketResponse execute(Long ticketId, com.abdelrahman.ticketing.entity.enums.TicketStatus newStatus, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", ticketId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        com.abdelrahman.ticketing.entity.enums.TicketStatus oldStatus = ticket.getStatus();

        Set<com.abdelrahman.ticketing.entity.enums.TicketStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(oldStatus, Set.of());
        if (!allowed.contains(newStatus)) {
            throw new InvalidTransitionException(oldStatus, newStatus);
        }

        ticket.setStatus(newStatus);
        ticketRepository.save(ticket);

        TicketStatusHistory history = TicketStatusHistory.builder()
                .ticket(ticket)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedBy(user)
                .build();
        statusHistoryRepository.save(history);

        return mapToResponse(ticket);
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
