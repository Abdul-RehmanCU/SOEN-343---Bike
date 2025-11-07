package com.qwikride.prc.pricing.rule;

import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;

public abstract class AbstractRuleHandler implements PricingRuleHandler {
    private PricingRuleHandler next;

    @Override
    public PricingRuleHandler setNext(PricingRuleHandler nextHandler) {
        this.next = nextHandler;
        return nextHandler;
    }

    @Override
    public void apply(PricingContext context, TripFacts tripFacts, MutableBill bill) {
        doApply(context, tripFacts, bill);
        if (next != null) {
            next.apply(context, tripFacts, bill);
        }
    }

    protected abstract void doApply(PricingContext context, TripFacts tripFacts, MutableBill bill);
}
