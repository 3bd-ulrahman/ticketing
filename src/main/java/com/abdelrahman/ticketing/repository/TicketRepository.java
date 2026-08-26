package com.abdelrahman.ticketing.repository;

import com.abdelrahman.ticketing.entity.Ticket;
import com.abdelrahman.ticketing.entity.enums.TicketPriority;
import com.abdelrahman.ticketing.entity.enums.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByStatus(TicketStatus status);
    List<Ticket> findByPriority(TicketPriority priority);
    List<Ticket> findByCreatedById(Long userId);
    List<Ticket> findByAssignedToId(Long userId);
    List<Ticket> findByCategoryId(Long categoryId);
}
