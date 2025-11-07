package com.qwikride.prc.pricing.rule;

import com.qwikride.prc.pricing.domain.ChargeLine;
import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class BaseFeeHandler extends AbstractRuleHandler {
    @Override
    protected void doApply(PricingContext context, TripFacts tripFacts, MutableBill bill) {
        BigDecimal baseFee = context.getRateSheet().getBaseFee();
        if (baseFee == null || baseFee.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        ChargeLine line = ChargeLine.builder()
                .code("BASE_FEE")
                .amount(baseFee)
                .meta(Map.of("planVersionId", context.getPlanVersionId().toString()))
                .build();
        bill.add(line);
    }
}
