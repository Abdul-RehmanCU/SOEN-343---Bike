package com.qwikride.prc.pricing.selector;

import com.qwikride.prc.pricing.domain.PricingContext;

import java.util.Optional;

public interface PlanSelectionStrategy {
    Optional<PricingContext> select(SelectionInput input);
}
