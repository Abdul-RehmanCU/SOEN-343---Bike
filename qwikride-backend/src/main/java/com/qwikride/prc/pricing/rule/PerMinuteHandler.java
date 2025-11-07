package com.qwikride.prc.pricing.rule;

import com.qwikride.prc.pricing.domain.ChargeLine;
import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class PerMinuteHandler extends AbstractRuleHandler {
    @Override
    protected void doApply(PricingContext context, TripFacts tripFacts, MutableBill bill) {
        BigDecimal perMinute = context.getRateSheet().getPerMinute();
        if (perMinute == null || perMinute.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        long minutes = Math.max(1L, tripFacts.durationMinutes());
        BigDecimal amount = perMinute.multiply(BigDecimal.valueOf(minutes));

        ChargeLine line = ChargeLine.builder()
                .code("PER_MINUTE")
                .amount(amount)
                .meta(Map.of("minutes", Long.toString(minutes)))
                .build();
        bill.add(line);
    }
}
