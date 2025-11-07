package com.qwikride.prc.pricing.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class FinalizedBill {
    UUID planVersionId;
    String planName;
    List<ChargeLine> charges;
    BigDecimal total;
}
