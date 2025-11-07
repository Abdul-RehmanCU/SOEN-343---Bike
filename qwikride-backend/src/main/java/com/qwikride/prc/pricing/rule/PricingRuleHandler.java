package com.qwikride.prc.pricing.rule;

import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;

public interface PricingRuleHandler {
    PricingRuleHandler setNext(PricingRuleHandler next);

    void apply(PricingContext context, TripFacts tripFacts, MutableBill bill);
}
