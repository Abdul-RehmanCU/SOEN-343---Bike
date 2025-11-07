package com.qwikride.prc.service;

import com.qwikride.prc.billing.BillingLedgerService;
import com.qwikride.prc.domain.DisputeStatus;
import com.qwikride.prc.dto.DisputeResolutionRequest;
import com.qwikride.prc.dto.DisputeTicketRequest;
import com.qwikride.prc.dto.DisputeTicketResponse;
import com.qwikride.prc.model.DisputeTicket;
import com.qwikride.prc.model.LedgerEntry;
import com.qwikride.prc.pricing.domain.ChargeLine;
import com.qwikride.prc.pricing.domain.FinalizedBill;
import com.qwikride.prc.repository.DisputeTicketRepository;
import com.qwikride.prc.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisputeService {
    private final DisputeTicketRepository disputeTicketRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final BillingLedgerService billingLedgerService;

    @Transactional
    public DisputeTicketResponse submit(Long riderId, DisputeTicketRequest request) {
        LedgerEntry entry = ledgerEntryRepository.findById(request.ledgerEntryId())
                .orElseThrow(() -> new IllegalArgumentException("Ledger entry not found"));
        if (!entry.getRiderId().equals(riderId)) {
            throw new IllegalArgumentException("Ledger entry does not belong to rider");
        }

        DisputeTicket ticket = new DisputeTicket();
        ticket.setRiderId(riderId);
        ticket.setLedgerEntryId(entry.getId());
        ticket.setReason(request.reason());
        ticket.setEvidenceUrl(request.evidenceUrl());
        ticket.setStatus(DisputeStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        DisputeTicket saved = disputeTicketRepository.save(ticket);
        return toResponse(saved);
    }

    @Transactional
    public DisputeTicketResponse resolve(Long ticketId, DisputeResolutionRequest request) {
        DisputeTicket ticket = disputeTicketRepository.findById(ticketId)
                .orElseThrow(() -> new IllegalArgumentException("Dispute ticket not found"));
        if (ticket.getStatus() == DisputeStatus.RESOLVED || ticket.getStatus() == DisputeStatus.REJECTED) {
            return toResponse(ticket);
        }

        ticket.setStatus(request.approved() ? DisputeStatus.RESOLVED : DisputeStatus.REJECTED);
        ticket.setResolutionNote(request.resolutionNote());
        ticket.setResolvedAt(LocalDateTime.now());

        if (request.approved()) {
            LedgerEntry original = ledgerEntryRepository.findById(ticket.getLedgerEntryId())
                    .orElseThrow(() -> new IllegalArgumentException("Original ledger entry not found"));
            BigDecimal adjustmentAmount = request.adjustmentAmount() == null ? BigDecimal.ZERO
                    : request.adjustmentAmount();
            if (adjustmentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Adjustment amount must be positive when approving disputes");
            }
            ChargeLine adjustmentLine = ChargeLine.builder()
                    .code("ADJUSTMENT")
                    .amount(adjustmentAmount.negate())
                    .meta(java.util.Map.of("source", "dispute"))
                    .build();
            FinalizedBill adjustmentBill = FinalizedBill.builder()
                    .planVersionId(original.getPlanVersionId())
                    .planName(original.getPlanName() + " Adjustment")
                    .charges(List.of(adjustmentLine))
                    .total(adjustmentAmount.negate())
                    .build();
            LedgerEntry adjustmentEntry = billingLedgerService.appendAdjustment(ticket.getRiderId(), original,
                    adjustmentBill,
                    request.resolutionNote() == null ? "Dispute adjustment" : request.resolutionNote());
            ticket.setAdjustmentEntryId(adjustmentEntry.getId());
        }

        return toResponse(disputeTicketRepository.save(ticket));
    }

    @Transactional(readOnly = true)
    public List<DisputeTicketResponse> listForRider(Long riderId) {
        return disputeTicketRepository.findByRiderIdOrderByCreatedAtDesc(riderId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DisputeTicketResponse> listOpenTickets() {
        return disputeTicketRepository.findByStatusOrderByCreatedAtAsc(DisputeStatus.OPEN).stream()
                .map(this::toResponse)
                .toList();
    }

    private DisputeTicketResponse toResponse(DisputeTicket ticket) {
        return new DisputeTicketResponse(ticket.getId(),
                ticket.getRiderId(),
                ticket.getLedgerEntryId(),
                ticket.getStatus(),
                ticket.getReason(),
                ticket.getEvidenceUrl(),
                ticket.getCreatedAt(),
                ticket.getResolvedAt(),
                ticket.getResolutionNote(),
                ticket.getAdjustmentEntryId());
    }
}
