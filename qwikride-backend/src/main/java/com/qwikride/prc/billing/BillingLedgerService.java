package com.qwikride.prc.billing;

import com.qwikride.prc.domain.PaymentStatus;
import com.qwikride.prc.dto.BillingEntryResponse;
import com.qwikride.prc.dto.ChargeLineResponse;
import com.qwikride.prc.dto.TripSummaryResponse;
import com.qwikride.prc.model.LedgerCharge;
import com.qwikride.prc.model.LedgerEntry;
import com.qwikride.prc.pricing.domain.FinalizedBill;
import com.qwikride.prc.pricing.domain.TripFacts;
import com.qwikride.prc.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillingLedgerService {
    private final LedgerEntryRepository ledgerEntryRepository;

    @Transactional
    public LedgerEntry appendTripEntry(FinalizedBill bill, TripFacts tripFacts) {
        LedgerEntry entry = new LedgerEntry();
        entry.setRiderId(tripFacts.getRiderId());
        entry.setPlanVersionId(bill.getPlanVersionId());
        entry.setPlanName(bill.getPlanName());
        entry.setBikeId(tripFacts.getBikeId());
        entry.setStartStationId(tripFacts.getStartStationId());
        entry.setEndStationId(tripFacts.getEndStationId());
        entry.setStartTime(tripFacts.getStartTime());
        entry.setEndTime(tripFacts.getEndTime());
        entry.setDurationMinutes(tripFacts.durationMinutes());
        entry.setDistanceKm(tripFacts.getDistanceKm());
        entry.setTotal(bill.getTotal());
        entry.setPaymentStatus(PaymentStatus.PENDING);
        entry.setCharges(bill.getCharges().stream()
                .map(line -> LedgerCharge.from(line.getCode(), line.getAmount(), line.safeMeta()))
                .collect(Collectors.toList()));
        entry.setSummary(
                String.format("%s • %d min • $%s", bill.getPlanName(), tripFacts.durationMinutes(), bill.getTotal()));

        return ledgerEntryRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<BillingEntryResponse> getHistory(Long riderId, LocalDateTime start, LocalDateTime end) {
        List<LedgerEntry> entries;
        if (start != null && end != null) {
            entries = ledgerEntryRepository.findByRiderAndDateRange(riderId, start, end);
        } else {
            entries = ledgerEntryRepository.findByRiderIdOrderByStartTimeDesc(riderId);
        }
        return entries.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripSummaryResponse buildSummary(LedgerEntry entry) {
        List<ChargeLineResponse> charges = entry.getCharges().stream()
                .map(charge -> new ChargeLineResponse(charge.getCode(), charge.getAmount(), charge.metaAsMap()))
                .collect(Collectors.toList());
        return new TripSummaryResponse(
                entry.getBikeId(),
                entry.getRiderId(),
                entry.getStartStationId(),
                entry.getEndStationId(),
                entry.getStartTime(),
                entry.getEndTime(),
                entry.getDurationMinutes(),
                entry.getDistanceKm(),
                entry.getTotal(),
                charges,
                entry.getPlanName());
    }

    @Transactional(readOnly = true)
    public TripSummaryResponse getSummary(Long ledgerEntryId) {
        LedgerEntry entry = ledgerEntryRepository.findById(ledgerEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Ledger entry not found"));
        return buildSummary(entry);
    }

    private BillingEntryResponse toResponse(LedgerEntry entry) {
        List<ChargeLineResponse> charges = entry.getCharges().stream()
                .map(charge -> new ChargeLineResponse(charge.getCode(), charge.getAmount(), charge.metaAsMap()))
                .collect(Collectors.toList());
        return new BillingEntryResponse(
                entry.getId(),
                entry.getPlanVersionId(),
                entry.getPlanName(),
                entry.getBikeId(),
                entry.getStartStationId(),
                entry.getEndStationId(),
                entry.getStartTime(),
                entry.getEndTime(),
                entry.getDurationMinutes(),
                entry.getDistanceKm(),
                entry.getTotal(),
                entry.getPaymentStatus(),
                charges,
                entry.getSummary());
    }
}
