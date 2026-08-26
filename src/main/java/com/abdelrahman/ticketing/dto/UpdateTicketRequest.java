package com.abdelrahman.ticketing.dto;

import com.abdelrahman.ticketing.entity.enums.TicketPriority;
import com.abdelrahman.ticketing.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTicketRequest {

    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    private TicketStatus status;

    private TicketPriority priority;

    private Long categoryId;

    private Long assignedToId;
}
