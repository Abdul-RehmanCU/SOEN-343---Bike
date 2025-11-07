package com.qwikride.prc.pricing.domain;

import com.qwikride.prc.domain.MembershipStatus;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class PricingContext {
    UUID planVersionId;
    RateSheet rateSheet;
    MembershipStatus membershipStatus;
    String planName;
}
