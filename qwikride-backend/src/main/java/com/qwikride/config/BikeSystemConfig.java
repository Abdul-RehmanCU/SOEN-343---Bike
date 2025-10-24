package com.qwikride.config;

import com.qwikride.adapter.BikeLocationPort;
import com.qwikride.adapter.FakeBikeSystemAdapter;
import com.qwikride.event.EventBus;
import com.qwikride.service.DashboardNotifier;
import com.qwikride.service.HistoryService;
import com.qwikride.service.PricingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BikeSystemConfig {

    @Bean
    public BikeLocationPort bikeLocationPort(FakeBikeSystemAdapter fakeAdapter) {
        // Use fake adapter for development/testing
        return fakeAdapter;
    }

    @Bean
    public String registerEventSubscribers(EventBus eventBus,
                                           PricingService pricingService,
                                           HistoryService historyService,
                                           DashboardNotifier dashboardNotifier) {
        eventBus.subscribe(pricingService);
        eventBus.subscribe(historyService);
        eventBus.subscribe(dashboardNotifier);
        return "Event subscribers registered";
    }
}
