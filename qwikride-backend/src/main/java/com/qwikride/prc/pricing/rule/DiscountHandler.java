package com.qwikride.prc.pricing.rule;

import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prc-discounts")
public class DiscountHandler extends AbstractRuleHandler {
    @Override
    protected void doApply(PricingContext context, TripFacts tripFacts, MutableBill bill) {
        // Placeholder for future discount rules (e.g., loyalty, promotions)
    }
}
