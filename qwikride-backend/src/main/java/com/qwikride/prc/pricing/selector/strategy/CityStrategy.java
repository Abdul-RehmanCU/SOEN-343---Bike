package com.qwikride.prc.pricing.selector.strategy;

import com.qwikride.prc.model.PricingPlanVersion;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.selector.PlanSelectionStrategy;
import com.qwikride.prc.pricing.selector.SelectionInput;
import com.qwikride.prc.repository.PricingPlanVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Order(20)
@RequiredArgsConstructor
public class CityStrategy implements PlanSelectionStrategy {
    private final PricingPlanVersionRepository repository;

    @Override
    public Optional<PricingContext> select(SelectionInput input) {
        if (input.getCityId() == null) {
            return Optional.empty();
        }

        return repository.findActivePlansForCity(input.getCityId(), input.getTripEndTime()).stream()
                .findFirst()
                .map(this::toContext);
    }

    private PricingContext toContext(PricingPlanVersion version) {
        return PricingContext.builder()
                .planVersionId(version.getId())
                .rateSheet(version.toRateSheet())
                .membershipStatus(version.getMembershipTier())
                .planName(version.getPlanName())
                .build();
    }
}
