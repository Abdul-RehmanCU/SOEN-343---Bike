package com.qwikride.prc.pricing.selector;

import com.qwikride.prc.domain.MembershipStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class SelectionInput {
    LocalDateTime tripEndTime;
    MembershipStatus membershipStatus;
    String cityId;
}
