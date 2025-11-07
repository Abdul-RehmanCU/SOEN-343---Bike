package com.qwikride.prc.dto;

public record DisputeTicketRequest(Long ledgerEntryId,
        String reason,
        String evidenceUrl) {
}
