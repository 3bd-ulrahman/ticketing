package com.abdelrahman.ticketing.dto;

import com.abdelrahman.ticketing.entity.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private TicketStatus status;

    @NotNull(message = "User ID is required")
    private Long userId;
}
