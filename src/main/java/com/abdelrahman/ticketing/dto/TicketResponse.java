package com.abdelrahman.ticketing.dto;

import com.abdelrahman.ticketing.entity.enums.TicketPriority;
import com.abdelrahman.ticketing.entity.enums.TicketStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketResponse {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private CategoryResponse category;
    private UserResponse createdBy;
    private UserResponse assignedTo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
