package com.kat.backend.tickets.repository;

import com.kat.backend.tickets.entity.TicketConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketConfigRepository extends JpaRepository<TicketConfig, String> {
}
