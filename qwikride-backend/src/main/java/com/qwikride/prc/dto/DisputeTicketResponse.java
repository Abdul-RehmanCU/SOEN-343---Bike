package com.qwikride.prc.dto;

import com.qwikride.prc.domain.DisputeStatus;

import java.time.LocalDateTime;

public record DisputeTicketResponse(Long id,
        Long riderId,
        Long ledgerEntryId,
        DisputeStatus status,
        String reason,
        String evidenceUrl,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        String resolutionNote,
        Long adjustmentEntryId) {
}
