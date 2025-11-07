package com.qwikride.prc.dto;

import java.math.BigDecimal;

public record DisputeResolutionRequest(boolean approved,
        BigDecimal adjustmentAmount,
        String resolutionNote) {
}
