package com.qwikride.prc;

import com.qwikride.prc.pricing.rule.BaseFeeHandler;
import com.qwikride.prc.pricing.rule.CapHandler;
import com.qwikride.prc.pricing.rule.DiscountHandler;
import com.qwikride.prc.pricing.rule.EbikeSurchargeHandler;
import com.qwikride.prc.pricing.rule.PerMinuteHandler;
import com.qwikride.prc.pricing.rule.PricingRuleHandler;
import com.qwikride.prc.pricing.rule.TaxHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrcConfiguration {

    @Bean
    public PricingRuleHandler pricingRuleChain(BaseFeeHandler baseHandler,
            PerMinuteHandler perMinuteHandler,
            EbikeSurchargeHandler ebikeHandler,
            ObjectProvider<DiscountHandler> discountHandler,
            ObjectProvider<CapHandler> capHandler,
            ObjectProvider<TaxHandler> taxHandler) {
        PricingRuleHandler head = baseHandler;
        PricingRuleHandler tail = baseHandler;

        tail = tail.setNext(perMinuteHandler);
        tail = tail.setNext(ebikeHandler);

        DiscountHandler discount = discountHandler.getIfAvailable();
        if (discount != null) {
            tail = tail.setNext(discount);
        }

        CapHandler cap = capHandler.getIfAvailable();
        if (cap != null) {
            tail = tail.setNext(cap);
        }

        TaxHandler tax = taxHandler.getIfAvailable();
        if (tax != null) {
            tail = tail.setNext(tax);
        }

        return head;
    }
}
