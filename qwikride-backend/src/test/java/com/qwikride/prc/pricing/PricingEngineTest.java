package com.qwikride.prc.pricing;

import com.qwikride.prc.pricing.domain.FinalizedBill;
import com.qwikride.prc.pricing.domain.PricingContext;
import com.qwikride.prc.pricing.domain.RateSheet;
import com.qwikride.prc.pricing.domain.TripFacts;
import com.qwikride.prc.pricing.rule.BaseFeeHandler;
import com.qwikride.prc.pricing.rule.EbikeSurchargeHandler;
import com.qwikride.prc.pricing.rule.PerMinuteHandler;
import com.qwikride.prc.pricing.selector.SelectionInput;
import com.qwikride.prc.pricing.selector.PricingPlanSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PricingEngineTest {

    private BaseFeeHandler baseFeeHandler;
    private PerMinuteHandler perMinuteHandler;
    private EbikeSurchargeHandler ebikeSurchargeHandler;

    @BeforeEach
    void setUp() {
        baseFeeHandler = new BaseFeeHandler();
        perMinuteHandler = new PerMinuteHandler();
        ebikeSurchargeHandler = new EbikeSurchargeHandler();
        baseFeeHandler.setNext(perMinuteHandler).setNext(ebikeSurchargeHandler);
    }

    @Test
    void priceAppliesStrategyAndRuleChain() {
        RateSheet rateSheet = RateSheet.builder()
                .baseFee(new BigDecimal("1.50"))
                .perMinute(new BigDecimal("0.25"))
                .ebikeSurcharge(new BigDecimal("0.80"))
                .build();
        PricingContext context = PricingContext.builder()
                .planVersionId(UUID.randomUUID())
                .planName("Member Plus")
                .rateSheet(rateSheet)
                .build();

        PricingPlanSelector selector = new PricingPlanSelector(List.of(any -> Optional.of(context)));
        PricingEngine pricingEngine = new PricingEngine(selector, baseFeeHandler);

        SelectionInput input = SelectionInput.builder()
                .tripEndTime(LocalDateTime.now())
                .build();
        LocalDateTime start = LocalDateTime.of(2025, 1, 10, 12, 0);
        LocalDateTime end = start.plusMinutes(18);
        TripFacts tripFacts = TripFacts.builder()
                .startTime(start)
                .endTime(end)
                .ebike(true)
                .build();

        FinalizedBill bill = pricingEngine.price(tripFacts, input);

        long minutes = tripFacts.durationMinutes();
        BigDecimal expectedTotal = rateSheet.getBaseFee()
                .add(rateSheet.getPerMinute().multiply(BigDecimal.valueOf(minutes)))
                .add(rateSheet.getEbikeSurcharge());

        assertEquals(0, expectedTotal.compareTo(bill.getTotal()));
        assertEquals("Member Plus", bill.getPlanName());
        assertEquals(3, bill.getCharges().size());
        assertTrue(bill.getCharges().stream().anyMatch(line -> "BASE_FEE".equals(line.getCode())));
        assertTrue(bill.getCharges().stream().anyMatch(line -> "PER_MINUTE".equals(line.getCode())));
        assertTrue(bill.getCharges().stream().anyMatch(line -> "E_BIKE_SURCHARGE".equals(line.getCode())));
    }
}
