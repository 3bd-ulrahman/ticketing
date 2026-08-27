package com.abdelrahman.ticketing.dto;

import com.abdelrahman.ticketing.entity.enums.TicketPriority;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePriorityRequest {

    @NotNull(message = "Priority is required")
    private TicketPriority priority;
}
