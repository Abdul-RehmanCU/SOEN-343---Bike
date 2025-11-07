package com.qwikride.prc.service;

import com.qwikride.model.User;
import com.qwikride.prc.domain.PaymentStatus;
import com.qwikride.prc.model.LedgerEntry;
import com.qwikride.prc.payment.PaymentGateway;
import com.qwikride.prc.payment.PaymentRequest;
import com.qwikride.prc.payment.PaymentResponse;
import com.qwikride.prc.repository.LedgerEntryRepository;
import com.qwikride.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentSettlementService {
    private final LedgerEntryRepository ledgerEntryRepository;
    private final UserRepository userRepository;
    private final PaymentGateway paymentGateway;

    @Transactional
    public PaymentResponse settle(Long riderId, Long ledgerEntryId, String paymentMethodToken) {
        LedgerEntry entry = ledgerEntryRepository.findById(ledgerEntryId)
                .orElseThrow(() -> new IllegalArgumentException("Ledger entry not found"));
        if (!entry.getRiderId().equals(riderId)) {
            throw new IllegalArgumentException("Ledger entry does not belong to rider");
        }
        if (entry.getPaymentStatus() == PaymentStatus.PAID) {
            return new PaymentResponse(true, entry.getPaymentReference(), entry.getPaymentProcessedAt(), null);
        }

        PaymentRequest request = new PaymentRequest(riderId,
                entry.getTotal(),
                paymentMethodToken,
                "LEDGER-" + ledgerEntryId);
        PaymentResponse response = paymentGateway.charge(request);

        if (response.success()) {
            entry.setPaymentStatus(PaymentStatus.PAID);
            entry.setPaymentReference(response.transactionId());
            entry.setPaymentProcessedAt(response.processedAt());
            ledgerEntryRepository.save(entry);
            adjustRiderBalance(riderId, entry.getTotal().negate());
        } else {
            entry.setPaymentStatus(PaymentStatus.FAILED);
            entry.setPaymentReference(response.failureReason());
            entry.setPaymentProcessedAt(LocalDateTime.now());
            ledgerEntryRepository.save(entry);
        }
        return response;
    }

    private void adjustRiderBalance(Long riderId, BigDecimal delta) {
        userRepository.findById(riderId).ifPresent(user -> {
            BigDecimal current = user.getPendingBalance() == null ? BigDecimal.ZERO : user.getPendingBalance();
            user.setPendingBalance(current.add(delta));
            userRepository.save(user);
        });
    }
}
