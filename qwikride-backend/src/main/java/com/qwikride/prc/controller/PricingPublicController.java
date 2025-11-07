package com.qwikride.prc.controller;

import com.qwikride.prc.dto.PricingPlanResponse;
import com.qwikride.prc.service.PricingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/prc/pricing")
@RequiredArgsConstructor
public class PricingPublicController {
    private final PricingPlanService pricingPlanService;

    @GetMapping("/plans")
    public ResponseEntity<List<PricingPlanResponse>> listPlans() {
        return ResponseEntity.ok(pricingPlanService.listPublishedPlans());
    }
}
