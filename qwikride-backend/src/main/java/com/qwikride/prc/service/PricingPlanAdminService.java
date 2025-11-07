package com.qwikride.prc.service;

import com.qwikride.prc.dto.PricingPlanResponse;
import com.qwikride.prc.dto.PricingPlanUpsertRequest;
import com.qwikride.prc.model.PricingPlanVersion;
import com.qwikride.prc.repository.PricingPlanVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingPlanAdminService {
    private final PricingPlanVersionRepository repository;
    private final PricingPlanService pricingPlanService;

    @Transactional
    public PricingPlanResponse create(PricingPlanUpsertRequest request) {
        PricingPlanVersion version = new PricingPlanVersion();
        version.setId(UUID.randomUUID());
        applyRequest(version, request);
        repository.save(version);
        return pricingPlanService.toResponse(version);
    }

    @Transactional
    public PricingPlanResponse update(UUID planVersionId, PricingPlanUpsertRequest request) {
        PricingPlanVersion version = repository.findById(planVersionId)
                .orElseThrow(() -> new IllegalArgumentException("Pricing plan not found"));
        applyRequest(version, request);
        repository.save(version);
        return pricingPlanService.toResponse(version);
    }

    @Transactional(readOnly = true)
    public List<PricingPlanResponse> listAll() {
        return repository.findAll().stream()
                .sorted((a, b) -> b.getEffectiveFrom().compareTo(a.getEffectiveFrom()))
                .map(pricingPlanService::toResponse)
                .collect(Collectors.toList());
    }

    private void applyRequest(PricingPlanVersion version, PricingPlanUpsertRequest request) {
        version.setPlanName(request.planName());
        version.setBaseFee(request.baseFee());
        version.setPerMinuteRate(request.perMinuteRate());
        version.setEbikeSurcharge(request.ebikeSurcharge());
        version.setMembershipTier(request.membershipTier());
        version.setCityId(request.cityId());
        version.setEffectiveFrom(request.effectiveFrom() == null ? LocalDateTime.now() : request.effectiveFrom());
        version.setEffectiveTo(request.effectiveTo());
        version.setDescription(request.description());
        version.setPublished(request.publish());
    }
}
