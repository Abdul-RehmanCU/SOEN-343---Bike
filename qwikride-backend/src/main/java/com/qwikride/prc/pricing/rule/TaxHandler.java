package com.qwikride.prc.pricing.rule;

import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;
import org.springframework.stereotype.Component;

@Component
public class TaxHandler extends AbstractRuleHandler {
    @Override
    protected void doApply(PricingContext context, TripFacts tripFacts, MutableBill bill) {
        // Placeholder for tax calculation logic if required by jurisdictions
    }
}
