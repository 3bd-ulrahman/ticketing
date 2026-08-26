package com.abdelrahman.ticketing.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignTicketRequest {

    @NotNull(message = "Assignee ID is required")
    private Long assigneeId;
}
