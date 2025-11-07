package com.qwikride.prc.controller;

import com.qwikride.prc.dto.PricingPlanResponse;
import com.qwikride.prc.dto.PricingPlanUpsertRequest;
import com.qwikride.prc.service.PricingPlanAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prc/pricing/admin")
@RequiredArgsConstructor
public class PricingPlanAdminController {
    private final PricingPlanAdminService pricingPlanAdminService;

    @GetMapping
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<List<PricingPlanResponse>> listPlans() {
        return ResponseEntity.ok(pricingPlanAdminService.listAll());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<PricingPlanResponse> createPlan(@RequestBody PricingPlanUpsertRequest request) {
        return ResponseEntity.ok(pricingPlanAdminService.create(request));
    }

    @PutMapping("/{planVersionId}")
    @PreAuthorize("hasAuthority('OPERATOR')")
    public ResponseEntity<PricingPlanResponse> updatePlan(@PathVariable UUID planVersionId,
            @RequestBody PricingPlanUpsertRequest request) {
        return ResponseEntity.ok(pricingPlanAdminService.update(planVersionId, request));
    }
}
