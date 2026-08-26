package com.abdelrahman.ticketing.exception;

import com.abdelrahman.ticketing.entity.enums.TicketStatus;

public class InvalidTransitionException extends RuntimeException {

    public InvalidTransitionException(TicketStatus from, TicketStatus to) {
        super(String.format("Invalid status transition from '%s' to '%s'", from, to));
    }
}
