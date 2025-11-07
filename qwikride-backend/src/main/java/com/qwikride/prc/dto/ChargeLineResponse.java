package com.qwikride.prc.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ChargeLineResponse(
        String code,
        BigDecimal amount,
        Map<String, String> meta) {
}
