package com.qwikride.prc.pricing.selector;

import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.RateSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PricingPlanSelectorTest {
    @Mock
    private PlanSelectionStrategy membershipStrategy;

    @Mock
    private PlanSelectionStrategy cityStrategy;

    private PricingPlanSelector selector;

    private SelectionInput input;

    @BeforeEach
    void setUp() {
        selector = new PricingPlanSelector(List.of(membershipStrategy, cityStrategy));
        input = SelectionInput.builder()
                .tripEndTime(LocalDateTime.now())
                .build();
    }

    @Test
    void selectReturnsFirstStrategyHit() {
        PricingContext expected = sampleContext("Member Plan");
        when(membershipStrategy.select(input)).thenReturn(Optional.of(expected));

        PricingContext result = selector.select(input);

        assertSame(expected, result);
        verify(membershipStrategy).select(input);
        verifyNoInteractions(cityStrategy);
    }

    @Test
    void selectFallsBackWhenFirstEmpty() {
        PricingContext expected = sampleContext("City Plan");
        when(membershipStrategy.select(input)).thenReturn(Optional.empty());
        when(cityStrategy.select(input)).thenReturn(Optional.of(expected));

        PricingContext result = selector.select(input);

        assertSame(expected, result);
        verify(membershipStrategy).select(input);
        verify(cityStrategy).select(input);
    }

    private PricingContext sampleContext(String planName) {
        return PricingContext.builder()
                .planVersionId(UUID.randomUUID())
                .planName(planName)
                .rateSheet(RateSheet.builder().build())
                .build();
    }
}
