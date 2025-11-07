package com.qwikride.prc.repository;

import com.qwikride.prc.domain.DisputeStatus;
import com.qwikride.prc.model.DisputeTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisputeTicketRepository extends JpaRepository<DisputeTicket, Long> {
    List<DisputeTicket> findByRiderIdOrderByCreatedAtDesc(Long riderId);

    List<DisputeTicket> findByStatusOrderByCreatedAtAsc(DisputeStatus status);
}
