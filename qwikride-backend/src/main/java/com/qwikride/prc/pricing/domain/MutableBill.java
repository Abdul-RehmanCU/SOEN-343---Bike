package com.qwikride.prc.pricing.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutableBill {
    private final List<ChargeLine> charges = new ArrayList<>();

    public void add(ChargeLine line) {
        charges.add(line);
    }

    public List<ChargeLine> getCharges() {
        return Collections.unmodifiableList(charges);
    }

    public BigDecimal total() {
        return charges.stream()
                .map(ChargeLine::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
