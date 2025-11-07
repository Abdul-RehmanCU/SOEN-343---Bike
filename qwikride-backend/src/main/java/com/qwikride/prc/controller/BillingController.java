package com.qwikride.prc.controller;

import com.qwikride.prc.billing.BillingLedgerService;
import com.qwikride.prc.dto.BillingEntryResponse;
import com.qwikride.prc.dto.TripSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/prc/billing")
@RequiredArgsConstructor
public class BillingController {
    private final BillingLedgerService billingLedgerService;

    @GetMapping("/history/{riderId}")
    @PreAuthorize("hasAuthority('RIDER') or hasAuthority('OPERATOR')")
    public ResponseEntity<List<BillingEntryResponse>> getHistory(
            @PathVariable Long riderId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        LocalDateTime normalizedStart = normalizeStart(start);
        LocalDateTime normalizedEnd = normalizeEnd(end);
        return ResponseEntity.ok(billingLedgerService.getHistory(riderId, normalizedStart, normalizedEnd));
    }

    @GetMapping("/summary/{ledgerEntryId}")
    @PreAuthorize("hasAuthority('RIDER') or hasAuthority('OPERATOR')")
    public ResponseEntity<TripSummaryResponse> getSummary(@PathVariable Long ledgerEntryId) {
        return ResponseEntity.ok(billingLedgerService.getSummary(ledgerEntryId));
    }

    private LocalDateTime normalizeStart(LocalDateTime input) {
        return input;
    }

    private LocalDateTime normalizeEnd(LocalDateTime input) {
        return input;
    }
}
