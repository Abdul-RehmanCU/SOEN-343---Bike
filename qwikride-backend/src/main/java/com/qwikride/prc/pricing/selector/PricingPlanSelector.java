package com.qwikride.prc.pricing.selector;

import com.qwikride.prc.pricing.domain.PricingContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PricingPlanSelector {
    private final List<PlanSelectionStrategy> strategies;

    public PricingContext select(SelectionInput input) {
        return strategies.stream()
                .map(strategy -> strategy.select(input))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No pricing plan available for provided input"));
    }
}
