package com.qwikride.prc.pricing.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RateSheet {
    BigDecimal baseFee;
    BigDecimal perMinute;
    BigDecimal ebikeSurcharge;
}
