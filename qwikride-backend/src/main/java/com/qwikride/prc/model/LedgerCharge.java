package com.qwikride.prc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerCharge {
    @Column(name = "charge_code", nullable = false)
    private String code;

    @Column(name = "charge_amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "charge_meta", length = 512)
    private String meta; // stored as key=value; pairs separated by semicolons

    public static LedgerCharge from(String code, BigDecimal amount, Map<String, String> meta) {
        String serializedMeta = serializeMeta(meta);
        return LedgerCharge.builder()
                .code(code)
                .amount(amount)
                .meta(serializedMeta)
                .build();
    }

    private static String serializeMeta(Map<String, String> meta) {
        if (meta == null || meta.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        meta.forEach((key, value) -> {
            if (builder.length() > 0) {
                builder.append(';');
            }
            builder.append(key).append('=').append(value);
        });
        return builder.toString();
    }

    public Map<String, String> metaAsMap() {
        Map<String, String> map = new HashMap<>();
        if (meta == null || meta.isEmpty()) {
            return map;
        }
        String[] pairs = meta.split(";");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }
}
