package com.qwikride.prc.pricing;

import com.qwikride.prc.pricing.domain.FinalizedBill;
import com.qwikride.prc.pricing.domain.MutableBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.TripFacts;
import com.qwikride.prc.pricing.rule.PricingRuleHandler;
import com.qwikride.prc.pricing.selector.PricingPlanSelector;
import com.qwikride.prc.pricing.selector.SelectionInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PricingEngine {
    private final PricingPlanSelector selector;
    private final PricingRuleHandler chainHead;

    @Autowired
    public PricingEngine(PricingPlanSelector selector,
            @Qualifier("pricingRuleChain") PricingRuleHandler chainHead) {
        this.selector = selector;
        this.chainHead = chainHead;
    }

    public FinalizedBill price(TripFacts tripFacts, SelectionInput input) {
        PricingContext context = selector.select(input);

        MutableBill bill = new MutableBill();
        chainHead.apply(context, tripFacts, bill);

        return FinalizedBill.builder()
                .planVersionId(context.getPlanVersionId())
                .planName(context.getPlanName())
                .charges(bill.getCharges())
                .total(bill.total())
                .build();
    }
}
