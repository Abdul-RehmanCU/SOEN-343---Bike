package com.qwikride.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StationStatusDTO {
    @NotBlank
    private String status; // expected values: ACTIVE or OUT_OF_SERVICE
}
