package com.qwikride.prc.pricing.rule;

import com.qwikride.prc.pricing.domain.ChargeLine;
import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class EbikeSurchargeHandler extends AbstractRuleHandler {
    @Override
    protected void doApply(PricingContext context, TripFacts tripFacts, MutableBill bill) {
        if (!tripFacts.isEbike()) {
            return;
        }

        BigDecimal surcharge = context.getRateSheet().getEbikeSurcharge();
        if (surcharge == null || surcharge.compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        ChargeLine line = ChargeLine.builder()
                .code("E_BIKE_SURCHARGE")
                .amount(surcharge)
                .meta(Map.of("ebike", "true"))
                .build();
        bill.add(line);
    }
}
