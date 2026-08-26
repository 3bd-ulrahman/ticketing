package com.abdelrahman.ticketing.repository;

import com.abdelrahman.ticketing.entity.TicketStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketStatusHistoryRepository extends JpaRepository<TicketStatusHistory, Long> {
    List<TicketStatusHistory> findByTicketId(Long ticketId);
}
