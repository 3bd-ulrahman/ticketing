package com.abdelrahman.ticketing.dto;

import com.abdelrahman.ticketing.entity.enums.TicketPriority;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePriorityRequest {

    @NotNull(message = "Priority is required")
    private TicketPriority priority;
}
