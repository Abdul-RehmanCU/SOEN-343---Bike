package com.qwikride.prc.pricing.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

@Value
@Builder
public class ChargeLine {
    String code;
    BigDecimal amount;
    Map<String, String> meta;

    public Map<String, String> safeMeta() {
        return meta == null ? Collections.emptyMap() : Collections.unmodifiableMap(meta);
    }
}
